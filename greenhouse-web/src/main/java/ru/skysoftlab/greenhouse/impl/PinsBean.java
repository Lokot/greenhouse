package ru.skysoftlab.greenhouse.impl;

import java.util.Properties;

import javax.inject.Inject;

import ru.skysoftlab.gpio.AnalogPin;
import ru.skysoftlab.gpio.DigitalPin;
import ru.skysoftlab.gpio.IAnalogPin;
import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.skylibs.annatations.AppPropertyFile;

public class PinsBean {

	@Inject
	@AppPropertyFile("pins.properties")
	private Properties pins = new Properties();
	
	public IDigitalPin getDigitalPin(String pName) {
		return new DigitalPin(pins.getProperty(pName));
	}

	public IAnalogPin getAnalogPin(String pName) {
		return new AnalogPin(pins.getProperty(pName));
	}
}
