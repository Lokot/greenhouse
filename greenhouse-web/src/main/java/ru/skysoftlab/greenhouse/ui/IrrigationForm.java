package ru.skysoftlab.greenhouse.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import org.sintef.jarduino.DigitalPin;

import ru.skysoftlab.crongen.CronGenExt;
import ru.skysoftlab.greenhouse.common.DurationTextConverter;
import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;
import ru.skysoftlab.skylibs.web.ui.AbstractForm;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

/**
 * Форма полива.
 * 
 * @author Loktionov Artem
 *
 */
public class IrrigationForm extends AbstractForm<IrrigationCountur> {

	private static final long serialVersionUID = 2372643403143137631L;

	private TextField cronExpr = new TextField("Период: ");
	private TextField name = new TextField("Контур: ");
	private TextField duration = new TextField("Длительность (мин.): ");
	private ComboBox pin = new ComboBox("Пин: ", EnumSet.allOf(DigitalPin.class));

	public IrrigationForm() {
		CronGenExt ext = new CronGenExt();
		ext.extend(cronExpr);
		duration.setConverter(new DurationTextConverter());
	}

	@Override
	protected Collection<? extends Component> getInputs() {
		// cronExpr = new TextField("Период: ");
		// duration = new TextField("Длительность (мин.): ");
		// name = new TextField("Контур: ");
		// pin = new ComboBox("Пин: ", EnumSet.allOf(DigitalPin.class));

		Collection<Component> rv = new ArrayList<>();
		rv.add(cronExpr);
		rv.add(duration);
		rv.add(name);
		rv.add(pin);
		return rv;
	}

	@Override
	protected void setFocus() {
		name.focus();
	}

}
