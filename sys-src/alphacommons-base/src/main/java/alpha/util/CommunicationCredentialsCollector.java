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
 * $Id: CommunicationCredentialsCollector.java 3994 2012-09-13 07:34:30Z uj32uvac $
 *************************************************************************/
package alpha.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import alpha.model.docconfig.AlphadocConfig;

// TODO: Auto-generated Javadoc
/**
 * Creates and displays a {@link JFrame} for collecting the communication
 * credentials of the local actor.
 */
public class CommunicationCredentialsCollector {

	/** True if information has been submitted by the actor. False otherwise. */
	boolean submit = false;

	/** The mail address. */
	String mailAddress;

	/** The username. */
	String username;

	/** The password. */
	String password;

	/** The smtp host. */
	String smtpHost;

	/** The smtp port. */
	String smtpPort;

	/** The imap mode. */
	String imapMode;

	/** The imap host. */
	String imapHost;

	/** The imap port. */
	String imapPort;

	/** The own kp path. */
	String ownKPPath;

	/** The own kp pass. */
	String ownKPPass;

	/** The pk path. */
	String pkPath;

	/** The intro label. */
	String introLabel = "Please supply your communication credentials.";

	/** The mail address label. */
	String mailAddressLabel = "Email-Address";

	/** The username label. */
	String usernameLabel = "Username";

	/** The password label. */
	String passwordLabel = "Password";

	/** The smtp host label. */
	String smtpHostLabel = "SMTP Host Address";

	/** The smtp port label. */
	String smtpPortLabel = "SMTP Port";

	/** The imap mode label. */
	String imapModeLabel = "IMAP Mode";

	/** The imap host label. */
	String imapHostLabel = "IMAP Host Address";

	/** The imap port label. */
	String imapPortLabel = "IMAP Port";

	/** The imap folder label. */
	String imapFolderLabel = "IMAP Folder";

	/** The own kp label. */
	String ownKPLabel = "Path to OpenPGP secret key";

	/** The own kp pass label. */
	String ownKPPassLabel = "Password for OpenPGP secret key";

	/** The pk label. */
	String pkLabel = "Path to OpenPGP public key ring";

	/** The ssl label. */
	String sslLabel = "Use SSL for SMTP";

	/** Indicates if ssl is used for smtp communication. */
	boolean ssl;

	/** Indicates if security option is selected. */
	boolean secureCommunication;

	/**
	 * Indicates if user is allowed to modify global settings i.e.
	 * enable/disable encryption
	 */
	boolean allowModifySettings;

	/** The use specific mailbox. */
	boolean useSpecificMailbox;

	/** Indicates whether the reception of a new version of an alpha card should be acknowledged. */
	boolean acknowledgeDelivery;

	/** The ack label. */
	String ackLabel = "Use Delivery ACKs";

	/** The ack check box. */
	JCheckBox ackCheckBox;

	/** Shows if CCC is collecting initial information during initial startup (true) or via editor to change address information (false). */
	boolean initialization;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String args[]) {
		final CommunicationCredentialsCollector ccc = new CommunicationCredentialsCollector(
				false, false, false);
		ccc.showDialog();
		System.out.println(ccc.isUseSpecificMailbox());
	}

	/**
	 * Instantiates a new {@link CommunicationCredentialsCollector} using
	 * default field values.
	 * 
	 * @param secureCommunication
	 *            the secure communication
	 * @param allowModifySettings
	 *            the allow modify settings
	 * @param acknowledgeDelivery TODO
	 */
	public CommunicationCredentialsCollector(final boolean secureCommunication,
			final boolean allowModifySettings, boolean acknowledgeDelivery) {
		// this.mailAddress = "promed.alphaflow@googlemail.com";
		// this.username = "promed.alphaflow";
		// this.password = "pr,R0m3d";
		this.mailAddress = "chalphadoc1@googlemail.com";
		this.username = "chalphadoc1";
		this.password = "alfaFlow1";
		this.smtpHost = "smtp.gmail.com";
		this.smtpPort = "465";
		this.imapHost = "imap.googlemail.com";
		this.imapPort = "993";
		this.imapMode = "imaps";
		this.ownKPPath = "./crypto_metadata/private_key_1.gpg";
		this.ownKPPass = "changeit";
		this.pkPath = "./crypto_metadata/public_key_ring_1.asc";
		this.ssl = true;
		this.secureCommunication = secureCommunication;
		this.allowModifySettings = allowModifySettings;
		this.useSpecificMailbox = false;
		this.acknowledgeDelivery = acknowledgeDelivery;
		this.initialization = true;
	}

	/**
	 * Instantiates a new {@link CommunicationCredentialsCollector} using field
	 * values from the supplied {@link AlphadocConfig}.
	 * 
	 * @param config
	 *            the config
	 * @param allowModifySettings
	 *            the allow modify settings
	 */
	public CommunicationCredentialsCollector(final AlphadocConfig config,
			final boolean allowModifySettings) {
		this.mailAddress = config.getLocalNodeID().getNode().getEmailAddress();
		this.username = config.getUsername();
		this.password = config.getPassword();
		this.smtpHost = config.getSmtpHost();
		this.smtpPort = config.getSmtpPort();
		this.imapHost = config.getImapHost();
		this.imapPort = config.getImapPort();
		this.imapMode = config.getImapMode();
		this.ownKPPath = config.getOwnKPPath();
		this.ownKPPass = config.getOwnKPPass();
		this.pkPath = config.getPkPath();
		this.ssl = config.isSsl();
		this.secureCommunication = config.isSecureCommunication();
		this.allowModifySettings = allowModifySettings;
		this.useSpecificMailbox = config.isUseSpecificMailbox();
		this.acknowledgeDelivery = config.isAcknowledgeDelivery();
		this.initialization = false;
	}

	/**
	 * Show dialog.
	 */
	public void showDialog() {
		final JFrame frame = new JFrame();

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(1, 1, 1, 1);

		final JPanel basic = new JPanel(new GridBagLayout());
		basic.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 20, 0);
		JLabel description = new JLabel(this.introLabel);
		basic.add(description, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(2, 5, 2, 2);

		description = new JLabel(this.mailAddressLabel);
		description.setPreferredSize(new Dimension(120, 20));
		basic.add(description, gbc);

		final JTextField mail = new JTextField(this.mailAddress);
		gbc.gridx++;
		basic.add(mail, gbc);

		description = new JLabel(this.usernameLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField user = new JTextField(this.username);
		gbc.gridx++;
		basic.add(user, gbc);

		description = new JLabel(this.passwordLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField pw = new JPasswordField(this.password);
		gbc.gridx++;
		basic.add(pw, gbc);

		description = new JLabel(this.smtpHostLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField smtphost = new JTextField(this.smtpHost);
		gbc.gridx++;
		basic.add(smtphost, gbc);

		description = new JLabel(this.smtpPortLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField smtpport = new JTextField(this.smtpPort);
		gbc.gridx++;
		basic.add(smtpport, gbc);

		description = new JLabel(this.sslLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JCheckBox sslCheck = new JCheckBox();
		sslCheck.setSelected(this.ssl);
		sslCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				if (!sslCheck.isSelected()) {
					CommunicationCredentialsCollector.this.ssl = false;
				} else {
					CommunicationCredentialsCollector.this.ssl = true;
				}
			}
		});
		gbc.gridx++;
		basic.add(sslCheck, gbc);

		description = new JLabel(this.imapHostLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField imaphost = new JTextField(this.imapHost);
		gbc.gridx++;
		basic.add(imaphost, gbc);

		description = new JLabel(this.imapPortLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField imapport = new JTextField(this.imapPort);
		gbc.gridx++;
		basic.add(imapport, gbc);

		description = new JLabel(this.imapModeLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField imapmode = new JTextField(this.imapMode);
		gbc.gridx++;
		basic.add(imapmode, gbc);

		description = new JLabel(this.imapFolderLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final String[] folders = { "Default Inbox",
				"Episode-specific subfolder" };
		final JComboBox imapfolder = new JComboBox(folders);
		if (this.isUseSpecificMailbox()) {
			imapfolder.setSelectedIndex(1);
		}
		gbc.gridx++;
		basic.add(imapfolder, gbc);

		final JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		basic.add(sep, gbc);

		final JPanel secPanel = new JPanel(new GridBagLayout());
		final JCheckBox secCheck = new JCheckBox(
				"Use OpenPGP encryption and electronic signatures for message exchange");
		secCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (secPanel.isEnabled()) {
					secPanel.setEnabled(false);
					for (final Component comp : secPanel.getComponents()) {
						comp.setEnabled(false);
					}
					CommunicationCredentialsCollector.this
							.setSecureCommunication(false);
				} else {
					secPanel.setEnabled(true);
					for (final Component comp : secPanel.getComponents()) {
						comp.setEnabled(true);
					}
					CommunicationCredentialsCollector.this
							.setSecureCommunication(true);
				}
				// System.out.println(isSecureCommunication());
			}
		});

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		basic.add(secCheck, gbc);
		gbc.gridy++;

		final GridBagConstraints secGbc = new GridBagConstraints();
		secGbc.fill = GridBagConstraints.HORIZONTAL;
		secGbc.insets = new Insets(2, 0, 2, 2);
		description = new JLabel(this.ownKPLabel);
		secGbc.gridx = 0;
		secGbc.gridy = 0;
		secPanel.add(description, secGbc);

		final JTextField ownKPPath = new JTextField(this.ownKPPath);
		secGbc.gridx++;
		secPanel.add(ownKPPath, secGbc);

		final JButton selectOwnKP = new JButton("Select");
		selectOwnKP.setSize(selectOwnKP.getWidth(), ownKPPath.getHeight());
		secGbc.gridx++;
		secPanel.add(selectOwnKP, secGbc);
		selectOwnKP.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.showOpenDialog(frame);
				if (fc.getSelectedFile() != null) {
					ownKPPath.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});

		description = new JLabel(this.ownKPPassLabel);
		secGbc.gridx = 0;
		secGbc.gridy++;
		secPanel.add(description, secGbc);

		final JTextField ownKPPass = new JPasswordField(this.ownKPPass);
		secGbc.gridx++;
		secPanel.add(ownKPPass, secGbc);

		description = new JLabel(this.pkLabel);
		secGbc.gridx = 0;
		secGbc.gridy++;
		secPanel.add(description, secGbc);

		final JTextField publicKeys = new JTextField(this.pkPath);
		secGbc.gridx++;
		secPanel.add(publicKeys, secGbc);

		final JButton selectPublicKeys = new JButton("Select");
		selectPublicKeys.setSize(selectPublicKeys.getWidth(),
				publicKeys.getHeight());
		secGbc.gridx++;
		secPanel.add(selectPublicKeys, secGbc);
		selectPublicKeys.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.showOpenDialog(frame);
				if (fc.getSelectedFile() != null) {
					publicKeys.setText(fc.getSelectedFile().getAbsolutePath());
				}

			}
		});

		secCheck.setSelected(this.isSecureCommunication());
		secCheck.setEnabled(this.isAllowModifySettings());
		secPanel.setEnabled(this.isSecureCommunication());
		for (final Component comp : secPanel.getComponents()) {
			comp.setEnabled(this.isSecureCommunication());
		}

		gbc.gridwidth = 2;
		basic.add(secPanel, gbc);

		final JSeparator sep2 = new JSeparator(SwingConstants.HORIZONTAL);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		basic.add(sep2, gbc);

		description = new JLabel(this.ackLabel);
		description.setEnabled(initialization);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JCheckBox ackCheckBox = new JCheckBox();
		// activate at initial startup
		ackCheckBox.setEnabled(initialization);
		ackCheckBox.setSelected(this.acknowledgeDelivery);
		ackCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				CommunicationCredentialsCollector.this.acknowledgeDelivery = ackCheckBox
						.isSelected();
			}
		});
		gbc.gridx++;
		basic.add(ackCheckBox, gbc);

		final JSeparator sep3 = new JSeparator(SwingConstants.HORIZONTAL);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		basic.add(sep3, gbc);

		final Object[] options = { "Submit", "Cancel" };
		final int ret = JOptionPane.showOptionDialog(frame, basic,
				"Communication Credentials", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		// if(ret == JOptionPane.CLOSED_OPTION){
		// return;
		// }
		if (ret == JOptionPane.OK_OPTION) {
			this.submit = true;
			this.mailAddress = mail.getText();
			this.username = user.getText();
			this.password = pw.getText();
			this.smtpHost = smtphost.getText();
			this.smtpPort = smtpport.getText();
			this.imapMode = imapmode.getText();
			this.imapHost = imaphost.getText();
			this.imapPort = imapport.getText();
			this.ownKPPath = ownKPPath.getText();
			this.ownKPPass = ownKPPass.getText();
			this.pkPath = publicKeys.getText();
			if (imapfolder.getSelectedIndex() == 0) {
				this.useSpecificMailbox = false;
			} else {
				this.useSpecificMailbox = true;
			}
		}
	}

	/**
	 * Checks if information has been submitted by the actor.
	 * 
	 * @return true, if information has been submitted by the actor.
	 */
	public boolean isSubmit() {
		return this.submit;
	}

	/**
	 * Gets the mail address.
	 * 
	 * @return the mail address
	 */
	public String getMailAddress() {
		return this.mailAddress;
	}

	/**
	 * Sets the mail address.
	 * 
	 * @param mailAddress
	 *            the new mail address
	 */
	public void setMailAddress(final String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Gets the smtp host.
	 * 
	 * @return the smtp host
	 */
	public String getSmtpHost() {
		return this.smtpHost;
	}

	/**
	 * Sets the smtp host.
	 * 
	 * @param smtpHost
	 *            the new smtp host
	 */
	public void setSmtpHost(final String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * Gets the smtp port.
	 * 
	 * @return the smtp port
	 */
	public String getSmtpPort() {
		return this.smtpPort;
	}

	/**
	 * Sets the smtp port.
	 * 
	 * @param smtpPort
	 *            the new smtp port
	 */
	public void setSmtpPort(final String smtpPort) {
		this.smtpPort = smtpPort;
	}

	/**
	 * Gets the imap mode.
	 * 
	 * @return the imap mode
	 */
	public String getImapMode() {
		return this.imapMode;
	}

	/**
	 * Sets the imap mode.
	 * 
	 * @param imapMode
	 *            the new imap mode
	 */
	public void setImapMode(final String imapMode) {
		this.imapMode = imapMode;
	}

	/**
	 * Gets the imap host.
	 * 
	 * @return the imap host
	 */
	public String getImapHost() {
		return this.imapHost;
	}

	/**
	 * Sets the imap host.
	 * 
	 * @param imapHost
	 *            the new imap host
	 */
	public void setImapHost(final String imapHost) {
		this.imapHost = imapHost;
	}

	/**
	 * Gets the own kp path.
	 * 
	 * @return the own kp path
	 */
	public String getOwnKPPath() {
		return this.ownKPPath;
	}

	/**
	 * Sets the own kp path.
	 * 
	 * @param ownKPPath
	 *            the new own kp path
	 */
	public void setOwnKPPath(final String ownKPPath) {
		this.ownKPPath = ownKPPath;
	}

	/**
	 * Gets the own kp pass.
	 * 
	 * @return the own kp pass
	 */
	public String getOwnKPPass() {
		return this.ownKPPass;
	}

	/**
	 * Sets the own kp pass.
	 * 
	 * @param ownKPPass
	 *            the new own kp pass
	 */
	public void setOwnKPPass(final String ownKPPass) {
		this.ownKPPass = ownKPPass;
	}

	/**
	 * Gets the pk path.
	 * 
	 * @return the pk path
	 */
	public String getPkPath() {
		return this.pkPath;
	}

	/**
	 * Sets the pk path.
	 * 
	 * @param pkPath
	 *            the new pk path
	 */
	public void setPkPath(final String pkPath) {
		this.pkPath = pkPath;
	}

	/**
	 * Checks if secure communication is selected.
	 * 
	 * @return true, if secure communication is selected
	 */
	public boolean isSecureCommunication() {
		return this.secureCommunication;
	}

	/**
	 * Sets secure communication flag.
	 * 
	 * @param secureCommunication
	 *            the new secure communication flag
	 */
	public void setSecureCommunication(final boolean secureCommunication) {
		this.secureCommunication = secureCommunication;
	}

	/**
	 * Checks if user is allowed to modify settings.
	 * 
	 * @return true, if user is allowed to modify settings
	 */
	public boolean isAllowModifySettings() {
		return this.allowModifySettings;
	}

	/**
	 * Sets if user is allowed to modify settings.
	 * 
	 * @param allowModifySettings
	 *            true, if user is allowed to modify settings
	 */
	public void setAllowModifySettings(final boolean allowModifySettings) {
		this.allowModifySettings = allowModifySettings;
	}

	/**
	 * Checks if is indicates if ssl is used for smtp communication.
	 * 
	 * @return the indicates if ssl is used for smtp communication
	 */
	public boolean isSsl() {
		return this.ssl;
	}

	/**
	 * Sets the indicates if ssl is used for smtp communication.
	 * 
	 * @param ssl
	 *            the new indicates if ssl is used for smtp communication
	 */
	public void setSsl(final boolean ssl) {
		this.ssl = ssl;
	}

	/**
	 * Gets the imap port.
	 * 
	 * @return the imap port
	 */
	public String getImapPort() {
		return this.imapPort;
	}

	/**
	 * Sets the imap port.
	 * 
	 * @param imapPort
	 *            the new imap port
	 */
	public void setImapPort(final String imapPort) {
		this.imapPort = imapPort;
	}

	/**
	 * Checks if is use specific mailbox.
	 * 
	 * @return true, if is use specific mailbox
	 */
	public boolean isUseSpecificMailbox() {
		return this.useSpecificMailbox;
	}

	/**
	 * Sets the use specific mailbox.
	 * 
	 * @param useSpecificMailbox
	 *            the new use specific mailbox
	 */
	public void setUseSpecificMailbox(final boolean useSpecificMailbox) {
		this.useSpecificMailbox = useSpecificMailbox;
	}

	/**
	 * Checks if is acknowledge delivery.
	 *
	 * @return the acknowledgeDelivery
	 */
	public boolean isAcknowledgeDelivery() {
		return acknowledgeDelivery;
	}

	/**
	 * Sets the acknowledge delivery.
	 *
	 * @param acknowledgeDelivery the acknowledgeDelivery to set
	 */
	public void setAcknowledgeDelivery(boolean acknowledgeDelivery) {
		this.acknowledgeDelivery = acknowledgeDelivery;
	}

	/**
	 * Checks if is initialization.
	 *
	 * @return the startup
	 */
	public boolean isInitialization() {
		return initialization;
	}

	/**
	 * Sets the initialization.
	 *
	 * @param startup the startup to set
	 */
	public void setInitialization(boolean startup) {
		this.initialization = startup;
	}

}
