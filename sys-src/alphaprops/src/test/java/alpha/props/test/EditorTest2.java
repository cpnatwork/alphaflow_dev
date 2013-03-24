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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import alpha.facades.AlphaPropsFacade;
import alpha.facades.exceptions.IllegalChangeException;
import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.AlphaDoc;
import alpha.model.Payload;
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
import alpha.model.psa.AlphaCardRelationshipType;
import alpha.model.psa.PSAPayload;
import alpha.props.AlphaPropsFacadeImpl;

/**
 * The Class EditorTest2.
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@Configurable
public class EditorTest2 extends AbstractTestNGSpringContextTests implements
		Observer {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(EditorTest.class.getName());

	/** The alpha props facade. */
	@Autowired
	private AlphaPropsFacade alphaPropsFacade;

	// Create AlphaDoc artificially...
	/** The doc. */
	protected AlphaDoc doc;

	/**
	 * Sets the up.
	 */
	@BeforeClass
	public void setUp() {
		final Properties properties = new Properties();
		final InputStream fis = this.getClass().getClassLoader()
				.getResourceAsStream("alphaconfig.properties");
		try {
			properties.load(fis);
		} catch (final IOException e) {

		}
		this.alphaPropsFacade = new AlphaPropsFacadeImpl();
		try {
			EditorTest2.LOGGER.finer("Initialize model...");
			this.alphaPropsFacade.initializeModel("./Desktop/myDoc");
		} catch (final VerVarStoreException e) {
			EditorTest2.LOGGER.severe("" + e);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.doc = this.bootstrapAlphaDoc();

		EditorTest2.LOGGER.info("Initialize config...");
		// alphaPropsFacade.initializeConfig(properties);
		this.alphaPropsFacade.initializeConfig("12345");

		EditorTest2.LOGGER.info("Add Editor as Observer to all Observables...");
		this.alphaPropsFacade.addObservers(this, this);

		try {
			Thread.sleep(1000 * 5);
		} catch (final InterruptedException e1) {
			e1.printStackTrace();
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
		doc.setTitle("A new AlphaDoc!");

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
	 * Adds the participant.
	 */
	@Test(enabled = false)
	public final void addParticipant() {
		EditorTest2.LOGGER.info("====================TEST: addParticipant...");
		final Participant participantNode = new Participant();
		participantNode.setContributor(new ContributorID("$epcollective",
				"$flowmanager", "$newactor"));
		participantNode.setNode(new EndpointID("localhost", null, "23457",
				"promed.alphaflow@googlemail.com"));

		try {
			EditorTest2.LOGGER
					.info("Adding a participant to AlphaCardDescriptor...");
			this.alphaPropsFacade.addParticipant(participantNode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the alpha card cac.
	 */
	@Test(enabled = false, dependsOnMethods = { "addParticipant" })
	public final void addAlphaCardCAC() {
		EditorTest2.LOGGER.info("====================TEST: addAlphaCard...");
		final AlphaCardDescriptor cac = new AlphaCardDescriptor();
		cac.setId(new AlphaCardID("ep23", "12345"));
		// cac.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
		cac.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.CONTENT.value());
		// cac.setVisibility(Visibility.PRIVATE);
		cac.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PRIVATE.value());
		// cac.setValidity(Validity.INVALID);
		cac.readAdornment(CorpusGenericus.VALIDITY.value()).setValue(
				Validity.INVALID.value());
		// cac.setVersion("0.0");
		cac.readAdornment(CorpusGenericus.VERSION.value()).setValue("0.0");

		try {
			this.alphaPropsFacade.addAlphaCard(cac);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check changeability cac.
	 */
	@Test(enabled = false, dependsOnMethods = { "addAlphaCardCAC" })
	public final void checkChangeabilityCAC() {
		EditorTest2.LOGGER
				.info("====================TEST: checkChangeabilityCAC... ");
		final Map<String, Boolean> changeables = this.alphaPropsFacade
				.getAlphaCardChangeability(new AlphaCardID("ep23", "12345"));
		EditorTest2.LOGGER
				.info("====================TEST: checkChangeabilityCAC... "
						+ changeables.toString());
	}

	/**
	 * Adds the alpha card ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "checkChangeabilityCAC" })
	public final void addAlphaCardCAC3() {

		final AlphaCardDescriptor cac3 = new AlphaCardDescriptor();
		cac3.setId(new AlphaCardID("ep23", "12346"));
		// cac3.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
		cac3.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.CONTENT.value());
		// cac3.setVisibility(Visibility.PRIVATE);
		cac3.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PRIVATE.value());
		// cac3.setValidity(Validity.INVALID);
		cac3.readAdornment(CorpusGenericus.VALIDITY.value()).setValue(
				Validity.INVALID.value());
		// cac3.setVersion("0.0");
		cac3.readAdornment(CorpusGenericus.VERSION.value()).setValue("0.0");

		try {
			this.alphaPropsFacade.addAlphaCard(cac3);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the relationship.
	 */
	@Test(enabled = false, dependsOnMethods = { "addAlphaCardCAC3" })
	public final void addRelationship() {
		EditorTest2.LOGGER.info("====================TEST: addRelationship...");
		try {
			this.alphaPropsFacade.addRelationship(new AlphaCardID("ep23",
					"12345"), new AlphaCardID("ep23", "12346"),
					AlphaCardRelationshipType.REQUIRES_RESULT);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * More change adornment requests.
	 */
	@Test(enabled = false, dependsOnMethods = { "addRelationship" })
	public final void moreChangeAdornmentRequests() {
		EditorTest2.LOGGER.info("====================TEST: makeVisible...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.VISIBILITY.value(),
					Visibility.PUBLIC.value());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12345"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds the payload ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "moreChangeAdornmentRequests" })
	public final void addPayloadCAC3() {
		EditorTest2.LOGGER.info("====================TEST: addPayload...");
		this.alphaPropsFacade.setPayload(new AlphaCardID("ep23", "12346"), this
				.getPayloadFromPath("attachments/Java_Rules_und_Drools.pdf"),
				null);
	}

	/**
	 * Make visible ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "addPayloadCAC3" })
	public final void makeVisibleCAC3() {
		try {
			final Map<String, String> adornments = new HashMap<String, String>();
			adornments.put(CorpusGenericus.VISIBILITY.value(),
					Visibility.PUBLIC.value());
			adornments.put(CorpusGenericus.VALIDITY.value(),
					Validity.VALID.value());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12346"), adornments);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the all alpha cards.
	 */
	@Test(enabled = false, dependsOnMethods = { "makeVisibleCAC3" })
	public final void getAllAlphaCards() {
		EditorTest2.LOGGER.info("====================TEST: getAlphaCard...");
		try {
			final AlphaCardDescriptor psa = this.alphaPropsFacade
					.getAlphaCard(this.doc.getCoordCardId(CoordCardType.PSA));
			EditorTest2.LOGGER.info("TEST: Get the TSAAlphaCard..."
					+ psa.getId());
			final AlphaCardDescriptor cra = this.alphaPropsFacade
					.getAlphaCard(this.doc.getCoordCardId(CoordCardType.CRA));
			EditorTest2.LOGGER.info("TEST: Get the CRAAlphaCard..."
					+ cra.getId());

			// for (AlphaCardDescriptor contentAC :
			// alphaPropsFacade.getAlphaDoc().getAlphaCards()) {
			// LOGGER.info("TEST: Get the ContentAlphaCard..." +
			// contentAC.toString());
			// }

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tear down.
	 */
	@AfterClass
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
		EditorTest2.LOGGER.info("-------- EDITOR reports: Updates done! "
				+ o.toString() + "," + arg.getClass() + ");");
	}

	/**
	 * Gets the payload from path.
	 * 
	 * @param payloadPath
	 *            the payload path
	 * @return the payload from path
	 */
	public Payload getPayloadFromPath(final String payloadPath) {
		final File payloadFile = new File(payloadPath);
		final Payload payload = new Payload();

		FileInputStream fis;
		try {
			fis = new FileInputStream(payloadFile);
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int len = 0;

			while ((len = fis.read(buffer)) > 0) {
				baos.write(buffer, 0, len);
			}

			fis.close();
			buffer = baos.toByteArray();

			// payload.setCra(false);
			// payload.setTsa(false);
			payload.setContent(buffer);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return payload;
	}
}
