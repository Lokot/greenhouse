package ru.skysoftlab.greenhouse.arduino;

import static ru.skysoftlab.greenhouse.common.ConfigurationNames.SERIAL_PORT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.sintef.jarduino.DigitalPin;
import org.sintef.jarduino.DigitalState;
import org.sintef.jarduino.InvalidPinTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import ru.skysoftlab.gpio.GpioException;
import ru.skysoftlab.gpio.IAnalogPin;
import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.gpio.IPin;
import ru.skysoftlab.gpio.ISensor;
import ru.skysoftlab.gpio.ISensorParam;
import ru.skysoftlab.gpio.PinMode;
import ru.skysoftlab.greenhouse.arduino.converter.ArduinoConverter;
import ru.skysoftlab.greenhouse.common.GableStateListener;
import ru.skysoftlab.greenhouse.common.IGableGpioDevice;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.events.ConfigurationListener;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;

public class ArduinoDevice implements IGableGpioDevice, ConfigurationListener {

	private Logger LOG = LoggerFactory.getLogger(ArduinoDevice.class);

	private final DigitalState LOW = DigitalState.LOW, HIGH = DigitalState.HIGH;

	@Inject
	@AppProperty(SERIAL_PORT)
	private String portName;

	private ArduinoConverter converter = new ArduinoConverter();

	private GrenHouseArduino arduino;

	@PostConstruct
	private void init() {
		try {
			arduino = new GrenHouseArduino(portName);
		} catch (Exception e) {
			LOG.error("Контроллер не найден на порту " + portName);
			if (arduino != null) {
				arduino.close();
			}
			arduino = null;
		}
	}

	@Override
	public void close() throws IOException {
		if (arduino == null)
			return;
		arduino.close();
		arduino = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.skylibs.events.ConfigurationListener#editIntervalEvent(
	 * ru.skysoftlab.skylibs.events.SystemConfigEvent)
	 */
	@Override
	public void configUpdated(@Observes SystemConfigEvent event) {
		String newSerialPortName = event.getParam(SERIAL_PORT);
		if (newSerialPortName != null && newSerialPortName.length() > 0 && !newSerialPortName.equals(portName)) {
			portName = newSerialPortName;
			if (arduino != null) {
				arduino.close();
			}
			init();
			// TODO возможно надо кидать event об переустановке PinMode
		}
	}

	@Override
	public void setPinMode(IPin pin, PinMode pinMode) throws GpioException {
		if (arduino == null)
			return;
		try {
			arduino.pinMode(converter.convertPin(pin), converter.convertPinMode(pinMode));
		} catch (InvalidPinTypeException e) {
			throw new GpioException("Error set " + pin + " in mode " + pinMode, e);
		}
	}

	@Override
	public short analogRead(IAnalogPin pin) throws GpioException {
		if (arduino == null)
			return -1;
		try {
			return arduino.analogRead(converter.convertPin(pin));
		} catch (InvalidPinTypeException e) {
			throw new GpioException("Error read anolog pin: " + pin, e);
		}
	}

	@Override
	public String sensorRead(IDigitalPin pin, ISensor sensor, ISensorParam sParam, long delay) throws GpioException {
		if (arduino == null)
			return "";
		try {
			return arduino.sensorRead(converter.convertPin(pin), converter.convertSensor(sensor),
					converter.convertSensorParametr(sensor, sParam), delay);
		} catch (InvalidPinTypeException e) {
			throw new GpioException("Error read sensor: " + pin, e);
		}
	}

	@Override
	public Boolean digitalRead(IDigitalPin pin) throws GpioException {
		if (arduino == null)
			return false;
		try {
			return arduino.digitalRead(converter.convertPin(pin)).getValue() > 0;
		} catch (InvalidPinTypeException e) {
			throw new GpioException("Error read digital pin: " + pin, e);
		}
	}

	@Override
	public void digitalWrite(IDigitalPin pin, Boolean state) throws GpioException {
		if (arduino == null)
			return;
		try {
			arduino.digitalWrite(converter.convertPin(pin), state ? HIGH : LOW);
		} catch (InvalidPinTypeException e) {
			throw new GpioException("Error write to digital pin: " + pin, e);
		}
	}

	@Override
	public void delay(long miliseconds) {
		if (arduino == null)
			return;
		arduino.delay(miliseconds);
	}

	@Override
	public Collection<IDigitalPin> getAvalibleDigitalPins() {
		Collection<IDigitalPin> rv = new ArrayList<>();
		for (DigitalPin pin : DigitalPin.values()) {
			rv.add(converter.convertToDigitalPin(pin));
		}
		return rv;
	}

	@Override
	public boolean isConnected() {
		return arduino != null;
	}

	@Override
	public Collection<String> getCommPorts() {
		Collection<String> rv = new ArrayList<>();
		for (SerialPort port : SerialPort.getCommPorts()) {
			rv.add(port.getSystemPortName());
		}
		return rv;
	}

	@Override
	public void setGableStateListener(GableStateListener listener) {
		if (arduino == null)
			return;
		arduino.setGableStateListener(listener);
	}

}
