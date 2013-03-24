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
package alpha.forms.application.view.panel;

import javax.swing.JPanel;

import alpha.forms.application.controller.ApplicationController;

/**
 * The Class FormView.
 */
public abstract class FormView extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The parent. */
	private JPanel parent;

	/** The controller. */
	protected ApplicationController controller;

	/**
	 * Instantiates a new form view.
	 * 
	 * @param controller
	 *            the controller
	 */
	public FormView(final ApplicationController controller) {
		this.controller = controller;
	}

	/**
	 * Show.
	 * 
	 * @param parent
	 *            the parent
	 */
	public void show(final JPanel parent) {
		this.parent = parent;
		this.parent.add(this);
	}

	/**
	 * Pause view.
	 */
	public void pauseView() {
		this.parent.remove(this);
	}

	/**
	 * Unpause view.
	 */
	public void unpauseView() {
		this.parent.add(this);
	}

	/**
	 * Stop view.
	 */
	public void stopView() {

	}
}
