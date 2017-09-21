package ru.skysoftlab.greenhouse.ui.charts;

import static ru.skysoftlab.greenhouse.common.ConfigurationNames.HUM_MAX;

import java.io.IOException;
import java.net.URL;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.common.AbstractChartBean;
import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.skylibs.annatations.AppProperty;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@RequestScoped
public class AngularHumChart extends AbstractChartBean {

	private static final long serialVersionUID = 7259550413022270923L;

	@Inject
	@AppProperty(HUM_MAX)
	private Float humMax;

	@Inject
	private IController arduino;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getOptions()
	 */
	@Override
	protected String getOptions() {
		try {
			URL url = Resources.getResource("charts/humChart.js");
			String options = Resources.toString(url, Charsets.UTF_8).replaceAll("HUM_MAX", humMax.toString())
					.replaceAll("HUM_NOW", arduino.getHumidity().toString());
			return options;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String updateValue() {
		String rv = "chart.series[0].update({data: [" + arduino.getHumidity().toString() + "]});";
		return rv;
	}

}
