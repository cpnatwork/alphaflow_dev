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
package alpha.forms.designer.view;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import alpha.forms.form.AlphaForm;
import alpha.forms.widget.model.container.AbstractContainerWidget;

/**
 * The Class FocusArea.
 */
public class FocusArea {

	/** The model. */
	private final AlphaForm model;

	/** The parent. */
	private final AbstractContainerWidget parent;

	/** The canvas. */
	private final FormCanvas canvas;

	/**
	 * Instantiates a new focus area.
	 * 
	 * @param model
	 *            the model
	 * @param parent
	 *            the parent
	 * @param canvas
	 *            the canvas
	 */
	public FocusArea(final AlphaForm model,
			final AbstractContainerWidget parent, final FormCanvas canvas) {
		this.model = model;
		this.parent = parent;
		this.canvas = canvas;
	}

	/**
	 * Gets the focus area bounds.
	 * 
	 * @return the focus area bounds
	 */
	public Rectangle getFocusAreaBounds() {
		if (this.parent != null)
			return this.parent.getBounds();
		else
			return this.canvas.getBounds();
	}

	/**
	 * Translate point.
	 * 
	 * @param p
	 *            the p
	 * @return the point
	 */
	public Point translatePoint(final Point p) {
		if (this.parent != null)
			return new Point(p.x - this.parent.getX(), p.y - this.parent.getY());
		else
			return new Point(p);
	}

	/**
	 * Translate rect.
	 * 
	 * @param rect
	 *            the rect
	 * @return the rectangle
	 */
	public Rectangle translateRect(final Rectangle rect) {
		if (this.parent != null) {
			rect.translate(this.parent.getX(), this.parent.getY());
			return new Rectangle(rect);
		} else
			return new Rectangle(rect);
	}

	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		if (this.parent != null)
			return this.parent.getWidth();
		else
			return this.canvas.getWidth();
	}

	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		if (this.parent != null)
			return this.parent.getHeight();
		else
			return this.canvas.getHeight();
	}

	/**
	 * Update ui.
	 */
	public void updateUI() {
		if (this.parent != null) {
			this.parent.getUi().updateUI();
		} else {
			this.canvas.updateUI();
		}
	}

	/**
	 * Draw overlay outside.
	 * 
	 * @param g
	 *            the g
	 */
	public void drawOverlayOutside(final Graphics2D g) {

	}

	/**
	 * Draw overlay inside.
	 * 
	 * @param g
	 *            the g
	 */
	public void drawOverlayInside(final Graphics2D g) {

	}

}
