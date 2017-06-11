package ru.skysoftlab.greenhouse.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

@Stateless
public class TestBean {
	
	@RolesAllowed("ADMIN")
	public String getProtectedInfo() {
		return "It's protected information.";
	}
}
