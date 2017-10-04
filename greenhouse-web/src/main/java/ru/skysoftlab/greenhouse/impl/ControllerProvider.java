package ru.skysoftlab.greenhouse.impl;

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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.gpio.GpioException;
import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.gpio.cdi.DeviceConnectedEvent;
import ru.skysoftlab.gpio.sensors.Dht22Params;
import ru.skysoftlab.gpio.sensors.Sensor;
import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.greenhouse.common.IGableGpioDevice;
import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;
import ru.skysoftlab.skylibs.events.EntityChangeEvent;
import ru.skysoftlab.skylibs.events.EntityChangeListener;

@Singleton
public class ControllerProvider extends PinsBean implements IController, EntityChangeListener {

	private static final long serialVersionUID = -3174953105876049988L;

	private Logger LOG = LoggerFactory.getLogger(ControllerProvider.class);

	@Inject
	private IGableGpioDevice gpioDevice;

	@Inject
	private GbStater gbStater;

	@Inject
	private DataBaseProvider baseProvider;

	@PostConstruct
	public void init() {
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
		try {
			return gpioDevice.analogRead(getAnalogPin(illumPin));
		} catch (GpioException e) {
			LOG.error("Error read illumination ", e);
			return -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#getTemperature()
	 */
	@Override
	public Float getTemperature() {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.arduino.IArduino#getHumidity()
	 */
	@Override
	public Float getHumidity() {
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

	// @Asynchronous
	@Produces
	public GableState getGableState() {
		return gbStater.getGableState();
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

	@Override
	public boolean isGableMoved() {
		return gbStater.isGableMoved();
	}

	@Override
	public void setGableState(GableState gableState) {
		gbStater.setGableState(gableState);		
	}

}
