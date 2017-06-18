package ru.skysoftlab.greenhouse.web;

import ru.skysoftlab.skylibs.web.navigation.NavigationService;

public interface MainMenu extends NavigationService {

	public final static int Chart = 0;
	public final static int ANALIZ = 1;
	public final static int CONFIG = 2;
	public final static int IRRIGATION = 3;
	
//	public static interface SystemMenu {
//		public final static int SYSTEM = 0;
//	}

	public static interface AlarmMenu {
		public final static int ALARMS = 0;
	}

}
