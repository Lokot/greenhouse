package ru.skysoftlab.greenhouse.impl;

import java.io.Serializable;

import ru.skysoftlab.greenhouse.common.GableState;

/**
 * Интерфейс взаимодействия с Arduino.
 * 
 * @author Lokot
 *
 */
public interface IArduino extends Serializable {

	public int getIllumination();

	public float getTemperature();

	public float getHumidity();
	
	public GableState getGableState();
	
	public void setGableState(GableState gableState);

	public boolean isConnected();
	
}
