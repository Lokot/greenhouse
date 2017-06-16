package ru.skysoftlab.greenhouse.impl;

import static ru.skysoftlab.greenhouse.common.GableState.Close;
import static ru.skysoftlab.greenhouse.common.GableState.Degrees30;
import static ru.skysoftlab.greenhouse.common.GableState.Degrees60;
import static ru.skysoftlab.greenhouse.common.GableState.Open;

import org.sintef.jarduino.DigitalPin;
import org.sintef.jarduino.DigitalState;
import org.sintef.jarduino.InvalidPinTypeException;
import org.sintef.jarduino.JArduinoDSensors;

import ru.skysoftlab.greenhouse.common.GableStateListener;

public class GrenHouseArduino extends JArduinoDSensors {

	private GableStateListener gableStateListener;
	
	public static final Object LOCK = new Object();

	public GrenHouseArduino(String id, GableStateListener gableStateListener) {
		super(id);
		this.gableStateListener = gableStateListener;
	}

	@Override
	protected void setup() throws InvalidPinTypeException {
	}

	@Override
	protected void loop() throws InvalidPinTypeException {
	}

	@Override
	protected void interruptDigitalPinState(DigitalPin pin, DigitalState state) {
		if (state.equals(HIGH)) {
			switch (pin) {
			case PIN_13:
				gableStateListener.gableStateIs(Open);
				break;

			case PIN_12:
				gableStateListener.gableStateIs(Degrees60);
				break;

			case PIN_11:
				gableStateListener.gableStateIs(Degrees30);
				break;

			case PIN_10:
				gableStateListener.gableStateIs(Close);
				break;

			default:
				break;
			}
		}
	}

}
