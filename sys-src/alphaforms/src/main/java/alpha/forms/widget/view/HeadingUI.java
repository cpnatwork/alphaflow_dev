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
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import alpha.forms.widget.model.Heading;

/**
 * The Class HeadingUI.
 */
public class HeadingUI extends FormWidgetUI {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private final Heading model;

	/** The label. */
	private JLabel label;

	/**
	 * Instantiates a new heading ui.
	 * 
	 * @param model
	 *            the model
	 */
	public HeadingUI(final Heading model) {
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
		this.label = new JLabel(this.model.getText());
		this.label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.BLACK));
		final Font font = this.label.getFont().deriveFont(Font.BOLD, 16);
		this.label.setFont(font);
		this.add(this.label);
		this.doLayout();
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

		this.label.setSize(this.model.getSize());
		this.label.setLocation(0, 0);
		this.label.setText(this.model.getText());
	}

}
