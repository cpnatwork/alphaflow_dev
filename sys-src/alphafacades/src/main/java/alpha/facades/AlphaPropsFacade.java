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
package alpha.facades;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

import alpha.facades.exceptions.IllegalChangeException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.AlphaDoc;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.APAPayload;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.EndpointID;
import alpha.model.cra.Participant;
import alpha.model.cra.Token;
import alpha.model.deliveryacknowledgment.AcknowledgmentStructure;
import alpha.model.deliveryacknowledgment.LocalTimestamp;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationshipType;
import alpha.model.psa.PSAPayload;
import alpha.model.versionmap.VersionMap;

// TODO: Auto-generated Javadoc
/**
 * This interface enables the AlphaDoc Editor to communicate with the AlphaProps
 * rule engine.
 * 
 */
public abstract interface AlphaPropsFacade {

	/**
	 * Send message.
	 * 
	 * @param attachments
	 *            the attachments
	 * @param recipients
	 *            the recipients
	 * @param subject
	 *            the subject
	 * @param messageContent
	 *            the message content
	 * @param encrypt
	 *            the encrypt
	 * @param sign
	 *            the sign
	 */
	public void sendMessage(List<Object> attachments,
			Set<EndpointID> recipients, String subject, String messageContent,
			boolean encrypt, boolean sign);

	/**
	 * Initiate join.
	 */
	public void initiateJoin();

	/**
	 * Gets the home path.
	 * 
	 * @return the home path
	 */
	public String getHomePath();

	/**
	 * This method retrieves the whole AlphaDoc from the AlphaProps working
	 * memory.
	 * 
	 * @return the alpha doc
	 */
	public AlphaDoc getAlphaDoc();

	/**
	 * Gets the alpha card.
	 * 
	 * @param alphaCardID
	 *            the alpha card id
	 * @return the alpha card
	 * @throws Exception
	 *             the exception
	 */
	public AlphaCardDescriptor getAlphaCard(AlphaCardID alphaCardID)
			throws Exception;

	/**
	 * Gets the list of alpha card descriptors.
	 * 
	 * @return the list of alpha card descriptors
	 */
	public List<AlphaCardDescriptor> getListOfACDs();

	/**
	 * Gets the cRA.
	 * 
	 * @return the cRA
	 */
	public CRAPayload getCRA();

	/**
	 * Gets the pSA.
	 * 
	 * @return the pSA
	 */
	public PSAPayload getPSA();

	/**
	 * Gets the aPA.
	 * 
	 * @return the aPA
	 */
	public APAPayload getAPA();

	/**
	 * Insert acds.
	 * 
	 * @param psapl
	 *            the psapl
	 * @param homePath
	 *            the home path
	 */
	public void insertACDs(PSAPayload psapl, String homePath);

	/**
	 * Inserts any object into drools.
	 * 
	 * @param obj
	 *            the obj
	 */
	public void insertIntoDrools(Object obj);

	/**
	 * Gets the alpha card changeability.
	 * 
	 * @param alphaCardID
	 *            the alpha card id
	 * @return the alpha card changeability
	 */
	public Map<String, Boolean> getAlphaCardChangeability(
			AlphaCardID alphaCardID);

	/**
	 * Sets the payload.
	 * 
	 * @param alphaCardID
	 *            the alpha card id
	 * @param payload
	 *            the payload
	 * @param adornmentChangeRequests
	 *            the adornment change requests
	 */
	public void setPayload(AlphaCardID alphaCardID, Object payload,
			Map<String, String> adornmentChangeRequests);

	/**
	 * Adds the relationship.
	 * 
	 * @param srcID
	 *            the src id
	 * @param dstID
	 *            the dst id
	 * @param type
	 *            the type
	 * @throws Exception
	 *             the exception
	 */
	public void addRelationship(AlphaCardID srcID, AlphaCardID dstID,
			AlphaCardRelationshipType type) throws Exception;

	/**
	 * Adds the participant.
	 * 
	 * @param participantNode
	 *            the participant node
	 * @throws Exception
	 *             the exception
	 */
	public void addParticipant(Participant participantNode) throws Exception;

	/**
	 * This method adds an additional AlphaCardDescriptor to the existing
	 * AlphaDoc.
	 * 
	 * @param alphaCardDescriptor
	 *            the alpha card descriptor
	 * @throws Exception
	 *             the exception
	 */
	public void addAlphaCard(AlphaCardDescriptor alphaCardDescriptor)
			throws Exception;

	/**
	 * Creates the new alpha card.
	 * 
	 * @return the alpha card descriptor
	 */
	public AlphaCardDescriptor createNewAlphaCardDescriptor();

	/**
	 * Update the values of the {@link PrototypedAdornment}s of an.
	 * 
	 * @param alphaCardID
	 *            the ID of the {@link AlphaCardDescriptor}
	 * @param loAdornments
	 *            the list of the changed {@link PrototypedAdornment}s
	 * @throws IllegalChangeException
	 *             the illegal change exception {@link AlphaCardDescriptor}.
	 */
	public void updateAdornmentInstances(AlphaCardID alphaCardID,
			Map<String, String> loAdornments) throws IllegalChangeException;

	/**
	 * Update the schema of an {@link AlphaCardDescriptor}.
	 * 
	 * @param alphaCardID
	 *            the ID of the {@link AlphaCardDescriptor}
	 * @param loAdornments
	 *            the list of the {@link PrototypedAdornment}s to update
	 * @throws IllegalChangeException
	 *             the illegal change exception
	 */
	public void updateAdornmentSchema(AlphaCardID alphaCardID,
			Map<String, Boolean> loAdornments) throws IllegalChangeException;

	/**
	 * Update the APA.
	 * 
	 * @param loAdornments
	 *            the list of the {@link PrototypedAdornment}s to update
	 * @param loAdornmentValues
	 *            the lo adornment values
	 * @throws IllegalChangeException
	 *             the illegal change exception
	 */
	public void updateAPA(Map<String, PrototypedAdornment> loAdornments,
			Map<String, String> loAdornmentValues)
			throws IllegalChangeException;

	/**
	 * This method initializes the AlphaProps rule engine.
	 * 
	 * @param port
	 *            the port
	 */
	public void initializeConfig(String port);

	/**
	 * This method initializes the model. This includes the initialization of
	 * the {@link VerVarStoreFacade} and the insertion of the the
	 * 
	 * @param homePath
	 *            the path to the folder with all related files
	 * @return true, if successful
	 * @throws Exception
	 *             the exception {@link VerVarStoreFacade}, the {@link AlphaDoc}
	 *             and all Payloads of Coordination {@link AlphaCardDescriptor}s
	 *             into the
	 */
	public boolean initializeModel(String homePath) throws Exception;

	/**
	 * Adds the observers.
	 * 
	 * @param alphaCardObserver
	 *            the alpha card observer
	 * @param logObserver
	 *            the log observer
	 */
	public void addObservers(Observer alphaCardObserver, Observer logObserver);

	/**
	 * Update participant port.
	 * 
	 * @param oldP
	 *            the old p
	 * @param newP
	 *            the new p
	 * @throws Exception
	 *             the exception
	 */
	public void updateParticipantPort(Participant oldP, Participant newP)
			throws Exception;

	/**
	 * This method shuts the AlphaProps rule engine.
	 * 
	 * @return true, if successful
	 */
	public boolean shutdown();

	/**
	 * Gets the alpha config.
	 * 
	 * @return the alpha config
	 */
	public AlphadocConfig getAlphaConfig();

	/**
	 * Gets the jar file.
	 * 
	 * @return the jar file
	 */
	public File getJarFile();

	/**
	 * Initialize overnet.
	 */
	public void initializeOvernet();

	/**
	 * Update alpha card descriptor.
	 * 
	 * @param id
	 *            the id
	 * @param acd
	 *            the acd
	 */
	public void updateAlphaCardDescriptor(AlphaCardID id,
			AlphaCardDescriptor acd);

	/**
	 * Update tokens.
	 * 
	 * @param participant
	 *            the participant
	 * @param loTokens
	 *            the list of tokens
	 * @param propagateChange
	 *            the propagate change if change should be propagated
	 * @throws Exception
	 *             the exception
	 */
	public void updateTokens(Participant participant,
			Map<String, String> loTokens, boolean propagateChange)
			throws Exception;

	/**
	 * Propagates the named Token from the actor itself to the receiver
	 * 
	 * @param receiver
	 *            the receiver of the sent token
	 * @param tokenName
	 *            name of the token to propagate (e.g. doyen)
	 * @throws Exception
	 *             the exception
	 */
	public void propagateToken(Participant receiver, String tokenName)
			throws Exception;

	/**
	 * Gets the locally stored participant
	 * 
	 * @param actor
	 *            the actorID of the participant to look for
	 * @return the participant
	 */
	public Participant getParticipantByActor(String actor);

	/**
	 * Updates the token
	 * 
	 * @param participant
	 *            whose token should be changed
	 * @param token
	 *            the new token
	 * @param propagateChange
	 *            if change should be propagated to other participants
	 * @throws Exception
	 *             the exception
	 */
	public void updateToken(Participant participant, Token token,
			boolean propagateChange) throws Exception;

	/**
	 * Returns the current doyen
	 * 
	 * @return the participant who is doyen
	 */
	public Participant getCurrentDoyen();

	/**
	 * Adds a new AcknowledgementStructureItem with the given parameters to the
	 * AcknowledgmentStructure with the given acid and versionMap
	 * 
	 * @param acid
	 *            alphacardid to acknowledge
	 * @param versionMap
	 *            which version of the alphacard to acknowledge
	 * @param sender
	 *            the sender of the acknowledgement
	 * @param senderTimestamp
	 *            timestamp when acknowledgement was sent
	 * @param receiverTimestamp
	 *            when was the ack received
	 */
	public void updateAcknowledgementStructure(AlphaCardID acid,
			VersionMap versionMap, Participant sender,
			LocalTimestamp senderTimestamp, LocalTimestamp receiverTimestamp);

	/**
	 * Initializes {@link AcknowledgmentStructure} when new Version is created.
	 * Creating AcknowledgementStructureItem for all Participants
	 * 
	 * @param acid
	 *            for which alphacardid
	 * @param versionMap
	 *            which version of alphacard
	 */
	public void initializeAcknowledgementStructure(AlphaCardID acid,
			VersionMap versionMap);
}
