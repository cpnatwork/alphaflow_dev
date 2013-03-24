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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import alpha.forms.widget.model.Button;

/**
 * The Class ButtonUI.
 */
public class ButtonUI extends FormWidgetUI {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5762456869414030415L;

	/** The model. */
	Button model;

	/** The button. */
	JButton button;

	/**
	 * Instantiates a new button ui.
	 * 
	 * @param model
	 *            the model
	 */
	public ButtonUI(final Button model) {
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
		this.button = new JButton(this.model.getLabel());
		this.doLayout();

		this.button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				ButtonUI.this.model.getOnClick().fire();
			}
		});
		this.add(this.button);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#doLayout()
	 */
	@Override
	public void doLayout() {
		super.setBounds(this.model.getX(), this.model.getY(),
				this.model.getWidth(), this.model.getHeight());
		super.doLayout();

		this.button.setSize(this.model.getSize());
		this.button.setLocation(0, 0);
		this.button.setText(this.model.getLabel());
	}

}
