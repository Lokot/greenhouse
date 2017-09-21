package ru.skysoftlab.greenhouse.common;

/**
 * Состояния конька крыши.
 * 
 * @author Lokot
 *
 */
public enum GableState {
	/** Закрыт */
	Close(0),
	/** Открыт на 30 градусов */
	Degrees30(30),
	/** Открыт на 60 градусов */
	Degrees60(60),
	/** Открыт */
	Open(100);

	private int state;

	private GableState(int s) {
		state = s;
	}

	public int toInt() {
		return state;
	}

	public String getStringState() {
		switch (this) {

		case Close:
			return "0%";

		case Degrees30:
			return "30%";

		case Degrees60:
			return "60%";

		case Open:
		default:
			return "100%";
		}
	}

}
