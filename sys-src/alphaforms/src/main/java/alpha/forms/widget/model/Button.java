/**************************************************************************
 * alpha-Forms: self-editable formulars in form of an active document
 * (originally for the alpha-Flow project)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Florian Wagner
 **************************************************************************
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 **************************************************************************
 * $Id$
 *************************************************************************/
package alpha.forms.widget.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alpha.forms.form.event.Event;
import alpha.forms.form.event.EventFactory;
import alpha.forms.form.event.EventMemento;
import alpha.forms.memento.model.DynamicAttributeMemento;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.ValueMemento;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.view.ButtonUI;

/**
 * The Class Button.
 */
public class Button extends FormWidget implements MementoOriginator {

	/** The label. */
	@WidgetProperty(description = "This text will show on the button.")
	protected String label;

	/** The on click. */
	@WidgetProperty(description = "This event will fire when the user clicks the button.")
	protected Event onClick;

	/**
	 * Instantiates a new button.
	 * 
	 * @param name
	 *            the name
	 */
	public Button(final String name) {
		super(name);
		this.width = 130;
		this.height = 30;
		this.label = name;
		this.onClick = EventFactory.getInstance().createDefaultEvent(this);
		this.ui = new ButtonUI(this);
	}

	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            the new label
	 */
	public void setLabel(final String label) {
		this.label = label;
		this.ui.doLayout();
	}

	/**
	 * Gets the on click.
	 * 
	 * @return the on click
	 */
	public Event getOnClick() {
		return this.onClick;
	}

	/**
	 * Sets the on click.
	 * 
	 * @param onClick
	 *            the new on click
	 */
	public void setOnClick(final Event onClick) {
		this.onClick = onClick;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createWidgetMemento()
	 */
	@Override
	public WidgetMemento createWidgetMemento() {
		final WidgetMemento m = new WidgetMemento();
		m.setName(this.name);
		m.setType(this.getClass());
		m.setValue("");
		m.addAttribute("label", this.label);
		m.addAttribute("x", this.x);
		m.addAttribute("y", this.y);
		m.addAttribute("width", this.width);
		m.addAttribute("height", this.height);
		m.addAttribute("visible", this.visible);
		m.addAttribute("ui", this.ui.getClass().getName());
		m.setValidators(this.validators.createMemento());
		final List<EventMemento> events = new ArrayList<EventMemento>();
		final EventMemento ev = this.onClick.createMemento();
		ev.setEventName("onClick");
		events.add(ev);
		m.setEvents(events);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setWidgetMemento(alpha.forms
	 * .memento.model.WidgetMemento)
	 */
	@Override
	public void setWidgetMemento(final WidgetMemento m) {
		if (m != null) {
			this.name = m.getName();
			final Map<String, Object> attributes = m.getAttributes();
			this.label = attributes.get("label").toString();
			this.x = Integer.parseInt(attributes.get("x").toString());
			this.y = Integer.parseInt(attributes.get("y").toString());
			this.width = Integer.parseInt(attributes.get("width").toString());
			this.height = Integer.parseInt(attributes.get("height").toString());
			this.visible = Boolean.parseBoolean(attributes.get("visible")
					.toString());
			this.setSize(this.width, this.height);
			this.setX(this.x);
			this.setY(this.y);
			this.validators.setMemento(m.getValidators());
			for (final EventMemento em : m.getEvents()) {
				if (em.getEventName().equals("onClick")) {
					this.onClick.setMemento(em);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#createDynamicAttributeMemento
	 * (alpha.forms.memento.model.WidgetMemento)
	 */
	@Override
	public DynamicAttributeMemento createDynamicAttributeMemento(
			final WidgetMemento ref) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setDynamicMemento(alpha.forms
	 * .memento.model.DynamicAttributeMemento)
	 */
	@Override
	public void setDynamicMemento(final DynamicAttributeMemento m) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createValueMemento()
	 */
	@Override
	public ValueMemento createValueMemento() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setValueMemento(alpha.forms
	 * .memento.model.ValueMemento)
	 */
	@Override
	public void setValueMemento(final ValueMemento m) {
	}

}
