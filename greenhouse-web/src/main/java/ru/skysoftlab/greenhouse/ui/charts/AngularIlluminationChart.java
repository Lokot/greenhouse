package ru.skysoftlab.greenhouse.ui.charts;

import java.io.IOException;
import java.net.URL;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.arduino.IArduino;
import ru.skysoftlab.greenhouse.common.AbstractChartBean;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@RequestScoped
public class AngularIlluminationChart extends AbstractChartBean {

	private static final long serialVersionUID = 7259550413022270923L;

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
			URL url = Resources.getResource("charts/IllumChart.js");
			String options = Resources.toString(url, Charsets.UTF_8).replaceAll("ILLUM_NOW",
					String.valueOf(arduino.getIllumination()));
			return options;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String updateValue() {
		String rv = "chart.series[0].update({data: [" + String.valueOf(arduino.getIllumination()) + "]});";
		return rv;
	}

}
