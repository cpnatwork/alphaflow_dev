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
 * $Id: TokenDialog.java 4000 2012-09-13 13:41:02Z uj32uvac $
 *************************************************************************/
package alpha.editor.tokenVisualisation;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alpha.editor.EditorData;
import alpha.editor.ParticipantTableModel;
import alpha.editor.Swing_Helper;
import alpha.editor.adornmentVisualisation.VisualisationState;
import alpha.facades.AlphaPropsFacade;
import alpha.model.cra.Participant;
import alpha.model.cra.PatientContact;
import alpha.model.cra.StandardTokens;
import alpha.model.cra.Token;
import alpha.model.docconfig.AlphadocConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class TokenDialog lets the actor change his tokens. It has a normal and
 * an admin mode (tabs).
 */
@Component
public class TokenDialog {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(TokenDialog.class.getName());

	/** True if information has been submitted by the actor. False otherwise. */
	boolean submit = false;

	/** The participants combo box. */
	private ParticipantComboBox participantsComboBox = new ParticipantComboBox();

	/** The current doyen label. */
	private final JLabel currentDoyenLabel = new JLabel("Aktueller Doyen");

	/** The new doyen label. */
	private final JLabel newDoyenLabel = new JLabel("Neuer Doyen");

	/** The current doyen instance label. */
	private final JLabel currentDoyenInstanceLabel = new JLabel();

	/** The current doyen. */
	private Participant currentDoyen;

	/** The local participant. */
	private Participant localParticipant;

	/** The patient contact label. */
	private final JLabel patientContactLabel = new JLabel("Patientenkontakt");

	/** The patient contact check box. */
	private final JCheckBox patientContactCheckBox = new JCheckBox();

	/** The button to deselect the participantsComboBox. */
	private final JButton discardButton = new JButton("Discard");

	/** Indicates if user has patient contact. */
	private boolean patientContact;

	/** The config. */
	private AlphadocConfig config;

	/** The ed. */
	@Autowired
	private final EditorData ed = null;

	/** The basic. */
	private JPanel adminPanel;

	/** The token visus. */
	protected HashMap<String, TokenVisualisation> tokenVisus;

	/** The tabbed pane. */
	private final JTabbedPane tabbedPane = new JTabbedPane();

	/** The apf. */
	@Autowired
	private final AlphaPropsFacade apf = null;

	/** The participant table model. */
	@Autowired
	private final ParticipantTableModel participantModel = null;

	/**
	 * Gets the tabbed pane.
	 * 
	 * @return the tabbed pane
	 */
	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}

	/**
	 * Gets the token visus.
	 * 
	 * @return the token visus
	 */
	public HashMap<String, TokenVisualisation> getTokenVisus() {
		return this.tokenVisus;
	}

	/** The value changed. */
	private boolean patientContactChanged = false;

	/**
	 * Checks if is value changed.
	 * 
	 * @return true, if is value changed
	 */
	public boolean isPatientContactChanged() {
		return this.patientContactChanged;
	}

	/**
	 * Sets the value changed.
	 *
	 * @param patientContactChanged the new patient contact changed
	 */
	public void setPatientContactChanged(final boolean patientContactChanged) {
		this.patientContactChanged = patientContactChanged;
	}

	/**
	 * Gets the participants combo box.
	 *
	 * @return the participants combo box
	 */
	public JComboBox getParticipantsComboBox() {
		return this.participantsComboBox;
	}

	/**
	 * Creates a new DoyenDialog.
	 *
	 */
	public void init() {
		this.setSubmit(false);
		this.config = this.ed.getConfig();
		this.currentDoyen = this.apf.getCurrentDoyen();
		TokenDialog.LOGGER.finer("TOKENDIALOG.INIT()---------------DOYEN"
				+ this.currentDoyen);

		this.localParticipant = this.apf.getParticipantByActor(this.config
				.getLocalNodeID().getContributor().getActor());
		TokenDialog.LOGGER
				.finer("TOKENDIALOG.INIT()---------------LOCALPARTICIPANT"
						+ this.localParticipant);

		this.participantsComboBox = new ParticipantComboBox();
		this.participantsComboBox.setModel(this.participantModel);

		// deactivate localParticipant in participantsComboBox
		this.participantsComboBox.setDisabledIndex(this.participantModel
				.getIndexOfParticipant(this.localParticipant));
		this.participantsComboBox.repaint();

		TokenDialog.LOGGER.finer("TOKENDIALOG.INIT()---------------"
				+ this.localParticipant.equals(this.currentDoyen));
		TokenDialog.LOGGER.finer("TOKENDIALOG.INIT()---------------"
				+ this.participantsComboBox.getModel().getSize());

		boolean state = ((this.participantsComboBox.getModel().getSize() > 1) && (this.localParticipant
				.equals(this.currentDoyen)));
		this.participantsComboBox.setEnabled(state);
		this.newDoyenLabel.setEnabled(state);
		this.discardButton.setEnabled(state);

		this.patientContact = PatientContact.PATIENT_CONTACT.value().equals(
				this.localParticipant.readToken(
						StandardTokens.PATIENT_CONTACT.value()).getValue());

		this.patientContactCheckBox.setSelected(this.patientContact);
		this.patientContactCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {

				TokenDialog.this.patientContact = TokenDialog.this.patientContactCheckBox
						.isSelected();

				TokenDialog.this.setPatientContactChanged(true);
			}
		});
	}

	/**
	 * Show dialog.
	 */
	public void showDialog() {

		final JFrame frame = new JFrame();

		this.currentDoyenLabel.setIcon(TokenIcons.DOYEN.getIcon());
		this.newDoyenLabel.setIcon(TokenIcons.NEWDOYEN.getIcon());
		this.patientContactLabel.setIcon(TokenIcons.PATIENT_CONTACT.getIcon());

		this.participantsComboBox.setSelectedIndex(-1);

		if (this.currentDoyen != null) {
			this.currentDoyenInstanceLabel.setText(this.currentDoyen
					.getContributor().toString());
		}

		// layout generated with netbeans
		final Container container = new Container();
		final GroupLayout layout = new GroupLayout(container);
		container.setLayout(layout);
		final JSeparator jSeparator1 = new javax.swing.JSeparator();

		this.discardButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						TokenDialog.this.participantsComboBox
								.setSelectedIndex(-1);
						TokenDialog.this.participantsComboBox.repaint();
					}
				});

		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
																.addComponent(
																		this.currentDoyenLabel,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(
																		this.newDoyenLabel,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		112,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addComponent(
														this.patientContactLabel))
								.addGap(45, 45, 45)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														this.participantsComboBox,
														0, 1, Short.MAX_VALUE)
												.addComponent(
														this.currentDoyenInstanceLabel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														300, Short.MAX_VALUE)
												.addComponent(
														this.patientContactCheckBox)
												.addComponent(discardButton))
								.addContainerGap())
				.addGroup(
						layout.createSequentialGroup().addGap(10, 10, 10)
								.addComponent(jSeparator1).addGap(10, 10, 10)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														this.currentDoyenLabel)
												.addComponent(
														this.currentDoyenInstanceLabel))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														this.newDoyenLabel)
												.addComponent(
														this.participantsComboBox,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(18, 18, 18)
								.addComponent(discardButton)
								.addGap(18, 18, 18)
								.addComponent(jSeparator1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										10,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.CENTER)
												.addComponent(
														this.patientContactCheckBox)
												.addComponent(
														this.patientContactLabel))
								.addContainerGap(174, Short.MAX_VALUE))

		);

		this.initAdminPanel();
		this.update();
		this.displayAdminPanel();

		this.tabbedPane.removeAll();
		this.tabbedPane.addTab("Tokenweitergabe", container);
		this.tabbedPane.addTab("Admin", this.adminPanel);

		final Object[] options = { "Submit", "Cancel" };
		final int ret = JOptionPane.showOptionDialog(frame, this.tabbedPane,
				"Token Dialog", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (ret == JOptionPane.OK_OPTION) {
			this.submit = true;
		}
	}

	/**
	 * Inits the admin panel.
	 */
	public void initAdminPanel() {
		final GridBagLayout gbl = new GridBagLayout();
		this.adminPanel = new JPanel(gbl);
		this.adminPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	}

	/**
	 * Updates the visualisation state of the tokens of the participant.
	 */
	private void update() {
		this.tokenVisus = new LinkedHashMap<String, TokenVisualisation>();

		for (final Token tt : this.localParticipant.readTokens()) {
			this.tokenVisus.put(tt.getName(), new TokenVisualisation(tt,
					VisualisationState.AVAILABLE));
		}
	}

	/**
	 * Display admin panel.
	 */
	private void displayAdminPanel() {

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(1, 1, 1, 1);
		this.adminPanel.removeAll();

		final JLabel instanceView = new JLabel("Tokens");
		instanceView.setFont(new Font("", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.adminPanel.add(instanceView, gbc);

		String oldCategory = "";
		for (final TokenVisualisation tokenVisualisation : this.tokenVisus
				.values()) {

			if (!tokenVisualisation.getCategoryVisualisation()
					.getSelectedItem().toString().equals(oldCategory)) {
				oldCategory = tokenVisualisation.getCategoryVisualisation()
						.getSelectedItem().toString();
				// draw a separator label for this ImpactScope
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.gridwidth = 3;
				this.adminPanel.add(
						Swing_Helper.createTitledSeparator(oldCategory), gbc);
				gbc.gridwidth = 1;
			}

			if (tokenVisualisation.getState().equals(VisualisationState.UPDATE)) {
				tokenVisualisation.enableValueVisualisation(true);
				TokenDialog.LOGGER.finer("Enable ValueVisu of Token "
						+ tokenVisualisation.getValueVisualisation().getName());
				this.adminPanel.revalidate();
				this.adminPanel.updateUI();

			} else {
				tokenVisualisation.enableValueVisualisation(false);
			}

			JComponent stateComponent = new JLabel("");
			switch (tokenVisualisation.getState()) {
			case UNCHANGEABLE:
			case UPDATE:
				stateComponent = new JLabel(tokenVisualisation.getState()
						.getIcon());
				stateComponent.setToolTipText(tokenVisualisation.getState()
						.getDescription());
				break;
			case AVAILABLE:
				/*
				 * the button is needed here, because else you can't easily
				 * specify the type of the listener (KeyListener for JTextField
				 * or ActionListener for JComboBox
				 */
				stateComponent = new JButton(
						VisualisationState.UPDATE.getIcon());
				stateComponent.setName(tokenVisualisation.getName());
				stateComponent.setToolTipText(VisualisationState.UPDATE
						.getDescription());
				((JButton) stateComponent)
						.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(final ActionEvent e) {
								final JComponent source = (JComponent) e
										.getSource();
								final TokenVisualisation tv = TokenDialog.this.tokenVisus
										.get(source.getName());
								tv.setState(VisualisationState.UPDATE);
								TokenDialog.this.displayAdminPanel();
							}
						});
				break;
			}
			stateComponent.setPreferredSize(new Dimension(45, 20));
			gbc.gridx = 0;
			gbc.gridy++;
			tokenVisualisation.getNameVisualisation().setEnabled(false);
			this.adminPanel.add(tokenVisualisation.getNameVisualisation(), gbc);
			gbc.gridx++;
			this.adminPanel
					.add(tokenVisualisation.getValueVisualisation(), gbc);
			gbc.gridx++;
			this.adminPanel.add(stateComponent, gbc);
		}

		gbc.gridx = 0;
		gbc.gridy++;
		final JButton discard = new JButton("Discard");
		discard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				TokenDialog.this.update();
				TokenDialog.this.displayAdminPanel();
			}
		});
		gbc.gridx++;
		this.adminPanel.add(discard, gbc);

		this.adminPanel.revalidate();
	}

	/**
	 * Checks if is indicates if user has patient contact.
	 * 
	 * @return the indicates if user has patient contact
	 */
	public boolean isPatientContact() {
		return this.patientContact;
	}

	/**
	 * Sets the indicates if user has patient contact.
	 * 
	 * @param patientContact
	 *            the new indicates if user has patient contact
	 */
	public void setPatientContact(final boolean patientContact) {
		this.patientContact = patientContact;
	}

	/**
	 * Gets the new doyen selected in the combobox.
	 * 
	 * @return the new doyen
	 */
	public Participant getNewDoyen() {
		return (Participant) this.participantModel.getSelectedItem();
	}

	/**
	 * Checks if is true if information has been submitted by the actor.
	 * 
	 * @return the true if information has been submitted by the actor
	 */
	public boolean isSubmit() {
		return this.submit;
	}

	/**
	 * Sets the true if information has been submitted by the actor.
	 * 
	 * @param submit
	 *            the new true if information has been submitted by the actor
	 */
	public void setSubmit(final boolean submit) {
		this.submit = submit;
	}
}
