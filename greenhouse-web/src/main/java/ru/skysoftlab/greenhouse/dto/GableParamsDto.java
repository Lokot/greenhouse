package ru.skysoftlab.greenhouse.dto;

public class GableParamsDto {

	private Float temperature;
	private Float humidity;

	public GableParamsDto(Float temperature, Float humidity) {
		this.temperature = temperature;
		this.humidity = humidity;
	}

	public Float getTemperature() {
		return temperature;
	}

	public Float getHumidity() {
		return humidity;
	}

}
