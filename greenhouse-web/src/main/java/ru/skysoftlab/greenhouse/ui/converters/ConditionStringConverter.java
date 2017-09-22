package ru.skysoftlab.greenhouse.ui.converters;

import static ru.skysoftlab.greenhouse.common.ConfigurationNames.HUM_MAX;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_1;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_2;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_MAX;
import static ru.skysoftlab.greenhouse.common.ConfigurationNames.TEMP_MIN;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.vaadin.data.util.converter.Converter;

import ru.skysoftlab.greenhouse.common.drools.ConditionParam;
import ru.skysoftlab.greenhouse.common.drools.ConditionVals;
import ru.skysoftlab.greenhouse.jpa.entitys.drools.Rule;
import ru.skysoftlab.skylibs.annatations.AppProperty;

@SuppressWarnings("rawtypes")
public class ConditionStringConverter implements Converter<String, List> {

	private static final long serialVersionUID = 2570832993397857818L;

	@Inject
	@AppProperty(TEMP_MAX)
	private Float tempMax;

	@Inject
	@AppProperty(TEMP_2)
	private Float temp2;

	@Inject
	@AppProperty(TEMP_1)
	private Float temp1;

	@Inject
	@AppProperty(TEMP_MIN)
	private Float tempMin;

	@Inject
	@AppProperty(HUM_MAX)
	private Float humMax;

	@Override
	public List convertToModel(String value, Class<? extends List> targetType, Locale locale)
			throws ConversionException {
		// не используется
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String convertToPresentation(List value, Class<? extends String> targetType, Locale locale)
			throws ConversionException {
		Rule rule = new Rule();
		rule.setConditions(value);
		String rv = rule.toString();
		rv = rv.replaceAll("&&", "и");
		rv = rv.replaceAll(ConditionParam.TEMPERATURE.toString(), "Температура");
		rv = rv.replaceAll(ConditionParam.HUMIDITY.toString(), "Влажность");
		rv = rv.replaceAll(ConditionVals.HUM_MAX.toString(), humMax + "%");
		rv = rv.replaceAll(ConditionVals.TEMP_1.toString(), temp1 + " °C");
		rv = rv.replaceAll(ConditionVals.TEMP_2.toString(), temp2 + " °C");
		rv = rv.replaceAll(ConditionVals.TEMP_MAX.toString(), tempMax + " °C");
		rv = rv.replaceAll(ConditionVals.TEMP_MIN.toString(), tempMin + " °C");
		return rv;
	}

	@Override
	public Class<List> getModelType() {
		return List.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
