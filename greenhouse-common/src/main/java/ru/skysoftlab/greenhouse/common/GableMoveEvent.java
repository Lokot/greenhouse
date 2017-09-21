package ru.skysoftlab.greenhouse.common;

public class GableMoveEvent {
	
	private GableState state;

	public GableMoveEvent(GableState state) {
		super();
		this.state = state;
	}

	public GableState getState() {
		return state;
	}

	public void setState(GableState state) {
		this.state = state;
	}

}
