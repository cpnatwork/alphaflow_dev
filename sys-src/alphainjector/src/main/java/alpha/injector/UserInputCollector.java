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
package alpha.injector;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.AlphaCardType;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.VersionControl;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;
import alpha.util.UIDGenerator;

/**
 * This class creates a GUI in which the user can insert values for the
 * Adornments AlphaCardName (Title), Subject, Object and AlphaCardType. This
 * values are put into am AlphaCardDescriptor instance that is returned to the
 * calling class.
 * 
 */
public class UserInputCollector {
	// transient private static final Logger LOGGER =
	// Logger.getLogger(ContentCardCreator.class.getName());
	/** The episode id. */
	private final String episodeID;

	/** The title t. */
	protected JTextField titleT;

	/** The institution t. */
	protected JTextField institutionT;

	/** The actor t. */
	protected JTextField actorT;

	/** The role t. */
	protected JTextField roleT;

	/** The type t. */
	protected JComboBox typeT;

	/** The config. */
	private final AlphadocConfig config;

	/**
	 * This Constructor initializes the variable alphaCardDescriptor.
	 * 
	 * @param episodeID
	 *            the episode id
	 * @param config
	 *            the config
	 */
	public UserInputCollector(final String episodeID,
			final AlphadocConfig config) {
		this.episodeID = episodeID;
		this.config = config;
	}

	/**
	 * This method inserts the values from the system parameters into the
	 * AlphaCardDescriptor instance.
	 * 
	 * @param alphaCardDescriptor
	 *            the alpha card descriptor
	 */
	public void fillAlphaCardWithSystemProperties(final AlphaCardDescriptor alphaCardDescriptor) {	
		String alphaCardTitle = System.getProperty("alphaCardTitle");
		if (alphaCardTitle == null)
			alphaCardTitle = "";
		
		String actorID = System.getProperty("actorID");
		if (actorID == null)
			actorID = "";
		
		String roleID = System.getProperty("roleID");
		if (roleID == null)
			roleID = "";
		
		String institutionID = System.getProperty("institutionID");
		if (institutionID == null)
			institutionID = "";
		
		//Default alphaCardType = DOCUMENTATION
		String alphaCardType = System.getProperty("alphaCardType");
		if (alphaCardType == null)
			alphaCardType = "DOCUMENTATION";
		
		alphaCardDescriptor.readAdornment(CorpusGenericus.TITLE.value())
				.setValue(alphaCardTitle);
		alphaCardDescriptor.readAdornment(CorpusGenericus.ACTOR.value())
				.setValue("=" + actorID);
		alphaCardDescriptor.readAdornment(CorpusGenericus.ROLE.value())
				.setValue("+" + roleID);
		alphaCardDescriptor.readAdornment(
				CorpusGenericus.INSTITUTION.value()).setValue(
				"@" + institutionID);

		alphaCardDescriptor.readAdornment(
				CorpusGenericus.ALPHACARDTYPE.value()).setValue(
						alphaCardType);

		/* set other values */
		alphaCardDescriptor.setId(new AlphaCardID(this.episodeID,
				UIDGenerator.generateUID()));
		alphaCardDescriptor.readAdornment(CorpusGenericus.VERSION.value())
				.setValue("1.0");
		alphaCardDescriptor.readAdornment(
				CorpusGenericus.VERSIONCONTROL.value()).setValue(
				VersionControl.VERSIONED.value());
	}
	
	
	/**
	 * This method displays the GUI and puts the inserted values into the
	 * AlphaCardDescriptor instance that is eventually returned.
	 * 
	 * @param alphaCardDescriptor
	 *            the alpha card descriptor
	 */
	public void fillAlphaCard(final AlphaCardDescriptor alphaCardDescriptor) {

		//if called from alpha-PrintPut
		String silent = System.getProperty("silent");
		if (silent != null) {
			fillAlphaCardWithSystemProperties(alphaCardDescriptor);		
		} else {
			
			//preallocate the values in the dialog
			String alphaCardTitle = System.getProperty("alphaCardTitle");
			if (alphaCardTitle == null)
				alphaCardTitle = alphaCardDescriptor.readAdornment(CorpusGenericus.TITLE.value()).getValue();
			
			String actorID = System.getProperty("actorID");
			if (actorID == null)
				actorID = this.config.getLocalNodeID().getContributor().getActor();
			
			String roleID = System.getProperty("roleID");
			if (roleID == null)
				roleID = this.config.getLocalNodeID().getContributor().getRole();
			
			String institutionID = System.getProperty("institutionID");
			if (institutionID == null)
				institutionID = this.config.getLocalNodeID().getContributor().getInstitution();
		
			String alphaCardType = System.getProperty("alphaCardType");
								
			
			
			final JFrame frame = new JFrame();
			final JPanel basic = this.visualize();

			if (alphaCardType != null)
			{					
				if (alphaCardType.equals("RESULTS_REPORT"))				
					this.typeT.setSelectedItem(AlphaCardType.RESULTS_REPORT);			
				if (alphaCardType.equals("DOCUMENTATION"))			
					this.typeT.setSelectedItem(AlphaCardType.DOCUMENTATION);
				if (alphaCardType.equals("REFERRAL_VOUCHER"))				
					this.typeT.setSelectedItem(AlphaCardType.REFERRAL_VOUCHER);
			}
			
			this.titleT.setText(alphaCardTitle);			
			this.institutionT.setText(institutionID);
			this.actorT.setText(actorID);
			this.roleT.setText(roleID);			
			
			
			final int ret = JOptionPane.showConfirmDialog(frame, basic,
					"AlphaCard Information", JOptionPane.DEFAULT_OPTION);
			if (ret == JOptionPane.OK_OPTION) {
				/* set values based on user input */
				alphaCardDescriptor
						.readAdornment(CorpusGenericus.TITLE.value()).setValue(
								this.titleT.getText());
				alphaCardDescriptor
						.readAdornment(CorpusGenericus.ACTOR.value()).setValue(
								"=" + this.actorT.getText());
				alphaCardDescriptor.readAdornment(CorpusGenericus.ROLE.value())
						.setValue("+" + this.roleT.getText());
				alphaCardDescriptor.readAdornment(
						CorpusGenericus.INSTITUTION.value()).setValue(
						"@" + this.institutionT.getText());

				alphaCardDescriptor.readAdornment(
						CorpusGenericus.ALPHACARDTYPE.value()).setValue(
						((AlphaCardType) this.typeT.getSelectedItem()).value());

				/* set other values */
				alphaCardDescriptor.setId(new AlphaCardID(this.episodeID,
						UIDGenerator.generateUID()));
				alphaCardDescriptor.readAdornment(
						CorpusGenericus.VERSION.value()).setValue("1.0");
				alphaCardDescriptor.readAdornment(
						CorpusGenericus.VERSIONCONTROL.value()).setValue(
						VersionControl.VERSIONED.value());
			}
		}
	}

	/**
	 * This method builds the JPanel, that is displayed in the GUI.
	 * 
	 * @return the j panel
	 */
	private JPanel visualize() {
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		final JPanel basic = new JPanel(new GridBagLayout());
		basic.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel adornmentName = new JLabel(CorpusGenericus.TITLE.value());
		gbc.gridx = 0;
		gbc.gridy = 0;
		basic.add(adornmentName, gbc);

		this.titleT = new JTextField();
		gbc.gridx++;
		basic.add(this.titleT, gbc);

		adornmentName = new JLabel(CorpusGenericus.INSTITUTION.value());
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(adornmentName, gbc);

		this.institutionT = new JTextField();
		gbc.gridx++;
		basic.add(this.institutionT, gbc);

		adornmentName = new JLabel(CorpusGenericus.ROLE.value());
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(adornmentName, gbc);

		this.roleT = new JTextField();
		gbc.gridx++;
		basic.add(this.roleT, gbc);

		adornmentName = new JLabel(CorpusGenericus.ACTOR.value());
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(adornmentName, gbc);

		this.actorT = new JTextField();
		gbc.gridx++;
		basic.add(this.actorT, gbc);

		adornmentName = new JLabel(CorpusGenericus.ALPHACARDTYPE.value());
		gbc.gridx = 0;
		gbc.gridy++;
		basic.add(adornmentName, gbc);

		this.typeT = new JComboBox(AlphaCardType.values());
		gbc.gridx++;
		basic.add(this.typeT, gbc);

		return basic;
	}

}
