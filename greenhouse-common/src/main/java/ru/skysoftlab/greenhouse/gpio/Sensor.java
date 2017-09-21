package ru.skysoftlab.greenhouse.gpio;

import ru.skysoftlab.gpio.ISensor;
import ru.skysoftlab.gpio.ISensorParam;

public enum Sensor implements ISensor {
	DHT22((byte) 0, Dht22Params.TEMP, Dht22Params.HUM);

	private final byte value;
	private final ISensorParam[] params;

	private Sensor(byte value, ISensorParam... parametrs) {
		this.value = value;
		this.params = parametrs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.gpio.ttt.ISensor#getValue()
	 */
	@Override
	public byte getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.gpio.ttt.ISensor#getParametrByVal(byte)
	 */
	@Override
	public ISensorParam getParametrByVal(byte parVal) {
		for (ISensorParam sensorParametr : params) {
			if (sensorParametr.isIt(parVal)) {
				return sensorParametr;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.gpio.ttt.ISensor#getParams()
	 */
	@Override
	public ISensorParam[] getParams() {
		return params;
	}
}
