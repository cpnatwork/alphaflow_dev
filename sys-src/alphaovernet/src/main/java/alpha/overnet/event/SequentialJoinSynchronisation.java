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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.cra.Participant;

/**
 * Represents a join message for sequential join synchronisation.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "latestAlphaCards", "knownActors",
		"propagateChange" })
@XmlRootElement()
public class SequentialJoinSynchronisation {

	/** The latest information about all locally known AlphaCards. */
	private List<ChangePayloadEvent> latestAlphaCards;

	/** The locally known actors. */
	private List<Participant> knownActors;

	/**
	 * Gets the locally known actors.
	 * 
	 * @return the locally known actors
	 */
	public List<Participant> getKnownActors() {
		return this.knownActors;
	}

	/**
	 * Sets the locally known actors.
	 * 
	 * @param knownActors
	 *            the locally known actors
	 */
	public void setKnownActors(final List<Participant> knownActors) {
		this.knownActors = knownActors;
	}

	/** True, if message should be propagated. */
	private boolean propagateChange;

	/**
	 * Checks if if message should be propagated.
	 * 
	 * @return true, if is propagate change
	 */
	public boolean isPropagateChange() {
		return this.propagateChange;
	}

	/**
	 * Sets if message should be propagated.
	 * 
	 * @param propagateChange
	 *            true, if message should be propagated
	 */
	public void setPropagateChange(final boolean propagateChange) {
		this.propagateChange = propagateChange;
	}

	/**
	 * Instantiates a new empty {@link SequentialJoinSynchronisation}.
	 */
	public SequentialJoinSynchronisation() {

	}

	/**
	 * Instantiates a new {@link SequentialJoinSynchronisation}.
	 * 
	 * @param latestAlphaCards
	 *            the latest information about all locally known AlphaCards
	 * @param knownActors
	 *            the locally known actors
	 * @param propagateChange
	 *            true, if message should be propagated.
	 */
	public SequentialJoinSynchronisation(
			final List<ChangePayloadEvent> latestAlphaCards,
			final List<Participant> knownActors, final boolean propagateChange) {
		this.latestAlphaCards = latestAlphaCards;
		this.knownActors = knownActors;
		this.propagateChange = propagateChange;
	}

	/**
	 * Gets the latest information about all locally known AlphaCards.
	 * 
	 * @return the latest information about all locally known AlphaCards
	 */
	public List<ChangePayloadEvent> getLatestAlphaCards() {
		return this.latestAlphaCards;
	}

	/**
	 * Sets the latest information about all locally known AlphaCards.
	 * 
	 * @param latestAlphaCards
	 *            the latest information about all locally known AlphaCards.
	 */
	public void setLatestAlphaCards(
			final List<ChangePayloadEvent> latestAlphaCards) {
		this.latestAlphaCards = latestAlphaCards;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder ret = new StringBuilder();
		if (this.latestAlphaCards != null) {
			ret.append(this.latestAlphaCards.toString() + "\n");
		}
		if (this.knownActors != null) {
			ret.append(this.knownActors.toString() + "\n");
		}
		ret.append(this.propagateChange);

		return ret.toString();
	}
}
