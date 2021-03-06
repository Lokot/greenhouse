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
package ru.skysoftlab.greenhouse.widgetset.client;

import org.vaadin.teemu.switchui.client.SwitchWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.vaadin.client.connectors.ClickableRendererConnector;
import com.vaadin.client.renderers.ClickableRenderer;
import com.vaadin.client.widget.grid.RendererCellReference;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;
import ru.skysoftlab.greenhouse.ui.renderers.SwitchRenderer;

/**
 * Connector for the CheckboxRenderer class.
 */
@Connect(SwitchRenderer.class)
public class SwitchRendererConnector extends ClickableRendererConnector<Boolean> {

	private static final long serialVersionUID = 5208520649600768229L;

	public SwitchRendererConnector() {
	}

	@Override
	public SwitchRenderer getRenderer() {
		return (SwitchRenderer) super.getRenderer();
	}

	@Override
	protected HandlerRegistration addClickHandler(ClickableRenderer.RendererClickHandler<JsonObject> handler) {
		return getRenderer().addClickHandler(handler);
	}

	/**
	 * Client-side implementation of the clickable checkbox renderer
	 */
	public static class SwitchRenderer extends ClickableRenderer<Boolean, SwitchWidget> {

		public SwitchRenderer() {
			super();
		}

		@Override
		public SwitchWidget createWidget() {
			SwitchWidget b = GWT.create(SwitchWidget.class);
			b.addClickHandler(this);
			return b;
		}

		@Override
		public void render(RendererCellReference cell, Boolean value, SwitchWidget checkBox) {
			checkBox.setValue(value, false);
		}

		@Override
		public void onClick(ClickEvent event) {
			super.onClick(event);
			// Don't let grid handle the events.
			event.stopPropagation();
		}
	}
}
