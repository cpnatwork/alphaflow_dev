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
 * $Id: InvitationDialogController.java 3994 2012-09-13 07:34:30Z uj32uvac $
 *************************************************************************/
package alpha.editor.invitation;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;

import alpha.facades.AlphaPropsFacade;
import alpha.model.cra.EndpointID;
import alpha.model.docconfig.AlphadocConfig;
import alpha.util.XmlBinder;
import alpha.util.ZipFileCreator;

/**
 * Encapsulates the complete logic for invitation creation and propagation.
 */
public class InvitationDialogController {

	/** Reference to global AlphaPropsFacade. */
	private final AlphaPropsFacade apf;

	/** Reference to the corresponding invitation gui. */
	private final InvitationDialogView invitationDialog;

	/**
	 * Reference to an XMLBinder instance for reading and writing persisted
	 * configuration files.
	 */
	private final XmlBinder binder;

	/**
	 * Copy of the global configuration file before the creation of the current
	 * invitation.
	 */
	private AlphadocConfig backupConfig;

	/**
	 * Instantiates a new InvitationDialogController.
	 * 
	 * @param apf
	 *            the global AlphaPropsFacade
	 * @param parent
	 *            the parent parent window of the invitation gui
	 * @param modal
	 *            selects if invitation gui forcibly catches focus
	 */
	public InvitationDialogController(final AlphaPropsFacade apf,
			final Frame parent, final boolean modal) {
		this.apf = apf;
		this.binder = new XmlBinder();
		this.invitationDialog = new InvitationDialogView(parent, modal);

		this.invitationDialog.setLocationRelativeTo(parent);
		this.invitationDialog.setInvitationDialogController(this);
		this.invitationDialog.getFileOutputFilenameField().setText(
				"invitation_" + apf.getAlphaConfig().getMyEpisodeId() + ".zip");
	}

	/**
	 * Shows/Hides the invitation gui.
	 * 
	 * @param show
	 *            show/hide the invitation gui
	 */
	public void showDialog(final boolean show) {
		this.invitationDialog.setVisible(show);
	}

	/**
	 * Recursively enables or disables all child components of a swing
	 * component.
	 * 
	 * @param comp
	 *            the component
	 * @param enable
	 *            enable/disable
	 */
	private void recursiveSetEnabled(final Component comp, final boolean enable) {

		if (comp instanceof Container) {
			final Component[] comps = ((Container) comp).getComponents();
			for (final Component com : comps) {
				this.recursiveSetEnabled(com, enable);
			}
		}
		comp.setEnabled(enable);
	}

	/**
	 * Selects file output for the current invitation and disables unnecessary
	 * gui components.
	 */
	public void toggleSelectFileOutput() {
		if (this.invitationDialog.getFileOutputRadio().isSelected()) {

			this.invitationDialog.getMailOutputRadio().setSelected(false);
			this.recursiveSetEnabled(
					this.invitationDialog.getMailOutputSelectionPanel(), false);
			this.recursiveSetEnabled(
					this.invitationDialog.getFileOutputSelectionPanel(), true);
			this.invitationDialog.getInviteButton().setEnabled(true);
		} else {
			this.recursiveSetEnabled(
					this.invitationDialog.getFileOutputSelectionPanel(), false);
			if (!this.invitationDialog.getMailOutputRadio().isSelected()) {
				this.invitationDialog.getInviteButton().setEnabled(false);
			}
		}
	}

	/**
	 * Selects mail output for the current invitation and disables unnecessary
	 * gui components.
	 */
	public void toggleSelectMailOutput() {
		if (this.invitationDialog.getMailOutputRadio().isSelected()) {

			this.recursiveSetEnabled(
					this.invitationDialog.getFileOutputSelectionPanel(), false);
			this.invitationDialog.getFileOutputRadio().setSelected(false);
			this.recursiveSetEnabled(
					this.invitationDialog.getMailOutputSelectionPanel(), true);
			this.invitationDialog.getInviteButton().setEnabled(true);
		} else {
			this.recursiveSetEnabled(
					this.invitationDialog.getMailOutputSelectionPanel(), false);
			if (!this.invitationDialog.getFileOutputRadio().isSelected()) {
				this.invitationDialog.getInviteButton().setEnabled(false);
			}
		}
	}

	/**
	 * Shows a file chooser dialog to retrieve the output path for file output.
	 */
	public void chooseFileOutputFile() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		final int ret = fc.showOpenDialog(this.invitationDialog);
		if (ret == JFileChooser.APPROVE_OPTION) {
			this.invitationDialog.getFileOutputField().setText(
					fc.getSelectedFile().getAbsolutePath());
		}
	}

	/**
	 * Closes the invitation dialog and dispose all related gui components.
	 */
	public void close() {
		this.invitationDialog.setVisible(false);
		this.invitationDialog.dispose();
	}

	/**
	 * Processes the current invitation based on user input.
	 */
	public void invite() {

		this.invitationDialog.setCursor(Cursor
				.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Set up
		this.backupConfig = this.retrieveLocalAlphaConfig();
		this.prepareAlphaConfig(this.backupConfig);

		if (this.invitationDialog.getFileOutputRadio().isSelected()) {

			ZipFileCreator.createZipFile(".", new File(this.invitationDialog
					.getFileOutputField().getText()
					+ "/"
					+ this.invitationDialog.getFileOutputFilenameField()
							.getText()), new File(this.apf.getHomePath())
					.getName());
		}

		if (this.invitationDialog.getMailOutputRadio().isSelected()) {

			final String subject = this.invitationDialog.getSubjectField()
					.getText();
			final String messageContent = this.invitationDialog
					.getMessageText().getText();

			final List<Object> attachments = new ArrayList<Object>();

			File tempFile = null;
			try {
				tempFile = File.createTempFile("invitation_", ".zip");
				tempFile.deleteOnExit();
				ZipFileCreator.createZipFile(".", tempFile,
						new File(this.apf.getHomePath()).getName());
			} catch (final IOException e) {
				e.printStackTrace();
			}

			attachments.add(tempFile);

			final Set<EndpointID> recipients = new LinkedHashSet<EndpointID>();
			final EndpointID recipient = new EndpointID("", "", "",
					this.invitationDialog.getRecipientField().getText());
			recipients.add(recipient);

			this.apf.sendMessage(attachments, recipients, subject,
					messageContent, false, false);
		}

		// Tear down
		this.writeAlphaConfig(this.backupConfig);
		this.invitationDialog.setCursor(Cursor.getDefaultCursor());
		this.close();
	}

	/**
	 * Retrieves the local configuration file object from disk.
	 * 
	 * @return the local configuration file object
	 */
	private AlphadocConfig retrieveLocalAlphaConfig() {

		final AlphadocConfig config = (AlphadocConfig) this.binder.load(
				this.apf.getHomePath() + "/" + "alphaconfig.xml",
				"alpha.model.docconfig");

		return config;
	}

	/**
	 * Prepares the local configuration file object to serve as an invitation
	 * configuration.
	 * 
	 * @param currentConfig
	 *            the configuration file object to be prepared
	 */
	private void prepareAlphaConfig(final AlphadocConfig currentConfig) {

		final AlphadocConfig config = new AlphadocConfig();

		config.setMyEpisodeId(currentConfig.getMyEpisodeId());
		config.setMyCurrentlyActiveCardId(currentConfig
				.getMyCurrentlyActiveCardId());
		config.setInvitation(true);
		config.setSecureCommunication(currentConfig.isSecureCommunication());
		config.setAcknowledgeDelivery(currentConfig.isAcknowledgeDelivery());

		this.writeAlphaConfig(config);
	}

	/**
	 * Writes a configuration file object to disk.
	 * 
	 * @param config
	 *            the configuration file object to be written to disk
	 */
	private void writeAlphaConfig(final AlphadocConfig config) {

		try {
			this.binder.store(config, this.apf.getHomePath() + "/"
					+ "alphaconfig.xml", "alpha.model.docconfig");
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}
}
