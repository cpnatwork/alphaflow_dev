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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipInputStream;

import org.hydra.core.LogicalUnit;
import org.hydra.core.Stage;
import org.hydra.core.State;

import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.vvs.common.FileSystemStorageUtil;

/**
 * Implementation of the Version Interface based on the Hydra VCS
 * implementation. Which provides the intergative abilities for exploring a
 * committed history and querying the various aspects of each stored state or
 * version necessary to support alpha-flow's versioning needs.
 * 
 * @author Scott A. Hady
 * @version 1.0
 * @since 1.0
 */
public class VersionImpl implements Version {

	/** The state. */
	private State state;

	/** The stage. */
	private final Stage stage;

	/** The acd. */
	private final AlphaCardDescriptor acd;

	/**
	 * Specialized constructor which creates the version as a proxy of the
	 * designated state.
	 * 
	 * @param versionId
	 *            String.
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param stage
	 *            Stage.
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public VersionImpl(final String versionId, final AlphaCardDescriptor acd,
			final Stage stage) throws VerVarStoreException {
		this.acd = acd;
		this.stage = stage;
		try {
			final LogicalUnit lu = stage.getLogicalUnit(acd.getId().getCardID()
					+ "x");
			this.state = lu.getHistoryCrawler().findCommitHash(lu.getHead(),
					versionId, null, true);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new VerVarStoreException("Unable to Initialize Version ["
					+ versionId + "].", e);
		}
	}

	/**
	 * Return the alpha-Card's Identification String.
	 * 
	 * @return acid - String.
	 */
	@Override
	public String getCardID() {
		return this.acd.getId().getCardID();
	}

	/**
	 * Returns the unique identifier for this version, which is equivalent to
	 * the state's hash id.
	 * 
	 * @return versionId - String.
	 */
	@Override
	public String getId() {
		return this.state.getHash();
	}

	/**
	 * Returns the creational time of this version.
	 * 
	 * @return creationalTimestamp - String.
	 */
	@Override
	public String getTimestamp() {
		return this.state.getTimestamp().toString();
	}

	/**
	 * Returns the committer responsible for creating the version.
	 * 
	 * @return committer - String.
	 */
	@Override
	public String getCommitter() {
		return this.state.getUserId();
	}

	/**
	 * Return the message associated with this version.
	 * 
	 * @return commitMessage - String.
	 */
	@Override
	public String getMessage() {
		return this.state.getMessage();
	}

	/**
	 * Return this version's position in the system path, counting upwards from
	 * the initial commit. This count includes all previous, both valid and
	 * invalid, versions.
	 * 
	 * @return systemPathPosition - int.
	 */
	@Override
	public int getSystemPathPosition() {
		int position = 1;
		Version ptr = this;
		while ((ptr = ptr.findPreviousSystemVersion()) != null) {
			position++;
		}
		return position;
	}

	/**
	 * Return the version's position in the valid path, counting upwards from
	 * the initial commit. This count only includes the previous valid commits.
	 * 
	 * @return validPathPosition - int.
	 */
	@Override
	public int getValidPathPosition() {
		int position = 1;
		Version ptr = this;
		while ((ptr = ptr.findPreviousValidVersion()) != null) {
			position++;
		}
		return position;
	}

	/**
	 * Return the alpha-Card's Descriptor as reflecting in this version's state.
	 * 
	 * @return descriptor - AlphaCardDescriptor.
	 */
	@Override
	public AlphaCardDescriptor retrieveDescriptor() {
		AlphaCardDescriptor acdVersion = null;
		BufferedInputStream acdBIS = null;
		try {
			final String acdPath = WorkspaceImpl.getDescriptorPath(this.acd);
			final File acdRepositoryFile = this.state.cloneContents()
					.getElement(acdPath).cloneRepositoryFile();
			final ZipInputStream zis = new ZipInputStream(new FileInputStream(
					acdRepositoryFile));
			zis.getNextEntry();
			acdBIS = new BufferedInputStream(zis);
			acdVersion = FileSystemStorageUtil.loadDescriptorFromStream(
					this.acd, acdBIS);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (acdBIS != null) {
				try {
					acdBIS.close();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		return acdVersion;
	}

	/**
	 * Return the alpha-Card's Payload as reflected in this version's state.
	 * 
	 * @return payload - Payload.
	 */
	@Override
	public Payload retrievePayload() {
		Payload payload = null;
		BufferedInputStream payloadBIS = null;
		try {
			final String payloadPath = WorkspaceImpl.getPayloadPath(this.acd);
			final File payloadRepositoryFile = this.state.cloneContents()
					.getElement(payloadPath).cloneRepositoryFile();
			final ZipInputStream zis = new ZipInputStream(new FileInputStream(
					payloadRepositoryFile));
			zis.getNextEntry();
			payloadBIS = new BufferedInputStream(zis);
			payload = FileSystemStorageUtil.loadPayloadFromStream(
					this.retrieveDescriptor(), payload, payloadBIS,
					payloadRepositoryFile.length());
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (payloadBIS != null) {
				try {
					payloadBIS.close();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		return payload;
	}

	/**
	 * Returns the previous verison along the system path.
	 * 
	 * @return previousSystemPathVersion - Version.
	 */
	@Override
	public Version findPreviousSystemVersion() {
		Version prevSystemVersion = null;
		final State[] prevStates = this.state.listPrevious();
		if (prevStates.length > 0) {
			try {
				if (prevStates.length == 1) {
					prevSystemVersion = new VersionImpl(
							prevStates[0].getHash(), this.acd, this.stage);
				} else {
					prevSystemVersion = new VersionImpl(
							prevStates[1].getHash(), this.acd, this.stage);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return prevSystemVersion;
	}

	/**
	 * Returns the previous verison along the valid path.
	 * 
	 * @return previousValidPathVersion - Version.
	 */
	@Override
	public Version findPreviousValidVersion() {
		Version prevValidVersion = null;
		final State[] prevStates = this.state.listPrevious();
		if (prevStates.length > 0) {
			try {
				prevValidVersion = new VersionImpl(prevStates[0].getHash(),
						this.acd, this.stage);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return prevValidVersion;
	}

	/**
	 * Returns the previous temporary verison.
	 * 
	 * @return previousTemporaryVersion - Version.
	 */
	@Override
	public Version findPreviousTemporaryVersion() {
		final Version prevTemporaryVersion = null;
		Version ptr = this;
		do {
			ptr = ptr.findPreviousSystemVersion();
		} while ((ptr != null) && !ptr.isTemporaryVersion());
		return prevTemporaryVersion;
	}

	/**
	 * Determine if this is the initial commit or there is no previous commits.
	 * 
	 * @return isInitialCommit - boolean.
	 */
	@Override
	public boolean isInitialVersion() {
		return (this.state.listPrevious().length == 0);
	}

	/**
	 * Determines if this version represents the reconciliation of a global
	 * conflict.
	 * 
	 * @return isReconciliation - boolean.
	 */
	@Override
	public boolean isReconciledVersion() {
		return (this.state.listPrevious().length > 1);
	}

	/**
	 * Determine if this is represents a temporary version, expected to be
	 * updated later.
	 * 
	 * @return isTemporaryCommit - boolean.
	 */
	@Override
	public boolean isTemporaryVersion() {
		return this.state.isTemporary();
	}

	/**
	 * Determine if this is a commit along the valid path.
	 * 
	 * @return isValidVersion - boolean.
	 */
	@Override
	public boolean isValidVersion() {
		boolean isValid = false;
		State ptr = this.stage.getLogicalUnit(this.acd.getId().getCardID())
				.getHead();
		while (ptr != null) {
			if (ptr.getHash().equals(this.getId())) {
				isValid = true;
				break;
			}
			ptr = ptr.listPrevious()[0];
		}
		return isValid;
	}

	/**
	 * VERSIONIMPL (PROTECTED) METHODS
	 * *****************************************.
	 * 
	 * @return the stage
	 */

	/**
	 * Return the stage used for versioning.
	 * 
	 * @return stage - Stage.
	 */
	protected Stage getStage() {
		return this.stage;
	}

	/**
	 * Return the logical unit representing the versioning for the alpha-Card.
	 * 
	 * @return logicalUnit - LogicalUnit.
	 */
	protected LogicalUnit getLogicalUnit() {
		return this.stage.getLogicalUnit(this.acd.getId().getCardID());
	}

	/**
	 * Return the specific state proxied by this version.
	 * 
	 * @return state - State.
	 */
	protected State getState() {
		return this.state;
	}

}
