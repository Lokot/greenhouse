package ru.skysoftlab.greenhouse.impl;

import static ru.skysoftlab.greenhouse.common.GableState.Close;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.closeSignal;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.openSignal;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.state30;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.state60;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.stateClose;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.stateOpen;
import static ru.skysoftlab.greenhouse.impl.ControllerPins.stopSignal;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.gpio.GpioException;
import ru.skysoftlab.greenhouse.common.GableMoveEvent;
import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.common.GableStateEvent;
import ru.skysoftlab.greenhouse.common.GableStateListener;
import ru.skysoftlab.greenhouse.common.IGableController;
import ru.skysoftlab.greenhouse.common.IGableGpioDevice;

public class GbStater extends PinsBean implements IGableController, GableStateListener {

	private Logger LOG = LoggerFactory.getLogger(GbStater.class);

	// TODO засинхронизировать
	private GableState gbState;
	private final Object gbStateLocker = new Object();
	private GableState newGbState;

	private boolean isCalibrateMode = false;
	private final Object calibrateLocker = new Object();

	@Inject
	private javax.enterprise.event.Event<GableMoveEvent> gableMoveEvent;

	@Inject
	private IGableGpioDevice gpioDevice;

	public GableState getGableState() {
		synchronized (gbStateLocker) {
			if (gbState == null) {
				// поиск конька
				GableState gableNow = findGable();
				if (gableNow != null) {
					gbState = gableNow;
				} else {
					gbState = Close;
					gableCalibrate();
				}
			}
			return gbState;
		}
	}

	private GableState findGable() {
		try {
			if (gpioDevice.digitalRead(getDigitalPin(stateClose))) {
				return GableState.Close;
			}
			if (gpioDevice.digitalRead(getDigitalPin(state30))) {
				return GableState.Degrees30;
			}
			if (gpioDevice.digitalRead(getDigitalPin(state60))) {
				return GableState.Degrees60;
			}
			if (gpioDevice.digitalRead(getDigitalPin(stateOpen))) {
				return GableState.Open;
			}
		} catch (GpioException e) {
			LOG.error("Error findGable ", e);
		}
		return null;
	}

	private void gableCalibrate() {
		synchronized (calibrateLocker) {
			isCalibrateMode = true;
			sendCloseSignal();
		}
	}

	@Override
	public void setGableState(GableState newGableState) {
		int compare = newGableState.compareTo(getGableState());
		if (compare == 0) {
			// соответствует текущему состоянию
			newGbState = null;
		} else {
			newGbState = newGableState;
			if (compare > 0) {
				sendOpenSignal();
				LOG.info("Gable opens to " + newGableState.getStringState());
			} else if (compare < 0) {
				sendCloseSignal();
				LOG.info("Gable close to " + newGableState.getStringState());
			}
		}
	}

	@Override
	public void gableStateIs(GableState gableState) {
		synchronized (calibrateLocker) {
			gbState = gableState;
			if (newGbState != null && newGbState.equals(gableState)) {
				sendStopSignal();
				isCalibrateMode = false;
				newGbState = null;
			}
			if (isCalibrateMode) {
				sendStopSignal();
				isCalibrateMode = false;
			}
		}
		// fire event to ru.skysoftlab.greenhouse.ui.components.GableStateSelector
		gableMoveEvent.fire(new GableMoveEvent(gableState));
	}

	@Override
	public boolean isGableMoved() {
		if (gbState != null) {
			if (newGbState == null)
				return isCalibrateMode;
			return !gbState.equals(newGbState) || isCalibrateMode;
		}
		return true;
	}

	private void sendStopSignal() {
		try {
			gpioDevice.digitalWrite(getDigitalPin(stopSignal), false);
			gpioDevice.delay(1000);
			gpioDevice.digitalWrite(getDigitalPin(stopSignal), true);
			gpioDevice.delay(1000);
		} catch (GpioException e) {
			LOG.error("Error sendStopSignal ", e);
		}
	}

	private void sendOpenSignal() {
		sendStopSignal();
		try {
			gpioDevice.digitalWrite(getDigitalPin(openSignal), false);
			gpioDevice.delay(1000);
			gpioDevice.digitalWrite(getDigitalPin(openSignal), true);
		} catch (GpioException e) {
			LOG.error("Error sendOpenSignal ", e);
		}
	}

	private void sendCloseSignal() {
		sendStopSignal();
		try {
			gpioDevice.digitalWrite(getDigitalPin(closeSignal), false);
			gpioDevice.delay(1000);
			gpioDevice.digitalWrite(getDigitalPin(closeSignal), true);
		} catch (GpioException e) {
			LOG.error("Error sendCloseSignal ", e);
		}
	}

	public void gableStateChange(@Observes GableStateEvent event) {
		gableStateIs(event.getState());
	}

}
