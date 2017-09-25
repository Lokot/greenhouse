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
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.gpio.AnalogPin;
import ru.skysoftlab.gpio.DigitalPin;
import ru.skysoftlab.gpio.GpioException;
import ru.skysoftlab.gpio.IAnalogPin;
import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.gpio.cdi.DeviceConnectedEvent;
import ru.skysoftlab.gpio.sensors.Dht22Params;
import ru.skysoftlab.gpio.sensors.Sensor;
import ru.skysoftlab.greenhouse.common.GableMoveEvent;
import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.common.GableStateListener;
import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.greenhouse.common.IGableGpioDevice;
import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;
import ru.skysoftlab.skylibs.annatations.AppPropertyFile;
import ru.skysoftlab.skylibs.events.EntityChangeEvent;
import ru.skysoftlab.skylibs.events.EntityChangeListener;

@Singleton
public class ControllerProvider implements IController, GableStateListener, EntityChangeListener {

	private static final long serialVersionUID = -3174953105876049988L;

	private Logger LOG = LoggerFactory.getLogger(ControllerProvider.class);

	public static final Object LOCK = new Object();

	@Inject
	private javax.enterprise.event.Event<GableMoveEvent> gableMoveEvent;

	private GableState gbState;
	private GableState newGbState;

	@Inject
	private IGableGpioDevice gpioDevice;

	@Inject
	@AppPropertyFile("pins.properties")
	private Properties pins = new Properties();

	@Inject
	private DataBaseProvider baseProvider;

	@PostConstruct
	public void init() {
		gpioDevice.setGableStateListener(this);
		try {
			initStaticPins();
			initDynamicPins();
		} catch (Exception e) {
			LOG.error("Controller ERROR !!!!!", e);
			closeDevice();
		}
	}
	
	@Override
	public void deviceConnectedEvent(@Observes DeviceConnectedEvent event) {
		init();
	}

	private void initStaticPins() {
		try {
			// TODO есть непонятный косяк с зависанием при отсылке данных в serialPort
			gpioDevice.setPinMode(getAnalogPin(illumPin), INPUT);
			gpioDevice.setPinMode(getDigitalPin(stateOpen), INPUT);
			gpioDevice.setPinMode(getDigitalPin(state60), INPUT);
			gpioDevice.setPinMode(getDigitalPin(state30), INPUT);
			gpioDevice.setPinMode(getDigitalPin(stateClose), INPUT);
			gpioDevice.setPinMode(getDigitalPin(openSignal), OUTPUT);
			gpioDevice.setPinMode(getDigitalPin(stopSignal), OUTPUT);
			gpioDevice.setPinMode(getDigitalPin(closeSignal), OUTPUT);
		} catch (GpioException e) {
			LOG.error("Error set pin mode ", e);
		}
	}

	/**
	 * Инициализирует пинов для полива.
	 */
	private void initDynamicPins() {
		List<IDigitalPin> irrPins = baseProvider.getIrrigationPins();
		for (IDigitalPin iDigitalPin : irrPins) {
			try {
				gpioDevice.setPinMode(iDigitalPin, OUTPUT);
			} catch (GpioException e) {
				LOG.error("Error set pin (" + iDigitalPin.getName() + ") mode", e);
			}
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
				return gpioDevice.analogRead(getAnalogPin(illumPin));
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
				value = gpioDevice.sensorRead(getDigitalPin(dhtPin), Sensor.DHT22, Dht22Params.TEMP, 5000);
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
				value = gpioDevice.sensorRead(getDigitalPin(dhtPin), Sensor.DHT22, Dht22Params.HUM, 5000);
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
				if (gpioDevice.digitalRead(getDigitalPin(stateClose))) {
					return GableState.Close;
				}
				if (gpioDevice.digitalRead(getDigitalPin(state30))) {
					return GableState.Degrees30;
				}
				if (gpioDevice.digitalRead(getDigitalPin(state60))) {
					return GableState.Degrees60;
				}
				if (gpioDevice.digitalRead(getDigitalPin(stateOpen))) {
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
		int compare = newGableState.compareTo(getGableState());
		if (compare == 0) {
			// соответствует текущему состоянию
			newGbState = null;
		} else {
			newGbState = newGableState;
			if (compare > 0) {
				sendOpenSignal();
				LOG.info("Gable opens to " + newGableState.getStringState());
			} else if (compare < 0) {
				sendCloseSignal();
				LOG.info("Gable close to " + newGableState.getStringState());
			}
		}
	}

	// TODO надо засинхронизировать функции setGableState и gableStateIs

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
		// fire event to ru.skysoftlab.greenhouse.ui.components.GableStateSelector
		gableMoveEvent.fire(new GableMoveEvent(gableState));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#isGableMoved()
	 */
	@Override
	public boolean isGableMoved() {
		if (gbState != null) {
			if (newGbState == null)
				return isCalibrateMode;
			return !gbState.equals(newGbState) || isCalibrateMode;
		}
		return true;
	}

	private void sendStopSignal() {
		try {
			gpioDevice.digitalWrite(getDigitalPin(stopSignal), false);
			gpioDevice.delay(1000);
			gpioDevice.digitalWrite(getDigitalPin(stopSignal), true);
			gpioDevice.delay(1000);
		} catch (GpioException e) {
			LOG.error("Error sendStopSignal ", e);
		}
	}

	private void sendOpenSignal() {
		sendStopSignal();
		try {
			gpioDevice.digitalWrite(getDigitalPin(openSignal), false);
			gpioDevice.delay(1000);
			gpioDevice.digitalWrite(getDigitalPin(openSignal), true);
		} catch (GpioException e) {
			LOG.error("Error sendOpenSignal ", e);
		}
	}

	private void sendCloseSignal() {
		sendStopSignal();
		try {
			gpioDevice.digitalWrite(getDigitalPin(closeSignal), false);
			gpioDevice.delay(1000);
			gpioDevice.digitalWrite(getDigitalPin(closeSignal), true);
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
	 * @see ru.skysoftlab.gpio.IGpioDevicePinsPorts#getAvalibleDigitalPins()
	 */
	@Override
	public Collection<IDigitalPin> getAvalibleDigitalPins() {
		return gpioDevice.getAvalibleDigitalPins();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.gpio.IGpioDevicePinsPorts#getCommPorts()
	 */
	@Override
	public Collection<String> getCommPorts() {
		return gpioDevice.getCommPorts();
	}

	private IDigitalPin getDigitalPin(String pName) {
		return new DigitalPin(pins.getProperty(pName));
	}

	private IAnalogPin getAnalogPin(String pName) {
		return new AnalogPin(pins.getProperty(pName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.skysoftlab.skylibs.events.EntityChangeListener#entityChange(ru.skysoftlab.
	 * skylibs.events.EntityChangeEvent)
	 */
	@Override
	public void entityChange(@Observes EntityChangeEvent event) {
		if (event.getEntityClass().equals(IrrigationCountur.class)) {
			switch (event.getState()) {
			case NEW:
			case UPDATE:
				IrrigationCountur countur = baseProvider.getIrrigationCountur(event.getId());
				try {
					gpioDevice.setPinMode(countur.getPin(), OUTPUT);
				} catch (GpioException e) {
					LOG.error("Error set pin (" + countur.getPin().getName() + ") mode ", e);
				}
				break;

			default:
				break;
			}
		}
	}

}
