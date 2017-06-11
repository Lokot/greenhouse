package ru.skysoftlab.greenhouse.ui.charts;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.common.AbstractChartBean;
import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.ui.MainView;
import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisType;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.GaugeAxis;
import at.downdrown.vaadinaddons.highchartsapi.model.PaneOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.series.GuageChartSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.HighChartsSeries;

@RequestScoped
public class SolidGaugeGableStateChart extends AbstractChartBean {

	private static final long serialVersionUID = 7259550413022270923L;

	private final int minScale = 0;
	private final int maxScale = 100;

	@Inject
	private GableState stateNow;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getConfig()
	 */
	@Override
	public ChartConfiguration getConfig() {
		GaugeAxis yTempAxis = new GaugeAxis(AxisType.yAxis);
		yTempAxis.setTitle("%");
		yTempAxis.setMin(minScale);
		yTempAxis.setMax(maxScale);
		yTempAxis.addStop(0.3f, Colors.GREEN);
		yTempAxis.addStop(0.6f, Colors.YELLOW);
		yTempAxis.addStop(1.0f, Colors.RED);

		ChartConfiguration lineConfiguration = new ChartConfiguration();
		lineConfiguration.setPaneOptions(new PaneOptions(-150, 150));
		lineConfiguration.setTitle("Открытие конька (%)");
		lineConfiguration.setChartType(ChartType.GAUGE);
		lineConfiguration.setBackgroundColor(MainView.Gray98);
		lineConfiguration.setyAxis(yTempAxis);
		return lineConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.skysoftlab.greenhouse.common.AbstractChartBean#getSeries()
	 */
	@Override
	protected List<HighChartsSeries> getSeries() {
		GuageChartSeries rv = new GuageChartSeries("Конек");
		rv.addData(stateNow.toInt());
		return Arrays.asList(rv);
	}

}
