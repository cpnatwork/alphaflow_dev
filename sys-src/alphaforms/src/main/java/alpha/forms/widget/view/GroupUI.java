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
package alpha.forms.widget.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;

import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.Group;

/**
 * The Class GroupUI.
 */
public class GroupUI extends FormWidgetUI {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private final Group model;

	/**
	 * Instantiates a new group ui.
	 * 
	 * @param model
	 *            the model
	 */
	public GroupUI(final Group model) {
		super(model);
		this.model = model;
		this.compose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#compose()
	 */
	@Override
	protected void compose() {
		// super.compose();
		this.setLayout(null);
		this.removeAll();
		for (final FormWidget w : this.model.getChildren()) {
			this.add(w.getUi());
		}
		this.doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#doLayout()
	 */
	@Override
	public void doLayout() {
		super.doLayout();
		switch (this.model.getBorder()) {
		case TITLED:
			this.setBorder(BorderFactory.createTitledBorder(this.model
					.getTitle()));
			break;
		case UNTITLED:
			this.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
			break;
		case NONE:
		default:
			this.setBorder(null);
		}
		for (final FormWidget w : this.model.getChildren()) {
			w.getUi().doLayout();
		}
		super.setSize(this.model.getSize());
		this.setVisible(this.model.isVisible());
	}

	/**
	 * Find component.
	 * 
	 * @param name
	 *            the name
	 * @return the form widget
	 */
	public FormWidget findComponent(final String name) {
		for (final Component c : this.getComponents()) {
			if ((c instanceof FormWidgetUI)
					&& ((FormWidgetUI) c).model.getName().equals(name))
				return ((FormWidgetUI) c).model;
		}
		return null;
	}

	/**
	 * Adds the.
	 * 
	 * @param w
	 *            the w
	 */
	public void add(final FormWidget w) {
		this.add(w.getUi());
		this.validate();
		this.doLayout();
	}

	/**
	 * Removes the.
	 * 
	 * @param w
	 *            the w
	 */
	public void remove(final FormWidget w) {
		this.remove(w.getUi());
		this.validate();
		this.doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	@Override
	public void setSize(final Dimension d) {
		super.setSize(d);
		this.doLayout();
	}

}
