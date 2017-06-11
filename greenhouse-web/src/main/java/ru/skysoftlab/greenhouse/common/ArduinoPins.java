package ru.skysoftlab.greenhouse.common;

import org.sintef.jarduino.DigitalState;
import org.sintef.jarduino.Pin;
import org.sintef.jarduino.PinMode;

public interface ArduinoPins {

	public final PinMode OUTPUT = PinMode.OUTPUT, INPUT = PinMode.INPUT;

	public final DigitalState LOW = DigitalState.LOW, HIGH = DigitalState.HIGH;

	public final Pin dhtPin = Pin.PIN_7, 
			
			illumPin = Pin.A_0,
			
			stateOpen = Pin.PIN_13, 
			state60 = Pin.PIN_12, 
			state30 = Pin.PIN_11,
			stateClose = Pin.PIN_10, 
			
			openSignal = Pin.PIN_4,
			stopSignal = Pin.PIN_3, 
			closeSignal = Pin.PIN_2,
			reservSignal = Pin.PIN_1;
}
