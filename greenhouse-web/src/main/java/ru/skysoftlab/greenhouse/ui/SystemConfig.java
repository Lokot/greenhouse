package ru.skysoftlab.greenhouse.ui;

import static ru.skysoftlab.skylibs.web.ui.VaadinUtils.comboboxReadOnly;

import java.util.Locale;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
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

import ru.skysoftlab.crongen.ParseCronGenComponent;
import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.greenhouse.dto.SystemConfigDto;
import ru.skysoftlab.greenhouse.impl.DataBaseProvider;
import ru.skysoftlab.greenhouse.ui.converters.CustomAutoGableConverter;
import ru.skysoftlab.greenhouse.web.MainMenu;
import ru.skysoftlab.greenhouse.web.MainMenu.ConfigMenu;
import ru.skysoftlab.greenhouse.web.Navigation;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;
import ru.skysoftlab.skylibs.security.RolesList;
import ru.skysoftlab.skylibs.web.annatations.MainMenuItem;
import ru.skysoftlab.skylibs.web.annatations.MenuItemView;
import ru.skysoftlab.skylibs.web.dto.VaadinItemDto;
import ru.skysoftlab.skylibs.web.ui.BaseMenuView;

/**
 * Системные настройки.
 * 
 * @author Артём
 *
 */
@CDIView(Navigation.SYSTEM)
@MainMenuItem(name = "Настройки", order = MainMenu.CONFIG)
@MenuItemView(name = "Конфигурация", order = ConfigMenu.CONFIG_CONFIG)
@RolesAllowed({ RolesList.ADMIN })
public class SystemConfig extends BaseMenuView implements Button.ClickListener, ValueChangeListener {

	private static final long serialVersionUID = 2039928987238266962L;

	private Logger LOG = LoggerFactory.getLogger(SystemConfig.class);
	private final Locale ruLocale = new Locale("ru");

	@Inject
	private DataBaseProvider dataBaseProvider;
	
	@Inject
	private IController controller;

	@Inject
	private javax.enterprise.event.Event<SystemConfigEvent> systemEvent;

	private BeanFieldGroup<SystemConfigDto> formFieldBindings;

	@Inject
	// @RequestScoped
	private SystemConfigDto dto;

	private ParseCronGenComponent scanInterval = new ParseCronGenComponent("Интервал сканирования (cron):", ruLocale);
	private ParseCronGenComponent dataInterval = new ParseCronGenComponent("Интервал получения данных (cron):",
			ruLocale);
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
		// illumMin.setMin(0);
		// illumMin.setMax(1000);
		// illumMin.setWidth("300px");
		serialPort.removeAllItems();
		serialPort.addItems(controller.getCommPorts());
		comboboxReadOnly(serialPort);
		auto.removeAllItems();
		auto.addItems(new VaadinItemDto(true, "Автоматическое"), new VaadinItemDto(false, "Ручное"));
		auto.setConverter(new CustomAutoGableConverter());
	}

	@Override
	protected void buildLayout() {
		Label title = new Label("Системные настройки");
		FormLayout form = new FormLayout(scanInterval, dataInterval, auto, serialPort, tempMax, temp2, temp1, tempMin,
				humMax, save);
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

}
