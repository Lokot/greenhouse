package ru.skysoftlab.greenhouse.arduino.converter;

import org.sintef.jarduino.JArduinoSensor;
import org.sintef.jarduino.JArduinoSensorParametr;
import org.sintef.jarduino.Pin;

import ru.skysoftlab.gpio.DigitalPin;
import ru.skysoftlab.gpio.IConverter;
import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.gpio.IPin;
import ru.skysoftlab.gpio.ISensor;
import ru.skysoftlab.gpio.ISensorParam;
import ru.skysoftlab.gpio.PinMode;
import ru.skysoftlab.greenhouse.arduino.JSensor;

public class ArduinoConverter implements
		IConverter<Pin, org.sintef.jarduino.PinMode, org.sintef.jarduino.DigitalPin, JArduinoSensor, JArduinoSensorParametr> {

	@Override
	public Pin convertPin(IPin pin) {
		return Pin.valueOf(pin.getName());
	}

	@Override
	public org.sintef.jarduino.PinMode convertPinMode(PinMode pinMode) {
		return org.sintef.jarduino.PinMode.valueOf(pinMode.name());
	}

	@Override
	public IDigitalPin convertToDigitalPin(org.sintef.jarduino.DigitalPin digitalPin) {
		return new DigitalPin(digitalPin.name());
	}

	@Override
	public JArduinoSensor convertSensor(ISensor sensor) {
		return JSensor.map.get(sensor.getValue());
	}

	@Override
	public JArduinoSensorParametr convertSensorParametr(ISensor sensor, ISensorParam param) {
		JArduinoSensor s = JSensor.map.get(sensor.getValue());
		for (JArduinoSensorParametr p : s.getParams()) {
			if(p.getValue() == param.getValue()) {
				return p;
			}
		}
		return null;
	}

}
