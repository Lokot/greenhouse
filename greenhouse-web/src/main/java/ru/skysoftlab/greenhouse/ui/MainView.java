package ru.skysoftlab.greenhouse.ui;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.ui.charts.AngularHumChart;
import ru.skysoftlab.greenhouse.ui.charts.AngularIlluminationChart;
import ru.skysoftlab.greenhouse.ui.charts.AngularTemperatureChart;
import ru.skysoftlab.greenhouse.ui.charts.SolidGaugeGableStateChart;
import ru.skysoftlab.greenhouse.ui.charts.TemperatureChart;
import ru.skysoftlab.greenhouse.ui.components.GableStateSelector;
import ru.skysoftlab.greenhouse.web.MainMenu;
import ru.skysoftlab.skylibs.security.RolesList;
import ru.skysoftlab.skylibs.vaadin.highcharts.HighChart;
import ru.skysoftlab.skylibs.web.ui.BaseMenuView;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.UIEvents;
import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * График температур.
 * 
 * @author Артём
 *
 */
@CDIView(MainMenu.MAIN)
@RolesAllowed({ RolesList.GUEST, RolesList.USER, RolesList.ADMIN })
public class MainView extends BaseMenuView implements UIEvents.PollListener {

	private static final long serialVersionUID = 1307704684754077226L;

	public static final Color Gray98 = new Color(250, 250, 250);

	private DateField date = new DateField();
	private CheckBox updateMainChart = new CheckBox("Обновление графика", true);
	private HighChart lineChart;
	private HighChart angularTempChart;
	private HighChart angularHumChart;
	private HighChart angularIllumChart;
	private HighChart angularGableChart;
	@Inject
	private TemperatureChart mainChart;
	@Inject
	private AngularTemperatureChart tempChart;
	@Inject
	private AngularHumChart humChart;
	@Inject
	private AngularIlluminationChart illumChart;
	@Inject
	private SolidGaugeGableStateChart gableChart;
	@Inject
	private GableStateSelector selector;

	@Override
	protected void configureComponents() {
		getUI().setPollInterval(1000 * 60);
		getUI().addPollListener(this);
		date.setValue(new Date());
		date.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -5911805285102919911L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				mainChart.setDate(date.getValue());
				redrawMiain();
			}
		});
		lineChart = mainChart.getChart();
		angularTempChart = tempChart.getChart();
		angularHumChart = humChart.getChart();
		angularIllumChart = illumChart.getChart();
		angularGableChart = gableChart.getChart();
	}

	@Override
	protected void buildLayout() {

		HorizontalLayout bottomLayout = new HorizontalLayout();
		HighChart[] charts = new HighChart[] { angularTempChart, angularHumChart, angularIllumChart, angularGableChart };

		for (HighChart highChart : charts) {
			highChart.setHeight(50, Unit.PERCENTAGE);
			highChart.setWidth(80, Unit.PERCENTAGE);
			bottomLayout.addComponent(highChart);
		}
		bottomLayout.setSizeFull();

		VerticalLayout left = new VerticalLayout(selector, date, lineChart, updateMainChart, bottomLayout);
		left.setSizeFull();
		left.setComponentAlignment(selector, Alignment.TOP_CENTER);
		left.setComponentAlignment(date, Alignment.MIDDLE_CENTER);
		left.setComponentAlignment(lineChart, Alignment.MIDDLE_CENTER);
		left.setComponentAlignment(updateMainChart, Alignment.MIDDLE_CENTER);
		left.setComponentAlignment(bottomLayout, Alignment.BOTTOM_CENTER);

		HorizontalLayout mainLayout = new HorizontalLayout(left);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(left, 1);
		layout.addComponent(mainLayout);
	}

	private void redrawMiain() {
		lineChart.updateOptions(mainChart.getSeries());
	}

	private void redrawOths() {
		angularTempChart.manipulateChart(tempChart.updateValue());
		angularHumChart.manipulateChart(humChart.updateValue());
		angularIllumChart.manipulateChart(illumChart.updateValue());
		angularGableChart.manipulateChart(gableChart.updateValue());
	}

	@Override
	public void poll(PollEvent event) {
		if (updateMainChart.getValue()) {
			redrawMiain();
		}
		redrawOths();
	}

}
