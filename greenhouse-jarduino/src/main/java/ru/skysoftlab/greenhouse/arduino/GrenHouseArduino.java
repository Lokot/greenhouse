package ru.skysoftlab.greenhouse.arduino;

import static ru.skysoftlab.greenhouse.common.GableState.Close;
import static ru.skysoftlab.greenhouse.common.GableState.Degrees30;
import static ru.skysoftlab.greenhouse.common.GableState.Degrees60;
import static ru.skysoftlab.greenhouse.common.GableState.Open;

import org.sintef.jarduino.DigitalPin;
import org.sintef.jarduino.DigitalState;
import org.sintef.jarduino.InvalidPinTypeException;
import org.sintef.jarduino.JArduinoDSensors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.greenhouse.common.GableStateListener;

public class GrenHouseArduino extends JArduinoDSensors {
	
	private Logger LOG = LoggerFactory.getLogger(GrenHouseArduino.class);

	private GableStateListener gableStateListener;

	public GrenHouseArduino(String id) {
		super(id);
	}

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
			if (gableStateListener != null) {
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
			} else {
				LOG.error("No gableStateListener !!!");
			}
		}
	}

	public void setGableStateListener(GableStateListener listener) {
		this.gableStateListener = listener;
	}

}
