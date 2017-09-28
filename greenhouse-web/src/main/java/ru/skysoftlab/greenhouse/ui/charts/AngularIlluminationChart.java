package ru.skysoftlab.greenhouse.ui.charts;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.skylibs.annatations.ResourcePropetry;
import ru.skysoftlab.skylibs.web.ui.highcharts.AbstractChartBean;
import ru.skysoftlab.skylibs.web.ui.highcharts.UpdatedChart;

@RequestScoped
public class AngularIlluminationChart extends AbstractChartBean implements UpdatedChart {

	private static final long serialVersionUID = 7259550413022270923L;

	@Inject
	private IController arduino;

	@Inject
	@ResourcePropetry("charts/IllumChart.js")
	private String chartOptions;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getOptions()
	 */
	@Override
	protected String getOptions() {
		return chartOptions.replaceAll("ILLUM_NOW", String.valueOf(arduino.getIllumination()));
	}

	@Override
	public String updateValue() {
		return "chart.series[0].update({data: [" + String.valueOf(arduino.getIllumination()) + "]});";
	}

	@Override
	public String updateValue(Object val) {
		return "chart.series[0].update({data: [" + val.toString() + "]});";
	}

}
