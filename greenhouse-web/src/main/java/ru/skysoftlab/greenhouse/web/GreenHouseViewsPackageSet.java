package ru.skysoftlab.greenhouse.web;

import ru.skysoftlab.skylibs.web.ui.ViewsPackageSet;

public class GreenHouseViewsPackageSet implements ViewsPackageSet {

	@Override
	public String[] getViewsPackages() {
		return new String[] { "ru.skysoftlab.greenhouse.ui" };
	}

}
