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
 * $Id: InvitationDialogView.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.editor.invitation;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Encapsulates all gui components necessary to show the invitation dialog
 * window.
 */
public class InvitationDialogView extends javax.swing.JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6258635608130837419L;

	/**
	 * Reference to the {@link InvitationDialogController} which controls the
	 * graphical representation of the invitation gui.
	 */
	private InvitationDialogController invitationDialogController;

	/**
	 * Creates and shows a new invitation dialog window.
	 * 
	 * @param parent
	 *            the parent
	 * @param modal
	 *            selects if invitation gui forcibly catches focus
	 */
	public InvitationDialogView(final java.awt.Frame parent, final boolean modal) {
		super(parent, modal);
		this.initComponents();
	}

	/**
	 * Initializes all gui components.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		this.jPanel1 = new javax.swing.JPanel();
		this.jLabel1 = new javax.swing.JLabel();
		this.fileOutputPanel = new javax.swing.JPanel();
		this.fileOutputRadio = new javax.swing.JRadioButton();
		this.fileOutputSelectionPanel = new javax.swing.JPanel();
		this.fileOutputPathLabel = new javax.swing.JLabel();
		this.fileOutputField = new javax.swing.JTextField();
		this.fileOutputSelect = new javax.swing.JButton();
		this.fileOutputFilenameLabel = new javax.swing.JLabel();
		this.fileOutputFilenameField = new javax.swing.JTextField();
		this.mailOutputPanel = new javax.swing.JPanel();
		this.mailOutputRadio = new javax.swing.JRadioButton();
		this.mailOutputSelectionPanel = new javax.swing.JPanel();
		this.recipientLabel = new javax.swing.JLabel();
		this.recipientField = new javax.swing.JTextField();
		this.subjectLabel = new javax.swing.JLabel();
		this.subjectField = new javax.swing.JTextField();
		this.messageLabel = new javax.swing.JLabel();
		this.messageScroll = new javax.swing.JScrollPane();
		this.messageText = new javax.swing.JTextArea();
		this.buttonPanel = new javax.swing.JPanel();
		this.inviteButton = new javax.swing.JButton();
		this.cancelButton = new javax.swing.JButton();

		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		this.jPanel1.setLayout(new java.awt.GridBagLayout());

		this.jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		this.jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		this.jLabel1
				.setText("Please select a method of invitation to invite the new actor.");
		this.jLabel1.setMinimumSize(new java.awt.Dimension(500, 14));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.insets = new java.awt.Insets(7, 0, 20, 0);
		this.jPanel1.add(this.jLabel1, gridBagConstraints);

		this.fileOutputPanel.setLayout(new java.awt.GridBagLayout());

		this.fileOutputRadio
				.setText("Deploy invitation directly to storage medium");
		this.fileOutputRadio.setMaximumSize(new java.awt.Dimension(500, 23));
		this.fileOutputRadio.setMinimumSize(new java.awt.Dimension(400, 23));
		this.fileOutputRadio.setPreferredSize(new java.awt.Dimension(400, 23));
		this.fileOutputRadio
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						InvitationDialogView.this
								.fileOutputRadioActionPerformed(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.fileOutputPanel.add(this.fileOutputRadio, gridBagConstraints);

		this.fileOutputSelectionPanel.setLayout(new java.awt.GridBagLayout());

		this.fileOutputPathLabel.setText("Output Path");
		this.fileOutputPathLabel.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		this.fileOutputSelectionPanel.add(this.fileOutputPathLabel,
				gridBagConstraints);

		this.fileOutputField.setEnabled(false);
		this.fileOutputField.setMinimumSize(new java.awt.Dimension(300, 20));
		this.fileOutputField.setPreferredSize(new java.awt.Dimension(300, 20));
		this.fileOutputField
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						InvitationDialogView.this
								.fileOutputFieldActionPerformed(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
		this.fileOutputSelectionPanel.add(this.fileOutputField,
				gridBagConstraints);

		this.fileOutputSelect.setText("Select Path");
		this.fileOutputSelect.setEnabled(false);
		this.fileOutputSelect
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						InvitationDialogView.this
								.fileOutputSelectActionPerformed(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		this.fileOutputSelectionPanel.add(this.fileOutputSelect,
				gridBagConstraints);

		this.fileOutputFilenameLabel.setText("Output Filename");
		this.fileOutputFilenameLabel.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
		this.fileOutputSelectionPanel.add(this.fileOutputFilenameLabel,
				gridBagConstraints);

		this.fileOutputFilenameField.setEnabled(false);
		this.fileOutputFilenameField.setMinimumSize(new java.awt.Dimension(300,
				20));
		this.fileOutputFilenameField
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						InvitationDialogView.this
								.fileOutputFilenameFieldActionPerformed(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(10, 12, 0, 0);
		this.fileOutputSelectionPanel.add(this.fileOutputFilenameField,
				gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.ipadx = 35;
		gridBagConstraints.ipady = 34;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 10);
		this.fileOutputPanel.add(this.fileOutputSelectionPanel,
				gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		this.jPanel1.add(this.fileOutputPanel, gridBagConstraints);

		this.mailOutputPanel.setLayout(new java.awt.GridBagLayout());

		this.mailOutputRadio.setText("Send invitation via email");
		this.mailOutputRadio.setMinimumSize(new java.awt.Dimension(500, 23));
		this.mailOutputRadio
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						InvitationDialogView.this
								.mailOutputRadioActionPerformed(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.mailOutputPanel.add(this.mailOutputRadio, gridBagConstraints);

		this.mailOutputSelectionPanel.setLayout(new java.awt.GridBagLayout());

		this.recipientLabel.setText("Recipient");
		this.recipientLabel.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		this.mailOutputSelectionPanel.add(this.recipientLabel,
				gridBagConstraints);

		this.recipientField.setEnabled(false);
		this.recipientField.setMinimumSize(new java.awt.Dimension(300, 20));
		this.recipientField.setPreferredSize(new java.awt.Dimension(300, 20));
		this.recipientField
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						InvitationDialogView.this
								.recipientFieldActionPerformed(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		this.mailOutputSelectionPanel.add(this.recipientField,
				gridBagConstraints);

		this.subjectLabel.setText("Subject");
		this.subjectLabel.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
		this.mailOutputSelectionPanel
				.add(this.subjectLabel, gridBagConstraints);

		this.subjectField.setEnabled(false);
		this.subjectField.setMinimumSize(new java.awt.Dimension(300, 20));
		this.subjectField
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						InvitationDialogView.this
								.subjectFieldActionPerformed(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
		this.mailOutputSelectionPanel
				.add(this.subjectField, gridBagConstraints);

		this.messageLabel.setText("Custom Message");
		this.messageLabel.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
		this.mailOutputSelectionPanel
				.add(this.messageLabel, gridBagConstraints);

		this.messageScroll.setBorder(null);
		this.messageScroll.setEnabled(false);
		this.messageScroll.setMinimumSize(new java.awt.Dimension(300, 150));
		this.messageScroll.setPreferredSize(new java.awt.Dimension(300, 150));

		this.messageText.setColumns(20);
		this.messageText.setRows(5);
		this.messageText.setBorder(this.subjectField.getBorder());
		this.messageText.setEnabled(false);
		this.messageText.setMinimumSize(new java.awt.Dimension(166, 96));
		this.messageScroll.setViewportView(this.messageText);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
		this.mailOutputSelectionPanel.add(this.messageScroll,
				gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.ipadx = 35;
		gridBagConstraints.ipady = 34;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 10);
		this.mailOutputPanel.add(this.mailOutputSelectionPanel,
				gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		this.jPanel1.add(this.mailOutputPanel, gridBagConstraints);

		this.buttonPanel.setMinimumSize(new java.awt.Dimension(200, 30));
		this.buttonPanel.setLayout(new java.awt.GridBagLayout());

		this.inviteButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		this.inviteButton.setText("Invite Actor");
		this.inviteButton.setEnabled(false);
		this.inviteButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						InvitationDialogView.this
								.inviteButtonActionPerformed(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		this.buttonPanel.add(this.inviteButton, gridBagConstraints);

		this.cancelButton.setText("Cancel");
		this.cancelButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						InvitationDialogView.this
								.cancelButtonActionPerformed(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		this.buttonPanel.add(this.cancelButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
		this.jPanel1.add(this.buttonPanel, gridBagConstraints);

		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 547, Short.MAX_VALUE)
				.addGroup(
						layout.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(
										layout.createSequentialGroup()
												.addGap(0, 0, Short.MAX_VALUE)
												.addComponent(
														this.jPanel1,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														547,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(0, 0, Short.MAX_VALUE))));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 482, Short.MAX_VALUE)
				.addGroup(
						layout.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(
										layout.createSequentialGroup()
												.addGap(0, 0, Short.MAX_VALUE)
												.addComponent(
														this.jPanel1,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														482,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(0, 0, Short.MAX_VALUE))));

		this.pack();
	}// </editor-fold>

	/**
	 * Handler for actions on the file output radio button.
	 * 
	 * @param evt
	 *            the occurred event
	 */
	private void fileOutputRadioActionPerformed(
			final java.awt.event.ActionEvent evt) {
		this.invitationDialogController.toggleSelectFileOutput();
	}

	/**
	 * Handler for actions on the file output text field.
	 * 
	 * @param evt
	 *            the occurred event
	 */
	private void fileOutputFieldActionPerformed(
			final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * Handler for actions on the file output select button.
	 * 
	 * @param evt
	 *            the occurred event
	 */
	private void fileOutputSelectActionPerformed(
			final java.awt.event.ActionEvent evt) {
		this.invitationDialogController.chooseFileOutputFile();
	}

	/**
	 * Handler for actions on the filename text field.
	 * 
	 * @param evt
	 *            the occurred event
	 */
	private void fileOutputFilenameFieldActionPerformed(
			final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * Handler for actions on the mail output radio button.
	 * 
	 * @param evt
	 *            the occurred event
	 */
	private void mailOutputRadioActionPerformed(
			final java.awt.event.ActionEvent evt) {
		this.invitationDialogController.toggleSelectMailOutput();
	}

	/**
	 * Handler for actions on the recipient text field.
	 * 
	 * @param evt
	 *            the occurred event
	 */
	private void recipientFieldActionPerformed(
			final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * Handler for actions on the subject text field.
	 * 
	 * @param evt
	 *            the occurred event
	 */
	private void subjectFieldActionPerformed(
			final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * Handler for actions on the invite button.
	 * 
	 * @param evt
	 *            the occurred event
	 */
	private void inviteButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {
		this.invitationDialogController.invite();
	}

	/**
	 * Handler for actions on the cancel button.
	 * 
	 * @param evt
	 *            the occurred event
	 */
	private void cancelButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {
		this.invitationDialogController.close();
	}

	/**
	 * Main method for demonstration purposes only.
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (final javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (final ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(
					InvitationDialogView.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (final InstantiationException ex) {
			java.util.logging.Logger.getLogger(
					InvitationDialogView.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (final IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(
					InvitationDialogView.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (final javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(
					InvitationDialogView.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				final InvitationDialogView dialog = new InvitationDialogView(
						new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {

					@Override
					public void windowClosing(final java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	/**
	 * Gets the button panel.
	 * 
	 * @return the button panel
	 */
	public JPanel getButtonPanel() {
		return this.buttonPanel;
	}

	/**
	 * Sets the button panel.
	 * 
	 * @param buttonPanel
	 *            the new button panel
	 */
	public void setButtonPanel(final JPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
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
	 * Gets the file output field.
	 * 
	 * @return the file output field
	 */
	public JTextField getFileOutputField() {
		return this.fileOutputField;
	}

	/**
	 * Sets the file output field.
	 * 
	 * @param fileOutputField
	 *            the new file output field
	 */
	public void setFileOutputField(final JTextField fileOutputField) {
		this.fileOutputField = fileOutputField;
	}

	/**
	 * Gets the file output filename field.
	 * 
	 * @return the file output filename field
	 */
	public JTextField getFileOutputFilenameField() {
		return this.fileOutputFilenameField;
	}

	/**
	 * Sets the file output filename field.
	 * 
	 * @param fileOutputFilenameField
	 *            the new file output filename field
	 */
	public void setFileOutputFilenameField(
			final JTextField fileOutputFilenameField) {
		this.fileOutputFilenameField = fileOutputFilenameField;
	}

	/**
	 * Gets the file output filename label.
	 * 
	 * @return the file output filename label
	 */
	public JLabel getFileOutputFilenameLabel() {
		return this.fileOutputFilenameLabel;
	}

	/**
	 * Sets the file output filename label.
	 * 
	 * @param fileOutputFilenameLabel
	 *            the new file output filename label
	 */
	public void setFileOutputFilenameLabel(final JLabel fileOutputFilenameLabel) {
		this.fileOutputFilenameLabel = fileOutputFilenameLabel;
	}

	/**
	 * Gets the file output panel.
	 * 
	 * @return the file output panel
	 */
	public JPanel getFileOutputPanel() {
		return this.fileOutputPanel;
	}

	/**
	 * Sets the file output panel.
	 * 
	 * @param fileOutputPanel
	 *            the new file output panel
	 */
	public void setFileOutputPanel(final JPanel fileOutputPanel) {
		this.fileOutputPanel = fileOutputPanel;
	}

	/**
	 * Gets the file output path label.
	 * 
	 * @return the file output path label
	 */
	public JLabel getFileOutputPathLabel() {
		return this.fileOutputPathLabel;
	}

	/**
	 * Sets the file output path label.
	 * 
	 * @param fileOutputPathLabel
	 *            the new file output path label
	 */
	public void setFileOutputPathLabel(final JLabel fileOutputPathLabel) {
		this.fileOutputPathLabel = fileOutputPathLabel;
	}

	/**
	 * Gets the file output radio.
	 * 
	 * @return the file output radio
	 */
	public JRadioButton getFileOutputRadio() {
		return this.fileOutputRadio;
	}

	/**
	 * Sets the file output radio.
	 * 
	 * @param fileOutputRadio
	 *            the new file output radio
	 */
	public void setFileOutputRadio(final JRadioButton fileOutputRadio) {
		this.fileOutputRadio = fileOutputRadio;
	}

	/**
	 * Gets the file output select.
	 * 
	 * @return the file output select
	 */
	public JButton getFileOutputSelect() {
		return this.fileOutputSelect;
	}

	/**
	 * Sets the file output select.
	 * 
	 * @param fileOutputSelect
	 *            the new file output select
	 */
	public void setFileOutputSelect(final JButton fileOutputSelect) {
		this.fileOutputSelect = fileOutputSelect;
	}

	/**
	 * Gets the file output selection panel.
	 * 
	 * @return the file output selection panel
	 */
	public JPanel getFileOutputSelectionPanel() {
		return this.fileOutputSelectionPanel;
	}

	/**
	 * Sets the file output selection panel.
	 * 
	 * @param fileOutputSelectionPanel
	 *            the new file output selection panel
	 */
	public void setFileOutputSelectionPanel(
			final JPanel fileOutputSelectionPanel) {
		this.fileOutputSelectionPanel = fileOutputSelectionPanel;
	}

	/**
	 * Gets the invitation dialog controller.
	 * 
	 * @return the invitation dialog controller
	 */
	public InvitationDialogController getInvitationDialogController() {
		return this.invitationDialogController;
	}

	/**
	 * Sets the invitation dialog controller.
	 * 
	 * @param invitationDialogController
	 *            the new invitation dialog controller
	 */
	public void setInvitationDialogController(
			final InvitationDialogController invitationDialogController) {
		this.invitationDialogController = invitationDialogController;
	}

	/**
	 * Gets the invite button.
	 * 
	 * @return the invite button
	 */
	public JButton getInviteButton() {
		return this.inviteButton;
	}

	/**
	 * Sets the invite button.
	 * 
	 * @param inviteButton
	 *            the new invite button
	 */
	public void setInviteButton(final JButton inviteButton) {
		this.inviteButton = inviteButton;
	}

	/**
	 * Gets the j label1.
	 * 
	 * @return the j label1
	 */
	public JLabel getjLabel1() {
		return this.jLabel1;
	}

	/**
	 * Sets the j label1.
	 * 
	 * @param jLabel1
	 *            the new j label1
	 */
	public void setjLabel1(final JLabel jLabel1) {
		this.jLabel1 = jLabel1;
	}

	/**
	 * Gets the j panel1.
	 * 
	 * @return the j panel1
	 */
	public JPanel getjPanel1() {
		return this.jPanel1;
	}

	/**
	 * Sets the j panel1.
	 * 
	 * @param jPanel1
	 *            the new j panel1
	 */
	public void setjPanel1(final JPanel jPanel1) {
		this.jPanel1 = jPanel1;
	}

	/**
	 * Gets the mail output panel.
	 * 
	 * @return the mail output panel
	 */
	public JPanel getMailOutputPanel() {
		return this.mailOutputPanel;
	}

	/**
	 * Sets the mail output panel.
	 * 
	 * @param mailOutputPanel
	 *            the new mail output panel
	 */
	public void setMailOutputPanel(final JPanel mailOutputPanel) {
		this.mailOutputPanel = mailOutputPanel;
	}

	/**
	 * Gets the mail output radio.
	 * 
	 * @return the mail output radio
	 */
	public JRadioButton getMailOutputRadio() {
		return this.mailOutputRadio;
	}

	/**
	 * Sets the mail output radio.
	 * 
	 * @param mailOutputRadio
	 *            the new mail output radio
	 */
	public void setMailOutputRadio(final JRadioButton mailOutputRadio) {
		this.mailOutputRadio = mailOutputRadio;
	}

	/**
	 * Gets the mail output selection panel.
	 * 
	 * @return the mail output selection panel
	 */
	public JPanel getMailOutputSelectionPanel() {
		return this.mailOutputSelectionPanel;
	}

	/**
	 * Sets the mail output selection panel.
	 * 
	 * @param mailOutputSelectionPanel
	 *            the new mail output selection panel
	 */
	public void setMailOutputSelectionPanel(
			final JPanel mailOutputSelectionPanel) {
		this.mailOutputSelectionPanel = mailOutputSelectionPanel;
	}

	/**
	 * Gets the message label.
	 * 
	 * @return the message label
	 */
	public JLabel getMessageLabel() {
		return this.messageLabel;
	}

	/**
	 * Sets the message label.
	 * 
	 * @param messageLabel
	 *            the new message label
	 */
	public void setMessageLabel(final JLabel messageLabel) {
		this.messageLabel = messageLabel;
	}

	/**
	 * Gets the message scroll.
	 * 
	 * @return the message scroll
	 */
	public JScrollPane getMessageScroll() {
		return this.messageScroll;
	}

	/**
	 * Sets the message scroll.
	 * 
	 * @param messageScroll
	 *            the new message scroll
	 */
	public void setMessageScroll(final JScrollPane messageScroll) {
		this.messageScroll = messageScroll;
	}

	/**
	 * Gets the message text.
	 * 
	 * @return the message text
	 */
	public JTextArea getMessageText() {
		return this.messageText;
	}

	/**
	 * Sets the message text.
	 * 
	 * @param messageText
	 *            the new message text
	 */
	public void setMessageText(final JTextArea messageText) {
		this.messageText = messageText;
	}

	/**
	 * Gets the recipient field.
	 * 
	 * @return the recipient field
	 */
	public JTextField getRecipientField() {
		return this.recipientField;
	}

	/**
	 * Sets the recipient field.
	 * 
	 * @param recipientField
	 *            the new recipient field
	 */
	public void setRecipientField(final JTextField recipientField) {
		this.recipientField = recipientField;
	}

	/**
	 * Gets the recipient label.
	 * 
	 * @return the recipient label
	 */
	public JLabel getRecipientLabel() {
		return this.recipientLabel;
	}

	/**
	 * Sets the recipient label.
	 * 
	 * @param recipientLabel
	 *            the new recipient label
	 */
	public void setRecipientLabel(final JLabel recipientLabel) {
		this.recipientLabel = recipientLabel;
	}

	/**
	 * Gets the subject field.
	 * 
	 * @return the subject field
	 */
	public JTextField getSubjectField() {
		return this.subjectField;
	}

	/**
	 * Sets the subject field.
	 * 
	 * @param subjectField
	 *            the new subject field
	 */
	public void setSubjectField(final JTextField subjectField) {
		this.subjectField = subjectField;
	}

	/**
	 * Gets the subject label.
	 * 
	 * @return the subject label
	 */
	public JLabel getSubjectLabel() {
		return this.subjectLabel;
	}

	/**
	 * Sets the subject label.
	 * 
	 * @param subjectLabel
	 *            the new subject label
	 */
	public void setSubjectLabel(final JLabel subjectLabel) {
		this.subjectLabel = subjectLabel;
	}

	// Variables declaration - do not modify
	/** The button panel. */
	private javax.swing.JPanel buttonPanel;

	/** The cancel button. */
	private javax.swing.JButton cancelButton;

	/** The file output field. */
	private javax.swing.JTextField fileOutputField;

	/** The file output filename field. */
	private javax.swing.JTextField fileOutputFilenameField;

	/** The file output filename label. */
	private javax.swing.JLabel fileOutputFilenameLabel;

	/** The file output panel. */
	private javax.swing.JPanel fileOutputPanel;

	/** The file output path label. */
	private javax.swing.JLabel fileOutputPathLabel;

	/** The file output radio. */
	private javax.swing.JRadioButton fileOutputRadio;

	/** The file output select. */
	private javax.swing.JButton fileOutputSelect;

	/** The file output selection panel. */
	private javax.swing.JPanel fileOutputSelectionPanel;

	/** The invite button. */
	private javax.swing.JButton inviteButton;

	/** The j label1. */
	private javax.swing.JLabel jLabel1;

	/** The j panel1. */
	private javax.swing.JPanel jPanel1;

	/** The mail output panel. */
	private javax.swing.JPanel mailOutputPanel;

	/** The mail output radio. */
	private javax.swing.JRadioButton mailOutputRadio;

	/** The mail output selection panel. */
	private javax.swing.JPanel mailOutputSelectionPanel;

	/** The message label. */
	private javax.swing.JLabel messageLabel;

	/** The message scroll. */
	private javax.swing.JScrollPane messageScroll;

	/** The message text. */
	private javax.swing.JTextArea messageText;

	/** The recipient field. */
	private javax.swing.JTextField recipientField;

	/** The recipient label. */
	private javax.swing.JLabel recipientLabel;

	/** The subject field. */
	private javax.swing.JTextField subjectField;

	/** The subject label. */
	private javax.swing.JLabel subjectLabel;
	// End of variables declaration
}
