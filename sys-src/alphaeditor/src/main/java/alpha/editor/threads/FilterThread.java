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
package alpha.editor.threads;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import java.util.logging.Logger;


import alpha.editor.panels.AlphaPanel;
import alpha.editor.panels.ConnectionPanel;
import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.FundamentalSemanticType;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;

/**
 * This class is derived from the class Thread. Its job is to decide weather or
 * not the individual AlphaPanels and ConnectionPanels are visible based on the
 * chosen filter parameter.
 * 
 */
public class FilterThread extends Thread {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(FilterThread.class.getName());

	/** The AlphaPropsFacade. */
	private final AlphaPropsFacade apf;

	/** The config. */
	private final AlphadocConfig config;

	/** The box. */
	private final JComboBox box;

	/**
	 * Instantiates a new filter thread.
	 * 
	 * @param source
	 *            the source
	 * @param config
	 *            the config
	 * @param apf
	 *            the apf
	 */
	public FilterThread(final Object source, final AlphadocConfig config,
			final AlphaPropsFacade apf) {
		this.box = (JComboBox) source;
		this.config = config;
		this.apf = apf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		final LinkedHashMap<AlphaCardID, AlphaPanel> panels = new LinkedHashMap<AlphaCardID, AlphaPanel>();
		final LinkedHashSet<ConnectionPanel> con = new LinkedHashSet<ConnectionPanel>();
		final LinkedHashMap<AlphaCardID, AlphaPanel> visible = new LinkedHashMap<AlphaCardID, AlphaPanel>();
		boolean showC = false;
		String selected = (String) this.box.getSelectedItem();
		selected = selected.toLowerCase();
		final JPanel model = (JPanel) this.box.getParent();
		final Component[] components = model.getComponents();
		for (final Component component : components) {
			if (component instanceof AlphaPanel) {
				panels.put(((AlphaPanel) component).getAci(),
						(AlphaPanel) component);
			} else if (component instanceof ConnectionPanel) {
				con.add((ConnectionPanel) component);
			} else if (component instanceof JCheckBox) {
				final JCheckBox box = (JCheckBox) component;
				if (box.getName().equals("showC")) {
					showC = box.isSelected();
				}
			}
		}
		try {
			if (selected.equals("all")) {
				for (final AlphaPanel ap : panels.values()) {
					final AlphaCardDescriptor cardDesc = this.apf
							.getAlphaCard(ap.getAci());
					final String fsType = cardDesc.readAdornment(
							CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
							.getValue();
					if (fsType
							.equalsIgnoreCase(FundamentalSemanticType.COORDINATION
									.value())) {
						ap.setVisible(showC);
					} else {
						ap.setVisible(true);
						visible.put(ap.getAci(), ap);
					}
				}
			} else if (selected.equals("own")) {
				for (final AlphaPanel ap : panels.values()) {
					if (this.config
							.getLocalNodeID()
							.getContributor()
							.getActor()
							.equals(this.apf
									.getAlphaCard(ap.getAci())
									.readAdornment(
											CorpusGenericus.ACTOR.value())
									.getValue().substring(1))) {
						ap.setVisible(true);
						visible.put(ap.getAci(), ap);
					} else {
						ap.setVisible(false);
					}
				}
			} else if (selected.equals("others")) {
				for (final AlphaPanel ap : panels.values()) {
					final String currentUserName = this.config
							.getLocalNodeID().getContributor().getActor();
					final AlphaCardDescriptor cardDesc = this.apf
							.getAlphaCard(ap.getAci());
					final String cardOwnerName = cardDesc
							.readAdornment(CorpusGenericus.ACTOR.value())
							.getValue().substring(1);
					FilterThread.LOGGER.finer("The card owner is "
							+ cardOwnerName);
					final String fsType = cardDesc.readAdornment(
							CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
							.getValue();
					if (currentUserName.equals(cardOwnerName)) {
						ap.setVisible(false);
					} else if (fsType
							.equalsIgnoreCase(FundamentalSemanticType.COORDINATION
									.value())) {
						ap.setVisible(showC);
					} else {
						ap.setVisible(true);
						visible.put(ap.getAci(), ap);
					}
				}
			} else {
				for (final AlphaPanel ap : panels.values()) {
					if (selected.equals(this.apf.getAlphaCard(ap.getAci())
							.readAdornment(CorpusGenericus.ACTOR.value())
							.getValue().substring(1).toLowerCase())) {
						ap.setVisible(true);
						visible.put(ap.getAci(), ap);
					} else {
						ap.setVisible(false);
					}
				}
			}
			for (final ConnectionPanel cp : con) {
				if (!(visible.containsKey(cp.getSrc()) || visible
						.containsKey(cp.getDst()))) {
					cp.setVisible(false);
				} else {
					cp.setVisible(true);
				}
			}
		} catch (final Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
