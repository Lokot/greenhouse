package ru.skysoftlab.greenhouse.quartz;

import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.AUTO;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.SCAN_INTERVAL;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.DATA_INTERVAL;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.openejb.quartz.JobBuilder;
import org.apache.openejb.quartz.JobDetail;
import org.apache.openejb.quartz.JobKey;
import org.apache.openejb.quartz.SchedulerException;
import org.apache.openejb.quartz.Trigger;
import org.apache.openejb.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.events.ConfigurationListener;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;
import ru.skysoftlab.skylibs.quartz.JobController;

/**
 * Контроллер задач.
 * 
 * @author Локтионов А.Г.
 *
 */
@Startup
@Singleton
public class GreenHouseJobController extends JobController implements
		ConfigurationListener {

	private Logger LOG = LoggerFactory.getLogger(GreenHouseJobController.class);

	private static final String SYSTEM_GROUP = "system-jobs";

	private final TriggerKey tempTK = TriggerKey.triggerKey(
			"scan-temp-startUp", SYSTEM_GROUP);
	private final TriggerKey gableTK = TriggerKey.triggerKey(
			"scan-gable-startUp", SYSTEM_GROUP);

	private final JobKey gableJobKey = JobKey
			.jobKey("scan-gable", SYSTEM_GROUP);

	@Inject
	@AppProperty(SCAN_INTERVAL)
	private String scanInterval;

	@Inject
	@AppProperty(DATA_INTERVAL)
	private String dataInterval;

	@Inject
	@AppProperty(AUTO)
	private Boolean auto;

	@Override
	protected void startJobs() {
		startScanTempJob();
		if (auto) {
			startGableJob();
		}
	}

	private void startScanTempJob() {
		try {
			JobKey scanJobKey = JobKey.jobKey("scan-readout", SYSTEM_GROUP);
			JobDetail scanJob = JobBuilder.newJob(ScanTempJob.class)
					.withIdentity(scanJobKey).build();
			Trigger trigger = createCronTrigger(tempTK, scanInterval, null);
			getScheduler().scheduleJob(scanJob, trigger);
		} catch (SchedulerException e) {
			LOG.error("Error create scan temp job", e);
		}
	}

	private void startGableJob() {
		try {
			JobDetail scanJob = JobBuilder.newJob(GableJob.class)
					.withIdentity(gableJobKey).build();
			Trigger trigger = createCronTrigger(gableTK, dataInterval, null);
			getScheduler().scheduleJob(scanJob, trigger);
		} catch (SchedulerException e) {
			LOG.error("Error create scan temp job", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.skysoftlab.skylibs.events.ConfigurationListener#editIntervalEvent(
	 * ru.skysoftlab.skylibs.events.SystemConfigEvent)
	 */
	@Override
	public void editIntervalEvent(@Observes SystemConfigEvent event) {
		String newScanInterval = event.getParam(SCAN_INTERVAL);
		if (newScanInterval != null && newScanInterval.length() > 0
				&& !newScanInterval.equals(scanInterval)) {
			scanInterval = newScanInterval;
			try {
				rescheduleJobNow(tempTK, scanInterval);
				LOG.info("RescheduleScanJob");
			} catch (SchedulerException e) {
				LOG.error("Error reschedule scan job", e);
			}
		}
		String newDataInterval = event.getParam(DATA_INTERVAL);
		if (newDataInterval != null && newDataInterval.length() > 0
				&& !newDataInterval.equals(scanInterval)) {
			dataInterval = newDataInterval;
			try {
				if(getScheduler().checkExists(gableJobKey)){
					rescheduleJobNow(gableTK, dataInterval);	
				}
				LOG.info("RescheduleGableJob");
			} catch (SchedulerException e) {
				LOG.error("Error reschedule gable job", e);
			}
		}
		Boolean newAuto = event.getParam(AUTO);
		if (newAuto != null && !newAuto.equals(auto)) {
			auto = newAuto;
			if (auto) {
				try {
					if(!getScheduler().checkExists(gableJobKey)){
						startGableJob();	
					}
				} catch (SchedulerException e) {
					LOG.error("Error checkExists gable job", e);
				}
			} else {
				try {
					resumeJobNow(gableJobKey);
				} catch (SchedulerException e) {
					LOG.error("Error resum gable job", e);
				}
			}
		}
	}

}
