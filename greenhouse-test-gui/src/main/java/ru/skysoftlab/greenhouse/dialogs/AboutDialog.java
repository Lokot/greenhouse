package ru.skysoftlab.greenhouse.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog extends Dialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AboutDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		Label label = new Label(container, SWT.NONE);
		label.setText("ZlockConfig 1.0 - Электронная база данных USB-запоминающих устройств");
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("Программа позволяет:\n"
				+ "- вести электронный учет USB-ЗУ\n"
				+ "- выгружать USB-ЗУ в файл конфигурации СЗИ \"Zlock-RV 4.0\"\n"
				+ "- выгружать список USB-ЗУ в xml, txt, csv файлы\n"
				+ "- анализировать логи СЗИ \"Zlock-RV 4.0\"\n"
				+ "- анализировать выгрузку устройств из программы \"USBDeview\"");
		Label label2 = new Label(container, SWT.NONE);
		label2.setText("Copyright © 2017 Локтионов А.Г.");
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 220);
	}

	@Override
    protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText("О программе");
    }
	
}
