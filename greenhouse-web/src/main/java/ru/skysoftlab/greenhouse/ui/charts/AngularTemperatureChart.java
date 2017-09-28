package ru.skysoftlab.greenhouse.ui.charts;

import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_1;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_2;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_MAX;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_MIN;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.annatations.ResourcePropetry;
import ru.skysoftlab.skylibs.web.ui.highcharts.AbstractChartBean;
import ru.skysoftlab.skylibs.web.ui.highcharts.UpdatedChart;

@RequestScoped
public class AngularTemperatureChart extends AbstractChartBean implements UpdatedChart {

	private static final long serialVersionUID = 7259550413022270923L;

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
	private IController arduino;

	@Inject
	@ResourcePropetry("charts/tempChart.js")
	private String chartOptions;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getOptions()
	 */
	@Override
	protected String getOptions() {
		return chartOptions.replaceAll("TEMP_MIN", tempMin.toString()).replaceAll("TEMP_1", temp1.toString())
				.replaceAll("TEMP_2", temp2.toString()).replaceAll("TEMP_MAX", tempMax.toString())
				.replaceAll("TEMP_NOW", arduino.getTemperature().toString());
	}

	@Override
	public String updateValue() {
		return "chart.series[0].update({data: [" + arduino.getTemperature().toString() + "]});";
	}

	@Override
	public String updateValue(Object val) {
		return "chart.series[0].update({data: [" + val.toString() + "]});";
	}

}
