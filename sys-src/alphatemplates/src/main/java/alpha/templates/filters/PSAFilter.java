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
 * $Id: PSAFilter.java 3650 2012-03-08 23:54:03Z sipareis $
 *************************************************************************/
package alpha.templates.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import java.util.logging.Logger;


import alpha.model.AlphaCardDescriptor;
import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.FundamentalSemanticType;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;
import alpha.templates.exceptions.FilterException;
import alpha.templates.gui.AlphaCardNamesPresenter;

/**
 * Filters or merges the alpha-card ids based on user decisions, the
 * alpha-card-relationships automatically.
 */
public class PSAFilter {

	/** The Constant LOGGER. */
	private transient static final Logger LOGGER = Logger
			.getLogger(PSAFilter.class.getName());

	/** The alpha card names presenter. */
	private final AlphaCardNamesPresenter alphaCardNamesPresenter;

	/**
	 * Instantiates a new pSA filter.
	 * 
	 * @param alphaCardNamesPresenter
	 *            the alpha card names presenter
	 */
	public PSAFilter(final AlphaCardNamesPresenter alphaCardNamesPresenter) {
		this.alphaCardNamesPresenter = alphaCardNamesPresenter;
	}

	/**
	 * filters the template psa - called when template is exported.
	 * 
	 * @param psaPayload
	 *            the psapl
	 * @param descriptorList
	 *            the acdlist
	 * @param output
	 *            the output
	 * @return the list
	 * @throws FilterException
	 *             the filter exception
	 */

	public PSAPayload filterPsa(final PSAPayload psaPayload,
			List<AlphaCardDescriptor> descriptorList, final String output)
			throws FilterException {

		if (psaPayload == null)
			throw new FilterException();

		// choose only between content cards
		descriptorList = this.createListOfContentCards(descriptorList);

		final HashMap<AlphaCardDescriptor, Boolean> alphaCardMap = (HashMap<AlphaCardDescriptor, Boolean>) this.alphaCardNamesPresenter
				.createAlphaCardMap(descriptorList, output);

		this.applyPsaFilter(psaPayload, alphaCardMap);

		return psaPayload;
	}

	/**
	 * Merge psas when template is imported.
	 * 
	 * @param descriptorList
	 *            the acdlist
	 * @param oldPsaPayload
	 *            the old psa
	 * @param templatePsaPayload
	 *            the template psa
	 * @param epId
	 *            the ep id
	 * @return the merged pSA payload
	 * @throws FilterException
	 *             the filter exception
	 */
	public PSAPayload mergePsa(final List<AlphaCardDescriptor> descriptorList,
			final PSAPayload oldPsaPayload,
			final PSAPayload templatePsaPayload, final String epId)
			throws FilterException {

		if ((oldPsaPayload == null) || (templatePsaPayload == null))
			throw new FilterException();

		final ArrayList<AlphaCardDescriptor> newDescriptorList = this
				.createListOfNewDescriptors(descriptorList, oldPsaPayload);

		final HashMap<AlphaCardDescriptor, Boolean> alphaCardMap = (HashMap<AlphaCardDescriptor, Boolean>) this.alphaCardNamesPresenter
				.createAlphaCardMap(newDescriptorList,
						"Which new Alpha-Cards do you want to add to the Alpha-Doc?");

		if (alphaCardMap == null) {
			PSAFilter.LOGGER.warning("Cannot merge PSA!");
			throw new FilterException();
		}

		final PSAPayload newPsaPayload = this.createMergedPsa(oldPsaPayload,
				templatePsaPayload, epId, alphaCardMap);

		return newPsaPayload;
	}

	/**
	 * Adapts lists of alphacards after filtering psa.
	 * 
	 * @param templateDescriptorList
	 *            the template ac ds
	 * @param templatePsaPayload
	 *            the new psa
	 * @param oldPsaPayload
	 *            the old psa
	 * @return the list
	 */
	public List<AlphaCardDescriptor> createListOfAdditionalCards(
			final List<AlphaCardDescriptor> templateDescriptorList,
			final PSAPayload templatePsaPayload, final PSAPayload oldPsaPayload) {

		final ArrayList<AlphaCardDescriptor> newDescriptorList = new ArrayList<AlphaCardDescriptor>();

		for (final AlphaCardDescriptor acd : templateDescriptorList) {
			if (templatePsaPayload.getListOfTodoItems().contains(acd.getId())) {
				// at merge-import, if acd was already present, do not use
				// template version
				if (oldPsaPayload.getListOfTodoItems().contains(acd.getId())) {
					continue;
				}
				newDescriptorList.add(acd);
			}
		}

		return newDescriptorList;
	}

	/**
	 * removes descriptors that are not part of psa, and that are coordination
	 * card descriptors.
	 * 
	 * @param oldDescriptorList
	 *            the old ac ds
	 * @param psaPayload
	 *            the psapl
	 * @return the list
	 */
	public List<AlphaCardDescriptor> adaptDescriptorsToPsa(
			final List<AlphaCardDescriptor> oldDescriptorList,
			final PSAPayload psaPayload) {

		final List<AlphaCardDescriptor> adaptedList = new ArrayList<AlphaCardDescriptor>();
		if (oldDescriptorList != null) {
			for (final AlphaCardDescriptor acd : oldDescriptorList) {
				if (psaPayload.getListOfTodoItems().contains(acd.getId())
						&& (acd.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE
								.value())).getValue().equals(
								FundamentalSemanticType.CONTENT.value())) {
					adaptedList.add(acd);
				}
			}
		}
		return adaptedList;
	}

	/**
	 * Apply psa filter.
	 * 
	 * @param psaPayload
	 *            the psa payload
	 * @param alphaCardMap
	 *            the alpha card map
	 * @throws FilterException
	 *             the filter exception
	 */
	private void applyPsaFilter(final PSAPayload psaPayload,
			final HashMap<AlphaCardDescriptor, Boolean> alphaCardMap)
			throws FilterException {
		if (alphaCardMap != null) {
			for (final Map.Entry<AlphaCardDescriptor, Boolean> entry : alphaCardMap
					.entrySet()) {
				if (entry.getValue() == false) {
					psaPayload.getListOfTodoItems().remove(
							entry.getKey().getId());
					for (final AlphaCardRelationship acr : psaPayload
							.getListOfTodoRelationships()) {
						if (acr.getDstID().equals(entry.getKey().getId())
								|| acr.getSrcID()
										.equals(entry.getKey().getId())) {
							psaPayload.getListOfTodoRelationships().remove(acr);
						}
					}
				}
			}
		} else {
			PSAFilter.LOGGER.warning("Cannot filter PSA!");
			throw new FilterException();
		}
	}

	/**
	 * Creates the merged psa.
	 * 
	 * @param oldPsaPayload
	 *            the old psa payload
	 * @param templatePsaPayload
	 *            the template psa payload
	 * @param epId
	 *            the ep id
	 * @param alphaCardMap
	 *            the alpha card map
	 * @return the pSA payload
	 */
	private PSAPayload createMergedPsa(final PSAPayload oldPsaPayload,
			final PSAPayload templatePsaPayload, final String epId,
			final HashMap<AlphaCardDescriptor, Boolean> alphaCardMap) {
		// copies the old psa
		final PSAPayload newPsaPayload = this.copyPsaPayload(oldPsaPayload,
				epId);

		// add all relationships from template
		newPsaPayload.getListOfTodoRelationships().addAll(
				templatePsaPayload.getListOfTodoRelationships());

		AlphaCardID currentAci = null;
		for (final Map.Entry<AlphaCardDescriptor, Boolean> entry : alphaCardMap
				.entrySet()) {
			if (entry.getValue() == true) {
				currentAci = entry.getKey().getId();
				if (!newPsaPayload.getListOfTodoItems().contains(currentAci)) {
					newPsaPayload.getListOfTodoItems().add(currentAci);
				}
			}
		}

		// remove relationships, if one part does not appear in new psa
		final Iterator<AlphaCardRelationship> it = newPsaPayload
				.getListOfTodoRelationships().iterator();

		while (it.hasNext()) {
			final AlphaCardRelationship acr = it.next();
			if (!newPsaPayload.getListOfTodoItems().contains(acr.getSrcID())
					|| !newPsaPayload.getListOfTodoItems().contains(
							acr.getDstID())) {
				newPsaPayload.getListOfTodoRelationships().remove(acr);
			}
		}
		return newPsaPayload;
	}

	/**
	 * Copy psa payload.
	 * 
	 * @param oldPsaPayload
	 *            the old psa payload
	 * @param epId
	 *            the ep id
	 * @return the pSA payload
	 */
	private PSAPayload copyPsaPayload(final PSAPayload oldPsaPayload,
			final String epId) {
		final PSAPayload newPsaPayload = new PSAPayload();
		final LinkedHashSet<AlphaCardID> lhs = new LinkedHashSet<AlphaCardID>();
		lhs.add(new AlphaCardID(epId, CoordCardType.PSA.id()));
		lhs.add(new AlphaCardID(epId, CoordCardType.APA.id()));
		lhs.add(new AlphaCardID(epId, CoordCardType.CRA.id()));

		newPsaPayload.setListOfTodoItems(lhs);
		newPsaPayload
				.setListOfTodoRelationships(new LinkedHashSet<AlphaCardRelationship>());

		if (oldPsaPayload != null) {
			// include old relationships & old ids
			newPsaPayload.getListOfTodoRelationships().addAll(
					oldPsaPayload.getListOfTodoRelationships());
			lhs.addAll(oldPsaPayload.getListOfTodoItems());
		}
		return newPsaPayload;
	}

	/**
	 * Creates the list of new descriptors.
	 * 
	 * @param descriptorList
	 *            the descriptor list
	 * @param oldPsaPayload
	 *            the old psa payload
	 * @return the array list
	 */
	private ArrayList<AlphaCardDescriptor> createListOfNewDescriptors(
			final List<AlphaCardDescriptor> descriptorList,
			final PSAPayload oldPsaPayload) {

		final ArrayList<AlphaCardDescriptor> newDescriptorList = new ArrayList<AlphaCardDescriptor>();

		// add alpha cards from template
		for (final AlphaCardDescriptor acd : descriptorList) {

			// if alpha card is already present, do not use the one from the
			// template
			if ((oldPsaPayload != null)
					&& oldPsaPayload.getListOfTodoItems().contains(acd.getId())) {
				continue;
			}

			PrototypedAdornment pa = acd.readAdornment(CorpusGenericus.TITLE
					.value());
			if (pa == null) {
				pa = new PrototypedAdornment(CorpusGenericus.TITLE.value(),
						ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
				pa.setValue(CorpusGenericus.TITLE.value());
				pa.setInstance(true);
				acd.updateOrCreateAdornment(pa);
			}
			newDescriptorList.add(acd);

		}
		return newDescriptorList;
	}

	/**
	 * Creates the list of content cards.
	 * 
	 * @param descriptorList
	 *            the acdlist
	 * @return the array list
	 */
	public ArrayList<AlphaCardDescriptor> createListOfContentCards(
			final List<AlphaCardDescriptor> descriptorList) {

		if (descriptorList == null)
			return null;

		final ArrayList<AlphaCardDescriptor> contentCardList = new ArrayList<AlphaCardDescriptor>();
		for (final AlphaCardDescriptor acd : descriptorList) {
			if ((acd.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE
					.value())).getValue().equals(
					FundamentalSemanticType.CONTENT.value())) {
				contentCardList.add(acd);
			}
		}
		return contentCardList;
	}

}
