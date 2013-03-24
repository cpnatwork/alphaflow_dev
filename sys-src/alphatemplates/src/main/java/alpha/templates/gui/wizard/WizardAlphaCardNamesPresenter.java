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
 * $Id: WizardAlphaCardNamesPresenter.java 3573 2012-02-14 11:01:02Z cpn $
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

import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.CorpusGenericus;
import alpha.templates.gui.AlphaCardNamesPresenter;

/**
 * Implements the AlphaCardNamesPresenter using a wizard with checkboxes for
 * each alpha card name.
 */
public class WizardAlphaCardNamesPresenter implements AlphaCardNamesPresenter {

	/** The hm. */
	HashMap<AlphaCardDescriptor, Boolean> alphaCardMap = null;

	/** The tw. */
	private final TemplateWizard tw;

	/**
	 * Instantiates a new wizard alpha card names presenter.
	 * 
	 * @param tw
	 *            the tw
	 */
	public WizardAlphaCardNamesPresenter(final TemplateWizard tw) {
		this.tw = tw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.templates.gui.AlphaCardNamesPresenter#createAlphaCardMap(java.util
	 * .List, java.lang.String)
	 */
	@Override
	public Map<AlphaCardDescriptor, Boolean> createAlphaCardMap(
			final List<AlphaCardDescriptor> descriptorList, final String output) {

		if ((descriptorList == null) || (this.tw == null))
			return null;

		final ArrayList<JCheckBox> checkBoxes = (ArrayList<JCheckBox>) this
				.rebuildMainPanel(descriptorList, output);

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
				WizardAlphaCardNamesPresenter.this.evaluateCheckBoxes(
						descriptorList, checkBoxes);
				WizardAlphaCardNamesPresenter.this.tw.getWizard().setVisible(
						false);
			}
		});
		this.tw.getCancelButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				WizardAlphaCardNamesPresenter.this.alphaCardMap = null;
				WizardAlphaCardNamesPresenter.this.tw.getWizard().setVisible(
						false);
			}
		});

		// blocks gui thread, returns after setting visibility to false by
		// actionlistener
		this.tw.getWizard().setVisible(true);

		return this.alphaCardMap;
	}

	/**
	 * Creates the new main panel.
	 * 
	 * @param descriptorList
	 *            the acdlist
	 * @param output
	 *            the output
	 * @return the array list
	 */
	private List<JCheckBox> rebuildMainPanel(
			final List<AlphaCardDescriptor> descriptorList, final String output) {

		this.tw.getTitleLabel().setText("<HTML>" + output + "</HTML>");
		this.tw.getChoosePanel().removeAll();

		if (descriptorList.size() >= 10) {
			this.tw.getChoosePanel().setLayout(
					new GridLayout(descriptorList.size(), 1));
		} else {
			this.tw.getChoosePanel().setLayout(new GridLayout(10, 1));
		}

		final ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();

		for (int i = 0; i < descriptorList.size(); i++) {

			if (descriptorList.get(i).readAdornment(
					CorpusGenericus.TITLE.value()) != null) {
				final JCheckBox tmp = new JCheckBox(descriptorList.get(i)
						.readAdornment(CorpusGenericus.TITLE.value())
						.getValue(), true);
				checkBoxes.add(tmp);
				this.tw.getChoosePanel().add(tmp);
			} else {
				final JCheckBox tmp = new JCheckBox(
						CorpusGenericus.TITLE.value(), true);
				checkBoxes.add(tmp);
				this.tw.getChoosePanel().add(tmp);
			}
		}

		if (descriptorList.size() == 0) {
			final JLabel noneLabel = new JLabel(
					"  There are no Alpha-Cards to display.");
			noneLabel.setFont(new Font(noneLabel.getFont().getName(), noneLabel
					.getFont().getStyle(), noneLabel.getFont().getSize() + 4));
			this.tw.getChoosePanel().add(noneLabel, BorderLayout.CENTER);
		}

		return checkBoxes;
	}

	/**
	 * Evaluate input.
	 * 
	 * @param descriptorList
	 *            the acdlist
	 * @param checkBoxes
	 *            the check boxes
	 */
	private void evaluateCheckBoxes(
			final List<AlphaCardDescriptor> descriptorList,
			final List<JCheckBox> checkBoxes) {

		this.alphaCardMap = new HashMap<AlphaCardDescriptor, Boolean>(
				checkBoxes.size());

		for (int i = 0; i < checkBoxes.size(); i++) {
			this.alphaCardMap.put(descriptorList.get(i), checkBoxes.get(i)
					.isSelected());
		}
	}

}
