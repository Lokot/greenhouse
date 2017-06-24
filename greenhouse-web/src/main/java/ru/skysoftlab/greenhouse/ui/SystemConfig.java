package ru.skysoftlab.greenhouse.ui;

import java.text.ParseException;
import java.util.Locale;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import jssc.SerialPortList;
import net.redhogs.cronparser.CronExpressionDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.crongen.CronGenExt;
import ru.skysoftlab.greenhouse.common.CustomAutoGableConverter;
import ru.skysoftlab.greenhouse.dto.SystemConfigDto;
import ru.skysoftlab.greenhouse.impl.DataBaseProvider;
import ru.skysoftlab.greenhouse.web.MainMenu;
import ru.skysoftlab.greenhouse.web.Navigation;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;
import ru.skysoftlab.skylibs.security.RolesList;
import ru.skysoftlab.skylibs.web.annatations.MainMenuItem;
import ru.skysoftlab.skylibs.web.dto.VaadinItemDto;
import ru.skysoftlab.skylibs.web.ui.BaseMenuView;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Системные настройки.
 * 
 * @author Артём
 *
 */
@CDIView(Navigation.SYSTEM)
@MainMenuItem(name = "Настройки", order = MainMenu.CONFIG, hasChilds = false)
@RolesAllowed({ RolesList.ADMIN })
public class SystemConfig extends BaseMenuView implements Button.ClickListener, ValueChangeListener {

	private static final long serialVersionUID = 2039928987238266962L;

	private Logger LOG = LoggerFactory.getLogger(SystemConfig.class);

	@Inject
	private DataBaseProvider dataBaseProvider;

	@Inject
	private javax.enterprise.event.Event<SystemConfigEvent> systemEvent;

	private BeanFieldGroup<SystemConfigDto> formFieldBindings;

	@Inject
	// @RequestScoped
	private SystemConfigDto dto;

	private TextField scanInterval = new TextField("Интервал сканирования (cron):");
	private Label scanIntervalLabel = new Label();
	private TextField dataInterval = new TextField("Интервал получения данных (cron):");
	private Label dataIntervalLabel = new Label();
	private OptionGroup auto = new OptionGroup("Управление коньком:");
	private ComboBox serialPort = new ComboBox("Порт для связи с Arduino:");
	private TextField tempMax = new TextField("Максимальная температура (°C):");
	private TextField temp2 = new TextField("Температура 2 (°C):");
	private TextField temp1 = new TextField("Температура 1 (°C):");
	private TextField tempMin = new TextField("Минимальная температура (°C):");
	private TextField humMax = new TextField("Максимальная влажность (°C):");
	// private Slider illumMin = new Slider("Минимальная освещенность:");

	private Button save = new Button("Сохранить", this);

	@Override
	protected void configureComponents() {
		CronGenExt ext = new CronGenExt();
		ext.extend(scanInterval);
		scanInterval.addTextChangeListener(new TextChangeListener() {

			private static final long serialVersionUID = 6560987133628416549L;

			@Override
			public void textChange(TextChangeEvent event) {
				scanIntervalLabel.setCaption(cronToString(event.getText()));
			}
		});
		scanIntervalLabel.setCaption(cronToString(dto.getScanInterval()));
		CronGenExt ext1 = new CronGenExt();
		ext1.extend(dataInterval);
		dataInterval.addTextChangeListener(new TextChangeListener() {

			private static final long serialVersionUID = 6560987133628416549L;

			@Override
			public void textChange(TextChangeEvent event) {
				dataIntervalLabel.setCaption(cronToString(event.getText()));
			}
		});
		dataIntervalLabel.setCaption(cronToString(dto.getDataInterval()));
		// illumMin.setMin(0);
		// illumMin.setMax(1000);
		// illumMin.setWidth("300px");
		serialPort.removeAllItems();
		serialPort.addItems(SerialPortList.getPortNames());
		auto.removeAllItems();
		auto.addItems(new VaadinItemDto(true, "Автоматическое"), new VaadinItemDto(false, "Ручное"));
		auto.setConverter(new CustomAutoGableConverter());
	}

	@Override
	protected void buildLayout() {
		Label title = new Label("Системные настройки");
		FormLayout form = new FormLayout(scanInterval, scanIntervalLabel, dataInterval, dataIntervalLabel, auto,
				serialPort, tempMax, temp2, temp1, tempMin, humMax, save);
		formFieldBindings = BeanFieldGroup.bindFieldsBuffered(dto, this);
		VerticalLayout left = new VerticalLayout(title, form);
		left.setExpandRatio(form, 1);
		left.setSizeFull();
		left.setHeight("600px");
		HorizontalLayout mainLayout = new HorizontalLayout(left);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(left, 1);

		layout.addComponent(mainLayout);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		try {
			formFieldBindings.commit();
			// Save DAO to backend with direct synchronous service API
			try {
				dataBaseProvider.saveConfig(dto);
				// событие изменения настроек
				systemEvent.fire(new SystemConfigEvent(dto.getDataForEvent()));
				Notification.show("Настройки сохранены.", Type.TRAY_NOTIFICATION);
			} catch (Exception e) {
				String msg = "System configuration save error";
				LOG.error(msg, e);
				Notification.show(msg, Type.ERROR_MESSAGE);
			}
		} catch (FieldGroup.CommitException e) {
			// Validation exceptions could be shown here
		}
	}

	@Override
	public void valueChange(ValueChangeEvent event) {

	}

	private String cronToString(String cronExpr) {
		try {
			return CronExpressionDescriptor.getDescription(cronExpr, new Locale("ru"));
		} catch (ParseException e1) {
			e1.printStackTrace();
			return "";
		}
	}

}
