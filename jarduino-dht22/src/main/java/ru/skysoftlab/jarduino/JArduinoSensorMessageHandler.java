package ru.skysoftlab.jarduino;

import org.sintef.jarduino.JArduinoMessageHandler;

import ru.skysoftlab.jarduino.msg.DigitalSensorReadMsg;

public abstract class JArduinoSensorMessageHandler extends JArduinoMessageHandler implements IJArduinoSensorMessageHandler {

	public void handleDigitalSensorRead(DigitalSensorReadMsg msg) { /* Nothing */ }
	
}
