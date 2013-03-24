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
package alpha.props.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import junit.framework.Assert;

import java.util.logging.Logger;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import alpha.facades.AlphaPropsFacade;
import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.AlphaDoc;
import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.AdpAdornment;
import alpha.model.adornment.ConsensusScope;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.CorpusGenericusOC;
import alpha.model.apa.FundamentalSemanticType;
import alpha.model.apa.Validity;
import alpha.model.apa.Visibility;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.ContributorID;
import alpha.model.cra.EndpointID;
import alpha.model.cra.ObjectUnderConsideration;
import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.PSAPayload;
import alpha.props.AlphaPropsFacadeImpl;

/**
 * The Class StartAndKeepAliveDroolsTest2.
 */
@Test(enabled = false)
public class StartAndKeepAliveDroolsTest2 implements Observer {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(StartAndKeepAliveDroolsTest.class.getName());

	/** The alpha props facade. */
	private AlphaPropsFacade alphaPropsFacade;

	// Create AlphaDoc artificially...
	/** The doc. */
	protected AlphaDoc doc;

	/**
	 * Sets the up.
	 */
	@BeforeSuite
	public void setUp() {

		final Properties properties = new Properties();
		final InputStream fis = this.getClass().getClassLoader()
				.getResourceAsStream("alphaconfig3.properties");

		try {
			properties.load(fis);
		} catch (final IOException e) {

		}
		this.alphaPropsFacade = new AlphaPropsFacadeImpl();
		try {
			StartAndKeepAliveDroolsTest2.LOGGER.finer("Initialize model...");
			this.alphaPropsFacade.initializeModel("./Desktop/myDoc");
		} catch (final VerVarStoreException e) {
			StartAndKeepAliveDroolsTest2.LOGGER.severe("" + e);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		StartAndKeepAliveDroolsTest2.LOGGER
				.info("Create AlphaDoc artificially...");
		this.doc = this.bootstrapAlphaDoc();

		StartAndKeepAliveDroolsTest2.LOGGER.info("Initialize config...");
		// alphaPropsFacade.initializeConfig(properties);
		this.alphaPropsFacade.initializeConfig("23457");

		StartAndKeepAliveDroolsTest2.LOGGER
				.info("Add Editor as Observer to all Observables...");
		this.alphaPropsFacade.addObservers(this, this);

		try {
			while (true) {
				Thread.sleep(1000 * 60);
				StartAndKeepAliveDroolsTest2.LOGGER.info("Time: " + new Date());
			}
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the alpha doc.
	 * 
	 * @return the alpha doc
	 */
	private AlphaDoc bootstrapAlphaDoc() {

		final AlphaDoc doc = new AlphaDoc();
		doc.setEpisodeID("ep23");

		final ObjectUnderConsideration oid = new ObjectUnderConsideration();
		oid.setId("1337");
		final AdpAdornment aa = new AdpAdornment(
				CorpusGenericusOC.OCNAME.value(), ConsensusScope.EPISODE_STD,
				AdornmentDataType.STRING);
		aa.setValue("Cuddy");
		oid.updateOrCreateAdornment(aa);

		// CREATE THE PSA ALPHA CARD
		final AlphaCardDescriptor psa = new AlphaCardDescriptor();
		psa.setId(new AlphaCardID("ep23", CoordCardType.PSA.id()));
		// psa.setFundamentalSemanticType(FundamentalSemanticType.COORDINATION);
		psa.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.COORDINATION.value());
		// psa.setValidity(Validity.VALID);
		psa.readAdornment(CorpusGenericus.VALIDITY.value()).setValue(
				Validity.VALID.value());
		// psa.setVisibility(Visibility.PUBLIC);
		psa.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PUBLIC.value());

		final ContributorID sid1 = new ContributorID();
		sid1.setActor("$anyactor");
		sid1.setInstitution("$epcollective");
		sid1.setRole("$flowmanager");
		// psa.setContributor(sid1);
		psa.readAdornment(CorpusGenericus.ACTOR.value()).setValue(
				sid1.getActor());
		psa.readAdornment(CorpusGenericus.ROLE.value())
				.setValue(sid1.getRole());
		psa.readAdornment(CorpusGenericus.INSTITUTION.value()).setValue(
				sid1.getInstitution());

		// psa.setObject(oid);
		psa.readAdornment(CorpusGenericus.OCID.value()).setValue(oid.getId());
		// psa.setVariant("-");
		psa.readAdornment(CorpusGenericus.VARIANT.value()).setValue("-");
		// psa.setVersion("1.0");
		psa.readAdornment(CorpusGenericus.VERSION.value()).setValue("1.0");
		// doc.getMapofACs().put(psa.getId(), psa);
		this.alphaPropsFacade.getListOfACDs().add(psa);
		Assert.assertNotNull(psa);

		final PSAPayload psaPayload = new PSAPayload();
		psaPayload.getListOfTodoItems().add(psa.getId());

		// CREATE THE CRA ALPHA CARD
		final AlphaCardDescriptor cra = new AlphaCardDescriptor();
		cra.setId(new AlphaCardID("ep23", CoordCardType.CRA.id()));
		// cra.setFundamentalSemanticType(FundamentalSemanticType.COORDINATION);
		cra.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.COORDINATION.value());
		// cra.setValidity(Validity.VALID);
		cra.readAdornment(CorpusGenericus.VALIDITY.value()).setValue(
				Validity.VALID.value());
		// cra.setVisibility(Visibility.PUBLIC);
		cra.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PUBLIC.value());

		// cra.setContributor(sid1);
		cra.readAdornment(CorpusGenericus.ACTOR.value()).setValue(
				sid1.getActor());
		cra.readAdornment(CorpusGenericus.ROLE.value())
				.setValue(sid1.getRole());
		cra.readAdornment(CorpusGenericus.INSTITUTION.value()).setValue(
				sid1.getInstitution());
		// cra.setObject(oid);
		cra.readAdornment(CorpusGenericus.OCID.value()).setValue(oid.getId());
		// cra.setVariant("-");
		cra.readAdornment(CorpusGenericus.VARIANT.value()).setValue("-");
		// cra.setVersion("1.0");
		cra.readAdornment(CorpusGenericus.VERSION.value()).setValue("1.0");
		// doc.getMapofACs().put(cra.getId(), cra);
		this.alphaPropsFacade.getListOfACDs().add(cra);
		Assert.assertNotNull(cra);

		final CRAPayload craPayload = new CRAPayload();

		// HOME
		final Participant participantNode = new Participant();
		participantNode.setContributor(sid1);
		participantNode.setNode(new EndpointID("localhost", null, "23456",
				"promed.alphaflow@googlemail.com"));

		// UNI
		// ParticipantNode p1 = new ParticipantNode();
		// p1.setSubject(sid1);
		// p1.setNode(new NodeID("faui6s5", null, "23455"));
		//
		// ParticipantNode p2 = new ParticipantNode();
		// p2.setSubject(new Contributor("@prinston", "+gynecologist",
		// "=wilson"));
		// p2.setNode(new NodeID("faui6s6", null, "23456"));
		//
		// ParticipantNode p3 = new ParticipantNode();
		// p3.setSubject(new Contributor("@uniKlinik", "+gynecologist",
		// "=guenther"));
		// p3.setNode(new NodeID("faui6s7", null, "23457"));

		// HOME
		craPayload.getListOfParticipants().add(participantNode);

		// UNI
		// craPayload.getLoParticipants().add(p1);
		// craPayload.getLoParticipants().add(p2);
		// craPayload.getLoParticipants().add(p3);

		psaPayload.getListOfTodoItems().add(cra.getId());

		this.alphaPropsFacade.setPayload(psa.getId(), psaPayload, null);
		this.alphaPropsFacade.setPayload(cra.getId(), craPayload, null);

		return doc;
	}

	/**
	 * Tear down.
	 */
	@AfterSuite
	public void tearDown() {
		this.alphaPropsFacade.shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(final Observable o, final Object arg) {
		// LOGGER.info("-------- Remote Updates done! " + o.toString() + "," +
		// arg.getClass() + ");");
		StartAndKeepAliveDroolsTest2.LOGGER
				.info("-------- Remote Updates done! ");
	}
}
