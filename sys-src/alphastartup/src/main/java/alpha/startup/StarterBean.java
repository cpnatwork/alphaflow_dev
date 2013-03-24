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
 * $Id: StarterBean.java 3858 2012-06-14 08:07:01Z uj32uvac $
 *************************************************************************/
package alpha.startup;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import alpha.bulletin.AlphadocEventLogger;
import alpha.doyen.AlphaDoyenUtility;
import alpha.editor.AlphaEditorException;
import alpha.editor.Editor;
import alpha.editor.listeners.EditorWindowListener;
import alpha.facades.AlphaPropsFacade;
import alpha.injector.Injector;
import alpha.injector.InjectorException;
import alpha.model.AlphaDoc;
import alpha.model.cra.ContributorID;
import alpha.model.cra.EndpointID;
import alpha.model.cra.NodeID;
import alpha.model.cra.Participant;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.PSAPayload;
import alpha.util.CommunicationCredentialsCollector;
import alpha.util.IdentityCollector;
import alpha.util.RNG;
import alpha.util.XmlBinder;

/**
 * The Class StarterBean.
 * 
 */
@Service
public class StarterBean {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(alpha.startup.StarterBean.class.getName());

	/** The apf. */
	@Autowired
	private final AlphaPropsFacade apf = null;

	/** The editor. */
	@Autowired
	private final Editor editor = null;

	/**
	 * Go.
	 * 
	 * @param args
	 *            the args
	 */
	public void go(final String[] args) {
		try {
			/*
			 * notes:CPN: <br/>
			 * http://www.rgagnon.com/javadetails/java-0391.html
			 */

			String selfURI = ClassUriHelper.getClassURI(this.getClass());
			selfURI = ClassUriHelper.removeClassUriPrefix(selfURI);
			selfURI = ClassUriHelper.removePostJarSuffix(selfURI);

			// string has now been trimmed to the fully qualified JAR-File path
			final File jarFile = new File(selfURI);
			final String homePath = ClassUriHelper.removeExtention(selfURI)
					+ "/";

			// The alphaDoc.xml and alphaconfig.xml
			final File aDocXml = new File(homePath + "alphaDoc.xml");

			// two types of startup: existing a-Doc vs. alph-o-matic
			final boolean selfIsExistingAlphaDoc = aDocXml.exists();
			final boolean selfIsPureInjector = (!selfIsExistingAlphaDoc);

			/* ***************************************************** */

			// alph-o-matic without first content payload is currently not
			// supported (i.e. creating an a-Doc without any content cards)
			if ((selfIsPureInjector) && (args.length == 0)) {
				JOptionPane.showMessageDialog(new JFrame(),
						"alphaDoc.xml is missing - Shutting down" + homePath);
				System.exit(0);
			}

			/* ***************************************************** */

			// for existing a-Docs first load the AlphaDoc and AlphadocConfig

			AlphaDoc doc = null;
			AlphadocConfig config = new AlphadocConfig();

			/* ***************************************************** */

			if (selfIsExistingAlphaDoc) {

				final XmlBinder binder = new XmlBinder();

				// read: alphaDoc.xml
				doc = (AlphaDoc) binder.load(aDocXml, "alpha.model");

				// read: alphaconfig.xml
				final File configXml = new File(homePath + "alphaconfig.xml");
				config = (AlphadocConfig) binder.load(configXml,
						"alpha.model.docconfig");

				// read: PSA payload
				PSAPayload psapl = null;
				final File psaPlXml = new File(homePath + doc.getEpisodeID()
						+ "/" + CoordCardType.PSA.id() + "/pl/Payload.xml");
				if (psaPlXml.exists()) {
					psapl = (PSAPayload) binder.load(psaPlXml,
							CoordCardType.PSA.getModel());

				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"Payload.xml of PSA is missing - Shutting down: "
									+ psaPlXml.getPath());
					System.exit(0);
				}

				// Load Config for AlphaPropsFacade
				this.apf.initializeConfig(new String(""));

				this.apf.insertIntoDrools(config);
				// apf.initializeOvernet();
				this.apf.insertIntoDrools(doc);

				this.apf.insertIntoDrools(jarFile);

				// Load content from PSAPayload into drools
				this.apf.insertACDs(psapl, homePath);

				/* Create VerVarStore */
				this.apf.initializeModel(homePath);
			}

			final InetAddress addr = InetAddress.getLocalHost();

			/* ***************************************************** */

			String actorId = "";

			// if (config.isInvitation() && (config.getLocalParticipant() ==
			// null)) {
			if ((config.getLocalNodeID() == null)) {

				final IdentityCollector identCc = new IdentityCollector();
				actorId = identCc.getActorID();

				// Only allow modification of global communication settings on
				// first startup (-> injection)
				final CommunicationCredentialsCollector ccCollect = new CommunicationCredentialsCollector(
						config.isSecureCommunication(), (args.length > 0),
						config.isAcknowledgeDelivery());

				// only make ACK choice editable if initial startup
				ccCollect.setInitialization(!config.isInvitation());
				
				ccCollect.showDialog();
				if (ccCollect.isSubmit()) {
					config.setMailAddress(ccCollect.getMailAddress());
					config.setUsername(ccCollect.getUsername());
					config.setPassword(ccCollect.getPassword());
					config.setSmtpHost(ccCollect.getSmtpHost());
					config.setSmtpPort(ccCollect.getSmtpPort());
					config.setImapHost(ccCollect.getImapHost());
					config.setImapPort(ccCollect.getImapPort());
					config.setImapMode(ccCollect.getImapMode());
					config.setUseSpecificMailbox(ccCollect
							.isUseSpecificMailbox());
					config.setOwnKPPath(ccCollect.getOwnKPPath());
					config.setOwnKPPass(ccCollect.getOwnKPPass());
					config.setPkPath(ccCollect.getPkPath());
					config.setSsl(ccCollect.isSsl());
					config.setSecureCommunication(ccCollect
							.isSecureCommunication());
					config.setAcknowledgeDelivery(ccCollect
							.isAcknowledgeDelivery());
					// userNode.setEmailAddress(ccCollect.getMailAddress());
				}

				final String port = this.generatePortNumber();

				/* Adding new ParticipantNode to CRA-Payload */
				final Participant participant = AlphaDoyenUtility
						.getNormalParticipant();
				if (!config.isInvitation()) {
					AlphaDoyenUtility.changeToInitialParticipant(participant);
				}

				final NodeID nodeID = new NodeID();
				final ContributorID sub = new ContributorID(
						identCc.getInstitutionID(), identCc.getRoleID(),
						actorId);
				final EndpointID node = new EndpointID(addr.getHostName(),
						addr.getHostAddress(), port, ccCollect.getMailAddress());
				node.setEmailAddress(ccCollect.getMailAddress());
				nodeID.setContributor(sub);
				nodeID.setNode(node);
				participant.setNodeID(nodeID);

				config.setLocalNodeID(nodeID);

				if (selfIsExistingAlphaDoc) {
					this.apf.addParticipant(participant);

					try {
						new XmlBinder().store(config, homePath
								+ "/alphaconfig.xml", "alpha.model.docconfig");
					} catch (final IOException ioe) {
						StarterBean.LOGGER.severe("IOError: " + ioe);
					}
				}
			}

			/* Create Logger */
			AlphadocEventLogger eventLog = null;
			if (new File(homePath + "bulletin.txt").exists()) {
				eventLog = new AlphadocEventLogger(homePath);
			}

			// FIXME@KT: ergänze Bedingung um -Dlistcards=true
			final boolean alph_o_matic_injection = (args.length > 0);

			if (alph_o_matic_injection) {
				/* Start Alph-O-Matic Injector */
				final Injector inj = new Injector(config, addr, jarFile,
						this.apf);
				inj.setLogger(eventLog);
				inj.setHomePath(homePath);
				if (doc == null) {
					inj.setZero();
				}
				inj.inject(args);
				this.apf.shutdown();
				System.exit(0);

			} else {
				/* open editor */
				config.setHomePath(homePath);

				/* Start AlphaDoc Editor */
				final AlphaCardID aci = new AlphaCardID(
						config.getMyEpisodeId(),
						config.getMyCurrentlyActiveCardId());

				this.editor.init(aci, eventLog, config, homePath);
				this.apf.addObservers(this.editor, eventLog);

				this.editor.pack();
				this.editor.setVisible(true);
				this.editor.setResizable(true);
				this.editor.addWindowListener(new EditorWindowListener(
						homePath, this.apf, config));

				// Moved here to avoid race condition with observers
				this.apf.initializeOvernet();
				if (config.isInvitation()) {
					config.setInvitation(false);
					this.apf.initiateJoin();
				}
			}
		} catch (final URISyntaxException e) {
			JOptionPane
					.showMessageDialog(new JFrame(), "FAIL: " + e.toString());
			StarterBean.LOGGER.severe("Error: " + e);
			System.exit(0);
		} catch (final UnknownHostException e) {
			JOptionPane
					.showMessageDialog(new JFrame(), "FAIL: " + e.toString());
			StarterBean.LOGGER.severe("Error: " + e);
			System.exit(0);
		} catch (final AlphaEditorException e) {
			JOptionPane.showMessageDialog(
					new JFrame(),
					"A critical error occured while running the AlphaDoc Editor. "
							+ "The Editor was therefore shut down" + "\n"
							+ e.toString());
			System.exit(0);
		} catch (final InjectorException e) {
			JOptionPane.showMessageDialog(
					new JFrame(),
					"A critical error occured while running the Alph-O-Matic Injector.\n"
							+ "The Editor was therefore shut down" + "\n"
							+ e.toString());
			System.exit(0);
		} catch (final Throwable t) {
			final StackTraceElement[] stack = t.getStackTrace();
			final StringBuffer buf = new StringBuffer();
			for (final StackTraceElement stackTraceElement : stack) {
				buf.append(stackTraceElement.toString() + "\n");
			}
			final String trace = buf.toString();
			JOptionPane.showMessageDialog(new JFrame(), "FAIL: " + t.toString()
					+ "\n" + t.getLocalizedMessage() + "\n" + trace);
			System.exit(0);
		}
	}

	/**
	 * Generate port number.
	 * 
	 * @return the string
	 */
	private String generatePortNumber() {
		int random = new RNG().generate() % 1000;
		if (random < 0) {
			random += 1000;
		}
		random += 23000;
		final String port = String.valueOf(random);
		return port;
	}

}
