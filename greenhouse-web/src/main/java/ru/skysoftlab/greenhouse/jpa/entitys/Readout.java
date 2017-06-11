package ru.skysoftlab.greenhouse.jpa.entitys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.skylibs.types.LocalDateUserType;
import ru.skysoftlab.skylibs.types.LocalTimeUserType;

/**
 * Показания датчиков.
 * 
 * @author Loktionov Artem
 *
 */
@Entity
@TypeDefs({ @TypeDef(name = "dateType", typeClass = LocalDateUserType.class),
		@TypeDef(name = "timeType", typeClass = LocalTimeUserType.class) })
@NamedQuery(name = "Readout.byDate", query = "SELECT e FROM Readout e WHERE e.date=:date")
@NamedNativeQueries({@NamedNativeQuery(name = "Readout.yearByMonth", query = "SELECT MONTH(lockal_date) as md,"
							+ " sum(temperature)/count(temperature) as ts,"
							+ " sum(humidity)/count(humidity) as hs,"
							+ " sum(illumination)/count(illumination) as ils"
							+ " FROM Readout"
							+ " WHERE lockal_date BETWEEN ? AND ?"
							+ " GROUP BY MONTH(lockal_date)"
							+ " ORDER BY md"),
					@NamedNativeQuery(name = "Readout.monthByDays", query = "SELECT DAY(lockal_date) as md,"
							+ " sum(temperature)/count(temperature) as ts,"
							+ " sum(humidity)/count(humidity) as hs,"
							+ " sum(illumination)/count(illumination) as ils"
							+ " FROM Readout"
							+ " WHERE lockal_date BETWEEN ? AND ?"
							+ " GROUP BY DAY(lockal_date)"
							+ " ORDER BY md")})
public class Readout implements Serializable {

	private static final long serialVersionUID = -6235416415329502064L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Float temperature;
	private Float humidity;
	private Integer illumination;
	@Enumerated(EnumType.STRING)
	private GableState gableState;

	@Type(type = "dateType")
	@Column(name = "lockal_date")
	private LocalDate date;
	@Type(type = "timeType")
	@Column(name = "lockal_time")
	private LocalTime time;

	@ManyToOne
	@JoinColumn(name = "CONFIG_VERSION")
	private DateConfig config;

	public Readout() {

	}

	public Readout(Float temperature, Float humidity, Integer illumination,
			Date date) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.illumination = illumination;
		LocalDateTime t = LocalDateTime.fromDateFields(date);
		this.time = t.toLocalTime();
		this.date = t.toLocalDate();
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

	public Float getTemperature() {
		return this.temperature;
	}

	public Readout setTemperature(Float temp) {
		this.temperature = temp;
		return this;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public DateConfig getConfig() {
		return config;
	}

	public void setConfig(DateConfig config) {
		this.config = config;
	}

	public GableState getGableState() {
		return gableState;
	}

	public void setGableState(GableState gableState) {
		this.gableState = gableState;
	}

}