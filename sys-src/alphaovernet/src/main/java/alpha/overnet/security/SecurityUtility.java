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
 * $Id: SecurityUtility.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.overnet.security;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Combines the functionality of {@link CryptographyUtility} and.
 * 
 * {@link ElectronicSignatureUtility} into one interface.
 */
public abstract class SecurityUtility implements ElectronicSignatureUtility,
		CryptographyUtility {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.overnet.security.CryptographyUtility#encrypt(java.io.OutputStream,
	 * java.io.InputStream, java.lang.String)
	 */
	@Override
	public void encrypt(final OutputStream outputStream,
			final InputStream inputStream, final String[] keyInfo) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.overnet.security.CryptographyUtility#decrypt(java.io.OutputStream,
	 * java.io.InputStream)
	 */
	@Override
	public void decrypt(final OutputStream outputStream,
			final InputStream inputStream) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.overnet.security.ElectronicSignatureUtility#sign(java.io.OutputStream
	 * , java.io.InputStream, java.lang.String)
	 */
	@Override
	public void sign(final OutputStream outputStream,
			final InputStream inputStream, final String keyInfo) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.overnet.security.ElectronicSignatureUtility#verify(java.io.OutputStream
	 * , java.io.InputStream)
	 */
	@Override
	public void verify(final OutputStream outputStream,
			final InputStream inputStream) {
	}

	/**
	 * Imports cryptography metadata from the supplied {@link InputStream} into
	 * the local cryptography metadata storage.
	 * 
	 * @param input
	 *            the input
	 */
	public abstract void importCryptographyMetadata(InputStream input);

}
