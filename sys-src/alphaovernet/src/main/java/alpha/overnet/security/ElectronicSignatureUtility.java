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
package alpha.overnet.security;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides generic methods for signing data and verifying electronic
 * signatures.
 */
public interface ElectronicSignatureUtility {

	/**
	 * Signs the data from the supplied {@link InputStream} using the supplied
	 * key information. Puts the result onto the supplied {@link OutputStream}.
	 * 
	 * @param outputStream
	 *            the output stream
	 * @param inputStream
	 *            the input stream
	 * @param keyInfo
	 *            the key information used for signing the data from the
	 *            {@link InputStream}
	 */
	public void sign(OutputStream outputStream, InputStream inputStream,
			String keyInfo);

	/**
	 * Verifies the data from the supplied {@link InputStream}. Puts the
	 * unsigned representation of the data onto the supplied
	 * 
	 * @param outputStream
	 *            the output stream
	 * @param inputStream
	 *            the input stream {@link OutputStream}. If the data could not
	 *            be verfied <code>null</code> is put onto the
	 *            {@link OutputStream}.
	 */
	public void verify(OutputStream outputStream, InputStream inputStream);

}
