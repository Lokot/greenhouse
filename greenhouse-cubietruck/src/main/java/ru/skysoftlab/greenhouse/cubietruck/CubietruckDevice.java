package ru.skysoftlab.greenhouse.cubietruck;

import static ru.skysoftlab.greenhouse.common.ConfigurationNames.SERIAL_PORT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.silverspoon.bulldog.core.pin.Pin;
import ru.skysoftlab.bulldog.cubietruck.Cubietruck;
import ru.skysoftlab.gpio.GpioException;
import ru.skysoftlab.gpio.IAnalogPin;
import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.gpio.IPin;
import ru.skysoftlab.gpio.ISensor;
import ru.skysoftlab.gpio.ISensorParam;
import ru.skysoftlab.gpio.PinMode;
import ru.skysoftlab.gpio.cdi.AbstractGpioDevice;
import ru.skysoftlab.greenhouse.common.GableStateListener;
import ru.skysoftlab.greenhouse.common.IGableGpioDevice;
import ru.skysoftlab.greenhouse.cubietruck.converter.CubietruckConverter;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.events.ConfigurationListener;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;

//TODO Реализовать
public class CubietruckDevice extends AbstractGpioDevice implements IGableGpioDevice, ConfigurationListener {

	private Logger LOG = LoggerFactory.getLogger(CubietruckDevice.class);

	@Inject
	@AppProperty(SERIAL_PORT)
	// В данном случае это может быть порс с OneWire-контроллером
	private String portName;
	
	private Cubietruck cubietruck = Cubietruck.getInstance();

	private CubietruckConverter converter = new CubietruckConverter();

	@PostConstruct
	private void init() {
		
	}

	@Override
	public void setPinMode(IPin pin, PinMode pinMode) throws GpioException {
	}

	@Override
	public short analogRead(IAnalogPin pin) throws GpioException {
		return 0;
	}

	@Override
	public String sensorRead(IDigitalPin pin, ISensor sensor, ISensorParam sParam, long delay) throws GpioException {
		return null;
	}

	@Override
	public Boolean digitalRead(IDigitalPin pin) throws GpioException {
		return null;
	}

	@Override
	public void digitalWrite(IDigitalPin pin, Boolean state) throws GpioException {
	}

	@Override
	public void delay(long miliseconds) {
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public Collection<IDigitalPin> getAvalibleDigitalPins() {
		Collection<IDigitalPin> rv = new ArrayList<>();
		for (Pin pin : cubietruck.getPins()) {
			rv.add(converter.convertToDigitalPin(pin));
		}
		return rv;
	}

	@Override
	public Collection<String> getCommPorts() {
		return null;
	}

	@Override
	public void close() throws IOException {
		cubietruck.shutdown();
	}

	@Override
	public void configUpdated(SystemConfigEvent event) {
	}

	@Override
	public void setGableStateListener(GableStateListener listener) {
	}

}
