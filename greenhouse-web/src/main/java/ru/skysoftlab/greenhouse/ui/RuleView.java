package ru.skysoftlab.greenhouse.ui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.cdi.CDIView;
import com.vaadin.data.Container.Indexed;

import ru.skysoftlab.greenhouse.jpa.RuleEntityProviderBean;
import ru.skysoftlab.greenhouse.jpa.entitys.drools.Rule;
import ru.skysoftlab.greenhouse.ui.converters.ConditionStringConverter;
import ru.skysoftlab.greenhouse.ui.converters.GableStateStringConverter;
import ru.skysoftlab.greenhouse.web.MainMenu;
import ru.skysoftlab.greenhouse.web.Navigation;
import ru.skysoftlab.greenhouse.web.MainMenu.ConfigMenu;
import ru.skysoftlab.skylibs.security.RolesList;
import ru.skysoftlab.skylibs.web.annatations.MainMenuItem;
import ru.skysoftlab.skylibs.web.annatations.MenuItemView;
import ru.skysoftlab.skylibs.web.ui.AbstractGridView;

@CDIView(Navigation.RULES)
@MainMenuItem(name = "Настройки", order = MainMenu.CONFIG)
@MenuItemView(name = "Правила", order = ConfigMenu.ROOLS)
@RolesAllowed({ RolesList.ADMIN })
public class RuleView extends AbstractGridView<Rule, RuleForm> {

	private static final long serialVersionUID = 1590529427015447316L;
	
	@Inject
	private RuleEntityProviderBean entityProvider;
	
	@Inject
	private ConditionStringConverter conditionConverter;

	@Inject
	private RuleForm form;
	
	public RuleView() {
		super(Rule.class);
	}
	
	@Override
	protected void configureGrid() {
		grid.getColumn("conditions").setConverter(conditionConverter);
		grid.getColumn("state").setConverter(new GableStateStringConverter());
	}

	@Override
	protected EntityProvider<Rule> getEntityProvider() {
		return entityProvider;
	}

	@Override
	protected RuleForm getEntityForm() {
		return form;
	}

	@Override
	protected String getTitle() {
		return "Правила управления коньком";
	}

	@Override
	protected Map<String, String> getColumnsNames() {
		Map<String, String> rv = new HashMap<>();
		rv.put("conditions", "Критерий");
		rv.put("state", "Состояние конька");
		return rv;
	}

	@Override
	protected String getNewButtonLabel() {
		return "Новое правило";
	}

	@Override
	protected String getDelButtonLabel() {
		return "Удалить правило";
	}

	@Override
	protected Object[] getRemoveColumn() {
		return new String[] { "id" };
	}

	@Override
	protected Object[] getColumnOrder() {
		return new String[] { "conditions", "state" };
	}

	@Override
	protected Indexed refreshData(String value) {
		return getJpaContainer();
	}

}
