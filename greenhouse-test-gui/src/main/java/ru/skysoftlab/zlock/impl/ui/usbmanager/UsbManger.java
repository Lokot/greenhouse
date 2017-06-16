package ru.skysoftlab.zlock.impl.ui.usbmanager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class UsbManger {

//	private UsbStorageViewer usbStorageViewer;
	private Button selectAllButton;
//	private EditEntityDialog<UsbStorage> addUsbStorageDialog;
//	private EditEntityDialog<Department> addDepartmentDialog;
//	private final UsbStorageProvider storageProvider = new UsbStorageProvider();
//	private final ImportExportProvider importExportProvider = new ImportExportProvider();

	public UsbManger(Shell shell) {
//		addUsbStorageDialog = new AddUsbStorageDialog(shell, storageProvider);
//		addDepartmentDialog = new AddDepartmentDialog(shell);

	}

	public Control createContents(final Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		{
			Composite tableTopContainer = new Composite(container, SWT.NONE);
			tableTopContainer.setLayout(new GridLayout(4, false));
			{
				Label searchLabel = new Label(tableTopContainer, SWT.NONE);
				searchLabel.setText("Поиск: ");
				final Text searchText = new Text(tableTopContainer, SWT.BORDER
						| SWT.SEARCH);
				searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
						| GridData.HORIZONTAL_ALIGN_FILL));
				searchText.addKeyListener(new KeyAdapter() {
					public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
						selectAllButton.setSelection(false);
//						usbStorageViewer.setSearchString(searchText.getText());
//						usbStorageViewer.refresh();
					};
				});
			}
		}
		return container;
	}
}
