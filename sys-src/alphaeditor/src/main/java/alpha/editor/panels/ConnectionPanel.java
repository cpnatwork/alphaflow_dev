/**************************************************************************
 * alpha-Flow: distributed case files in form of active documents
 * (supporting knowledge-driven ad-hoc processes in healthcare)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
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
package alpha.editor.panels;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import alpha.model.identification.AlphaCardID;

/**
 * This class displays the connecting line between related AlphaCardDescriptor
 * pairs.
 * 
 */
public class ConnectionPanel extends JPanel {

	/** The src. */
	private AlphaCardID src;

	/** The dst. */
	private AlphaCardID dst;

	/**
	 * Instantiates a new connection panel.
	 */
	public ConnectionPanel() {
		super();
	}

	/**
	 * Instantiates a new connection panel.
	 * 
	 * @param isDoubleBuffered
	 *            the is double buffered
	 */
	public ConnectionPanel(final boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	/**
	 * Instantiates a new connection panel.
	 * 
	 * @param layout
	 *            the layout
	 * @param isDoubleBuffered
	 *            the is double buffered
	 */
	public ConnectionPanel(final LayoutManager layout,
			final boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	/**
	 * Instantiates a new connection panel.
	 * 
	 * @param layout
	 *            the layout
	 */
	public ConnectionPanel(final LayoutManager layout) {
		super(layout);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5613216096764217039L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(final Graphics g) {
		// Graphics2D g2 = (Graphics2D)g;
		// Dimension d = this.getSize();
		// int h = (int)Double.doubleToLongBits(d.getHeight());
		// int w = (int)Double.doubleToLongBits(d.getWidth());
		g.drawRect(0, 10, 600, 1);
		g.fillRect(0, 10, 600, 1);
		// g2.drawLine(0, 10, 60, 10);
	}

	/**
	 * Gets the dst.
	 * 
	 * @return the dst
	 */
	public AlphaCardID getDst() {
		return this.dst;
	}

	/**
	 * Sets the dst.
	 * 
	 * @param dst
	 *            the dst to set
	 */
	public void setDst(final AlphaCardID dst) {
		this.dst = dst;
	}

	/**
	 * Gets the src.
	 * 
	 * @return the src
	 */
	public AlphaCardID getSrc() {
		return this.src;
	}

	/**
	 * Sets the src.
	 * 
	 * @param src
	 *            the src to set
	 */
	public void setSrc(final AlphaCardID src) {
		this.src = src;
	}

}
