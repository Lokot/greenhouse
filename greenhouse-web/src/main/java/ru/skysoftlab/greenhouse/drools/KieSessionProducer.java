package ru.skysoftlab.greenhouse.drools;

import java.io.InputStream;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.drools.template.DataProviderCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import ru.skysoftlab.skylibs.annatations.AppProperty;

public class KieSessionProducer {

	@Inject
	private RulsDataProvider rulsDataProvider;

	@Produces
	@AppProperty("")
	public KieSession getKieSession(InjectionPoint ip) {
		DataProviderCompiler converter = new DataProviderCompiler();
		String drl = converter.compile(rulsDataProvider, getTemplate(ip));
		KieServices kieServices = KieServices.Factory.get();
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		kieFileSystem.write("src/main/resources/rule.drl", drl);
		kieServices.newKieBuilder(kieFileSystem).buildAll();

		KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
		KieSession statelessKieSession = kieContainer.getKieBase().newKieSession();
		return statelessKieSession;
	}

	public void closeKieSession(@Disposes KieSession kieSession) {
		kieSession.dispose();
	}

	private String getFileName(InjectionPoint ip) {
		return ip.getAnnotated().getAnnotation(AppProperty.class).value();
	}

	private InputStream getTemplate(InjectionPoint ip) {
		return getClass().getClassLoader().getResourceAsStream(getFileName(ip));
	}

}
