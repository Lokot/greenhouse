package ru.skysoftlab.greenhouse.dto;


public class ReadOutDto extends ReadOutActionDto {

	private Integer dayOrMonth;

	public ReadOutDto() {
		super();
	}

	public ReadOutDto(Integer dayOrMonth, Float temperature, Float humidity,
			Integer illumination) {
		super(temperature, humidity, illumination);
		this.dayOrMonth = dayOrMonth;
	}

	public Integer getDayOrMonth() {
		return dayOrMonth;
	}

	public void setDayOrMonth(Integer dayOrMonth) {
		this.dayOrMonth = dayOrMonth;
	}

}
