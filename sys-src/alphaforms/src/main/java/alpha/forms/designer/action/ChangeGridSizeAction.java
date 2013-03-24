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
package alpha.forms.designer.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import alpha.forms.designer.view.FormCanvas;
import alpha.forms.form.AlphaForm;

/**
 * The Class ChangeGridSizeAction.
 */
public class ChangeGridSizeAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3139029449611792433L;

	/** The form. */
	private final AlphaForm form;

	/** The canvas. */
	private final FormCanvas canvas;

	/** The size. */
	private final int size;

	/**
	 * Instantiates a new change grid size action.
	 * 
	 * @param text
	 *            the text
	 * @param size
	 *            the size
	 * @param form
	 *            the form
	 * @param canvas
	 *            the canvas
	 */
	public ChangeGridSizeAction(final String text, final int size,
			final AlphaForm form, final FormCanvas canvas) {
		super(text);
		this.form = form;
		this.canvas = canvas;
		this.size = size;
		this.putValue(Action.SELECTED_KEY, canvas.getGridSize() == size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent ev) {
		this.canvas.setGridSize(this.size);
		this.putValue(Action.SELECTED_KEY,
				this.canvas.getGridSize() == this.size);
		this.canvas.repaint();
	}
}
