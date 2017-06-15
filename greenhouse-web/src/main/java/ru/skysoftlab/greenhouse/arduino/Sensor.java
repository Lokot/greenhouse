package ru.skysoftlab.greenhouse.arduino;

import org.sintef.jarduino.JArduinoSensor;
import org.sintef.jarduino.JArduinoSensorParametr;

public enum Sensor implements JArduinoSensor {

	DHT22((byte) 0);
	
	static {
		map.put(DHT22.getValue(), Sensor.DHT22);
	}

	private final byte value;
	private final JArduinoSensorParametr[] params;

	private Sensor(byte value, JArduinoSensorParametr ...parametrs) {
		this.value = value;
		this.params = parametrs;
	}

	@Override
	public byte getValue() {
		return value;
	}
	
	@Override
	public JArduinoSensorParametr getParametrByVal(byte parVal){
		for (JArduinoSensorParametr sensorParametr : params) {
			if(sensorParametr.isIt(parVal)){
				return sensorParametr;
			}
		}
		return null;
	}
	
	@Override
	public JArduinoSensorParametr[] getParams() {
		return params;
	}
	
}
