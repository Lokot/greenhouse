package ru.skysoftlab.greenhouse.common;

import java.io.Serializable;

import ru.skysoftlab.skylibs.vaadin.highcharts.HighChart;

/**
 * Интерфейс бина с графиком.
 * 
 * @author Lokot
 *
 */
public interface IHasChart extends Serializable {

	/**
	 * Возвращает график.
	 * 
	 * @return
	 */
	public HighChart getChart();
}
