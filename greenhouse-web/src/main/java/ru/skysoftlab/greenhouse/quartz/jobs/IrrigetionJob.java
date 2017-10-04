package ru.skysoftlab.greenhouse.quartz.jobs;

import javax.inject.Inject;

import org.apache.openejb.quartz.Job;
import org.apache.openejb.quartz.JobExecutionContext;
import org.apache.openejb.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;

public class IrrigetionJob implements Job {

	private Logger LOG = LoggerFactory.getLogger(IrrigetionJob.class);

	public static final String CONTUR = "irrigationCountur";

	@Inject
	private IController arduino;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		IrrigationCountur irrigationCountur = (IrrigationCountur) context.getJobDetail().getJobDataMap().get(CONTUR);
		LOG.info("Open irrigation countur - " + irrigationCountur.getName());
		arduino.openIrrigationCountur(irrigationCountur.getPin());
		try {
			Thread.sleep(irrigationCountur.getDuration().getMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOG.info("Close irrigation countur - " + irrigationCountur.getName());
		arduino.closeIrrigationCountur(irrigationCountur.getPin());
	}

}
