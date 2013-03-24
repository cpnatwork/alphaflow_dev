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
 * $Id: TokenPropagation.java 3785 2012-05-03 07:49:23Z uj32uvac $
 *************************************************************************/
package alpha.overnet.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.cra.Participant;
import alpha.model.cra.Token;
import alpha.model.versionmap.VersionMap;

/**
 * Represents a token passing message for forwarding a token.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sender", "receiver", "receiverToken",
		"propagateChange"})
@XmlRootElement(name = "TokenPropagation")
public class TokenPropagation {

	/** The actor sending the token message. */
	@XmlElement(required = true)
	private Participant sender;

	/** The actor receiving the token message. */
	@XmlElement(required = true)
	private Participant receiver;

	/** The token to be passed. */
	@XmlElement(required = true)
	private Token receiverToken;

	/** True, if message should be propagated. */
	@XmlElement(required = true)
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
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	public Participant getSender() {
		return this.sender;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param sender
	 *            the new sender
	 */
	public void setSender(final Participant sender) {
		this.sender = sender;
	}

	/**
	 * Gets the receiver.
	 * 
	 * @return the receiver
	 */
	public Participant getReceiver() {
		return this.receiver;
	}

	/**
	 * Sets the receiver.
	 * 
	 * @param receiver
	 *            the new receiver
	 */
	public void setReceiver(final Participant receiver) {
		this.receiver = receiver;
	}

	/**
	 * Gets the token to be passed.
	 * 
	 * @return the token to be passed
	 */
	public Token getReceiverToken() {
		return this.receiverToken;
	}

	/**
	 * Sets the token to be passed.
	 * 
	 * @param token
	 *            the new token to be passed
	 */
	public void setReceiverToken(final Token token) {
		this.receiverToken = token;
	}

	/**
	 * Instantiates a new {@link TokenPropagation}.
	 * 
	 * @param sender
	 *            the actor sending the token
	 * @param recipient
	 *            the actor receiving the token
	 * @param receiverToken
	 *            the token
	 * @param propagateChange
	 *            true, if message should be propagated {@link VersionMap}s
	 *            known by the sending actor.
	 */
	public TokenPropagation(final Participant sender,
			final Participant recipient, final Token receiverToken,
			final boolean propagateChange) {
		super();
		this.sender = sender;
		this.receiver = recipient;
		this.receiverToken = receiverToken;
		this.propagateChange = propagateChange;
	}

	/**
	 * Instantiates a new empty {@link TokenPropagation}.
	 */
	public TokenPropagation() {
	}

	@Override
	public String toString() {
		return "TokenPropagation [sender=" + sender.getContributor().getActor()
				+ ", recipient=" + receiver.getContributor().getActor()
				+ ", receiverToken=" + receiverToken.getValue()
				+ ", propagateChange=" + propagateChange + "]";
	}

}