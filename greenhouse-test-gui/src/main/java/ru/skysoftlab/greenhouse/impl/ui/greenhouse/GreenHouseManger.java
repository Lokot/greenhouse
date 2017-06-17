package ru.skysoftlab.greenhouse.impl.ui.greenhouse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ru.skysoftlab.greenhouse.impl.ui.ArduinoEmulator;

public class GreenHouseManger implements IArduinoGreenHouse {

	private Text tempText, humText, illumText;
	private Button buttonClose, button30, button60, buttonOpen;
	private Label gableStateLabel;

	private ArduinoEmulator emulator = new ArduinoEmulator();

	public GreenHouseManger(Shell shell) {
		emulator.setArduinoGreenHouse(this);
	}

	public Control createContents(final Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		{
			Label tempLabel = new Label(container, SWT.NONE);
			tempLabel.setText("Температура: ");
			tempText = new Text(container, SWT.BORDER);
			tempText.setText("22.0");

			Label humLabel = new Label(container, SWT.NONE);
			humLabel.setText("Влажность: ");
			humText = new Text(container, SWT.BORDER);
			humText.setText("35.0");

			Label illumLabel = new Label(container, SWT.NONE);
			illumLabel.setText("Освещенность: ");
			illumText = new Text(container, SWT.BORDER);
			illumText.setText("725");

			buttonClose = new Button(container, SWT.RADIO);
			buttonClose.setText("Закрыт");
			buttonClose.setData("state", 0);
			buttonClose.setSelection(true);
			buttonClose.addListener(SWT.Selection, emulator.getListener());
			button30 = new Button(container, SWT.RADIO);
			button30.setText("Открыт на 30%");
			button30.setData("state", 1);
			button30.addListener(SWT.Selection, emulator.getListener());
			button60 = new Button(container, SWT.RADIO);
			button60.setText("Открыт на 60%");
			button60.setData("state", 2);
			button60.addListener(SWT.Selection, emulator.getListener());
			buttonOpen = new Button(container, SWT.RADIO);
			buttonOpen.setText("Открыт");
			buttonOpen.setData("state", 3);
			buttonOpen.addListener(SWT.Selection, emulator.getListener());

			Label gLabel = new Label(container, SWT.NONE);
			gLabel.setText("Состояние: ");
			gableStateLabel = new Label(container, SWT.NONE);
			gableStateLabel.setText("Конек остановлен");
		}
		return container;
	}

	@Override
	public String getTemp() {
		return tempText.getText();
	}

	@Override
	public String getHum() {
		return humText.getText();
	}

	@Override
	public int getIllum() {
		return Integer.valueOf(illumText.getText());
	}

	@Override
	public byte getGableState(int objectState) {
		switch (objectState) {
		case 0:
			return (byte) ((buttonClose.getSelection()) ? 1 : 0);
		case 1:
			return (byte) ((button30.getSelection()) ? 1 : 0);
		case 2:
			return (byte) ((button60.getSelection()) ? 1 : 0);
		case 3:
			return (byte) ((buttonOpen.getSelection()) ? 1 : 0);
		}
		return 0;
	}

	@Override
	public void gableOpen(byte state) {
		if (state > 0) {
			gableStateLabel.setText("Конек открывается");
		}
	}

	@Override
	public void gableStop(byte state) {
		if (state > 0) {
			gableStateLabel.setText("Конек остановлен");
		}
	}

	@Override
	public void gableClose(byte state) {
		if (state > 0) {
			gableStateLabel.setText("Конек закрывается");
		}
	}
}
