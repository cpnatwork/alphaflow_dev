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
 * $Id: APAFilter.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.templates.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Logger;


import alpha.adaptive.AlphaAdaptiveFacadeImpl;
import alpha.facades.AlphaAdaptiveFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.adornment.AdpAdornment;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.APAPayload;
import alpha.templates.exceptions.FilterException;
import alpha.templates.gui.AdornmentNamesPresenter;

/**
 * Filters or merges the custom adornment types based on user decisions.
 */
public class APAFilter {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(APAFilter.class.getName());

	/** The adornment names presenter. */
	private final AdornmentNamesPresenter adornmentNamesPresenter;

	/** The updated adornments. */
	private ArrayList<PrototypedAdornment> updatedAdornments;

	/**
	 * Instantiates a new aPA filter.
	 * 
	 * @param adornmentNamesPresenter
	 *            the adornment names presenter
	 */
	public APAFilter(final AdornmentNamesPresenter adornmentNamesPresenter) {
		this.adornmentNamesPresenter = adornmentNamesPresenter;
	}

	/**
	 * filter apa and adapt list of acds if necessary.
	 * 
	 * @param oldApaPayload
	 *            the apapl
	 * @param oldApaDescriptor
	 *            the old ap a_desc
	 * @param output
	 *            the output
	 * @return the aPA payload
	 * @throws FilterException
	 *             the filter exception
	 */
	public APAPayload filterApa(final APAPayload oldApaPayload,
			final AlphaCardDescriptor oldApaDescriptor, final String output)
			throws FilterException {

		if (oldApaPayload == null)
			throw new FilterException();

		final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();
		final APAPayload newApaPayload = new APAPayload();

		// apaDesc contains all possible adornments types now, can be shortened
		// during process
		final AlphaCardDescriptor newApaDescriptor = aaf
				.cloneAlphaCardDescriptor(oldApaPayload
						.getContentCardAdornmentPrototype());

		final ArrayList<String> adornmentNames = this.getNewAdornmentNames(
				oldApaDescriptor, newApaDescriptor);

		final HashMap<String, Boolean> adornmentMap = (HashMap<String, Boolean>) this.adornmentNamesPresenter
				.createAdornmentMap(adornmentNames, output);

		this.applyApaFilter(newApaDescriptor, adornmentMap);

		newApaPayload.setContentCardAdornmentPrototype(newApaDescriptor);

		return newApaPayload;
	}

	/**
	 * Merge apa.
	 * 
	 * @param oldApaPayload
	 *            the old apa
	 * @param templateApaPayload
	 *            the template apa
	 * @param output
	 *            the output
	 * @return the aPA payload
	 * @throws FilterException
	 *             the filter exception
	 */
	public APAPayload mergeApa(final APAPayload oldApaPayload,
			final APAPayload templateApaPayload, final String output)
			throws FilterException {

		if ((oldApaPayload == null) || (templateApaPayload == null))
			throw new FilterException();

		// collect all new adornment types in a list, in order to update the old
		// descriptors later on
		this.updatedAdornments = new ArrayList<PrototypedAdornment>();

		final APAPayload unitedApaPayload = this.createUnitedApa(oldApaPayload,
				templateApaPayload);

		// method removes added adornment types from mergedAPA if chosen by user
		final APAPayload newApaPayload = this.filterApa(unitedApaPayload,
				oldApaPayload.getContentCardAdornmentPrototype(), output);

		this.createUpdatedAdornmentList(oldApaPayload, newApaPayload);

		return newApaPayload;
	}

	/**
	 * Apply apa filter.
	 * 
	 * @param newApaDescriptor
	 *            the new apa descriptor
	 * @param adornmentMap
	 *            the adornment map
	 * @throws FilterException
	 *             the filter exception
	 */
	private void applyApaFilter(final AlphaCardDescriptor newApaDescriptor,
			final HashMap<String, Boolean> adornmentMap) throws FilterException {
		if (adornmentMap != null) {

			for (final Map.Entry<String, Boolean> entry : adornmentMap
					.entrySet()) {

				// delete adornment type, if user does not want to use it
				if (!entry.getValue()) {
					newApaDescriptor.deleteAdornment(entry.getKey());
				}
			}
		} else {
			APAFilter.LOGGER.warning("Cannot filter APA!");
			throw new FilterException();
		}
	}

	/**
	 * Gets the new adornment names.
	 * 
	 * @param oldApaDescriptor
	 *            the old apa descriptor
	 * @param newApaDescriptor
	 *            the new apa descriptor
	 * @return the new adornment names
	 */
	private ArrayList<String> getNewAdornmentNames(
			final AlphaCardDescriptor oldApaDescriptor,
			final AlphaCardDescriptor newApaDescriptor) {

		// will be the list of adornments presented to the user
		final ArrayList<String> adornmentNames = new ArrayList<String>();

		for (final AdpAdornment aa : newApaDescriptor.readAdornments()) {
			if (!aa.getConsensusScope().equals(ConsensusScope.GENERIC_STD)) {

				// at merge import, if adornment has already been in apa, do not
				// present to user
				if ((oldApaDescriptor != null)
						&& (oldApaDescriptor.readAdornment(aa.getName()) != null)) {
					continue;
				}

				adornmentNames.add(aa.getName());
			}
		}
		return adornmentNames;
	}

	/**
	 * Creates the updated adornment list.
	 * 
	 * @param oldApaPayload
	 *            the old apa payload
	 * @param newApaPayload
	 *            the new apa payload
	 */
	private void createUpdatedAdornmentList(final APAPayload oldApaPayload,
			final APAPayload newApaPayload) {
		// add new adornments to existing cards, check if there are any new
		// adornments before
		if (newApaPayload.getContentCardAdornmentPrototype().readAdornments()
				.size() != oldApaPayload.getContentCardAdornmentPrototype()
				.readAdornments().size()) {

			for (final PrototypedAdornment pa : newApaPayload
					.getContentCardAdornmentPrototype().readAdornments()) {
				if (!pa.getConsensusScope().equals(ConsensusScope.GENERIC_STD)
						&& !oldApaPayload.getContentCardAdornmentPrototype()
								.readAdornments().contains(pa)) {
					this.updatedAdornments.add(pa);
				}
			}
		}
	}

	/**
	 * Creates the united apa.
	 * 
	 * @param oldApaPayload
	 *            the old apa payload
	 * @param templateApaPayload
	 *            the template apa payload
	 * @return the aPA payload
	 */
	private APAPayload createUnitedApa(final APAPayload oldApaPayload,
			final APAPayload templateApaPayload) {
		final APAPayload mergedApaPayload = new APAPayload();

		final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();
		final AlphaCardDescriptor oldApaDescriptor = aaf
				.cloneAlphaCardDescriptor(oldApaPayload
						.getContentCardAdornmentPrototype());

		final AlphaCardDescriptor templateApaDescriptor = aaf
				.cloneAlphaCardDescriptor(templateApaPayload
						.getContentCardAdornmentPrototype());

		for (final PrototypedAdornment template_pa : templateApaDescriptor
				.readAdornments()) {
			// if template adornment type is non generic, and not yet in apa,
			// add it to temporarily extended apa/apaDesc
			if (!template_pa.getConsensusScope().equals(
					ConsensusScope.GENERIC_STD)
					&& (oldApaDescriptor.readAdornment(template_pa.getName()) == null)) {
				final PrototypedAdornment pa = new PrototypedAdornment(
						template_pa.getName(), template_pa.getConsensusScope(),
						template_pa.getDataType());
				pa.setEnumRange(template_pa.getEnumRange());
				pa.setInstance(pa.isInstance());
				pa.setValue(template_pa.getValue());
				oldApaDescriptor.updateOrCreateAdornment(pa);
			}
		}

		mergedApaPayload.setContentCardAdornmentPrototype(oldApaDescriptor);
		return mergedApaPayload;
	}

	/**
	 * Gets the updated adornments.
	 * 
	 * @return the updated adornments
	 */
	public List<PrototypedAdornment> getUpdatedAdornments() {
		return this.updatedAdornments;
	}
}
