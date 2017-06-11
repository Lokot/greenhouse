package ru.skysoftlab.jarduino.example;

import org.sintef.jarduino.InvalidPinTypeException;
import org.sintef.jarduino.JArduino;
import org.sintef.jarduino.Pin;
import org.sintef.jarduino.comm.Serial4JArduino;

import ru.skysoftlab.jarduino.JArduinoDSensors;
import ru.skysoftlab.jarduino.sensors.Dht22Params;
import ru.skysoftlab.jarduino.sensors.Sensor;

public class Dht22Reader extends JArduinoDSensors {

	private final Pin dhtPin = p2;

	public Dht22Reader(String port) {
		super(port);
	}

	@Override
	protected void setup() throws InvalidPinTypeException {
		// pinMode(dhtPin, INPUT);
		pinMode(pA0, INPUT);
	}

	@Override
	protected void loop() throws InvalidPinTypeException {
		// System.out.println(analogRead(pA0));
		// delay(3000);
		String value = sensorRead(dhtPin, Sensor.DHT22, Dht22Params.TEMP);
		System.out.println(value);
		delay(1000); 
		value = sensorRead(dhtPin, Sensor.DHT22, Dht22Params.HUM);
		System.out.println(value);
		delay(1000); 
	}

	public static void main(String[] args) throws InvalidPinTypeException {
		JArduinoDSensors arduino = new Dht22Reader("COM3");
		String value = arduino.sensorRead(Pin.PIN_2, Sensor.DHT22, Dht22Params.TEMP);
		System.out.println(value);
//		arduino.runArduinoProcess();
	}
}
