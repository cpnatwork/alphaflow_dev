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
 * $Id: AlphaCardAdornmentSchemaPanel.java 3786 2012-05-04 08:00:25Z uj32uvac $
 *************************************************************************/
package alpha.editor.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alpha.editor.AdornmentChecker;
import alpha.editor.EditorData;
import alpha.editor.Swing_Helper;
import alpha.editor.adornmentVisualisation.AdornmentVisualisation;
import alpha.editor.adornmentVisualisation.VisualisationState;
import alpha.facades.AlphaPropsFacade;
import alpha.facades.exceptions.IllegalChangeException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;

/**
 * This class creates a GUI to manage the Adornment schema of an
 * AlphaCardDescriptor.
 * 
 * 
 */
@Component
public class AlphaCardAdornmentSchemaPanel extends JPanel {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphaCardAdornmentSchemaPanel.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7923288014956927084L;

	/** The editor data. */
	@Autowired
	private final EditorData editorData = null;

	/** The apf. */
	@Autowired
	private final AlphaPropsFacade apf = null;

	/** The adornment checker. */
	@Autowired
	private final AdornmentChecker adornmentChecker = null;

	/** The adornment visus. */
	protected HashMap<String, AdornmentVisualisation> adornmentVisus;

	/** The basic. */
	protected JPanel basic;

	/**
	 * Inits the.
	 */
	public void init() {
		final GridBagLayout gbl = new GridBagLayout();
		this.basic = new JPanel(gbl);
		this.basic.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.add(this.basic, BorderLayout.CENTER);
		this.update();
	}

	/**
	 * Update.
	 */
	public void update() {
		try {
			this.adornmentVisus = new LinkedHashMap<String, AdornmentVisualisation>();

			// get adornments from alphaCardDescriptor
			final AlphaCardDescriptor ac = this.apf
					.getAlphaCard(this.editorData.getCurrent());

			final String user = this.editorData.getConfig()
					.getLocalNodeID().getContributor().getActor();
			final boolean owner = this.adornmentChecker.isAlphaCardOwner(
					this.editorData.getCurrent(), user);
			for (final PrototypedAdornment a : ac.readAdornments()) {
				VisualisationState state;
				if (!owner) {
					state = VisualisationState.UNCHANGEABLE;
				} else {
					if (a.isInstance()) {
						if (a.getConsensusScope().equals(
								ConsensusScope.GENERIC_STD)) {
							state = VisualisationState.UNCHANGEABLE;
						} else {
							state = VisualisationState.AVAILABLE;
						}
					} else {
						state = VisualisationState.UNAVAILABLE;
					}
				}
				this.adornmentVisus.put(a.getName(),
						new AdornmentVisualisation(a, state));
				AlphaCardAdornmentSchemaPanel.LOGGER
						.finer("Added new schema visualisation element for adornment "
								+ a.getName());

				final AdornmentVisualisation adornmentVisualisation = this.adornmentVisus
						.get(a.getName());

				Swing_Helper.setPreferredWidth(
						adornmentVisualisation.getNameVisualisation(), 150);
				adornmentVisualisation.getNameVisualisation().setEnabled(false);
				Swing_Helper.setPreferredWidth(
						adornmentVisualisation.getDataTypeVisualisation(), 150);
				adornmentVisualisation.getDataTypeVisualisation().setEnabled(
						false);
			}
			this.displayAdornments();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Display adornments.
	 */
	private void displayAdornments() {
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(1, 1, 1, 1);
		this.basic.removeAll();

		final JLabel schemaView = new JLabel("Schema View");
		schemaView.setFont(new Font("", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.basic.add(schemaView, gbc);

		final JButton showInstances = new JButton("Goto: Instance View");
		showInstances.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				AlphaCardAdornmentSchemaPanel.this.editorData
						.getAdornmentsInstancesPanel().setVisible(true);
				AlphaCardAdornmentSchemaPanel.this.editorData
						.getAdornmentsSchemaPanel().setVisible(false);
			}
		});
		gbc.gridx++;
		gbc.gridwidth = 2;
		this.basic.add(showInstances, gbc);
		gbc.gridwidth = 1;

		String oldCategory = "";
		for (final AdornmentVisualisation av : this.adornmentVisus.values()) {
			if (!av.getCategoryVisualisation().getSelectedItem().toString()
					.equals(oldCategory)) {
				oldCategory = av.getCategoryVisualisation().getSelectedItem()
						.toString();
				// draw a separator label for this ConsensusScope
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.gridwidth = 3;
				this.basic.add(Swing_Helper.createTitledSeparator(oldCategory),
						gbc);
				gbc.gridwidth = 1;
			}
			JComponent stateComponent = new JLabel("");
			switch (av.getState()) {
			case UNCHANGEABLE:
			case DELETE:
			case ADD:
				stateComponent = new JLabel(av.getState().getIcon());
				stateComponent.setToolTipText(av.getState().getDescription());
				break;
			case AVAILABLE:
				stateComponent = new JButton(
						VisualisationState.DELETE.getIcon());
				stateComponent.setName(av.getName());
				stateComponent.setToolTipText(VisualisationState.DELETE
						.getDescription());
				((JButton) stateComponent)
						.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(final ActionEvent ae) {
								final JButton source = (JButton) ae.getSource();
								AlphaCardAdornmentSchemaPanel.LOGGER
										.finer("Source of JButton is "
												+ source.getName());
								final AdornmentVisualisation av = AlphaCardAdornmentSchemaPanel.this.adornmentVisus
										.get(source.getName());
								av.setState(VisualisationState.DELETE);
								AlphaCardAdornmentSchemaPanel.this
										.displayAdornments();
							}
						});
				break;
			case UNAVAILABLE:
				stateComponent = new JButton(VisualisationState.ADD.getIcon());
				stateComponent.setName(av.getName());
				stateComponent.setToolTipText(VisualisationState.ADD
						.getDescription());
				((JButton) stateComponent)
						.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(final ActionEvent ae) {
								final JButton source = (JButton) ae.getSource();
								AlphaCardAdornmentSchemaPanel.LOGGER
										.finer("Source of JButton is "
												+ source.getName());
								final AdornmentVisualisation av = AlphaCardAdornmentSchemaPanel.this.adornmentVisus
										.get(source.getName());
								av.setState(VisualisationState.ADD);
								AlphaCardAdornmentSchemaPanel.this
										.displayAdornments();
							}
						});
				break;
			}
			stateComponent.setPreferredSize(new Dimension(25, 20));
			gbc.gridx = 0;
			gbc.gridy++;
			this.basic.add(av.getNameVisualisation(), gbc);
			gbc.gridx++;
			this.basic.add(av.getDataTypeVisualisation(), gbc);
			gbc.gridx++;
			this.basic.add(stateComponent, gbc);
		}

		final JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Map<String, Boolean> updatedAdornments = new HashMap<String, Boolean>();
				for (final AdornmentVisualisation av : AlphaCardAdornmentSchemaPanel.this.adornmentVisus
						.values()) {
					if (av.getState().equals(VisualisationState.ADD)) {
						updatedAdornments.put(av.getName(), true);
					} else if (av.getState().equals(VisualisationState.DELETE)) {
						updatedAdornments.put(av.getName(), false);
					}
				}
				try {
					AlphaCardAdornmentSchemaPanel.this.apf
							.updateAdornmentSchema(
									AlphaCardAdornmentSchemaPanel.this.editorData
											.getCurrent(), updatedAdornments);
				} catch (final IllegalChangeException ice) {
					final String message = "Saving changed Adornment schema failed: "
							+ ice;
					JOptionPane.showMessageDialog(new JFrame(), message);
					AlphaCardAdornmentSchemaPanel.LOGGER.info(message);
				}
			}
		});
		gbc.gridx = 0;
		gbc.gridy++;
		this.basic.add(save, gbc);

		final JButton discard = new JButton("Discard");
		discard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				AlphaCardAdornmentSchemaPanel.this.update();
			}
		});
		gbc.gridx++;
		this.basic.add(discard, gbc);
	}

}
