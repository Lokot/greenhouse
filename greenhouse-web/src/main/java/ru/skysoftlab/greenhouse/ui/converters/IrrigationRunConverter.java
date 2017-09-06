package ru.skysoftlab.greenhouse.ui.converters;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class IrrigationRunConverter implements Converter<String, Boolean> {

	private static final long serialVersionUID = 2570832993397857818L;

	@Override
	public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null && value.length() > 0) {
			return value.equals("Включен");
		}
		return false;
	}

	@Override
	public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null && value) {
			return "Включен";
		} else {
			return "Выключен";
		}
	}

	@Override
	public Class<Boolean> getModelType() {
		return Boolean.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
