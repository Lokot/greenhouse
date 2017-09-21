package ru.skysoftlab.greenhouse.dto;

import static ru.skysoftlab.greenhouse.common.ConfigurationNames.AUTO;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.DATA_INTERVAL;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.HUM_MAX;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.SCAN_INTERVAL;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.SERIAL_PORT;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_1;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_2;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_MAX;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_MIN;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ru.skysoftlab.skylibs.annatations.AppProperty;

public class SystemConfigDto implements Serializable {

	private static final long serialVersionUID = -1809506535079134130L;

	@Inject
	@AppProperty(SCAN_INTERVAL)
	private String scanInterval;

	@Inject
	@AppProperty(DATA_INTERVAL)
	private String dataInterval;

	@Inject
	@AppProperty(SERIAL_PORT)
	private String serialPort;
	
	@Inject
	@AppProperty(TEMP_MAX)
	private Float tempMax;

	@Inject
	@AppProperty(TEMP_2)
	private Float temp2;

	@Inject
	@AppProperty(TEMP_1)
	private Float temp1;

	@Inject
	@AppProperty(TEMP_MIN)
	private Float tempMin;

	@Inject
	@AppProperty(HUM_MAX)
	private Float humMax;

	@Inject
	@AppProperty(AUTO)
	private Boolean auto;

	// @Inject
	// @AppProperty(ILLUM_MIN)
	// private Double illumMin;

	public Map<String, Object> getDataForEvent() {
		Map<String, Object> rv = new HashMap<>();
		rv.put(SCAN_INTERVAL, getScanInterval());
		rv.put(DATA_INTERVAL, getDataInterval());
		rv.put(SERIAL_PORT, getSerialPort());
		rv.put(TEMP_MAX, getTempMax());
		rv.put(TEMP_2, getTemp2());
		rv.put(TEMP_1, getTemp1());
		rv.put(TEMP_MIN, getTempMin());
		rv.put(HUM_MAX, getHumMax());
		rv.put(AUTO, getAuto());
		// rv.put(ILLUM_MIN, getIllumMin());
		return rv;
	}

	public String getScanInterval() {
		return scanInterval;
	}

	public void setScanInterval(String scanInterval) {
		this.scanInterval = scanInterval;
	}

	public String getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(String serialPort) {
		this.serialPort = serialPort;
	}

	public String getDataInterval() {
		return dataInterval;
	}

	public void setDataInterval(String dataInterval) {
		this.dataInterval = dataInterval;
	}
	
	public Float getTempMax() {
		return tempMax;
	}

	public void setTempMax(Float tempMax) {
		this.tempMax = tempMax;
	}

	public Float getTempMin() {
		return tempMin;
	}

	public void setTempMin(Float tempMin) {
		this.tempMin = tempMin;
	}

	public Float getHumMax() {
		return humMax;
	}

	public void setHumMax(Float humMax) {
		this.humMax = humMax;
	}

	public Float getTemp2() {
		return temp2;
	}

	public void setTemp2(Float temp2) {
		this.temp2 = temp2;
	}

	public Float getTemp1() {
		return temp1;
	}

	public void setTemp1(Float temp1) {
		this.temp1 = temp1;
	}

	public Boolean getAuto() {
		return auto;
	}

	public void setAuto(Boolean auto) {
		this.auto = auto;
	}

	// public Double getIllumMin() {
	// return illumMin;
	// }
	//
	// public void setIllumMin(Double illumMin) {
	// this.illumMin = illumMin;
	// }

}
