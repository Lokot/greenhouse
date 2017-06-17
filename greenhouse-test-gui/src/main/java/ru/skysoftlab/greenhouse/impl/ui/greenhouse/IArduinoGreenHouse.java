package ru.skysoftlab.greenhouse.impl.ui.greenhouse;

public interface IArduinoGreenHouse {

	public String getTemp();
	public String getHum();
	public int getIllum();
	public byte getGableState(int objectState);
	public void gableOpen(byte state);
	public void gableStop(byte state);
	public void gableClose(byte state);
}
