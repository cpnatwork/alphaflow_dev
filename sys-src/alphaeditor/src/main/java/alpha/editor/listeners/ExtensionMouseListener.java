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

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import alpha.editor.panels.AlphaPanel;

/**
 * This class displays an extended view of the AlphaCardDescriptor in the
 * AlphaPanels.
 * 
 */
public class ExtensionMouseListener implements ItemListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(final ItemEvent e) {
		final JCheckBox box = (JCheckBox) e.getSource();
		final JPanel model = (JPanel) box.getParent();
		final Component[] components = model.getComponents();
		for (final Component component : components) {
			if (component instanceof AlphaPanel) {
				final AlphaPanel ap = (AlphaPanel) component;
				ap.getAdornmentsPanel().setVisible(box.isSelected());
			}
		}
	}
}
