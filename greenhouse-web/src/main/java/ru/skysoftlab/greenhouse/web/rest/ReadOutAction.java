package ru.skysoftlab.greenhouse.web.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.common.IController;
import ru.skysoftlab.skylibs.annatations.SimpleQualifier;
import ru.skysoftlab.skylibs.web.common.UniversalAction;

@SimpleQualifier("readout")
public class ReadOutAction implements UniversalAction {

	private static final long serialVersionUID = -5326151984656784276L;

	public static final String ACT = "act";
	public static final String TEMP = "temp";
	public static final String HUM = "hum";
	public static final String ILLUM = "illum";
	public static final String GABLE = "gable";
	public static final String ALL = "all";

	@Inject
	private IController controller;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String act = request.getParameter(ACT);
		try {
			if (act != null && act.length() > 0) {
				switch (act) {
				case TEMP:
					Float temperature = controller.getTemperature();
					response.getWriter().println("{\"" + TEMP + "\":" + temperature + "}");
					break;

				case HUM:
					Float humidity = controller.getHumidity();
					response.getWriter().println("{\"" + HUM + "\":" + humidity + "}");
					break;

				case ILLUM:
					Integer illumination = controller.getIllumination();
					response.getWriter().println("{\"" + ILLUM + "\":" + illumination + "}");
					break;

				case GABLE:
					GableState gState = controller.getGableState();
					response.getWriter().println("{\"" + GABLE + "\":" + gState.toString() + "}");
					break;

				case ALL:
					Float temp = controller.getTemperature();
					Float hum = controller.getHumidity();
					Integer illum = controller.getIllumination();
					GableState state = controller.getGableState();
					response.getWriter().println(
							"{\"" + TEMP + "\":" + temp + "," + "\"" + HUM + "\":" + hum + "," + "\"" + ILLUM + "\":"
									+ illum + "," + "\"" + GABLE + "\":" + state + "}");
					break;

				default:
					response.getWriter().println("{\"error\":\"No data\"}");
					break;
				}

			} else {
				response.getWriter().println("{\"error\":\"No data\"}");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

}
