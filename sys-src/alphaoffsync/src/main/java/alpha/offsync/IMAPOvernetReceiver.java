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
 * $Id: IMAPOvernetReceiver.java 3786 2012-05-04 08:00:25Z uj32uvac $
 *************************************************************************/
package alpha.offsync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.Unmarshaller;

import java.util.logging.Logger;


import alpha.facades.AlphaOvernetFacade;
import alpha.model.docconfig.AlphadocConfig;
import alpha.offsync.security.MailAuthenticator;
import alpha.overnet.OvernetReceiver;
import alpha.overnet.security.SecurityUtility;
import alpha.util.HardwareAddress;

import com.sun.mail.imap.IMAPFolder;

/**
 * IMAP-based implementation of the {@link OvernetReceiver} interface.
 */
public class IMAPOvernetReceiver implements OvernetReceiver, Runnable {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(IMAPOvernetReceiver.class.getName());

	/** The unmarshaller. */
	protected Unmarshaller unmarshaller;

	/** The ovn facade. */
	private final AlphaOvernetFacade ovnFacade;

	/** The config. */
	private final AlphadocConfig config;

	/** The sec util. */
	private final SecurityUtility secUtil;

	/** The authenticator. */
	private final Authenticator authenticator;

	/** The folder. */
	private IMAPFolder folder = null;

	/** The helper. */
	private final MessagingHelper helper;

	/**
	 * Instantiates a new {@link IMAPOvernetReceiver}.
	 * 
	 * @param ovnFacade
	 *            the local {@link AlphaOvernetFacade}
	 * @param config
	 *            the local {@link AlphadocConfig}
	 * @param authenticator
	 *            the {@link Authenticator} containing the IMAP communication
	 *            credentials
	 * @param secUtil
	 *            the {@link SecurityUtility} for decryption and signature
	 *            verification of incoming messages
	 */
	public IMAPOvernetReceiver(final AlphaOvernetFacade ovnFacade,
			final AlphadocConfig config, final Authenticator authenticator,
			final SecurityUtility secUtil) {

		this.ovnFacade = ovnFacade;
		this.config = config;
		this.secUtil = secUtil;
		this.authenticator = authenticator;
		this.helper = new MessagingHelper(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.OvernetReceiver#receiveUpdates()
	 */
	@Override
	public void receiveUpdates() {
		final MailAuthenticator auth = (MailAuthenticator) this.authenticator;

		try {
			IMAPOvernetReceiver.LOGGER.info("Starting IMAP Receiver");

			// An empty Properties object is sufficient, as our session will be
			// only used to fetch emails from the server.
			// Sending email is done in the UpdateServiceSender.
			final Properties properties = this.helper
					.retrieveImapProperties((MailAuthenticator) this.authenticator);

			// Create new session with the supplied login credentials
			// NOTE: Never use getDefaultInstance(..) if the application is
			// supposed
			// to send AND receive AT THE SAME TIME! Even if sending and
			// receiving
			// takes place in DIFFERENT Threads!
			final Session session = Session.getInstance(properties,
					this.authenticator);

			// Create a store object to access the mailbox
			final Store store = session.getStore(auth.getImapMode());

			// Connect to the mailbox. Supply null as password to indicate usage
			// of the authenticator created above
			store.connect(auth.getImapHost(),
					Integer.parseInt(auth.getImapPort()), null, null);

			if (this.config.isUseSpecificMailbox()) {
				this.folder = (IMAPFolder) store.getFolder(this.config
						.getMyEpisodeId());
			} else {
				// Access the inbox folder (will be always present on the server
				// as
				// stated in RFC 3501)
				this.folder = (IMAPFolder) store.getFolder("INBOX");
			}

			// Open the inbox folder for reading AND writing
			this.folder.open(Folder.READ_WRITE);

			IMAPOvernetReceiver.LOGGER.info("Folder opened:"
					+ this.folder.getFullName());

			// Access the messages in the folder
			final Message[] messages = this.getNewMessages();

			// Process all messages since last acces to the IMAP server
			this.processMessages(messages);

			// Enter IMAP IDLE Mode to receive push notifications from the
			// server
			final MessageCountAdapter adapter = new MessageCountAdapter() {

				@Override
				public void messagesAdded(final MessageCountEvent ev) {

					IMAPOvernetReceiver.LOGGER.info("Processing new messages");
					final Message[] messages = ev.getMessages();

					try {
						IMAPOvernetReceiver.this.processMessages(messages);
					} catch (final IOException e) {
						e.printStackTrace();
					} catch (final MessagingException e) {
						e.printStackTrace();
					}
				}
			};
			this.helper.enterIdleMode(this.folder, adapter);

		} catch (final NoSuchProviderException e) {
			e.printStackTrace();
		} catch (final MessagingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Processes incoming messages.
	 * 
	 * @param messages
	 *            the incoming messages
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws MessagingException
	 *             thrown if a mailbox error is encountered
	 */
	public void processMessages(final Message[] messages) throws IOException,
			MessagingException {

		// Process each newly arrived message
		for (int i = 0; i < messages.length; i++) {

			// Only process a message if it is an alphaFlow message for the
			// current episode and was not sent from localhost
			IMAPOvernetReceiver.LOGGER
					.info("ALPHA:"
							+ this.helper
									.isAlphaFlowMessage((MimeMessage) messages[i]));
			IMAPOvernetReceiver.LOGGER.info("SENT:"
					+ this.helper
							.isSentFromLocalhost((MimeMessage) messages[i]));
			IMAPOvernetReceiver.LOGGER.info("READ:"
					+ this.helper.isReadByLocalhost((MimeMessage) messages[i]));

			if ((this.helper.isAlphaFlowMessage((MimeMessage) messages[i]))
					&& (!this.helper
							.isSentFromLocalhost((MimeMessage) messages[i]))
					&& (!this.helper
							.isReadByLocalhost((MimeMessage) messages[i]))) {
				// if (!isReadByLocalhost((MimeMessage) messages[i])) {

				final Multipart mp = (Multipart) messages[i].getContent();

				// Iterate over all parts of the message to get the
				// attachment
				final int n = mp.getCount();

				boolean messageHasAttachment = false;

				for (int j = 0; j < n; j++) {

					final String disposition = mp.getBodyPart(j)
							.getDisposition();
					if (!(disposition == null)
							&& (disposition.equalsIgnoreCase(Part.ATTACHMENT))) {

						final File attachment = File.createTempFile(
								"attachment-", ".xml");
						attachment.deleteOnExit();
						final MimeBodyPart mbp = (MimeBodyPart) mp
								.getBodyPart(j);
						mbp.saveFile(attachment);
						messageHasAttachment = true;
						IMAPOvernetReceiver.LOGGER
								.info("Attachment downloaded");

						if (mbp.getDescription()
								.equals(MessageContentDescriptions.ALPHAFLOW_CRYPTOGRAPHY_METADATA
										.toString())) {
							if (this.secUtil != null) {
								this.secUtil
										.importCryptographyMetadata(new FileInputStream(
												attachment));
							}
							continue;
						}

						if (this.secUtil != null) {
							final File literalAttachment = File.createTempFile(
									"literalAttachment-", ".xml");
							literalAttachment.deleteOnExit();

							final String secureAttachmentPath = attachment
									.getAbsolutePath();

							final PipedInputStream in2 = new PipedInputStream();
							final PipedOutputStream out2 = new PipedOutputStream(
									in2);

							new Thread(new Runnable() {
								@Override
								public void run() {

									try {
										IMAPOvernetReceiver.this.secUtil
												.decrypt(
														out2,
														new FileInputStream(
																secureAttachmentPath));
										out2.close();
									} catch (final IOException e) {
										e.printStackTrace();
									}
								}
							}).start();

							this.secUtil.verify(new FileOutputStream(
									literalAttachment), in2);

							this.ovnFacade.delegateUpdate(literalAttachment);
						} else {
							this.ovnFacade.delegateUpdate(attachment);
						}

					}

				}

				if (!messageHasAttachment) {
					IMAPOvernetReceiver.LOGGER
							.info("Malformed alphaFlow Message: Attachment missing");
				}

				// Flag message with local hostname to indicate that this
				// message has already been processed by the local machine
				final Flags flags = new Flags();
				flags.add(this.config.getLocalNodeID().getContributor()
						.getActor()
						+ "@" + HardwareAddress.getHardwareAddress());
				messages[i].setFlags(flags, true);

			} else {
				IMAPOvernetReceiver.LOGGER.info("Message was skipped.");
			}

		}

		// Save highest UID
		if (messages.length != 0) {
			this.config.setNextImapUid(this.folder
					.getUID(messages[messages.length - 1]) + 1);
		}
	}

	// /**
	// * Enters IDLE monitoring mode on the specified folder.
	// *
	// * @param folder the folder to be monitored
	// */
	// private void enterIdleMode(Folder folder) {
	//
	// folder.addMessageCountListener(new MessageCountAdapter() {
	//
	// public void messagesAdded(MessageCountEvent ev) {
	//
	// LOGGER.info("Processing new messages");
	// Message[] messages = ev.getMessages();
	//
	// try {
	// processMessages(messages);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (MessagingException e) {
	// e.printStackTrace();
	// }
	// }
	// });
	//
	// boolean folderHasIdleSupport = false;
	// try {
	// if (folder instanceof IMAPFolder) {
	//
	// LOGGER.info(folder.getFullName()
	// + " supports IMAP IDLE Command. Using push notifications.");
	// IMAPFolder f = (IMAPFolder) folder;
	// f.idle();
	// folderHasIdleSupport = true;
	// }
	// } catch (FolderClosedException e) {
	// e.printStackTrace();
	// } catch (MessagingException mex) {
	// folderHasIdleSupport = false;
	// }
	//
	// // Infinite IDLE Loop
	// while (true) {
	//
	// if(!folder.isOpen()){
	// LOGGER.info("Inbox folder closed. Receiver is terminating.");
	// return;
	// }
	//
	// if (folderHasIdleSupport && folder instanceof IMAPFolder) {
	// IMAPFolder f = (IMAPFolder) folder;
	//
	// try {
	// LOGGER.info("Sending IMAP IDLE command");
	// f.idle();
	// } catch (MessagingException e) {
	// LOGGER.info("IMAP IDLE Command not successful");
	// e.printStackTrace();
	// } catch (IllegalStateException e){
	// LOGGER.info("Access to inbox folder denied.");
	// }
	//
	// } else {
	// LOGGER.info("IMAP IDLE Command is not supported. Falling back to polling mode.");
	// try {
	// Thread.sleep(60000);
	// } catch (InterruptedException e) {
	// LOGGER.info("Interruption during polling mode.");
	// e.printStackTrace();
	// }
	// try {
	// folder.getMessageCount();
	// } catch (MessagingException e) {
	// LOGGER.info("Could not get EXIST message from server.");
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// }

	// /**
	// * Checks if the incoming message is relevant for the local AlphaDoc.
	// *
	// * @param message the incoming message
	// * @return true, if message is relevant
	// */
	// protected boolean isAlphaFlowMessage(MimeMessage message) {
	// try {
	// if (message.getSubject().contains("[alphaFlow Update]")
	// && message.getSubject().contains(config.getMyEpisodeId())) {
	//
	// return !(isSentFromLocalhost(message));
	// }
	// } catch (MessagingException e) {
	// e.printStackTrace();
	// }
	//
	// return false;
	// }

	// /**
	// * Checks if the incoming message is sent from the local actor and the
	// local workstation.
	// *
	// * @param message the incoming message
	// * @return true, if message is sent from the local actor and the local
	// workstation
	// */
	// protected boolean isSentFromLocalhost(MimeMessage message) {
	//
	// try {
	// if (message.getSubject().contains(HardwareAddress.getHardwareAddress())
	// && message.getSubject().contains(
	// config.getLocalParticipant().getContributor()
	// .getActor())) {
	// return true;
	// }
	// } catch (MessagingException e) {
	// e.printStackTrace();
	// }
	//
	// return false;
	//
	// }

	// /**
	// * Checks if the incoming message has already been read by the local actor
	// on the local workstation.
	// *
	// * @param message the incoming message
	// * @return true, if message has already been read by the local actor on
	// the local workstation
	// */
	// protected boolean isReadByLocalhost(MimeMessage message) {
	//
	// try {
	//
	// String[] flags = message.getFlags().getUserFlags();
	//
	// for (int j = 0; j < flags.length; j++) {
	// LOGGER.info(flags[j]);
	// }
	//
	// if (message.getFlags().contains(
	// config.getLocalParticipant().getContributor().getActor()
	// + "@" + HardwareAddress.getHardwareAddress())) {
	// LOGGER.info("Message has been already read on this machine and was therefore skipped");
	// return true;
	// }
	// } catch (MessagingException e) {
	// e.printStackTrace();
	// }
	//
	// return false;
	// }

	/**
	 * Gets all messages since last session from the INBOX folder.
	 * 
	 * @return the new messages
	 * @throws MessagingException
	 *             thrown if an error is encountered during message retrieval
	 */
	private Message[] getNewMessages() throws MessagingException {

		Message[] messages;
		final long uidValidity = this.folder.getUIDValidity();

		if (uidValidity > this.config.getImapUidValidity()) {

			this.config.setImapUidValidity(uidValidity);
			this.config.setNextImapUid(this.folder.getUIDNext());
			messages = this.folder.getMessages();

			IMAPOvernetReceiver.LOGGER
					.info("UIDs invalidated. Scanning whole mailbox.");

		} else {

			messages = this.folder.getMessagesByUID(
					this.config.getNextImapUid(), UIDFolder.LASTUID);

			IMAPOvernetReceiver.LOGGER
					.info("UIDs still valid. Examining messages added since last session only.");
		}

		return messages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.receiveUpdates();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.OvernetReceiver#shutdown()
	 */
	@Override
	public void shutdown() {
		if (this.folder != null) {
			if (this.folder.isOpen()) {
				try {
					this.folder.close(false);
				} catch (final MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
