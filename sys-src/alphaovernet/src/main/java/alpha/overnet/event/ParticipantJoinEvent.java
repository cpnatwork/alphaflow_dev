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

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.AlphaCardDescriptor;

/**
 * A POJO for the technical event for adding an {@link AlphaCardDescriptor}.
 * 
 */
@ObservableEvent
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "knownAlphaCards", "isAnswer",
		"propagateChange" })
@XmlRootElement(name = "participantJoinEvent")
public class ParticipantJoinEvent {

	/** The known alpha cards. */
	private List<ChangePayloadEvent> knownAlphaCards;

	/** The is answer. */
	private boolean isAnswer;

	/**
	 * Checks if is answer.
	 * 
	 * @return true, if is answer
	 */
	public boolean isAnswer() {
		return this.isAnswer;
	}

	/**
	 * Sets the answer.
	 * 
	 * @param isAnswer
	 *            the new answer
	 */
	public void setAnswer(final boolean isAnswer) {
		this.isAnswer = isAnswer;
	}

	/**
	 * Gets the known alpha cards.
	 * 
	 * @return the known alpha cards
	 */
	public List<ChangePayloadEvent> getKnownAlphaCards() {
		return this.knownAlphaCards;
	}

	/**
	 * Sets the known alpha cards.
	 * 
	 * @param knownAlphaCards
	 *            the new known alpha cards
	 */
	public void setKnownAlphaCards(
			final List<ChangePayloadEvent> knownAlphaCards) {
		this.knownAlphaCards = knownAlphaCards;
	}

	/** The propagate change. */
	@XmlElement(required = true)
	private boolean propagateChange;

	/**
	 * Gets the propagate change.
	 * 
	 * @return the propagate change
	 */
	public boolean getPropagateChange() {
		return this.propagateChange;
	}

	/**
	 * Sets the propagate change.
	 * 
	 * @param propagateChange
	 *            the new propagate change
	 */
	public void setPropagateChange(final boolean propagateChange) {
		this.propagateChange = propagateChange;
	}

	/**
	 * Instantiates a new adds the alpha card event.
	 * 
	 */

	public ParticipantJoinEvent() {
		this.knownAlphaCards = new LinkedList<ChangePayloadEvent>();
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#hashCode()
	// */
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime
	// * result
	// + ((alphaCardDescriptor == null) ? 0 : alphaCardDescriptor
	// .hashCode());
	// return result;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#equals(java.lang.Object)
	// */
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (obj == null)
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// AddAlphaCardEvent other = (AddAlphaCardEvent) obj;
	// if (alphaCardDescriptor == null) {
	// if (other.alphaCardDescriptor != null)
	// return false;
	// } else if (!alphaCardDescriptor.equals(other.alphaCardDescriptor))
	// return false;
	// return true;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#toString()
	// */
	// @Override
	// public String toString() {
	// return "AddAlphaCardEvent [alphaCardDescriptor=" + alphaCardDescriptor
	// + "]";
	// }

}
