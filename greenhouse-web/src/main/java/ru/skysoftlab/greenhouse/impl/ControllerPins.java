package ru.skysoftlab.greenhouse.impl;

import ru.skysoftlab.gpio.DigitalState;
import ru.skysoftlab.gpio.PinMode;

public interface ControllerPins {

	public final PinMode OUTPUT = PinMode.OUTPUT, INPUT = PinMode.INPUT;

	public final DigitalState LOW = DigitalState.LOW, HIGH = DigitalState.HIGH;

	public final String illumPin = "illumPin", dhtPin = "dhtPin", stateOpen = "stateOpen", state60 = "state60",
			state30 = "state30", stateClose = "stateClose", openSignal = "openSignal", stopSignal = "stopSignal",
			closeSignal = "closeSignal", reservSignal = "reservSignal";
}
