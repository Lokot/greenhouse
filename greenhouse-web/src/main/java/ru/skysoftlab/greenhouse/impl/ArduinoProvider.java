package ru.skysoftlab.greenhouse.impl;

import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.HIGH;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.INPUT;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.LOW;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.OUTPUT;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.closeSignal;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.dhtPin;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.illumPin;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.openSignal;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.state30;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.state60;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.stateClose;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.stateOpen;
import static ru.skysoftlab.greenhouse.arduino.ArduinoPins.stopSignal;
import static ru.skysoftlab.greenhouse.common.GableState.Close;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.SERIAL_PORT;
import static ru.skysoftlab.greenhouse.impl.GrenHouseArduino.LOCK;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.sintef.jarduino.DigitalPin;
import org.sintef.jarduino.DigitalState;
import org.sintef.jarduino.InvalidPinTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.greenhouse.arduino.Dht22Params;
import ru.skysoftlab.greenhouse.arduino.IArduino;
import ru.skysoftlab.greenhouse.arduino.Sensor;
import ru.skysoftlab.greenhouse.common.GableMoveEvent;
import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.common.GableStateListener;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.events.ConfigurationListener;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@Singleton
// @Startup
public class ArduinoProvider implements IArduino, ConfigurationListener, GableStateListener {

	private static final long serialVersionUID = -3174953105876049988L;

	private Logger LOG = LoggerFactory.getLogger(ArduinoProvider.class);

	@Inject
	@AppProperty(SERIAL_PORT)
	private String portName;

	@Inject
	private javax.enterprise.event.Event<GableMoveEvent> gableMoveEvent;

	private GableState gbState;
	private GableState newGbState;

	private GrenHouseArduino arduino;

	@PostConstruct
	private void init() {
		try {
			arduino = new GrenHouseArduino(portName, this);
			arduino.pinMode(illumPin, INPUT);
			arduino.pinMode(stateOpen, INPUT);
			arduino.pinMode(state60, INPUT);
			arduino.pinMode(state30, INPUT);
			arduino.pinMode(stateClose, INPUT);
			arduino.pinMode(openSignal, OUTPUT);
			arduino.pinMode(stopSignal, OUTPUT);
			arduino.pinMode(closeSignal, OUTPUT);
		} catch (Exception e) {
			try {
				Notification.show("Контроллер не найден на порту " + portName, Type.ERROR_MESSAGE);
			} catch (NullPointerException e1) {
			}
			LOG.error("Контроллер не найден на порту " + portName);
			if (arduino != null) {
				arduino.close();
			}
			arduino = null;
		}
	}

	@PreDestroy
	private void deInit() {
		arduino.close();
		arduino = null;
	}

	/**
	 * Округлить.
	 * 
	 * @param number
	 * @param scale
	 * @return
	 */
	private float round(float number, int scale) {
		int pow = 10;
		for (int i = 1; i < scale; i++)
			pow *= 10;
		float tmp = number * pow;
		return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.skylibs.events.ConfigurationListener#editIntervalEvent(
	 * ru.skysoftlab.skylibs.events.SystemConfigEvent)
	 */
	@Override
	public void editIntervalEvent(@Observes SystemConfigEvent event) {
		String newSerialPortName = event.getParam(SERIAL_PORT);
		if (newSerialPortName != null && newSerialPortName.length() > 0 && !newSerialPortName.equals(portName)) {
			portName = newSerialPortName;
			if (arduino != null) {
				arduino.close();
			}
			init();
		}
	}

	@Override
	public int getIllumination() {
		synchronized (LOCK) {
			if (arduino == null)
				return -1;
			try {
				return arduino.analogRead(illumPin);
			} catch (InvalidPinTypeException e) {
				LOG.error(e.getMessage());
			}
			return -1;
		}
	}

	@Override
	public Float getTemperature() {
		synchronized (LOCK) {
			if (arduino == null)
				return -1f;
			String value = arduino.sensorRead(dhtPin, Sensor.DHT22, Dht22Params.TEMP, 5000);
			try {
				return round(Float.parseFloat(value), 1);
			} catch (Exception e) {
				return -1f;
			}
		}
	}

	@Override
	public Float getHumidity() {
		synchronized (LOCK) {
			if (arduino == null)
				return -1f;
			String value = arduino.sensorRead(dhtPin, Sensor.DHT22, Dht22Params.HUM, 5000);
			try {
				return round(Float.parseFloat(value), 1);
			} catch (Exception e) {
				return -1f;
			}
		}
	}

	// @Asynchronous
	@Override
	@Produces
	public GableState getGableState() {
		if (gbState == null) {
			// поиск конька
			GableState gableNow = findGable();
			if (gableNow != null) {
				gbState = gableNow;
			} else {
				gbState = Close;
				gableCalibrate();
			}
		}
		return gbState;
	}

	private GableState findGable() {
		synchronized (LOCK) {
			if (arduino != null) {
				DigitalState closeSignalState = arduino.digitalRead(stateClose);
				if (closeSignalState != null && closeSignalState.equals(HIGH)) {
					return GableState.Close;
				}
				DigitalState degrees30SignalState = arduino.digitalRead(state30);
				if (degrees30SignalState != null && degrees30SignalState.equals(HIGH)) {
					return GableState.Degrees30;
				}
				DigitalState degrees60SignalState = arduino.digitalRead(state60);
				if (degrees60SignalState != null && degrees60SignalState.equals(HIGH)) {
					return GableState.Degrees60;
				}
				DigitalState openSignalState = arduino.digitalRead(stateOpen);
				if (openSignalState != null && openSignalState.equals(HIGH)) {
					return GableState.Open;
				}
			}
			return null;
		}
	}

	private boolean isCalibrateMode = false;
	private final Object calibrateLocker = new Object();

	private void gableCalibrate() {
		synchronized (calibrateLocker) {
			isCalibrateMode = true;
			sendCloseSignal();
		}
	}

	@Override
	public void setGableState(GableState newGableState) {
		newGbState = newGableState;
		int compare = newGableState.compareTo(getGableState());
		if (compare > 0) {
			sendOpenSignal();
		} else if (compare < 0) {
			sendCloseSignal();
		} else {
			// текущее состояние соответствует
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.GableStateListener#gableStateIs(ru.
	 * skysoftlab .greenhouse.common.GableState)
	 */
	@Override
	public void gableStateIs(GableState gableState) {
		synchronized (calibrateLocker) {
			gbState = gableState;
			if (newGbState != null && newGbState.equals(gableState)) {
				sendStopSignal();
				isCalibrateMode = false;
				newGbState = null;
			}
			if (isCalibrateMode) {
				sendStopSignal();
				isCalibrateMode = false;
			}
		}
		gableMoveEvent.fire(new GableMoveEvent(gableState));
	}

	private void sendStopSignal() {
		if (arduino == null)
			return;
		arduino.digitalWrite(stopSignal, LOW);
		arduino.delay(1000);
		arduino.digitalWrite(stopSignal, HIGH);
		arduino.delay(1000);
	}

	private void sendOpenSignal() {
		if (arduino == null)
			return;
		sendStopSignal();
		arduino.digitalWrite(openSignal, LOW);
		arduino.delay(1000);
		arduino.digitalWrite(openSignal, HIGH);
	}

	private void sendCloseSignal() {
		if (arduino == null)
			return;
		sendStopSignal();
		arduino.digitalWrite(closeSignal, LOW);
		arduino.delay(1000);
		arduino.digitalWrite(closeSignal, HIGH);
	}

	@Override
	public void openIrrigationCountur(DigitalPin pin) {
		arduino.digitalWrite(pin, HIGH);
	}

	@Override
	public void closeIrrigationCountur(DigitalPin pin) {
		arduino.digitalWrite(pin, LOW);
	}

	@Override
	public boolean isConnected() {
		return !(arduino == null);
	}

}
