package ru.skysoftlab.greenhouse.ui;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.vaadin.teemu.switchui.Switch;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import ru.skysoftlab.crongen.CronGenExt;
import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;
import ru.skysoftlab.greenhouse.ui.converters.DurationStringConverter;
import ru.skysoftlab.skylibs.web.ui.AbstractForm;

/**
 * Форма полива.
 * 
 * @author Loktionov Artem
 *
 */
public class IrrigationForm extends AbstractForm<IrrigationCountur> {

	private static final long serialVersionUID = 2372643403143137631L;
	
	@Inject
	private IController arduino;
	
	private TextField cronExpr = new TextField("Период: ");
	private TextField name = new TextField("Контур: ");
	private TextField duration = new TextField("Длительность (мин.): ");
	private ComboBox pin;
	private Switch run = new Switch("Запуск: ");

	public IrrigationForm() {
		CronGenExt ext = new CronGenExt();
		ext.extend(cronExpr);
		duration.setConverter(new DurationStringConverter());
	}
	
	protected void configureComponents() {
		pin = new ComboBox("Пин: ", arduino.getAvalibleDigitalPins());
		super.configureComponents();
	}

	@Override
	protected Collection<? extends Component> getInputs() {
		Collection<Component> rv = new ArrayList<>();
		rv.add(cronExpr);
		rv.add(duration);
		rv.add(name);
		rv.add(pin);
		rv.add(run);
		return rv;
	}

	@Override
	protected void setFocus() {
		name.focus();
	}

}
