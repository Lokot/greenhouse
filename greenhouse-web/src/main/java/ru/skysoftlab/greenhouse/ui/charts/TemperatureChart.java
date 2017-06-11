package ru.skysoftlab.greenhouse.ui.charts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

import ru.skysoftlab.greenhouse.common.AbstractChartBean;
import ru.skysoftlab.greenhouse.impl.DataBaseProvider;
import ru.skysoftlab.greenhouse.jpa.entitys.Readout;
import ru.skysoftlab.greenhouse.ui.MainView;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisType;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisValueType;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.MultipleAxis;
import at.downdrown.vaadinaddons.highchartsapi.model.data.base.TimeNomberData;
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.SplineChartPlotOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.series.HighChartsSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.SplineChartSeries;

public class TemperatureChart extends AbstractChartBean {

	private static final long serialVersionUID = -9040087776384049197L;

	@Inject
	private DataBaseProvider dataBaseProvider;

	private Date date = new Date();

	@Override
	public ChartConfiguration getConfig() {
		Axis xAxis = new Axis(AxisType.xAxis);
		xAxis.setTitle("Время");
		xAxis.setAxisValueType(AxisValueType.DATETIME);
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
		Axis yGableAxis = new Axis(AxisType.yAxis);
		yGableAxis.setTitle("Состояние конька");
		yGableAxis.setOpposite(true);
		yAx.addAxis(yGableAxis);
		ChartConfiguration lineConfiguration = new ChartConfiguration();
		lineConfiguration.setTitle("Температура за сутки");
		lineConfiguration.setSubTitle(new SimpleDateFormat("dd MMM YYYY")
				.format(date));
		lineConfiguration.setChartType(ChartType.SPLINE);
		SplineChartPlotOptions splineChartPlotOptions = new SplineChartPlotOptions();
		splineChartPlotOptions.setMarker(false);
		splineChartPlotOptions.setDataLabelsEnabled(false);
		lineConfiguration.setPlotOptions(splineChartPlotOptions);
		lineConfiguration.setBackgroundColor(MainView.Gray98);
		lineConfiguration.setxAxis(xAxis);
		lineConfiguration.setyAxis(yAx);
		return lineConfiguration;
	}

	@Override
	protected List<HighChartsSeries> getSeries() {
		List<Readout> readouts = dataBaseProvider.getDateTemp(date);
		List<HighChartsSeries> rv = new ArrayList<>();
		SplineChartSeries tempSer = new SplineChartSeries("Температура");
		SplineChartSeries tempMax = new SplineChartSeries("T Max");
		SplineChartSeries temp2 = new SplineChartSeries("T2");
		SplineChartSeries temp1 = new SplineChartSeries("T1");
		SplineChartSeries tempMin = new SplineChartSeries("T Min");
		SplineChartSeries humSer = new SplineChartSeries("Влажность");
		SplineChartSeries humMax = new SplineChartSeries("Влажность Max");
		SplineChartSeries illumSer = new SplineChartSeries("Освещенность");
		SplineChartSeries gableSer = new SplineChartSeries("Конек");
		gableSer.setType(ChartType.COLUMN);
		tempSer.setyAxis(0);
		tempMax.setyAxis(0);
		tempMax.setVisible(false);
		temp2.setyAxis(0);
		temp2.setVisible(false);
		temp1.setyAxis(0);
		temp1.setVisible(false);
		tempMin.setyAxis(0);
		tempMin.setVisible(false);
		humSer.setyAxis(1);
		humMax.setyAxis(1);
		humMax.setVisible(false);
		illumSer.setyAxis(2);
		gableSer.setyAxis(3);
		for (Readout readout : readouts) {
			tempSer.addData(getTimeNomberData(readout.getTime(),
					readout.getTemperature()));
			tempMax.addData(getTimeNomberData(readout.getTime(), readout
					.getConfig().getTempMax()));
			temp2.addData(getTimeNomberData(readout.getTime(), readout
					.getConfig().getTemp2()));
			temp1.addData(getTimeNomberData(readout.getTime(), readout
					.getConfig().getTemp1()));
			tempMin.addData(getTimeNomberData(readout.getTime(), readout
					.getConfig().getTempMin()));
			humSer.addData(getTimeNomberData(readout.getTime(),
					readout.getHumidity()));
			humMax.addData(getTimeNomberData(readout.getTime(), readout
					.getConfig().getHumMax()));
			illumSer.addData(getTimeNomberData(readout.getTime(),
					readout.getIllumination()));
			gableSer.addData(getTimeNomberData(readout.getTime(), readout
					.getGableState().toInt()));
		}
		rv.add(gableSer);
		rv.add(tempMax);
		rv.add(temp2);
		rv.add(temp1);
		rv.add(tempMin);
		rv.add(humMax);
		rv.add(tempSer);
		rv.add(humSer);
		rv.add(illumSer);
		return rv;
	}

	private TimeNomberData getTimeNomberData(LocalTime time, float val) {
		return new TimeNomberData(time.toDateTimeToday(
				DateTimeZone.forOffsetHours(0)).getMillis(), val);
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
