package ru.skysoftlab.greenhouse.cubietruck.converter;

import io.silverspoon.bulldog.core.pin.Pin;
import ru.skysoftlab.gpio.DigitalPin;
import ru.skysoftlab.gpio.IConverter;
import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.gpio.IPin;
import ru.skysoftlab.gpio.ISensor;
import ru.skysoftlab.gpio.ISensorParam;
import ru.skysoftlab.gpio.PinMode;

public class CubietruckConverter implements IConverter<Pin, Object, Pin, Object, Object> {

	@Override
	public Pin convertPin(IPin p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object convertPinMode(PinMode p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDigitalPin convertToDigitalPin(Pin digitalPin) {
		return new DigitalPin(digitalPin.getName());
	}

	@Override
	public Object convertSensor(ISensor sensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object convertSensorParametr(ISensor sensor, ISensorParam param) {
		// TODO Auto-generated method stub
		return null;
	}

}
