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
 * $Id: WizardContributorNamesPresenter.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.templates.gui.wizard;

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

import alpha.model.apa.CorpusGenericusOC;
import alpha.model.cra.ObjectUnderConsideration;
import alpha.model.cra.Participant;
import alpha.templates.gui.ContributorNamesPresenter;

/**
 * Implements the ContributorNamesPresenter using a wizard with checkboxes for
 * each contributor name, including physicians and the object under
 * consideration.
 */
public class WizardContributorNamesPresenter implements
		ContributorNamesPresenter {

	/** The tw. */
	private final TemplateWizard tw;

	/** The hm. */
	private HashMap<Participant, Boolean> contributorMap = null;

	/** The oc. */
	private final ObjectUnderConsideration oc;

	/**
	 * Instantiates a new wizard contributor names presenter.
	 * 
	 * @param tw
	 *            the tw
	 * @param oc
	 *            the oc
	 */
	public WizardContributorNamesPresenter(final TemplateWizard tw,
			final ObjectUnderConsideration oc) {
		this.tw = tw;
		this.oc = oc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.templates.gui.ContributorNamesPresenter#createContributorMap(java
	 * .util.List, java.lang.String)
	 */
	@Override
	public Map<Participant, Boolean> createContributorMap(
			final List<Participant> participantList, final String output) {

		if ((participantList == null) || (this.tw == null))
			return null;

		String ocName = "";
		if (this.oc != null) {
			ocName = this.oc.readAdornment(CorpusGenericusOC.OCNAME.value())
					.getValue();
		}
		for (final ActionListener al : this.tw.getNextButton()
				.getActionListeners()) {
			this.tw.getNextButton().removeActionListener(al);
		}
		for (final ActionListener al : this.tw.getCancelButton()
				.getActionListeners()) {
			this.tw.getCancelButton().removeActionListener(al);
		}
		final ArrayList<JCheckBox> checkBoxes = (ArrayList<JCheckBox>) this
				.rebuildMainPanel(participantList, output, ocName);

		this.tw.getNextButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				WizardContributorNamesPresenter.this.evaluateCheckBoxes(
						participantList, checkBoxes);
				WizardContributorNamesPresenter.this.tw.getWizard().setVisible(
						false);
			}
		});
		this.tw.getCancelButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				WizardContributorNamesPresenter.this.contributorMap = null;
				WizardContributorNamesPresenter.this.tw.getWizard().setVisible(
						false);
			}
		});

		this.tw.getWizard().setVisible(true);

		return this.contributorMap;
	}

	/**
	 * Creates the new main panel.
	 * 
	 * @param contributorList
	 *            the contributor list
	 * @param output
	 *            the output
	 * @param ocName
	 *            the oc name
	 * @return the array list
	 */
	private List<JCheckBox> rebuildMainPanel(
			final List<Participant> contributorList, final String output,
			final String ocName) {

		this.tw.getChoosePanel().removeAll();
		this.tw.getTitleLabel().setText("<HTML>" + output + "</HTML>");

		if (contributorList.size() >= 10) {
			this.tw.getChoosePanel().setLayout(
					new GridLayout(contributorList.size(), 1));
		} else {
			this.tw.getChoosePanel().setLayout(new GridLayout(10, 1));
		}

		final ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();

		JCheckBox tmp = null;
		if (this.oc != null) {
			final JLabel ocLabel = new JLabel("Object under Consideration:");
			ocLabel.setFont(new Font(ocLabel.getFont().getName(), ocLabel
					.getFont().getStyle(), ocLabel.getFont().getSize() + 2));
			this.tw.getChoosePanel().add(ocLabel);
			tmp = new JCheckBox("ID: " + this.oc.getId() + "; Name: " + ocName,
					true);
			this.tw.getChoosePanel().add(tmp);
		}

		final JLabel conLabel = new JLabel("Contributors:");
		conLabel.setFont(new Font(conLabel.getFont().getName(), conLabel
				.getFont().getStyle(), conLabel.getFont().getSize() + 2));

		if (contributorList.size() == 0) {
			conLabel.setText("There are no contributors to be chosen.");
		}
		this.tw.getChoosePanel().add(conLabel);
		for (final Participant pn : contributorList) {
			final JCheckBox tmp2 = new JCheckBox(
					pn.getContributor().toString(), true);
			checkBoxes.add(tmp2);
			this.tw.getChoosePanel().add(tmp2);
		}
		// add oc at last position if necessary
		if (tmp != null) {
			checkBoxes.add(tmp);
		}

		return checkBoxes;

	}

	/**
	 * Evaluate input.
	 * 
	 * @param participantList
	 *            the contributor list
	 * @param checkBoxes
	 *            the check boxes
	 */
	private void evaluateCheckBoxes(final List<Participant> participantList,
			final List<JCheckBox> checkBoxes) {
		this.contributorMap = new HashMap<Participant, Boolean>(
				checkBoxes.size());

		for (int i = 0; i < (checkBoxes.size() - 1); i++) {
			this.contributorMap.put(participantList.get(i), checkBoxes.get(i)
					.isSelected());
		}

		// there is no stuff to be chosen
		if (checkBoxes.size() == 0)
			return;

		// if oc is included, set last key to null to characterize
		if (this.oc != null) {
			this.contributorMap.put(null, checkBoxes.get(checkBoxes.size() - 1)
					.isSelected());
		} else {
			this.contributorMap.put(
					participantList.get(participantList.size() - 1), checkBoxes
							.get(checkBoxes.size() - 1).isSelected());
		}
	}

}
