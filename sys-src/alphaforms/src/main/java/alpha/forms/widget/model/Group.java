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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.memento.model.DynamicAttributeMemento;
import alpha.forms.memento.model.GroupValueMemento;
import alpha.forms.memento.model.GroupWidgetMemento;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.ValueMemento;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.model.container.AbstractContainerWidget;
import alpha.forms.widget.util.WidgetFactory;
import alpha.forms.widget.view.GroupUI;

/**
 * The Class Group.
 */
public class Group extends AbstractContainerWidget implements MementoOriginator {

	/** The title. */
	@WidgetProperty
	private String title;

	/** The border. */
	@WidgetProperty
	private BorderType border = BorderType.TITLED;

	/**
	 * Instantiates a new group.
	 * 
	 * @param name
	 *            the name
	 */
	public Group(final String name) {
		super(name);
		this.title = name;
		this.width = 150;
		this.height = 60;
		this.ui = new GroupUI(this);
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Gets the border.
	 * 
	 * @return the border
	 */
	public BorderType getBorder() {
		return this.border;
	}

	/**
	 * Sets the border.
	 * 
	 * @param border
	 *            the new border
	 */
	public void setBorder(final BorderType border) {
		this.border = border;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMinimumHeight()
	 */
	@Override
	public int getMinimumHeight() {
		return 55;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMiniumWidth()
	 */
	@Override
	public int getMiniumWidth() {
		return 100;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getDescription()
	 */
	@Override
	public String getDescription() {
		return "A option group.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createWidgetMemento()
	 */
	@Override
	public WidgetMemento createWidgetMemento() {
		final WidgetMemento m = new GroupWidgetMemento();
		m.setName(this.name);
		m.setType(this.getClass());
		m.setValidators(this.validators.createMemento());
		m.addAttribute("title", this.title);
		m.addAttribute("border", this.border);
		m.addAttribute("x", this.x);
		m.addAttribute("y", this.y);
		m.addAttribute("width", this.width);
		m.addAttribute("height", this.height);
		m.addAttribute("ui", this.ui.getClass().getName());
		final List<FormWidget> copyChildList = new ArrayList<FormWidget>();
		for (final FormWidget w : this.childWidgets) {
			copyChildList.add(WidgetFactory.cloneWidget(w));
		}
		m.setValue(copyChildList);
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
			final Iterator<FormWidget> i = this.childWidgets.iterator();
			while (i.hasNext()) {
				final FormWidget w = i.next();
				i.remove();
				this.removeChild(w);
			}
			this.name = m.getName();
			for (final FormWidget w : (List<FormWidget>) m.getValue()) {
				this.addChild(WidgetFactory.cloneWidget(w));
			}
			final Map<String, Object> attributes = m.getAttributes();
			this.title = attributes.get("title").toString();
			this.border = BorderType.valueOf(attributes.get("border")
					.toString());
			this.x = Integer.parseInt(attributes.get("x").toString());
			this.y = Integer.parseInt(attributes.get("y").toString());
			this.width = Integer.parseInt(attributes.get("width").toString());
			this.height = Integer.parseInt(attributes.get("height").toString());
			this.setSize(this.width, this.height);
			this.setX(this.x);
			this.setY(this.y);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.widget.model.container.AbstractContainerWidget#addChild(alpha
	 * .forms.widget.model.FormWidget)
	 */
	@Override
	public void addChild(final FormWidget w) {
		super.addChild(w);
		((GroupUI) this.ui).add(w);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.widget.model.container.AbstractContainerWidget#removeChild
	 * (alpha.forms.widget.model.FormWidget)
	 */
	@Override
	public void removeChild(final FormWidget w) {
		((GroupUI) this.ui).remove(w);
		super.removeChild(w);
		this.ui.doLayout();
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createValueMemento()
	 */
	@Override
	public ValueMemento createValueMemento() {
		final ValueMemento m = new GroupValueMemento(this);
		m.setName(this.name);
		final List<FormWidget> clonedWidgets = new ArrayList<FormWidget>();
		for (final FormWidget w : this.childWidgets) {
			clonedWidgets.add(WidgetFactory.cloneWidget(w));
		}
		m.setValue(new ArrayList<FormWidget>(clonedWidgets));
		return m;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#validate()
	 */
	@Override
	public Set<ValidationFailure> validate() {
		final Set<ValidationFailure> failures = new HashSet<ValidationFailure>();
		for (final FormWidget w : this.childWidgets) {
			failures.addAll(w.validate());
		}
		return failures;
	}

}
