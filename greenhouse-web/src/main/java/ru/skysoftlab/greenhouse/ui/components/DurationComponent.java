package ru.skysoftlab.greenhouse.ui.components;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import ru.skysoftlab.skylibs.web.dto.VaadinItemDto;

import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

public class DurationComponent extends CustomField<Duration> {

	private static final long serialVersionUID = 7085537974833859738L;

	private Panel panel;
	private ComboBox durationType = new ComboBox("Ед. изм.:");
	private TextField durationTypeLenth = new TextField("Продолжительность:");
	
	public DurationComponent(String caption) {
		panel = new Panel(caption);
		durationType.addItem(new VaadinItemDto(0, "Минуты"));
		durationType.addItem(new VaadinItemDto(1, "Часы"));
	}

	@Override
	protected Component initContent() {
		HorizontalLayout panelContent = new HorizontalLayout();
		panel.setContent(panelContent);
		panelContent.addComponent(durationType);
		panelContent.addComponent(durationTypeLenth);
//		setCompositionRoot(panel);
//		panelContent.setSizeUndefined();
//		panel.setSizeUndefined();
		return panel;
	}

	@Override
	public Duration getValue() {
		Integer typeVal = ((VaadinItemDto) durationType.getValue()).<Integer>getObj();
		Long dValue = Long.valueOf(durationTypeLenth.getValue());
		switch (typeVal) {
		case 1:
			return Duration.standardHours(dValue);

		default:
			return Duration.standardMinutes(dValue);
		}
	}

	@Override
	public void setValue(Duration newFieldValue)
			throws com.vaadin.data.Property.ReadOnlyException,
			ConversionException {
		long seconds = newFieldValue.getStandardSeconds();
		Long minutes = seconds/DateTimeConstants.SECONDS_PER_MINUTE;
		durationType.select(0);
		durationTypeLenth.setValue(minutes.toString());
		
	}

	@Override
	public Class<? extends Duration> getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
