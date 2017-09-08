package ru.skysoftlab.greenhouse.ui.charts;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import ru.skysoftlab.greenhouse.common.AbstractChartBean;
import ru.skysoftlab.greenhouse.dto.ReadOutDto;
import ru.skysoftlab.greenhouse.impl.DataBaseProvider;

/**
 * График анализа показателей за месяц или год.
 * 
 * @author Lokot
 *
 */
public class AnaliseTemperatureChart extends AbstractChartBean {

	private static final long serialVersionUID = -7850512294106721583L;

	@Inject
	private DataBaseProvider dataBaseProvider;

	private Date date = new Date();

	private AnaliseChartType type = AnaliseChartType.Year;

	@Override
	protected String getOptions() {

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

		try {
			URL url = Resources.getResource("charts/analiseChart.js");
			String options = Resources.toString(url, Charsets.UTF_8);
			options = options.replaceAll("CHART_TITLE", title);
			options = options.replaceAll("CHART_SUBTITLE", subtitle);
			options = options.replaceAll("TEMP", temp.toString());
			options = options.replaceAll("HUM", hum.toString());
			options = options.replaceAll("ILLUM", illum.toString());
			return options;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setType(AnaliseChartType type) {
		this.type = type;
	}

	private String getTimeNomberData(int time, float val) {
		return "[" + getValueName(time) + "," + val + "]";
	}

	public String getValueName(Integer monthOrDay) {
		switch (type) {
		case Month:
			return "Число: " + monthOrDay;

		case Year:
		default:
			return new DateFormatSymbols().getShortMonths()[monthOrDay - 1];
		}
	}

	public enum AnaliseChartType {
		Year, Month;
	}
}
