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
package alpha.vvs;

import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;

/**
 * Provides an interface for querying the various apsects of the each
 * alpha-Card's evolution. The actual method of representing the histories of
 * the alpha-Cards is left to the implementation based on the underlying version
 * control subsystem.
 * 
 * @author Scott A. Hady
 * @version 1.0
 * @since 1.0
 */
public interface HistoryInvestigator {

	/**
	 * List the names of the alpha-Cards currently being tracked in the system.
	 * 
	 * @return alphaCardList - String[].
	 */
	public String[] listTrackedAlphaCards();

	/**
	 * List the names of the items currently being tracked as part of a
	 * designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return cardItems - String[].
	 */
	public String[] listAlphaCardItems(AlphaCardDescriptor acd);

	/**
	 * Determine if the system is tracking the alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor
	 * @return isTracking - boolean.
	 */
	public boolean isTrackingAlphaCard(AlphaCardDescriptor acd);

	/**
	 * Return the commit log for a designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param systemLog
	 *            the system log
	 * @return historyLog - String.
	 */
	public String getHistoryLog(AlphaCardDescriptor acd, boolean systemLog);

	/**
	 * Return the last committed version for the designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return headVersion - Version.
	 */
	public Version getHeadVersion(AlphaCardDescriptor acd);

	/**
	 * Return the last version reverted to or committed for the designated
	 * alpha-Card, which represents the current state of the workspace.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return currentVersion - Version.
	 */
	public Version getCurrentVersion(AlphaCardDescriptor acd);

	/**
	 * Return the version of designated alpha-Card with the specified version
	 * id.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param versionId
	 *            String.
	 * @return version - Version.
	 */
	public Version findVersionByVersionId(AlphaCardDescriptor acd,
			String versionId);

	/**
	 * Return the version that represents the initial commit of the deignated
	 * alpha-Card's descriptor.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return initialCommittedVersion - Version.
	 */
	public Version findVersionWithInitialDescriptor(AlphaCardDescriptor acd);

	/**
	 * Return the version that represents the initial commit of the deignated
	 * alpha-Card's payload.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return initialCommittedVersion - Version.
	 */
	public Version findVersionWithInitialPayload(AlphaCardDescriptor acd);

	/**
	 * Return the a descriptor reflecting the state persisted in the designated
	 * version.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param versionId
	 *            - String.
	 * @return acd - AlphaCardDescriptor.
	 */
	public AlphaCardDescriptor retrieveDesciptorByVersionId(
			AlphaCardDescriptor acd, String versionId);

	/**
	 * Return the a payload reflecting the state persisted in the designated
	 * version.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param versionId
	 *            - String.
	 * @return payload - Payload.
	 */
	public Payload retrievePayloadByVersionId(AlphaCardDescriptor acd,
			String versionId);

}
