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
 * Provides generic methods for encrypting and decrypting data.
 */
public interface CryptographyUtility {

	/**
	 * Encrypts the incoming {@link InputStream} using the supplied key
	 * information. Puts result onto the supplied {@link OutputStream}.
	 * 
	 * @param outputStream
	 *            the {@link OutputStream}
	 * @param inputStream
	 *            the {@link InputStream}
	 * @param keyInfo
	 *            the information about the key to be used for encryption
	 */
	public void encrypt(OutputStream outputStream, InputStream inputStream,
			String[] keyInfo);

	/**
	 * Decrypts the incoming {@link InputStream}. Puts result onto the supplied
	 * 
	 * @param outputStream
	 *            the {@link OutputStream}
	 * @param inputStream
	 *            the {@link InputStream} {@link OutputStream}.
	 */
	public void decrypt(OutputStream outputStream, InputStream inputStream);
}
