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
import java.util.LinkedHashSet;

import junit.framework.TestCase;

import org.junit.Test;

import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;

/**
 * The Class VerVarStoreImplTest.
 * 
 */
public class VerVarStoreImplTest extends TestCase {

	/** The vvs. */
	VerVarStoreImpl vvs = null;

	/**
	 * Test method for.
	 * 
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 * @throws URISyntaxException
	 *             the uRI syntax exception
	 * 
	 */
	@Test
	public final void testStoreAlphaCardIdentifierPayloadTSA()
			throws VerVarStoreException, URISyntaxException {
		String homePath = VerVarStoreImplTest.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().toString();
		homePath = homePath.replace("file:/", "");
		// JOptionPane.showMessageDialog(new JFrame(), homePath);
		this.vvs = new VerVarStoreImpl(homePath);
		final AlphaCardID aci = new AlphaCardID("ep23", CoordCardType.PSA);
		final PSAPayload psa = new PSAPayload();
		// psa.setTsa(true);
		psa.setListOfTodoItems(new LinkedHashSet<AlphaCardID>());
		psa.setListOfTodoRelationships(new LinkedHashSet<AlphaCardRelationship>());
		this.vvs.store(aci, psa);
	}

	/**
	 * Test store alpha card identifier payload cra.
	 * 
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 * @throws URISyntaxException
	 *             the uRI syntax exception
	 */
	@Test
	public final void testStoreAlphaCardIdentifierPayloadCRA()
			throws VerVarStoreException, URISyntaxException {
		String homePath = VerVarStoreImplTest.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().toString();
		homePath = homePath.replace("file:/", "");
		// JOptionPane.showMessageDialog(new JFrame(), homePath);
		this.vvs = new VerVarStoreImpl(homePath);
		final AlphaCardID aci = new AlphaCardID("ep23", CoordCardType.CRA);
		final CRAPayload cra = new CRAPayload();
		// cra.setCra(true);
		cra.setListOfParticipants(new LinkedHashSet<Participant>());
		this.vvs.store(aci, cra);
	}

	/**
	 * Test method for.
	 * 
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 * @throws URISyntaxException
	 *             the uRI syntax exception
	 *             {@link alpha.vvs.VerVarStoreImpl#load(alpha.model.identification.AlphaCardID)}
	 *             .
	 */
	@Test
	public final void testLoadAlphaCardIdentifierTSA()
			throws VerVarStoreException, URISyntaxException {
		String homePath = VerVarStoreImplTest.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().toString();
		homePath = homePath.replace("file:/", "");
		// JOptionPane.showMessageDialog(new JFrame(), homePath);
		this.vvs = new VerVarStoreImpl(homePath);
		final AlphaCardID aci = new AlphaCardID("ep23", CoordCardType.PSA);
		this.vvs.load(aci);
	}

	/**
	 * Test load alpha card identifier cra.
	 * 
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 * @throws URISyntaxException
	 *             the uRI syntax exception
	 */
	@Test
	public final void testLoadAlphaCardIdentifierCRA()
			throws VerVarStoreException, URISyntaxException {
		String homePath = VerVarStoreImplTest.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().toString();
		homePath = homePath.replace("file:/", "");
		// JOptionPane.showMessageDialog(new JFrame(), homePath);
		this.vvs = new VerVarStoreImpl(homePath);
		final AlphaCardID aci = new AlphaCardID("ep23", CoordCardType.CRA);
		this.vvs.load(aci);
	}

	/**
	 * Test method for.
	 * 
	 * {@link alpha.vvs.VerVarStoreImpl#load(alpha.model.identification.AlphaCardID, java.lang.String)}
	 * .
	 */
	@Test
	public final void testLoadAlphaCardIdentifierString() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for.
	 * 
	 * {@link alpha.vvs.VerVarStoreImpl#load(alpha.model.identification.AlphaCardID, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public final void testLoadAlphaCardIdentifierStringString() {
		// fail("Not yet implemented");
	}

}
