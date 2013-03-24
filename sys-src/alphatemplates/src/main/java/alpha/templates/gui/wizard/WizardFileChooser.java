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
 * $Id: WizardFileChooser.java 3606 2012-02-22 00:24:52Z sipareis $
 *************************************************************************/
package alpha.templates.gui.wizard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;

import java.util.logging.Logger;

import alpha.templates.gui.AlphaFileChooser;

/**
 * Implements the AlphaFileChooser and embeds a JFileChooser into the wizard, in
 * which a user may choose a file from the file system.
 */
public class WizardFileChooser implements AlphaFileChooser {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(alpha.templates.gui.wizard.WizardFileChooser.class
					.getName());

	/** The tw. */
	private final TemplateWizard wizard;

	/** The file. */
	private File chosenFile;

	/** The button text. */
	private final String buttonText;

	/**
	 * Instantiates a new wizard file chooser.
	 * 
	 * @param wizard
	 *            the tw
	 * @param buttonText
	 *            the button text
	 */
	public WizardFileChooser(final TemplateWizard wizard,
			final String buttonText) {
		this.wizard = wizard;
		this.buttonText = buttonText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.templates.gui.AlphaFileChooser#choose(java.io.File)
	 */
	@Override
	public File choose(final File pathFile, final String output) {

		final JFileChooser fc = new JFileChooser(pathFile);
		fc.setControlButtonsAreShown(false);
		fc.setDialogTitle(output);
		final FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"XML files (*.xml)", "xml");
		fc.setFileFilter(filter);

		this.wizard.getChoosePanel().removeAll();
		this.wizard.getChoosePanel().setLayout(new BorderLayout());
		this.wizard.getTitleLabel().setText("<HTML>" + output + "</HTML>");
		this.wizard.getChoosePanel().add(fc, BorderLayout.CENTER);
		this.wizard.getNextButton().setText(this.buttonText);

		for (final ActionListener al : this.wizard.getNextButton()
				.getActionListeners()) {
			this.wizard.getNextButton().removeActionListener(al);
		}
		this.wizard.getNextButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				// workaround to get the filename, if it is typed manually:
				// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4528663
				if (fc.getUI() instanceof BasicFileChooserUI) {
					BasicFileChooserUI ui = (BasicFileChooserUI) fc.getUI();
					ui.getApproveSelectionAction().actionPerformed(null);
				}

				WizardFileChooser.this.chosenFile = fc.getSelectedFile();

				if (WizardFileChooser.this.chosenFile == null)
					return;
				WizardFileChooser.LOGGER.info("Opening: "
						+ WizardFileChooser.this.chosenFile.getName() + ".");
				WizardFileChooser.this.wizard.getWizard().setVisible(false);
				WizardFileChooser.this.wizard.getNextButton().setText("Next");
			}
		});
		this.wizard.getCancelButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				WizardFileChooser.LOGGER
						.warning("Open command cancelled by user.");
				WizardFileChooser.this.wizard.getWizard().setVisible(false);
				WizardFileChooser.this.wizard.getNextButton().setText("Next");
			}
		});

		this.wizard.getWizard().setVisible(true);

		return this.chosenFile;
	}

}
