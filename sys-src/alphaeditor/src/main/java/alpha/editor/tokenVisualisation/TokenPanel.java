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
 * $Id: TokenPanel.java 3792 2012-05-05 10:27:03Z uj32uvac $
 *************************************************************************/
package alpha.editor.tokenVisualisation;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import alpha.model.cra.Doyen;
import alpha.model.cra.Initiator;
import alpha.model.cra.PatientContact;
import alpha.model.cra.StandardTokens;
import alpha.model.cra.Token;

/**
 * The Class TokenPanel.
 */
public class TokenPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4738309232558345189L;

	/** The icon initiator. */
	private final JLabel iconInitiator = new JLabel(
			TokenIcons.INITIATOR.getDescription());

	/** The icon doyen. */
	private final JLabel iconDoyen = new JLabel(
			TokenIcons.DOYEN.getDescription());

	/** The icon patient contact. */
	private final JLabel iconPatientContact = new JLabel(
			TokenIcons.PATIENT_CONTACT.getDescription());

	/**
	 * Inits the.
	 */
	public void init() {
		this.iconInitiator.setIcon(TokenIcons.INITIATOR.getIcon());
		this.iconDoyen.setIcon(TokenIcons.DOYEN.getIcon());
		this.iconPatientContact.setIcon(TokenIcons.PATIENT_CONTACT.getIcon());

		this.setMaximumSize(new java.awt.Dimension(205, 128));
		this.setMinimumSize(new java.awt.Dimension(205, 128));
		this.setPreferredSize(new Dimension(233, 128));
		this.setName("");

		// generated by netbeans
		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														this.iconInitiator,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														this.iconDoyen,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														this.iconPatientContact,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(9, 9, 9)
								.addComponent(this.iconInitiator,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										32,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.iconDoyen,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										32,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.iconPatientContact,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										32,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		this.iconInitiator.setVisible(false);
		this.iconDoyen.setVisible(false);
		this.iconPatientContact.setVisible(false);
	}

	/**
	 * Update.
	 * 
	 * @param tokens
	 *            the tokens
	 */
	public void update(final List<Token> tokens) {
		for (final Token t : tokens) {
			if (t.getName().equals(StandardTokens.INITIATOR.value())) {
				this.iconInitiator.setVisible(Initiator.INITIATOR.value()
						.equals(t.getValue()));
			} else if (t.getName().equals(StandardTokens.DOYEN.value())) {
				this.iconDoyen.setVisible(Doyen.DOYEN.value().equals(
						t.getValue()));
			} else if (t.getName().equals(
					StandardTokens.PATIENT_CONTACT.value())) {
				this.iconPatientContact
						.setVisible(PatientContact.PATIENT_CONTACT.value()
								.equals(t.getValue()));
			}
		}
	}

}