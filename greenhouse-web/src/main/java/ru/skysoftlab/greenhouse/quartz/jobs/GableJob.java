package ru.skysoftlab.greenhouse.quartz.jobs;

import static ru.skysoftlab.greenhouse.impl.ControllerProvider.LOCK;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.AUTO;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.HUM_MAX;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_1;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_2;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_MAX;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_MIN;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.openejb.quartz.Job;
import org.apache.openejb.quartz.JobExecutionContext;
import org.apache.openejb.quartz.JobExecutionException;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.greenhouse.dto.GableParamsDto;
import ru.skysoftlab.greenhouse.dto.SystemConfigDto;
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

	private Boolean auto;

	@Inject
	private IController controller;

	@Inject
	private SystemConfigDto dto;

	@Inject
	@KSession("ksession-rules")
	private KieSession kSession;

	@PostConstruct
	public void init() {
		auto = dto.getAuto();
		kSession.setGlobal(HUM_MAX, dto.getHumMax());
		kSession.setGlobal(TEMP_MAX, dto.getTempMax());
		kSession.setGlobal(TEMP_2, dto.getTemp2());
		kSession.setGlobal(TEMP_1, dto.getTemp1());
		kSession.setGlobal(TEMP_MIN, dto.getTempMin());
		kSession.setGlobal("arduino", controller);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.openejb.quartz.Job#execute(org.apache.openejb.quartz.
	 * JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		synchronized (LOCK) {
			LOG.info("Check params to gable " + context.getJobDetail().getKey());
			if (auto && controller.isConnected() && !controller.isGableMoved()) {
				try {
					LOG.info("Gable Auto Mode");
					Float temperature = controller.getTemperature();
					Float humidity = controller.getHumidity();

					GableParamsDto readOutDto = new GableParamsDto(temperature, humidity);
					kSession.insert(readOutDto);
					kSession.fireAllRules();
				} catch (NullPointerException e) {
					LOG.error("Arduino not reporting:", e);
				}
			} else {
				LOG.info("Gable Manual Mode");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.skylibs.events.ConfigurationListener#configUpdated(ru.
	 * skysoftlab.skylibs.events.SystemConfigEvent)
	 */
	@Override
	public void configUpdated(@Observes SystemConfigEvent event) {
		Float tempMax = event.getParam(TEMP_MAX);
		if (tempMax != null) {
			kSession.setGlobal(TEMP_MAX, tempMax);
		}
		Float temp2 = event.getParam(TEMP_2);
		if (temp2 != null) {
			kSession.setGlobal(TEMP_2, temp2);
		}
		Float temp1 = event.getParam(TEMP_1);
		if (temp1 != null) {
			kSession.setGlobal(TEMP_1, temp1);
		}
		Float tempMin = event.getParam(TEMP_MIN);
		if (tempMin != null) {
			kSession.setGlobal(TEMP_MIN, tempMin);
		}
		Float humMax = event.getParam(HUM_MAX);
		if (humMax != null) {
			kSession.setGlobal(HUM_MAX, humMax);
		}
		Boolean newAuto = event.getParam(AUTO);
		if (newAuto != null && !newAuto.equals(auto)) {
			auto = newAuto;
		}
	}

}
