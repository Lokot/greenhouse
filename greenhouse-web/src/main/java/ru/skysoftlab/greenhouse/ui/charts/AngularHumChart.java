package ru.skysoftlab.greenhouse.ui.charts;

import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.HUM_MAX;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.common.AbstractChartBean;
import ru.skysoftlab.greenhouse.impl.IArduino;
import ru.skysoftlab.greenhouse.ui.MainView;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisType;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.GaugeAxis;
import at.downdrown.vaadinaddons.highchartsapi.model.PaneOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.series.GuageChartSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.HighChartsSeries;

@RequestScoped
public class AngularHumChart extends AbstractChartBean {

	private static final long serialVersionUID = 7259550413022270923L;

	private final int minScale = 0;
	private final int maxScale = 100;

	@Inject
	@AppProperty(HUM_MAX)
	private Float humMax;
	
	@Inject
	private IArduino arduino;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getConfig()
	 */
	@Override
	public ChartConfiguration getConfig() {
		GaugeAxis yTempAxis = new GaugeAxis(AxisType.yAxis);
		yTempAxis.setTitle("%");
		yTempAxis.setMin(minScale);
		yTempAxis.setMax(maxScale);
		yTempAxis.addPlotBand(minScale, humMax, Colors.GREEN);
		yTempAxis.addPlotBand(humMax, maxScale, Colors.RED);

		ChartConfiguration lineConfiguration = new ChartConfiguration();
		lineConfiguration.setPaneOptions(new PaneOptions(-150, 150));
		lineConfiguration.setTitle("Влажность (%)");
		lineConfiguration.setChartType(ChartType.GAUGE);
		lineConfiguration.setBackgroundColor(MainView.Gray98);
		lineConfiguration.setyAxis(yTempAxis);
		return lineConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getSeries()
	 */
	@Override
	protected List<HighChartsSeries> getSeries() {
		GuageChartSeries rv = new GuageChartSeries("Влажность");
		rv.addData(arduino.getHumidity());
		return Arrays.asList(rv);
	}

}
