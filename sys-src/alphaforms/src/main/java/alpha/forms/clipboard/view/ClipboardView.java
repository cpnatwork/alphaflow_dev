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
package alpha.forms.clipboard.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import alpha.forms.application.controller.ApplicationController;
import alpha.forms.application.view.panel.FormView;
import alpha.forms.form.AlphaForm;
import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.SwitchViewSignal;

/**
 * The Class ClipboardView.
 */
public class ClipboardView extends FormView {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private AlphaForm model;

	/**
	 * Instantiates a new clipboard view.
	 * 
	 * @param model
	 *            the model
	 * @param controller
	 *            the controller
	 */
	public ClipboardView(final AlphaForm model,
			final ApplicationController controller) {
		super(controller);
		this.model = model;

		model.createState("clipboardStart");

		this.setLayout(new BorderLayout());

		final JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		final JButton button1 = new JButton("Save");
		final FormView view = this;
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				final List<ValidationFailure> valFails = model.validateForm();
				if (valFails.size() > 0) {
					switch (model.getValidationLevel()) {
					case ERROR:
						StringBuilder sb = new StringBuilder();
						sb.append("Validation failed for the following reasons:\n");
						for (final ValidationFailure vf : valFails) {
							sb.append(vf.toString()).append("\n");
						}
						JOptionPane.showMessageDialog(view, sb.toString(),
								"Validation Failed", JOptionPane.ERROR_MESSAGE);
						return;
					case WARN:
						sb = new StringBuilder();
						sb.append("Validation failed for the following reasons:\n");
						for (final ValidationFailure vf : valFails) {
							sb.append(vf.toString()).append("\n");
						}
						sb.append("Do you still want to save the form?");
						final int userOpt = JOptionPane.showConfirmDialog(view,
								sb.toString(), "Validation Failed",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
						if (userOpt == JOptionPane.NO_OPTION)
							return;
					case IGNORE:
					}
				}
				controller.persistForm("clipboardStart");
			}
		});

		final JButton button2 = new JButton("Go to designer mode");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				final SwitchViewSignal s = new SwitchViewSignal();
				s.setFromView(SwitchViewSignal.VIEWER);
				s.setToView(SwitchViewSignal.DESIGNER);
				SignalManager.getInstance().sendSignal(s, "clipboard");
			}
		});
		toolbar.add(button1);
		toolbar.add(button2);
		toolbar.addSeparator();

		final FormViewPanel formView = new FormViewPanel(model);
		this.add(toolbar, BorderLayout.NORTH);
		this.add(formView, BorderLayout.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getMaximumSize()
	 */
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(this.model.getWidth(), this.model.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(600, 400);
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

}
