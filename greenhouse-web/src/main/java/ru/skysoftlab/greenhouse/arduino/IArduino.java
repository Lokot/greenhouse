package ru.skysoftlab.greenhouse.arduino;

import java.io.Serializable;

import org.sintef.jarduino.DigitalPin;

import ru.skysoftlab.greenhouse.common.GableState;

/**
 * Интерфейс взаимодействия с Arduino.
 * 
 * @author Lokot
 *
 */
public interface IArduino extends Serializable {

	public int getIllumination();

	public Float getTemperature();

	public Float getHumidity();
	
	public GableState getGableState();
	
	public boolean isGableMoved();
	
	public void setGableState(GableState gableState);

	public boolean isConnected();

	public void openIrrigationCountur(DigitalPin pin);

	public void closeIrrigationCountur(DigitalPin pin);
	
}
