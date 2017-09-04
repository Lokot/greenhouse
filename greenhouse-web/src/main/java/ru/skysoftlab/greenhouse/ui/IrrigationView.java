package ru.skysoftlab.greenhouse.ui;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.joda.time.Duration;

import ru.skysoftlab.greenhouse.common.CronExprTextConverter;
import ru.skysoftlab.greenhouse.common.DurationTextConverter;
import ru.skysoftlab.greenhouse.jpa.IrrigationCounturEntityProviderBean;
import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;
import ru.skysoftlab.greenhouse.web.MainMenu;
import ru.skysoftlab.greenhouse.web.Navigation;
import ru.skysoftlab.skylibs.security.RolesList;
import ru.skysoftlab.skylibs.web.annatations.MainMenuItem;
import ru.skysoftlab.skylibs.web.ui.AbstractGridView;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.cdi.CDIView;
import com.vaadin.data.Container.Indexed;

/**
 * Управление устройствами.
 * 
 * @author Loktionov Artem
 *
 */
@CDIView(Navigation.IRRIGATION)
@MainMenuItem(name = "Полив", order = MainMenu.IRRIGATION, hasChilds = false)
@RolesAllowed({ RolesList.ADMIN })
public class IrrigationView extends AbstractGridView<IrrigationCountur, IrrigationForm> {

	private static final long serialVersionUID = 6698245813955647506L;

	@Inject
	private IrrigationCounturEntityProviderBean entityProvider;

	@Inject
	private IrrigationForm form;

	public IrrigationView() {
		super(IrrigationCountur.class);
	}

	@Override
	protected void configureComponents() {
		super.configureComponents();
		getGrid().getColumn("cronExpr").setConverter(new CronExprTextConverter());
		getGrid().getColumn("duration").setConverter(new DurationTextConverter() {

			private static final long serialVersionUID = -9001409719898343333L;

			@Override
			public String convertToPresentation(Duration value, Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return super.convertToPresentation(value, targetType, locale) + " мин.";
			}
        });
	}

	@Override
	protected String getNewButtonLabel() {
		return "Новый контур";
	}
	
	@Override
	protected String getDelButtonLabel() {
		return "Удалить контур";
	}

	@Override
	protected Object[] getRemoveColumn() {
		return new String[] { "id" };
	}

	@Override
	protected Object[] getColumnOrder() {
		return new String[] { "name", "cronExpr", "duration", "pin" };
	}

	@Override
	protected Indexed refreshData(String value) {
		return getJpaContainer();
	}

	@Override
	protected String getTitle() {
		return "Список контуров";
	}

	@Override
	protected Map<String, String> getColumnsNames() {
		Map<String, String> rv = new HashMap<>();
		rv.put("name", "Наименование");
		rv.put("cronExpr", "Периодичность");
		rv.put("duration", "Продолжительность");
		rv.put("pin", "Пин на Arduino");
		return rv;
	}

	@Override
	protected EntityProvider<IrrigationCountur> getEntityProvider() {
		return entityProvider;
	}

	@Override
	protected IrrigationForm getEntityForm() {
		return form;
	}

}
