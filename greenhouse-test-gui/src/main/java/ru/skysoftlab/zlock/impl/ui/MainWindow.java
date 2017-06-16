package ru.skysoftlab.zlock.impl.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ru.skysoftlab.zlock.impl.ui.usbmanager.UsbManger;
import ru.skysoftlab.zlock.impl.ui.usbmanager.dialogs.AboutDialog;

public class MainWindow extends ApplicationWindow {

	private Shell shell;
	private UsbManger mainContent;
	private AboutDialog aboutDialog;

	/**
	 * Create the application window.
	 */
	public MainWindow() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(final Composite parent) {
		return mainContent.createContents(parent);
	}

	/**
	 * Create the menu manager.
	 * 
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager mainMenu = new MenuManager();
		MenuManager fileMenu = new MenuManager("Файл");
		MenuManager helpMenu = new MenuManager("Помощь");

		// File popup menu
		// fileMenu.add(new ImportFile());
		fileMenu.add(new UsbManager());
		fileMenu.add(new Exit(this));

		// Help popup menu
		helpMenu.add(new About());

		mainMenu.add(fileMenu);
		mainMenu.add(helpMenu);

		return mainMenu;
	}

	/**
	 * Create the toolbar manager.
	 * 
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * 
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MainWindow window = new MainWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("ZLock Configurator");
//		newShell.setImage(Utils.getImage(newShell.getDisplay(), "zlock_conf_icon.png"));
		shell = newShell;
		mainContent = new UsbManger(shell);
//		usbAnalize = new UsbAnalize();
		aboutDialog = new AboutDialog(shell);
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	class UsbManager extends Action {
		public UsbManager() {
			super("&Управление устройствами@Ctrl+M", AS_PUSH_BUTTON);
		}

		public void run() {
			clearShell();
			Control control = mainContent.createContents(shell);
			control.pack();
			control.setSize(shell.getSize());
		}
	}

	class Exit extends Action {
		ApplicationWindow win;

		public Exit(ApplicationWindow aWin) {
			super("В&ыход@Alt+X", AS_PUSH_BUTTON);
			this.win = aWin;
		}

		public void run() {
			this.win.close();
		}
	}

	class About extends Action {

		public About() {
			super("О программе", AS_PUSH_BUTTON);
		}

		public void run() {
			aboutDialog.open();
		}
	}
	
	private void clearShell() {
		for (Control kid : shell.getChildren()) {
			kid.dispose();
		}
	}
}
