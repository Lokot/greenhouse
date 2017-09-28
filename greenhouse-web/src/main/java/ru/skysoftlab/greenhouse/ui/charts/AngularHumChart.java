package ru.skysoftlab.greenhouse.ui.charts;

import static ru.skysoftlab.greenhouse.common.ConfigurationNames.HUM_MAX;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.annatations.ResourcePropetry;
import ru.skysoftlab.skylibs.web.ui.highcharts.AbstractChartBean;
import ru.skysoftlab.skylibs.web.ui.highcharts.UpdatedChart;

@RequestScoped
public class AngularHumChart extends AbstractChartBean implements UpdatedChart {

	private static final long serialVersionUID = 7259550413022270923L;

	@Inject
	@AppProperty(HUM_MAX)
	private Float humMax;

	@Inject
	private IController arduino;

	@Inject
	@ResourcePropetry("charts/humChart.js")
	private String chartOptions;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getOptions()
	 */
	@Override
	protected String getOptions() {
		return chartOptions.replaceAll("HUM_MAX", humMax.toString()).replaceAll("HUM_NOW",
				arduino.getHumidity().toString());
	}

	@Override
	public String updateValue() {
		return "chart.series[0].update({data: [" + arduino.getHumidity().toString() + "]});";
	}

	@Override
	public String updateValue(Object val) {
		return "chart.series[0].update({data: [" + val.toString() + "]});";
	}

}
