package ru.skysoftlab.greenhouse.ui.converters;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import ru.skysoftlab.greenhouse.common.GableState;

public class GableStateStringConverter implements Converter<String, GableState> {

	private static final long serialVersionUID = 2570832993397857818L;

	@Override
	public GableState convertToModel(String value, Class<? extends GableState> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		switch (value) {
		case "Закрыть":
			return GableState.Close;
		case "Открыть на 30%":
			return GableState.Degrees30;
		case "Открыть на 60%":
			return GableState.Degrees60;
		default:
			return GableState.Open;
		}
	}

	@Override
	public String convertToPresentation(GableState value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		switch (value) {
		case Close:
			return "Закрыть";
		case Degrees30:
			return "Открыть на 30%";
		case Degrees60:
			return "Открыть на 60%";
		default:
			return "Открыть на 100%";
		}
	}

	@Override
	public Class<GableState> getModelType() {
		return GableState.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
