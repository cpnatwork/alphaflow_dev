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
 * $Id: IdentityCollector.java 3579 2012-02-15 10:59:19Z cpn $
 *************************************************************************/
package alpha.util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Creates and displays a {@link JFrame} to collect initial identification
 * information from an actor.
 */
public class IdentityCollector {

	/** True if information has been submitted by the actor. False otherwise. */
	boolean submit = false;

	/** ID of the actor. */
	String actorID;

	/** Role of the actor. */
	String roleID;

	/** Institution of the actor. */
	String institutionID;

	/** Introducing text information. */
	String introLabel = "Please supply your valid identification information.";

	/** Label text for actorID field. */
	String actorLabel = "Actor ID";

	/** Label text for institutionID field. */
	String institutionLabel = "Institution ID";

	/** Label text for roleID field. */
	String roleLabel = "Role ID";

	/**
	 * Checks if information has been submitted by the actor.
	 * 
	 * @return true, if information has been submitted by the actor.
	 */
	public boolean isSubmit() {
		return this.submit;
	}

	/**
	 * Gets the actorID
	 * 
	 * @return the actorID
	 */
	public String getActorID() {
		return this.actorID;
	}

	/**
	 * Sets the actorID.
	 * 
	 * @param actorID
	 *            the new actorID
	 */
	public void setActorID(final String actorID) {
		this.actorID = actorID;
	}

	/**
	 * Gets the roleID.
	 * 
	 * @return the roleID
	 */
	public String getRoleID() {
		return this.roleID;
	}

	/**
	 * Sets the roleID.
	 * 
	 * @param roleID
	 *            the new roleID
	 */
	public void setRoleID(final String roleID) {
		this.roleID = roleID;
	}

	/**
	 * Gets the institutionID.
	 * 
	 * @return the institutionID
	 */
	public String getInstitutionID() {
		return this.institutionID;
	}

	/**
	 * Sets the institutionID.
	 * 
	 * @param institutionID
	 *            the new institutionID
	 */
	public void setInstitutionID(final String institutionID) {
		this.institutionID = institutionID;
	}

	/**
	 * Instantiates a new {@link IdentityCollector}.
	 */
	public IdentityCollector() {

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

		description = new JLabel(this.actorLabel);
		description.setPreferredSize(new Dimension(120, 20));
		basic.add(description, gbc);

		final JTextField actor = new JTextField("Actor ID");
		gbc.gridx++;
		basic.add(actor, gbc);

		description = new JLabel(this.roleLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField role = new JTextField("Role ID");
		gbc.gridx++;
		basic.add(role, gbc);

		description = new JLabel(this.institutionLabel);
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(description, gbc);

		final JTextField institution = new JTextField("Institution ID");
		gbc.gridx++;
		basic.add(institution, gbc);

		final Object[] options = { "Submit" };

		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		final int ret = JOptionPane.showOptionDialog(frame, basic,
				"Identification Information", JOptionPane.OK_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		frame.setAlwaysOnTop(false);
		frame.setVisible(false);

		if (ret == JOptionPane.OK_OPTION) {
			this.submit = true;
			this.actorID = actor.getText();
			this.roleID = role.getText();
			this.institutionID = institution.getText();
		}

	}

}
