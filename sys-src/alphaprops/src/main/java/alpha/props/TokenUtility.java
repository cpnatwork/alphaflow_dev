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
 * $Id: TokenUtility.java 4000 2012-09-13 13:41:02Z uj32uvac $
 *************************************************************************/
package alpha.props;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

import alpha.facades.AlphaPropsFacade;
import alpha.model.cra.Participant;
import alpha.model.cra.Token;
import alpha.model.timestamp.Occurrence;
import alpha.overnet.event.TokenPropagation;
import alpha.util.XmlBinder;

/**
 * The Class TokenUtility.
 */
public class TokenUtility {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(TokenUtility.class.getName());

	/** The global {@link AlphaPropsFacade}. */
	private final AlphaPropsFacade apf;

	/**
	 * Instantiates a new token utility.
	 * 
	 * @param apf
	 *            the apf
	 */
	public TokenUtility(final AlphaPropsFacade apf) {
		this.apf = apf;
	}

	/**
	 * Generates the TokenPropagationEvent which is forwarded by the overnet component.
	 *
	 * @param receiver the receiver
	 * @param receiverToken the receiver token
	 * @return the token propagation
	 */
	public TokenPropagation generateTokenPropagation(
			final Participant receiver, final Token receiverToken) {

		final Participant sender = this.getLocalParticipant();
		final TokenPropagation tpm = new TokenPropagation(sender, receiver,
				receiverToken, true);

		return tpm;
	}

	/**
	 * Gets the local participant.
	 * 
	 * @return the local participant
	 */
	private Participant getLocalParticipant() {
		final Participant participant = this.apf.getParticipantByActor(this.apf
				.getAlphaConfig().getLocalNodeID().getContributor().getActor());
		return participant;
	}

	/**
	 * Handles incoming {@link TokenPropagation} responses.
	 * 
	 * @param tp
	 *            the incoming {@link TokenPropagation}
	 */
	public void handleTokenPropagation(final TokenPropagation tp) {
		try {
			final Participant recipient = tp.getReceiver();
			final Participant sender = tp.getSender();

			// sender of token gets negated receiverToken
			Token senderToken = TokenUtility.cloneToken(tp.getReceiverToken());
			senderToken.setValue(tp.getReceiverToken().getNegatedValue());
			senderToken.setVersionMap(tp.getReceiverToken().getVersionMap());

			LOGGER.finer("handleTokenPropagation_______sender="
					+ tp.getSender().getContributor().getActor() + " receiver="
					+ tp.getReceiver().getContributor().getActor()
					+ " tokenReceiver=" + tp.getReceiverToken()
					+ " versionMap=" + tp.getReceiverToken().getVersionMap());

			// check if version in TP is newer
			Token localDoyenToken = apf.getCurrentDoyen().readToken(
					tp.getReceiverToken().getName());

			final Occurrence occ = tp.getReceiverToken().getVersionMap()
					.compare(localDoyenToken.getVersionMap());

			LOGGER.finer("handleTokenPropagation_______localDoyen:"
					+ apf.getCurrentDoyen().getContributor().getActor());
			LOGGER.finer("handleTokenPropagation_______localDoyenToken:"
					+ localDoyenToken.getVersionMap());
			LOGGER.finer("handleTokenPropagation_______tp.getRecToken(:"
					+ tp.getReceiverToken().getVersionMap());
			LOGGER.finer("handleTokenPropagation_______occurence:" + occ);

			if (occ == Occurrence.FOLLOWING) {
				// update sender token
				this.apf.updateToken(sender, senderToken, false);
				// update receiver token
				this.apf.updateToken(recipient, tp.getReceiverToken(), false);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Clone token.
	 *
	 * @param seed the seed
	 * @return the token
	 */
	public static Token cloneToken(final Token seed) {
		return (Token) getClone(seed);
	}

	/**
	 * Gets a clone of the object.
	 * 
	 * @param seed
	 *            the to clone
	 * @return the clone of the object
	 */
	private static Object getClone(final Object seed) {
		Object clone = null;
		final String schema = "alpha.model.cra";
		final XmlBinder xmlb = new XmlBinder();

		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(bos);
			xmlb.store(seed, out, schema);

			final ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			clone = xmlb.load(in, schema);

			bos.close();
		} catch (final IOException e) {
			LOGGER.severe("Error: " + e);
		}
		LOGGER.finer("The clone is: " + clone);
		return clone;
	}

}
