package ru.skysoftlab.greenhouse.common;

import javax.annotation.PostConstruct;

import ru.skysoftlab.skylibs.vaadin.highcharts.HighChart;

/**
 * Абстрактная реализация бина с графиком.
 * 
 * @author Lokot
 *
 */
public abstract class AbstractChartBean implements IHasChart {

	private static final long serialVersionUID = 5218321154702350055L;

	private HighChart chart = new HighChart();

	@PostConstruct
	private void init() {
		chart.setHcjs(getOptions());
	}

	protected abstract String getOptions();

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.IHasChart#getChart()
	 */
	@Override
	public HighChart getChart() {
		return chart;
	}

	/**
	 * Устанавливает высоту и ширину графика.
	 * 
	 * @param chart
	 */
	protected void setHW(HighChart chart) {
		// chart.setHeight(80, Unit.PERCENTAGE);
		// chart.setWidth(80, Unit.PERCENTAGE);
	}

	public String getSeries() {
		return getOptions();
	}

}
