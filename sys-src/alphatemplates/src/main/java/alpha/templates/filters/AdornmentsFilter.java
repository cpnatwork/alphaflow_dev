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
 * $Id: AdornmentsFilter.java 3650 2012-03-08 23:54:03Z sipareis $
 *************************************************************************/
package alpha.templates.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.logging.Logger;

import alpha.model.AlphaCardDescriptor;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.APAPayload;
import alpha.model.apa.CorpusGenericus;
import alpha.templates.exceptions.FilterException;
import alpha.templates.gui.AdornmentNamesPresenter;
import alpha.templates.gui.AlphaCardNamesPresenter;

/**
 * Filters all Descriptors of all Alpha-Cards based on user decisions.
 */
public class AdornmentsFilter {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AdornmentsFilter.class.getName());

	/** The apapl. */
	private final APAPayload apaPayload;

	/** The adornment presenter. */
	private final AdornmentNamesPresenter adornmentNamesPresenter;

	/** The alpha card names presenter. */
	private final AlphaCardNamesPresenter alphaCardNamesPresenter;

	/** The adornments, which are always exported. */
	private final Set<CorpusGenericus> alwaysExportedAdornments = new LinkedHashSet<CorpusGenericus>(
			Arrays.asList(CorpusGenericus.TITLE, CorpusGenericus.ALPHACARDTYPE,
					CorpusGenericus.FUNDAMENTALSEMANTICTYPE));

	/** The adornments, which are never exported. */
	private Set<CorpusGenericus> neverExportedAdornments = new LinkedHashSet<CorpusGenericus>(
			Arrays.asList(CorpusGenericus.VISIBILITY, CorpusGenericus.VALIDITY,
					CorpusGenericus.VERSION, CorpusGenericus.VARIANT,
					CorpusGenericus.SYNTACTICPAYLOADTYPE,
					CorpusGenericus.DUEDATE));

	/** The adornment ask list. */
	private ArrayList<String> adornmentMayAskList;

	/** The is export. */
	private final boolean isExport;

	/**
	 * Gets the never exported adornments.
	 * 
	 * @return the never exported adornments
	 */
	public Set<CorpusGenericus> getNeverExportedAdornments() {
		return this.neverExportedAdornments;
	}

	/**
	 * Sets the never exported adornments.
	 * 
	 * @param neverExportedAdornments
	 *            the new never exported adornments
	 */
	public void setNeverExportedAdornments(
			final Set<CorpusGenericus> neverExportedAdornments) {
		this.neverExportedAdornments = neverExportedAdornments;
	}

	/**
	 * Instantiates a new descriptor adornments filter.
	 * 
	 * @param apaPayload
	 *            the apapl
	 * @param adornmentNamesPresenter
	 *            the adornment names presenter
	 * @param alphaCardNamesPresenter
	 *            the alpha card names presenter
	 * @param isExport
	 *            the is export
	 */
	public AdornmentsFilter(final APAPayload apaPayload,
			final AdornmentNamesPresenter adornmentNamesPresenter,
			final AlphaCardNamesPresenter alphaCardNamesPresenter,
			final boolean isExport) {
		this.apaPayload = apaPayload;
		this.adornmentNamesPresenter = adornmentNamesPresenter;
		this.alphaCardNamesPresenter = alphaCardNamesPresenter;
		this.isExport = isExport;
		// create the list of adornments that may be used but don't have to
		this.createAdornmentMayAskList();
	}

	/**
	 * filters adornments of all alpha card descriptors.
	 * 
	 * @param descriptorList
	 *            the acdlist
	 * @return the list
	 * @throws FilterException
	 *             the filter exception
	 */
	public List<AlphaCardDescriptor> filterAdornments(
			final List<AlphaCardDescriptor> descriptorList)
			throws FilterException {

		if (descriptorList == null)
			throw new FilterException();

		if (descriptorList.size() == 0) {
			AdornmentsFilter.LOGGER
					.warning("No adornment filter needed, as there are no alpha cards");
			return descriptorList;
		}
		// creates adornment sets for all Alpha-Cards
		final ArrayList<LinkedHashSet<String>> adornmentSets = (ArrayList<LinkedHashSet<String>>) this
				.createAdornmentSets(descriptorList);

		this.applyAdornmentsFilter(descriptorList, adornmentSets);
		return descriptorList;
	}

	/**
	 * Apply adornments filter.
	 * 
	 * @param descriptorList
	 *            the descriptor list
	 * @param adornmentSets
	 *            the adornment sets
	 */
	public void applyAdornmentsFilter(
			final List<AlphaCardDescriptor> descriptorList,
			final List<LinkedHashSet<String>> adornmentSets) {
		for (int i = 0; i < descriptorList.size(); i++) {

			final ArrayList<PrototypedAdornment> aalist = new ArrayList<PrototypedAdornment>(
					descriptorList.get(i).readAdornments());
			for (final PrototypedAdornment aa : aalist) {

				// export fundamental semantic type anyway
				if (aa.getName().equals(
						CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())) {
					continue;
				}

				// don't export adornment, if it has default value && default
				// instance value
				if (!adornmentSets.get(i).contains(aa.getName())
						|| ((this.isExport == true)
								&& descriptorList
										.get(i)
										.readAdornment(aa.getName())
										.getValue()
										.equals(this.apaPayload
												.getContentCardAdornmentPrototype()
												.readAdornment(aa.getName())
												.getValue()) && (descriptorList
								.get(i).readAdornment(aa.getName())
								.isInstance() == this.apaPayload
								.getContentCardAdornmentPrototype()
								.readAdornment(aa.getName()).isInstance()))) {
					descriptorList.get(i).deleteAdornment(aa.getName());
				}
			}
		}
	}

	/**
	 * creates the filtered adornment sets for every alpha card descriptor.
	 * 
	 * @param descriptorList
	 *            the filtered descriptor list
	 * @return the list
	 * @throws FilterException
	 *             the filter exception
	 */
	private List<LinkedHashSet<String>> createAdornmentSets(
			final List<AlphaCardDescriptor> descriptorList)
			throws FilterException {

		// defines default set of adornments, which may be used for all
		// Alpha-Cards
		final LinkedHashSet<String> defaultAdornmentSet = (LinkedHashSet<String>) this
				.createSingleAdornmentSet(
						(List<PrototypedAdornment>) this.apaPayload
								.getContentCardAdornmentPrototype()
								.readAdornments(),
						"Please define a default set of adornments: ");

		// asks for which Alpha-Cards default adornments shall be
		// imported/exported
		final HashMap<AlphaCardDescriptor, Boolean> alphaCardMap = (HashMap<AlphaCardDescriptor, Boolean>) this.alphaCardNamesPresenter
				.createAlphaCardMap(descriptorList,
						"Apply previously defined default set to which cards?");

		if (alphaCardMap == null) {
			AdornmentsFilter.LOGGER.warning("Cannot filter adornments");
			throw new FilterException();
		}

		final ArrayList<LinkedHashSet<String>> adornmentLists = new ArrayList<LinkedHashSet<String>>();

		// based on previous user choice, either default set is used, or ask for
		// each other card separately
		for (final Map.Entry<AlphaCardDescriptor, Boolean> entry : alphaCardMap
				.entrySet()) {
			if (entry.getValue() == true) {
				adornmentLists.add(defaultAdornmentSet);
			} else {
				adornmentLists.add((LinkedHashSet<String>) this
						.createSingleAdornmentSet(
								(List<PrototypedAdornment>) this.apaPayload
										.getContentCardAdornmentPrototype()
										.readAdornments(),
								"Select Alpha-Adornments for: "
										+ entry.getKey()
												.readAdornment(
														CorpusGenericus.TITLE
																.value())
												.getValue()));
			}
		}

		return adornmentLists;

	}

	/**
	 * this method asks for the set of adornments of a single alpha card
	 * descriptor.
	 * 
	 * @param aalist
	 *            the aalist
	 * @param output
	 *            the output
	 * @return list of adornments to be exported
	 * @throws FilterException
	 *             the filter exception
	 */
	private Set<String> createSingleAdornmentSet(
			final List<PrototypedAdornment> aalist, final String output)
			throws FilterException {
		// the list of adornments to be exported
		final LinkedHashSet<String> adornmentSet = new LinkedHashSet<String>();

		// export these adornments anyway
		adornmentSet.add(CorpusGenericus.TITLE.value());
		adornmentSet.add(CorpusGenericus.ALPHACARDTYPE.value());
		adornmentSet.add(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value());

		final ArrayList<String> adornmentAskList = new ArrayList<String>();

		// always ask user if it is non-generic
		for (final PrototypedAdornment adornment : aalist) {
			if (!adornment.getConsensusScope().equals(
					ConsensusScope.GENERIC_STD)
					|| this.adornmentMayAskList.contains(adornment.getName())) {
				adornmentAskList.add(adornment.getName());
			}
		}
		final HashMap<String, Boolean> adornmentMap = (HashMap<String, Boolean>) this.adornmentNamesPresenter
				.createAdornmentMap(adornmentAskList, output);

		if (adornmentMap == null)
			throw new FilterException();

		// add all selected adornments
		for (final Map.Entry<String, Boolean> entry : adornmentMap.entrySet()) {
			if (entry.getValue() == true) {
				adornmentSet.add(entry.getKey());
			}
		}
		return adornmentSet;
	}

	/**
	 * Creates the list of adornments that may be used, but do not have to.
	 */
	private void createAdornmentMayAskList() {
		this.adornmentMayAskList = new ArrayList<String>();
		for (final CorpusGenericus cg : CorpusGenericus.values()) {
			/*
			 * if adornment may be imported/exported, but does not have to, add
			 * to "askList"
			 */
			if (!this.alwaysExportedAdornments.contains(cg)
					&& !this.neverExportedAdornments.contains(cg)) {
				this.adornmentMayAskList.add(cg.value());
			}
		}
	}

}
