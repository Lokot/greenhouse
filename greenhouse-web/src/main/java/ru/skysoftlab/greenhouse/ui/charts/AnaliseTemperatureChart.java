package ru.skysoftlab.greenhouse.ui.charts;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ru.skysoftlab.greenhouse.dto.ReadOutDto;
import ru.skysoftlab.greenhouse.impl.DataBaseProvider;
import ru.skysoftlab.skylibs.annatations.ResourcePropetry;
import ru.skysoftlab.skylibs.web.ui.highcharts.AbstractChartBean;

/**
 * График анализа показателей за месяц или год.
 * 
 * @author Lokot
 *
 */
public class AnaliseTemperatureChart extends AbstractChartBean {

	private static final long serialVersionUID = -7850512294106721583L;

	private Date date = new Date();

	private AnaliseChartType type = AnaliseChartType.Year;

	@Inject
	private DataBaseProvider dataBaseProvider;

	@Inject
	@ResourcePropetry("charts/analiseChart.js")
	private String chartOptions;

	@Override
	protected String getOptions() {
		String rv = chartOptions;
		StringBuilder illum = new StringBuilder(), temp = new StringBuilder(), hum = new StringBuilder();
		String title, subtitle;
		List<ReadOutDto> readouts = null;

		switch (type) {
		case Month:
			title = "Показатели за месяц";
			subtitle = new SimpleDateFormat("MMM YYYY").format(date);
			readouts = dataBaseProvider.getMonthTemp(date);
			break;

		case Year:
		default:
			title = "Показатели за год";
			subtitle = new SimpleDateFormat("YYYY").format(date);
			readouts = dataBaseProvider.getYearTemp(date);
			break;
		}

		int count = 1;
		for (ReadOutDto readout : readouts) {
			if (count > 1) {
				temp.append(",");
				hum.append(",");
				illum.append(",");
			}
			temp.append(getTimeNomberData(readout.getDayOrMonth(), readout.getTemperature()));
			hum.append(getTimeNomberData(readout.getDayOrMonth(), readout.getHumidity()));
			illum.append(getTimeNomberData(readout.getDayOrMonth(), readout.getIllumination()));
			count++;
		}

		rv = rv.replaceAll("CHART_TITLE", title);
		rv = rv.replaceAll("CHART_SUBTITLE", subtitle);
		rv = rv.replaceAll("TEMP", temp.toString());
		rv = rv.replaceAll("HUM", hum.toString());
		rv = rv.replaceAll("ILLUM", illum.toString());
		return rv;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setType(AnaliseChartType type) {
		this.type = type;
	}

	private String getTimeNomberData(int time, float val) {
		return "['" + getValueName(time) + "'," + val + "]";
	}

	public String getValueName(Integer monthOrDay) {
		switch (type) {
		case Month:
			return monthOrDay + " " + new SimpleDateFormat("MMM").format(date);

		case Year:
		default:
			return new DateFormatSymbols().getShortMonths()[monthOrDay - 1];
		}
	}

	public enum AnaliseChartType {
		Year, Month;
	}
}
