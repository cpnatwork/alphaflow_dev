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
import java.util.ArrayList;
import java.util.Arrays;

import org.hydra.core.Artifact;
import org.hydra.core.Container;
import org.hydra.core.Element;
import org.hydra.core.InvalidElementException;
import org.hydra.core.LogicalUnit;
import org.hydra.core.Stage;
import org.hydra.core.State;

import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;

/**
 * Implemenation of the History Investigator and Manipulator interfaces, based
 * on the Hydra Version Control System, for the recording and manipulating the
 * evoutionary aspects of each of the alpha-Cards independently. Key concepts
 * are the state validity property and independent evolution of each alpha-Card.
 * 
 * @author Scott A. Hady
 * @version 1.0
 * @since 1.0
 */
public class HistorianImpl extends Historian {

	/** The stage. */
	private Stage stage;

	/**
	 * Instantiates a new historian impl.
	 * 
	 * @param workspace
	 *            the workspace
	 */
	public HistorianImpl(final File workspace) {
		try {
			this.stage = new Stage(workspace);
		} catch (final InvalidElementException e) {
			this.logException(e);
		}
	}

	/**
	 * HISTORYINVESTIGATOR IMPLEMENTATION *************************************.
	 * 
	 * @return the string[]
	 */

	/**
	 * List the alpha-Cards currently being tracked in the system.
	 * 
	 * @return alphaCardList - String[].
	 */
	@Override
	public String[] listTrackedAlphaCards() {
		final ArrayList<String> cardNames = new ArrayList<String>();
		for (final LogicalUnit lu : this.stage.listManaged()) {
			cardNames.add(lu.getName());
		}
		return cardNames.toArray(new String[cardNames.size()]);
	}

	/**
	 * Determine if the system is tracking the alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor
	 * @return isTracking - boolean.
	 */
	@Override
	public boolean isTrackingAlphaCard(final AlphaCardDescriptor acd) {
		return Arrays.asList(this.listTrackedAlphaCards()).contains(
				this.getLogicalUnitName(acd));
	}

	/**
	 * Return the commit log for a designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param systemLog
	 *            the system log
	 * @return historyLog - String.
	 */
	@Override
	public String getHistoryLog(final AlphaCardDescriptor acd,
			final boolean systemLog) {
		return this.getLogicalUnit(acd).getHistoryCrawler()
				.getHistoryLog(systemLog);
	}

	/**
	 * List the names of the items currently being tracked as part of a
	 * designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return cardItens - String[].
	 */
	@Override
	public String[] listAlphaCardItems(final AlphaCardDescriptor acd) {
		final ArrayList<String> itemNames = new ArrayList<String>();
		itemNames.addAll(Arrays.asList(this.getContainedItems(this
				.getLogicalUnit(acd).getContents())));
		return itemNames.toArray(new String[itemNames.size()]);

	}

	/**
	 * Gets the contained items.
	 * 
	 * @param container
	 *            the container
	 * @return the contained items
	 */
	private String[] getContainedItems(final Container container) {
		final ArrayList<String> subItemNames = new ArrayList<String>();
		for (final Element e : container.listElements()) {
			if (e instanceof Artifact) {
				subItemNames.add(e.getName());
			} else {
				subItemNames.addAll(Arrays.asList(this
						.getContainedItems((Container) e)));
			}
		}
		return subItemNames.toArray(new String[subItemNames.size()]);
	}

	/**
	 * Return the last committed version for the designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return headVersion - Version.
	 */
	@Override
	public Version getHeadVersion(final AlphaCardDescriptor acd) {
		Version version = null;
		try {
			version = new VersionImpl(this.getLogicalUnit(acd).getHeadHash(),
					acd, this.stage);
		} catch (final Exception e) {
			this.logException(e);
		}
		return version;
	}

	/**
	 * Return the last version reverted to or committed for the designated
	 * alpha-Card, which represents the current state of the workspace.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return currentVersion - Version.
	 */
	@Override
	public Version getCurrentVersion(final AlphaCardDescriptor acd) {
		Version version = null;
		try {
			version = new VersionImpl(
					this.getLogicalUnit(acd).getCurrentHash(), acd, this.stage);
		} catch (final Exception e) {
			this.logException(e);
		}
		return version;
	}

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
	@Override
	public Version findVersionByVersionId(final AlphaCardDescriptor acd,
			final String versionId) {
		Version version = this.getHeadVersion(acd);
		while ((version != null) && !version.getId().equals(versionId)) {
			version = version.findPreviousSystemVersion();
		}
		return version;
	}

	/**
	 * Return the version that represents the initial commit of the deignated
	 * alpha-Card's descriptor.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return initialCommittedVersion - Version.
	 */
	@Override
	public Version findVersionWithInitialDescriptor(
			final AlphaCardDescriptor acd) {
		Version version = null;
		try {
			version = new VersionImpl(this.getLogicalUnit(acd).getHeadHash(),
					acd, this.stage);
			while (this.previousHasDescriptor(version)) {
				version = version.findPreviousSystemVersion();
			}
		} catch (final Exception e) {
			this.logException(e);
		}
		return version;
	}

	/**
	 * Previous has descriptor.
	 * 
	 * @param version
	 *            the version
	 * @return true, if successful
	 */
	private boolean previousHasDescriptor(final Version version) {
		final Version prev = version.findPreviousSystemVersion();
		if ((prev != null) && (prev.retrieveDescriptor() != null))
			return true;
		else
			return false;
	}

	/**
	 * Return the version that represents the initial commit of the deignated
	 * alpha-Card's payload.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return initialCommittedVersion - Version.
	 */
	@Override
	public Version findVersionWithInitialPayload(final AlphaCardDescriptor acd) {
		Version version = null;
		try {
			version = new VersionImpl(this.getLogicalUnit(acd).getHeadHash(),
					acd, this.stage);
			while (this.previousHasPayload(version)) {
				version = version.findPreviousSystemVersion();
			}
		} catch (final Exception e) {
			this.logException(e);
		}
		return version;
	}

	/**
	 * Previous has payload.
	 * 
	 * @param version
	 *            the version
	 * @return true, if successful
	 */
	private boolean previousHasPayload(final Version version) {
		final Version prev = version.findPreviousSystemVersion();
		if ((prev != null) && (prev.retrievePayload() != null))
			return true;
		else
			return false;
	}

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
	@Override
	public AlphaCardDescriptor retrieveDesciptorByVersionId(
			final AlphaCardDescriptor acd, final String versionId) {
		final Version version = this.findVersionByVersionId(acd, versionId);
		if (version != null)
			return version.retrieveDescriptor();
		else
			return null;
	}

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
	@Override
	public Payload retrievePayloadByVersionId(final AlphaCardDescriptor acd,
			final String versionId) {
		final Version version = this.findVersionByVersionId(acd, versionId);
		if (version != null)
			return version.retrievePayload();
		else
			return null;
	}

	/**
	 * HISTORYMANIPULATOR IMPLEMENTAION ***************************************.
	 * 
	 * @param acd
	 *            the acd
	 * @return true, if successful
	 */

	/**
	 * Track the evolution of the designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return success - boolean.
	 */
	@Override
	public boolean trackAlphaCard(final AlphaCardDescriptor acd) {
		boolean success = true;
		if (this.getLogicalUnit(acd) == null) {
			try {
				success = (this.stage.createLogicalUnit(this
						.getLogicalUnitName(acd)) != null);
			} catch (final Exception e) {
				success = false;
				this.logException(e);
			}
		}
		return success;
	}

	/**
	 * Ignore the evolution of the designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return success - boolean.
	 */
	@Override
	public boolean ignoreAlphaCard(final AlphaCardDescriptor acd) {
		// Not Supported in this Version.
		return false;
	}

	/**
	 * Add an item to be tracked to a designated alpha-Card.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param item
	 *            File.
	 * @return success - boolean.
	 */
	@Override
	public boolean addAlphaCardItem(final AlphaCardDescriptor acd,
			final File item) {
		boolean success = true;
		final LogicalUnit lu = this.getLogicalUnit(acd);
		try {
			success = lu.getContents().addElement(new Artifact(item));
		} catch (final Exception e) {
			success = false;
			this.logException(e);
		}
		return success;
	}

	/**
	 * Remove an item to being tracked from a designated alpha-Card. This only
	 * removes the item from tracking. It must be removed from the workspace by
	 * another method if desired.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param item
	 *            File.
	 * @return success - boolean.
	 */
	@Override
	public boolean removeAlphaCardItem(final AlphaCardDescriptor acd,
			final File item) {
		boolean success = true;
		final LogicalUnit lu = this.getLogicalUnit(acd);
		try {
			success = lu.getContents().removeElement(new Artifact(item));
		} catch (final Exception e) {
			success = false;
			this.logException(e);
		}
		return success;
	}

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
	@Override
	public Version commit(final AlphaCardDescriptor acd,
			final String committerId, final String message)
			throws VerVarStoreException {
		final String versionId = this.getLogicalUnit(acd).commit(committerId,
				message);
		this.refreshLogicalUnit(acd);
		return new VersionImpl(versionId, acd, this.stage);
	}

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
	@Override
	public Version commitTemporary(final AlphaCardDescriptor acd,
			final String committerId, final String message)
			throws VerVarStoreException {
		final String versionId = this.getLogicalUnit(acd).commitTemporary(
				committerId, message);
		this.refreshLogicalUnit(acd);
		return new VersionImpl(versionId, acd, this.stage);
	}

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
	@Override
	public Version commitValidPath(final AlphaCardDescriptor acd,
			final String committerId, final String message,
			final Version prevValidVersion) throws VerVarStoreException {
		try {
			final Version oldHeadVersion = this.getHeadVersion(acd);
			final Version newHeadVersion = this.commit(acd, committerId,
					message);
			this.setPredecessors(newHeadVersion, prevValidVersion,
					oldHeadVersion);
			this.getLogicalUnit(acd).recordReferences();
			this.refreshLogicalUnit(acd);
			return newHeadVersion;
		} catch (final Exception e) {
			this.logException(e);
			return null;
		}
	}

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
	 *            the prev valid version
	 * @return committedTemporaryVersion - Version.
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	@Override
	public Version commitValidPathTemporary(final AlphaCardDescriptor acd,
			final String committerId, final String message,
			final Version prevValidVersion) throws VerVarStoreException {
		try {
			final Version oldHeadVersion = this.getHeadVersion(acd);
			final Version newHeadVersion = this.commitTemporary(acd,
					committerId, message);
			this.setPredecessors(newHeadVersion, prevValidVersion,
					oldHeadVersion);
			this.refreshLogicalUnit(acd);
			return newHeadVersion;
		} catch (final Exception e) {
			this.logException(e);
			return null;
		}
	}

	/**
	 * Insert the alpha-Card's current workspace state into the history before
	 * the next version. This may or may not result in the version being
	 * maintained on the valid path.
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
	@Override
	public Version commitBeforeOnSystemPath(final AlphaCardDescriptor acd,
			final String committerId, final String message,
			final Version nextVersion) throws VerVarStoreException {
		final LogicalUnit lu = this.getLogicalUnit(acd);
		final Version prevSystemVersion = nextVersion
				.findPreviousSystemVersion();
		final String versionId = lu.commitInsert(committerId, message,
				prevSystemVersion.getId(), nextVersion.getId());
		final Version insertedVersion = new VersionImpl(versionId, acd,
				this.stage);
		this.setPredecessors(insertedVersion, prevSystemVersion,
				prevSystemVersion);
		this.setPredecessors(nextVersion,
				nextVersion.findPreviousValidVersion(), insertedVersion);
		this.refreshLogicalUnit(acd);
		return insertedVersion;
	}

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
	@Override
	public Version commitBeforeOnValidPath(final AlphaCardDescriptor acd,
			final String committerId, final String message,
			final Version nextVersion) throws VerVarStoreException {
		final Version insertedVersion = this.commitBeforeOnSystemPath(acd,
				committerId, message, nextVersion);
		this.setPredecessors(insertedVersion,
				nextVersion.findPreviousValidVersion(),
				nextVersion.findPreviousSystemVersion());
		this.setPredecessors(nextVersion, insertedVersion, insertedVersion);
		this.refreshLogicalUnit(acd);
		return insertedVersion;
	}

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
	@Override
	public boolean revertToVersion(final AlphaCardDescriptor acd,
			final Version targetVersion) {
		return this.getLogicalUnit(acd).revert(targetVersion.getId());
	}

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
	@Override
	public Version updateCommittedVersion(final AlphaCardDescriptor acd,
			final String committerId, final String message,
			final Version updatedVersion) throws VerVarStoreException {
		final LogicalUnit lu = this.getLogicalUnit(acd);
		this.getLogicalUnit(acd).commitUpdate(updatedVersion.getId(),
				committerId, message);
		this.refreshLogicalUnit(acd);
		return updatedVersion;
	}

	/**
	 * HISTORIANIMPL PRIVATE METHODS ***********************************.
	 * 
	 * @param target
	 *            the target
	 * @param valid
	 *            the valid
	 * @param system
	 *            the system
	 */

	private void setPredecessors(final Version target, final Version valid,
			final Version system) {
		final State targetState = ((VersionImpl) target).getState();
		final State validState = ((VersionImpl) valid).getState();
		final State systemState = ((VersionImpl) system).getState();
		for (final State prevState : targetState.listPrevious()) {
			targetState.removePrevious(prevState);
		}
		targetState.addPrevious(validState);
		if (!validState.getHash().equals(systemState.getHash())) {
			targetState.addPrevious(systemState);
		}

		targetState.store();
		validState.store();
		systemState.store();
	}

	/**
	 * Gets the logical unit.
	 * 
	 * @param acd
	 *            the acd
	 * @return the logical unit
	 */
	private LogicalUnit getLogicalUnit(final AlphaCardDescriptor acd) {
		return this.stage.getLogicalUnit(this.getLogicalUnitName(acd));
	}

	/**
	 * Refresh logical unit.
	 * 
	 * @param acd
	 *            the acd
	 */
	private void refreshLogicalUnit(final AlphaCardDescriptor acd) {
		this.getLogicalUnit(acd).recordReferences();
		try {
			this.getLogicalUnit(acd).loadReferences();
		} catch (final InvalidElementException e) {
			this.logException(e);
		}
	}

	/**
	 * Gets the logical unit name.
	 * 
	 * @param acd
	 *            the acd
	 * @return the logical unit name
	 */
	private String getLogicalUnitName(final AlphaCardDescriptor acd) {
		return acd.getId().getCardID() + "x";
	}

	/**
	 * Log exception.
	 * 
	 * @param e
	 *            the e
	 */
	private void logException(final Exception e) {
		e.printStackTrace();
	}
}
