package ru.skysoftlab.greenhouse.arduino;

import java.util.HashMap;
import java.util.Map;

import org.sintef.jarduino.JArduinoSensorParametr;

public enum JDht22Params implements JArduinoSensorParametr {
	
	TEMP((byte) 0), HUM((byte) 1);

	private final byte value;

	private JDht22Params(byte value) {
		this.value = value;
	}

	@Override
	public byte getValue() {
		return value;
	}
	
	private static final Map<Byte, JDht22Params> map;

	static {
		map = new HashMap<Byte, JDht22Params>();
		map.put(TEMP.getValue(), TEMP);
		map.put(HUM.getValue(), HUM);
	}

	public static JDht22Params fromValue(byte b) {
		return map.get(b);
	}

	@Override
	public boolean isIt(byte value) {
		return this.value == value;
	}

}
