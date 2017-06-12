package ru.skysoftlab.jarduino;

import org.sintef.jarduino.JArduinoMessageHandler;
import org.sintef.jarduino.msg.AnalogReadResultMsg;
import org.sintef.jarduino.msg.DigitalReadResultMsg;
import org.sintef.jarduino.msg.Eeprom_valueMsg;
import org.sintef.jarduino.msg.Eeprom_write_ackMsg;
import org.sintef.jarduino.msg.InterruptNotificationMsg;
import org.sintef.jarduino.msg.PongMsg;
import org.sintef.jarduino.msg.PulseInResultMsg;

import ru.skysoftlab.jarduino.msg.DigitalSensorReadMsg;

public abstract class JArduinoSensorMessageHandler extends JArduinoMessageHandler implements IJArduinoSensorMessageHandler {

	public void handleDigitalReadResult(DigitalReadResultMsg msg) { /* Nothing */ }
	public void handleAnalogReadResult(AnalogReadResultMsg msg) { /* Nothing */ }
	public void handlePulseInResult(PulseInResultMsg msg){ /* Nothing */ }
	public void handlePong(PongMsg msg) { /* Nothing */ }
	public void handleInterruptNotification(InterruptNotificationMsg msg){ /* Nothing */ }
	public void handleEeprom_value(Eeprom_valueMsg msg) { /* Nothing */ }
	public void handleEeprom_write_ack(Eeprom_write_ackMsg msg) { /* Nothing */ }
	
	public void handleDigitalSensorRead(DigitalSensorReadMsg msg) { /* Nothing */ }
	
}
