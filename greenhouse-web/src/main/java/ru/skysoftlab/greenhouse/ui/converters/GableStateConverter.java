package ru.skysoftlab.greenhouse.ui.converters;

import static ru.skysoftlab.greenhouse.ui.RuleForm.GABLE_30;
import static ru.skysoftlab.greenhouse.ui.RuleForm.GABLE_60;
import static ru.skysoftlab.greenhouse.ui.RuleForm.GABLE_CLOSE;
import static ru.skysoftlab.greenhouse.ui.RuleForm.GABLE_OPEN;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.skylibs.web.dto.VaadinItemDto;

public class GableStateConverter implements Converter<Object, GableState> {

	private static final long serialVersionUID = 2570832993397857818L;

	@Override
	public GableState convertToModel(Object value, Class<? extends GableState> targetType, Locale locale)
			throws ConversionException {
		return ((VaadinItemDto) value).<GableState>getObj();
	}

	@Override
	public Object convertToPresentation(GableState value, Class<? extends Object> targetType, Locale locale)
			throws ConversionException {
		if (value == null) {
			return GABLE_CLOSE;
		}
		switch (value) {
		case Close:
			return GABLE_CLOSE;
		case Degrees30:
			return GABLE_30;
		case Degrees60:
			return GABLE_60;
		default:
			return GABLE_OPEN;
		}
	}

	@Override
	public Class<GableState> getModelType() {
		return GableState.class;
	}

	@Override
	public Class<Object> getPresentationType() {
		return Object.class;
	}

}
