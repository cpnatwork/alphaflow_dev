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

import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.CorpusGenericus;
import alpha.model.identification.AlphaCardID;
import alpha.vvs.common.FileSystemStorageUtil;

/**
 * Implementation of the workspace interface using a basic file system
 * infrastructure. Within the designated home directory, each alpha-Card will
 * have a sub-directory. Each alpha-Card directory contains two sub-directories,
 * one for the card's descriptor and one for the payload. The card's actual
 * descriptor and payload files will be found in their respective directories.
 * 
 * @author Scott A. Hady
 * @version 1.0
 * @since 1.0
 */
public class WorkspaceImpl implements Workspace {

	/** The workspace. */
	private final File workspace;

	/**
	 * Specialized constructor, which manages a workspace within the designated
	 * directory.
	 * 
	 * @param workspaceHome
	 *            File.
	 */
	public WorkspaceImpl(final File workspaceHome) {
		this.workspace = workspaceHome;
	}

	/**
	 * Return the workspace's home address.
	 * 
	 * @return home - File.
	 */
	@Override
	public File getHome() {
		return this.workspace;
	}

	/**
	 * Return a file handle for the designated alpha-Card's directory.
	 * 
	 * @param aci
	 *            AlphaCardID.
	 * @return alphaCardDirectory - File.
	 */
	public File getAlphaCardDirectory(final AlphaCardID aci) {
		return new File(this.workspace, aci.getCardID());
	}

	/**
	 * Return the subpath to the descriptor's file.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return descriptorPath - String.
	 */
	public static String getDescriptorPath(final AlphaCardDescriptor acd) {
		final String SEP = File.separator;
		return acd.getId().getCardID() + SEP + "desc" + SEP + "Descriptor.xml";
	}

	/**
	 * Return a file handle for the designated alpha-Card's descriptor file
	 * within the workspace.
	 * 
	 * @param acd
	 *            the acd
	 * @return descriptorFile - File.
	 */
	@Override
	public File getDescriptorFile(final AlphaCardDescriptor acd) {
		return new File(this.workspace, WorkspaceImpl.getDescriptorPath(acd));
	}

	/**
	 * Return the subpath to the payload's file.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return payloadSubPath - String.
	 */
	public static String getPayloadPath(final AlphaCardDescriptor acd) {
		final String SEP = File.separator;
		final String fileExtension = acd.readAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value()).getValue();
		return acd.getId().getCardID() + SEP + "pl" + SEP + "Payload."
				+ fileExtension;
	}

	/**
	 * Return a file handle for the designated alpha-Card's payload file within
	 * the workspace.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return descriptorFile - File.
	 */
	@Override
	public File getPayloadFile(final AlphaCardDescriptor acd) {
		return new File(this.workspace, WorkspaceImpl.getPayloadPath(acd));
	}

	/**
	 * Load the most current version of the designated alpha-Card's descriptor
	 * in the workspace.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return passThroughACD - AlphaCardDescriptor.
	 */
	@Override
	public AlphaCardDescriptor loadDescriptor(final AlphaCardDescriptor acd) {
		return FileSystemStorageUtil.loadDescriptorFromFile(acd,
				this.getDescriptorFile(acd));
	}

	/**
	 * Load the most current version of the designated alpha-Card's payload in
	 * the workspace.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param payload
	 *            Payload.
	 * @return passThroughPayload - Payload.
	 */
	@Override
	public Payload loadPayload(final AlphaCardDescriptor acd, Payload payload) {
		payload = FileSystemStorageUtil.loadPayloadFromFile(acd, payload,
				this.getPayloadFile(acd));
		return payload;
	}

	/**
	 * Store the designated alpha-Card descriptor as the current version within
	 * the workspace. Notice, this does not automatically create a maintained
	 * version within the version control system.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return success - boolean.
	 */
	@Override
	public boolean storeDescriptor(final AlphaCardDescriptor acd) {
		return FileSystemStorageUtil.storeDescriptorToFile(acd,
				this.getDescriptorFile(acd));
	}

	/**
	 * Store the designated alpha-Card payload as the current version within the
	 * workspace. Notice, this does not automatically create a maintained
	 * version within the version control system.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param payload
	 *            Payload.
	 * @return success - boolean.
	 */
	@Override
	public boolean storePayload(final AlphaCardDescriptor acd,
			final Payload payload) {
		return FileSystemStorageUtil.storePayloadToFile(acd, payload,
				this.getPayloadFile(acd));
	}

}
