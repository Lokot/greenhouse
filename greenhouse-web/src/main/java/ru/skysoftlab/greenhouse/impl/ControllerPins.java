package ru.skysoftlab.greenhouse.impl;

import ru.skysoftlab.gpio.AnalogPin;
import ru.skysoftlab.gpio.DigitalPin;
import ru.skysoftlab.gpio.DigitalState;
import ru.skysoftlab.gpio.PinMode;

public interface ControllerPins {

	public final PinMode OUTPUT = PinMode.OUTPUT, INPUT = PinMode.INPUT;

	public final DigitalState LOW = DigitalState.LOW, HIGH = DigitalState.HIGH;

	public final AnalogPin illumPin = new AnalogPin("A_0");
	
	// TODO сделать определение пинов более универсальным
	public final DigitalPin dhtPin = new DigitalPin("PIN_7"), 
							stateOpen = new DigitalPin("PIN_13"),
							state60 = new DigitalPin("PIN_12"), 
							state30 = new DigitalPin("PIN_11"),
							stateClose = new DigitalPin("PIN_10"), 
							
							openSignal = new DigitalPin("PIN_4"),
							stopSignal = new DigitalPin("PIN_3"),
							closeSignal = new DigitalPin("PIN_2"),
							reservSignal = new DigitalPin("PIN_1");
}
