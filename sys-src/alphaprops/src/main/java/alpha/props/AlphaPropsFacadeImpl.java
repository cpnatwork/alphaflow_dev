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
package alpha.props;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.drools.KnowledgeBase;
import org.drools.SystemEventListener;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;
import org.hydra.core.HydraFacade;
import org.springframework.stereotype.Service;

import alpha.adaptive.AlphaAdaptiveFacadeImpl;
import alpha.doyen.AlphaDoyenUtility;
import alpha.facades.AlphaAdaptiveFacade;
import alpha.facades.AlphaOvernetFacade;
import alpha.facades.AlphaPropsFacade;
import alpha.facades.exceptions.IllegalChangeException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.AlphaDoc;
import alpha.model.Payload;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.APAPayload;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.FundamentalSemanticType;
import alpha.model.apa.Validity;
import alpha.model.apa.Visibility;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.EndpointID;
import alpha.model.cra.Participant;
import alpha.model.cra.Token;
import alpha.model.deliveryacknowledgment.AcknowledgementStructureItem;
import alpha.model.deliveryacknowledgment.AcknowledgmentStructure;
import alpha.model.deliveryacknowledgment.LocalTimestamp;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.AlphaCardRelationshipType;
import alpha.model.psa.PSAPayload;
import alpha.model.versionmap.VersionMap;
import alpha.offsync.AlphaOffSyncFacade;
import alpha.offsync.time.VersionMapHistoryUtility;
import alpha.overnet.event.AddAlphaCardEvent;
import alpha.overnet.event.ChangeAlphaCardDescriptorEvent;
import alpha.overnet.event.ChangePayloadEvent;
import alpha.props.eventlistener.UIAlphaCardNotifierEventListener;
import alpha.props.eventlistener.UIChangeEventNotifierEventListener;
import alpha.props.eventlistener.UINotifierEventListener;
import alpha.util.XmlBinder;
import alpha.vvs.Workspace;
import alpha.vvs.WorkspaceImpl;

/**
 * This class is an implementation of the {@link AlphaPropsFacade} interface.
 * 
 * 
 */
@Service
public class AlphaPropsFacadeImpl implements AlphaPropsFacade {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphaPropsFacadeImpl.class.getName());

	/** The Constant LOGGER. */
	transient private static final Logger DROOLSLOGGER = Logger
			.getLogger("alphaprops-rules");

	/** The ksession. */
	protected StatefulKnowledgeSession ksession = null;

	/**
	 * Gets the ksession.
	 * 
	 * @return the ksession
	 */
	public StatefulKnowledgeSession getKsession() {
		return this.ksession;
	}

	/** The k rt logger. */
	protected KnowledgeRuntimeLogger kRtLogger = null;

	/** The ovn facade. */
	protected AlphaOvernetFacade ovnFacade = null;

	/** The home path. */
	private String homePath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#getHomePath()
	 */
	@Override
	public String getHomePath() {
		return this.homePath;
	}

	/**
	 * Sets the home path.
	 * 
	 * @param homePath
	 *            the new home path
	 */
	public void setHomePath(final String homePath) {
		this.homePath = homePath;
	}

	/** The join util. */
	private JoinUtility joinUtil;

	/**
	 * Gets the join util.
	 * 
	 * @return the join util
	 */
	public JoinUtility getJoinUtil() {
		return this.joinUtil;
	}

	/**
	 * Sets the join util.
	 * 
	 * @param joinUtil
	 *            the new join util
	 */
	public void setJoinUtil(final JoinUtility joinUtil) {
		this.joinUtil = joinUtil;
	}

	/** The token util. */
	private TokenUtility tokenUtil = new TokenUtility(this);

	private AcknowledgementUtility ackUtil = new AcknowledgementUtility(this);

	// /**
	// * Gets the token util.
	// *
	// * @return the token util
	// */
	// public TokenUtility getTokenUtil() {
	// return this.tokenUtil;
	// }
	//
	// /**
	// * Sets the token util.
	// *
	// * @param tokenUtil
	// * the new token util
	// */
	// public void setTokenUtil(final TokenUtility tokenUtil) {
	// this.tokenUtil = tokenUtil;
	// }

	/** The hwf. */
	private Workspace hwf;

	/**
	 * Gets the hwf.
	 * 
	 * @return the hwf
	 */
	public Workspace getHwf() {
		return this.hwf;
	}

	/**
	 * Sets the hwf.
	 * 
	 * @param hwf
	 *            the new hwf
	 */
	public void setHwf(final Workspace hwf) {
		this.hwf = hwf;
	}

	/**
	 * Instantiates a new alpha props facade impl.
	 * 
	 */
	public AlphaPropsFacadeImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#addObservers(java.util.Observer,
	 * java.util.Observer)
	 */
	@Override
	public void addObservers(final Observer alphaCardObserver,
			final Observer logObserver) {

		if (this.ksession.getWorkingMemoryEventListeners() != null) {
			final Collection<WorkingMemoryEventListener> myEventListners = this.ksession
					.getWorkingMemoryEventListeners();

			for (final WorkingMemoryEventListener workingMemoryEventListener : myEventListners) {
				if (workingMemoryEventListener instanceof UIAlphaCardNotifierEventListener) {
					AlphaPropsFacadeImpl.LOGGER
							.info("Add Editor as Observer to this Observable..."
									+ workingMemoryEventListener.getClass()
											.getCanonicalName());
					((UIAlphaCardNotifierEventListener) workingMemoryEventListener)
							.addObserver(alphaCardObserver);
				} else if (workingMemoryEventListener instanceof UIChangeEventNotifierEventListener) {
					AlphaPropsFacadeImpl.LOGGER
							.info("Add Editor as Observer to this Observable..."
									+ workingMemoryEventListener.getClass()
											.getCanonicalName());
					((UIChangeEventNotifierEventListener) workingMemoryEventListener)
							.addObserver(logObserver);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#sendMessage(java.util.List,
	 * java.util.Set, java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public void sendMessage(final List<Object> attachments,
			final Set<EndpointID> recipients, final String subject,
			final String messageContent, final boolean encrypt,
			final boolean sign) {
		this.ovnFacade.sendUpdate(attachments, recipients, subject,
				messageContent, encrypt, sign);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#initializeConfig(java.lang.String)
	 */
	@Override
	public void initializeConfig(final String port) {
		// USING KnowledgeBuilder
		/*
		 * Knowledge Builder can be used in order to build the Knowledge Base.
		 * Nevertheless, a Knowledge Agent was used, so that a dynamic load of
		 * rule packages is possible.
		 */
		// KnowledgeBuilder kbuilder =
		// KnowledgeBuilderFactory.newKnowledgeBuilder();
		// //
		// kbuilder.add(ResourceFactory.newUrlResource(url),ResourceType.CHANGE_SET);
		// String changeset = config.getProperty("changeset");
		// kbuilder.add(ResourceFactory.newClassPathResource(changeset,
		// getClass()), ResourceType.CHANGE_SET);

		// KnowledgeBuilderErrors errors = kbuilder.getErrors();
		// if (errors.size() > 0) {
		// for (KnowledgeBuilderError error : errors) {
		// LOGGER.severe(error);
		// }
		// throw new IllegalArgumentException("Could not parse knowledge.");
		// }

		// KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		// kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		// USING KnowledgeAgent
		final KnowledgeAgent kagent = KnowledgeAgentFactory
				.newKnowledgeAgent("MyAgent");
		kagent.applyChangeSet(ResourceFactory.newClassPathResource(
				"changeset.xml", this.getClass()));
		kagent.setSystemEventListener(new SystemEventListener() {

			@Override
			public void warning(final String message, final Object object) {
				// TODO Auto-generated method stub
				this.log(message);
			}

			@Override
			public void warning(final String message) {
				// TODO Auto-generated method stub
				this.log(message);
			}

			@Override
			public void info(final String message, final Object object) {
				// TODO Auto-generated method stub
				this.log(message);
			}

			@Override
			public void info(final String message) {
				// TODO Auto-generated method stub
				this.log(message);
			}

			@Override
			public void exception(final String message, final Throwable e) {
				// TODO Auto-generated method stub
				this.log(e);
			}

			@Override
			public void exception(final Throwable e) {
				// TODO Auto-generated method stub
				this.log(e);

			}

			@Override
			public void debug(final String message, final Object object) {
				// TODO Auto-generated method stub
				this.log(message);
			}

			@Override
			public void debug(final String message) {
				// TODO Auto-generated method stub
				this.log(message);
			}

			private void log(final Object obj) {
				AlphaPropsFacadeImpl.LOGGER.info("__________-DROOLS___________"
						+ obj);
			}
		});
		final KnowledgeBase kbase = kagent.getKnowledgeBase();

		ResourceFactory.getResourceChangeNotifierService().start();
		// polling every 60sec per default; configure explicitly otherwise
		ResourceFactory.getResourceChangeScannerService().start();

		this.ksession = kbase.newStatefulKnowledgeSession();

		// these listeners monitor changes in the WorkingMemory and notify all
		// registered Observers
		final UINotifierEventListener cenel = new UIChangeEventNotifierEventListener();
		this.ksession.addEventListener(cenel);
		final UINotifierEventListener acnel = new UIAlphaCardNotifierEventListener();
		this.ksession.addEventListener(acnel);

		// initialize broadcasting
		/*
		 * updateServiceSender = new TCPUpdateServiceSender();
		 * 
		 * updateServiceReceiver = new TCPUpdateServiceReceiver();
		 * updateServiceReceiver.setUpPipeline(ksession); //
		 * updateServiceReceiver
		 * .setPort(Integer.valueOf(config.getProperty("listeningPort")));
		 * updateServiceReceiver.setPort(Integer.valueOf(port));
		 * ResultHandlerImpl resultHandler = new ResultHandlerImpl();
		 * updateServiceReceiver.setResultHandler(resultHandler); new
		 * Thread((Runnable) updateServiceReceiver).start();
		 * 
		 * ksession.setGlobal("updateServiceSender", updateServiceSender);
		 */
		this.ksession.setGlobal("LOGGER", AlphaPropsFacadeImpl.DROOLSLOGGER);

		if (AlphaPropsFacadeImpl.LOGGER.isLoggable(Level.FINEST)) {
			// if FINEST/TRACE mode, then activate kRtLogger
			this.kRtLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(
					this.ksession, "alphaprops-KnowledgeRuntimeLogger");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#initializeOvernet()
	 */
	@Override
	public void initializeOvernet() {

		if (this.ovnFacade != null) {
			this.ovnFacade.shutdown();
		}

		// Legacy Implementation:
		// ovnFacade = new AlphaOvernetLegacyFacade(ksession, getAlphaConfig());

		// SMTP/IMAP based implementation:
		this.ovnFacade = new AlphaOffSyncFacade(this.ksession,
				this.getAlphaConfig());

		this.ovnFacade.receiveUpdates();

		this.joinUtil = new JoinUtility(this);

		this.ksession.insert(this.joinUtil);

		// this.tokenUtil = new TokenUtility(this);
		this.ksession.insert(this.tokenUtil);
		this.ksession.insert(this.ackUtil);
		// SequentialJoinCallback sjc = joinUtil.initiateJoin();
		// SequentialJoinSynchronisation sjs =
		// joinUtil.handleSequentialJoinCallback(sjc);
		// ParallelJoinCallback pjc =
		// joinUtil.handleSequentialJoinSynchronisation(sjs);
		// ParallelJoinSynchronisation pjs =
		// joinUtil.handleParallelJoinCallback(pjc);
		// joinUtil.handleParallelJoinSynchronisation(pjs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#initializeModel(java.lang.String)
	 */
	@Override
	public boolean initializeModel(final String homePath) throws Exception {
		// FIXME: migrate to use Spring dependency injection
		/*
		 * this.vvs = new VerVarStoreImpl(homePath, this);
		 * 
		 * for (AlphaCardDescriptor alphaCardDescriptor : getListOfACDs()) {
		 * String spType = alphaCardDescriptor.getAlphaAdornment(
		 * CorpusGenericus.SYNTACTICPAYLOADTYPE.value()).getValue(); if (spType
		 * != null) { // FIXME: eliminate setSptype/setVersion
		 * vvs.setSptype(spType);
		 * vvs.setVersion(alphaCardDescriptor.getAlphaAdornment(
		 * CorpusGenericus.VERSION.value()).getValue());
		 * vvs.load(alphaCardDescriptor.getId(), alphaCardDescriptor
		 * .getAlphaAdornment(CorpusGenericus.VERSION.value()) .getValue()); }
		 * String fsType = alphaCardDescriptor.getAlphaAdornment(
		 * CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value()).getValue(); if
		 * (fsType.equals(FundamentalSemanticType.COORDINATION.value())) {
		 * Payload payload = vvs.getPayload(alphaCardDescriptor.getId());
		 * ksession.insert(payload); } } ksession.insert(vvs);
		 * ksession.fireAllRules(); return true;
		 */
		this.homePath = homePath;
		final String episodeId = this.getAlphaConfig().getMyEpisodeId();
		this.hwf = new WorkspaceImpl(new File(homePath + "/" + episodeId));
		final HydraFacade hvs = new HydraFacade(new File(homePath + "/"
				+ episodeId));

		for (final AlphaCardDescriptor alphaCardDescriptor : this
				.getListOfACDs()) {
			final String spType = alphaCardDescriptor.readAdornment(
					CorpusGenericus.SYNTACTICPAYLOADTYPE.value()).getValue();
			if (spType != null) {
				// vvs.setSptype(spType);
				// vvs.setVersion(alphaCardDescriptor.getAdaptiveAdornment(
				// CorpusGenericus.VERSION.value()).getValue());
				this.hwf.loadDescriptor(alphaCardDescriptor);
			}
			final String fsType = alphaCardDescriptor.readAdornment(
					CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value()).getValue();
			if (fsType.equals(FundamentalSemanticType.COORDINATION.value())) {
				final Payload payload = this.hwf.loadPayload(
						alphaCardDescriptor, new Payload());
				this.ksession.insert(payload);
			}
		}
		this.ksession.insert(this.hwf);
		this.ksession.insert(hvs);

		final VersionMapHistoryUtility vvu = new VersionMapHistoryUtility(
				this.hwf, true);
		this.ksession.insert(vvu);

		this.ksession.fireAllRules();
		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#initiateJoin()
	 */
	@Override
	public void initiateJoin() {
		AlphaPropsFacadeImpl.LOGGER.info("Initiating Join");

		final File pubKeyRing = new File(this.homePath + "publicKeyRing.asc");
		if (!pubKeyRing.exists()) {
			AlphaPropsFacadeImpl.LOGGER
					.info("Copying existing public key ring file into alphaDoc.");
			try {
				AlphaPropsFacadeImpl.copyFile(new File(this.getAlphaConfig()
						.getPkPath()), pubKeyRing);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else {
			AlphaPropsFacadeImpl.LOGGER
					.info("Importing public keys into existing public key file.");
			try {
				this.ovnFacade.importCryptographyMetadata(new FileInputStream(
						this.getAlphaConfig().getPkPath()));
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		this.insertIntoDrools(this.joinUtil.initiateJoin());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#shutdown()
	 */
	@Override
	public boolean shutdown() {
		try {
			this.ksession.dispose();
			if (this.kRtLogger != null) {
				this.kRtLogger.close();
			}
		} catch (final Throwable t) {
			AlphaPropsFacadeImpl.LOGGER.severe("Error: " + t);
		}

		if (this.ovnFacade != null) {
			this.ovnFacade.shutdown();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#getAlphaDoc()
	 */
	@Override
	public AlphaDoc getAlphaDoc() {

		if (AlphaPropsFacadeImpl.LOGGER.isLoggable(Level.FINER)) {
			AlphaPropsFacadeImpl.LOGGER.finer("Number of Objects in the WM: "
					+ this.ksession.getObjects().size());
		}

		final QueryResults resultsAD = this.ksession
				.getQueryResults("alphaDoc");
		for (final QueryResultsRow rowAD : resultsAD) {
			final AlphaDoc ad = (AlphaDoc) rowAD.get("ad");
			return ad;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#getCRA()
	 */
	@Override
	public CRAPayload getCRA() {
		return (CRAPayload) this.getPayload(this.getAlphaDoc().getCoordCardId(
				CoordCardType.CRA));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#getPSA()
	 */
	@Override
	public PSAPayload getPSA() {
		return (PSAPayload) this.getPayload(this.getAlphaDoc().getCoordCardId(
				CoordCardType.PSA));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#getAPA()
	 */
	@Override
	public APAPayload getAPA() {
		return (APAPayload) this.getPayload(this.getAlphaDoc().getCoordCardId(
				CoordCardType.APA));
	}

	/**
	 * Gets the payload.
	 * 
	 * @param aci
	 *            the aci
	 * @return the payload
	 */
	private Payload getPayload(final AlphaCardID aci) {

		try {
			this.hwf = (Workspace) this.ksession.getObject(this.ksession
					.getFactHandle(this.hwf));
			final AlphaCardDescriptor acd = this.getAlphaCard(aci);
			// String fileExtension = acd.readAdornment(
			// CorpusGenericus.SYNTACTICPAYLOADTYPE.value()).getValue();
			return this.hwf.loadPayload(acd, new Payload());
		} catch (final Exception e) {
			AlphaPropsFacadeImpl.LOGGER.severe(e.toString());
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#getAlphaCard(alpha.model.identification
	 * .AlphaCardIdentifier)
	 */
	@Override
	public AlphaCardDescriptor getAlphaCard(final AlphaCardID alphaCardID)
			throws Exception {

		final Object[] args = new Object[1];
		args[0] = alphaCardID;
		final QueryResults resultsACs = this.ksession.getQueryResults(
				"alphaCardDescriptorByID", args);

		for (final QueryResultsRow rowAC : resultsACs) {
			final AlphaCardDescriptor ac = (AlphaCardDescriptor) rowAC
					.get("ac");
			return ac;
		}

		// USING ClassObjectFilter
		// Collection<Object> alphaCardFacts =
		// ksession.getObjects(alphaCardObjFilter);
		// for (Object alphaCardFact : alphaCardFacts) {
		// if (((AlphaCardDescriptor)
		// alphaCardFact).getId().equals(alphaCardID)) {
		// return (AlphaCardDescriptor) alphaCardFact;
		// }
		// }
		throw new Exception("AlphaCardDescriptor not found!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#getListOfACDs()
	 */
	@Override
	public List<AlphaCardDescriptor> getListOfACDs() {

		final ArrayList<AlphaCardDescriptor> acdlist = new ArrayList<AlphaCardDescriptor>();

		final QueryResults resultsACs = this.ksession
				.getQueryResults("alphaCardDescriptors");
		for (final QueryResultsRow rowAC : resultsACs) {
			final AlphaCardDescriptor ac = (AlphaCardDescriptor) rowAC
					.get("ac");
			acdlist.add(ac);
		}

		return acdlist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#addAlphaCard(alpha.model.AlphaCardDescriptor
	 * )
	 */
	@Override
	public void addAlphaCard(final AlphaCardDescriptor alphaCardDescriptor)
			throws Exception {
		if (!(this.getPSA()).getListOfTodoItems().contains(
				alphaCardDescriptor.getId())) {

			final AddAlphaCardEvent aace = new AddAlphaCardEvent(
					alphaCardDescriptor);
			aace.setPropagateChange(true);
			this.ksession.insert(aace);
			this.ksession.fireAllRules();

		} else {
			throw new Exception("AlphaCardDescriptor already in the AlphaDoc!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#createNewAlphaCard()
	 */
	@Override
	public AlphaCardDescriptor createNewAlphaCardDescriptor() {
		final APAPayload apaPl = this.getAPA();
		final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();
		return aaf.cloneAlphaCardDescriptor(apaPl
				.getContentCardAdornmentPrototype());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#addRelationship(alpha.model.identification
	 * .AlphaCardIdentifier, alpha.model.identification.AlphaCardID,
	 * alpha.model.apa.AlphaCardRelationshipType)
	 */
	@Override
	public void addRelationship(final AlphaCardID src, final AlphaCardID dst,
			final AlphaCardRelationshipType type) throws Exception {
		final AlphaDoc currentAlphaDoc = this.getAlphaDoc();
		final AlphaCardRelationship newRelationship = new AlphaCardRelationship(
				src, dst, type);

		if (!this.getPSA().getListOfTodoRelationships()
				.contains(newRelationship)) {
			final ChangePayloadEvent cpe = new ChangePayloadEvent(
					currentAlphaDoc.getCoordCardId(CoordCardType.PSA),
					newRelationship, this.getAlphaCard(currentAlphaDoc
							.getCoordCardId(CoordCardType.PSA)));
			cpe.setPropagateChange(true);
			this.ksession.insert(cpe);
			this.ksession.fireAllRules();
		} else {
			throw new Exception("Relationship already exists!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#addParticipant(alpha.model.cra.ParticipantNode
	 * )
	 */
	@Override
	public void addParticipant(final Participant participantNode)
			throws Exception {
		if (this.getAlphaConfig().isInvitation()) {
			final ChangePayloadEvent cpe = new ChangePayloadEvent(
					new AlphaCardID(this.getAlphaConfig().getMyEpisodeId(),
							CoordCardType.CRA), participantNode);
			cpe.setPropagateChange(true);
			this.ksession.insert(cpe);
			this.ksession.fireAllRules();
		} else {
			if (!this.getCRA().getListOfParticipants()
					.contains(participantNode)) {
				final ChangePayloadEvent cpe = new ChangePayloadEvent(
						new AlphaCardID(this.getAlphaConfig().getMyEpisodeId(),
								CoordCardType.CRA), participantNode);

				// currentAlphaDoc.getCoordCardId(CoordCardType.CRA),
				// participantNode);
				cpe.setPropagateChange(true);
				this.ksession.insert(cpe);
				this.ksession.fireAllRules();
			} else {
				throw new Exception("ParticipantNode already exists!");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#updateParticipantPort(alpha.model.cra.
	 * ParticipantNode, alpha.model.cra.ParticipantNode)
	 */
	@Override
	public void updateParticipantPort(final Participant oldParticipant,
			final Participant newParticipant) throws Exception {
		final AlphaDoc currentAlphaDoc = this.getAlphaDoc();
		if (this.getCRA().getListOfParticipants().contains(oldParticipant)) {
			final ChangePayloadEvent cpe = new ChangePayloadEvent(
					currentAlphaDoc.getCoordCardId(CoordCardType.CRA),
					newParticipant);
			// cpe.setPropagateChange(true);
			cpe.setPropagateChange(false); // the change will not be propagated
			// until a new start takes place
			this.ksession.insert(cpe);
			this.ksession.fireAllRules();
		} else {
			throw new Exception(
					"ParticipantNode cannot be altered, because it does not exist!");
		}

		// FIXME
		// String newPort = newParticipant.getNode().getPort();
		// updateServiceReceiver.setPort(Integer.valueOf(newPort));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#setPayload(alpha.model.identification.
	 * AlphaCardID, java.lang.Object, java.util.Map)
	 */
	@Override
	public void setPayload(final AlphaCardID alphaCardID, final Object payload,
			final Map<String, String> adornmentChangeRequests) {
		try {
			if (adornmentChangeRequests != null) {
				this.updateAdornmentInstances(alphaCardID,
						adornmentChangeRequests);
			}

			final ChangePayloadEvent cpe = new ChangePayloadEvent(alphaCardID,
					payload, (AlphaCardDescriptor) this.getClone(
							this.getAlphaCard(alphaCardID), "alpha.model"));
			if (AlphaPropsFacadeImpl.LOGGER.isLoggable(Level.FINER)) {
				AlphaPropsFacadeImpl.LOGGER
						.finer("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW "
								+ this.getAlphaCard(alphaCardID).toString());
			}

			// Visibility of the respecting alphaCard is ignored.
			// Reason: To maintain a consistent history at each participating
			// site it is required that artifacts with are identical versionMap
			// share identical contents.
			// -> Payload must be always propagated to ensure this consistency!
			// The alphaCard will still be marked as PRIVATE for other
			// participating sites but the payload is distributed globally!
			// Furthermore this ensures that the payload is available to all
			// participants as soon as the alphaCard is made PUBLIC.
			// if (getAlphaCard(alphaCardID)
			// .readAdornment(CorpusGenericus.VISIBILITY.value())
			// .getValue().equals(Visibility.PUBLIC.value())) {
			cpe.setPropagateChange(true);
			AlphaPropsFacadeImpl.LOGGER
					.finer("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW "
							+ cpe.toString());
			// } else {
			// cpe.setPropagateChange(false);
			// LOGGER.finer("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW "
			// + cpe.toString());
			// }

			this.ksession.insert(cpe);
			this.ksession.fireAllRules();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#getAlphaCardChangeability(alpha.model.
	 * identification.AlphaCardID, java.lang.String)
	 */
	@Override
	public Map<String, Boolean> getAlphaCardChangeability(
			final AlphaCardID alphaCardID) {

		try {
			final AlphaCardDescriptor ac = this.getAlphaCard(alphaCardID);
			final Map<String, Boolean> changeables = new HashMap<String, Boolean>();

			for (final PrototypedAdornment a : ac.readAdornments()) {
				changeables.put(a.getName(), Boolean.TRUE);
			}
			changeables.put(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value(),
					Boolean.FALSE);
			changeables.put(CorpusGenericus.VARIANT.value(), Boolean.FALSE);
			changeables.put(CorpusGenericus.VERSION.value(), Boolean.FALSE);
			changeables.put(CorpusGenericus.VERSIONCONTROL.value(),
					Boolean.FALSE);
			changeables.put(CorpusGenericus.OCID.value(), Boolean.FALSE);

			if (ac.readAdornment(CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
					.getValue() == null) {
				changeables.put(CorpusGenericus.VISIBILITY.value(),
						Boolean.FALSE);
				changeables
						.put(CorpusGenericus.VALIDITY.value(), Boolean.FALSE);
			} else {
				changeables.put(CorpusGenericus.VISIBILITY.value(),
						Boolean.TRUE);
				changeables.put(CorpusGenericus.VALIDITY.value(), Boolean.TRUE);
			}

			if (ac.readAdornment(CorpusGenericus.VISIBILITY.value()).getValue()
					.equalsIgnoreCase(Visibility.PUBLIC.value())) {
				changeables.put(CorpusGenericus.VISIBILITY.value(),
						Boolean.FALSE);
			}

			if (ac.readAdornment(CorpusGenericus.VALIDITY.value()).getValue()
					.equalsIgnoreCase(Validity.VALID.value())) {
				changeables
						.put(CorpusGenericus.VALIDITY.value(), Boolean.FALSE);
			}

			if (ac.readAdornment(
					CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value()).getValue()
					.equalsIgnoreCase(FundamentalSemanticType.CONTENT.value())
					&& ac.readAdornment(CorpusGenericus.DELETED.value())
							.getValue()
							.equalsIgnoreCase(Boolean.TRUE.toString())) {
				for (final PrototypedAdornment a : ac.readAdornments()) {
					if (a.getName().equals(CorpusGenericus.DELETED.value())) {
						changeables.put(a.getName(), Boolean.TRUE);
					} else {
						changeables.put(a.getName(), Boolean.FALSE);
					}
				}

			}

			return changeables;
		} catch (final Exception e) {
			AlphaPropsFacadeImpl.LOGGER.severe(e.toString());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seealpha.facades.AlphaPropsFacade#insertACDs(alpha.model.payload.psa.
	 * PSAPayload, java.lang.String)
	 */
	@Override
	public void insertACDs(final PSAPayload psapl, final String homePath) {

		this.ksession.insert(psapl);

		final Set<AlphaCardID> acilist = psapl.getListOfTodoItems();

		final XmlBinder xb = new XmlBinder();
		AlphaCardDescriptor acd = null;

		for (final AlphaCardID aci : acilist) {

			acd = (AlphaCardDescriptor) xb.load(homePath
					+ this.getAlphaDoc().getEpisodeID() + "/" + aci.getCardID()
					+ "/desc/Descriptor.xml", "alpha.model");
			this.ksession.insert(acd);
			AlphaPropsFacadeImpl.LOGGER.info("inserting AlphaCard into Drools");
		}
		this.ksession.fireAllRules();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#insertIntoDrools(java.lang.Object)
	 */
	@Override
	public void insertIntoDrools(final Object obj) {

		this.ksession.insert(obj);
		this.ksession.fireAllRules();

	}

	/**
	 * Sets the aPA.
	 * 
	 * @param apa
	 *            the new aPA
	 */
	private void setAPA(final AlphaCardDescriptor apa) {
		final ChangePayloadEvent cpe = new ChangePayloadEvent(this
				.getAlphaDoc().getCoordCardId(CoordCardType.APA), apa);
		cpe.setPropagateChange(true);
		this.ksession.insert(cpe);
		this.ksession.fireAllRules();
	}

	/**
	 * Gets the list of content ac ds.
	 * 
	 * @return the list of content ac ds
	 */
	private List<AlphaCardDescriptor> getListOfContentACDs() {
		final ArrayList<AlphaCardDescriptor> cacdlist = new ArrayList<AlphaCardDescriptor>();
		for (final AlphaCardDescriptor ac : this.getListOfACDs()) {
			final String fsType = ac.readAdornment(
					CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value()).getValue();
			if (fsType.equals(FundamentalSemanticType.CONTENT.value())) {
				cacdlist.add(ac);
			}
		}
		return cacdlist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#getAlphaConfig()
	 */
	@Override
	public AlphadocConfig getAlphaConfig() {

		final QueryResults resultsAC = this.ksession
				.getQueryResults("alphaConfig");
		for (final QueryResultsRow rowAC : resultsAC) {
			final AlphadocConfig ac = (AlphadocConfig) rowAC.get("ac");
			return ac;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#getJarFile()
	 */
	@Override
	public File getJarFile() {

		final QueryResults resultsAC = this.ksession.getQueryResults("jarFile");
		for (final QueryResultsRow rowAC : resultsAC) {
			final File jarFile = (File) rowAC.get("jarFile");
			return jarFile;
		}

		return null;
	}

	/**
	 * Gets a clone of the object.
	 * 
	 * @param toClone
	 *            the to clone
	 * @param schema
	 *            TODO
	 * @return the clone of the object
	 */
	public final Object getClone(final Object toClone, final String schema) {
		Object clone = null;
		// String schema = "alpha.model";
		final XmlBinder xmlb = new XmlBinder();

		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(bos);
			xmlb.store(toClone, out, schema);

			final ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			clone = xmlb.load(in, schema);

			bos.close();
		} catch (final IOException e) {
			AlphaPropsFacadeImpl.LOGGER.severe("Error: " + e);
		}
		AlphaPropsFacadeImpl.LOGGER.finer("The clone is: " + clone);
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#updateAdornmentInstances(alpha.model.
	 * identification.AlphaCardID, java.util.Map)
	 */
	@Override
	public void updateAdornmentInstances(final AlphaCardID alphaCardID,
			final Map<String, String> loAdornments)
			throws IllegalChangeException {
		AlphaCardDescriptor descriptor;
		try {
			descriptor = this.getAlphaCard(alphaCardID);
		} catch (final Exception e) {
			AlphaPropsFacadeImpl.LOGGER.warning(e.getMessage() + " ID: "
					+ alphaCardID);
			throw new IllegalChangeException(e.getMessage() + " ID: "
					+ alphaCardID);
		}

		/* first validate changes */
		final String validationResult = this.validatePrivileges(alphaCardID,
				loAdornments.keySet());
		// TODO value validation
		if (validationResult != "") {
			throw new IllegalChangeException(validationResult);
		}

		/* then set new values for descriptor */
		for (final Map.Entry<String, String> e : loAdornments.entrySet()) {
			descriptor.readAdornment(e.getKey()).setValue(e.getValue());
		}
		this.updateAlphaCardDescriptor(alphaCardID, descriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#updateAdornmentSchema(alpha.model.
	 * identification.AlphaCardID, java.util.Map)
	 */
	@Override
	public void updateAdornmentSchema(final AlphaCardID alphaCardID,
			final Map<String, Boolean> loAdornments)
			throws IllegalChangeException {
		AlphaCardDescriptor descriptor;
		try {
			descriptor = this.getAlphaCard(alphaCardID);
		} catch (final Exception e) {
			AlphaPropsFacadeImpl.LOGGER.warning(e.getMessage() + " ID: "
					+ alphaCardID);
			throw new IllegalChangeException(e.getMessage() + " ID: "
					+ alphaCardID);
		}

		/* first validate changes */
		final String validationResult = this.validatePrivileges(alphaCardID,
				loAdornments.keySet());
		if (validationResult != "") {
			throw new IllegalChangeException(validationResult);
		}

		/* then set new instance flags for descriptor */
		for (final Map.Entry<String, Boolean> e : loAdornments.entrySet()) {
			descriptor.readAdornment(e.getKey()).setInstance(e.getValue());
		}
		this.updateAlphaCardDescriptor(alphaCardID, descriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaPropsFacade#updateAPA(java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public void updateAPA(final Map<String, PrototypedAdornment> loAdornments,
			final Map<String, String> loAdornmentValues)
			throws IllegalChangeException {
		final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();
		final AlphaCardDescriptor oldApa = this.getAPA()
				.getContentCardAdornmentPrototype();
		final AlphaCardDescriptor newApa = aaf.cloneAlphaCardDescriptor(oldApa);

		/*
		 * first validate changes The generic adornments of the APA can't be
		 * changed
		 */
		String notifications = "";
		for (final String name : loAdornments.keySet()) {
			final PrototypedAdornment a = newApa.readAdornment(name);
			if ((a != null)
					&& a.getConsensusScope().equals(ConsensusScope.GENERIC_STD)) {
				notifications += "You are not allowed to change adornment'"
						+ name + "' of the APA!\n";
			}
		}
		// TODO default value validation
		if (notifications != "") {
			throw new IllegalChangeException(notifications);
		}

		/* then set new adornments for APA */
		for (final Map.Entry<String, PrototypedAdornment> e : loAdornments
				.entrySet()) {
			final String adornmentName = e.getKey();
			final PrototypedAdornment adornment = e.getValue();
			final String adornmentValue = loAdornmentValues.get(adornmentName);
			if (adornment == null) { // this adornment should be deleted from
				// the APA
				newApa.deleteAdornment(adornmentName);
			} else if (newApa.readAdornment(adornmentName) == null) { // this
				// adornment
				// should
				// be
				// added
				// to
				// the
				// APA
				adornment.setValue(adornmentValue);
				newApa.updateOrCreateAdornment(adornment);
			} else { // this adornment should be updated
				newApa.deleteAdornment(adornmentName);
				adornment.setValue(adornmentValue);
				newApa.updateOrCreateAdornment(adornment);
			}
		}
		this.setAPA(newApa);

		/* then update also all descriptors */
		for (final AlphaCardDescriptor descriptor : this.getListOfContentACDs()) {
			for (final Map.Entry<String, PrototypedAdornment> e : loAdornments
					.entrySet()) {
				final String adornmentName = e.getKey();
				final PrototypedAdornment newApaAdornment = e.getValue();
				final String adornmentValue = loAdornmentValues
						.get(adornmentName);
				if (newApaAdornment == null) { // this adornment should be
					// deleted from the descriptor
					descriptor.deleteAdornment(adornmentName);
				} else if (descriptor.readAdornment(adornmentName) == null) { // this
					// adornment
					// should
					// be
					// added
					// deep copy => all descriptors use own instance!
					newApaAdornment.setValue(adornmentValue);
					final PrototypedAdornment clone = aaf
							.cloneAdornment(newApaAdornment);
					descriptor.updateOrCreateAdornment(clone);
				} else { // this adornment should be updated
					newApaAdornment.setValue(adornmentValue);
					final PrototypedAdornment oldApaAdornment = oldApa
							.readAdornment(adornmentName);
					final PrototypedAdornment cardAdornment = descriptor
							.readAdornment(adornmentName);
					final PrototypedAdornment pa = aaf.updateAdornment(
							cardAdornment, newApaAdornment, oldApaAdornment);
					descriptor.updateOrCreateAdornment(pa);
				}
			}
			//TODO create single event for list of changes (all descriptors). 
			this.updateAlphaCardDescriptor(descriptor.getId(), descriptor);
		}
	}

	/**
	 * Validate privileges.
	 * 
	 * @param alphaCardID
	 *            the alpha card id
	 * @param adornments
	 *            the adornments
	 * @return the string
	 */
	private String validatePrivileges(final AlphaCardID alphaCardID,
			final Set<String> adornments) {
		final Map<String, Boolean> changeables = this
				.getAlphaCardChangeability(alphaCardID);
		String notifications = "";

		for (final String s : adornments) {
			if ((changeables != null) && changeables.containsKey(s)
					&& !changeables.get(s)) {
				notifications += "You are not allowed to change '" + s
						+ "' of alphaCardDescriptor " + alphaCardID + "!\n";
			}
		}
		return notifications;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#updateAlphaCardDescriptor(alpha.model.
	 * identification.AlphaCardID, alpha.model.AlphaCardDescriptor)
	 */
	@Override
	public void updateAlphaCardDescriptor(final AlphaCardID alphaCardID,
			final AlphaCardDescriptor descriptor) {
		final ChangeAlphaCardDescriptorEvent casEvent = new ChangeAlphaCardDescriptorEvent(
				alphaCardID, (AlphaCardDescriptor) this.getClone(descriptor,
						"alpha.model"));

		casEvent.setPropagateChange(true);
		this.ksession.insert(casEvent);
		this.ksession.fireAllRules();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#updateTokens(alpha.model.cra.Participant,
	 * java.util.Map)
	 */
	@Override
	public void updateTokens(final Participant participant,
			final Map<String, String> loTokens, boolean propagateChange)
			throws Exception {

		LOGGER.finer("APF___________updateTokens():participant: "
				+ participant.getContributor().getActor());
		LOGGER.finer("APF___________updateTokens():loTokens: " + loTokens);
		LOGGER.finer("APF___________updateTokens():propagateChange: "
				+ propagateChange);

		for (final Map.Entry<String, String> entry : loTokens.entrySet()) {
			participant.readToken(entry.getKey()).setValue(entry.getValue());
			participant.readToken(entry.getKey()).setVersionMap(
					new VersionMap());
		}

		final AlphaDoc currentAlphaDoc = this.getAlphaDoc();
		final ChangePayloadEvent cpe = new ChangePayloadEvent(
				currentAlphaDoc.getCoordCardId(CoordCardType.CRA), participant,
				this.getAlphaCard(currentAlphaDoc
						.getCoordCardId(CoordCardType.CRA)));

		cpe.setPropagateChange(propagateChange);
		this.insertIntoDrools(cpe);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaPropsFacade#propagateToken(alpha.model.cra.Participant
	 * , alpha.model.cra.Token)
	 */
	@Override
	public void propagateToken(final Participant receiver, String tokenName) throws Exception {
		String actor = this.getAlphaConfig().getLocalNodeID().getContributor()
				.getActor();
		Token receiverToken = TokenUtility.cloneToken(this
				.getParticipantByActor(actor).readToken(
						tokenName));

		if (receiver != null) {
			AlphaPropsFacadeImpl.LOGGER
					.info("Propagate Token - " + tokenName + "receiverToken:VersionMap: " + receiverToken.getVersionMap());
			this.insertIntoDrools(this.tokenUtil.generateTokenPropagation(
					receiver, receiverToken));
		}

	}

	public void updateToken(Participant participant, Token token,
			boolean propagateChange) {

		participant.updateOrCreateToken(token);

		final AlphaDoc currentAlphaDoc = this.getAlphaDoc();
		ChangePayloadEvent cpe;
		try {
			cpe = new ChangePayloadEvent(
					currentAlphaDoc.getCoordCardId(CoordCardType.CRA),
					participant, this.getAlphaCard(currentAlphaDoc
							.getCoordCardId(CoordCardType.CRA)));
			cpe.setPropagateChange(propagateChange);
			this.insertIntoDrools(cpe);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Copies a file to a new location preserving the file date.
	 * <p>
	 * This method copies the contents of the specified source file to the
	 * specified destination file. The directory holding the destination file is
	 * created if it does not exist. If the destination file exists, then this
	 * method will overwrite it.
	 * <p>
	 * <strong>Note:</strong> This method tries to preserve the file's last
	 * modified date/times using {@link File#setLastModified(long)}, however it
	 * is not guaranteed that the operation will succeed. If the modification
	 * operation fails, no indication is provided.
	 * 
	 * @param srcFile
	 *            an existing file to copy, must not be <code>null</code>
	 * @param destFile
	 *            the new file, must not be <code>null</code>
	 * 
	 * @throws NullPointerException
	 *             if source or destination is <code>null</code>
	 * @throws IOException
	 *             if source or destination is invalid
	 * @throws IOException
	 *             if an IO error occurs during copying
	 */
	public static void copyFile(final File srcFile, final File destFile)
			throws IOException {
		AlphaPropsFacadeImpl.copyFile(srcFile, destFile, true);
	}

	/**
	 * Copies a file to a new location.
	 * <p>
	 * This method copies the contents of the specified source file to the
	 * specified destination file. The directory holding the destination file is
	 * created if it does not exist. If the destination file exists, then this
	 * method will overwrite it.
	 * <p>
	 * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
	 * <code>true</code> tries to preserve the file's last modified date/times
	 * using {@link File#setLastModified(long)}, however it is not guaranteed
	 * that the operation will succeed. If the modification operation fails, no
	 * indication is provided.
	 * 
	 * @param srcFile
	 *            an existing file to copy, must not be <code>null</code>
	 * @param destFile
	 *            the new file, must not be <code>null</code>
	 * @param preserveFileDate
	 *            true if the file date of the copy should be the same as the
	 *            original
	 * 
	 * @throws NullPointerException
	 *             if source or destination is <code>null</code>
	 * @throws IOException
	 *             if source or destination is invalid
	 * @throws IOException
	 *             if an IO error occurs during copying
	 */
	public static void copyFile(final File srcFile, final File destFile,
			final boolean preserveFileDate) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (srcFile.exists() == false) {
			throw new FileNotFoundException("Source '" + srcFile
					+ "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile
					+ "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '"
					+ destFile + "' are the same");
		}
		if ((destFile.getParentFile() != null)
				&& (destFile.getParentFile().exists() == false)) {
			if (destFile.getParentFile().mkdirs() == false) {
				throw new IOException("Destination '" + destFile
						+ "' directory cannot be created");
			}
		}
		if (destFile.exists() && (destFile.canWrite() == false)) {
			throw new IOException("Destination '" + destFile
					+ "' exists but is read-only");
		}
		AlphaPropsFacadeImpl.doCopyFile(srcFile, destFile, preserveFileDate);
	}

	/**
	 * Internal copy file method.
	 * 
	 * @param srcFile
	 *            the validated source file, must not be <code>null</code>
	 * @param destFile
	 *            the validated destination file, must not be <code>null</code>
	 * @param preserveFileDate
	 *            whether to preserve the file date
	 * @throws IOException
	 *             if an error occurs
	 */
	private static void doCopyFile(final File srcFile, final File destFile,
			final boolean preserveFileDate) throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile
					+ "' exists but is a directory");
		}
		final long FIFTY_MB = 1024 * 1024 * 50;

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			final long size = input.size();
			long pos = 0;
			long count = 0;
			while (pos < size) {
				count = (size - pos) > FIFTY_MB ? FIFTY_MB : (size - pos);
				pos += output.transferFrom(input, pos, count);
			}
		} finally {
			AlphaPropsFacadeImpl.closeQuietly(output);
			AlphaPropsFacadeImpl.closeQuietly(fos);
			AlphaPropsFacadeImpl.closeQuietly(input);
			AlphaPropsFacadeImpl.closeQuietly(fis);
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '"
					+ srcFile + "' to '" + destFile + "'");
		}
		if (preserveFileDate) {
			destFile.setLastModified(srcFile.lastModified());
		}
	}

	/**
	 * Unconditionally close a <code>Closeable</code>.
	 * <p>
	 * Equivalent to {@link Closeable#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * Closeable closeable = null;
	 * try {
	 * 	closeable = new FileReader(&quot;foo.txt&quot;);
	 * 	// process closeable
	 * 	closeable.close();
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	IOUtils.closeQuietly(closeable);
	 * }
	 * </pre>
	 * 
	 * @param closeable
	 *            the object to close, may be null or already closed
	 * @since Commons IO 2.0
	 */
	public static void closeQuietly(final Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (final IOException ioe) {
			// ignore
		}
	}

	@Override
	public Participant getParticipantByActor(final String actor) {
		Participant participant = null;

		for (Participant p : this.getCRA().getListOfParticipants()) {
			if (p.getContributor().getActor().equals(actor)) {
				participant = p;
			}
		}
		return participant;
	}

	@Override
	public Participant getCurrentDoyen() {
		Participant doyen = null;
		final List<Participant> participants = new LinkedList<Participant>(this
				.getCRA().getListOfParticipants());
		for (Participant participant : participants) {
			if (AlphaDoyenUtility.checkDoyen(participant)) {
				doyen = participant;
				break;
			}
		}
		return doyen;
	}

	@Override
	public void updateAcknowledgementStructure(AlphaCardID acid,
			VersionMap versionMap, Participant sender,
			LocalTimestamp senderTimestamp, LocalTimestamp receiverTimestamp) {

		AlphaDoc ad = this.getAlphaDoc();
		AcknowledgmentStructure as = ad.getAs(acid, versionMap);

		as.addItem(new AcknowledgementStructureItem(sender.getNodeID(), this
				.getAlphaConfig().getLocalNodeID(), senderTimestamp,
				receiverTimestamp));
	}

	@Override
	public void initializeAcknowledgementStructure(AlphaCardID acid,
			VersionMap versionMap) {
		for (Participant sender : getCRA().getListOfParticipants()) {
			this.updateAcknowledgementStructure(acid, versionMap, sender, null,
					null);
		}
	}

}
