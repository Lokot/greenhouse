package ru.skysoftlab.greenhouse.quartz;

import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.AUTO;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.HUM_MAX;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_1;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_2;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_MAX;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_MIN;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.openejb.quartz.Job;
import org.apache.openejb.quartz.JobExecutionContext;
import org.apache.openejb.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.impl.IArduino;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.events.ConfigurationListener;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;

/**
 * Задание на сканирование температуры.
 * 
 * @author Локтионов А.Г.
 *
 */
@Singleton
public class GableJob implements Job, ConfigurationListener {

	private Logger LOG = LoggerFactory.getLogger(GableJob.class);

	@Inject
	private IArduino arduino;

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

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		LOG.info("Check params to gable " + context.getJobDetail().getKey());
		if (auto && arduino.isConnected()) {
			try {
				LOG.info("Gable Auto Mode");
				Float temperature = arduino.getTemperature();
				Float humidity = arduino.getHumidity();
				if (humidity < humMax) {
					if (temperature >= tempMin && temperature < temp1) {
						arduino.setGableState(GableState.Close);
					} else if (temperature >= temp1 && temperature < temp2) {
						arduino.setGableState(GableState.Degrees30);
					} else if (temperature >= temp2 && temperature < tempMax) {
						arduino.setGableState(GableState.Degrees60);
					} else if (temperature >= tempMax) {
						arduino.setGableState(GableState.Open);
					} else {
						// temperature < tempMin
						arduino.setGableState(GableState.Close);
					}
				} else {
					if (temperature >= tempMin && temperature < temp1) {
						arduino.setGableState(GableState.Degrees30);
					} else if (temperature >= temp1 && temperature < temp2) {
						arduino.setGableState(GableState.Degrees60);
					} else if (temperature >= temp2) {
						arduino.setGableState(GableState.Open);
					} else {
						// temperature < tempMin
						arduino.setGableState(GableState.Degrees30);
					}
				}
			} catch (NullPointerException e) {
				LOG.error("Arduino not reporting:", e);
			}
		} else {
			LOG.info("Gable Manual Mode");
		}
	}

	@Override
	public void editIntervalEvent(@Observes SystemConfigEvent event) {
		Float newtempMax = event.getParam(TEMP_MAX);
		if (newtempMax != null && !newtempMax.equals(tempMax)) {
			tempMax = newtempMax;
		}
		Float newtemp2 = event.getParam(TEMP_2);
		if (newtemp2 != null && !newtemp2.equals(temp2)) {
			temp2 = newtemp2;
		}
		Float newtemp1 = event.getParam(TEMP_1);
		if (newtemp1 != null && !newtemp1.equals(temp1)) {
			temp1 = newtemp1;
		}
		Float newtempMin = event.getParam(TEMP_MIN);
		if (newtempMin != null && !newtempMin.equals(tempMin)) {
			tempMin = newtempMin;
		}
		Float newHumMax = event.getParam(HUM_MAX);
		if (newHumMax != null && !newHumMax.equals(humMax)) {
			humMax = newHumMax;
		}
		Boolean newAuto = event.getParam(AUTO);
		if (newAuto != null && !newAuto.equals(auto)) {
			auto = newAuto;
		}
	}

}
