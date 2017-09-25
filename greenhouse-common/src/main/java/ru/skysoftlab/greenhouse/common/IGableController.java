package ru.skysoftlab.greenhouse.common;

public interface IGableController {
	
	public GableState getGableState();

	public boolean isGableMoved();

	public void setGableState(GableState gableState);
}
