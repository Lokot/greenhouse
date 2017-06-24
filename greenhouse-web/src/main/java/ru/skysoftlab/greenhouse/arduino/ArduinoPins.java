package ru.skysoftlab.greenhouse.arduino;

import org.sintef.jarduino.DigitalPin;
import org.sintef.jarduino.DigitalState;
import org.sintef.jarduino.Pin;
import org.sintef.jarduino.PinMode;

public interface ArduinoPins {

	public final PinMode OUTPUT = PinMode.OUTPUT, INPUT = PinMode.INPUT;

	public final DigitalState LOW = DigitalState.LOW, HIGH = DigitalState.HIGH;

	public final Pin illumPin = Pin.A_0;
	
	public final DigitalPin dhtPin = DigitalPin.PIN_7, 
							stateOpen = DigitalPin.PIN_13, 
							state60 = DigitalPin.PIN_12, 
							state30 = DigitalPin.PIN_11,
							stateClose = DigitalPin.PIN_10, 
							
							openSignal = DigitalPin.PIN_4,
							stopSignal = DigitalPin.PIN_3, 
							closeSignal = DigitalPin.PIN_2,
							reservSignal = DigitalPin.PIN_1;
}
