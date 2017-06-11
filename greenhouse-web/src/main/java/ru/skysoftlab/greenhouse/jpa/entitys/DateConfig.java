package ru.skysoftlab.greenhouse.jpa.entitys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Type;

import ru.skysoftlab.greenhouse.dto.SystemConfigDto;

@Entity
@NamedQueries({ @NamedQuery(name = "DateConfig.byMaxDate", 
	query = "select f from DateConfig f where f.version = (select max(ff.version) from DateConfig ff)") })
// query = "select f from DateConfig f where f.version = (select max(ff.version) from DateConfig ff where ff.version = f.version)"
// SELECT e FROM DateConfig e WHERE e.date=:date
public class DateConfig implements Serializable {

	private static final long serialVersionUID = 595291147774458871L;

	@Id
	@Type(type="timestamp")
	private Date version;

	private Float tempMax;

	private Float temp2;

	private Float temp1;

	private Float tempMin;

	private Float humMax;

	public DateConfig() {

	}

	public DateConfig(SystemConfigDto configDto) {
		temp1 = configDto.getTemp1();
		temp2 = configDto.getTemp2();
		tempMax = configDto.getTempMax();
		tempMin = configDto.getTempMin();
		humMax = configDto.getHumMax();
		version = new Date();
	}

	public Float getTempMax() {
		return tempMax;
	}

	public void setTempMax(Float tempMax) {
		this.tempMax = tempMax;
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

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

}
