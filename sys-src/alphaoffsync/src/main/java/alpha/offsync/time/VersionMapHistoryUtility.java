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
 * $Id: VersionMapHistoryUtility.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.offsync.time;

import java.util.logging.Logger;


import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.timestamp.Occurrence;
import alpha.model.versionmap.VersionMap;
import alpha.vvs.Historian;
import alpha.vvs.HistorianImpl;
import alpha.vvs.Version;
import alpha.vvs.Workspace;

/**
 * Implementation of {@link LogicalTimestampHistoryUtility} using.
 * 
 * {@link VersionMap} instances as artifact-specific logical timestamps.
 */
public class VersionMapHistoryUtility implements LogicalTimestampHistoryUtility {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(VersionMapHistoryUtility.class.getName());

	/** The VVS {@link Workspace}. */
	private Workspace hwf;

	/** The VVS {@link Historian}. */
	private Historian hist;

	/** True, if warnings should be display graphically. */
	private final boolean graphicalWarnings;

	/** The userID. */
	private final String userId = "Local User";

	/**
	 * Gets the {@link Workspace}.
	 * 
	 * @return the {@link Workspace}
	 */
	public Workspace getHwf() {
		return this.hwf;
	}

	/**
	 * Sets the {@link Workspace}.
	 * 
	 * @param hwf
	 *            the new {@link Workspace}
	 */
	public void setHwf(final Workspace hwf) {
		this.hwf = hwf;
	}

	/**
	 * Gets the {@link Historian}.
	 * 
	 * @return the {@link Historian}
	 */
	public Historian getHist() {
		return this.hist;
	}

	/**
	 * Sets the {@link Historian}.
	 * 
	 * @param hist
	 *            the new {@link Historian}
	 */
	public void setHist(final Historian hist) {
		this.hist = hist;
	}

	/**
	 * Instantiates a new {@link VersionMapHistoryUtility}.
	 * 
	 * @param hwf
	 *            the Workspace
	 * @param graphicalWarnings
	 *            true, if warnings should be displayed graphically
	 */
	public VersionMapHistoryUtility(final Workspace hwf,
			final boolean graphicalWarnings) {
		this.hwf = hwf;
		this.hist = new HistorianImpl(hwf.getHome());
		this.graphicalWarnings = graphicalWarnings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.offsync.time.LogicalTimestampHistoryUtility#insertIntoHistory(alpha
	 * .model.AlphaCardDescriptor, alpha.model.Payload)
	 */
	@Override
	public AlphaCardDescriptor insertIntoHistory(
			final AlphaCardDescriptor incomingAcd, final Payload payload)
			throws Exception {
		if (!this.hist.isTrackingAlphaCard(incomingAcd)) {
			this.trackAlphaCard(incomingAcd, payload); // done!
		} else {
			this.updateTrackedAlphaCard(incomingAcd, payload);
		}

		this.hist.revertToVersion(incomingAcd,
				this.hist.getHeadVersion(incomingAcd));
		final AlphaCardDescriptor currentHead = this.hwf
				.loadDescriptor(incomingAcd);

		return currentHead;
	}

	/**
	 * Updates an already tracked alpha card.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @param payload
	 *            the incoming {@link Payload}
	 * @throws VerVarStoreException
	 *             thrown if an error is encountered
	 */
	private void updateTrackedAlphaCard(final AlphaCardDescriptor incomingAcd,
			final Payload payload) throws VerVarStoreException {

		final AlphaCardDescriptor currentHead = this.hwf
				.loadDescriptor(incomingAcd);
		final VersionMap currentHeadVV = currentHead.getVersionMap();

		final Occurrence occ = incomingAcd.getVersionMap().compare(
				currentHeadVV);

		VersionMapHistoryUtility.LOGGER.info("Incoming Version"
				+ incomingAcd.getVersionMap().toString());
		VersionMapHistoryUtility.LOGGER.info("Current head Version"
				+ currentHead.getVersionMap().toString());
		VersionMapHistoryUtility.LOGGER.info("occurrence is: " + occ);
		switch (occ) {

		case IDENTICAL:
			this.recordIdentical(incomingAcd, payload); // done!
			break;

		case FOLLOWING:
			this.recordFollowing(incomingAcd, payload); // done!
			break;

		case PRECEDING:
			this.recordPreceding(incomingAcd, payload);
			break;

		case CONCURRENT:
			this.recordConcurrent(incomingAcd, payload); // done!
			if (this.graphicalWarnings) {
				ConcurrentModificationWarning
						.showConcurrentModificationWarning(incomingAcd);
			}
			break;

		case UNDEFINED:
			// do nothing
			break;

		}
	}

	/**
	 * Records an AlphaCard if {@link Occurrence} to current local head version
	 * is concurrent.
	 * 
	 * @param incomingAcd
	 *            the incoming acd
	 * @param payload
	 *            the payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	private void recordConcurrent(final AlphaCardDescriptor incomingAcd,
			final Payload payload) throws VerVarStoreException {
		System.out.println("Record concurrent");
		Version start = this.hist.getHeadVersion(incomingAcd);

		do {
			if (!start.isTemporaryVersion()) {
				final Occurrence occ = (start.retrieveDescriptor()
						.getVersionMap().compare(incomingAcd.getVersionMap()));
				if (occ == Occurrence.PRECEDING) {
					break;
				}
			}
			start = start.findPreviousValidVersion();

		} while (!start.isInitialVersion());

		// Create merge version vector
		final VersionMap merged = (VersionMap) (incomingAcd.getVersionMap())
				.merge(this.hist.getHeadVersion(incomingAcd)
						.retrieveDescriptor().getVersionMap());
		if (!this.hist.getHeadVersion(incomingAcd).isReconciledVersion()) {
			this.hist.revertToVersion(incomingAcd, start);
			this.hist.commitValidPath(incomingAcd,
					this.getCommitterId(incomingAcd),
					this.getMessage(incomingAcd, "ValidPath"), start);
		}

		// Update version vector to reflect reconciliation
		// AlphaCardDescriptor acd =
		// hist.retrieveDesciptorByVersionId(incomingAcd,
		// hist.getHeadVersion(incomingAcd).getId());
		this.hist.revertToVersion(incomingAcd,
				this.hist.getHeadVersion(incomingAcd));
		final AlphaCardDescriptor acd = this.hwf.loadDescriptor(incomingAcd);
		acd.setVersionMap(merged);
		this.hwf.storeDescriptor(acd);
		this.hist.updateCommittedVersion(acd, this.getCommitterId(incomingAcd),
				this.getMessage(acd, "Reconciled"),
				this.hist.getHeadVersion(incomingAcd));

		this.insertBeforeOnSystemPath(incomingAcd, payload,
				this.hist.getHeadVersion(incomingAcd));
	}

	/**
	 * Records an AlphaCard if {@link Occurrence} to current local head version
	 * is preceding.
	 * 
	 * @param incomingAcd
	 *            the incoming acd
	 * @param payload
	 *            the payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	private void recordPreceding(final AlphaCardDescriptor incomingAcd,
			final Payload payload) throws VerVarStoreException {
		System.out.println("Record preceding");
		final Version start = this.hist.getHeadVersion(incomingAcd);
		final Version end = this.hist.getHeadVersion(incomingAcd);

		this.findInsertionRange(incomingAcd, start, end);

		if (end.isReconciledVersion()) {
			final VersionMap merged = (VersionMap) end.retrieveDescriptor()
					.getVersionMap().merge(incomingAcd.getVersionMap());
			end.retrieveDescriptor().setVersionMap(merged);
			this.hwf.storeDescriptor(end.retrieveDescriptor());
			this.hist.updateCommittedVersion(end.retrieveDescriptor(), this
					.getCommitterId(incomingAcd), this.getMessage(
					end.retrieveDescriptor(), "Preceding-Update"), end);

			this.insertBeforeOnSystemPath(incomingAcd, payload, end);
		} else {
			final long distance = incomingAcd.getVersionMap().getDistance(
					end.retrieveDescriptor().getVersionMap());

			Version v = end;
			for (long i = 0; i <= distance; i++) {
				v = v.findPreviousValidVersion();
			}
			if (v.isTemporaryVersion()) {
				this.storeAlphaCard(incomingAcd, payload);
				this.hist.updateCommittedVersion(incomingAcd, this
						.getCommitterId(incomingAcd), this.getMessage(
						incomingAcd, "Preceding-UpdateTemporary"), v);
				this.hist.revertToVersion(incomingAcd,
						this.hist.getHeadVersion(incomingAcd));
			} else {
				final VersionMap merged = (VersionMap) (incomingAcd
						.getVersionMap()).merge(this.hist
						.getHeadVersion(incomingAcd).retrieveDescriptor()
						.getVersionMap());
				this.hist.revertToVersion(incomingAcd, start);
				this.hist.commitValidPath(incomingAcd,
						this.getCommitterId(incomingAcd),
						this.getMessage(incomingAcd, "Preceding-Validpath"),
						start);

				this.hist.revertToVersion(incomingAcd,
						this.hist.getHeadVersion(incomingAcd));
				final AlphaCardDescriptor acd = this.hwf
						.loadDescriptor(incomingAcd);
				acd.setVersionMap(merged);
				this.hwf.storeDescriptor(acd);
				this.hist.updateCommittedVersion(acd, this.getCommitterId(acd),
						this.getMessage(acd, "Preceding-Update"),
						this.hist.getHeadVersion(incomingAcd));

				this.insertBeforeOnSystemPath(incomingAcd, payload,
						this.hist.getHeadVersion(incomingAcd));

			}
		}

	}

	/**
	 * Inserts an AlphaCard before a certain version on the system path.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @param payload
	 *            the incoming {@link Payload}
	 * @param nextVersion
	 *            the next version
	 * @throws VerVarStoreException
	 *             thrown if an error is encountered
	 */
	private void insertBeforeOnSystemPath(
			final AlphaCardDescriptor incomingAcd, final Payload payload,
			final Version nextVersion) throws VerVarStoreException {
		final AlphaCardDescriptor nextAcd = nextVersion.retrieveDescriptor();
		final Payload p = this.hwf.loadPayload(nextAcd, new Payload());
		// hwf.storeDescriptor(incomingAcd);
		this.storeAlphaCard(incomingAcd, payload);
		this.hist.commitBeforeOnSystemPath(incomingAcd,
				this.getCommitterId(incomingAcd),
				this.getMessage(incomingAcd, "InsertBeforeOnSystemPath"),
				nextVersion);
		this.hwf.storeDescriptor(nextAcd);
		this.hwf.storePayload(nextAcd, p);
		this.hist.updateCommittedVersion(nextAcd, this.getCommitterId(nextAcd),
				nextVersion.getMessage(), this.hist.getHeadVersion(nextAcd));
		this.hist.revertToVersion(incomingAcd, nextVersion);
	}

	/**
	 * Finds the correct insertion range for an AlphaCard.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @param start
	 *            the start of the insertion range
	 * @param end
	 *            the end of the insertion range
	 */
	private void findInsertionRange(final AlphaCardDescriptor incomingAcd,
			Version start, Version end) {

		do {
			if (!start.isTemporaryVersion()) {
				end = this.hist.findVersionByVersionId(incomingAcd,
						start.getId());
				final Occurrence occ = (start.retrieveDescriptor()
						.getVersionMap().compare(incomingAcd.getVersionMap()));
				if (occ == Occurrence.PRECEDING) {
					break;
				}
			}

			start = start.findPreviousValidVersion();
		} while (!start.isInitialVersion());
	}

	/**
	 * Records an AlphaCard if {@link Occurrence} to current local head version
	 * is following.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @param payload
	 *            the incoming {@link Payload}
	 * @throws VerVarStoreException
	 *             thrown if an error is encountered
	 */
	private void recordFollowing(final AlphaCardDescriptor incomingAcd,
			final Payload payload) throws VerVarStoreException {
		System.out.println("Record following");
		VersionMapHistoryUtility.LOGGER.info("Begin Record following");
		this.determineInsertionPosition(incomingAcd);
		this.storeAlphaCard(incomingAcd, payload);
		this.hist.commit(incomingAcd, this.userId, "Store following "
				+ incomingAcd.getVersionMap().versionCounters);
		VersionMapHistoryUtility.LOGGER.info("End Record following");
	}

	/**
	 * Records an AlphaCard if {@link Occurrence} to current local head version
	 * is identical.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @param payload
	 *            the incoming {@link Payload}
	 */
	private void recordIdentical(final AlphaCardDescriptor incomingAcd,
			final Payload payload) {
		System.out.println("Record identical");
		this.hwf.storeDescriptor(incomingAcd);
		if (payload != null) {
			this.hwf.storePayload(incomingAcd, payload);
		}
		try {
			this.hist.updateCommittedVersion(incomingAcd,
					this.getCommitterId(incomingAcd),
					this.getMessage(incomingAcd, "Update"),
					this.hist.getHeadVersion(incomingAcd));
		} catch (final VerVarStoreException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Stores an AlphaCard to disk.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @param payload
	 *            the incoming {@link Payload}
	 */
	private void storeAlphaCard(final AlphaCardDescriptor incomingAcd,
			final Payload payload) {
		VersionMapHistoryUtility.LOGGER.info("Begin Store alphacard");
		this.hwf.storeDescriptor(incomingAcd);
		this.hist.addAlphaCardItem(incomingAcd,
				this.hwf.getDescriptorFile(incomingAcd));
		if (payload != null) {
			this.hwf.storePayload(incomingAcd, payload);
			this.hist.addAlphaCardItem(incomingAcd,
					this.hwf.getPayloadFile(incomingAcd));
		}

		VersionMapHistoryUtility.LOGGER.info("End Store alphacard");
	}

	/**
	 * Determines the initial insertion position of an untracked AlphaCard.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @throws VerVarStoreException
	 *             thrown if an error is encountered
	 */
	private void determineInitialInsertionPosition(
			final AlphaCardDescriptor incomingAcd) throws VerVarStoreException {
		VersionMapHistoryUtility.LOGGER
				.info("Begin determine insertion position");

		final long distance = new VersionMap().getDistance(incomingAcd
				.getVersionMap());
		VersionMapHistoryUtility.LOGGER.info("Distance is: " + distance);
		for (long i = 1; i < distance; i++) {
			this.hist.commitTemporary(incomingAcd,
					this.getCommitterId(incomingAcd),
					this.getMessage(incomingAcd, "Temporary"));
		}
		VersionMapHistoryUtility.LOGGER
				.info("End determine insertion position");
	}

	/**
	 * Determines the insertion position of an AlphaCard.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @throws VerVarStoreException
	 *             thrown if an error is encountered
	 */
	private void determineInsertionPosition(
			final AlphaCardDescriptor incomingAcd) throws VerVarStoreException {
		VersionMapHistoryUtility.LOGGER
				.info("Begin determine insertion position");

		final AlphaCardDescriptor currentHead = this.hwf
				.loadDescriptor(incomingAcd);
		final VersionMap currentHeadVV = currentHead.getVersionMap();

		final long distance = currentHeadVV.getDistance(incomingAcd
				.getVersionMap());
		VersionMapHistoryUtility.LOGGER.info("Distance is: " + distance);
		for (long i = 1; i <= distance; i++) {
			this.hist.commitTemporary(incomingAcd,
					this.getCommitterId(incomingAcd),
					this.getMessage(incomingAcd, "Temporary"));
		}
		VersionMapHistoryUtility.LOGGER
				.info("End determine insertion position");
	}

	/**
	 * Tracks an AlphaCard.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @param payload
	 *            the incoming {@link Payload}
	 * @throws VerVarStoreException
	 *             thrown if an error is encountered
	 */
	public void trackAlphaCard(final AlphaCardDescriptor incomingAcd,
			final Payload payload) throws VerVarStoreException {
		if (!this.hist.isTrackingAlphaCard(incomingAcd)) {
			this.hist.trackAlphaCard(incomingAcd);
		}
		this.determineInitialInsertionPosition(incomingAcd);
		this.storeAlphaCard(incomingAcd, payload);
		this.hist.commit(incomingAcd, this.userId, "Initial Insertion "
				+ incomingAcd.getVersionMap().versionCounters);
	}

	/**
	 * Gets the committerID.
	 * 
	 * @param acd
	 *            the acd
	 * @return the committer id
	 */
	private String getCommitterId(final AlphaCardDescriptor acd) {
		return "Local User";
	}

	/**
	 * Gets the commit message.
	 * 
	 * @param acd
	 *            the {@link AlphaCardDescriptor} of the AlphaCard to be
	 *            committed
	 * @param message
	 *            the custom commit message
	 * @return the commit message
	 */
	private String getMessage(final AlphaCardDescriptor acd,
			final String message) {
		return message + " " + acd.getVersionMap().versionCounters;
	}
}
