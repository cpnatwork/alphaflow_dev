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
 * $Id: WizardMessagePresenter.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.templates.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import alpha.templates.gui.MessagePresenter;

/**
 * Presents a simple text message in the wizard window.
 */
public class WizardMessagePresenter implements MessagePresenter {

	/** The tw. */
	private final TemplateWizard wizard;

	/** The welcome. */
	private boolean welcome;

	/**
	 * Checks if is welcome.
	 * 
	 * @return true, if is welcome
	 */
	public boolean isWelcome() {
		return this.welcome;
	}

	/**
	 * Sets the welcome.
	 * 
	 * @param welcome
	 *            the new welcome
	 */
	public void setWelcome(final boolean welcome) {
		this.welcome = welcome;
	}

	/**
	 * Instantiates a new wizard message presenter.
	 * 
	 * @param tw
	 *            the tw
	 */
	public WizardMessagePresenter(final TemplateWizard tw) {
		this.wizard = tw;
		this.welcome = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.templates.gui.ResultPresenter#print(java.lang.String)
	 */
	@Override
	public void print(final String output) {

		this.wizard.getChoosePanel().removeAll();
		this.wizard.getChoosePanel().setLayout(new BorderLayout());
		this.wizard.getTitleLabel().setText("");

		final JLabel titleLabel = new JLabel("<HTML>   " + output + "</HTML>");
		titleLabel.setFont(new Font(titleLabel.getFont().getName(), titleLabel
				.getFont().getStyle(), titleLabel.getFont().getSize() * 2));
		this.wizard.getChoosePanel().add(titleLabel, BorderLayout.CENTER);

		if (this.welcome == true) {
			this.wizard.getNextButton().setText("Start");
		} else {
			this.wizard.getNextButton().setText("Finish");
		}

		this.wizard.getCancelButton().setEnabled(false);

		for (final ActionListener al : this.wizard.getNextButton()
				.getActionListeners()) {
			this.wizard.getNextButton().removeActionListener(al);
		}
		this.wizard.getNextButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				WizardMessagePresenter.this.wizard.getWizard()
						.setVisible(false);
				WizardMessagePresenter.this.wizard.getNextButton().setText(
						"Next");
				WizardMessagePresenter.this.wizard.getCancelButton()
						.setEnabled(true);
			}
		});

		this.wizard.getWizard().setVisible(true);

	}
}
