package ru.skysoftlab.greenhouse.quartz.jobs;

import static ru.skysoftlab.greenhouse.impl.GrenHouseArduino.LOCK;

import javax.inject.Inject;

import org.apache.openejb.quartz.Job;
import org.apache.openejb.quartz.JobExecutionContext;
import org.apache.openejb.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.greenhouse.arduino.IArduino;
import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;

public class IrrigetionJob implements Job {

	private Logger LOG = LoggerFactory.getLogger(IrrigetionJob.class);

	public static final String CONTUR = "irrigationCountur";

	@Inject
	private IArduino arduino;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		IrrigationCountur irrigationCountur = (IrrigationCountur) context.getJobDetail().getJobDataMap().get(CONTUR);
		LOG.info("Open irrigation countur - " + irrigationCountur.getName());
		synchronized (LOCK) {
			arduino.openIrrigationCountur(irrigationCountur.getPin());
		}
		try {
			Thread.sleep(irrigationCountur.getDuration().getMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOG.info("Close irrigation countur - " + irrigationCountur.getName());
		synchronized (LOCK) {
			arduino.closeIrrigationCountur(irrigationCountur.getPin());
		}
	}

}
