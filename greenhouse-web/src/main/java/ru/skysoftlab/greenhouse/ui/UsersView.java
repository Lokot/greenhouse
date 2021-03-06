package ru.skysoftlab.greenhouse.ui;

import ru.skysoftlab.skylibs.security.RolesList;
import ru.skysoftlab.skylibs.web.annatations.MainMenuItem;
import ru.skysoftlab.skylibs.web.annatations.MenuItemView;
import ru.skysoftlab.skylibs.web.ui.views.UserView;
import ru.skysoftlab.greenhouse.web.MainMenu;
import ru.skysoftlab.greenhouse.web.MainMenu.ConfigMenu;

import javax.annotation.security.RolesAllowed;

import com.vaadin.cdi.CDIView;

@CDIView(MainMenu.USERS)
@MainMenuItem(name = "Настройки", order = MainMenu.CONFIG)
@MenuItemView(name = "Пользователи", order = ConfigMenu.CONFIG_USERS)
@RolesAllowed({ RolesList.ADMIN })
public class UsersView extends UserView {

	private static final long serialVersionUID = 2125858000767147430L;

}
