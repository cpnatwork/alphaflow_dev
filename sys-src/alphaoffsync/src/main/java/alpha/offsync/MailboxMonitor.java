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
 * $Id: MailboxMonitor.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.offsync;

import java.util.ArrayList;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeMessage;

import java.util.logging.Logger;


import alpha.model.docconfig.AlphadocConfig;
import alpha.offsync.security.MailAuthenticator;

import com.sun.mail.imap.IMAPFolder;

/**
 * Monitor which observes a specific mailbox and copies all alphaDoc-related
 * messages to another folder on the mail server.
 */
public class MailboxMonitor implements Runnable {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(MailboxMonitor.class.getName());

	/** The config. */
	private final AlphadocConfig config;

	/** The src mail box. */
	private String srcMailBox;

	/** The src folder. */
	private Folder srcFolder;

	/** The dest folder. */
	private Folder destFolder;

	/** The authenticator. */
	private final MailAuthenticator authenticator;

	/** The session. */
	private final Session session;

	/** The store. */
	private Store store;

	/** The helper. */
	private final MessagingHelper helper;

	/**
	 * Instantiates a new mailbox monitor.
	 * 
	 * @param config
	 *            the global {@link AlphadocConfig}
	 * @param srcMailBox
	 *            the mailbox to be monitored
	 * @param destMailBox
	 *            the mailbox incoming relevant messages are copied to
	 * @param authenticator
	 *            the {@link Authenticator} used for establishing a connection
	 *            to the server
	 */
	public MailboxMonitor(final AlphadocConfig config, final String srcMailBox,
			final String destMailBox, final MailAuthenticator authenticator) {
		this.authenticator = authenticator;
		this.config = config;
		this.helper = new MessagingHelper(config);
		this.session = Session.getInstance(
				this.helper.retrieveImapProperties(this.authenticator),
				this.authenticator);
		try {
			this.store = this.session
					.getStore(this.authenticator.getImapMode());
			this.srcMailBox = srcMailBox;

			this.store.connect(this.authenticator.getImapHost(),
					Integer.parseInt(this.authenticator.getImapPort()), null,
					null);
			this.destFolder = this.store.getFolder(destMailBox);
			if (!this.destFolder.exists()) {
				this.destFolder.create(Folder.HOLDS_MESSAGES);
			}
			this.store.close();
		} catch (final NoSuchProviderException e) {
			e.printStackTrace();
		} catch (final MessagingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Start monitoring the specified mailbox.
	 */
	public void startMonitoring() {
		if (this.store != null) {
			try {
				this.store.connect(this.authenticator.getImapHost(),
						this.authenticator.getMailAddress(), null);
				this.srcFolder = this.store.getFolder(this.srcMailBox);

				this.srcFolder.open(Folder.READ_WRITE);
				this.destFolder.open(Folder.READ_WRITE);

				final MessageCountAdapter adapter = new MessageCountAdapter() {
					@Override
					public void messagesAdded(final MessageCountEvent ev) {
						final Message[] messages = ev.getMessages();
						try {

							MailboxMonitor.this.srcFolder.copyMessages(
									MailboxMonitor.this
											.filterMessages(messages),
									MailboxMonitor.this.destFolder);

							if (messages.length != 0) {
								MailboxMonitor.this.config
										.setNextMonitorImapUid(((IMAPFolder) MailboxMonitor.this.srcFolder)
												.getUID(messages[messages.length - 1]) + 1);
							}

							for (final Message message : MailboxMonitor.this
									.filterMessages(messages)) {
								message.setFlag(Flags.Flag.DELETED, true);
							}
							MailboxMonitor.this.srcFolder.expunge();
						} catch (final MessagingException e) {
							e.printStackTrace();
						}
					}
				};

				Message[] messages;
				final long uidValidity = ((IMAPFolder) this.srcFolder)
						.getUIDValidity();

				if (uidValidity > this.config.getMonitorImapUidValidity()) {

					MailboxMonitor.LOGGER
							.info("UIDs invalidated. Scanning whole mailbox.");
					this.config.setMonitorImapUidValidity(uidValidity);
					this.config
							.setNextMonitorImapUid(((IMAPFolder) this.srcFolder)
									.getUIDNext());
					messages = this.srcFolder.getMessages();

				} else {

					MailboxMonitor.LOGGER
							.info("UIDs still valid. Examining messages added since last session only.");
					messages = ((IMAPFolder) this.srcFolder).getMessagesByUID(
							this.config.getNextMonitorImapUid(),
							UIDFolder.LASTUID);
				}

				this.srcFolder.copyMessages(this.filterMessages(messages),
						this.destFolder);

				for (final Message message : this.filterMessages(messages)) {
					message.setFlag(Flags.Flag.DELETED, true);
				}
				this.srcFolder.expunge();

				this.helper.enterIdleMode(this.srcFolder, adapter);

			} catch (final MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Stop monitoring the specified mailbox.
	 */
	public void stopMonitoring() {
		try {
			if ((this.srcFolder != null) && this.srcFolder.isOpen()) {
				this.srcFolder.close(true);
			}
		} catch (final MessagingException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.startMonitoring();
	}

	/**
	 * Filter all messages that are not related to the local alphaDoc.
	 * 
	 * @param messages
	 *            the messages to be filtered
	 * @return the resulting filtered messages
	 */
	public Message[] filterMessages(final Message[] messages) {

		final ArrayList<Message> copyMessages = new ArrayList<Message>();
		for (final Message message : messages) {
			if ((this.helper.isAlphaFlowMessage((MimeMessage) message))) {
				copyMessages.add(message);
			}
		}
		final Message[] returnMessages = new Message[copyMessages.size()];
		int i = 0;
		for (final Message message : copyMessages) {
			returnMessages[i] = message;
			i++;
		}
		return returnMessages;
	}

}
