package ru.skysoftlab.greenhouse.quartz.jobs;

import static ru.skysoftlab.greenhouse.impl.GrenHouseArduino.LOCK;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.openejb.quartz.Job;
import org.apache.openejb.quartz.JobExecutionContext;
import org.apache.openejb.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.greenhouse.arduino.IArduino;
import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.impl.DataBaseProvider;
import ru.skysoftlab.greenhouse.jpa.entitys.Readout;

/**
 * Задание на сканирование температуры.
 * 
 * @author Локтионов А.Г.
 *
 */
@Singleton
public class ScanTempJob implements Job {

	private Logger LOG = LoggerFactory.getLogger(ScanTempJob.class);

	@Inject
	private IArduino arduino;

	@Inject
	private DataBaseProvider dataBaseProvider;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		synchronized (LOCK) {
			if (arduino.isConnected()) {
				try {
					LOG.info("Check params to database "
							+ context.getJobDetail().getKey());
					Date now = context.getScheduledFireTime();
					Float temperature = arduino.getTemperature();
					Float humidity = arduino.getHumidity();
					int illumination = arduino.getIllumination();
					GableState gableState = arduino.getGableState();
					Readout rd = new Readout(temperature, humidity, illumination, now);
					rd.setGableState(gableState);
					try {
						dataBaseProvider.saveReadout(rd);
					} catch (Exception e) {
						LOG.error("Save Readout error", e);
					}
				} catch (NullPointerException e) {
					LOG.error("Arduino not reporting:", e);
				}
			}
		}
	}
}
