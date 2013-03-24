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
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import alpha.forms.designer.view.SelectionState;
import alpha.forms.widget.model.TextField;
import alpha.forms.widget.util.WidgetLabelPosition;

/**
 * The Class TextFieldUI.
 */
public class TextFieldUI extends FormWidgetUI {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	protected TextField model;

	/** The label. */
	private JLabel label;

	/** The text. */
	private JTextField text;

	/** The hgap. */
	private int hgap = 5;

	/**
	 * Instantiates a new text field ui.
	 * 
	 * @param model
	 *            the model
	 */
	public TextFieldUI(final TextField model) {
		super(model);
		this.model = model;
		this.compose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#doLayout()
	 */
	@Override
	public void doLayout() {
		super.doLayout();
		final Dimension d = this.model.getSize();
		super.setSize(d);
		this.text.setText(this.model.getText());
		if ((this.label != null)
				&& (this.model.getShowLabel() == WidgetLabelPosition.LEFT)) {
			this.label.setText(this.model.getLabel());
			final Dimension dl = this.label.getPreferredSize();
			this.label.setSize(dl);
			this.label.setLocation(0, (d.height / 2) - (dl.height / 2));
			this.text.setLocation(dl.width + this.hgap, 0);
			this.text.setSize(d.width - this.hgap - dl.width, d.height);
		} else if ((this.label != null)
				&& (this.model.getShowLabel() == WidgetLabelPosition.RIGHT)) {
			this.label.setText(this.model.getLabel());
			final Dimension dl = this.label.getPreferredSize();
			this.label.setSize(dl);
			this.label.setLocation(d.width - dl.width, (d.height / 2)
					- (dl.height / 2));
			this.text.setLocation(0, 0);
			this.text.setSize(d.width - this.hgap - dl.width, d.height);
		} else {
			this.text.setLocation(0, 0);
			this.text.setSize(d.width, d.height);
			this.label.setSize(0, 0);
		}
		this.setVisible(this.model.isVisible());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#getSubselection()
	 */
	@Override
	public Rectangle getSubselection() {
		if ((this.model.getShowLabel() == WidgetLabelPosition.LEFT)
				|| (this.model.getShowLabel() == WidgetLabelPosition.RIGHT)) {
			final Rectangle r = this.text.getBounds();
			r.translate(this.getLocation().x, this.getLocation().y);
			return r;
		} else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#isSubselectionResizable(int)
	 */
	@Override
	public boolean isSubselectionResizable(final int direction) {
		return ((direction == SelectionState.WEST) && (this.model
				.getShowLabel() == WidgetLabelPosition.LEFT))
				|| ((direction == SelectionState.EAST) && (this.model
						.getShowLabel() == WidgetLabelPosition.RIGHT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#updateSubselectionSize(int,
	 * int)
	 */
	@Override
	public void updateSubselectionSize(final int delta, final int direction) {
		if ((direction == SelectionState.WEST)
				&& (this.model.getShowLabel() == WidgetLabelPosition.LEFT)) {
			final int tmpgap = this.hgap - delta;
			if ((tmpgap >= 5)
					&& ((tmpgap + this.label.getWidth()) < this.model.getSize().width)) {
				this.hgap = tmpgap;
			}
		} else if ((direction == SelectionState.EAST)
				&& (this.model.getShowLabel() == WidgetLabelPosition.RIGHT)) {
			final int tmpgap = this.hgap - (delta - this.text.getWidth());
			if ((tmpgap >= 5)
					&& ((tmpgap + this.label.getWidth()) < this.model.getSize().width)) {
				this.hgap = tmpgap;
			}
		}
		this.doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#compose()
	 */
	@Override
	protected void compose() {
		super.compose();
		this.setLayout(null);
		final TextField tf = this.model;
		this.text = new JTextField();
		this.text.setOpaque(this.isOpaque());
		this.text.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent ev) {
				tf.setText(TextFieldUI.this.text.getText());
			}

			@Override
			public void insertUpdate(final DocumentEvent arg0) {
				tf.setText(TextFieldUI.this.text.getText());
			}

			@Override
			public void removeUpdate(final DocumentEvent arg0) {
				tf.setText(TextFieldUI.this.text.getText());
			}

		});
		if (tf.getShowLabel() == WidgetLabelPosition.LEFT) {
			this.label = new JLabel(tf.getLabel());
			final Dimension d = this.label.getPreferredSize();
			final Dimension dt = this.text.getPreferredSize();

			this.label.setLocation(0, (dt.height / 2) - (d.height / 2));
			this.label.setSize(d.width, d.height);
			this.text.setSize(
					this.model.getMiniumWidth() - d.width - this.hgap,
					dt.height);
			this.text.setLocation(d.width + this.hgap, 0);
			this.add(this.label);
			this.add(this.text);
			this.minimumHeight = dt.height;
			this.minimumWidth = d.width + this.hgap + dt.width;
		} else if (tf.getShowLabel() == WidgetLabelPosition.RIGHT) {
			this.label = new JLabel(tf.getLabel());
			final Dimension d = this.label.getMinimumSize();
			final Dimension dt = this.text.getMinimumSize();
			this.text.setBounds(0, 0,
					tf.getMiniumWidth() - d.width - this.hgap, dt.height);
			this.label.setBounds(tf.getMiniumWidth() + this.hgap,
					(dt.height / 2) - (d.height / 2), d.width, d.height);
			this.add(this.text);
			this.add(this.label);
			this.minimumHeight = dt.height;
			this.minimumWidth = tf.getMiniumWidth();
		} else {
			final Dimension dt = this.text.getMinimumSize();
			this.text.setBounds(0, 0, tf.getMiniumWidth(), dt.height);
			this.add(this.text);
			this.minimumHeight = dt.height;
			this.minimumWidth = tf.getMiniumWidth();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	@Override
	public void setSize(final Dimension d) {
		super.setSize(d);
		this.doLayout();
	}

}
