package ru.skysoftlab.jarduino;

import org.sintef.jarduino.JArduinoMessageHandler;
import org.sintef.jarduino.JArduinoProtocolPacket;
import org.sintef.jarduino.msg.AnalogReadResultMsg;
import org.sintef.jarduino.msg.DigitalReadResultMsg;
import org.sintef.jarduino.msg.Eeprom_valueMsg;
import org.sintef.jarduino.msg.Eeprom_write_ackMsg;
import org.sintef.jarduino.msg.InterruptNotificationMsg;
import org.sintef.jarduino.msg.PongMsg;
import org.sintef.jarduino.msg.PulseInResultMsg;
import org.sintef.jarduino.observer.JArduinoObserver;

public abstract class JArduinoDriverMessageHandlerSensorWrapper extends
		JArduinoSensorMessageHandler implements JArduinoObserver {

	private JArduinoMessageHandler handler;

	public JArduinoDriverMessageHandlerSensorWrapper(
			JArduinoMessageHandler handler) {
		this.handler = handler;
	}

	public void handleDigitalReadResult(DigitalReadResultMsg msg) {
		handler.handleDigitalReadResult(msg);
	}

	public void handleAnalogReadResult(AnalogReadResultMsg msg) {
		handler.handleAnalogReadResult(msg);
	}

	public void handlePulseInResult(PulseInResultMsg msg) {
		handler.handlePulseInResult(msg);
	}

	public void handlePong(PongMsg msg) {
		handler.handlePong(msg);
	}

	public void handleInterruptNotification(InterruptNotificationMsg msg) {
		handler.handleInterruptNotification(msg);
	}

	public void handleEeprom_value(Eeprom_valueMsg msg) {
		handler.handleEeprom_value(msg);
	}

	public void handleEeprom_write_ack(Eeprom_write_ackMsg msg) {
		handler.handleEeprom_write_ack(msg);
	}

	public void receiveMsg(byte[] msg) {
		JArduinoProtocolPacket p = (JArduinoProtocolPacket) JArduinoSensorProtocol
				.createMessageFromPacket(msg);
		if (p != null) {
			p.acceptHandler(this);
		}
	}

}
