package ru.skysoftlab.greenhouse.ui.charts;

import java.io.IOException;
import java.net.URL;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.common.AbstractChartBean;
import ru.skysoftlab.greenhouse.common.GableState;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@RequestScoped
public class SolidGaugeGableStateChart extends AbstractChartBean {

	private static final long serialVersionUID = 7259550413022270923L;

	@Inject
	private GableState stateNow;

	@Override
	protected String getOptions() {
		try {
			URL url = Resources.getResource("charts/gableChart.js");
			String options = Resources.toString(url, Charsets.UTF_8).replaceAll("GABLE_NOW",
					String.valueOf(stateNow.toInt()));
			return options;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String updateValue() {
		String rv = "chart.series[0].update({data: [" + String.valueOf(stateNow.toInt()) + "]});";
		return rv;
	}

}
