package ru.skysoftlab.greenhouse.jpa.entitys;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.joda.time.Duration;
import org.sintef.jarduino.DigitalPin;

import ru.skysoftlab.skylibs.types.DurationUserType;

@Entity
@TypeDefs({ @TypeDef(name = "durationType", typeClass = DurationUserType.class)})
public class IrrigationCountur implements Serializable {

	private static final long serialVersionUID = -1927142160552166374L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String cronExpr;
	
	@Type(type = "durationType")
	private Duration duration;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private DigitalPin pin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCronExpr() {
		return cronExpr;
	}

	public void setCronExpr(String cronExpr) {
		this.cronExpr = cronExpr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DigitalPin getPin() {
		return pin;
	}

	public void setPin(DigitalPin pin) {
		this.pin = pin;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

}