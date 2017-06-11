package ru.skysoftlab.jarduino;

import org.sintef.jarduino.IJArduinoMessageHandler;

import ru.skysoftlab.jarduino.msg.DigitalPinStateNotification;
import ru.skysoftlab.jarduino.msg.DigitalSensorReadMsg;
import ru.skysoftlab.jarduino.msg.DigitalSensorReadResultMsg;

public interface IJArduinoSensorMessageHandler extends IJArduinoMessageHandler {
	
	public void handleDigitalSensorRead(DigitalSensorReadMsg msg);
	
	public void handleDigitalSensorReadResult(DigitalSensorReadResultMsg msg);
	
	public void handleDigitalPinStateNotification(DigitalPinStateNotification msg);
}
