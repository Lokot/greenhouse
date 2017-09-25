package ru.skysoftlab.greenhouse.jpa.entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.joda.time.Duration;

import ru.skysoftlab.gpio.DigitalPin;
import ru.skysoftlab.skylibs.common.EditableEntity;
import ru.skysoftlab.skylibs.joda.types.DurationUserType;
import ru.skysoftlab.skylibs.types.PinDbType;

/**
 * Поливочный контур.
 * 
 * @author Lokot
 *
 */
@Entity
@TypeDefs({ @TypeDef(name = "durationType", typeClass = DurationUserType.class),
		@TypeDef(name = "pinType", typeClass = PinDbType.class) })
@NamedQuery(name = "IrrigationCountur.getAll", query = "SELECT e FROM IrrigationCountur e WHERE e.run=true")
public class IrrigationCountur implements EditableEntity<Long> {

	private static final long serialVersionUID = -1927142160552166374L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String cronExpr;

	@Type(type = "durationType")
	private Duration duration;

	private String name;

	@Type(type = "pinType")
	private DigitalPin pin;

	private Boolean run;

	@Override
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

	@Override
	public String toString() {
		return name;
	}

	public Boolean getRun() {
		return run;
	}

	public void setRun(Boolean run) {
		this.run = run;
	}

}