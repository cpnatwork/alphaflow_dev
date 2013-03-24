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
package alpha.overnet.event;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.model.versionmap.VersionMap;

/**
 * Represents a join message for parallel join synchronisation.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "inquirer", "latestAlphaCards",
		"requestedAlphaCards", "propagateChange" })
@XmlRootElement()
public class ParallelJoinSynchronisation {

	/** The actor sending the join message. */
	private Participant inquirer;

	/** The latest information about all locally known AlphaCards. */
	private List<ChangePayloadEvent> latestAlphaCards;

	/** The latest information about all requested AlphaCards. */
	private Map<AlphaCardID, VersionMap> requestedAlphaCards;

	/** True, if message should be propagated. */
	private boolean propagateChange;

	/**
	 * Gets the inquirer.
	 * 
	 * @return the inquirer
	 */
	public Participant getInquirer() {
		return this.inquirer;
	}

	/**
	 * Sets the inquirer.
	 * 
	 * @param inquirer
	 *            the new inquirer
	 */
	public void setInquirer(final Participant inquirer) {
		this.inquirer = inquirer;
	}

	/**
	 * Checks if message should be propagated.
	 * 
	 * @return true, if is propagate change
	 */
	public boolean isPropagateChange() {
		return this.propagateChange;
	}

	/**
	 * Sets if if message should be propagated.
	 * 
	 * @param propagateChange
	 *            the new true, if message should be propagated
	 */
	public void setPropagateChange(final boolean propagateChange) {
		this.propagateChange = propagateChange;
	}

	/**
	 * Gets the latest alpha cards.
	 * 
	 * @return the latest alpha cards
	 */
	public List<ChangePayloadEvent> getLatestAlphaCards() {
		return this.latestAlphaCards;
	}

	/**
	 * Sets the latest alpha cards.
	 * 
	 * @param latestAlphaCards
	 *            the new latest alpha cards
	 */
	public void setLatestAlphaCards(
			final List<ChangePayloadEvent> latestAlphaCards) {
		this.latestAlphaCards = latestAlphaCards;
	}

	/**
	 * Gets the requested alpha cards.
	 * 
	 * @return the requested alpha cards
	 */
	public Map<AlphaCardID, VersionMap> getRequestedAlphaCards() {
		return this.requestedAlphaCards;
	}

	/**
	 * Sets the requested alpha cards.
	 * 
	 * @param requestedAlphaCards
	 *            the requested alpha cards
	 */
	public void setRequestedAlphaCards(
			final Map<AlphaCardID, VersionMap> requestedAlphaCards) {
		this.requestedAlphaCards = requestedAlphaCards;
	}

	/**
	 * Instantiates a new empty {@link ParallelJoinSynchronisation}.
	 */
	public ParallelJoinSynchronisation() {

	}

	/**
	 * Instantiates a new {@link ParallelJoinSynchronisation}.
	 * 
	 * @param inquirer
	 *            the sending actor
	 * @param latestAlphaCards
	 *            the latest information about all locally known AlphaCards
	 * @param requestedAlphaCards
	 *            the latest information about all requested AlphaCards
	 * @param propagateChange
	 *            true, if message should be propagated.
	 */
	public ParallelJoinSynchronisation(final Participant inquirer,
			final List<ChangePayloadEvent> latestAlphaCards,
			final Map<AlphaCardID, VersionMap> requestedAlphaCards,
			final boolean propagateChange) {
		this.inquirer = inquirer;
		this.latestAlphaCards = latestAlphaCards;
		this.requestedAlphaCards = requestedAlphaCards;
		this.propagateChange = propagateChange;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder ret = new StringBuilder();
		if (this.inquirer != null) {
			ret.append(this.inquirer.toString() + "\n");
		}
		if (this.latestAlphaCards != null) {
			ret.append(this.latestAlphaCards.toString() + "\n");
		}
		if (this.requestedAlphaCards != null) {
			ret.append(this.requestedAlphaCards.toString() + "\n");
		}
		ret.append(this.propagateChange);

		return ret.toString();
	}
}
