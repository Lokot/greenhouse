package ru.skysoftlab.greenhouse.ui.components;

import static ru.skysoftlab.greenhouse.common.ConfigurationNames.AUTO;
import static ru.skysoftlab.greenhouse.common.GableState.Close;
import static ru.skysoftlab.greenhouse.common.GableState.Degrees30;
import static ru.skysoftlab.greenhouse.common.GableState.Degrees60;
import static ru.skysoftlab.greenhouse.common.GableState.Open;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ru.skysoftlab.greenhouse.common.GableMoveEvent;
import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.greenhouse.impl.DataBaseProvider;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;
import ru.skysoftlab.skylibs.web.dto.VaadinItemDto;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;

/**
 * Компонент управления коньком.
 * 
 * @author Lokot
 *
 */
public class GableStateSelector extends GridLayout {

	private static final long serialVersionUID = -5570816315675931244L;

	@Inject
	private IController arduino;

	@Inject
	private DataBaseProvider dataBaseProvider;
	
	@Inject
	private javax.enterprise.event.Event<SystemConfigEvent> systemEvent;

	@Inject
	@AppProperty(AUTO)
	private Boolean autoConfig;

	private OptionGroup autoGableSelector = new OptionGroup(
			"Управление коньком");
	private ComboBox statesCombo = new ComboBox("Открыть на:");
	private Button changeButton = new Button("Открыть/Закрыть");

	private final VaadinItemDto auto = new VaadinItemDto(true, "Автоматическое"),
			manual = new VaadinItemDto(false, "Ручное");

	private final VaadinItemDto close = new VaadinItemDto(Close, "Закрыть"),
			open30 = new VaadinItemDto(Degrees30, "Открыть на 30%"),
			open60 = new VaadinItemDto(Degrees60, "Открыть на 60%"),
			open = new VaadinItemDto(Open, "Открыть на 100%");

	public GableStateSelector() {
		super(2, 2);
		addComponents(autoGableSelector, statesCombo, changeButton);
	}

	@PostConstruct
	private void init() {
		autoGableSelector.addItems(auto, manual);
		if (autoConfig) {
			autoGableSelector.setValue(auto);
			statesCombo.setVisible(false);
			changeButton.setVisible(false);
		} else {
			autoGableSelector.setValue(manual);
			statesCombo.setVisible(true);
			changeButton.setVisible(true);
		}
		autoGableSelector.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -5025316696883268535L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				VaadinItemDto item = (VaadinItemDto) ((OptionGroup) event.getProperty())
						.getValue();
				statesCombo.setVisible(!item.<Boolean> getObj());
				changeButton.setVisible(!item.<Boolean> getObj());
				try {
					dataBaseProvider.updateBooleanProperty(AUTO,
							item.<Boolean> getObj(), "Автоматический режим");
					Notification.show("Настройки управления коньком изменены:",
							String.valueOf(autoGableSelector.getValue()),
							Type.TRAY_NOTIFICATION);
					systemEvent.fire(new SystemConfigEvent(getDataForEvent()));
				} catch (Exception e) {
					Notification.show("Ошибка сохранения настроек:",
							e.getMessage(), Type.TRAY_NOTIFICATION);
				}
			}
		});
		// состояния
		statesCombo.addItems(close, open30, open60, open);
		setStateOfGable(arduino.getGableState());

		changeButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2226831226306602215L;

			@Override
			public void buttonClick(ClickEvent event) {
				// НАСТРОЙКИ РЕЖИМА РАБОТЫ
				arduino.setGableState(((VaadinItemDto) statesCombo.getValue())
						.<GableState> getObj());
				Notification.show("Настройки конька изменены:",
						String.valueOf(statesCombo.getValue()),
						Type.TRAY_NOTIFICATION);
			}
		});
	}
	
	public void gableMoveEvent(@Observes GableMoveEvent event) {
		setStateOfGable(event.getState());
	}
	
	private void setStateOfGable(GableState state){
		switch (state) {
		case Open:
			statesCombo.setValue(open);
			break;
		case Degrees30:
			statesCombo.setValue(open30);
			break;
		case Degrees60:
			statesCombo.setValue(open60);
			break;
		case Close:
		default:
			statesCombo.setValue(close);
			break;
		}
	}
	
	public Map<String, Object> getDataForEvent() {
		Map<String, Object> rv = new HashMap<>();
		rv.put(AUTO, ((VaadinItemDto) autoGableSelector.getValue()).<Boolean> getObj());
		return rv;
	}
}
