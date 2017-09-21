package ru.skysoftlab.greenhouse.impl;

import static ru.skysoftlab.greenhouse.common.GableState.Close;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.INPUT;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.OUTPUT;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.closeSignal;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.dhtPin;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.illumPin;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.openSignal;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.state30;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.state60;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.stateClose;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.stateOpen;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.stopSignal;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.gpio.GpioException;
import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.greenhouse.common.GableMoveEvent;
import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.common.GableStateListener;
import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.greenhouse.common.IGableGpioDevice;
import ru.skysoftlab.greenhouse.gpio.Dht22Params;
import ru.skysoftlab.greenhouse.gpio.Sensor;

@Singleton
// @Startup
public class ControllerProvider implements IController, GableStateListener {

	private static final long serialVersionUID = -3174953105876049988L;

	private Logger LOG = LoggerFactory.getLogger(ControllerProvider.class);

	public static final Object LOCK = new Object();

	@Inject
	private javax.enterprise.event.Event<GableMoveEvent> gableMoveEvent;

	private GableState gbState;
	private GableState newGbState;

	@Inject
	private IGableGpioDevice gpioDevice;

	@PostConstruct
	private void init() {
		gpioDevice.setGableStateListener(this);
		try {
			gpioDevice.setPinMode(illumPin, INPUT);
			gpioDevice.setPinMode(stateOpen, INPUT);
			gpioDevice.setPinMode(state60, INPUT);
			gpioDevice.setPinMode(state30, INPUT);
			gpioDevice.setPinMode(stateClose, INPUT);
			gpioDevice.setPinMode(openSignal, OUTPUT);
			gpioDevice.setPinMode(stopSignal, OUTPUT);
			gpioDevice.setPinMode(closeSignal, OUTPUT);
			// TODO инициализация пинов для полива
		} catch (GpioException e) {
			LOG.error("Error set pin mode ", e);
		} catch (Exception e) {
			LOG.error("Controller ERROR !!!!!", e);
			closeDevice();
		}
	}

	@PreDestroy
	private void deInit() {
		closeDevice();
	}

	private void closeDevice() {
		try {
			gpioDevice.close();
		} catch (IOException e) {
			LOG.error("GpioDevice close error ", e);
		}
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
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#getIllumination()
	 */
	@Override
	public int getIllumination() {
		synchronized (LOCK) {
			try {
				return gpioDevice.analogRead(illumPin);
			} catch (GpioException e) {
				LOG.error("Error read illumination ", e);
				return -1;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#getTemperature()
	 */
	@Override
	public Float getTemperature() {
		synchronized (LOCK) {
			String value;
			try {
				value = gpioDevice.sensorRead(dhtPin, Sensor.DHT22, Dht22Params.TEMP, 5000);
				try {
					return round(Float.parseFloat(value), 1);
				} catch (Exception e) {
					return -1f;
				}
			} catch (GpioException e1) {
				LOG.error("Error read temperature ", e1);
				return -1f;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#getHumidity()
	 */
	@Override
	public Float getHumidity() {
		synchronized (LOCK) {
			String value;
			try {
				value = gpioDevice.sensorRead(dhtPin, Sensor.DHT22, Dht22Params.HUM, 5000);
				try {
					return round(Float.parseFloat(value), 1);
				} catch (Exception e) {
					return -1f;
				}
			} catch (GpioException e1) {
				LOG.error("Error read humidity ", e1);
				return -1f;
			}
		}
	}

	// @Asynchronous
	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#getGableState()
	 */
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
			try {
				if (gpioDevice.digitalRead(stateClose)) {
					return GableState.Close;
				}
				if (gpioDevice.digitalRead(state30)) {
					return GableState.Degrees30;
				}
				if (gpioDevice.digitalRead(state60)) {
					return GableState.Degrees60;
				}
				if (gpioDevice.digitalRead(stateOpen)) {
					return GableState.Open;
				}
			} catch (GpioException e) {
				LOG.error("Error findGable ", e);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#setGableState(ru.skysoftlab.
	 * greenhouse.common.GableState)
	 */
	@Override
	public void setGableState(GableState newGableState) {
		newGbState = newGableState;
		int compare = newGableState.compareTo(getGableState());
		if (compare > 0) {
			sendOpenSignal();
			LOG.info("Gable opens to " + newGableState.getStringState());
		} else if (compare < 0) {
			sendCloseSignal();
			LOG.info("Gable close to " + newGableState.getStringState());
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
		try {
			gpioDevice.digitalWrite(stopSignal, false);
			gpioDevice.delay(1000);
			gpioDevice.digitalWrite(stopSignal, true);
			gpioDevice.delay(1000);
		} catch (GpioException e) {
			LOG.error("Error sendStopSignal ", e);
		}
	}

	private void sendOpenSignal() {
		sendStopSignal();
		try {
			gpioDevice.digitalWrite(openSignal, false);
			gpioDevice.delay(1000);
			gpioDevice.digitalWrite(openSignal, true);
		} catch (GpioException e) {
			LOG.error("Error sendOpenSignal ", e);
		}
	}

	private void sendCloseSignal() {
		sendStopSignal();
		try {
			gpioDevice.digitalWrite(closeSignal, false);
			gpioDevice.delay(1000);
			gpioDevice.digitalWrite(closeSignal, true);
		} catch (GpioException e) {
			LOG.error("Error sendCloseSignal ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.skysoftlab.greenhouse.arduino.IArduino#openIrrigationCountur(org.sintef.
	 * jarduino.DigitalPin)
	 */
	@Override
	public void openIrrigationCountur(IDigitalPin pin) {
		try {
			gpioDevice.digitalWrite(pin, true);
		} catch (GpioException e) {
			LOG.error("Error openIrrigationCountur ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.skysoftlab.greenhouse.arduino.IArduino#closeIrrigationCountur(org.sintef.
	 * jarduino.DigitalPin)
	 */
	@Override
	public void closeIrrigationCountur(IDigitalPin pin) {
		try {
			gpioDevice.digitalWrite(pin, false);
		} catch (GpioException e) {
			LOG.error("Error closeIrrigationCountur ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return gpioDevice.isConnected();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#isGableMoved()
	 */
	@Override
	public boolean isGableMoved() {
		if (gbState != null) {
			return !gbState.equals(newGbState) || isCalibrateMode;
		}
		return true;
	}

	@Override
	public Collection<IDigitalPin> getAvalibleDigitalPins() {
		return gpioDevice.getAvalibleDigitalPins();
	}

	@Override
	public Collection<String> getCommPorts() {
		return gpioDevice.getCommPorts();
	}

}
