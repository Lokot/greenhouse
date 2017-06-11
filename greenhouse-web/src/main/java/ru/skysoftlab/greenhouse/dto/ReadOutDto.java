package ru.skysoftlab.greenhouse.dto;


public class ReadOutDto {

	private Integer dayOrMonth;

	private Float temperature;
	private Float humidity;
	private Integer illumination;

	public ReadOutDto() {
		super();
	}

	public ReadOutDto(Integer dayOrMonth, Float temperature, Float humidity,
			Integer illumination) {
		super();
		this.dayOrMonth = dayOrMonth;
		this.temperature = temperature;
		this.humidity = humidity;
		this.illumination = illumination;
	}

	public Integer getDayOrMonth() {
		return dayOrMonth;
	}

	public void setDayOrMonth(Integer dayOrMonth) {
		this.dayOrMonth = dayOrMonth;
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
