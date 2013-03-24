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
package alpha.editor.listeners;

import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import alpha.doyen.AlphaDoyenUtility;
import alpha.editor.AdornmentChecker;
import alpha.editor.EditorData;
import alpha.editor.adornmentVisualisation.AdornmentVisualisation;
import alpha.editor.adornmentVisualisation.VisualisationState;
import alpha.facades.AlphaPropsFacade;
import alpha.facades.exceptions.IllegalChangeException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.ContributorID;
import alpha.model.cra.EndpointID;
import alpha.model.cra.Participant;
import alpha.model.cra.Token;
import alpha.model.identification.AlphaCardID;
import alpha.util.XmlBinder;

/**
 * This class enables the user to change adornments through a GUI that is
 * displayed after clicking certain adornment values in the extended
 * visualization.
 * 
 * @see ChangeEvent
 */
@Scope("prototype")
@Component
public class ChangeListener implements MouseListener {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(ChangeListener.class.getName());

	/** The apf. */
	@Autowired
	private AlphaPropsFacade apf;

	/** The ed. */
	@Autowired
	private final EditorData ed = null;

	/** The name. */
	private final String name;

	/** The aci. */
	private AlphaCardID aci = null;

	/** The ad checker. */
	@Autowired
	private final AdornmentChecker adChecker = null;

	/**
	 * Instantiates a new change listener.
	 * 
	 * @param value
	 *            the value
	 */
	public ChangeListener(final String value) {
		this.name = value;
	}

	/**
	 * Instantiates a new change listener.
	 * 
	 * @param value
	 *            the value
	 * @param aci
	 *            the aci
	 */
	public ChangeListener(final String value, final AlphaCardID aci) {
		this.name = value;
		this.aci = aci;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(final MouseEvent e) {
		if (this.name.equals("port")) {
			final String port = JOptionPane.showInputDialog(new JFrame(),
					"Please insert new Port (23000 - 23999): ", "Change port",
					JOptionPane.PLAIN_MESSAGE);
			if ((port == null) || (Integer.parseInt(port) < 23000)
					|| (Integer.parseInt(port) > 23999))
				return;
			final CRAPayload cra = this.apf.getCRA();
			final Set<Participant> lop = cra.getListOfParticipants();
			Participant oldP = null;
			Participant newP = null;
			for (final Participant p : lop) {
				if (p.getContributor()
						.getActor()
						.equals(this.ed.getConfig().getLocalNodeID()
								.getContributor().getActor())
						&& p.getNode()
								.getPort()
								.equals(this.ed.getConfig().getLocalNodeID()
										.getNode().getPort())
						&& p.getNode()
								.getIp()
								.equals(this.ed.getConfig().getLocalNodeID()
										.getNode().getIp())) {
					oldP = p;
				}
			}
			if (oldP != null) {
				newP = AlphaDoyenUtility.getNormalParticipant();
				final ContributorID sub = new ContributorID(oldP
						.getContributor().getInstitution(), oldP
						.getContributor().getRole(), oldP.getContributor()
						.getActor());
				final EndpointID node = new EndpointID(
						oldP.getNode().getHost(), oldP.getNode().getIp(), port,
						oldP.getNode().getEmailAddress());
				node.setEmailAddress(oldP.getNode().getEmailAddress());
				newP.setNode(node);
				newP.setContributor(sub);
				for (Token token : oldP.readTokens()) {
					newP.updateOrCreateToken(token);
				}
				try {
					this.apf.updateParticipantPort(oldP, newP);
				} catch (final Exception e1) {
					ChangeListener.LOGGER.severe("Error: " + e1);
				}
				this.ed.getConfig().getLocalNodeID().getNode().setPort(port);
				final XmlBinder xb = new XmlBinder();
				try {
					xb.store(this.ed.getConfig(), this.ed.getHomePath()
							+ "/alphaconfig.xml", "alpha.model.docconfig");
				} catch (final IOException ioe) {
					ChangeListener.LOGGER.severe("IOError:" + ioe);
				}
			}
		} else { /* name specifies adornment to change */
			/*
			 * the changeable check cannot be done in AlphaPanel's createBox,
			 * because this method is invoked from Editor's update() method -->
			 * the update() method is part of the update functionality of
			 * Drools, and so the current changes are not yet performed
			 */
			if (this.adChecker.isAdornmentChangeable(this.aci, this.name)) {
				try {
					final AlphaCardDescriptor ac = this.apf
							.getAlphaCard(this.aci);
					final AdornmentVisualisation adornmentVisualisation = new AdornmentVisualisation(
							ac.readAdornment(this.name),
							VisualisationState.AVAILABLE);

					final int retVal = JOptionPane.showConfirmDialog(
							new JFrame(),
							adornmentVisualisation.getValueVisualisation(),
							"Edit " + this.name, JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					try {
						if (retVal == JOptionPane.OK_OPTION) {
							final Map<String, String> adornment = new HashMap<String, String>();
							adornment.put(adornmentVisualisation.getName(),
									adornmentVisualisation.getValue());
							this.apf.updateAdornmentInstances(this.aci,
									adornment);
							this.ed.getAdornmentsInstancesPanel().update();

						}
					} catch (final IllegalChangeException e1) {
						JOptionPane.showMessageDialog(new JFrame(),
								"Change adornment failed");
						ChangeListener.LOGGER
								.severe("Change adornment failed: " + e1);
					}
				} catch (final HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (final Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(final MouseEvent e) {
		/* not yet needed here */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(final MouseEvent e) {
		/* not yet needed here */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(final MouseEvent e) {
		/* not yet needed here */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(final MouseEvent e) {
		/* not yet needed here */
	}

}
