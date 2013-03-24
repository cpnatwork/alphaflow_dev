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

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import alpha.forms.designer.view.FormCanvas;
import alpha.forms.form.AlphaForm;

/**
 * The Class ShrinkToFitAction.
 */
public class ShrinkToFitAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7491685835386129998L;

	/** The form. */
	private final AlphaForm form;

	/** The canvas. */
	private final FormCanvas canvas;

	/**
	 * Instantiates a new shrink to fit action.
	 * 
	 * @param text
	 *            the text
	 * @param form
	 *            the form
	 * @param canvas
	 *            the canvas
	 */
	public ShrinkToFitAction(final String text, final AlphaForm form,
			final FormCanvas canvas) {
		super(text);
		this.form = form;
		this.canvas = canvas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent ev) {
		this.form.recalculateDimensions();
		this.canvas.setSize(new Dimension(this.form.getWidth(), this.form
				.getHeight()));
	}

}
