package ru.skysoftlab.greenhouse.ui.charts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

import ru.skysoftlab.greenhouse.impl.DataBaseProvider;
import ru.skysoftlab.greenhouse.jpa.entitys.Readout;
import ru.skysoftlab.skylibs.annatations.ResourcePropetry;
import ru.skysoftlab.skylibs.web.ui.highcharts.AbstractChartBean;

/**
 * График показателей за сутки.
 * 
 * @author Lokot
 *
 */
public class TemperatureChart extends AbstractChartBean {

	private static final long serialVersionUID = -9040087776384049197L;

	@Inject
	private DataBaseProvider dataBaseProvider;

	@Inject
	@ResourcePropetry("charts/mainTempChart.js")
	private String chartOptions;

	private Date date = new Date();

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getOptions()
	 */
	@Override
	protected String getOptions() {
		StringBuilder tempSer = new StringBuilder(), tempMax = new StringBuilder(), temp2 = new StringBuilder();
		StringBuilder temp1 = new StringBuilder(), tempMin = new StringBuilder(), humSer = new StringBuilder();
		StringBuilder humMax = new StringBuilder(), illumSer = new StringBuilder(), gableSer = new StringBuilder();
		List<Readout> readouts = dataBaseProvider.getDateTemp(date);
		int count = 1;
		for (Readout readout : readouts) {
			if (count > 1) {
				tempSer.append(",");
				tempMax.append(",");
				temp2.append(",");
				temp1.append(",");
				tempMin.append(",");
				humSer.append(",");
				humMax.append(",");
				illumSer.append(",");
				gableSer.append(",");
			}
			tempSer.append(getTimeNomberData(readout.getTime(), readout.getTemperature()));
			tempMax.append(getTimeNomberData(readout.getTime(), readout.getConfig().getTempMax()));
			temp2.append(getTimeNomberData(readout.getTime(), readout.getConfig().getTemp2()));
			temp1.append(getTimeNomberData(readout.getTime(), readout.getConfig().getTemp1()));
			tempMin.append(getTimeNomberData(readout.getTime(), readout.getConfig().getTempMin()));
			humSer.append(getTimeNomberData(readout.getTime(), readout.getHumidity()));
			humMax.append(getTimeNomberData(readout.getTime(), readout.getConfig().getHumMax()));
			illumSer.append(getTimeNomberData(readout.getTime(), readout.getIllumination()));
			gableSer.append(getTimeNomberData(readout.getTime(), readout.getGableState().toInt()));
			count++;
		}

		String options = chartOptions.replaceAll("DATE", new SimpleDateFormat("dd MMM YYYY").format(date));
		options = options.replaceAll("TEMP_SER", tempSer.toString());
		options = options.replaceAll("TEMP_MAX", tempMax.toString());
		options = options.replaceAll("TEMP_2", temp2.toString());
		options = options.replaceAll("TEMP_1", temp1.toString());
		options = options.replaceAll("TEMP_MIN", tempMin.toString());
		options = options.replaceAll("HUM_SER", humSer.toString());
		options = options.replaceAll("HUM_MAX", humMax.toString());
		options = options.replaceAll("ILLUM_SER", illumSer.toString());
		options = options.replaceAll("GABLE_SER", gableSer.toString());
		return options;
	}

	private String getTimeNomberData(LocalTime time, float val) {
		return "[" + time.toDateTimeToday(DateTimeZone.forOffsetHours(0)).getMillis() + "," + val + "]";
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
