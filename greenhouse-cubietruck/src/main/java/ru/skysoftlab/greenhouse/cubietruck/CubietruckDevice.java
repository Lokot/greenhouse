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
import ru.skysoftlab.greenhouse.common.GableStateListener;
import ru.skysoftlab.greenhouse.common.IGableGpioDevice;
import ru.skysoftlab.greenhouse.cubietruck.converter.CubietruckConverter;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.events.ConfigurationListener;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;

public class CubietruckDevice implements IGableGpioDevice, ConfigurationListener {

	private Logger LOG = LoggerFactory.getLogger(CubietruckDevice.class);

	@Inject
	@AppProperty(SERIAL_PORT)
	// В данном случае это может быть порс с OneWire-контроллером
	private String portName;
	
	private Cubietruck cubietruck = Cubietruck.getInstance();

	private CubietruckConverter converter = new CubietruckConverter();

	@PostConstruct
	private void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPinMode(IPin pin, PinMode pinMode) throws GpioException {
		// TODO Auto-generated method stub
	}

	@Override
	public short analogRead(IAnalogPin pin) throws GpioException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String sensorRead(IDigitalPin pin, ISensor sensor, ISensorParam sParam, long delay) throws GpioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean digitalRead(IDigitalPin pin) throws GpioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void digitalWrite(IDigitalPin pin, Boolean state) throws GpioException {
		// TODO Auto-generated method stub
	}

	@Override
	public void delay(long miliseconds) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		cubietruck.shutdown();
	}

	@Override
	public void configUpdated(SystemConfigEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setGableStateListener(GableStateListener listener) {
		// TODO Auto-generated method stub
	}

}
