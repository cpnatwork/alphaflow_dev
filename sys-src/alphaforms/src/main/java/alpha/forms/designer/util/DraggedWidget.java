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
package alpha.forms.designer.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;

import alpha.forms.form.AlphaForm;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.container.ContainerWidget;

/**
 * The Class DraggedWidget.
 */
public class DraggedWidget {

	/** The widget. */
	protected FormWidget widget;

	/** The x. */
	protected int x;

	/** The y. */
	protected int y;

	/** The width. */
	protected int width;

	/** The height. */
	protected int height;

	/** The parent. */
	protected ContainerWidget parent = null;

	/** The model. */
	protected AlphaForm model = null;

	/**
	 * Instantiates a new dragged widget.
	 * 
	 * @param widget
	 *            the widget
	 * @param model
	 *            the model
	 */
	public DraggedWidget(final FormWidget widget, final AlphaForm model) {
		super();
		this.widget = widget;
		this.model = model;
		if (widget.getParent() == null) {
			this.x = widget.getX();
			this.y = widget.getY();

		} else {
			this.parent = (ContainerWidget) widget.getParent();
			this.x = widget.getX() + ((FormWidget) this.parent).getX();
			this.y = widget.getY() + ((FormWidget) this.parent).getY();
		}
		this.width = widget.getWidth();
		this.height = widget.getHeight();

	}

	/**
	 * Gets the widget.
	 * 
	 * @return the widget
	 */
	public FormWidget getWidget() {
		return this.widget;
	}

	/**
	 * Sets the widget.
	 * 
	 * @param widget
	 *            the new widget
	 */
	public void setWidget(final FormWidget widget) {
		this.widget = widget;
	}

	/**
	 * Gets the x.
	 * 
	 * @return the x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Sets the x.
	 * 
	 * @param x
	 *            the new x
	 */
	public void setX(final int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 * 
	 * @return the y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Sets the y.
	 * 
	 * @param y
	 *            the new y
	 */
	public void setY(final int y) {
		this.y = y;
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
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public ContainerWidget getParent() {
		return this.parent;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            the new parent
	 */
	public void setParent(final ContainerWidget parent) {
		this.parent = parent;
	}

	/**
	 * Render.
	 * 
	 * @param g
	 *            the g
	 */
	public void render(final Graphics2D g) {
		g.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		final Image img = this.widget.getUi().getAsImage();
		g.drawImage(img, this.x, this.y, null);
	}

	/**
	 * Show dropable overlay.
	 * 
	 * @param c
	 *            the c
	 * @return true, if successful
	 */
	public boolean showDropableOverlay(final ContainerWidget c) {
		return ((this.parent == null) || (this.parent != c));
	}

	/**
	 * Finalize.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param parent
	 *            the parent
	 */
	public void finalize(final int x, final int y, final ContainerWidget parent) {
		if (parent == null) {
			if (this.parent != null) {
				this.parent.removeChild(this.widget);
				this.model.addWidget(this.widget);
				this.parent = null;
			}
			this.widget.setX(x);
			this.widget.setY(y);
		} else if (parent != this.parent) {
			if (this.parent == null) {
				this.model.getWidgets().remove(this.widget);
				parent.addChild(this.widget);
				this.parent = parent;
			} else {
				this.parent.removeChild(this.widget);
				parent.addChild(this.widget);
				this.parent = parent;
			}
			this.widget.setX(x - ((FormWidget) parent).getX());
			this.widget.setY(y - ((FormWidget) parent).getY());
		} else {
			this.widget.setX(x - ((FormWidget) parent).getX());
			this.widget.setY(y - ((FormWidget) parent).getY());
		}
	}

}
