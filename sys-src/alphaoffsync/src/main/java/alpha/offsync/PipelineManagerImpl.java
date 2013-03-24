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
package alpha.offsync;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.pipeline.Action;
import org.drools.runtime.pipeline.KnowledgeRuntimeCommand;
import org.drools.runtime.pipeline.Pipeline;
import org.drools.runtime.pipeline.PipelineFactory;
import org.drools.runtime.pipeline.ResultHandler;
import org.drools.runtime.pipeline.Transformer;
import java.util.logging.Logger;


import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.Priority;
import alpha.model.cra.ContributorID;
import alpha.model.cra.ObjectUnderConsideration;
import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;
import alpha.overnet.PipelineManager;
import alpha.overnet.event.AddAlphaCardEvent;
import alpha.overnet.event.ChangeAlphaCardDescriptorEvent;
import alpha.overnet.event.ChangePayloadEvent;
import alpha.overnet.event.DeliveryAcknowledgement;
import alpha.overnet.event.ParallelJoinCallback;
import alpha.overnet.event.ParallelJoinSynchronisation;
import alpha.overnet.event.ParticipantJoinEvent;
import alpha.overnet.event.SequentialJoinCallback;
import alpha.overnet.event.SequentialJoinSynchronisation;
import alpha.overnet.event.TokenPropagation;
import alpha.util.StringWrapper;

/**
 * Implementation of the {@link PipelineManager} interface.
 */
public class PipelineManagerImpl implements PipelineManager {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(PipelineManagerImpl.class.getName());

	/** The Constant classes. */
	@SuppressWarnings({ "unchecked" })
	private static final Class[] classes = new Class[] {
			AlphaCardDescriptor.class, AddAlphaCardEvent.class,
			ParticipantJoinEvent.class, ChangeAlphaCardDescriptorEvent.class,
			ChangePayloadEvent.class, Participant.class, AlphaCardID.class,
			AlphaCardRelationship.class, Payload.class, Priority.class,
			ContributorID.class, ObjectUnderConsideration.class,
			StringWrapper.class, SequentialJoinCallback.class,
			SequentialJoinSynchronisation.class, ParallelJoinCallback.class,
			ParallelJoinSynchronisation.class, PSAPayload.class, TokenPropagation.class, DeliveryAcknowledgement.class };

	/** The locally running {@link StatefulKnowledgeSession}. */
	private final StatefulKnowledgeSession sfkSession;

	/** The result handler for the {@link StatefulKnowledgeSession}. */
	private final ResultHandler resultHandler;

	/**
	 * The pipeline used for delegating updates into the.
	 * {@link StatefulKnowledgeSession}.
	 */
	private final Pipeline pipeline;

	/**
	 * Instantiates a new {@link PipelineManagerImpl}.
	 * 
	 * @param sfkSession
	 *            the locally running {@link StatefulKnowledgeSession}
	 * @param resulthandler
	 *            the result handler for the {@link StatefulKnowledgeSession}
	 */
	public PipelineManagerImpl(final StatefulKnowledgeSession sfkSession,
			final ResultHandler resulthandler) {
		this.sfkSession = sfkSession;
		this.pipeline = this.createPipeline(sfkSession);
		this.resultHandler = resulthandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.PipelineManager#createPipeline(org.drools.runtime.
	 * StatefulKnowledgeSession)
	 */
	@Override
	public Pipeline createPipeline(final StatefulKnowledgeSession sfkSession) {

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
			final JAXBContext jaxbCtx = JAXBContext.newInstance(
					PipelineManagerImpl.classes, null);
			unmarshaller = jaxbCtx.createUnmarshaller();
		} catch (final JAXBException e) {
			PipelineManagerImpl.LOGGER.severe("Error: " + e);
		}

		// Create the transformer instance and create the Transformer stage
		// where XML-objects get transformed into POJOs
		final Transformer transformer = PipelineFactory
				.newJaxbFromXmlTransformer(unmarshaller);
		transformer.setReceiver(insertStage);

		// Create the start adapter Pipeline for StatefulKnowledgeSessions
		final Pipeline pipe = PipelineFactory
				.newStatefulKnowledgeSessionPipeline(sfkSession);
		pipe.setReceiver(transformer);

		return pipe;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.PipelineManager#delegateUpdate(java.lang.Object)
	 */
	@Override
	public void delegateUpdate(final Object update) {
		this.pipeline.insert(update, this.resultHandler);
		this.sfkSession.fireAllRules();
	}

}
