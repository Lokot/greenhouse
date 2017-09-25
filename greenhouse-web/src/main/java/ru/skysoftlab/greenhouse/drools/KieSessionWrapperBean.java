package ru.skysoftlab.greenhouse.drools;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.kie.api.runtime.KieSession;

import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.greenhouse.dto.SystemConfigDto;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.one.shot.scope.extension.OneShotScope;

@OneShotScope
public class KieSessionWrapperBean {

	@Inject
	private IController controller;

	@Inject
	private SystemConfigDto dto;

	@Inject
	@AppProperty("META-INF/rules/GableRuleTemplate.drt")
	private KieSession kSession;

	@PostConstruct
	private void init() {
		kSession.setGlobal("HUM_MAX", dto.getHumMax());
		kSession.setGlobal("TEMP_MAX", dto.getTempMax());
		kSession.setGlobal("TEMP_2", dto.getTemp2());
		kSession.setGlobal("TEMP_1", dto.getTemp1());
		kSession.setGlobal("TEMP_MIN", dto.getTempMin());
		kSession.setGlobal("controller", controller);
	}

	public KieSession getkSession() {
		return kSession;
	}

}
