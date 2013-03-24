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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.logging.Logger;

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.APAPayload;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.Priority;
import alpha.model.cra.ContributorID;
import alpha.model.cra.ObjectUnderConsideration;
import alpha.model.cra.Participant;
import alpha.model.cra.Token;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;
import alpha.model.timestamp.LogicalTimestamp;
import alpha.model.timestamp.Occurrence;
import alpha.model.versionmap.VersionMap;
import alpha.overnet.event.AddAlphaCardEvent;
import alpha.overnet.event.ChangeAlphaCardDescriptorEvent;
import alpha.overnet.event.ChangePayloadEvent;
import alpha.overnet.event.ParallelJoinCallback;
import alpha.overnet.event.ParallelJoinSynchronisation;
import alpha.overnet.event.ParticipantJoinEvent;
import alpha.overnet.event.SequentialJoinCallback;
import alpha.overnet.event.SequentialJoinSynchronisation;
import alpha.util.StringWrapper;

// TODO: Auto-generated Javadoc
/**
 * Offers functionality to generate all necessary types of different join
 * messages.
 */
public class JoinUtility {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(JoinUtility.class.getName());

	/** The classes. */
	private final Class<?>[] classes = new Class[] { AlphaCardDescriptor.class,
			AddAlphaCardEvent.class, ParticipantJoinEvent.class,
			ChangeAlphaCardDescriptorEvent.class, ChangePayloadEvent.class,
			Participant.class, AlphaCardID.class, AlphaCardRelationship.class,
			Payload.class, Priority.class, ContributorID.class,
			ObjectUnderConsideration.class, StringWrapper.class,
			SequentialJoinCallback.class, SequentialJoinSynchronisation.class,
			ParallelJoinCallback.class, ParallelJoinSynchronisation.class,
			APAPayload.class, PSAPayload.class };

	/** The global {@link AlphaPropsFacade}. */
	private final AlphaPropsFacade apf;

	/**
	 * Instantiates a new {@link JoinUtility}.
	 * 
	 * @param apf
	 *            the global {@link AlphaPropsFacade}
	 */
	public JoinUtility(final AlphaPropsFacade apf) {
		this.apf = apf;
	}

	/**
	 * Gets the local participant.
	 * 
	 * @return the local participant
	 */
	private Participant getLocalParticipant() {
		// Set<Participant> participants = apf.getCRA().getListOfParticipants();
		// Iterator<Participant> it = participants.iterator();
		//
		// Participant participant = null;
		// while (it.hasNext()) {
		// participant = (Participant) it.next();
		// if (participant.getContributor().getActor() == apf.getAlphaConfig()
		// .getMyCurrentUser().getName()) {
		// break;
		// }
		// }
		final Participant participant = this.apf.getParticipantByActor(this.apf
				.getAlphaConfig().getLocalNodeID().getContributor().getActor());
		return participant;
	}

	/**
	 * Gets the latest information about all locally known AlphaCards.
	 * 
	 * @return the latest information about all locally known AlphaCards
	 */
	private Map<AlphaCardID, VersionMap> getLocallyKnownAlphaCards() {
		final List<AlphaCardDescriptor> acds = this.apf.getListOfACDs();
		final Map<AlphaCardID, VersionMap> knownAlphaCards = new HashMap<AlphaCardID, VersionMap>();
		for (final AlphaCardDescriptor acd : acds) {
			if (acd.getId().getCardID().equals(CoordCardType.CRA.id())) {
				continue;
			}

			knownAlphaCards.put(acd.getId(), acd.getVersionMap());
		}

		return knownAlphaCards;
	}

	/**
	 * Initiates a new join process.
	 * 
	 * @return the initial {@link SequentialJoinCallback}
	 */
	public SequentialJoinCallback initiateJoin() {

		final Participant participant = this.getLocalParticipant();
		final Map<AlphaCardID, VersionMap> knownAlphaCards = this
				.getLocallyKnownAlphaCards();

		final SequentialJoinCallback sjc = new SequentialJoinCallback(
				participant, knownAlphaCards, true);

		// handleSequentialJoinCallback(sjc);
		return sjc;
	}

	/**
	 * Handles incoming {@link SequentialJoinCallback} requests.
	 * 
	 * @param sjc
	 *            the incoming {@link SequentialJoinCallback}
	 * @return the corresponding {@link SequentialJoinSynchronisation} response
	 */
	public SequentialJoinSynchronisation handleSequentialJoinCallback(
			final SequentialJoinCallback sjc) {

		try {
			this.apf.addParticipant(sjc.getInquirer());
		} catch (final Exception e) {
			e.printStackTrace();
		}

		final List<ChangePayloadEvent> latestAlphaCards = new LinkedList<ChangePayloadEvent>();

		final List<AlphaCardDescriptor> acds = this.apf.getListOfACDs();

		if (sjc.getKnownAlphaCards() != null) {
			for (final AlphaCardDescriptor acd : acds) {
				if (acd.getId().getCardID().equals(CoordCardType.CRA.id())) {
					continue;
				}

				Occurrence occ = Occurrence.UNDEFINED;

				final LogicalTimestamp local = acd.getVersionMap();
				final LogicalTimestamp incoming = sjc.getKnownAlphaCards().get(
						acd.getId());

				if (incoming != null) {
					occ = incoming.compare(local);
				}

				if (!sjc.getKnownAlphaCards().containsKey(acd.getId())
						|| (occ == Occurrence.PRECEDING)) {
					// || occ == Occurrence.IDENTICAL) { // TODO remove last
					// case!!
					Payload payload = null;

					if (acd.getId().getCardID().equals(CoordCardType.APA.id())) {
						// payload = (Payload) binder.load(apf.getHomePath()+
						// apf.getAlphaDoc().getEpisodeID() +
						// "/$APA/pl/Payload.xml", classes);
						payload = this.apf.getAPA();
					}
					if (acd.getId().getCardID().equals(CoordCardType.PSA.id())) {
						payload = this.apf.getPSA();
					}

					if (!(acd.readAdornment(
							CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
							.getValue() == null)) {
						payload = ((AlphaPropsFacadeImpl) this.apf).getHwf()
								.loadPayload(acd, new Payload());
					}

					final ChangePayloadEvent cpe = new ChangePayloadEvent(
							acd.getId(), payload, acd);
					latestAlphaCards.add(cpe);

				}
			}
		}

		final List<Participant> participants = new LinkedList<Participant>(
				this.apf.getCRA().getListOfParticipants());
		final SequentialJoinSynchronisation sjs = new SequentialJoinSynchronisation(
				latestAlphaCards, participants, true);

		// apf.insertIntoDrools(sjs);

		return sjs;
		// handleSequentialJoinSynchronisation(sjs);
	}

	/**
	 * Gets the locally missing participants.
	 * 
	 * @param incomingList
	 *            the incoming list of participants
	 * @param actorNames
	 *            the locally known actor names
	 * @return the missing participants
	 */
	public List<Participant> getMissingParticipants(
			final List<Participant> incomingList, Set<String> actorNames) {

		actorNames = apf.getCRA().getActorNames();

		final List<Participant> joinedParallel = new LinkedList<Participant>();

		if (incomingList != null) {
			for (final Participant participant : incomingList) {
				if (!actorNames.contains(participant.getContributor()
						.getActor())) {

					try {
						joinedParallel.add(participant);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return joinedParallel;
	}

	/**
	 * Handles incoming {@link SequentialJoinSynchronisation} responses.
	 * 
	 * @param sjs
	 *            the incoming {@link SequentialJoinSynchronisation}
	 * @return the corresponding {@link ParallelJoinCallback}
	 */
	public ParallelJoinCallback handleSequentialJoinSynchronisation(
			final SequentialJoinSynchronisation sjs) {

		final Map<AlphaCardID, VersionMap> knownAlphaCards = new LinkedHashMap<AlphaCardID, VersionMap>();

		if (sjs.getLatestAlphaCards() != null) {

			for (final ChangePayloadEvent cpe : sjs.getLatestAlphaCards()) {
				JoinUtility.LOGGER.info("CPE found:"
						+ cpe.getAlphaCardDescriptor().getId().getCardID());
				JoinUtility.LOGGER.info("progate CPE:"
						+ cpe.isPropagateChange());
				if (cpe.getPayloadContainer().getObj() == null) {
					JoinUtility.LOGGER.info("CPE does not have payload");
					final AddAlphaCardEvent aace = new AddAlphaCardEvent(
							cpe.getAlphaCardDescriptor());
					aace.setPropagateChange(false);
					this.apf.insertIntoDrools(aace);
				} else {
					JoinUtility.LOGGER.info("CPE has payload");
					cpe.setPropagateChange(false);
					this.apf.insertIntoDrools(cpe);
				}

				AlphaCardDescriptor acd = null;
				try {
					acd = this.apf.getAlphaCard(cpe.getAlphaCardID());
				} catch (final Exception e) {
					knownAlphaCards.put(cpe.getAlphaCardID(), cpe
							.getAlphaCardDescriptor().getVersionMap());
				}
				if (acd != null) {
					final Occurrence occ = acd.getVersionMap().compare(
							cpe.getAlphaCardDescriptor().getVersionMap());
					if ((occ == Occurrence.PRECEDING)) {
						knownAlphaCards.put(cpe.getAlphaCardID(), cpe
								.getAlphaCardDescriptor().getVersionMap());
					}
				}

			}
			for (final AlphaCardDescriptor acd : this.apf.getListOfACDs()) {
				JoinUtility.LOGGER.info("ACD:" + acd.getId().toString());
			}

			for (final AlphaCardID acid : this.apf.getPSA()
					.getListOfTodoItems()) {
				JoinUtility.LOGGER.info("ACID:" + acid.toString());
			}
		}
		final List<Participant> joinedParallel = this.getMissingParticipants(
				sjs.getKnownActors(), this.apf.getCRA().getActorNames());

		for (final Participant participant : joinedParallel) {
			try {
				this.apf.addParticipant(participant);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		// Consider changes made to tokens during join protocol. Checks whether
		// participants have new tokens
		this.mergeTokens(sjs.getKnownActors());

		final Map<AlphaCardID, VersionMap> locallyKnownCards = this
				.getLocallyKnownAlphaCards();
		for (final AlphaCardID id : locallyKnownCards.keySet()) {
			if (!knownAlphaCards.keySet().contains(id)) {
				knownAlphaCards.put(id, locallyKnownCards.get(id));
			}
		}

		final ParallelJoinCallback pjc = new ParallelJoinCallback(
				this.getLocalParticipant(), knownAlphaCards, true);

		return pjc;
		// handleParallelJoinCallback(pjc);
	}

	/**
	 * Handles incoming {@link ParallelJoinCallback} requests.
	 * 
	 * @param pjc
	 *            the incoming {@link ParallelJoinCallback}
	 * @return the corresponding {@link ParallelJoinSynchronisation} response
	 */
	public ParallelJoinSynchronisation handleParallelJoinCallback(
			final ParallelJoinCallback pjc) {

		try {
			this.apf.addParticipant(pjc.getInquirer());
		} catch (final Exception e) {
			e.printStackTrace();
		}

		final List<ChangePayloadEvent> latestStatus = new LinkedList<ChangePayloadEvent>();
		final Map<AlphaCardID, VersionMap> requestedAlphaCards = new HashMap<AlphaCardID, VersionMap>();

		final List<AlphaCardDescriptor> acds = this.apf.getListOfACDs();

		if (pjc.getKnownAlphaCards() != null) {

			for (final AlphaCardID id : pjc.getKnownAlphaCards().keySet()) {
				if (!this.getLocallyKnownAlphaCards().containsKey(id)) {
					requestedAlphaCards.put(id, new VersionMap());
					continue;
				}
				if (this.getLocallyKnownAlphaCards().get(id)
						.compare(pjc.getKnownAlphaCards().get(id)) == Occurrence.PRECEDING) {
					requestedAlphaCards.put(id, new VersionMap());
				}
			}

			for (final AlphaCardDescriptor acd : acds) {
				if (acd.getId().getCardID().equals(CoordCardType.CRA.id())) {
					continue;
				}

				Occurrence occ = Occurrence.UNDEFINED;

				final LogicalTimestamp local = acd.getVersionMap();
				final LogicalTimestamp incoming = pjc.getKnownAlphaCards().get(
						acd.getId());

				if (incoming != null) {
					occ = incoming.compare(local);
				}

				if (!pjc.getKnownAlphaCards().containsKey(acd.getId())
						|| (occ == Occurrence.PRECEDING)) {
					Payload payload = null;

					if (acd.getId().getCardID().equals(CoordCardType.APA.id())) {
						payload = this.apf.getAPA();
					}
					if (acd.getId().getCardID().equals(CoordCardType.PSA.id())) {
						payload = this.apf.getPSA();
					}

					if (!(acd.readAdornment(
							CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
							.getValue() == null)) {
						payload = ((AlphaPropsFacadeImpl) this.apf).getHwf()
								.loadPayload(acd, new Payload());
					}

					final ChangePayloadEvent cpe = new ChangePayloadEvent(
							acd.getId(), payload, acd);
					latestStatus.add(cpe);

				}

				if ((occ == Occurrence.FOLLOWING)
						|| (occ == Occurrence.CONCURRENT)) {
					requestedAlphaCards.put(acd.getId(), acd.getVersionMap());
				}
			}

		}

		final ParallelJoinSynchronisation pjs = new ParallelJoinSynchronisation(
				this.getLocalParticipant(), latestStatus, requestedAlphaCards,
				true);

		return pjs;
	}

	/**
	 * Handles incoming {@link ParallelJoinSynchronisation} responses.
	 * 
	 * @param pjs
	 *            the incoming {@link ParallelJoinSynchronisation}
	 * @return the corresponding {@link SequentialJoinSynchronisation}
	 */
	public SequentialJoinSynchronisation handleParallelJoinSynchronisation(
			final ParallelJoinSynchronisation pjs) {

		if (pjs.getLatestAlphaCards() != null) {
			for (final ChangePayloadEvent cpe : pjs.getLatestAlphaCards()) {
				if (cpe.getPayloadContainer().getObj() == null) {
					this.apf.insertIntoDrools(new AddAlphaCardEvent(cpe
							.getAlphaCardDescriptor()));
				} else {
					this.apf.insertIntoDrools(cpe);
				}
			}
		}

		final List<ChangePayloadEvent> latestAlphaCards = new LinkedList<ChangePayloadEvent>();

		final List<AlphaCardDescriptor> acds = this.apf.getListOfACDs();

		if (pjs.getRequestedAlphaCards() != null) {
			for (final AlphaCardID id : pjs.getRequestedAlphaCards().keySet()) {

				AlphaCardDescriptor acd;
				try {
					acd = this.apf.getAlphaCard(id);
				} catch (final Exception e) {
					continue;
				}
				Payload payload = null;

				if (id.getCardID().equals(CoordCardType.APA.id())) {
					payload = this.apf.getAPA();
				}
				if (id.getCardID().equals(CoordCardType.PSA.id())) {
					payload = this.apf.getPSA();
				}

				if (!(acd.readAdornment(
						CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
						.getValue() == null)) {
					payload = ((AlphaPropsFacadeImpl) this.apf).getHwf()
							.loadPayload(acd, new Payload());
				}

				final ChangePayloadEvent cpe = new ChangePayloadEvent(
						acd.getId(), payload, acd);

				latestAlphaCards.add(cpe);
			}

		}
		final List<Participant> participants = new LinkedList<Participant>(
				this.apf.getCRA().getListOfParticipants());
		final SequentialJoinSynchronisation sjs = new SequentialJoinSynchronisation(
				latestAlphaCards, participants, true);

		return sjs;
	}

	/**
	 * Checks if the local actor is allowed to modify the supplied AlphaCard.
	 * 
	 * This method can be used to compose join messages in which only cards are
	 * exchanged that are locally modifiable.
	 * 
	 * @param acd
	 *            the AlphaCard to be checked
	 * @return true, if the local actor is allowed to modify the supplied
	 *         AlphaCard
	 */
	private boolean localActorIsOwner(final AlphaCardDescriptor acd) {

		final AlphaCardID cardId = acd.getId();
		if ((cardId.equals(CoordCardType.PSA.id())
				|| cardId.equals(CoordCardType.CRA.id()) || cardId
					.equals(CoordCardType.APA.id())))
			return true;
		else {
			final String owner = acd
					.readAdornment(CorpusGenericus.ACTOR.value()).getValue()
					.substring(1);

			final String localActor = this.apf.getAlphaConfig()
					.getLocalNodeID().getContributor().getActor();

			return owner.equals(localActor);
		}
	}

	/**
	 * Merges the tokens of the incoming Participants with the locally known
	 * participants regarding logical timestamps inherent in tokens. Granularity
	 * is of tokens (comparision of versionMaps on token level). Considers
	 * changes made to tokens during join process
	 * 
	 * @param incomingList
	 *            List of incoming Participants from Join-Protocol
	 */
	private void mergeTokens(final List<Participant> incomingList) {
		Occurrence occ;
		Participant localParticipant;

		for (Participant participant : incomingList) {
			localParticipant = this.apf.getParticipantByActor(participant
					.getContributor().getActor());
			for (Token token : participant.readTokens()) {
				occ = token.getVersionMap().compare(
						localParticipant.readToken(token.getName())
								.getVersionMap());
				if (occ == Occurrence.FOLLOWING) {
					JoinUtility.LOGGER.finer("MERGE TOKENS________________"
							+ token.getValue());
					try {
						this.apf.updateToken(localParticipant, token, false);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}

	}
}
