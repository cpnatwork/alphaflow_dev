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

import java.util.List;
import java.util.Set;

import alpha.model.cra.EndpointID;

/**
 * Wrapper class containing information about single messages to be sent to the
 * overlay network.
 */
public class MessageInformation {

	/** The update to be sent. */
	private final List<Object> updates;

	/** The recipients of the message. */
	private final Set<EndpointID> recipients;

	/** The subject of the message. */
	private final String subject;

	/** The content of the message. */
	private final String content;

	/** Indicates if the message should be encrypted. */
	private final boolean encrypt;

	/** Indicates if the message should be signed. */
	private final boolean sign;

	/**
	 * Instantiates a new {@link MessageInformation}.
	 * 
	 * @param updates
	 *            the updates to be sent
	 * @param recipients
	 *            the recipients of the message
	 * @param subject
	 *            the subject of the message
	 * @param content
	 *            the content of the message
	 * @param encrypt
	 *            indicates if the message should be encrypted
	 * @param sign
	 *            indicates if the message should be signed
	 */
	public MessageInformation(final List<Object> updates,
			final Set<EndpointID> recipients, final String subject,
			final String content, final boolean encrypt, final boolean sign) {
		this.updates = updates;
		this.recipients = recipients;
		this.subject = subject;
		this.content = content;
		this.encrypt = encrypt;
		this.sign = sign;
	}

	/**
	 * Gets the updates to be sent.
	 * 
	 * @return the updates to be sent
	 */
	public List<Object> getUpdates() {
		return this.updates;
	}

	/**
	 * Gets the recipients of the message.
	 * 
	 * @return the recipients of the message
	 */
	public Set<EndpointID> getRecipients() {
		return this.recipients;
	}

	/**
	 * Gets the subject of the message.
	 * 
	 * @return the subject of the message
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * Gets the content of the message.
	 * 
	 * @return the content of the message
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Indicates if the message should be encrypted.
	 * 
	 * @return true, if message should be encrypted
	 */
	public boolean isEncrypt() {
		return this.encrypt;
	}

	/**
	 * Indicates if the message should be signed.
	 * 
	 * @return true, if message should be signed
	 */
	public boolean isSign() {
		return this.sign;
	}

}
