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

/**
 * Provides interface for commonly accessing the current version of each
 * alpha-Card's descriptor and payload throughout the alpha-Flow system. This
 * provides a decoupling of the system from the underlying data storage
 * mechanism employed.
 * 
 * @author Scott A. Hady
 * @version 1.0
 * @since 1.0
 */
public interface Workspace {

	/**
	 * Return the workspace's home address.
	 * 
	 * @return home - File.
	 */
	public File getHome();

	/**
	 * Return a file handle for the designated alpha-Card's descriptor file
	 * within the workspace.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return descriptorFile - File.
	 */
	public File getDescriptorFile(AlphaCardDescriptor acd);

	/**
	 * Return a file handle for the designated alpha-Card's payload file within
	 * the workspace.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return descriptorFile - File.
	 */
	public File getPayloadFile(AlphaCardDescriptor acd);

	/**
	 * Load the most current version of the designated alpha-Card's descriptor.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return passThroughACD - AlphaCardDescriptor.
	 */
	public AlphaCardDescriptor loadDescriptor(AlphaCardDescriptor acd);

	/**
	 * Load the most current version of the designated alpha-Card's payload.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param payload
	 *            Payload.
	 * @return passThroughPayload - Payload.
	 */
	public Payload loadPayload(AlphaCardDescriptor acd, Payload payload);

	/**
	 * Store the designated alpha-Card descriptor as the current version within
	 * the workspace. Notice, this does not automatically create a maintained
	 * version within the version control system.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return success - boolean.
	 */
	public boolean storeDescriptor(AlphaCardDescriptor acd);

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
	public boolean storePayload(AlphaCardDescriptor acd, Payload payload);

}
