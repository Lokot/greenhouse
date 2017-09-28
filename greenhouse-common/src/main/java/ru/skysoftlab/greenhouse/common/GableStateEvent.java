package ru.skysoftlab.greenhouse.common;

public class GableStateEvent {
	
	private GableState state;

	public GableStateEvent(GableState state) {
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
