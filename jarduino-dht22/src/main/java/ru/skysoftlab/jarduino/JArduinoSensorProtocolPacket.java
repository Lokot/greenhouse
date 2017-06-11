package ru.skysoftlab.jarduino;

import org.sintef.jarduino.JArduinoMessageHandler;
import org.sintef.jarduino.JArduinoProtocolPacket;

public abstract class JArduinoSensorProtocolPacket extends JArduinoProtocolPacket {

	public abstract void acceptHandler(JArduinoSensorMessageHandler v);

	@Override
	public void acceptHandler(JArduinoMessageHandler v) {
		acceptHandler((JArduinoSensorMessageHandler) v);
	}
}
