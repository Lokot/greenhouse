package ru.skysoftlab.greenhouse.common;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.series.HighChartsSeries;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * Абстрактная реализация бина с графиком.
 * 
 * @author Lokot
 *
 */
public abstract class AbstractChartBean implements IHasChart {

	private static final long serialVersionUID = 5218321154702350055L;

	private Logger LOG = LoggerFactory.getLogger(AbstractChartBean.class);

	private HighChart lineChart = new HighChart();

	@PostConstruct
	private void init() {
		try {
			ChartConfiguration lineConfiguration = getConfig();
			lineConfiguration.getSeriesList().addAll(getSeries());
			lineChart.setChartoptions(lineConfiguration.getHighChartValue());
			lineChart.setChartConfiguration(lineConfiguration);
			setHW(lineChart);
		} catch (HighChartsException e) {
			LOG.error("Ошибка сериализации конфигурации графика.", e);
			Notification.show(e.getMessage(), Type.TRAY_NOTIFICATION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.IHasChart#getChart()
	 */
	@Override
	public HighChart getChart() {
		return lineChart;
	}

	/**
	 * Возвращает конфигурацию графика.
	 * 
	 * @return
	 */
	public abstract ChartConfiguration getConfig();

	public ChartConfiguration getConfigWithSeries(){
		ChartConfiguration rv = getConfig();
		rv.getSeriesList().addAll(getSeries());
		return rv;
	}
	
	
	/**
	 * Возвращает значения для осей графика.
	 * 
	 * @return
	 */
	protected abstract List<HighChartsSeries> getSeries();

	/**
	 * Устанавливает высоту и ширину графика.
	 * 
	 * @param chart
	 */
	protected void setHW(HighChart chart) {
		chart.setHeight(80, Unit.PERCENTAGE);
		chart.setWidth(80, Unit.PERCENTAGE);
	}

	public void redraw() {
		try {
			lineChart.redraw(getConfig());
		} catch (HighChartsException e) {
			LOG.error("Ошибка отображения графика.", e);
			Notification.show(e.getMessage(), Type.TRAY_NOTIFICATION);
		}
	}

}
