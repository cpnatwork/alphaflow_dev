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
 * $Id: MessagingHelper.java 3786 2012-05-04 08:00:25Z uj32uvac $
 *************************************************************************/
package alpha.offsync;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.internet.MimeMessage;

import java.util.logging.Logger;


import alpha.model.docconfig.AlphadocConfig;
import alpha.offsync.security.MailAuthenticator;
import alpha.util.HardwareAddress;

import com.sun.mail.imap.IMAPFolder;

/**
 * Encapsulates common functionalities used for email-based communication.
 */
public class MessagingHelper {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(MessagingHelper.class.getName());

	/** The config. */
	AlphadocConfig config;

	/**
	 * Instantiates a new messaging helper.
	 * 
	 * @param config
	 *            the global {@link AlphadocConfig}
	 */
	public MessagingHelper(final AlphadocConfig config) {
		this.config = config;
	}

	/**
	 * Enters IDLE monitoring mode on the specified folder.
	 * 
	 * @param folder
	 *            the folder to be monitored
	 * @param adapter
	 *            the adapter
	 */
	public void enterIdleMode(final Folder folder,
			final MessageCountAdapter adapter) {

		folder.addMessageCountListener(adapter);
		boolean folderHasIdleSupport = false;
		try {
			if (folder instanceof IMAPFolder) {

				MessagingHelper.LOGGER
						.info(folder.getFullName()
								+ " supports IMAP IDLE Command. Using push notifications.");
				final IMAPFolder f = (IMAPFolder) folder;
				f.idle();
				folderHasIdleSupport = true;
			}
		} catch (final FolderClosedException e) {
			MessagingHelper.LOGGER.info(folder.getFullName() + " is closed.");
		} catch (final MessagingException mex) {
			folderHasIdleSupport = false;
		}

		// Infinite IDLE Loop
		while (true) {

			if (!folder.isOpen()) {
				MessagingHelper.LOGGER.info(folder.getFullName()
						+ " is closed. Receiver is terminating.");
				return;
			}

			if (folderHasIdleSupport && (folder instanceof IMAPFolder)) {
				final IMAPFolder f = (IMAPFolder) folder;

				try {
					MessagingHelper.LOGGER.info("Sending IMAP IDLE command");
					f.idle();
				} catch (final MessagingException e) {
					MessagingHelper.LOGGER
							.info("IMAP IDLE Command not successful");
					e.printStackTrace();
				} catch (final IllegalStateException e) {
					MessagingHelper.LOGGER.info("Access to"
							+ folder.getFullName() + " denied.");
				}

			} else {
				MessagingHelper.LOGGER
						.info("IMAP IDLE Command is not supported. Falling back to polling mode.");
				try {
					Thread.sleep(60000);
				} catch (final InterruptedException e) {
					MessagingHelper.LOGGER
							.info("Interruption during polling mode.");
					e.printStackTrace();
				}
				try {
					folder.getMessageCount();
				} catch (final MessagingException e) {
					MessagingHelper.LOGGER
							.info("Could not get EXIST message from server.");
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Checks if the incoming message is relevant for the local AlphaDoc.
	 * 
	 * @param message
	 *            the incoming message
	 * @return true, if message is relevant
	 */
	public boolean isAlphaFlowMessage(final MimeMessage message) {
		try {
			if (message.getSubject().contains("[alphaFlow Update]")
					&& message.getSubject().contains(
							this.config.getMyEpisodeId()))
				return true;
		} catch (final MessagingException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Checks if the incoming message is sent from the local actor and the local
	 * workstation.
	 * 
	 * @param message
	 *            the incoming message
	 * @return true, if message is sent from the local actor and the local
	 *         workstation
	 */
	public boolean isSentFromLocalhost(final MimeMessage message) {

		try {
			if (message.getSubject().contains(
					this.config.getLocalNodeID().getContributor()
							.getActor()
							+ "@" + HardwareAddress.getHardwareAddress()))
				return true;
		} catch (final MessagingException e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * Checks if the incoming message has already been read by the local actor
	 * on the local workstation.
	 * 
	 * @param message
	 *            the incoming message
	 * @return true, if message has already been read by the local actor on the
	 *         local workstation
	 */
	public boolean isReadByLocalhost(final MimeMessage message) {

		try {

			final String[] flags = message.getFlags().getUserFlags();

			for (final String flag : flags) {
				MessagingHelper.LOGGER.info(flag);
			}

			if (message.getFlags().contains(
					this.config.getLocalNodeID().getContributor()
							.getActor()
							+ "@" + HardwareAddress.getHardwareAddress())) {
				MessagingHelper.LOGGER
						.info("Message has been already read on this machine and was therefore skipped");
				return true;
			}
		} catch (final MessagingException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Retrieve smtp properties.
	 * 
	 * @param auth
	 *            the auth
	 * @return the properties
	 */
	public Properties retrieveSmtpProperties(final MailAuthenticator auth) {
		final Properties properties = new Properties();
		properties.put("mail.smtp.host", auth.getSmtpHost());
		properties.put("mail.smtp.socketFactory.port",
				auth.getSmtpSocketFactoryPort());
		if (auth.getSmtpSocketFactoryClass().length() > 0) {
			properties.put("mail.smtp.socketFactory.class",
					auth.getSmtpSocketFactoryClass());
		}

		properties.put("mail.smtp.auth", auth.getSmtpAuth());
		properties.put("mail.smtp.port", auth.getSmtpPort());

		// Requested by CPN: Ignore invalid SSL certificates
		properties.put("mail.smtp.ssl.checkserveridentity", "false");
		properties.put("mail.smtp.ssl.trust", "*");

		return properties;
	}

	/**
	 * Retrieve imap properties.
	 * 
	 * @param authenticator
	 *            the authenticator
	 * @return the properties
	 */
	public Properties retrieveImapProperties(
			final MailAuthenticator authenticator) {
		final Properties properties = new Properties();

		// Requested by CPN: Ignore invalid SSL certificates
		properties.put("mail.imap.ssl.checkserveridentity", "false");
		properties.put("mail.imap.ssl.trust", "*");

		// Requested by CPN: Ignore invalid SSL certificates
		properties.put("mail.imaps.ssl.checkserveridentity", "false");
		properties.put("mail.imaps.ssl.trust", "*");

		return properties;
	}

}
