package ru.skysoftlab.greenhouse.common;

import java.io.Serializable;

import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.gpio.IGpioDevicePinsPorts;

/**
 * Интерфейс взаимодействия с Arduino.
 * 
 * @author Lokot
 *
 */
public interface IController extends IGpioDevicePinsPorts, Serializable {

	public int getIllumination();

	public Float getTemperature();

	public Float getHumidity();
	
	public GableState getGableState();
	
	public boolean isGableMoved();
	
	public void setGableState(GableState gableState);

	public boolean isConnected();

	public void openIrrigationCountur(IDigitalPin pin);

	public void closeIrrigationCountur(IDigitalPin pin);
	
}
