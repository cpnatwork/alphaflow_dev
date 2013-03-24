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
 * $Id$
 *************************************************************************/
package alpha.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.AlphaDoc;
import alpha.model.apa.AlphaCardType;
import alpha.model.apa.CorpusGenericus;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.ContributorID;
import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.util.UIDGenerator;

/**
 * With this class new AlphaCards can be created. All adornments save
 * "alphaCardName", "contributor" and "alphaCardType" will get the default
 * values.
 * 
 * 
 */
public class ContentCardCreator {
	// transient private static final Logger LOGGER =
	// Logger.getLogger(ContentCardCreator.class.getName());
	/** The apf. */
	protected AlphaPropsFacade apf = null;

	/** The cra. */
	private CRAPayload cra = null;

	/** The user node. */
	private Participant userNode;

	/**
	 * Instantiates a new content card creator.
	 * 
	 * @param apf
	 *            the apf
	 */
	public ContentCardCreator(final AlphaPropsFacade apf) {
		this.apf = apf;
	}

	/**
	 * Creates the.
	 * 
	 * @param state
	 *            the state
	 * @return the alpha card descriptor
	 */
	public AlphaCardDescriptor create(final int state) {
		final AlphaDoc ad = this.apf.getAlphaDoc();
		AlphaCardDescriptor ac = this.apf.createNewAlphaCardDescriptor();
		ac.setId(new AlphaCardID(ad.getEpisodeID(), UIDGenerator.generateUID()));

		/** Query for Name, Contributor, AlphaCardType */
		ac = this.createGui(ac, state);
		if (ac == null)
			return null;

		return ac;
	}

	/**
	 * Creates the gui.
	 * 
	 * @param alphaCardDescriptor
	 *            the alpha card descriptor
	 * @param state
	 *            the state
	 * @return the alpha card descriptor
	 */
	private AlphaCardDescriptor createGui(
			final AlphaCardDescriptor alphaCardDescriptor, final int state) {
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		final JPanel basic = new JPanel(new GridBagLayout());
		basic.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		final ContributorID sub = this.getSubject();

		JLabel adornmentName = new JLabel("AlphaCardDescriptor Title");
		adornmentName.setPreferredSize(new Dimension(120, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		basic.add(adornmentName, gbc);

		final JTextField title = new JTextField();
		title.setPreferredSize(new Dimension(120, 20));
		title.setText("Title");
		gbc.gridx++;
		basic.add(title, gbc);

		adornmentName = new JLabel(CorpusGenericus.INSTITUTION.value());
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(adornmentName, gbc);

		final JTextField institution = new JTextField();
		institution.setText(sub.getInstitution());
		gbc.gridx++;
		basic.add(institution, gbc);

		adornmentName = new JLabel(CorpusGenericus.ROLE.value());
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(adornmentName, gbc);

		final JTextField role = new JTextField();
		role.setText(sub.getRole());
		gbc.gridx++;
		basic.add(role, gbc);

		adornmentName = new JLabel(CorpusGenericus.ACTOR.value());
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(adornmentName, gbc);

		final JTextField actor = new JTextField();
		actor.setText(sub.getActor());
		gbc.gridx++;
		basic.add(actor, gbc);

		adornmentName = new JLabel(CorpusGenericus.ALPHACARDTYPE.value());
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(adornmentName, gbc);

		final JComboBox alphaCardType = new JComboBox(AlphaCardType.values());
		if (state == 1) {
			alphaCardType.setVisible(false);
		}
		gbc.gridx++;
		basic.add(alphaCardType, gbc);

		final int ret = JOptionPane.showConfirmDialog(new JFrame(), basic,
				"AlphaCardDescriptor values", JOptionPane.DEFAULT_OPTION);
		if (ret == JOptionPane.OK_OPTION) {

			alphaCardDescriptor.readAdornment(CorpusGenericus.TITLE.value())
					.setValue(title.getText());

			alphaCardDescriptor.readAdornment(CorpusGenericus.ACTOR.value())
					.setValue("=" + actor.getText());
			alphaCardDescriptor.readAdornment(CorpusGenericus.ROLE.value())
					.setValue("+" + role.getText());
			alphaCardDescriptor.readAdornment(
					CorpusGenericus.INSTITUTION.value()).setValue(
					"@" + institution.getText());

			alphaCardDescriptor.readAdornment(
					CorpusGenericus.ALPHACARDTYPE.value()).setValue(
					((AlphaCardType) alphaCardType.getSelectedItem()).value());

		} else
			return null;
		return alphaCardDescriptor;
	}

	/**
	 * Gets the subject.
	 * 
	 * @return the subject
	 */
	private ContributorID getSubject() {
		ContributorID sub = new ContributorID("institution", "role", "actor");
		final Set<Participant> part = this.cra.getListOfParticipants();
		for (final Participant participantNode : part) {
			final String name = participantNode.getContributor().getActor();
			final String port = participantNode.getNode().getPort();
			final String ip = participantNode.getNode().getIp();
			if (name.equals(this.userNode.getContributor().getActor())
					&& port.equals(this.userNode.getNode().getPort())
					&& ip.equals(this.userNode.getNode().getIp())) {
				sub = participantNode.getContributor();
			}
		}
		return sub;
	}

	/**
	 * Sets the cra.
	 * 
	 * @param cra
	 *            the new cra
	 */
	public void setCra(final CRAPayload cra) {
		this.cra = cra;
	}

	/**
	 * Sets the user.
	 * 
	 * @param userNode
	 *            the new user
	 */
	public void setUser(final Participant userNode) {
		this.userNode = userNode;
	}
}
