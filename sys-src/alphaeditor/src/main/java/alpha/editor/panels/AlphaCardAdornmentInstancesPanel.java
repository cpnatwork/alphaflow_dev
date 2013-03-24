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
 * $Id: AlphaCardAdornmentInstancesPanel.java 3986 2012-09-12 15:34:20Z uj32uvac $
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
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.CorpusGenericus;

/**
 * This class creates a GUI to manage the Adornment instances of an
 * AlphaCardDescriptor.
 * 
 */
@Component
public class AlphaCardAdornmentInstancesPanel extends JPanel {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphaCardAdornmentInstancesPanel.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7923288014956927084L;

	/** The editor data. */
	@Autowired
	private final EditorData editorData = null;

	/** The adornment checker. */
	@Autowired
	private final AdornmentChecker adornmentChecker = null;

	/** The apf. */
	@Autowired
	private final AlphaPropsFacade apf = null;

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

			final String user = this.editorData.getConfig().getLocalNodeID()
					.getContributor().getActor();
			final boolean owner = this.adornmentChecker.isAlphaCardOwner(
					this.editorData.getCurrent(), user);
			/* set state of adaptive adornments dependend on 'changeability' */
			final Map<String, Boolean> adaptiveChangeability = this.apf
					.getAlphaCardChangeability(this.editorData.getCurrent());
			AlphaCardAdornmentInstancesPanel.LOGGER
					.finer("Changeability map is: "
							+ adaptiveChangeability.toString());
			for (final PrototypedAdornment aa : ac.readAdornments()) {
				if (aa.isInstance()) {
					this.adornmentVisus.put(aa.getName(),
							new AdornmentVisualisation(aa,
									VisualisationState.AVAILABLE));
					AlphaCardAdornmentInstancesPanel.LOGGER
							.finer("Added new instance visualisation element for adornment "
									+ aa);

					final AdornmentVisualisation adornmentVisualisation = this.adornmentVisus
							.get(aa.getName());
					if (!owner) {
						adornmentVisualisation
								.setState(VisualisationState.UNCHANGEABLE);
						// final NotifyUserThread nt = new NotifyUserThread(
						// this.editorData.getNotify(),
						// "You are not the owner of the selected AlphaCard.");
						// nt.start();
					} else {
						final Boolean change = adaptiveChangeability.get(aa
								.getName());
						AlphaCardAdornmentInstancesPanel.LOGGER
								.finer("Adornment changeability entry for '"
										+ aa.getName() + "' is " + change);
						if (!change) {
							adornmentVisualisation
									.setState(VisualisationState.UNCHANGEABLE);
						}
					}

					Swing_Helper.setPreferredWidth(
							adornmentVisualisation.getNameVisualisation(), 150);
					Swing_Helper
							.setPreferredWidth(adornmentVisualisation
									.getValueVisualisation(), 150);
				}
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

		final JLabel instanceView = new JLabel("Instance View");
		instanceView.setFont(new Font("", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.basic.add(instanceView, gbc);

		final JButton showSchema = new JButton("Goto: Schema View");
		showSchema.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				AlphaCardAdornmentInstancesPanel.this.editorData
						.getAdornmentsInstancesPanel().setVisible(false);
				AlphaCardAdornmentInstancesPanel.this.editorData
						.getAdornmentsSchemaPanel().setVisible(true);
			}
		});
		gbc.gridx++;
		gbc.gridwidth = 2;
		this.basic.add(showSchema, gbc);
		gbc.gridwidth = 1;

		String oldCategory = "";
		for (final AdornmentVisualisation adornmentVisualisation : this.adornmentVisus
				.values()) {
			if (!adornmentVisualisation.getCategoryVisualisation()
					.getSelectedItem().toString().equals(oldCategory)) {
				oldCategory = adornmentVisualisation.getCategoryVisualisation()
						.getSelectedItem().toString();
				// draw a separator label for this ConsensusScope
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.gridwidth = 3;
				this.basic.add(Swing_Helper.createTitledSeparator(oldCategory),
						gbc);
				gbc.gridwidth = 1;
			}

			if (adornmentVisualisation.getState().equals(
					VisualisationState.UPDATE)) {
				adornmentVisualisation.enableValueVisualisation(true);
				AlphaCardAdornmentInstancesPanel.LOGGER
						.finer("Enable ValueVisu of Adornment "
								+ adornmentVisualisation
										.getValueVisualisation().getName());
				this.editorData.getBasic().revalidate();
				this.editorData.getBasic().updateUI();

			} else {
				adornmentVisualisation.enableValueVisualisation(false);
			}

			JComponent stateComponent = new JLabel("");
			switch (adornmentVisualisation.getState()) {
			case UNCHANGEABLE:
			case UPDATE:
				stateComponent = new JLabel(adornmentVisualisation.getState()
						.getIcon());
				stateComponent.setToolTipText(adornmentVisualisation.getState()
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
				stateComponent.setName(adornmentVisualisation.getName());
				stateComponent.setToolTipText(VisualisationState.UPDATE
						.getDescription());
				((JButton) stateComponent)
						.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(final ActionEvent e) {
								final JComponent source = (JComponent) e
										.getSource();
								final AdornmentVisualisation iv = AlphaCardAdornmentInstancesPanel.this.adornmentVisus
										.get(source.getName());
								iv.setState(VisualisationState.UPDATE);
								AlphaCardAdornmentInstancesPanel.this
										.displayAdornments();
							}
						});
				break;
			}
			stateComponent.setPreferredSize(new Dimension(25, 20));
			gbc.gridx = 0;
			gbc.gridy++;
			adornmentVisualisation.getNameVisualisation().setEnabled(false);
			this.basic.add(adornmentVisualisation.getNameVisualisation(), gbc);
			gbc.gridx++;
			this.basic.add(adornmentVisualisation.getValueVisualisation(), gbc);
			gbc.gridx++;
			this.basic.add(stateComponent, gbc);
		}

		final JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Map<String, String> updatedAdornments = new HashMap<String, String>();
				for (final AdornmentVisualisation iv : AlphaCardAdornmentInstancesPanel.this.adornmentVisus
						.values()) {
					if (iv.getState().equals(VisualisationState.UPDATE)) {

						/* replenish the XRI/XDI characters to value */
						String value = iv.getValue();
						if (iv.getName().equals(CorpusGenericus.ACTOR.value())) {
							value = "=" + iv.getValue();
						}
						if (iv.getName().equals(CorpusGenericus.ROLE.value())) {
							value = "+" + iv.getValue();
						}
						if (iv.getName().equals(
								CorpusGenericus.INSTITUTION.value())) {
							value = "@" + iv.getValue();
						}

						/*
						 * first fill map with changes
						 */
						updatedAdornments.put(iv.getName(), value);

					}
				}

				try {
					/*
					 * then generate event
					 */
					AlphaCardAdornmentInstancesPanel.this.apf
							.updateAdornmentInstances(
									AlphaCardAdornmentInstancesPanel.this.editorData
											.getCurrent(), updatedAdornments);
				} catch (final IllegalChangeException ice) {
					final String message = "Saving changed Adornment values failed: "
							+ ice;
					JOptionPane.showMessageDialog(new JFrame(), message);
					AlphaCardAdornmentInstancesPanel.LOGGER.info(message);
				}

				AlphaCardAdornmentInstancesPanel.this.update();
			}
		});
		gbc.gridx = 0;
		gbc.gridy++;
		this.basic.add(save, gbc);

		final JButton discard = new JButton("Discard");
		discard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				AlphaCardAdornmentInstancesPanel.this.update();
			}
		});
		gbc.gridx++;
		this.basic.add(discard, gbc);

		this.basic.revalidate();
	}

}
