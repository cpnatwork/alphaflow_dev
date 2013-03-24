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
 * $Id: WizardAdornmentNamesPresenter.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.templates.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import java.util.logging.Logger;


import alpha.templates.gui.AdornmentNamesPresenter;

/**
 * Implements the AdornmentNamesPresenter using a wizard with checkboxes for
 * each adornment name.
 */
public class WizardAdornmentNamesPresenter implements AdornmentNamesPresenter {

	/** The Constant LOGGER. */
	@SuppressWarnings("unused")
	private static final transient Logger LOGGER = Logger
			.getLogger(WizardAdornmentNamesPresenter.class.getName());

	/** The tw. */
	private final TemplateWizard tw;

	/** The adornment map. */
	private HashMap<String, Boolean> adornmentMap = null;

	/**
	 * Instantiates a new wizard adornment names presenter.
	 * 
	 * @param tw
	 *            the tw
	 */
	public WizardAdornmentNamesPresenter(final TemplateWizard tw) {
		this.tw = tw;
	}

	/**
	 * shows a popup and creates a String -> Boolean hashmap : adornment ->
	 * isSelected.
	 * 
	 * @param adornmentAskList
	 *            the adornment ask list
	 * @param output
	 *            the output
	 * @return adornment checkbox list
	 */
	@Override
	public Map<String, Boolean> createAdornmentMap(
			final List<String> adornmentAskList, final String output) {

		if ((adornmentAskList == null) || (this.tw == null))
			return null;

		// the list of adornment checkboxes, where user may select adornment
		// names
		final ArrayList<JCheckBox> checkBoxes = (ArrayList<JCheckBox>) this
				.rebuildMainPanel(adornmentAskList, output);

		for (final ActionListener al : this.tw.getNextButton()
				.getActionListeners()) {
			this.tw.getNextButton().removeActionListener(al);
		}
		for (final ActionListener al : this.tw.getCancelButton()
				.getActionListeners()) {
			this.tw.getCancelButton().removeActionListener(al);
		}

		this.tw.getNextButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				WizardAdornmentNamesPresenter.this
						.evaluateCheckBoxes(checkBoxes);
				WizardAdornmentNamesPresenter.this.tw.getWizard().setVisible(
						false);
			}
		});
		this.tw.getCancelButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				WizardAdornmentNamesPresenter.this.adornmentMap = null;
				WizardAdornmentNamesPresenter.this.tw.getWizard().setVisible(
						false);
			}
		});

		this.tw.getWizard().setVisible(true);

		return this.adornmentMap;
	}

	/**
	 * Creates the new main panel.
	 * 
	 * @param adornmentAskList
	 *            the adornment ask list
	 * @param output
	 *            the output
	 * @return the array list
	 */
	private List<JCheckBox> rebuildMainPanel(
			final List<String> adornmentAskList, final String output) {

		this.tw.getChoosePanel().removeAll();
		this.tw.getTitleLabel().setText("<HTML>" + output + "</HTML>");

		if (adornmentAskList.size() >= 10) {
			this.tw.getChoosePanel().setLayout(
					new GridLayout(adornmentAskList.size(), 1));
		} else {
			this.tw.getChoosePanel().setLayout(new GridLayout(10, 1));
		}

		final ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();

		for (final String adornmentName : adornmentAskList) {
			final JCheckBox tmp = new JCheckBox(adornmentName, true);
			checkBoxes.add(tmp);
			this.tw.getChoosePanel().add(tmp);
		}

		if (adornmentAskList.size() == 0) {
			final JLabel noneLabel = new JLabel(
					"  There are no Alpha-Adornments to display.");
			noneLabel.setFont(new Font(noneLabel.getFont().getName(), noneLabel
					.getFont().getStyle(), noneLabel.getFont().getSize() + 4));
			this.tw.getChoosePanel().add(noneLabel, BorderLayout.CENTER);
		}

		return checkBoxes;
	}

	/**
	 * Evaluate input.
	 * 
	 * @param checkBoxes
	 *            the check boxes
	 */
	private void evaluateCheckBoxes(final List<JCheckBox> checkBoxes) {

		this.adornmentMap = new HashMap<String, Boolean>(checkBoxes.size());

		for (int i = 0; i < checkBoxes.size(); i++) {
			this.adornmentMap.put(checkBoxes.get(i).getText(), checkBoxes
					.get(i).isSelected());
		}
	}

}
