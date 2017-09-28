package ru.skysoftlab.greenhouse.common;

import javax.inject.Inject;

import ru.skysoftlab.gpio.cdi.AbstractGpioDevice;
import ru.skysoftlab.skylibs.events.ConfigurationListener;

public abstract class AbstractGableGpioDevice extends AbstractGpioDevice
		implements IGableGpioDevice, ConfigurationListener, GableStateListener {

	@Inject
	private javax.enterprise.event.Event<GableStateEvent> gableStateEvent;

	@Override
	public void gableStateIs(GableState gableState) {
		gableStateEvent.fire(new GableStateEvent(gableState));
	}

}
