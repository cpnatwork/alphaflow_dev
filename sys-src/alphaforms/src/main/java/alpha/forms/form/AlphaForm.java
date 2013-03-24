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
package alpha.forms.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alpha.forms.designer.util.DraggedWidget;
import alpha.forms.form.naming.WidgetNameExistsException;
import alpha.forms.form.naming.WidgetNamingService;
import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.form.validation.ValidationLevel;
import alpha.forms.memento.model.AlphaFormMemento;
import alpha.forms.memento.model.FormMementoOriginator;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class AlphaForm.
 */
public class AlphaForm implements FormMementoOriginator, WidgetNamingService {

	/** The children. */
	private final Map<String, FormWidget> children = new HashMap<String, FormWidget>();

	/** The document states. */
	private final List<String> documentStates = new ArrayList<String>();

	/** The active document state. */
	private String activeDocumentState = null;

	/** The widget states. */
	protected Map<String, List<WidgetMemento>> widgetStates = new HashMap<String, List<WidgetMemento>>();

	/** The title. */
	@WidgetProperty(description = "The name of the form.")
	private String title;

	/** The width. */
	@WidgetProperty
	private int width = 600;

	/** The height. */
	@WidgetProperty
	private int height = 400;

	/** The validation level. */
	@WidgetProperty
	private ValidationLevel validationLevel = ValidationLevel.WARN;

	/** The min width. */
	private int minWidth = 10;

	/** The min height. */
	private int minHeight = 10;

	/**
	 * Instantiates a new alpha form.
	 */
	public AlphaForm() {
		this.title = "AlphaForm1";
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
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Sets the width.
	 * 
	 * @param width
	 *            the new width
	 */
	public void setWidth(final int width) {
		this.width = width;
	}

	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Sets the height.
	 * 
	 * @param height
	 *            the new height
	 */
	public void setHeight(final int height) {
		this.height = height;
	}

	/**
	 * Gets the min width.
	 * 
	 * @return the min width
	 */
	public int getMinWidth() {
		return this.minWidth;
	}

	/**
	 * Gets the min height.
	 * 
	 * @return the min height
	 */
	public int getMinHeight() {
		return this.minHeight;
	}

	/**
	 * Gets the widgets.
	 * 
	 * @return the widgets
	 */
	public Collection<FormWidget> getWidgets() {
		return this.children.values();
	}

	/**
	 * Adds the widget.
	 * 
	 * @param w
	 *            the w
	 */
	public void addWidget(final FormWidget w) {
		if (this.isWidget(w.getName())) {

		} else {
			this.children.put(w.getName(), w);
			this.recalculateDimensions(w);
		}
	}

	/**
	 * Checks if is widget.
	 * 
	 * @param name
	 *            the name
	 * @return true, if is widget
	 */
	public boolean isWidget(final String name) {
		return (this.children.get(name) != null);
	}

	/**
	 * Gets the widget.
	 * 
	 * @param name
	 *            the name
	 * @return the widget
	 */
	public FormWidget getWidget(final String name) {
		return this.children.get(name);
	}

	/**
	 * Gets the document states.
	 * 
	 * @return the document states
	 */
	public List<String> getDocumentStates() {
		return this.documentStates;
	}

	/**
	 * Adds the document states.
	 * 
	 * @param states
	 *            the states
	 */
	public void addDocumentStates(final Collection<String> states) {
		this.documentStates.addAll(states);
	}

	/**
	 * Adds the document state.
	 * 
	 * @param state
	 *            the state
	 */
	public void addDocumentState(final String state) {
		this.documentStates.add(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.FormMementoOriginator#createMemento()
	 */
	@Override
	public AlphaFormMemento createMemento() {
		final AlphaFormMemento m = new AlphaFormMemento();
		m.setTitle(this.title);
		m.setAttribute("height", this.height);
		m.setAttribute("width", this.width);
		m.setWidgets(new ArrayList<FormWidget>(this.children.values()));
		m.setWidgetStates(this.widgetStates);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.FormMementoOriginator#setMemento(alpha.forms
	 * .memento.model.AlphaFormMemento)
	 */
	@Override
	public void setMemento(final AlphaFormMemento m) {
		this.children.clear();
		if (m != null) {
			this.title = m.getTitle();
			this.width = Integer.parseInt(m.getAttribute("width").toString());
			this.height = Integer.parseInt(m.getAttribute("height").toString());
			for (final FormWidget w : m.getWidgets()) {
				this.children.put(w.getName(), w);
			}
		}
	}

	/**
	 * Creates the state.
	 * 
	 * @param name
	 *            the name
	 */
	public void createState(final String name) {
		final List<WidgetMemento> m = new ArrayList<WidgetMemento>();
		for (final FormWidget w : this.children.values()) {
			if (w instanceof MementoOriginator) {
				m.add(((MementoOriginator) w).createWidgetMemento());
			}
		}
		this.widgetStates.put(name, m);
	}

	/**
	 * Delete state.
	 * 
	 * @param name
	 *            the name
	 */
	public void deleteState(final String name) {
		this.widgetStates.remove(name);
	}

	/**
	 * Revert to state.
	 * 
	 * @param name
	 *            the name
	 */
	public void revertToState(final String name) {
		final List<WidgetMemento> mList = this.widgetStates.get(name);
		if (mList != null) {
			for (final WidgetMemento m : mList) {
				final FormWidget w = this.children.get(m.getName());
				if ((w != null) && (w instanceof MementoOriginator)) {
					((MementoOriginator) w).setWidgetMemento(m);
				}
			}
		}
	}

	/**
	 * Recalculate dimensions.
	 */
	public void recalculateDimensions() {
		this.minWidth = 10;
		this.minHeight = 10;
		this.width = this.minWidth;
		this.height = this.minHeight;
		for (final Entry<String, FormWidget> e : this.children.entrySet()) {
			this.recalculateDimensions(e.getValue());
		}
		this.width += 10;
		this.height += 10;
	}

	/**
	 * Recalculate dimensions.
	 * 
	 * @param w
	 *            the w
	 */
	public void recalculateDimensions(final FormWidget w) {
		this.minWidth = Math.max(this.minWidth, w.getX() + w.getWidth());
		this.minHeight = Math.max(this.minHeight, w.getY() + w.getHeight());
		this.width = Math.max(this.width, this.minWidth);
		this.height = Math.max(this.height, this.minHeight);
	}

	/**
	 * Recalculate dimensions.
	 * 
	 * @param w
	 *            the w
	 */
	public void recalculateDimensions(final DraggedWidget w) {
		this.minWidth = Math.max(this.minWidth, w.getX() + w.getWidth());
		this.minHeight = Math.max(this.minHeight, w.getY() + w.getHeight());
		this.width = Math.max(this.width, this.minWidth);
		this.height = Math.max(this.height, this.minHeight);
	}

	/**
	 * Gets the active document state.
	 * 
	 * @return the active document state
	 */
	public String getActiveDocumentState() {
		return this.activeDocumentState;
	}

	/**
	 * Sets the active document state.
	 * 
	 * @param activeDocumentState
	 *            the new active document state
	 */
	public void setActiveDocumentState(final String activeDocumentState) {
		if (this.documentStates.contains(activeDocumentState)) {
			this.activeDocumentState = activeDocumentState;
		}
	}

	/**
	 * Gets the validation level.
	 * 
	 * @return the validation level
	 */
	public ValidationLevel getValidationLevel() {
		return this.validationLevel;
	}

	/**
	 * Sets the validation level.
	 * 
	 * @param validationLevel
	 *            the new validation level
	 */
	public void setValidationLevel(final ValidationLevel validationLevel) {
		this.validationLevel = validationLevel;
	}

	/**
	 * Validate form.
	 * 
	 * @return the list
	 */
	public List<ValidationFailure> validateForm() {
		final List<ValidationFailure> validationFailedList = new ArrayList<ValidationFailure>();
		for (final FormWidget w : this.children.values()) {
			validationFailedList.addAll(w.validate());
		}
		return validationFailedList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.naming.WidgetNamingService#checkName(java.lang.String,
	 * alpha.forms.widget.model.FormWidget)
	 */
	@Override
	public String checkName(final String name, final FormWidget w)
			throws WidgetNameExistsException {
		if (this.checkNameExists(name))
			throw new WidgetNameExistsException("Widget with name '" + name
					+ "' already exists.");
		final String oldName = w.getName();
		final FormWidget storedWidget = this.children.get(oldName);
		if ((storedWidget == w) && (this.children.remove(oldName) != null)) {
			this.children.put(name, w);
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.naming.WidgetNamingService#checkNameExists(java.lang
	 * .String)
	 */
	@Override
	public boolean checkNameExists(final String name) {
		return this.children.containsKey(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.naming.WidgetNamingService#checkName(java.lang.String)
	 */
	@Override
	public String checkName(final String name) throws WidgetNameExistsException {
		if (this.checkNameExists(name))
			throw new WidgetNameExistsException("Widget with name '" + name
					+ "' already exists.");
		return name;
	}

}
