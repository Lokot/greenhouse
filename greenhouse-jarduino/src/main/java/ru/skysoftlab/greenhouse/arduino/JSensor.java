package ru.skysoftlab.greenhouse.arduino;

import org.sintef.jarduino.JArduinoSensor;
import org.sintef.jarduino.JArduinoSensorParametr;

public enum JSensor implements JArduinoSensor {

	DHT22((byte) 0, JDht22Params.TEMP, JDht22Params.HUM);
	
	static {
		map.put(DHT22.getValue(), JSensor.DHT22);
	}

	private final byte value;
	private final JArduinoSensorParametr[] params;

	private JSensor(byte value, JArduinoSensorParametr ...parametrs) {
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
