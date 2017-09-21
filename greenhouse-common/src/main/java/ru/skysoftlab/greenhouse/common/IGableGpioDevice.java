package ru.skysoftlab.greenhouse.common;

import ru.skysoftlab.gpio.IGpioDevice;

public interface IGableGpioDevice extends IGpioDevice {
	
	public void setGableStateListener(GableStateListener listener);
}
