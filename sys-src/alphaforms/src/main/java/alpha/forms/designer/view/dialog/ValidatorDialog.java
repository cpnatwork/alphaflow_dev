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
package alpha.forms.designer.view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import alpha.forms.form.validation.Validator;
import alpha.forms.form.validation.ValidatorFactory;
import alpha.forms.form.validation.ValidatorGroup;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class ValidatorDialog.
 */
public class ValidatorDialog extends JDialog implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3905906111405466499L;

	/** The validator. */
	private Validator validator;

	/** The group. */
	private ValidatorGroup group;

	/** The available validators. */
	private final List<Validator> availableValidators = new ArrayList<Validator>();

	/** The validator choice. */
	private JComboBox validatorChoice;

	/** The panel. */
	private final JPanel panel;

	/** The save. */
	private final JButton save;

	/** The cancel. */
	private final JButton cancel;

	/** The was cancled. */
	private boolean wasCancled;

	/** The was saved. */
	private boolean wasSaved;

	/**
	 * Creates the.
	 * 
	 * @param v
	 *            the v
	 * @param group
	 *            the group
	 * @param w
	 *            the w
	 * @return the validator dialog
	 */
	public static ValidatorDialog create(final Validator v,
			final ValidatorGroup group, final FormWidget w) {
		final ValidatorDialog dialog = new ValidatorDialog(w, group, v);
		// dialog.setGroup(group);
		// dialog.setValidator(v);
		return dialog;
	}

	/**
	 * Instantiates a new validator dialog.
	 * 
	 * @param w
	 *            the w
	 * @param group
	 *            the group
	 * @param validator
	 *            the validator
	 */
	public ValidatorDialog(final FormWidget w, final ValidatorGroup group,
			final Validator validator) {
		super((Frame) null, "Validator:", true);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setResizable(false);

		this.panel = new JPanel(new BorderLayout());
		this.panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		this.panel.add(new JSeparator(), BorderLayout.CENTER);

		final List<Validator> validatorList = ValidatorFactory
				.getAvailableValidators();

		for (final Validator v : validatorList) {
			if (!group.contains(v) && v.isCompatibleWith(w.getClass())) {
				this.availableValidators.add(v);
			}
		}

		final JPanel buttonPanel = new JPanel(new FlowLayout());

		this.save = new JButton("Save");
		this.save.addActionListener(this);
		this.save.setActionCommand("save");
		this.cancel = new JButton("Cancel");
		this.cancel.addActionListener(this);
		this.cancel.setActionCommand("cancel");

		buttonPanel.add(this.save);
		buttonPanel.add(this.cancel);

		this.add(this.panel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(this.save);

		this.setGroup(group);
		this.setValidator(validator);
	}

	/**
	 * Sets the validator.
	 * 
	 * @param validator
	 *            the new validator
	 */
	public void setValidator(final Validator validator) {
		if (validator != null) {
			this.validator = validator;
			this.validatorChoice.addItem(validator);
			this.validatorChoice.setSelectedItem(validator);
			this.validatorChoice.setEnabled(false);
			this.save.setEnabled(true);
		} else if (this.validatorChoice.getModel().getSize() > 0) {
			this.validatorChoice.setSelectedIndex(0);
		}
	}

	/**
	 * Gets the validator.
	 * 
	 * @return the validator
	 */
	public Validator getValidator() {
		return this.validator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean flag) {
		if (flag == true) {
			this.pack();
			this.wasCancled = false;
			this.wasSaved = false;
		}
		super.setVisible(flag);
	}

	/**
	 * Sets the group.
	 * 
	 * @param group
	 *            the new group
	 */
	public void setGroup(final ValidatorGroup group) {
		this.group = group;

		this.validatorChoice = new JComboBox(this.availableValidators.toArray());

		if (this.availableValidators.size() == 0) {
			this.validatorChoice.setEnabled(false);
			this.save.setEnabled(false);
		}

		this.validatorChoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Validator v = (Validator) ValidatorDialog.this.validatorChoice
						.getSelectedItem();
				if (v != null) {
					ValidatorDialog.this.validator = v;
					ValidatorDialog.this.panel.add(v.getOptionUI(),
							BorderLayout.SOUTH);
					ValidatorDialog.this.pack();
				}
			}
		});

		this.panel.add(this.validatorChoice, BorderLayout.NORTH);
	}

	/**
	 * Was cancled.
	 * 
	 * @return true, if successful
	 */
	public boolean wasCancled() {
		return this.wasCancled;
	}

	/**
	 * Was saved.
	 * 
	 * @return true, if successful
	 */
	public boolean wasSaved() {
		return this.wasSaved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("save")) {
			this.setVisible(false);
			this.wasSaved = true;
		} else if (e.getActionCommand().equals("cancel")) {
			this.setVisible(false);
			this.wasCancled = true;
		}
	}
}
