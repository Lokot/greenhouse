package ru.skysoftlab.jarduino.sensors;

import java.util.HashMap;
import java.util.Map;

public enum Dht22Params implements SensorParametr {
	
	TEMP((byte) 0), HUM((byte) 1);

	private final byte value;

	private Dht22Params(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}
	
	private static final Map<Byte, Dht22Params> map;

	static {
		map = new HashMap<Byte, Dht22Params>();
		map.put(TEMP.getValue(), TEMP);
		map.put(HUM.getValue(), HUM);
	}

	public static Dht22Params fromValue(byte b) {
		return map.get(b);
	}

	public boolean isIt(byte value) {
		return this.value == value;
	}

}
