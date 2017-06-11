package ru.skysoftlab.jarduino.sensors;

public interface SensorParametr {
	
	public byte getValue();
	
	public boolean isIt(byte value);
}
