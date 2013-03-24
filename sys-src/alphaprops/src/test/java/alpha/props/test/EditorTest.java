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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.AlphaCardType;
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

/**
 * The Class EditorTest.
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@Configurable
public class EditorTest extends AbstractTestNGSpringContextTests implements
		Observer {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(EditorTest.class.getName());

	/** The alpha props facade. */
	@Autowired(required = true)
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
		try {
			EditorTest.LOGGER.finer("Initialize model...");
			this.alphaPropsFacade.initializeModel("./Desktop/myDoc");
		} catch (final VerVarStoreException e) {
			EditorTest.LOGGER.severe("" + e);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.doc = this.bootstrapAlphaDoc();

		EditorTest.LOGGER.finer("Initialize config...");
		// alphaPropsFacade.initializeConfig(properties);
		this.alphaPropsFacade.initializeConfig("12345");

		EditorTest.LOGGER.finer("Add Editor as Observer to all Observables...");
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
		psa.readAdornment(CorpusGenericusOC.OCNAME.value()).setValue(
				oid.readAdornment(CorpusGenericusOC.OCNAME.value()).getValue());
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
		cra.readAdornment(CorpusGenericusOC.OCNAME.value()).setValue(
				oid.readAdornment(CorpusGenericusOC.OCNAME.value()).getValue());
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
		EditorTest.LOGGER.finer("====================TEST: addParticipant...");
		final Participant participantNode = new Participant();
		participantNode.setContributor(new ContributorID("$epcollective",
				"$flowmanager", "$newactor"));
		participantNode.setNode(new EndpointID("localhost", null, "23457",
				"promed.alphaflow@googlemail.com"));

		try {
			EditorTest.LOGGER
					.finer("Adding a participant to AlphaCardDescriptor...");
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
		EditorTest.LOGGER.finer("====================TEST: addAlphaCard...");
		final AlphaCardDescriptor cac = new AlphaCardDescriptor();
		cac.setId(new AlphaCardID("ep23", "12345"));
		// cac.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
		cac.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.CONTENT.value());
		// cac.setVisibility(Visibility.PRIVATE);
		cac.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PRIVATE.value());
		// cac.setVersion("0");
		cac.readAdornment(CorpusGenericus.VERSION.value()).setValue("0");

		try {
			this.alphaPropsFacade.addAlphaCard(cac);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "FAIL");
			e.printStackTrace();
		} catch (final Throwable t) {
			JOptionPane.showMessageDialog(new JFrame(), "FAIL");
		}

	}

	/*
	 * @Test(enabled=false, dependsOnMethods = { "addAlphaCardCAC" }) public
	 * final void addAlphaCardCAC2() {
	 * 
	 * AlphaCardDescriptor cac2 = new AlphaCardDescriptor(); cac2.setId(new
	 * AlphaCardID("ep23", String.valueOf(new RNG().generate())));
	 * cac2.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
	 * cac2.setVisibility(Visibility.PRIVATE); cac2.setVersion("0");
	 * 
	 * try { alphaPropsFacade.addAlphaCard(cac2); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 * 
	 * @Test(enabled=false, dependsOnMethods = { "addAlphaCardCAC2" }) public
	 * final void addAlphaCardCAC3() {
	 * 
	 * AlphaCardDescriptor cac3 = new AlphaCardDescriptor(); cac3.setId(new
	 * AlphaCardID("ep23", String.valueOf(new RNG().generate())));
	 * cac3.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
	 * cac3.setVisibility(Visibility.PRIVATE); cac3.setVersion("0");
	 * 
	 * try { alphaPropsFacade.addAlphaCard(cac3); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 * 
	 * @Test(enabled=false, dependsOnMethods = { "addAlphaCardCAC3" }) public
	 * final void addAlphaCard4() { AlphaCardDescriptor cac4 = new
	 * AlphaCardDescriptor(); cac4.setId(new AlphaCardID("ep23",
	 * String.valueOf(new RNG().generate())));
	 * cac4.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
	 * cac4.setVisibility(Visibility.PRIVATE);
	 * cac4.setAlphaCardName("TestCard"); cac4.setVersion("0");
	 * 
	 * try { alphaPropsFacade.addAlphaCard(cac4);
	 * LOGGER.finer("New Test AC added!\n"); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
	/**
	 * Check changeability cac.
	 */
	@Test(enabled = false, dependsOnMethods = { "addAlphaCardCAC" })
	public final void checkChangeabilityCAC() {
		final Map<String, Boolean> changeables = this.alphaPropsFacade
				.getAlphaCardChangeability(new AlphaCardID("ep23", "12345"));
		Assert.assertNotNull(changeables);
		EditorTest.LOGGER
				.finer("====================TEST: checkChangeabilityCAC... "
						+ changeables.toString());
	}

	/**
	 * Sets the alpha card name cac.
	 */
	@Test(enabled = false, dependsOnMethods = { "checkChangeabilityCAC" })
	public final void setAlphaCardNameCAC() {
		EditorTest.LOGGER
				.finer("====================TEST: setAlphaCardNameCAC...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.TITLE.value(), "Anesthesia Report");
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12345"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the object cac.
	 */
	@Test(enabled = false, dependsOnMethods = { "setAlphaCardNameCAC" })
	public final void setObjectCAC() {
		EditorTest.LOGGER.finer("====================TEST: setObjectCAC...");
		final ObjectUnderConsideration oid = new ObjectUnderConsideration();
		oid.setId("1337");
		final AdpAdornment aa = new AdpAdornment(
				CorpusGenericusOC.OCNAME.value(), ConsensusScope.EPISODE_STD,
				AdornmentDataType.STRING);
		aa.setValue("Cuddy");
		oid.updateOrCreateAdornment(aa);
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericusOC.OCNAME.value(),
					oid.readAdornment(CorpusGenericusOC.OCNAME.value())
							.getValue());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12345"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the subject cac.
	 */
	@Test(enabled = false, dependsOnMethods = { "setObjectCAC" })
	public final void setSubjectCAC() {
		EditorTest.LOGGER.finer("====================TEST: setSubjectCAC...");
		final CRAPayload craPayload = this.alphaPropsFacade.getCRA();
		try {
			final ContributorID contributor = craPayload
					.getListOfParticipants().iterator().next().getContributor();
			final Map<String, String> adornments = new HashMap<String, String>();
			adornments.put(CorpusGenericus.ACTOR.value(),
					contributor.getActor());
			adornments.put(CorpusGenericus.ROLE.value(), contributor.getRole());
			adornments.put(CorpusGenericus.INSTITUTION.value(),
					contributor.getInstitution());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12345"), adornments);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets the alpha card type cac.
	 */
	@Test(enabled = false, dependsOnMethods = { "setSubjectCAC" })
	public final void setAlphaCardTypeCAC() {
		EditorTest.LOGGER
				.finer("====================TEST: setAlphaCardTypeCAC...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.ALPHACARDTYPE.value(),
					AlphaCardType.RESULTS_REPORT.value());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12345"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the variant cac.
	 */
	@Test(enabled = false, dependsOnMethods = { "setAlphaCardTypeCAC" })
	public final void setVariantCAC() {
		// alphaPropsFacade.changeAdornmentRequest(alphaPropsFacade.getAlphaDoc().getAlphaCards().get(2).getId(),
		// AdornmentType.VARIANT, "0");
	}

	/**
	 * Adds the dynamic adornment.
	 */
	@Test(enabled = false, dependsOnMethods = { "setVariantCAC" })
	public final void addDynamicAdornment() {
		EditorTest.LOGGER
				.finer("====================TEST: addDynamicAdornment...");
		final AlphaCardDescriptor myCard = new AlphaCardDescriptor();
		myCard.setId(new AlphaCardID("ep23", "86420"));
		// myCard.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
		myCard.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.CONTENT.value());
		// myCard.setVisibility(Visibility.PRIVATE);
		myCard.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PRIVATE.value());
		// myCard.setVersion("0");
		myCard.readAdornment(CorpusGenericus.VERSION.value()).setValue("0");
		// myCard.setAlphaCardName("dynamic alphaCardDescriptor");
		myCard.readAdornment(CorpusGenericus.TITLE.value()).setValue(
				"dynamic alphaCardDescriptor");
		final PrototypedAdornment myAdornment = new PrototypedAdornment(
				"Temperature", ConsensusScope.DOMAIN_STD,
				AdornmentDataType.INTEGER);
		myAdornment.setValue("37");
		myCard.updateOrCreateAdornment(myAdornment);

		try {
			this.alphaPropsFacade.addAlphaCard(myCard);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "FAIL");
			e.printStackTrace();
		} catch (final Throwable t) {
			JOptionPane.showMessageDialog(new JFrame(), "FAIL");
			t.printStackTrace();
		}
	}

	/**
	 * Adds the alpha card ca c2.
	 */
	@Test(enabled = false, dependsOnMethods = { "setVariantCAC" })
	public final void addAlphaCardCAC2() {
		EditorTest.LOGGER
				.finer("====================TEST: addAlphaCardCAC2...");
		final AlphaCardDescriptor cac2 = new AlphaCardDescriptor();
		cac2.setId(new AlphaCardID("ep23", "12346"));
		// cac2.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
		cac2.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.CONTENT.value());
		// cac2.setVisibility(Visibility.PRIVATE);
		cac2.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PRIVATE.value());
		// cac2.setVersion("0");
		cac2.readAdornment(CorpusGenericus.VERSION.value()).setValue("0");

		try {
			this.alphaPropsFacade.addAlphaCard(cac2);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the alpha card name ca c2.
	 */
	@Test(enabled = false, dependsOnMethods = { "addAlphaCardCAC2" })
	public final void setAlphaCardNameCAC2() {
		EditorTest.LOGGER
				.finer("====================TEST: setAlphaCardNameCAC2...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.TITLE.value(),
					"Referral Voucher for Mammography Report");
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12346"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the object ca c2.
	 */
	@Test(enabled = false, dependsOnMethods = { "setAlphaCardNameCAC2" })
	public final void setObjectCAC2() {
		EditorTest.LOGGER.finer("====================TEST: setObjectCAC2...");
		try {
			final String objectName = this.alphaPropsFacade.getListOfACDs()
					.get(0).readAdornment(CorpusGenericusOC.OCNAME.value())
					.getValue();
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericusOC.OCNAME.value(), objectName);
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12346"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the subject ca c2.
	 */
	@Test(enabled = false, dependsOnMethods = { "setObjectCAC2" })
	public final void setSubjectCAC2() {
		EditorTest.LOGGER.finer("====================TEST: setSubjectCAC2...");
		final CRAPayload craPayload = this.alphaPropsFacade.getCRA();
		try {
			final ContributorID contributor = craPayload
					.getListOfParticipants().iterator().next().getContributor();
			final Map<String, String> adornments = new HashMap<String, String>();
			adornments.put(CorpusGenericus.ACTOR.value(),
					contributor.getActor());
			adornments.put(CorpusGenericus.ROLE.value(), contributor.getRole());
			adornments.put(CorpusGenericus.INSTITUTION.value(),
					contributor.getInstitution());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12346"), adornments);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the alpha card type ca c2.
	 */
	@Test(enabled = false, dependsOnMethods = { "setSubjectCAC2" })
	public final void setAlphaCardTypeCAC2() {
		EditorTest.LOGGER
				.finer("====================TEST: setAlphaCardTypeCAC2...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.ALPHACARDTYPE.value(),
					AlphaCardType.REFERRAL_VOUCHER.value());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12346"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the alpha card ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "setAlphaCardTypeCAC2" })
	public final void addAlphaCardCAC3() {
		EditorTest.LOGGER
				.finer("====================TEST: addAlphaCardCAC3...");
		final AlphaCardDescriptor cac3 = new AlphaCardDescriptor();
		cac3.setId(new AlphaCardID("ep23", "12347"));
		// cac3.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
		cac3.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.CONTENT.value());
		// cac3.setVisibility(Visibility.PRIVATE);
		cac3.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PRIVATE.value());
		// cac3.setVersion("0");
		cac3.readAdornment(CorpusGenericus.VERSION.value()).setValue("0");

		try {
			this.alphaPropsFacade.addAlphaCard(cac3);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the alpha card name ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "addAlphaCardCAC3" })
	public final void setAlphaCardNameCAC3() {
		EditorTest.LOGGER
				.finer("====================TEST: setAlphaCardNameCAC3...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.TITLE.value(), "Mammography Report");
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12347"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the object ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "setAlphaCardNameCAC3" })
	public final void setObjectCAC3() {
		EditorTest.LOGGER.finer("====================TEST: setObjectCAC3...");
		try {
			final String objectName = this.alphaPropsFacade.getListOfACDs()
					.get(0).readAdornment(CorpusGenericusOC.OCNAME.value())
					.getValue();
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericusOC.OCNAME.value(), objectName);
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12347"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the subject ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "setObjectCAC3" })
	public final void setSubjectCAC3() {
		EditorTest.LOGGER.finer("====================TEST: setSubjectCAC3...");
		try {
			final String contributorName = this.alphaPropsFacade
					.getListOfACDs().get(1)
					.readAdornment(CorpusGenericus.ACTOR.value()).getValue();
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.ACTOR.value(), contributorName);
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12347"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the alpha card type ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "setSubjectCAC3" })
	public final void setAlphaCardTypeCAC3() {
		EditorTest.LOGGER
				.finer("====================TEST: setAlphaCardTypeCAC3...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.ALPHACARDTYPE.value(),
					AlphaCardType.RESULTS_REPORT.value());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12347"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the relationship.
	 */
	@Test(enabled = false, dependsOnMethods = { "setAlphaCardTypeCAC3" })
	public final void addRelationship() {
		EditorTest.LOGGER.finer("====================TEST: addRelationship...");
		try {
			this.alphaPropsFacade.addRelationship(new AlphaCardID("ep23",
					"12346"), new AlphaCardID("ep23", "12347"),
					AlphaCardRelationshipType.REQUIRES_RESULT);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make visible cac.
	 */
	@Test(enabled = false, dependsOnMethods = { "addRelationship" })
	public final void makeVisibleCAC() {
		EditorTest.LOGGER.finer("====================TEST: makeVisibleCAC...");
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
	 * Make visible ca c2.
	 */
	@Test(enabled = false, dependsOnMethods = { "makeVisibleCAC" })
	public final void makeVisibleCAC2() {
		EditorTest.LOGGER.finer("====================TEST: makeVisibleCAC2...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.VISIBILITY.value(),
					Visibility.PUBLIC.value());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12346"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds the payload ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "makeVisibleCAC2" })
	public final void addPayloadCAC3() {
		EditorTest.LOGGER.finer("====================TEST: addPayloadCAC3...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.SYNTACTICPAYLOADTYPE.value(), "pdf");
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12347"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
		this.alphaPropsFacade.setPayload(new AlphaCardID("ep23", "12347"),
				"attachments/Result_Report_Anesthesia.pdf", null);

	}

	/**
	 * Make visible ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "addPayloadCAC3" })
	public final void makeVisibleCAC3() {
		EditorTest.LOGGER.finer("====================TEST: makeVisibleCAC3...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.VISIBILITY.value(),
					Visibility.PUBLIC.value());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12347"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make valid ca c3.
	 */
	@Test(enabled = false, dependsOnMethods = { "makeVisibleCAC3" })
	// public final void setSPTypeCAC3() {
	// try {
	// alphaPropsFacade.changeAdornmentRequest(alphaPropsFacade.getAlphaDoc().getAlphaCards().get(4).getId(),
	// AdornmentType.SPTYPE,
	// FileExtensionFilter.getFileExtension("Result_Report_Anesthesia.pdf"));
	// } catch (IllegalChangeException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// @Test(enabled=false, dependsOnMethods = { "setSPTypeCAC3" })
	public final void makeValidCAC3() {
		EditorTest.LOGGER.finer("====================TEST: makeValidCAC3...");
		try {
			final Map<String, String> adornment = new HashMap<String, String>();
			adornment.put(CorpusGenericus.VALIDITY.value(),
					Validity.VALID.value());
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12347"), adornment);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the alpha card4.
	 */
	@Test(enabled = false, dependsOnMethods = { "makeValidCAC3" })
	public final void addAlphaCard4() {
		EditorTest.LOGGER.finer("====================TEST: addAlphaCard4...");
		final AlphaCardDescriptor cac4 = new AlphaCardDescriptor();
		cac4.setId(new AlphaCardID("ep23", "12348"));
		// cac4.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
		cac4.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.CONTENT.value());
		// cac4.setVisibility(Visibility.PRIVATE);
		cac4.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PRIVATE.value());
		// cac4.setAlphaCardName("TestCard");
		cac4.readAdornment(CorpusGenericus.TITLE.value()).setValue("TestCard");
		// cac4.setVersion("0");
		cac4.readAdornment(CorpusGenericus.VERSION.value()).setValue("0");

		try {
			this.alphaPropsFacade.addAlphaCard(cac4);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the payload ca c4.
	 */
	@Test(enabled = false, dependsOnMethods = { "addAlphaCard4" })
	public final void addPayloadCAC4() {
		EditorTest.LOGGER.finer("====================TEST: addPayloadCAC4...");

		final Map<String, String> adornmentChangeRequests = new HashMap<String, String>();
		adornmentChangeRequests.put(CorpusGenericus.VISIBILITY.value(),
				Visibility.PUBLIC.value());
		// adornmentChangeRequests.put(AdornmentType.VALIDITY,
		// Validity.INVALID);
		// adornmentChangeRequests.put(AdornmentType.VALIDITY, Validity.VALID);
		// adornmentChangeRequests.put(AdornmentType.TITLE, "TestCard");
		// adornmentChangeRequests.put(AdornmentType.OBJECT,
		// alphaPropsFacade.getAlphaDoc().getAlphaCards().get(1).getObject());
		// CRAPayload craPayload = (CRAPayload)
		// vvs.load(alphaPropsFacade.getAlphaDoc().getCraId());
		// adornmentChangeRequests.put(AdornmentType.CONTRIBUTOR,
		// craPayload.getLoParticipants().iterator().next().getSubject());
		// adornmentChangeRequests.put(AdornmentType.ALPHACARDTYPE,
		// AlphaCardType.RESULTS_REPORT);
		adornmentChangeRequests.put(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(), "pdf");
		// adornmentChangeRequests.put(AdornmentType.VERSIONING, true);
		// adornmentChangeRequests.put(AdornmentType.VERSION, "4");
		// adornmentChangeRequests.put(AdornmentType.VARIANT, "2");

		// alphaPropsFacade.addPayload(new AlphaCardID("ep23","12348"),
		// "attachments/Result_Report_Anesthesia.pdf", adornmentChangeRequests);
		this.alphaPropsFacade
				.setPayload(
						new AlphaCardID("ep23", "12348"),
						this.getPayloadFromPath("attachments/Result_Report_Anesthesia.pdf"),
						adornmentChangeRequests);
	}

	/**
	 * Test adornment change events.
	 */
	@Test(enabled = false, dependsOnMethods = { "addPayloadCAC4" })
	public final void testAdornmentChangeEvents() {
		EditorTest.LOGGER
				.finer("====================TEST: testAdornmentChangeEvents...");
		try {
			final Map<String, String> adornments = new HashMap<String, String>();

			adornments.put(CorpusGenericus.VALIDITY.value(),
					Validity.INVALID.value());
			adornments.put(CorpusGenericus.VALIDITY.value(),
					Validity.VALID.value());
			final String objectName = this.alphaPropsFacade.getListOfACDs()
					.get(0).readAdornment(CorpusGenericusOC.OCNAME.value())
					.getValue();
			adornments.put(CorpusGenericusOC.OCNAME.value(), objectName);
			final String contributorName = this.alphaPropsFacade
					.getListOfACDs().get(1)
					.readAdornment(CorpusGenericus.ACTOR.value()).getValue();
			adornments.put(CorpusGenericus.ACTOR.value(), contributorName);
			adornments.put(CorpusGenericus.ALPHACARDTYPE.value(),
					AlphaCardType.RESULTS_REPORT.value());
			adornments.put(CorpusGenericus.VERSION.value(), "4");
			adornments.put(CorpusGenericus.VARIANT.value(), "2");
			this.alphaPropsFacade.updateAdornmentInstances(new AlphaCardID(
					"ep23", "12348"), adornments);
		} catch (final IllegalChangeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the all alpha cards.
	 */
	@Test(enabled = false, dependsOnMethods = { "testAdornmentChangeEvents" })
	public final void getAllAlphaCards() {
		EditorTest.LOGGER.finer("====================TEST: getAlphaCard...");
		try {
			final AlphaCardDescriptor psa = this.alphaPropsFacade
					.getAlphaCard(this.doc.getCoordCardId(CoordCardType.PSA));
			EditorTest.LOGGER.finer("TEST: Get the TSAAlphaCard..."
					+ psa.getId());
			final AlphaCardDescriptor cra = this.alphaPropsFacade
					.getAlphaCard(this.doc.getCoordCardId(CoordCardType.CRA));
			EditorTest.LOGGER.finer("TEST: Get the CRAAlphaCard..."
					+ cra.getId());

			// for (AlphaCardDescriptor contentAC :
			// alphaPropsFacade.getAlphaDoc().getAlphaCards()) {
			// LOGGER.finer("TEST: Get the ContentAlphaCard..." +
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
		// LOGGER.finer("-------- EDITOR reports: Updates done! " + o.toString()
		// + "," + arg.getClass() + ");");
		EditorTest.LOGGER.finer("-------- EDITOR reports: Updates done! ");
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
