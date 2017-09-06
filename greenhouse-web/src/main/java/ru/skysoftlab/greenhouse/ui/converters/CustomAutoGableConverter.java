package ru.skysoftlab.greenhouse.ui.converters;

import java.util.Locale;

import ru.skysoftlab.skylibs.web.dto.VaadinItemDto;

import com.vaadin.data.util.converter.Converter;

public class CustomAutoGableConverter implements Converter<Object, Boolean> {

	private static final long serialVersionUID = 2570832993397857818L;

	@Override
	public Boolean convertToModel(Object value,
			Class<? extends Boolean> targetType, Locale locale)
			throws ConversionException {
		return ((VaadinItemDto) value).<Boolean> getObj();
	}

	@Override
	public Object convertToPresentation(Boolean value,
			Class<? extends Object> targetType, Locale locale)
			throws ConversionException {
		if (value) {
			return new VaadinItemDto(true, "Автоматическое");
		} else {
			return new VaadinItemDto(false, "Ручное");
		}
	}

	@Override
	public Class<Boolean> getModelType() {
		return Boolean.class;
	}

	@Override
	public Class<Object> getPresentationType() {
		return Object.class;
	}

}
