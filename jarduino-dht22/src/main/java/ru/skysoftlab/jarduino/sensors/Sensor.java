package ru.skysoftlab.jarduino.sensors;

import java.util.HashMap;
import java.util.Map;

public enum Sensor {

	DHT22((byte) 0);

	private final byte value;
	private final SensorParametr[] params;

	private Sensor(byte value, SensorParametr ...parametrs) {
		this.value = value;
		this.params = parametrs;
	}

	public byte getValue() {
		return value;
	}
	
	public SensorParametr getParametrByVal(byte parVal){
		for (SensorParametr sensorParametr : params) {
			if(sensorParametr.isIt(parVal)){
				return sensorParametr;
			}
		}
		return null;
	}
	
	public SensorParametr[] getParams() {
		return params;
	}

	private static final Map<Byte, Sensor> map;

	static {
		map = new HashMap<Byte, Sensor>();
		map.put(DHT22.getValue(), Sensor.DHT22);
	}

	public static Sensor fromValue(byte b) {
		return map.get(b);
	}
}
