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

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.model.versionmap.VersionMap;

/**
 * Represents a join message for sequential join callback.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "inquirer", "knownAlphaCards",
		"propagateChange" })
@XmlRootElement()
public class SequentialJoinCallback {

	/** The actor sending the join message. */
	private Participant inquirer;

	/**
	 * The {@link AlphaCardID}s and the corresponding {@link VersionMap}s known
	 * by the sending actor.
	 */
	private Map<AlphaCardID, VersionMap> knownAlphaCards;

	/** True, if message should be propagated. */
	private boolean propagateChange;

	/**
	 * Checks if message should be propagated.
	 * 
	 * @return true, if message should be propagated.
	 */
	public boolean isPropagateChange() {
		return this.propagateChange;
	}

	/**
	 * Sets if message should be propagated.
	 * 
	 * @param propagateChange
	 *            the new true, if message should be propagated
	 */
	public void setPropagateChange(final boolean propagateChange) {
		this.propagateChange = propagateChange;
	}

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
	 * Gets the known alpha cards.
	 * 
	 * @return the known alpha cards
	 */
	public Map<AlphaCardID, VersionMap> getKnownAlphaCards() {
		return this.knownAlphaCards;
	}

	/**
	 * Sets the known alpha cards.
	 * 
	 * @param knownAlphaCards
	 *            the known alpha cards
	 */
	public void setKnownAlphaCards(
			final Map<AlphaCardID, VersionMap> knownAlphaCards) {
		this.knownAlphaCards = knownAlphaCards;
	}

	/**
	 * Instantiates a new {@link SequentialJoinCallback}.
	 * 
	 * @param inquirer
	 *            the actor sending the join message
	 * @param knownAlphaCards
	 *            the {@link AlphaCardID}s and the corresponding
	 * @param propagateChange
	 *            true, if message should be propagated {@link VersionMap}s
	 *            known by the sending actor.
	 */
	public SequentialJoinCallback(final Participant inquirer,
			final Map<AlphaCardID, VersionMap> knownAlphaCards,
			final boolean propagateChange) {
		this.inquirer = inquirer;
		this.knownAlphaCards = knownAlphaCards;
		this.propagateChange = propagateChange;
	}

	/**
	 * Instantiates a new empty {@link SequentialJoinCallback}.
	 */
	public SequentialJoinCallback() {

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
		if (this.knownAlphaCards != null) {
			ret.append(this.knownAlphaCards.toString() + "\n");
		}
		ret.append(this.propagateChange);

		return ret.toString();
	}
}
