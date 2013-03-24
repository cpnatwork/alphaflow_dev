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
 * $Id$
 *************************************************************************/
package alpha.vvs.common;

import java.io.File;

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.CorpusGenericus;
import alpha.model.docenv.AlphaDocHomeDir;
import alpha.model.identification.AlphaCardID;

/**
 * FIXME: use Spring for injections.
 * 
 */
public class DescriptorToFileUtil {

	/** The alpha props facade. */
	private static AlphaPropsFacade alphaPropsFacade;

	/** The home dir. */
	private static AlphaDocHomeDir homeDir;

	/**
	 * This method should never be called except by Spring.
	 * 
	 * @param apf
	 *            the new alpha props facade
	 */
	// @Autowired(required = true)
	public void setAlphaPropsFacade(final AlphaPropsFacade apf) {
		DescriptorToFileUtil.alphaPropsFacade = apf;
	}

	/**
	 * Access alpha card.
	 * 
	 * @param aci
	 *            the aci
	 * @return the alpha card descriptor
	 */
	private static AlphaCardDescriptor accessAlphaCard(final AlphaCardID aci) {
		AlphaCardDescriptor aC;
		try {
			aC = DescriptorToFileUtil.alphaPropsFacade.getAlphaCard(aci);
		} catch (final Exception e) {
			throw new Error(e);
		}
		return aC;
	}

	/**
	 * Alphacard in vvs.
	 * 
	 * @param aci
	 *            the aci
	 * @return the file
	 */
	public static File alphacardInVVS(final AlphaCardID aci) {
		final AlphaCardDescriptor aCard = DescriptorToFileUtil
				.accessAlphaCard(aci);
		File acFile = DescriptorToFileUtil.alphacardFolderInVVS(aci, aCard);
		acFile = new File(acFile, "Payload."
				+ aCard.readAdornment(
						CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
						.getValue());
		return acFile;
	}

	/**
	 * Alphacard folder in vvs.
	 * 
	 * @param aci
	 *            the aci
	 * @return the file
	 */
	public static File alphacardFolderInVVS(final AlphaCardID aci) {
		return DescriptorToFileUtil.alphacardFolderInVVS(aci, null);
	}

	/**
	 * Alphacard folder in vvs.
	 * 
	 * @param aci
	 *            the aci
	 * @param aCardParam
	 *            the a card param
	 * @return the file
	 */
	private static File alphacardFolderInVVS(final AlphaCardID aci,
			final AlphaCardDescriptor aCardParam) {
		final AlphaCardDescriptor aCard = (aCardParam == null) ? DescriptorToFileUtil
				.accessAlphaCard(aci) : aCardParam;
		File acFile = null;
		acFile = new File(DescriptorToFileUtil.homeDir, aCard.getId()
				.getEpisodeID());
		acFile = new File(acFile, aCard.getId().getCardID());
		acFile = new File(acFile, aCard.readAdornment(
				CorpusGenericus.VERSION.value()).getValue());
		return acFile;
	}
}
