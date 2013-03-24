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
 * Interface which provides the intergative abilities for exploring a committed
 * history and querying the various aspects of each stored state or version
 * necessary to support alpha-flow's versioning needs.
 * 
 * @author Scott A. Hady
 * @version 1.0
 * @since 1.0
 */
public interface Version {

	/**
	 * Return the alpha-Card's Identification String.
	 * 
	 * @return acid - String.
	 */
	public String getCardID();

	/**
	 * Return the version's unique idenfication.
	 * 
	 * @return versionId - String.
	 */
	public String getId();

	/**
	 * Return the timestamp indicating when the version was created.
	 * 
	 * @return versionCreationTimeStamp - String.
	 */
	public String getTimestamp();

	/**
	 * Return the party responsible for committing the version.
	 * 
	 * @return committerId - String.
	 */
	public String getCommitter();

	/**
	 * Return the message associated with the version.
	 * 
	 * @return commitMessage - String.
	 */
	public String getMessage();

	/**
	 * Return the version's position along the logical system path.
	 * 
	 * @return systemPathPostion - int.
	 */
	public int getSystemPathPosition();

	/**
	 * Return the version's position along the logical valid path.
	 * 
	 * @return validPathPosition - int.
	 */
	public int getValidPathPosition();

	/**
	 * Return the alpha-Card descriptor state reflected in the version.
	 * 
	 * @return descriptor - AlphaCardDescriptor.
	 */
	public AlphaCardDescriptor retrieveDescriptor();

	/**
	 * Return the alpha-Card payload state reflected in the version.
	 * 
	 * @return payload - Payload.
	 */
	public Payload retrievePayload();

	/**
	 * Find the previous version along the system path.
	 * 
	 * @return prevSystemPathVersion - Version.
	 */
	public Version findPreviousSystemVersion();

	/**
	 * Find the previous version along the valid path.
	 * 
	 * @return prevValidPathVersion - Version.
	 */
	public Version findPreviousValidVersion();

	/**
	 * Find the previous temporary version along the system path.
	 * 
	 * @return prevTemporaryVersion - Version.
	 */
	public Version findPreviousTemporaryVersion();

	/**
	 * Determine if this version is the first committed version.
	 * 
	 * @return initialCommitted - boolean.
	 */
	public boolean isInitialVersion();

	/**
	 * Determines if this version represents the reconciliation of a global
	 * conflict.
	 * 
	 * @return isReconciliation - boolean.
	 */
	public boolean isReconciledVersion();

	/**
	 * Determine if the version is a temporary version.
	 * 
	 * @return isTemporary - boolean.
	 */
	public boolean isTemporaryVersion();

	/**
	 * Determine if the version is considered a valid version.
	 * 
	 * @return isValid - boolean.
	 */
	public boolean isValidVersion();

}
