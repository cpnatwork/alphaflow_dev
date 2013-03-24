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
 * $Id: TemplateWizard.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.templates.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * Creates and represents a gui wizard, with different JPanels for buttons,
 * checkboxes, headlines etc.
 */
public class TemplateWizard {

	/** The wizard. */
	private JDialog wizard;

	/** The main panel. */
	private JPanel mainPanel;

	/** The picture panel. */
	private final JPanel picturePanel;

	/** The choose panel. */
	private JPanel choosePanel;

	/** The button panel. */
	private JPanel buttonPanel;

	/** The title panel. */
	private JPanel titlePanel;

	/** The title label. */
	private JLabel titleLabel;

	/** The next button. */
	private JButton nextButton;

	/** The cancel button. */
	private JButton cancelButton;

	/**
	 * Instantiates a new template wizard.
	 * 
	 * @param title
	 *            the title
	 */
	public TemplateWizard(final String title) {
		this.wizard = new JDialog(new JFrame(), title, true);

		this.mainPanel = new JPanel();
		this.choosePanel = new JPanel();
		this.titlePanel = new JPanel();

		this.picturePanel = new JPanel();

		this.nextButton = new JButton("Next");
		this.cancelButton = new JButton("Cancel");
		this.init();
	}

	/**
	 * Gets the wizard.
	 * 
	 * @return the wizard
	 */
	public JDialog getWizard() {
		return this.wizard;
	}

	/**
	 * Sets the wizard.
	 * 
	 * @param wizard
	 *            the new wizard
	 */
	public void setWizard(final JDialog wizard) {
		this.wizard = wizard;
	}

	/**
	 * Gets the next button.
	 * 
	 * @return the next button
	 */
	public JButton getNextButton() {
		return this.nextButton;
	}

	/**
	 * Gets the main panel.
	 * 
	 * @return the main panel
	 */
	public JPanel getMainPanel() {
		return this.mainPanel;
	}

	/**
	 * Sets the main panel.
	 * 
	 * @param mainPanel
	 *            the new main panel
	 */
	public void setMainPanel(final JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	/**
	 * Sets the next button.
	 * 
	 * @param nextButton
	 *            the new next button
	 */
	public void setNextButton(final JButton nextButton) {
		this.nextButton = nextButton;
	}

	/**
	 * Gets the cancel button.
	 * 
	 * @return the cancel button
	 */
	public JButton getCancelButton() {
		return this.cancelButton;
	}

	/**
	 * Sets the cancel button.
	 * 
	 * @param cancelButton
	 *            the new cancel button
	 */
	public void setCancelButton(final JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	/**
	 * Inits the.
	 */
	private void init() {

		this.wizard.setLayout(null);
		// wizard.setLocationRelativeTo(null);

		final Insets insets = this.wizard.getInsets();

		this.buttonPanel = new JPanel();
		final Box buttonBox = new Box(BoxLayout.X_AXIS);
		this.buttonPanel.setBounds(insets.left, 480 + insets.top, 800, 40);
		this.buttonPanel.setLayout(new BorderLayout());
		this.buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

		buttonBox.add(this.nextButton);
		buttonBox.add(Box.createHorizontalStrut(100));
		buttonBox.add(this.cancelButton);
		buttonBox.add(Box.createHorizontalStrut(20));
		this.buttonPanel.add(buttonBox, BorderLayout.EAST);

		this.picturePanel.setBounds(insets.left, insets.top, 220, 480);
		final ImageIcon picture = new ImageIcon(this.getClass()
				.getClassLoader().getResource("icons/wizard.png"));
		final JLabel pic_label = new JLabel(picture);
		this.picturePanel.add(pic_label);

		this.mainPanel.setBounds(insets.left + 220, insets.top, 580, 480);
		this.mainPanel.setLayout(new BorderLayout());

		this.titleLabel = new JLabel();
		this.titleLabel.setFont(new Font(this.titleLabel.getFont().getName(),
				this.titleLabel.getFont().getStyle(), this.titleLabel.getFont()
						.getSize() + 4));
		this.titlePanel.add(this.titleLabel);
		this.mainPanel.add(this.titlePanel, BorderLayout.NORTH);

		this.mainPanel.add(this.choosePanel, BorderLayout.CENTER);

		this.wizard.getContentPane().add(this.buttonPanel);
		this.wizard.getContentPane().add(this.mainPanel);
		this.wizard.getContentPane().add(this.picturePanel);

		this.wizard.setResizable(false);

		this.wizard.setSize(800 + insets.left + insets.right, 545 + insets.top
				+ insets.bottom);

	}

	/**
	 * Gets the title label.
	 * 
	 * @return the title label
	 */
	public JLabel getTitleLabel() {
		return this.titleLabel;
	}

	/**
	 * Sets the title label.
	 * 
	 * @param titleLabel
	 *            the new title label
	 */
	public void setTitleLabel(final JLabel titleLabel) {
		this.titleLabel = titleLabel;
	}

	/**
	 * Gets the choose panel.
	 * 
	 * @return the choose panel
	 */
	public JPanel getChoosePanel() {
		return this.choosePanel;
	}

	/**
	 * Sets the choose panel.
	 * 
	 * @param choosePanel
	 *            the new choose panel
	 */
	public void setChoosePanel(final JPanel choosePanel) {
		this.choosePanel = choosePanel;
	}

	/**
	 * Gets the title panel.
	 * 
	 * @return the title panel
	 */
	public JPanel getTitlePanel() {
		return this.titlePanel;
	}

	/**
	 * Sets the title panel.
	 * 
	 * @param titlePanel
	 *            the new title panel
	 */
	public void setTitlePanel(final JPanel titlePanel) {
		this.titlePanel = titlePanel;
	}
}
