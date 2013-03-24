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
package alpha.overnet;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.pipeline.Action;
import org.drools.runtime.pipeline.KnowledgeRuntimeCommand;
import org.drools.runtime.pipeline.Pipeline;
import org.drools.runtime.pipeline.PipelineFactory;
import org.drools.runtime.pipeline.Transformer;

import alpha.commons.drools.ResultHandlerImpl;
import alpha.facades.AlphaOvernetFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.APAPayload;
import alpha.model.apa.Priority;
import alpha.model.cra.ContributorID;
import alpha.model.cra.EndpointID;
import alpha.model.cra.ObjectUnderConsideration;
import alpha.model.cra.Participant;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;
import alpha.overnet.event.AddAlphaCardEvent;
import alpha.overnet.event.ChangeAlphaCardDescriptorEvent;
import alpha.overnet.event.ChangePayloadEvent;
import alpha.overnet.event.ParallelJoinCallback;
import alpha.overnet.event.ParallelJoinSynchronisation;
import alpha.overnet.event.ParticipantJoinEvent;
import alpha.overnet.event.SequentialJoinCallback;
import alpha.overnet.event.SequentialJoinSynchronisation;
import alpha.util.StringWrapper;

/**
 * The Class AlphaOvernetLegacyFacade.
 * 
 * @deprecated Facade for socket-based network communication.
 */
@Deprecated
public class AlphaOvernetLegacyFacade implements AlphaOvernetFacade {

	/** The ovn sender. */
	private final OvernetSender ovnSender;

	/** The ovn receiver. */
	private final OvernetReceiver ovnReceiver;

	/** The Constant classes. */
	@SuppressWarnings({ "unchecked" })
	private final Class[] classes = new Class[] { AlphaCardDescriptor.class,
			AddAlphaCardEvent.class, ParticipantJoinEvent.class,
			ChangeAlphaCardDescriptorEvent.class, ChangePayloadEvent.class,
			Participant.class, AlphaCardID.class, AlphaCardRelationship.class,
			Payload.class, Priority.class, ContributorID.class,
			ObjectUnderConsideration.class, StringWrapper.class,
			SequentialJoinCallback.class, SequentialJoinSynchronisation.class,
			ParallelJoinCallback.class, ParallelJoinSynchronisation.class,
			APAPayload.class, PSAPayload.class };

	/**
	 * Instantiates a new {@link AlphaOvernetLegacyFacade}.
	 * 
	 * @param sfkSession
	 *            the sfk session
	 * @param config
	 *            the config
	 */
	public AlphaOvernetLegacyFacade(final StatefulKnowledgeSession sfkSession,
			final AlphadocConfig config) {

		this.ovnSender = new TCPUpdateServiceSender();
		this.ovnReceiver = new TCPUpdateServiceReceiver(Integer.parseInt(config
				.getLocalNodeID().getNode().getPort()), sfkSession,
				this.createPipeline(sfkSession), new ResultHandlerImpl());

		sfkSession.setGlobal("ovnFacade", this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#sendUpdate(java.lang.Object,
	 * java.util.Set)
	 */
	@Override
	public boolean sendUpdate(final Object object,
			final Set<EndpointID> recipients) {

		this.ovnSender.sendUpdate(object, recipients);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#receiveUpdates()
	 */
	@Override
	public void receiveUpdates() {

		final Thread receiverThread = new Thread((Runnable) this.ovnReceiver);
		receiverThread.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#delegateUpdate(java.lang.Object)
	 */
	@Override
	public void delegateUpdate(final Object update) {
		// not used

	}

	/**
	 * Creates the pipeline.
	 * 
	 * @param sfkSession
	 *            the sfk session
	 * @return the pipeline
	 */
	private Pipeline createPipeline(final StatefulKnowledgeSession sfkSession) {

		// Make the results (here: FactHandles) available to the user
		final Action executeResultHandler = PipelineFactory
				.newExecuteResultHandler();

		// Insert the object into the session associated with the
		// PipelineContext
		final KnowledgeRuntimeCommand insertStage = PipelineFactory
				.newStatefulKnowledgeSessionInsert();
		insertStage.setReceiver(executeResultHandler);

		Unmarshaller unmarshaller = null;
		try {
			// JAXBContext jaxbCtx =
			// JAXBContext.newInstance("alpha.overnet.event:alpha.model");
			final JAXBContext jaxbCtx = JAXBContext.newInstance(this.classes,
					null);
			unmarshaller = jaxbCtx.createUnmarshaller();
		} catch (final JAXBException e) {
			e.printStackTrace();
		}

		// Create the transformer instance and create the Transformer stage
		// where XML-objects get transformed into POJOs
		final Transformer transformer = PipelineFactory
				.newJaxbFromXmlTransformer(unmarshaller);
		transformer.setReceiver(insertStage);

		// Create the start adapter Pipeline for StatefulKnowledgeSessions
		final Pipeline pipeline = PipelineFactory
				.newStatefulKnowledgeSessionPipeline(sfkSession);
		pipeline.setReceiver(transformer);

		return pipeline;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#shutdown()
	 */
	@Override
	public void shutdown() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#sendUpdate(java.util.List,
	 * java.util.Set, java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public boolean sendUpdate(final List<Object> objects,
			final Set<EndpointID> recipients, final String subject,
			final String messageContent, final boolean encrypt,
			final boolean sign) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#importCryptographyMetadata(java.io.
	 * InputStream)
	 */
	@Override
	public void importCryptographyMetadata(final InputStream input) {
		// TODO Auto-generated method stub

	}
}
