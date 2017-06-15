package ru.skysoftlab.greenhouse.ui.charts;

import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_1;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_2;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_MAX;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_MIN;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.arduino.IArduino;
import ru.skysoftlab.greenhouse.common.AbstractChartBean;
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
public class AngularTemperatureChart extends AbstractChartBean {

	private static final long serialVersionUID = 7259550413022270923L;

	private final int minScale = 10;
	private final int maxScale = 45;

	@Inject
	@AppProperty(TEMP_MAX)
	private Float tempMax;

	@Inject
	@AppProperty(TEMP_2)
	private Float temp2;

	@Inject
	@AppProperty(TEMP_1)
	private Float temp1;

	@Inject
	@AppProperty(TEMP_MIN)
	private Float tempMin;
	
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
		yTempAxis.setTitle("°C");
		yTempAxis.setMin(minScale);
		yTempAxis.setMax(maxScale);
		yTempAxis.addPlotBand(minScale, tempMin, Colors.BLUE);
		yTempAxis.addPlotBand(tempMin, temp1, Colors.GREEN);
		yTempAxis.addPlotBand(temp1, temp2, Colors.GREENYELLOW);
		yTempAxis.addPlotBand(temp2, tempMax, Colors.YELLOW);
		yTempAxis.addPlotBand(tempMax, maxScale, Colors.RED);

		ChartConfiguration lineConfiguration = new ChartConfiguration();
		lineConfiguration.setPaneOptions(new PaneOptions(-150, 150));
		lineConfiguration.setTitle("Температура (°C)");
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
		GuageChartSeries rv = new GuageChartSeries("Температура");
		rv.addData(arduino.getTemperature());
		return Arrays.asList(rv);
	}

}
