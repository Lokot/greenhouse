package ru.skysoftlab.greenhouse.dto;

public class ReadOutActionDto {

	private Float temperature;
	private Float humidity;
	private Integer illumination;

	public ReadOutActionDto() {
		super();
	}

	public ReadOutActionDto(Float temperature, Float humidity, Integer illumination) {
		super();
		this.temperature = temperature;
		this.humidity = humidity;
		this.illumination = illumination;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public Float getHumidity() {
		return humidity;
	}

	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}

	public Integer getIllumination() {
		return illumination;
	}

	public void setIllumination(Integer illumination) {
		this.illumination = illumination;
	}

}
