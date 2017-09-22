package ru.skysoftlab.greenhouse.web;

import ru.skysoftlab.skylibs.web.navigation.NavigationService;

public interface MainMenu extends NavigationService {
	
	public static final String USERS = "users";

	public final static int Chart = 0;
	public final static int ANALIZ = 1;
	public final static int CONFIG = 2;
	
	public interface ConfigMenu {
		public static final int CONFIG_CONFIG = 0;
		public static final int CONFIG_USERS = 1;	
		public final static int IRRIGATION = 2;
		public final static int ROOLS = 3;
	}

}
