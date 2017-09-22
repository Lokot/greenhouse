package ru.skysoftlab.greenhouse.ui;

import static ru.skysoftlab.skylibs.web.ui.VaadinUtils.comboboxReadOnlyAndSelectFirst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.greenhouse.jpa.entitys.drools.Condition;
import ru.skysoftlab.greenhouse.jpa.entitys.drools.Rule;
import ru.skysoftlab.greenhouse.ui.components.ConditionComponent;
import ru.skysoftlab.greenhouse.ui.converters.GableStateConverter;
import ru.skysoftlab.skylibs.web.dto.VaadinItemDto;
import ru.skysoftlab.skylibs.web.ui.AbstractForm;

public class RuleForm extends AbstractForm<Rule> {

	private static final long serialVersionUID = -7745572868469404876L;

	public static final VaadinItemDto GABLE_CLOSE = new VaadinItemDto(GableState.Close, "Закрыть");
	public static final VaadinItemDto GABLE_30 = new VaadinItemDto(GableState.Degrees30, "Открыть на 30%");
	public static final VaadinItemDto GABLE_60 = new VaadinItemDto(GableState.Degrees60, "Открыть на 60%");
	public static final VaadinItemDto GABLE_OPEN = new VaadinItemDto(GableState.Open, "Открыть на 100%");

	private ComboBox state = new ComboBox("Конек:");
	private Label condLabel = new Label("Критерии:");
	private GridLayout conditionLayout = new GridLayout(2, 1);
	private Button addConditionButton = new Button("Добавить критерий");

	@Override
	protected void configureComponents() {
		state.addItem(GABLE_CLOSE);
		state.addItem(GABLE_30);
		state.addItem(GABLE_60);
		state.addItem(GABLE_OPEN);
		state.setConverter(new GableStateConverter());
		comboboxReadOnlyAndSelectFirst(state);

		addConditionButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 5160428473426477070L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConditionComponent conditionComponent = new ConditionComponent("");
				addConditionComponent(conditionComponent);
			}
		});

		conditionLayout.setSpacing(true);
		
		super.configureComponents();
	}

	@Override
	public void edit(Rule rule) {
		super.edit(rule);
		conditionLayout.removeAllComponents();
		if (entity != null && entity.getConditions() != null) {
			for (Condition condition : entity.getConditions()) {
				ConditionComponent conditionComponent = new ConditionComponent("");
				conditionComponent.setValue(condition);
				addConditionComponent(conditionComponent);
			}
		}
	}

	private void addConditionComponent(ConditionComponent conditionComponent) {
		conditionLayout.addComponent(conditionComponent);
		Button delCondButton = new Button("Удалить");
		delCondButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -2606641158649165000L;

			@Override
			public void buttonClick(ClickEvent event) {
				conditionLayout.removeComponent(conditionComponent);
				conditionLayout.removeComponent(delCondButton);
			}

		});
		conditionLayout.addComponent(delCondButton);
		conditionLayout.setComponentAlignment(delCondButton, Alignment.MIDDLE_CENTER);
	}

	@Override
	protected Button.ClickListener getSaveClickListener() {
		final Button.ClickListener superListener = super.getSaveClickListener();
		return new Button.ClickListener() {

			private static final long serialVersionUID = 4489078023605774389L;

			@Override
			public void buttonClick(ClickEvent event) {
				List<Condition> conditions = new ArrayList<>();
				for (Component conditionComponent : conditionLayout) {
					if (conditionComponent instanceof ConditionComponent) {
						conditions.add(((ConditionComponent) conditionComponent).getValue());
					}
				}
				if (conditions.size() > 0) {
					entity.setConditions(conditions);
					superListener.buttonClick(event);
				} else {
					Notification.show("Не заданы условия для правила.", Type.ERROR_MESSAGE);
				}
			}
		};
	}

	@Override
	protected Collection<? extends Component> getInputs() {
		Collection<Component> rv = new ArrayList<>();
		rv.add(state);
		rv.add(condLabel);
		rv.add(conditionLayout);
		rv.add(addConditionButton);
		return rv;
	}

	@Override
	protected void setFocus() {
		state.focus();
	}

}
