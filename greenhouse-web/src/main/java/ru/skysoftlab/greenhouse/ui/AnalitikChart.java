package ru.skysoftlab.greenhouse.ui;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.ui.charts.AnaliseTemperatureChart;
import ru.skysoftlab.greenhouse.ui.charts.AnaliseTemperatureChart.AnaliseChartType;
import ru.skysoftlab.greenhouse.web.MainMenu;
import ru.skysoftlab.greenhouse.web.Navigation;
import ru.skysoftlab.skylibs.security.RolesList;
import ru.skysoftlab.skylibs.web.annatations.MainMenuItem;
import ru.skysoftlab.skylibs.web.dto.VaadinItemDto;
import ru.skysoftlab.skylibs.web.ui.BaseMenuView;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

@CDIView(Navigation.ANALIZ)
@MainMenuItem(name = "Аналитика", order = MainMenu.ANALIZ, hasChilds = false)
@RolesAllowed({ RolesList.USER, RolesList.ADMIN })
public class AnalitikChart extends BaseMenuView {

	private static final long serialVersionUID = -7698513842362840162L;
	
	@Inject
	private AnaliseTemperatureChart tchart;
	
	private OptionGroup dateType = new OptionGroup("Период анализа");
	private DateField date = new DateField();
	private HighChart chart;
	
	
	@Override
	protected void configureComponents() {
		dateType.addItems(new VaadinItemDto(true, "За год"), new VaadinItemDto(false,
				"За Месяц"));
		dateType.setValue(new VaadinItemDto(true, "За год"));
		dateType.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -5025316696883268535L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				VaadinItemDto item = (VaadinItemDto) ((OptionGroup) event.getProperty())
						.getValue();
				if(item.<Boolean>getObj()){
					date.setResolution(Resolution.YEAR);
					tchart.setType(AnaliseChartType.Year);
				} else {
					date.setResolution(Resolution.MONTH);
					tchart.setType(AnaliseChartType.Month);
				}
				redrawChart();
			}
		});
		
		date.setResolution(Resolution.YEAR);
		date.setValue(new Date());
		date.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -5911805285102919911L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				tchart.setDate(date.getValue());
				redrawChart();
			}
		});
		chart = tchart.getChart();
	}
	
	private void redrawChart(){
		try {
			chart.redraw(tchart.getConfigWithSeries());
		} catch (HighChartsException e) {
			Notification.show(e.getMessage(), Type.TRAY_NOTIFICATION);
		}
	}

	@Override
	protected void buildLayout() {
		VerticalLayout left = new VerticalLayout(dateType, date, chart);
		left.setSizeFull();
		left.setComponentAlignment(dateType, Alignment.TOP_CENTER);
		left.setComponentAlignment(date, Alignment.TOP_CENTER);
		left.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);

		HorizontalLayout mainLayout = new HorizontalLayout(left);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(left, 1);
		layout.addComponent(mainLayout);
	}

}
