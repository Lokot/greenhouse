package ru.skysoftlab.greenhouse.common;

import java.io.Serializable;

import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.gpio.IGpioDevicePinsPorts;
import ru.skysoftlab.gpio.cdi.DeviceConnectedEvent;

/**
 * Интерфейс взаимодействия с Arduino.
 * 
 * @author Lokot
 *
 */
public interface IController extends IGableController, IGpioDevicePinsPorts, Serializable {

	public int getIllumination();

	public Float getTemperature();

	public Float getHumidity();

	public boolean isConnected();

	public void openIrrigationCountur(IDigitalPin pin);

	public void closeIrrigationCountur(IDigitalPin pin);
	
	public void deviceConnectedEvent(DeviceConnectedEvent event);
	
}
