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
 * $Id: ContentCardAdornmentPrototypePanel.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.editor.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alpha.editor.EditorData;
import alpha.editor.Swing_Helper;
import alpha.editor.adornmentVisualisation.AdornmentVisualisation;
import alpha.editor.adornmentVisualisation.RangeItemVisu;
import alpha.editor.adornmentVisualisation.VisualisationState;
import alpha.facades.AlphaPropsFacade;
import alpha.facades.exceptions.IllegalChangeException;
import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.AdornmentEnumRange;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;
import alpha.util.RNG;

import com.toedter.calendar.JDateChooser;

/**
 * This class creates a GUI to manage the adornment prototype of the AlphaDoc.
 * 
 * 
 */
@Component
public class ContentCardAdornmentPrototypePanel extends JPanel {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(ContentCardAdornmentPrototypePanel.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2706786046987710299L;

	/** The editor data. */
	@Autowired
	private EditorData editorData;

	/** The apf. */
	@Autowired
	private final AlphaPropsFacade apf = null;

	/** The adornment visus. */
	private LinkedHashMap<String, AdornmentVisualisation> adornmentVisus;

	/** The basic. */
	private JPanel basic;

	/** The show generic adornments. */
	private boolean showGenericAdornments = false;

	/**
	 * Display.
	 */
	public void display() {
		final JPanel container = new JPanel(new BorderLayout());
		final JCheckBox genericAdornmentBox = new JCheckBox(
				"Show Generic Adornments");
		genericAdornmentBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				final JCheckBox source = (JCheckBox) e.getSource();
				ContentCardAdornmentPrototypePanel.this.showGenericAdornments = source
						.isSelected();
				ContentCardAdornmentPrototypePanel.this.displayAdornments();
			}
		});
		container.add(genericAdornmentBox, BorderLayout.NORTH);

		this.basic = new JPanel(new GridBagLayout());
		/*
		 * put the basic panel in another panel to receive vertical top
		 * alignment of basic
		 */
		final JPanel basicPanelWrapper = new JPanel(new BorderLayout());
		basicPanelWrapper.add(this.basic, BorderLayout.NORTH);
		container.add(basicPanelWrapper, BorderLayout.CENTER);
		this.basic.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(1, 1, 1, 1);
		final JPanel buttons = new JPanel(new GridBagLayout());

		final JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Map<String, PrototypedAdornment> updatedAdornments = new HashMap<String, PrototypedAdornment>();
				final Map<String, String> updatedValues = new HashMap<String, String>();
				/* check the states of the (non-generic) Adornments */
				for (final AdornmentVisualisation av : ContentCardAdornmentPrototypePanel.this.adornmentVisus
						.values()) {
					switch (av.getState()) {
					case DELETE:
						updatedAdornments.put(av.getName(), null);
						break;
					case ADD:
					case UPDATE:
						updatedAdornments.put(av.getName(), av.getAdornment());
						updatedValues.put(av.getName(), av.getValue());
						break;
					}
				}
				try {
					ContentCardAdornmentPrototypePanel.this.apf.updateAPA(
							updatedAdornments, updatedValues);
				} catch (final IllegalChangeException ice) {
					final String message = "Saving changed APA failed: " + ice;
					JOptionPane.showMessageDialog(new JFrame(), message);
					ContentCardAdornmentPrototypePanel.LOGGER.info(message);
				}

				ContentCardAdornmentPrototypePanel.this.editorData
						.getAdornmentsInstancesPanel().update();
				ContentCardAdornmentPrototypePanel.this.editorData
						.getAdornmentsSchemaPanel().update();
				ContentCardAdornmentPrototypePanel.this.initAdornmentVisu();
				ContentCardAdornmentPrototypePanel.this.displayAdornments();
			}
		});
		save.setPreferredSize(new Dimension(160, 25));
		buttons.add(save, gbc);

		final JButton discard = new JButton("Discard");
		discard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				ContentCardAdornmentPrototypePanel.this.initAdornmentVisu();
				ContentCardAdornmentPrototypePanel.this.displayAdornments();
			}
		});
		discard.setPreferredSize(new Dimension(160, 25));
		buttons.add(discard, gbc);

		final JButton addAdornment = new JButton("Add Adornment",
				VisualisationState.ADD.getIcon());
		addAdornment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final String s = "Adornment"
						+ String.valueOf(new RNG().generate());
				final PrototypedAdornment newAdornment = new PrototypedAdornment(
						s, ConsensusScope.DOMAIN_STD, AdornmentDataType.INTEGER);
				ContentCardAdornmentPrototypePanel.this
						.createAdornmentVisu(newAdornment);
				ContentCardAdornmentPrototypePanel.this.updateAdornmentState(
						ContentCardAdornmentPrototypePanel.this.adornmentVisus
								.get(s), VisualisationState.ADD);
				ContentCardAdornmentPrototypePanel.this.displayAdornments();
			}
		});
		addAdornment.setPreferredSize(new Dimension(160, 25));
		buttons.add(addAdornment, gbc);

		final JButton importButton = new JButton("Import");
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JOptionPane.showMessageDialog(new JFrame(), "TODO import APA");
			}
		});
		importButton.setPreferredSize(new Dimension(160, 25));
		buttons.add(importButton, gbc);

		final JButton export = new JButton("Export");
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JOptionPane.showMessageDialog(new JFrame(), "TODO export APA");
			}
		});
		export.setPreferredSize(new Dimension(160, 25));
		buttons.add(export, gbc);

		// TODO remove after testing
		final JButton test = new JButton("Create some Adornments");
		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final ArrayList<PrototypedAdornment> tempAdornments = new ArrayList<PrototypedAdornment>();

				PrototypedAdornment aa = new PrototypedAdornment(
						"CONDITION_INDICATOR", ConsensusScope.EPISODE_STD,
						AdornmentDataType.ENUM);
				aa.setEnumRange(new AdornmentEnumRange(new String[] { "NORMAL",
						"GUARDED", "SERIOUS", "CRITICAL" }));
				aa.setValue("GUARDED");
				tempAdornments.add(aa);
				aa = new PrototypedAdornment("DIAGNOSIS_CERTAINTY",
						ConsensusScope.EPISODE_STD, AdornmentDataType.ENUM);
				aa.setEnumRange(new AdornmentEnumRange(new String[] {
						"ABSOLUTE", "HIGH", "MODERATE", "LOW" }));
				aa.setValue("MODERATE");
				tempAdornments.add(aa);
				aa = new PrototypedAdornment("Informational_Notes",
						ConsensusScope.DOMAIN_STD, AdornmentDataType.TEXTBLOCK);
				/* 100 words: */
				aa.setValue("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
				tempAdornments.add(aa);
				aa = new PrototypedAdornment("URGENCY",
						ConsensusScope.EPISODE_STD, AdornmentDataType.ENUM);
				aa.setEnumRange(new AdornmentEnumRange(new String[] {
						"ROUTINE", "ASAP", "STAT" }));
				aa.setValue("ROUTINE");
				tempAdornments.add(aa);

				for (final PrototypedAdornment a : tempAdornments) {
					ContentCardAdornmentPrototypePanel.this
							.createAdornmentVisu(a);
					ContentCardAdornmentPrototypePanel.this.adornmentVisus.get(
							a.getName()).setState(VisualisationState.ADD);
				}
				ContentCardAdornmentPrototypePanel.this.displayAdornments();
			}
		});
		buttons.add(test, gbc);

		this.initAdornmentVisu();
		this.displayAdornments();

		container.add(buttons, BorderLayout.SOUTH);
		this.add(container, BorderLayout.WEST);
	}

	/**
	 * Inits the adornment visu.
	 */
	public void initAdornmentVisu() {
		this.adornmentVisus = new LinkedHashMap<String, AdornmentVisualisation>();

		for (final PrototypedAdornment aa : this.apf.getAPA()
				.getContentCardAdornmentPrototype().readAdornments()) {
			this.createAdornmentVisu(aa);
		}
	}

	/**
	 * Creates the adornment visu.
	 * 
	 * @param a
	 *            the a
	 */
	private void createAdornmentVisu(final PrototypedAdornment a) {
		this.adornmentVisus.put(a.getName(), new AdornmentVisualisation(a,
				VisualisationState.AVAILABLE));
		if (a.getConsensusScope().equals(ConsensusScope.GENERIC_STD)) {
			this.updateAdornmentState(this.adornmentVisus.get(a.getName()),
					VisualisationState.UNCHANGEABLE);
		}

		final AdornmentVisualisation adornmentVisualisation = this.adornmentVisus
				.get(a.getName());
		Swing_Helper.setPreferredWidth(
				adornmentVisualisation.getNameVisualisation(), 160);
		Swing_Helper.setPreferredWidth(
				adornmentVisualisation.getCategoryVisualisation(), 120);
		Swing_Helper.setPreferredWidth(
				adornmentVisualisation.getDataTypeVisualisation(), 120);
		this.initValueVisualisation(adornmentVisualisation);

		if (a.getDataType().equals(AdornmentDataType.ENUM)) {
			for (final String s : a.getEnumRange().values()) {
				this.createRangeItemVisu(adornmentVisualisation, s);
			}
			this.initRange(adornmentVisualisation);
		}

		adornmentVisualisation.getNameVisualisation().addKeyListener(
				new KeyListener() {
					@Override
					public void keyTyped(final KeyEvent ke) {
						final JTextField textField = (JTextField) ke
								.getSource();
						final int pos = ContentCardAdornmentPrototypePanel.this
								.updateTextField(textField);
						final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
								.get(textField.getName());
						ContentCardAdornmentPrototypePanel.this
								.updateAdornmentState(av,
										VisualisationState.UPDATE);
						ContentCardAdornmentPrototypePanel.this
								.displayAdornments();
						textField.requestFocusInWindow();
						textField.setCaretPosition(pos);
					}

					@Override
					public void keyReleased(final KeyEvent ke) {
					} // not yet needed here

					@Override
					public void keyPressed(final KeyEvent ke) {
					} // not yet needed here
				});

		adornmentVisualisation.getCategoryVisualisation().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent ae) {
						final JComboBox source = (JComboBox) ae.getSource();
						final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
								.get(source.getName());
						ContentCardAdornmentPrototypePanel.this
								.updateAdornmentState(av,
										VisualisationState.UPDATE);
						ContentCardAdornmentPrototypePanel.this
								.displayAdornments();
					}
				});

		adornmentVisualisation.getDataTypeVisualisation().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent ae) {
						final JComboBox source = (JComboBox) ae.getSource();
						final String sourceName = source.getName();
						final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
								.get(sourceName);
						if (source.getSelectedItem().equals(
								AdornmentDataType.ENUM)) {
							ContentCardAdornmentPrototypePanel.this
									.initRange(av);
						} else {
							av.setRangeVisualisation(null);
							av.setRange(null);
						}
						av.setValueVisualisation();
						ContentCardAdornmentPrototypePanel.this
								.initValueVisualisation(av);
						ContentCardAdornmentPrototypePanel.this
								.updateAdornmentState(av,
										VisualisationState.UPDATE);
						ContentCardAdornmentPrototypePanel.this
								.displayAdornments();
					}
				});

		adornmentVisualisation.getInstantiateVisualisation().addItemListener(
				new ItemListener() {
					@Override
					public void itemStateChanged(final ItemEvent ie) {
						final JCheckBox source = (JCheckBox) ie.getSource();
						final String sourceName = source.getName();
						final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
								.get(sourceName);
						ContentCardAdornmentPrototypePanel.this
								.updateAdornmentState(av,
										VisualisationState.UPDATE);
						ContentCardAdornmentPrototypePanel.this
								.displayAdornments();
					}
				});
	}

	/**
	 * Inits the value visualisation.
	 * 
	 * @param av
	 *            the av
	 */
	private void initValueVisualisation(final AdornmentVisualisation av) {
		final JComponent valueVisu = av.getValueVisualisation();
		Swing_Helper.setPreferredWidth(valueVisu, 160);

		/* set the ActionListener based on the type of the valueVisualisation */
		final AdornmentDataType type = (AdornmentDataType) av
				.getDataTypeVisualisation().getSelectedItem();
		switch (type) {
		case ENUM:
			final JComboBox box = (JComboBox) valueVisu;
			box.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent ae) {
					final JComboBox source = (JComboBox) ae.getSource();
					final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
							.get(source.getName());
					ContentCardAdornmentPrototypePanel.this
							.updateAdornmentState(av, VisualisationState.UPDATE);
					ContentCardAdornmentPrototypePanel.this.displayAdornments();
				}
			});
			break;
		case TEXTBLOCK:
			final JTextArea textArea = (JTextArea) ((JScrollPane) valueVisu)
					.getViewport().getComponent(0);
			textArea.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(final KeyEvent ke) {
					final JTextArea textArea = (JTextArea) ke.getSource();
					final int pos = ContentCardAdornmentPrototypePanel.this
							.updateTextField(textArea);
					final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
							.get(textArea.getName());
					ContentCardAdornmentPrototypePanel.this
							.updateAdornmentState(av, VisualisationState.UPDATE);
					ContentCardAdornmentPrototypePanel.this.displayAdornments();
					textArea.requestFocusInWindow();
					textArea.setCaretPosition(pos);
				}

				@Override
				public void keyReleased(final KeyEvent ke) {
				} // not yet needed here

				@Override
				public void keyPressed(final KeyEvent ke) {
				} // not yet needed here
			});
			break;
		case TIMESTAMP:
			final JDateChooser datePicker = (JDateChooser) valueVisu;
			datePicker.addPropertyChangeListener("date",
					new PropertyChangeListener() {

						@Override
						public void propertyChange(final PropertyChangeEvent pce) {
							final JDateChooser source = (JDateChooser) pce
									.getSource();
							final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
									.get(source.getName());
							ContentCardAdornmentPrototypePanel.this
									.updateAdornmentState(av,
											VisualisationState.UPDATE);
							ContentCardAdornmentPrototypePanel.this
									.displayAdornments();
						}
					});
			break;
		default:
			valueVisu.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(final KeyEvent ke) {
					final JTextField text = (JTextField) ke.getSource();
					final int pos = ContentCardAdornmentPrototypePanel.this
							.updateTextField(text);
					final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
							.get(text.getName());
					ContentCardAdornmentPrototypePanel.this
							.updateAdornmentState(av, VisualisationState.UPDATE);
					ContentCardAdornmentPrototypePanel.this.displayAdornments();
					text.requestFocusInWindow();
					text.setCaretPosition(pos);
				}

				@Override
				public void keyReleased(final KeyEvent ke) {
				} // not yet needed here

				@Override
				public void keyPressed(final KeyEvent ke) {
				} // not yet needed here
			});
		}
	}

	/**
	 * Creates the range item visu.
	 * 
	 * @param av
	 *            the av
	 * @param name
	 *            the name
	 */
	private void createRangeItemVisu(final AdornmentVisualisation av,
			final String name) {

		final JTextField rangeItem = av.getRange().get(name).getItem();
		rangeItem.setPreferredSize(new Dimension(120, 20));
		rangeItem.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(final KeyEvent ke) {
				final JTextField text = (JTextField) ke.getSource();
				final int pos = ContentCardAdornmentPrototypePanel.this
						.updateTextField(text);
				final String sourceName = text.getName();
				final String sourceParent = text.getParent().getName();
				final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
						.get(sourceParent);
				if (av.getRange().get(sourceName).getState()
						.equals(VisualisationState.AVAILABLE)) {
					av.getRange().get(sourceName)
							.setState(VisualisationState.UPDATE);
				}
				ContentCardAdornmentPrototypePanel.this.updateAdornmentState(
						av, VisualisationState.UPDATE);
				ContentCardAdornmentPrototypePanel.this.displayAdornments();
				ContentCardAdornmentPrototypePanel.this
						.displayRange(sourceParent);
				text.requestFocusInWindow();
				text.setCaretPosition(pos);
			}

			@Override
			public void keyReleased(final KeyEvent ke) {
			} // not yet needed here

			@Override
			public void keyPressed(final KeyEvent ke) {
			} // not yet needed here
		});
	}

	/**
	 * Update text field.
	 * 
	 * @param textComponent
	 *            the text component
	 * @return the int
	 */
	private int updateTextField(final JTextComponent textComponent) {
		int pos = textComponent.getCaretPosition();
		if (textComponent.getSelectedText() != null) {
			final int startSelectionPos = textComponent.getSelectionStart();
			final int endSelectionPos = textComponent.getSelectionEnd();
			String text = textComponent.getText();
			text = text.substring(0, startSelectionPos)
					+ text.substring(endSelectionPos, text.length());
			textComponent.setText(text);
			if (pos >= endSelectionPos) {
				pos -= (endSelectionPos - startSelectionPos);
			}
		}
		return pos;
	}

	/**
	 * Inits the range.
	 * 
	 * @param av
	 *            the av
	 */
	private void initRange(final AdornmentVisualisation av) {
		final JPanel p = new JPanel(new GridBagLayout());
		final TitledBorder border = BorderFactory.createTitledBorder(av
				.getName() + "'s Range");
		switch (av.getState()) {
		case UNCHANGEABLE:
		case DELETE:
			p.setEnabled(false);
			break;
		}
		p.setBorder(border);
		p.setName(av.getName());
		av.setRangeVisualisation(p);
		this.displayRange(av.getName());
	}

	/**
	 * Update adornment state.
	 * 
	 * @param av
	 *            the av
	 * @param state
	 *            the state
	 */
	private void updateAdornmentState(final AdornmentVisualisation av,
			final VisualisationState state) {
		boolean changeable;
		if (av.getState().equals(VisualisationState.AVAILABLE)) {
			av.setState(state);
			// disable visualisation elements that are not editable
			switch (av.getState()) {
			case UNCHANGEABLE:
			case DELETE:
				changeable = false;
				break;
			case AVAILABLE:
			case UPDATE:
			case ADD:
			default:
				changeable = true;
				break;
			}
			av.getNameVisualisation().setEnabled(changeable);
			av.getCategoryVisualisation().setEnabled(changeable);
			av.getDataTypeVisualisation().setEnabled(changeable);
			av.getInstantiateVisualisation().setEnabled(changeable);
			av.enableValueVisualisation(changeable);
			if (av.getRangeVisualisation() != null) {
				av.getRangeVisualisation().setEnabled(changeable);
			}
		}
	}

	/**
	 * Display adornments.
	 */
	public void displayAdornments() {

		this.basic.removeAll();
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 1;
		gbc.gridy = 0;
		JLabel header = new JLabel("Inst.");
		header.setPreferredSize(new Dimension(50, 20));
		this.basic.add(header, gbc);
		gbc.gridx++;
		header = new JLabel("Name");
		header.setPreferredSize(new Dimension(160, 20));
		this.basic.add(header, gbc);
		gbc.gridx++;
		header = new JLabel("ScopeIdentifier");
		header.setPreferredSize(new Dimension(120, 20));
		this.basic.add(header, gbc);
		gbc.gridx++;
		header = new JLabel("Data Type");
		header.setPreferredSize(new Dimension(120, 20));
		this.basic.add(header, gbc);
		/* skip the column for the range */
		gbc.gridx += 2;
		header = new JLabel("Default Value");
		header.setPreferredSize(new Dimension(160, 20));
		this.basic.add(header, gbc);

		for (final AdornmentVisualisation av : this.adornmentVisus.values()) {
			/* check, if generic Adornments shall be displayed or not */
			final String category = av.getCategoryVisualisation()
					.getSelectedItem().toString();
			if (!category.equals(ConsensusScope.GENERIC_STD.value())
					|| this.showGenericAdornments) {
				/* display the state via icon */
				JComponent stateComponent = new JLabel("");
				switch (av.getState()) {
				case UNCHANGEABLE:
				case UPDATE:
				case DELETE:
				case ADD:
					stateComponent = new JLabel(av.getState().getIcon());
					stateComponent.setToolTipText(av.getState()
							.getDescription());
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
								public void actionPerformed(final ActionEvent e) {
									final JComponent source = (JComponent) e
											.getSource();
									final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
											.get(source.getName());
									ContentCardAdornmentPrototypePanel.this
											.updateAdornmentState(av,
													VisualisationState.DELETE);
									// adornmentVisus.remove(source.getName());
									ContentCardAdornmentPrototypePanel.this
											.displayRange(source.getName());
									ContentCardAdornmentPrototypePanel.this
											.displayAdornments();
								}
							});
					break;
				}
				stateComponent.setPreferredSize(new Dimension(25, 20));

				gbc.gridx = 0;
				gbc.gridy++;
				this.basic.add(stateComponent, gbc);
				gbc.gridx++;
				this.basic.add(av.getInstantiateVisualisation(), gbc);
				gbc.gridx++;
				this.basic.add(av.getNameVisualisation(), gbc);
				gbc.gridx++;
				this.basic.add(av.getCategoryVisualisation(), gbc);
				gbc.gridx++;
				this.basic.add(av.getDataTypeVisualisation(), gbc);
				gbc.gridx++;
				if (av.getRangeVisualisation() != null) {
					this.basic.add(av.getRangeVisualisation(), gbc);
				}
				gbc.gridx++;
				this.basic.add(av.getValueVisualisation(), gbc);
			}
		}
		this.basic.updateUI();
	}

	/**
	 * Display range.
	 * 
	 * @param name
	 *            the name
	 */
	private void displayRange(final String name) {
		final AdornmentVisualisation av = this.adornmentVisus.get(name);
		if (av.getRangeVisualisation() == null)
			return;
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.gridy = 0;
		final JPanel p = av.getRangeVisualisation();
		if (!p.isEnabled()) {
			((TitledBorder) p.getBorder()).setTitleColor(Color.gray);
		}
		p.removeAll();

		for (final RangeItemVisu rv : av.getRange().values()) {
			JComponent stateComponent = new JLabel("");
			rv.getItem().setEnabled(p.isEnabled());
			if (!p.isEnabled()) {
				stateComponent = new JLabel(av.getState().getIcon());
			} else {
				// display the state via icon
				ContentCardAdornmentPrototypePanel.LOGGER
						.finer("The state of the range item is "
								+ rv.getState());
				switch (rv.getState()) {
				case UNCHANGEABLE:
				case DELETE:
					rv.getItem().setEnabled(false);
					ContentCardAdornmentPrototypePanel.LOGGER
							.finer("The item with name "
									+ rv.getItem().getName()
									+ " is now disabled.");
					/*
					 * no break here, the stateComponent should be set for all 4
					 * cases!
					 */
				case UPDATE:
				case ADD:
					stateComponent = new JLabel(rv.getState().getIcon());
					stateComponent.setToolTipText(rv.getState()
							.getDescription());
					break;
				case AVAILABLE:
					stateComponent = new JButton(
							VisualisationState.DELETE.getIcon());
					stateComponent.setName(rv.getItem().getName());
					stateComponent.setToolTipText(VisualisationState.DELETE
							.getDescription());
					((JButton) stateComponent)
							.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(final ActionEvent ae) {
									final JComponent source = (JComponent) ae
											.getSource();
									final String sourceName = source.getName();
									final String sourceParent = source
											.getParent().getName();
									final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
											.get(sourceParent);
									av.getRange()
											.get(sourceName)
											.setState(VisualisationState.DELETE);
									ContentCardAdornmentPrototypePanel.this
											.updateAdornmentState(av,
													VisualisationState.UPDATE);
									ContentCardAdornmentPrototypePanel.this
											.displayRange(sourceParent);
									av.narrowValueVisualisationRange(sourceName);
								}
							});
					break;
				}
			}
			stateComponent.setPreferredSize(new Dimension(25, 20));
			gbc.gridx = 0;
			p.add(stateComponent, gbc);
			gbc.gridx++;
			p.add(rv.getItem(), gbc);
			gbc.gridy++;
		}

		final JButton addRangeItem = new JButton("Add Item",
				VisualisationState.ADD.getIcon());
		addRangeItem.setName(name);
		addRangeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final String sourceName = ((JComponent) e.getSource())
						.getName();
				final AdornmentVisualisation av = ContentCardAdornmentPrototypePanel.this.adornmentVisus
						.get(sourceName);
				final String itemName = "Item "
						+ String.format("%03d", av.getRange().size() + 1);
				av.getRange().put(itemName,
						new RangeItemVisu(itemName, VisualisationState.ADD));
				ContentCardAdornmentPrototypePanel.this.createRangeItemVisu(av,
						itemName);
				ContentCardAdornmentPrototypePanel.this.updateAdornmentState(
						av, VisualisationState.UPDATE);
				ContentCardAdornmentPrototypePanel.this
						.displayRange(sourceName);
				av.extendValueVisualisationRange(itemName);
			}
		});
		addRangeItem.setVisible(p.isEnabled());
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		addRangeItem.setPreferredSize(new Dimension(150, 20));
		p.add(addRangeItem, gbc);
		gbc.gridwidth = 1;

		this.displayAdornments();
	}

}
