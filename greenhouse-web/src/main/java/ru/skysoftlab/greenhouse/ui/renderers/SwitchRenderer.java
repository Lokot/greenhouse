/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.skysoftlab.greenhouse.ui.renderers;

import static ru.skysoftlab.skylibs.common.EditableEntityState.UPDATE;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.renderers.ClickableRenderer;

import ru.skysoftlab.skylibs.events.EntityChangeEvent;

/**
 * CheckboxRenderer that supports updating data on the server when checkbox is
 * clicked.
 */
public class SwitchRenderer extends ClickableRenderer<Boolean> {

	private static final long serialVersionUID = -3268403895597719476L;

	@Inject
	private Event<EntityChangeEvent> entityChangeEvent;

	public SwitchRenderer() {
		super(Boolean.class, null);
	}

	@PostConstruct
	private void init() {
		addClickListener(new RendererClickListener() {

			private static final long serialVersionUID = 7262317462934839612L;

			@Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void click(RendererClickEvent event) {
				Object itemId = event.getItemId();
				Object propertyId = event.getPropertyId();

				Container.Indexed containerDataSource = getParentGrid().getContainerDataSource();
				Property<Boolean> itemProperty = containerDataSource.getItem(itemId).getItemProperty(propertyId);
				boolean value = !Boolean.TRUE.equals(itemProperty.getValue());
				itemProperty.setValue(value);

				JPAContainer jpaContainer = (JPAContainer) containerDataSource;
				Object entity = jpaContainer.getItem(itemId).getEntity();
				jpaContainer.addEntity(entity);
				// событие изменения настроек
				entityChangeEvent.fire(new EntityChangeEvent(itemId, entity.getClass(), UPDATE));
			}
		});
	}

}
