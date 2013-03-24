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
package alpha.editor.handlers;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import alpha.editor.EditorData;
import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.Validity;
import alpha.model.apa.Visibility;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.ContributorID;
import alpha.model.cra.NodeID;
import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.util.UIDGenerator;

/**
 * This class enables a user to drop files on a GUI. After being dropped the
 * file is inserted into the current AlphaDoc. Either as the Payload for a
 * existing AlphaCardDescriptor or for a newly created one.
 * 
 * 
 */
@Scope("prototype")
@Component
public class FileDropHandler extends TransferHandler {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(FileDropHandler.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5499078779446550021L;

	/** The apf. */
	@Autowired
	private AlphaPropsFacade apf;

	/** The editor data. */
	@Autowired
	private EditorData editorData;

	/** The aci. */
	private AlphaCardID aci;

	/** The title t. */
	private JTextField titleT;

	/** The actor t. */
	private JTextField actorT;

	/** The role t. */
	private JTextField roleT;

	/** The institution t. */
	private JTextField institutionT;

	/** The visible b. */
	private JComboBox visibleB;

	/** The valid b. */
	private JComboBox validB;

	/** The variant t. */
	private JTextField variantT;

	/**
	 * This method returns true if a file is dragged over this TransferHandler
	 * and false in any other case.
	 * 
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @return true, if successful
	 */
	@Override
	public boolean canImport(final JComponent arg0, final DataFlavor[] arg1) {
		for (final DataFlavor flavor : arg1) {
			if (flavor.equals(DataFlavor.javaFileListFlavor))
				return true;
		}
		return false;
	}

	/**
	 * This method takes the file and adds it as a Payload to the AlphaDoc.
	 * 
	 * @param comp
	 *            the comp
	 * @param t
	 *            the t
	 * @return true, if successful
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean importData(final JComponent comp, final Transferable t) {
		final DataFlavor[] flavors = t.getTransferDataFlavors();
		AlphaCardID internalAci;
		for (final DataFlavor flavor : flavors) {
			try {
				if (flavor.equals(DataFlavor.javaFileListFlavor)) {
					final List<File> l = (List<File>) t
							.getTransferData(DataFlavor.javaFileListFlavor);
					final Iterator<File> iter = l.iterator();
					while (iter.hasNext()) {
						final File file = iter.next();
						HashMap<String, String> adornmentChangeRequests = new HashMap<String, String>();
						HashMap<String, String> adornmentValues;

						/*
						 * A new AlphaCardDescriptor is created if this is the
						 * default handler
						 */
						if (this.aci == null) {
							final AlphaCardDescriptor ac = this.apf
									.createNewAlphaCardDescriptor();

							/* create a new AlphaCardID */
							internalAci = new AlphaCardID(this.apf
									.getAlphaDoc().getEpisodeID(),
									UIDGenerator.generateUID());
							ac.setId(internalAci);

							/*
							 * set the AlphaCard owner to the current editor
							 * user
							 */
							// UserNode userNode =
							// editorData.getConfig().getMyCurrentUser();
							final NodeID userNode = this.editorData
									.getConfig().getLocalNodeID();
							final CRAPayload cra = this.apf.getCRA();
							final Set<Participant> part = cra
									.getListOfParticipants();
							for (final Participant participantNode : part) {
								final ContributorID contributor = participantNode
										.getContributor();
								final String name = contributor.getActor();
								final String port = participantNode.getNode()
										.getPort();
								final String ip = participantNode.getNode()
										.getIp();
								if (name.equals(userNode.getContributor()
										.getActor())
										&& port.equals(userNode.getNode()
												.getPort())
										&& ip.equals(userNode.getNode().getIp())) {
									ac.readAdornment(
											CorpusGenericus.ACTOR.value())
											.setValue(
													"="
															+ contributor
																	.getActor());
									ac.readAdornment(
											CorpusGenericus.ROLE.value())
											.setValue(
													"+" + contributor.getRole());
									ac.readAdornment(
											CorpusGenericus.INSTITUTION.value())
											.setValue(
													"@"
															+ contributor
																	.getInstitution());
								}
							}
							/*
							 * ask user to set new Adornment values before
							 * adding the AlphaCard
							 */
							adornmentValues = this.changeAdornmentGUI(ac);
							if (adornmentValues != null) {
								for (final Map.Entry<String, String> e : adornmentValues
										.entrySet()) {
									ac.readAdornment(e.getKey()).setValue(
											e.getValue());
								}
							}
							this.apf.addAlphaCard(ac);
						} else { /* AlphaCard already exists */
							internalAci = this.aci;
							final AlphaCardDescriptor alphaCard = this.apf
									.getAlphaCard(internalAci);
							final String cardOwner = alphaCard
									.readAdornment(
											CorpusGenericus.ACTOR.value())
									.getValue().substring(1);
							if (!cardOwner.equals(this.editorData.getConfig()
									.getLocalNodeID().getContributor()
									.getActor())) {
								JOptionPane
										.showMessageDialog(new JFrame(),
												"You are not allowed to add a payload to this AlphaCard");
								return true;
							}
							/*
							 * ask user to set new Adornment values
							 */
							adornmentValues = this
									.changeAdornmentGUI(alphaCard);
							if (adornmentValues != null) {
								adornmentChangeRequests = adornmentValues;
							}
						}

						/* change the SyntacticPayloadType */
						final int last = file.getAbsolutePath()
								.lastIndexOf(".");
						final String type = file.getAbsolutePath().substring(
								last + 1);
						adornmentChangeRequests.put(
								CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
								type);

						/* read file to create payload */
						final long fsize = file.length();
						final byte[] fdump = new byte[(int) fsize];

						FileInputStream fr;
						fr = new FileInputStream(file);
						fr.read(fdump);
						fr.close();

						final Payload pl = new Payload();
						pl.setContent(fdump);

						/*
						 * Workaround to resolve timing issues with drools:
						 * Allows to modify visibility and validity adornments
						 * upon updating the payload
						 */
						final HashMap<String, String> visiValFix = new HashMap<String, String>(
								adornmentChangeRequests);
						visiValFix.remove(CorpusGenericus.VALIDITY.value());
						visiValFix.remove(CorpusGenericus.VISIBILITY.value());
						this.apf.setPayload(internalAci, pl, visiValFix);
						final AlphaCardDescriptor descriptor = this.apf
								.getAlphaCard(internalAci);
						for (final Map.Entry<String, String> e : adornmentChangeRequests
								.entrySet()) {
							descriptor.readAdornment(e.getKey()).setValue(
									e.getValue());
						}
						this.apf.updateAlphaCardDescriptor(internalAci,
								descriptor);

						/*
						 * Check if the dropped file shall be deleted after
						 * insertion into the AlphaDoc
						 */
						final int result = JOptionPane.showConfirmDialog(
								new JFrame(), "Delete payload source file?",
								"Input", JOptionPane.YES_NO_OPTION,
								JOptionPane.PLAIN_MESSAGE);
						if (result == JOptionPane.YES_OPTION) {
							if (file.delete() == false) {
								FileDropHandler.LOGGER
										.warning("Unable to delete the file "
												+ file);
							}
						}
					}

					this.editorData.getAdornmentsInstancesPanel().update();
					this.editorData.getAdornmentsSchemaPanel().update();
					return true;
				}
			} catch (final IOException ex) {
				FileDropHandler.LOGGER
						.severe("Adding failed - IOError getting data:" + ex);
				JOptionPane.showMessageDialog(new JFrame(),
						"Adding failed - IOError getting data:" + ex);
			} catch (final UnsupportedFlavorException e) {
				FileDropHandler.LOGGER
						.severe("Adding failed - Unsupported Flavor:" + e);
				JOptionPane.showMessageDialog(new JFrame(),
						"Adding failed - Unsupported Flavor:" + e);
			} catch (final Exception e) {
				FileDropHandler.LOGGER.severe("Adding failed: " + e);
				JOptionPane.showMessageDialog(new JFrame(), "Adding failed: "
						+ e.toString());
			}
		}
		JOptionPane.showMessageDialog(new JFrame(), "Adding failed");
		return false;
	}

	/**
	 * This method display a JOptionPane where the user can chose whether or not
	 * to change any adornments.
	 * 
	 * @param card
	 *            the card
	 * @return the int
	 */
	private HashMap<String, String> changeAdornmentGUI(
			final AlphaCardDescriptor card) {
		HashMap<String, String> adornmentValues = null;
		int state = JOptionPane.showConfirmDialog(new JFrame(),
				"Change any Adornments?", "Question",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (state == JOptionPane.YES_OPTION) {
			final JPanel basic = this.createGUI(card);
			state = JOptionPane.showConfirmDialog(new JFrame(), basic,
					"AlphaCardDescriptor values", JOptionPane.DEFAULT_OPTION);
			if (state == JOptionPane.OK_OPTION) {
				adornmentValues = new HashMap<String, String>();
				adornmentValues.put(CorpusGenericus.TITLE.value(),
						this.titleT.getText());
				adornmentValues.put(CorpusGenericus.ACTOR.value(), "="
						+ this.actorT.getText());
				adornmentValues.put(CorpusGenericus.ROLE.value(), "+"
						+ this.roleT.getText());
				adornmentValues.put(CorpusGenericus.INSTITUTION.value(), "@"
						+ this.institutionT.getText());
				adornmentValues.put(CorpusGenericus.VISIBILITY.value(),
						((Visibility) this.visibleB.getSelectedItem()).value());
				adornmentValues.put(CorpusGenericus.VALIDITY.value(),
						((Validity) this.validB.getSelectedItem()).value());

				// Remove entry for adornment variant as Hydra does not maintain
				// any information about variants in form of adornments.
				// adornmentValues.put(CorpusGenericus.VARIANT.value(), variantT
				// .getText());
			}
		}
		return adornmentValues;
	}

	/**
	 * Create a GUI that allows the user to change the title, contributor,
	 * visibility, validity and variant of the card.
	 * 
	 * @param card
	 *            the card
	 * @return the j panel
	 */
	private JPanel createGUI(final AlphaCardDescriptor card) {
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		final JPanel basic = new JPanel(new GridBagLayout());

		JLabel adornmentName = new JLabel(CorpusGenericus.TITLE.value());
		adornmentName.setPreferredSize(new Dimension(120, 20));
		c.gridx = 0;
		c.gridy = 0;
		basic.add(adornmentName, c);

		this.titleT = new JTextField(card.readAdornment(
				CorpusGenericus.TITLE.value()).getValue());
		this.titleT.setPreferredSize(new Dimension(120, 20));
		c.gridx++;
		basic.add(this.titleT, c);

		adornmentName = new JLabel(CorpusGenericus.ACTOR.value());
		c.gridx = 0;
		c.gridy++;
		basic.add(adornmentName, c);

		this.actorT = new JTextField(card
				.readAdornment(CorpusGenericus.ACTOR.value()).getValue()
				.substring(1));
		c.gridx++;
		basic.add(this.actorT, c);

		adornmentName = new JLabel(CorpusGenericus.ROLE.value());
		c.gridx = 0;
		c.gridy++;
		basic.add(adornmentName, c);

		this.roleT = new JTextField(card
				.readAdornment(CorpusGenericus.ROLE.value()).getValue()
				.substring(1));
		c.gridx++;
		basic.add(this.roleT, c);

		adornmentName = new JLabel(CorpusGenericus.INSTITUTION.value());
		c.gridx = 0;
		c.gridy++;
		basic.add(adornmentName, c);

		this.institutionT = new JTextField(card
				.readAdornment(CorpusGenericus.INSTITUTION.value()).getValue()
				.substring(1));
		c.gridx++;
		basic.add(this.institutionT, c);

		adornmentName = new JLabel(CorpusGenericus.VISIBILITY.value());
		c.gridx = 0;
		c.gridy++;
		basic.add(adornmentName, c);

		this.visibleB = new JComboBox(Visibility.values());
		this.visibleB.setSelectedItem(Visibility.fromValue(card.readAdornment(
				CorpusGenericus.VISIBILITY.value()).getValue()));

		c.gridx++;
		basic.add(this.visibleB, c);

		adornmentName = new JLabel(CorpusGenericus.VALIDITY.value());
		c.gridx = 0;
		c.gridy++;
		basic.add(adornmentName, c);

		this.validB = new JComboBox(Validity.values());
		this.validB.setSelectedItem(Validity.fromValue(card.readAdornment(
				CorpusGenericus.VALIDITY.value()).getValue()));
		c.gridx++;
		basic.add(this.validB, c);

		// Remove entry for adornment variant as Hydra does not maintain any
		// information about variants in form of adornments.
		// adornmentName = new JLabel(CorpusGenericus.VARIANT.value());
		// c.gridx = 0;
		// c.gridy++;
		// basic.add(adornmentName, c);
		//
		// variantT = new JTextField();
		// variantT.setText(card.readAdornment(
		// CorpusGenericus.VARIANT.value()).getValue());
		// c.gridx++;
		// basic.add(variantT, c);

		return basic;
	}

	/**
	 * Sets the aci.
	 * 
	 * @param aci
	 *            the aci to set
	 */
	public void setAci(final AlphaCardID aci) {
		this.aci = aci;
	}
}
