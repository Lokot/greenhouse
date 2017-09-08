package ru.skysoftlab.greenhouse.web;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import ru.skysoftlab.skylibs.web.common.AppNames;
import ru.skysoftlab.skylibs.web.navigation.MainVaadinUI;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.cdi.server.VaadinCDIServlet;

/**
 * Сервлет для подгрузки widgetset.
 * 
 * @author Артём
 *
 */
@WebServlet(value = "/*", asyncSupported = true, initParams = { @WebInitParam(name = "session-timeout", value = "60"),
		@WebInitParam(name = "UIProvider", value = "com.vaadin.cdi.CDIUIProvider"),
		@WebInitParam(name = "viewprovider", value = "com.vaadin.cdi.CDIViewProvider") })
@VaadinServletConfiguration(productionMode = false, ui = MainVaadinUI.class, closeIdleSessions = true, widgetset = "ru.skysoftlab.greenhouse.widgetset.VaadinauthWidgetset")
public class Servlet extends VaadinCDIServlet {

	private static final long serialVersionUID = 2918754860952978590L;
	
	public Servlet() {
		System.setProperty(AppNames.APP_NAME, "Система управления теплицей");
	}

}