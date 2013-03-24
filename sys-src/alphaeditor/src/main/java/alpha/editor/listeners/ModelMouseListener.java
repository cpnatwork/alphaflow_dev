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

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import alpha.editor.AlphaCardStatus;
import alpha.editor.panels.AlphaPanel;
import alpha.editor.panels.ConnectionPanel;
import alpha.model.identification.AlphaCardID;

/**
 * This class is a filter that displays either all AlphaPanels that contain
 * "open" AlphaCards or all AlphaPanels that contain "complete" AlphaCards. An
 * AlphaCardDescriptor is "complete" if its Adornment "Visibility" has the value
 * "public" and the Adornment "Validity" has the value "valid". Any other values
 * describe an "open" AlphaCardDescriptor.
 * 
 */
public class ModelMouseListener implements ItemListener {

	/** The open. */
	private final boolean open;

	/**
	 * Instantiates a new model mouse listener.
	 * 
	 * @param open
	 *            the open
	 */
	public ModelMouseListener(final boolean open) {
		this.open = open;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(final ItemEvent e) {
		final LinkedHashMap<AlphaCardID, AlphaPanel> panels = new LinkedHashMap<AlphaCardID, AlphaPanel>();
		final LinkedHashSet<ConnectionPanel> con = new LinkedHashSet<ConnectionPanel>();
		final JCheckBox box = (JCheckBox) e.getSource();
		final String self = box.getText();
		final JPanel model = (JPanel) box.getParent();
		final Component[] components = model.getComponents();
		/* hide unwanted AlphaPanels */
		for (final Component component : components) {
			if (component instanceof AlphaPanel) {
				panels.put(((AlphaPanel) component).getAci(),
						(AlphaPanel) component);
			} else if (component instanceof ConnectionPanel) {
				con.add((ConnectionPanel) component);
			} else if (component instanceof JCheckBox) {
				final JCheckBox cb = (JCheckBox) component;
				if (box.isSelected()) {
					if (cb.getText().toLowerCase().contains("expand")) {
						// don't touch it
					} else if (cb.getText().toLowerCase().contains("open")
							&& self.toLowerCase().contains("complete")) {
						cb.setSelected(false);
					} else if (cb.getText().toLowerCase().contains("complete")
							&& self.toLowerCase().contains("open")) {
						cb.setSelected(false);
					}
				}
			}
		}

		for (final AlphaPanel ap : panels.values()) {
			ap.setVisible(true);
			if (box.isSelected()) {
				if (this.open) {
					if (ap.getStatus() == AlphaCardStatus.VALIDPUBLIC) {
						ap.setVisible(false);
					}
				} else {
					if (!(ap.getStatus() == AlphaCardStatus.VALIDPUBLIC)) {
						ap.setVisible(false);
					}
				}
			}
		}

		for (final ConnectionPanel conn : con) {
			if (panels.get(conn.getSrc()).isVisible()
					&& panels.get(conn.getDst()).isVisible()) {
				conn.setVisible(true);
			} else {
				conn.setVisible(false);
			}
		}
	}
}
