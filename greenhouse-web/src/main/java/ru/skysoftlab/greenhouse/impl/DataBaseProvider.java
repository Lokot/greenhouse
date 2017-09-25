package ru.skysoftlab.greenhouse.impl;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.gpio.IDigitalPin;
import ru.skysoftlab.greenhouse.dto.ReadOutDto;
import ru.skysoftlab.greenhouse.dto.SystemConfigDto;
import ru.skysoftlab.greenhouse.jpa.entitys.DateConfig;
import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;
import ru.skysoftlab.greenhouse.jpa.entitys.Readout;
import ru.skysoftlab.greenhouse.jpa.entitys.drools.Rule;
import ru.skysoftlab.skylibs.entitys.properties.api.PropertyProvider;

public class DataBaseProvider implements Serializable {

	private static final long serialVersionUID = -3903680512655813319L;

	private Logger LOG = LoggerFactory.getLogger(DataBaseProvider.class);

	@Inject
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	@Inject
	private PropertyProvider propertyProvider;

	public void saveReadout(Readout readout) throws Exception {
		utx.begin();
		DateConfig cfg = null;
		try {
			cfg = getMaxVersionConfig();
		} catch (NoResultException e) {
			cfg = new DateConfig();
			cfg.setVersion(new Date());
			cfg.setHumMax(propertyProvider.getFloatValue(HUM_MAX));
			cfg.setTemp1(propertyProvider.getFloatValue(TEMP_1));
			cfg.setTemp2(propertyProvider.getFloatValue(TEMP_2));
			cfg.setTempMax(propertyProvider.getFloatValue(TEMP_MAX));
			cfg.setTempMin(propertyProvider.getFloatValue(TEMP_MIN));
			em.persist(cfg);
		}
		readout.setConfig(cfg);
		em.persist(readout);
		utx.commit();
	}

	public void saveConfig(SystemConfigDto dto) throws Exception {
		try {
			utx.begin();
			propertyProvider.setStringValue(SCAN_INTERVAL, dto.getScanInterval(), "Интервал сканирования (минуты)");
			propertyProvider.setStringValue(DATA_INTERVAL, dto.getScanInterval(), "Управления коньком (минуты)");
			propertyProvider.setStringValue(SERIAL_PORT, dto.getSerialPort(), "Порт для связи с Arduino");
			propertyProvider.setFloatValue(TEMP_MAX, dto.getTempMax(), "Максимальная температура");
			propertyProvider.setFloatValue(TEMP_2, dto.getTemp2(), "Температура 2");
			propertyProvider.setFloatValue(TEMP_1, dto.getTemp1(), "Температура 1");
			propertyProvider.setFloatValue(TEMP_MIN, dto.getTempMin(), "Минимальная температура");
			propertyProvider.setFloatValue(HUM_MAX, dto.getHumMax(), "Максимальная влажность");
			propertyProvider.setBooleanValue(AUTO, dto.getAuto(), "Автоматический режим");
			// propertyProvider.setDoubleValue(ILLUM_MIN, dto.getIllumMin(),
			// "Минимальная освещенность");
			em.persist(new DateConfig(dto));
			utx.commit();
		} finally {
			if (utx.getStatus() == 0) {
				utx.rollback();
				LOG.error("Save config error");
			}
		}
	}

	public DateConfig getMaxVersionConfig() throws NoResultException {
		TypedQuery<DateConfig> query = em.createNamedQuery("DateConfig.byMaxDate", DateConfig.class);
		DateConfig results = query.getSingleResult();
		return results;
	}

	/**
	 * Возвращает показания за день.
	 * 
	 * @param date
	 * @return
	 */
	public List<Readout> getDateTemp(Date date) {
		TypedQuery<Readout> query = em.createNamedQuery("Readout.byDate", Readout.class);
		query.setParameter("date", LocalDate.fromDateFields(date));
		List<Readout> results = query.getResultList();
		return results;
	}

	/**
	 * Возвращает показания за месяц.
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReadOutDto> getMonthTemp(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		Query query = em.createNamedQuery("Readout.monthByDays");
		query.setParameter(1, new GregorianCalendar(year, month, 1, 0, 0).getTime());
		query.setParameter(2, new GregorianCalendar(year, month, c.getActualMaximum(Calendar.DAY_OF_MONTH),
				c.getActualMaximum(Calendar.HOUR_OF_DAY), 59, 59).getTime());
		List<Object[]> resultList = query.getResultList();
		List<ReadOutDto> results = new ArrayList<>();
		for (Object[] obj : resultList) {
			ReadOutDto dto = new ReadOutDto();
			dto.setDayOrMonth((Integer) obj[0]);
			dto.setTemperature(round(Float.parseFloat(obj[1].toString()), 1));
			dto.setHumidity(round(Float.parseFloat(obj[2].toString()), 1));
			dto.setIllumination(Integer.parseInt(obj[3].toString()));
			results.add(dto);
		}
		return results;
	}

	/**
	 * Возвращает показания за год.
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReadOutDto> getYearTemp(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		Query query = em.createNamedQuery("Readout.yearByMonth");
		query.setParameter(1, new GregorianCalendar(year, Calendar.JANUARY, 1).getTime());
		query.setParameter(2,
				new GregorianCalendar(year, Calendar.DECEMBER, 31, c.getActualMaximum(Calendar.HOUR_OF_DAY), 59, 59)
						.getTime());
		List<Object[]> resultList = query.getResultList();
		List<ReadOutDto> results = new ArrayList<>();
		for (Object[] obj : resultList) {
			ReadOutDto dto = new ReadOutDto();
			dto.setDayOrMonth((Integer) obj[0]);
			dto.setTemperature(round(Float.parseFloat(obj[1].toString()), 1));
			dto.setHumidity(round(Float.parseFloat(obj[2].toString()), 1));
			dto.setIllumination(Integer.parseInt(obj[3].toString()));
			results.add(dto);
		}
		return results;
	}

	private float round(float number, int scale) {
		int pow = 10;
		for (int i = 1; i < scale; i++)
			pow *= 10;
		float tmp = number * pow;
		return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
	}

	public void updateBooleanProperty(String key, Boolean value, String name) throws Exception {
		try {
			utx.begin();
			propertyProvider.setBooleanValue(key, value, name);
			utx.commit();
		} finally {
			if (utx.getStatus() == 0) {
				utx.rollback();
				LOG.error("Save config error");
			}
		}
	}

	public List<IrrigationCountur> getIrigationCounters() {
		TypedQuery<IrrigationCountur> query = em.createNamedQuery("IrrigationCountur.getAll", IrrigationCountur.class);
		List<IrrigationCountur> results = query.getResultList();
		return results;
	}

	public IrrigationCountur getIrrigationCountur(Object id) {
		return em.find(IrrigationCountur.class, id);
	}

	@Produces
	public Iterator<Rule> getAllRules() {
		TypedQuery<Rule> query = em.createNamedQuery("Rule.getAll", Rule.class);
		List<Rule> results = query.getResultList();
		return results.iterator();
	}

	public List<IDigitalPin> getIrrigationPins() {
		List<IDigitalPin> rv = new ArrayList<>();
		for (IrrigationCountur countur : getIrigationCounters()) {
			rv.add(countur.getPin());
		}
		return rv;
	}
}
