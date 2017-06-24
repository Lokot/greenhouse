package ru.skysoftlab.greenhouse.common;

import java.text.ParseException;
import java.util.Locale;

import net.redhogs.cronparser.CronExpressionDescriptor;

import com.vaadin.data.util.converter.Converter;

public class CronExprTextConverter implements Converter<String, String> {

	private static final long serialVersionUID = 2570832993397857818L;

	@Override
	public String convertToModel(String value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value;
	}

	@Override
	public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null) {
			try {
				return CronExpressionDescriptor.getDescription(value, new Locale("ru"));
			} catch (ParseException e) {
				e.printStackTrace();
				return value;
			} 
		} else {
			return "";
		}
	}

	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
