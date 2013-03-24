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

import java.io.File;

import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.AlphaCardDescriptor;

/**
 * Provides an interface for recording and managing which state of the each
 * alpha-Card's evolution is the current method. The actual method of managing
 * the histories and each states properties is left to the implementation based
 * on the underlying version control subsystem.
 * 
 * @author Scott A. Hady
 * @version 1.0
 * @since 1.0
 */
public interface HistoryManipulator {

	/**
	 * Track the evolution of the designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return success - boolean.
	 */
	public boolean trackAlphaCard(AlphaCardDescriptor acd);

	/**
	 * Ignore the evolution of the designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return success - boolean.
	 */
	public boolean ignoreAlphaCard(AlphaCardDescriptor acd);

	/**
	 * Add an item to be tracked to a designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param item
	 *            File.
	 * @return success - boolean.
	 */
	public boolean addAlphaCardItem(AlphaCardDescriptor acd, File item);

	/**
	 * Remove an item to being tracked from a designated alpha-Card. This does
	 * not remove the item from the workspace, only from the alpha-Cards'
	 * tracking functionality.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param item
	 *            File.
	 * @return success - boolean.
	 */
	public boolean removeAlphaCardItem(AlphaCardDescriptor acd, File item);

	/**
	 * Commit the alpha-Card's current workspace state. The current version
	 * maintained being employed will be used as the version's valid path
	 * predecessor and the head will be used as the version's system path
	 * predecessor.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param committerId
	 *            String.
	 * @param message
	 *            String.
	 * @return committedVersion - Version.
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public Version commit(AlphaCardDescriptor acd, String committerId,
			String message) throws VerVarStoreException;

	/**
	 * Commit a temporary version, expected to be later updated with content.
	 * Path semantics are the same as a regular commit.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param committerId
	 *            String.
	 * @param message
	 *            String.
	 * @return committedTemporaryVersion - Version.
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public Version commitTemporary(AlphaCardDescriptor acd, String committerId,
			String message) throws VerVarStoreException;

	/**
	 * Commit the alpha-Card's current workspace state and specify the
	 * designated version as the valid path predecessor, while the head will be
	 * used as the version's system path predecessor.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param committerId
	 *            String.
	 * @param message
	 *            String.
	 * @param prevValidVersion
	 *            Version.
	 * @return committedVersion - Version.
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public Version commitValidPath(AlphaCardDescriptor acd, String committerId,
			String message, Version prevValidVersion)
			throws VerVarStoreException;

	/**
	 * Commit a temporary version, expected to be later updated with content,
	 * and ensure that this temporary version lies along the valid path.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param committerId
	 *            String.
	 * @param message
	 *            String.
	 * @param prevValidVersion
	 *            Version.
	 * @return committedTemporaryVersion - Version.
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public Version commitValidPathTemporary(AlphaCardDescriptor acd,
			String committerId, String message, Version prevValidVersion)
			throws VerVarStoreException;

	/**
	 * Insert the alpha-Card's current workspace state into the history before
	 * the next version along the system path. This may or may not result in the
	 * version being maintained on the valid path.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param committerId
	 *            String.
	 * @param message
	 *            String.
	 * @param nextVersion
	 *            Version.
	 * @return committedVersion - Version.
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public Version commitBeforeOnSystemPath(AlphaCardDescriptor acd,
			String committerId, String message, Version nextVersion)
			throws VerVarStoreException;

	/**
	 * Insert the alpha-Card's current workspace state into the history before
	 * the next version. The next version's valid path will be updated to
	 * reflect this inserted state to be its predecessory valid state.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param committerId
	 *            String.
	 * @param message
	 *            String.
	 * @param nextVersion
	 *            Version.
	 * @return committedVersion - Version.
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public Version commitBeforeOnValidPath(AlphaCardDescriptor acd,
			String committerId, String message, Version nextVersion)
			throws VerVarStoreException;

	/**
	 * Revert the designated alpha-Card's workspace to reflect that described in
	 * designated version.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param targetVersion
	 *            Version.
	 * @return success - boolean.
	 */
	public boolean revertToVersion(AlphaCardDescriptor acd,
			Version targetVersion);

	/**
	 * Update the designated version's content to reflect the current
	 * alpha-Card's workspace.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param committerId
	 *            String.
	 * @param message
	 *            String.
	 * @param updatedVersion
	 *            Version.
	 * @return updatedVersion - Version.
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public Version updateCommittedVersion(AlphaCardDescriptor acd,
			String committerId, String message, Version updatedVersion)
			throws VerVarStoreException;

}
