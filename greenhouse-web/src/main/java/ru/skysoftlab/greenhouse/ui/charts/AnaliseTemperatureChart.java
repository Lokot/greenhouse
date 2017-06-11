package ru.skysoftlab.greenhouse.ui.charts;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ru.skysoftlab.greenhouse.common.AbstractChartBean;
import ru.skysoftlab.greenhouse.dto.ReadOutDto;
import ru.skysoftlab.greenhouse.impl.DataBaseProvider;
import ru.skysoftlab.greenhouse.ui.MainView;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisType;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisValueType;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.MultipleAxis;
import at.downdrown.vaadinaddons.highchartsapi.model.data.base.StringNomberData;
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.SplineChartPlotOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.series.HighChartsSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.SplineChartSeries;

public class AnaliseTemperatureChart extends AbstractChartBean {

	private static final long serialVersionUID = -7850512294106721583L;

	@Inject
	private DataBaseProvider dataBaseProvider;

	private Date date = new Date();

	private AnaliseChartType type = AnaliseChartType.Year;

	@Override
	public ChartConfiguration getConfig() {
		Axis xAxis = new Axis(AxisType.xAxis);
		xAxis.setTitle("Время");
		xAxis.setAxisValueType(AxisValueType.CATEGORY);
		MultipleAxis yAx = new MultipleAxis(AxisType.yAxis);
		Axis yTempAxis = new Axis(AxisType.yAxis);
		yTempAxis.setTitle("Температура (°C)");
		yAx.addAxis(yTempAxis);
		Axis yHumAxis = new Axis(AxisType.yAxis);
		yHumAxis.setTitle("Влажность (%)");
		yHumAxis.setOpposite(true);
		yAx.addAxis(yHumAxis);
		Axis yIllumAxis = new Axis(AxisType.yAxis);
		yIllumAxis.setTitle("Освещенность");
		yAx.addAxis(yIllumAxis);
		ChartConfiguration lineConfiguration = new ChartConfiguration();
//		SplineChartPlotOptions splineChartPlotOptions = new SplineChartPlotOptions();
//		splineChartPlotOptions.setMarker(false);
//		splineChartPlotOptions.setDataLabelsEnabled(false);
//		lineConfiguration.setPlotOptions(splineChartPlotOptions);
		switch (type) {
		case Month:
			lineConfiguration.setTitle("Показатели за месяц");
			lineConfiguration.setSubTitle(new SimpleDateFormat("MMM YYYY")
					.format(date));
			break;

		case Year:
		default:
			lineConfiguration.setTitle("Показатели за год");
			lineConfiguration.setSubTitle(new SimpleDateFormat("YYYY")
					.format(date));
			break;
		}
		lineConfiguration.setChartType(ChartType.SPLINE);
		lineConfiguration.setBackgroundColor(MainView.Gray98);
		lineConfiguration.setxAxis(xAxis);
		lineConfiguration.setyAxis(yAx);
		return lineConfiguration;
	}

	@Override
	protected List<HighChartsSeries> getSeries() {
		List<ReadOutDto> readouts = null;
		switch (type) {
		case Month:
			readouts = dataBaseProvider.getMonthTemp(date);
			break;

		case Year:
		default:

			readouts = dataBaseProvider.getYearTemp(date);
			break;
		}

		List<HighChartsSeries> rv = new ArrayList<>();
		SplineChartSeries tempSer = new SplineChartSeries("Температура");
		SplineChartSeries humSer = new SplineChartSeries("Влажность");
		SplineChartSeries illumSer = new SplineChartSeries("Освещенность");
		tempSer.setyAxis(0);
		humSer.setyAxis(1);
		illumSer.setyAxis(2);
		for (ReadOutDto readout : readouts) {
			tempSer.addData(new StringNomberData(getValueName(readout
					.getDayOrMonth()), readout.getTemperature()));
			humSer.addData(new StringNomberData(getValueName(readout
					.getDayOrMonth()), readout.getHumidity()));
			illumSer.addData(new StringNomberData(getValueName(readout
					.getDayOrMonth()), readout.getIllumination()));
		}
		rv.add(tempSer);
		rv.add(humSer);
		rv.add(illumSer);
		return rv;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setType(AnaliseChartType type) {
		this.type = type;
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
