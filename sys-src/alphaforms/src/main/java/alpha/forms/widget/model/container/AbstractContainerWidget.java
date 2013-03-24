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
package alpha.forms.widget.model.container;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import alpha.forms.widget.model.FormWidget;

/**
 * The Class AbstractContainerWidget.
 */
public abstract class AbstractContainerWidget extends FormWidget implements
		ContainerWidget {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The child widgets. */
	protected List<FormWidget> childWidgets = new ArrayList<FormWidget>();

	/**
	 * Instantiates a new abstract container widget.
	 * 
	 * @param name
	 *            the name
	 */
	public AbstractContainerWidget(final String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.widget.model.container.ContainerWidget#doesAcceptWidget(alpha
	 * .forms.widget.model.FormWidget)
	 */
	@Override
	public boolean doesAcceptWidget(final FormWidget w) {
		return !(w instanceof ContainerWidget);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.container.ContainerWidget#getChildren()
	 */
	@Override
	public List<FormWidget> getChildren() {
		return this.childWidgets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.widget.model.container.ContainerWidget#setChildren(java.util
	 * .List)
	 */
	@Override
	public void setChildren(final List<FormWidget> widget) {
		this.childWidgets = widget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.widget.model.container.ContainerWidget#addChild(alpha.forms
	 * .widget.model.FormWidget)
	 */
	@Override
	public void addChild(final FormWidget w) {
		this.childWidgets.add(w);
		w.setParent(this);
		if (this.ui != null) {
			this.ui.doLayout();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.widget.model.container.ContainerWidget#removeChild(alpha.
	 * forms.widget.model.FormWidget)
	 */
	@Override
	public void removeChild(final FormWidget w) {
		this.childWidgets.remove(w);
		w.setParent(null);
		if (this.ui != null) {
			this.ui.doLayout();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.widget.model.container.ContainerWidget#getWidgetByLocation
	 * (java.awt.Point)
	 */
	@Override
	public FormWidget getWidgetByLocation(final Point p) {
		p.translate(-this.x, -this.y);
		for (final FormWidget w : this.childWidgets) {
			if (w.getBounds().contains(p))
				return w;
		}
		return null;
	}

	/**
	 * Gets the widget by name.
	 * 
	 * @param name
	 *            the name
	 * @return the widget by name
	 */
	public FormWidget getWidgetByName(final String name) {
		for (final FormWidget w : this.childWidgets) {
			if (w.getName().equals(name))
				return w;
		}
		return null;
	}

}
