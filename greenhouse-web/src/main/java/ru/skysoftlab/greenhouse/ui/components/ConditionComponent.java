package ru.skysoftlab.greenhouse.ui.components;

import static ru.skysoftlab.greenhouse.common.drools.ConditionOperator.EQUAL_TO;
import static ru.skysoftlab.greenhouse.common.drools.ConditionOperator.GREATER_THAN;
import static ru.skysoftlab.greenhouse.common.drools.ConditionOperator.GREATER_THAN_OR_EQUAL_TO;
import static ru.skysoftlab.greenhouse.common.drools.ConditionOperator.LESS_THAN;
import static ru.skysoftlab.greenhouse.common.drools.ConditionOperator.LESS_THAN_OR_EQUAL_TO;
import static ru.skysoftlab.greenhouse.common.drools.ConditionOperator.NOT_EQUAL_TO;
import static ru.skysoftlab.greenhouse.common.drools.ConditionParam.HUMIDITY;
import static ru.skysoftlab.greenhouse.common.drools.ConditionParam.TEMPERATURE;
import static ru.skysoftlab.greenhouse.common.drools.ConditionVals.HUM_MAX;
import static ru.skysoftlab.greenhouse.common.drools.ConditionVals.TEMP_1;
import static ru.skysoftlab.greenhouse.common.drools.ConditionVals.TEMP_2;
import static ru.skysoftlab.greenhouse.common.drools.ConditionVals.TEMP_MAX;
import static ru.skysoftlab.greenhouse.common.drools.ConditionVals.TEMP_MIN;
import static ru.skysoftlab.skylibs.web.ui.VaadinUtils.comboboxReadOnlyAndSelectFirst;
import static ru.skysoftlab.skylibs.web.ui.VaadinUtils.selectComboBox;
import static ru.skysoftlab.skylibs.web.ui.VaadinUtils.selectFirst;

import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

import ru.skysoftlab.greenhouse.common.drools.ConditionOperator;
import ru.skysoftlab.greenhouse.common.drools.ConditionParam;
import ru.skysoftlab.greenhouse.common.drools.ConditionVals;
import ru.skysoftlab.greenhouse.jpa.entitys.drools.Condition;
import ru.skysoftlab.skylibs.web.dto.VaadinItemDto;

public class ConditionComponent extends CustomField<Condition> {

	private static final long serialVersionUID = 7085537974833859738L;

	private Condition condition;

	private Panel panel;
	private ComboBox conditionField = new ComboBox("Параметр:");
	private ComboBox conditionOperator = new ComboBox("Условие:");
	private ComboBox conditionVals = new ComboBox("Значение:");

	public ConditionComponent(String caption) {
		panel = new Panel(caption);

		conditionField.addItem(new VaadinItemDto(TEMPERATURE, "Температура"));
		conditionField.addItem(new VaadinItemDto(HUMIDITY, "Влажность"));
		conditionField.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -4211139597466704819L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				conditionVals.removeAllItems();
				ConditionParam field = ((VaadinItemDto) conditionField.getValue()).<ConditionParam>getObj();
				switch (field) {
				case TEMPERATURE:
					conditionVals.addItem(new VaadinItemDto(TEMP_MIN, "T min"));
					conditionVals.addItem(new VaadinItemDto(TEMP_1, "T1"));
					conditionVals.addItem(new VaadinItemDto(TEMP_2, "T2"));
					conditionVals.addItem(new VaadinItemDto(TEMP_MAX, "T max"));
					break;

				case HUMIDITY:
				default:
					conditionVals.addItem(new VaadinItemDto(HUM_MAX, "Hum Max"));
					break;
				}
				selectFirst(conditionVals);
			}
		});

		conditionOperator.addItem(new VaadinItemDto(EQUAL_TO, "="));
		conditionOperator.addItem(new VaadinItemDto(NOT_EQUAL_TO, "!="));
		conditionOperator.addItem(new VaadinItemDto(GREATER_THAN, ">"));
		conditionOperator.addItem(new VaadinItemDto(GREATER_THAN_OR_EQUAL_TO, ">="));
		conditionOperator.addItem(new VaadinItemDto(LESS_THAN, "<"));
		conditionOperator.addItem(new VaadinItemDto(LESS_THAN_OR_EQUAL_TO, "<="));

		conditionVals.addItem(new VaadinItemDto(TEMP_MIN, "T min"));
		conditionVals.addItem(new VaadinItemDto(TEMP_1, "T1"));
		conditionVals.addItem(new VaadinItemDto(TEMP_2, "T2"));
		conditionVals.addItem(new VaadinItemDto(TEMP_MAX, "T max"));

		comboboxReadOnlyAndSelectFirst(conditionField);
		comboboxReadOnlyAndSelectFirst(conditionOperator);
		comboboxReadOnlyAndSelectFirst(conditionVals);
	}

	@Override
	protected Component initContent() {
		HorizontalLayout panelContent = new HorizontalLayout();
		panel.setContent(panelContent);
		panelContent.addComponent(conditionField);
		panelContent.addComponent(conditionOperator);
		panelContent.addComponent(conditionVals);
		return panel;
	}

	@Override
	public Condition getValue() {
		if (condition == null) {
			condition = new Condition();
		}
		ConditionParam field = ((VaadinItemDto) conditionField.getValue()).<ConditionParam>getObj();
		ConditionOperator operator = ((VaadinItemDto) conditionOperator.getValue()).<ConditionOperator>getObj();
		ConditionVals val = ((VaadinItemDto) conditionVals.getValue()).<ConditionVals>getObj();
		condition.setField(field);
		condition.setOperator(operator);
		condition.setValue(val);
		return condition;
	}

	@Override
	public void setValue(Condition newFieldValue)
			throws com.vaadin.data.Property.ReadOnlyException, ConversionException {
		condition = newFieldValue;
		selectComboBox(conditionField, condition.getField());
		selectComboBox(conditionOperator, condition.getOperator());
		selectComboBox(conditionVals, condition.getValue());
	}

	@Override
	public Class<? extends Condition> getType() {
		return Condition.class;
	}

}
