/**************************************************************************
 * alpha-Forms: self-editable formulars in form of an active document
 * (originally for the alpha-Flow project)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Florian Wagner
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
package alpha.forms.clipboard.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import alpha.forms.form.AlphaForm;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class FormViewPanel.
 */
public class FormViewPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private final AlphaForm model;

	/**
	 * Instantiates a new form view panel.
	 * 
	 * @param model
	 *            the model
	 */
	public FormViewPanel(final AlphaForm model) {
		this.model = model;
		this.setMinimumSize(new Dimension(640, 480));
		this.setLayout(new BorderLayout());

		final JPanel header = new JPanel(new GridBagLayout());
		header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		final GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.2;

		final JLabel formTitle = new JLabel(model.getTitle());
		formTitle.setFont(formTitle.getFont().deriveFont(18.0f));

		header.add(formTitle, c);

		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0.0;

		JComboBox documentStates;
		if (model.getDocumentStates().isEmpty()) {
			documentStates = new JComboBox(new String[] { "NONE" });
			documentStates.setEditable(false);
			documentStates.setEnabled(false);
		} else {
			documentStates = new JComboBox(model.getDocumentStates().toArray());
			documentStates.setEditable(false);
		}

		header.add(documentStates, c);

		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		final JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

		header.add(separator, c);

		this.add(header, BorderLayout.NORTH);

		final JPanel form = new JPanel();
		form.setLayout(null);

		form.setMinimumSize(new Dimension(model.getWidth(), model.getHeight()));
		form.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));

		for (final FormWidget w : model.getWidgets()) {
			form.add(w.getUi());
		}

		final JScrollPane scrollPane = new JScrollPane(form);
		scrollPane.setBorder(null);

		this.add(scrollPane, BorderLayout.CENTER);
	}

}
