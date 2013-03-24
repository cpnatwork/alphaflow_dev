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
 * $Id: CRAFilter.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.templates.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.logging.Logger;


import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.CorpusGenericus;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.Participant;
import alpha.templates.exceptions.FilterException;
import alpha.templates.gui.ContributorNamesPresenter;

/**
 * Filters or merges the contributors in the CRA based on user decisions.
 */
public class CRAFilter {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(CRAFilter.class.getName());

	/** The cnp. */
	private final ContributorNamesPresenter contributorNamesPresenter;

	/** The old cra payload. */
	private final CRAPayload oldCraPayload;

	/**
	 * Instantiates a new cRA filter.
	 * 
	 * @param cnp
	 *            the cnp
	 * @param oldCraPayload
	 *            the old cra payload
	 */
	public CRAFilter(final ContributorNamesPresenter cnp,
			final CRAPayload oldCraPayload) {
		this.contributorNamesPresenter = cnp;
		this.oldCraPayload = oldCraPayload;
	}

	/**
	 * Filters cra based on user choice.
	 * 
	 * @return the cRA payload
	 * @throws FilterException
	 *             the filter exception
	 */
	public CRAPayload filterCra() throws FilterException {

		if (this.oldCraPayload == null)
			throw new FilterException();

		final CRAPayload newCraPayload = new CRAPayload();

		// will be set to null again, if user does not want to import/export
		newCraPayload.setOc(this.oldCraPayload.getOc());
		newCraPayload.setListOfParticipants(new LinkedHashSet<Participant>());

		final HashMap<Participant, Boolean> contributorMap = (HashMap<Participant, Boolean>) this.contributorNamesPresenter
				.createContributorMap(new ArrayList<Participant>(
						this.oldCraPayload.getListOfParticipants()),
						"Choose participants of the new episode:");

		this.applyCraFilter(newCraPayload, contributorMap);

		return newCraPayload;
	}

	/**
	 * Merge cra.
	 * 
	 * @param templateCraPayload
	 *            the template cra
	 * @return the cRA payload
	 * @throws FilterException
	 *             the filter exception
	 */
	public CRAPayload mergeCra(final CRAPayload templateCraPayload)
			throws FilterException {

		if ((templateCraPayload == null) || (this.oldCraPayload == null))
			throw new FilterException();

		final LinkedHashSet<Participant> newParticipants = this
				.createSetOfNewParticipants(templateCraPayload);

		final HashMap<Participant, Boolean> contributorMap = (HashMap<Participant, Boolean>) this.contributorNamesPresenter
				.createContributorMap(new ArrayList<Participant>(
						newParticipants),
						"Which contributors should be added to the episode?");

		final CRAPayload newCraPayload = this.createMergedCraPayload(
				newParticipants, contributorMap);

		return newCraPayload;
	}

	/**
	 * This method removes the "Actor Id" from those alpha cards, whose actor is
	 * not exported.
	 * 
	 * @param craPayload
	 *            the cra payload
	 * @param descriptorList
	 *            the acdlist
	 * @return the list
	 */
	public List<AlphaCardDescriptor> removeActors(final CRAPayload craPayload,
			final List<AlphaCardDescriptor> descriptorList) {

		final LinkedHashSet<String> actors = (LinkedHashSet<String>) craPayload
				.getActorNames();

		for (final AlphaCardDescriptor acd : descriptorList) {
			if (!actors.contains(acd.readAdornment(
					CorpusGenericus.ACTOR.value()).getValue())) {
				acd.deleteAdornment(CorpusGenericus.ACTOR.value());
			}
		}

		return descriptorList;
	}

	/**
	 * Apply cra filter.
	 * 
	 * @param newCraPayload
	 *            the new cra payload
	 * @param contributorMap
	 *            the contributor map
	 * @throws FilterException
	 *             the filter exception
	 */
	private void applyCraFilter(final CRAPayload newCraPayload,
			final HashMap<Participant, Boolean> contributorMap)
			throws FilterException {

		final LinkedHashSet<String> actorNames = (LinkedHashSet<String>) this.oldCraPayload
				.getActorNames();

		if (contributorMap != null) {
			final Set<Participant> plist = this.oldCraPayload
					.getListOfParticipants();
			for (final Map.Entry<Participant, Boolean> entry : contributorMap
					.entrySet()) {
				if (!entry.getValue()) {
					// null if it's the oc
					if (entry.getKey() == null) {
						newCraPayload.setOc(null);
					}
					// add contributor if actor is not already present
				} else if ((entry.getKey() != null)
						&& !actorNames.contains(entry.getKey().getContributor()
								.getActor())) {
					plist.add(entry.getKey());
					actorNames.add(entry.getKey().getContributor().getActor());
				}
			}
			newCraPayload.setListOfParticipants(plist);
		} else {
			CRAFilter.LOGGER.warning("Cannot filter CRA!");
			throw new FilterException();
		}
	}

	/**
	 * Creates the merged cra payload.
	 * 
	 * @param lhs
	 *            the lhs
	 * @param hm
	 *            the hm
	 * @return the cRA payload
	 * @throws FilterException
	 *             the filter exception
	 */
	private CRAPayload createMergedCraPayload(
			final LinkedHashSet<Participant> lhs,
			final HashMap<Participant, Boolean> hm) throws FilterException {
		final CRAPayload newCraPayload = new CRAPayload();
		// do not import oc, as the oc was already defined at alpha-doc creation
		newCraPayload.setOc(this.oldCraPayload.getOc());
		if (hm != null) {
			for (final Map.Entry<Participant, Boolean> entry : hm.entrySet()) {
				if (!entry.getValue() && (entry.getKey() != null)) {
					lhs.remove(entry.getKey());
				}
			}
		} else {
			CRAFilter.LOGGER.warning("Cannot merge CRA!");
			throw new FilterException();
		}
		// new list consists of old participants and chosen ones
		lhs.addAll(this.oldCraPayload.getListOfParticipants());
		newCraPayload.setListOfParticipants(lhs);
		return newCraPayload;
	}

	/**
	 * Creates the set of new participants.
	 * 
	 * @param templateCraPayload
	 *            the template cra payload
	 * @return the linked hash set
	 */
	private LinkedHashSet<Participant> createSetOfNewParticipants(
			final CRAPayload templateCraPayload) {
		final LinkedHashSet<Participant> lhs = new LinkedHashSet<Participant>();

		final LinkedHashSet<String> actorNames = (LinkedHashSet<String>) this.oldCraPayload
				.getActorNames();

		// ask only if new contributors should be imported
		for (final Participant pn : templateCraPayload.getListOfParticipants()) {
			if (!actorNames.contains(pn.getContributor().getActor())) {
				lhs.add(pn);
				actorNames.add(pn.getContributor().getActor());
			}
		}
		return lhs;
	}

}
