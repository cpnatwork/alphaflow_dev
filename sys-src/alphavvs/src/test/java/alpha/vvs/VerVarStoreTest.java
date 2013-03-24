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

import java.net.URISyntaxException;

import junit.framework.TestCase;
import alpha.facades.AlphaPropsFacade;
import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.Payload;
import alpha.model.identification.AlphaCardID;

/**
 * The Class VerVarStoreTest.
 */
public class VerVarStoreTest extends TestCase {

	/** The sut. */
	VerVarStoreImpl sut;

	/** The payload. */
	Payload payload = new Payload();

	/** The apf. */
	private AlphaPropsFacade apf = null;

	/**
	 * Instantiates a new ver var store test.
	 * 
	 * @param apf
	 *            the apf
	 */
	public VerVarStoreTest(final AlphaPropsFacade apf) {

		this.apf = apf;

	}

	/**
	 * Test store alpha card identifier payload.
	 * 
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 * @throws URISyntaxException
	 *             the uRI syntax exception
	 */
	public final void testStoreAlphaCardIdentifierPayload()
			throws VerVarStoreException, URISyntaxException {
		String homePath = VerVarStoreTest.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().toString();
		homePath = homePath.replace("file:/", "");
		this.sut = new VerVarStoreImpl(homePath, this.apf);
		final AlphaCardID aci = this.apf.getListOfACDs().get(0).getId();
		this.sut.setVersion("1.0");
		this.sut.setSptype("pdf");
		this.payload = this.sut.load(aci);
		this.sut.store(aci, this.payload);
	}

	/**
	 * Test store alpha card identifier payload string.
	 */
	public final void testStoreAlphaCardIdentifierPayloadString() {
		// fail("Not yet implemented");
	}

	/**
	 * Test store alpha card identifier payload string string.
	 */
	public final void testStoreAlphaCardIdentifierPayloadStringString() {
		// fail("Not yet implemented");
	}

	/**
	 * Test load alpha card identifier.
	 * 
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 * @throws URISyntaxException
	 *             the uRI syntax exception
	 */
	public final void testLoadAlphaCardIdentifier()
			throws VerVarStoreException, URISyntaxException {
		String homePath = VerVarStoreTest.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().toString();
		homePath = homePath.replace("file:/", "");
		this.sut = new VerVarStoreImpl(homePath, this.apf);
		final AlphaCardID aci = (this.apf.getListOfACDs().get(0)).getId();
		this.sut.setVersion("1.0");
		this.sut.setSptype("pdf");
		this.sut.load(aci);
	}

	/**
	 * Test load alpha card identifier string.
	 */
	public final void testLoadAlphaCardIdentifierString() {
		// fail("Not yet implemented");
	}

	/**
	 * Test load alpha card identifier string string.
	 */
	public final void testLoadAlphaCardIdentifierStringString() {
		// fail("Not yet implemented");
	}
}
