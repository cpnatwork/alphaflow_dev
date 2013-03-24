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
 * $Id: AdornmentChecker.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.editor;

import java.util.Map;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.CorpusGenericus;
import alpha.model.identification.AlphaCardID;

/**
 * The Class AdornmentChecker.
 * 
 */
@Service
public class AdornmentChecker {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AdornmentChecker.class.getName());

	/** The apf. */
	@Autowired
	private AlphaPropsFacade apf;

	/**
	 * This method checks if the current user (AlphadocConfig) is the owner of
	 * the AlphaCardDescriptor described by the parameter aci.
	 * 
	 * @param aci
	 *            the aci
	 * @param user
	 *            the user
	 * @return true, if is alpha card owner
	 */
	public boolean isAlphaCardOwner(final AlphaCardID aci, final String user) {
		try {
			boolean result = false;

			if (aci == null)
				return result;

			final AlphaCardDescriptor ac = this.apf.getAlphaCard(aci);
			if (ac == null)
				return result;
			final String actor = ac
					.readAdornment(CorpusGenericus.ACTOR.value()).getValue();
			AdornmentChecker.LOGGER.finer("AlphaCard Owner '" + actor
					+ "', current user is '" + user + "'.");
			if (actor.substring(1).equals(user)) {
				result = true;
			}

			return result;
		} catch (final Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method checks if a certain Adornment of an AlphaCardDescriptor is
	 * changeable.
	 * 
	 * @param aci
	 *            The AlphaCardDescriptor
	 * @param name
	 *            The name of the Adornment
	 * @return true, if is adornment changeable
	 */
	public boolean isAdornmentChangeable(final AlphaCardID aci,
			final String name) {
		final Map<String, Boolean> adaptiveChangeability = this.apf
				.getAlphaCardChangeability(aci);
		AdornmentChecker.LOGGER.finer("Changeability map is: "
				+ adaptiveChangeability.toString());

		final Boolean change = adaptiveChangeability.get(name);
		AdornmentChecker.LOGGER.finer("Changeability entry for adornment '"
				+ name + "' is '" + change + "'");
		if (change != null)
			return change;
		return false;
	}

}
