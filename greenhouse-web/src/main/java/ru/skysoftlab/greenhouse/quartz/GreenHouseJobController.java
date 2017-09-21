package ru.skysoftlab.greenhouse.quartz;

import static ru.skysoftlab.greenhouse.common.ConfigurationNames.AUTO;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.DATA_INTERVAL;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.SCAN_INTERVAL;
import static ru.skysoftlab.greenhouse.quartz.jobs.IrrigetionJob.CONTUR;

import java.util.List;

import javax.ejb.Local;
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

import ru.skysoftlab.greenhouse.impl.DataBaseProvider;
import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;
import ru.skysoftlab.greenhouse.quartz.jobs.GableJob;
import ru.skysoftlab.greenhouse.quartz.jobs.IrrigetionJob;
import ru.skysoftlab.greenhouse.quartz.jobs.ScanTempJob;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.common.EditableEntityState;
import ru.skysoftlab.skylibs.events.ConfigurationListener;
import ru.skysoftlab.skylibs.events.EntityChangeEvent;
import ru.skysoftlab.skylibs.events.EntityChangeListener;
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
@Local({ConfigurationListener.class, EntityChangeListener.class})
public class GreenHouseJobController extends JobController implements ConfigurationListener, EntityChangeListener {

	private Logger LOG = LoggerFactory.getLogger(GreenHouseJobController.class);

	private static final String SYSTEM_GROUP = "system-jobs";

	private final TriggerKey tempTK = TriggerKey.triggerKey("scan-temp-startUp", SYSTEM_GROUP);
	private final TriggerKey gableTK = TriggerKey.triggerKey("scan-gable-startUp", SYSTEM_GROUP);

	private final JobKey scanJobKey = JobKey.jobKey("scan-readout", SYSTEM_GROUP);
	private final JobKey gableJobKey = JobKey.jobKey("scan-gable", SYSTEM_GROUP);

	@Inject
	@AppProperty(SCAN_INTERVAL)
	private String scanInterval;

	@Inject
	@AppProperty(DATA_INTERVAL)
	private String dataInterval;

	@Inject
	@AppProperty(AUTO)
	private Boolean auto;

	@Inject
	private DataBaseProvider baseProvider;

	@Override
	protected void startJobs() {
		startScanTempJob();
		if (auto) {
			startGableJob();
		}
		startIrrigationJobs();
	}

	private void startScanTempJob() {
		try {
			JobDetail scanJob = JobBuilder.newJob(ScanTempJob.class).withIdentity(scanJobKey).build();
			Trigger trigger = createCronTrigger(tempTK, scanInterval, null);
			getScheduler().scheduleJob(scanJob, trigger);
		} catch (SchedulerException e) {
			LOG.error("Error create scan temp job", e);
		}
	}

	private void startGableJob() {
		try {
			JobDetail scanJob = JobBuilder.newJob(GableJob.class).withIdentity(gableJobKey).build();
			Trigger trigger = createCronTrigger(gableTK, dataInterval, null);
			getScheduler().scheduleJob(scanJob, trigger);
		} catch (SchedulerException e) {
			LOG.error("Error create scan temp job", e);
		}
	}

	private void startIrrigationJobs() {
		List<IrrigationCountur> counturs = baseProvider.getIrigationCounters();
		for (IrrigationCountur iCountur : counturs) {
			startIrrigationJob(iCountur);
		}
	}

	private void startIrrigationJob(IrrigationCountur iCountur) {
		try {
			JobDetail irrigationJob = createIrragationJobDetail(getKeyIrragationJob(iCountur.getId()), iCountur);
			Trigger trigger = createCronTrigger(getKeyIrragationTrigger(iCountur.getId()), iCountur.getCronExpr(),
					null);
			getScheduler().scheduleJob(irrigationJob, trigger);
		} catch (SchedulerException e) {
			LOG.error("Error create scan temp job", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.skylibs.events.ConfigurationListener#editIntervalEvent(
	 * ru.skysoftlab.skylibs.events.SystemConfigEvent)
	 */
	@Override
	public void configUpdated(@Observes SystemConfigEvent event) {
		updateScanIntervalParam(event);
		updateDataIntervalParam(event);
		updateAutoParam(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.skysoftlab.skylibs.events.EntityChangeListener#entityChange(ru.skysoftlab.
	 * skylibs.events.EntityChangeEvent)
	 */
	@Override
	public void entityChange(@Observes EntityChangeEvent event) {
		if (event.getEntityClass().equals(IrrigationCountur.class)) {
			JobKey jobKey = getKeyIrragationJob(event.getId());
			if (event.getState().equals(EditableEntityState.DELETE)) {
				try {
					if (getScheduler().checkExists(jobKey)) {
						deleteJobNow(jobKey);
					}
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			} else {
				IrrigationCountur countur = baseProvider.getIrrigationCountur(event.getId());
				try {
					if (getScheduler().checkExists(jobKey)) {
						// update
						deleteJobNow(jobKey);
					}
					if (countur.getRun()) {
						// new
						startIrrigationJob(countur);
					}
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Обновляет интервал сканирования, если был изменен.
	 * 
	 * @param event
	 */
	private void updateScanIntervalParam(SystemConfigEvent event) {
		String newScanInterval = event.getParam(SCAN_INTERVAL);
		if (newScanInterval != null && newScanInterval.length() > 0 && !newScanInterval.equals(scanInterval)) {
			scanInterval = newScanInterval;
			try {
				rescheduleJobNow(tempTK, scanInterval);
				LOG.info("RescheduleScanJob");
			} catch (SchedulerException e) {
				LOG.error("Error reschedule scan job", e);
			}
		}
	}

	/**
	 * Обновляет интервал сканирования, если был изменен.
	 * 
	 * @param event
	 */
	private void updateDataIntervalParam(SystemConfigEvent event) {
		String newDataInterval = event.getParam(DATA_INTERVAL);
		if (newDataInterval != null && newDataInterval.length() > 0 && !newDataInterval.equals(scanInterval)) {
			dataInterval = newDataInterval;
			try {
				if (getScheduler().checkExists(gableJobKey)) {
					rescheduleJobNow(gableTK, dataInterval);
				}
				LOG.info("RescheduleGableJob");
			} catch (SchedulerException e) {
				LOG.error("Error reschedule gable job", e);
			}
		}
	}

	/**
	 * Обновляет параметр автоуправления коньком.
	 * 
	 * @param event
	 */
	private void updateAutoParam(SystemConfigEvent event) {
		Boolean newAuto = event.getParam(AUTO);
		if (newAuto != null && !newAuto.equals(auto)) {
			auto = newAuto;
			if (auto) {
				try {
					if (!getScheduler().checkExists(gableJobKey)) {
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

	private JobKey getKeyIrragationJob(Object id) {
		return JobKey.jobKey("irrigation-" + id, SYSTEM_GROUP);
	}

	private TriggerKey getKeyIrragationTrigger(Object id) {
		return TriggerKey.triggerKey("irrigation-startUp-" + id, SYSTEM_GROUP);
	}

	private JobDetail createIrragationJobDetail(JobKey jobKey, IrrigationCountur irrigationCountur) {
		JobDetail jobDetail = JobBuilder.newJob(IrrigetionJob.class).withIdentity(jobKey).build();
		jobDetail.getJobDataMap().put(CONTUR, irrigationCountur);
		return jobDetail;
	}

}
