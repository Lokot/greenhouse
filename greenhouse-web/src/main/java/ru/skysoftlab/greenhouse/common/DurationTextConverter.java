package ru.skysoftlab.greenhouse.common;

import java.util.Locale;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import com.vaadin.data.util.converter.Converter;

public class DurationTextConverter implements Converter<String, Duration> {

	private static final long serialVersionUID = 2570832993397857818L;

	@Override
	public Duration convertToModel(String value, Class<? extends Duration> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null && value.length() > 0) {
			return Duration.standardMinutes(Long.valueOf(value));
		} else {
			return Duration.standardMinutes(0);
		}
	}

	@Override
	public String convertToPresentation(Duration value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value != null) {
			return String.valueOf(value.getStandardSeconds() / DateTimeConstants.SECONDS_PER_MINUTE);
		} else {
			return "";
		}
	}

	@Override
	public Class<Duration> getModelType() {
		return Duration.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
