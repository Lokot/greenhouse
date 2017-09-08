package ru.skysoftlab.greenhouse.ui.charts;

import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_1;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_2;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_MAX;
import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.TEMP_MIN;

import java.io.IOException;
import java.net.URL;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.arduino.IArduino;
import ru.skysoftlab.greenhouse.common.AbstractChartBean;
import ru.skysoftlab.skylibs.annatations.AppProperty;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@RequestScoped
public class AngularTemperatureChart extends AbstractChartBean {

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
	private IArduino arduino;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getOptions()
	 */
	@Override
	protected String getOptions() {
		try {
			URL url = Resources.getResource("charts/tempChart.js");
			String options = Resources.toString(url, Charsets.UTF_8).replaceAll("TEMP_MIN", tempMin.toString())
					.replaceAll("TEMP_1", temp1.toString()).replaceAll("TEMP_2", temp2.toString())
					.replaceAll("TEMP_MAX", tempMax.toString())
					.replaceAll("TEMP_NOW", arduino.getTemperature().toString());
			return options;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String updateValue() {
		String rv = "chart.series[0].update({data: [" + arduino.getTemperature().toString() + "]});";
		return rv;
	}

}
