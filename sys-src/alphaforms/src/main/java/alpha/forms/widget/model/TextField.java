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

import java.util.Map;
import java.util.Set;

import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.memento.model.DynamicAttributeMemento;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.ValueMemento;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.util.WidgetLabelPosition;
import alpha.forms.widget.view.TextFieldUI;

/**
 * The Class TextField.
 */
public class TextField extends FormWidget implements MementoOriginator {

	/** The label. */
	@WidgetProperty(description = "The label text.")
	protected String label;

	/** The show label. */
	@WidgetProperty(name = "labelOrientation", description = "Determines the position of the label in relation to the text input field.")
	protected WidgetLabelPosition showLabel = WidgetLabelPosition.LEFT;

	/** The text. */
	@WidgetProperty(description = "The content of this widget.")
	protected String text;

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(final String text) {
		this.text = text;
		this.ui.updateUI();
	}

	/**
	 * Instantiates a new text field.
	 * 
	 * @param name
	 *            the name
	 */
	public TextField(final String name) {
		super(name);
		this.width = 200;
		this.height = 22;
		this.label = name;
		this.ui = new TextFieldUI(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#validate()
	 */
	@Override
	public Set<ValidationFailure> validate() {
		return this.validateValue(this.text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getDescription()
	 */
	@Override
	public String getDescription() {
		return "A text input field.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMinimumHeight()
	 */
	@Override
	public int getMinimumHeight() {
		return this.ui.getMinimumSize().height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMiniumWidth()
	 */
	@Override
	public int getMiniumWidth() {
		return 300;
	}

	/**
	 * Gets the show label.
	 * 
	 * @return the show label
	 */
	public WidgetLabelPosition getShowLabel() {
		return this.showLabel;
	}

	/**
	 * Sets the show label.
	 * 
	 * @param showLabel
	 *            the new show label
	 */
	public void setShowLabel(final WidgetLabelPosition showLabel) {
		this.showLabel = showLabel;
		this.ui.updateUI();
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
		this.ui.revalidate();
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
		m.setValue(this.getText());
		m.addAttribute("label", this.label);
		m.addAttribute("showLabel", this.showLabel);
		m.addAttribute("x", this.x);
		m.addAttribute("y", this.y);
		m.addAttribute("width", this.width);
		m.addAttribute("height", this.height);
		m.addAttribute("ui", this.ui.getClass().getName());
		m.addAttribute("visible", this.visible);
		m.setValidators(this.validators.createMemento());
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
			this.showLabel = WidgetLabelPosition.valueOf(attributes.get(
					"showLabel").toString());
			this.x = Integer.parseInt(attributes.get("x").toString());
			this.y = Integer.parseInt(attributes.get("y").toString());
			this.width = Integer.parseInt(attributes.get("width").toString());
			this.height = Integer.parseInt(attributes.get("height").toString());
			this.visible = Boolean.parseBoolean(attributes.get("visible")
					.toString());
			this.setSize(this.width, this.height);
			this.setX(this.x);
			this.setY(this.y);
			this.text = (m.getValue() == null) ? "" : m.getValue().toString();
			this.validators.setMemento(m.getValidators());
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createValueMemento()
	 */
	@Override
	public ValueMemento createValueMemento() {
		final ValueMemento v = new ValueMemento();
		v.setName(this.name);
		v.setValue(this.text);
		return v;
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
		this.text = m.getValue().toString();
	}

}
