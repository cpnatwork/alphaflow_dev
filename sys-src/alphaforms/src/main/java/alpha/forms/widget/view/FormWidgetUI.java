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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import alpha.forms.widget.model.FormWidget;

/**
 * The Class FormWidgetUI.
 */
public abstract class FormWidgetUI extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	protected FormWidget model;

	/** The minimum width. */
	protected int minimumWidth = 0;

	/** The minimum height. */
	protected int minimumHeight = 0;

	/**
	 * Instantiates a new form widget ui.
	 * 
	 * @param model
	 *            the model
	 */
	public FormWidgetUI(final FormWidget model) {
		this.model = model;
	}

	/**
	 * Compose.
	 */
	protected void compose() {
		// put together the widget
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	@Override
	public Dimension getMinimumSize() {
		this.doLayout();
		return new Dimension(this.minimumHeight, this.minimumHeight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#getBounds()
	 */
	@Override
	public Rectangle getBounds() {
		return new Rectangle(this.getX(), this.getY(), this.model.getWidth(),
				this.model.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.model.getWidth(), this.model.getHeight());
	}

	/**
	 * Gets the as image.
	 * 
	 * @return the as image
	 */
	public Image getAsImage() {
		final BufferedImage im = new BufferedImage(this.model.getWidth(),
				this.model.getHeight(), BufferedImage.TYPE_INT_RGB);
		final Graphics g = im.createGraphics();
		this.doLayout();
		this.setOpaque(true);
		this.addNotify();
		this.validate();
		this.printAll(g);
		// paint(g);
		g.dispose();
		return im;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#doLayout()
	 */
	@Override
	public void doLayout() {
		super.doLayout();
		this.setVisible(this.model.isVisible());
	}

	/**
	 * Gets the subselection.
	 * 
	 * @return the subselection
	 */
	public Rectangle getSubselection() {
		return null;
	}

	/**
	 * Checks if is subselection resizable.
	 * 
	 * @param direction
	 *            the direction
	 * @return true, if is subselection resizable
	 */
	public boolean isSubselectionResizable(final int direction) {
		return false;
	}

	/**
	 * Update subselection size.
	 * 
	 * @param delta
	 *            the delta
	 * @param direction
	 *            the direction
	 */
	public void updateSubselectionSize(final int delta, final int direction) {

	}

}
