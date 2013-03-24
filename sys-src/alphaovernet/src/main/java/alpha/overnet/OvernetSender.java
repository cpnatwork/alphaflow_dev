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
package alpha.overnet;

import java.util.List;
import java.util.Set;

import alpha.model.cra.EndpointID;

/**
 * Describes a generic interface for sending information objects to the overlay
 * network.
 */
public interface OvernetSender {

	/**
	 * Sends an update message. (Convenience method)
	 * 
	 * @param object
	 *            the information object to be sent
	 * @param recipients
	 *            the recipients of the message
	 * @return true, if message was sent successfully
	 */
	public boolean sendUpdate(Object object, Set<EndpointID> recipients);

	/**
	 * Sends an update message.
	 * 
	 * @param objects
	 *            the information objects to be sent
	 * @param recipients
	 *            the recipients of the message
	 * @param subject
	 *            the subject of the message
	 * @param messageContent
	 *            the content of the message
	 * @param encrypt
	 *            true, if message has to be encrypted
	 * @param sign
	 *            true, if message has to be signed
	 * @return true, if message was sent successfully
	 */
	public boolean sendUpdate(List<Object> objects, Set<EndpointID> recipients,
			String subject, String messageContent, boolean encrypt, boolean sign);
}
