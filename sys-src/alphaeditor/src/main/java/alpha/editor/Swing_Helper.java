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
 * $Id: Swing_Helper.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * The Class Swing_Helper.
 */
public class Swing_Helper {

	/**
	 * This method creates a titled separator line.
	 * 
	 * @param title
	 *            the title
	 * @return the JComponent serving as separator
	 */
	public static JComponent createTitledSeparator(final String title) {
		final JLabel separator = new JLabel();
		final Border border = BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray), title,
				TitledBorder.CENTER, TitledBorder.BOTTOM);
		separator.setBorder(border);

		return separator;
	}

	/**
	 * Sets the width of a JComponent without changing its height.
	 * 
	 * @param component
	 *            the component
	 * @param width
	 *            the width
	 */
	public static void setPreferredWidth(final JComponent component,
			final int width) {
		final int height = (int) component.getPreferredSize().getHeight();
		component.setPreferredSize(new Dimension(width, height));
	}

	/**
	 * This method creates a titled border (used for structuring of GUI
	 * elements).
	 * 
	 * @param title
	 *            the title
	 * @return the border
	 */
	public static Border createGuiElementBorder(final String title) {
		final Border titledBorder = BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), title,
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		final Border emptyBoder = BorderFactory.createEmptyBorder(0, 2, 0, 2);

		return BorderFactory.createCompoundBorder(titledBorder, emptyBoder);
	}

	/**
	 * This method creates the border and the title for a panel in the central
	 * area.
	 * 
	 * @param panel
	 *            the panel
	 * @param title
	 *            the title
	 */
	public static void initEditorPanel(final JPanel panel, final String title) {
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		if (title != null) {
			final JLabel titleL = new JLabel(title);
			titleL.setFont(new Font("", Font.BOLD, 18));
			panel.add(titleL, BorderLayout.NORTH);
		}
	}
}
