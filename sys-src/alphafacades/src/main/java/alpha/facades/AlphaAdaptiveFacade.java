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
package alpha.facades;

import alpha.model.AlphaCardDescriptor;
import alpha.model.adornment.PrototypedAdornment;

/**
 * This interface provides the basic functionality of the AlphaAdaptive module.
 * 
 */
public abstract interface AlphaAdaptiveFacade {

	/**
	 * This method clones an {@link PrototypedAdornment}.
	 * 
	 * @param seed
	 *            the {@link PrototypedAdornment} to clone
	 * @return the clone of the {@link PrototypedAdornment}
	 */
	public PrototypedAdornment cloneAdornment(PrototypedAdornment seed);

	/**
	 * This method clones an {@link AlphaCardDescriptor}.
	 * 
	 * @param seed
	 *            the {@link AlphaCardDescriptor} to clone
	 * @return the clone of the {@link AlphaCardDescriptor}
	 */
	public AlphaCardDescriptor cloneAlphaCardDescriptor(AlphaCardDescriptor seed);

	/**
	 * This method updates an {@link PrototypedAdornment} of an.
	 * 
	 * @param cardAdornment
	 *            the {@link PrototypedAdornment} to update
	 * @param apaAdornment
	 *            the APA {@link PrototypedAdornment} with the update info
	 * @param oldApaAdornment
	 *            the old APA {@link PrototypedAdornment}
	 * @return the clone of the {@link AlphaCardDescriptor}
	 *         {@link AlphaCardDescriptor}.
	 */
	public PrototypedAdornment updateAdornment(
			PrototypedAdornment cardAdornment,
			PrototypedAdornment apaAdornment,
			PrototypedAdornment oldApaAdornment);

	/**
	 * Gets the initial APA for Coordination {@link AlphaCardDescriptor}s.
	 * 
	 * @param episodeID
	 *            the episode id
	 * @param ocId
	 *            the oc id
	 * @return the initial APA for Coordination {@link AlphaCardDescriptor}s
	 */
	public AlphaCardDescriptor getInitialCoordinationCardAPA(String episodeID,
			String ocId);

	/**
	 * Gets the initial APA for Content {@link AlphaCardDescriptor}s.
	 * 
	 * @param episodeID
	 *            the episode id
	 * @param ocId
	 *            the oc id
	 * @return the the initial APA for Content {@link AlphaCardDescriptor}s
	 */
	public AlphaCardDescriptor getInitialContentCardAPA(String episodeID,
			String ocId);

}
