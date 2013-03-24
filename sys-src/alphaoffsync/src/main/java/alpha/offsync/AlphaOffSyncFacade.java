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
 * $Id: AlphaOffSyncFacade.java 3580 2012-02-15 11:12:32Z cpn $
 *************************************************************************/
package alpha.offsync;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.drools.runtime.StatefulKnowledgeSession;

import alpha.commons.drools.ResultHandlerImpl;
import alpha.facades.AlphaOvernetFacade;
import alpha.model.cra.EndpointID;
import alpha.model.docconfig.AlphadocConfig;
import alpha.offsync.security.MailAuthenticator;
import alpha.offsync.security.OpenPGPSecurityUtility;
import alpha.overnet.OvernetReceiver;
import alpha.overnet.OvernetSender;
import alpha.overnet.PipelineManager;
import alpha.overnet.security.SecurityUtility;

/**
 * Implementation of {@link AlphaOvernetFacade} using SMTP/IMAP-based
 * implementations of {@link OvernetSender} and {@link OvernetReceiver}.
 * Cryptography is handled by an OpenPGP-based implementation of
 * {@link SecurityUtility}.
 */
public class AlphaOffSyncFacade implements AlphaOvernetFacade {

	/** The authenticator. */
	private final MailAuthenticator auth;

	/** The {@link SecurityUtility}. */
	private SecurityUtility secUtil;

	/** The {@link OvernetSender}. */
	private final OvernetSender ovnSender;

	/** The sender thread. */
	private final Thread senderThread;

	/** The {@link OvernetReceiver}. */
	private final OvernetReceiver ovnReceiver;

	/** The receiver thread. */
	private final Thread receiverThread;

	/** The {@link PipelineManager}. */
	private final PipelineManager pipeManager;

	/** The optional {@link MailboxMonitor}. */
	private MailboxMonitor monitor;

	/** The local message queue. */
	private final BlockingQueue<MessageInformation> queue;

	/**
	 * Instantiates a new {@link AlphaOffSyncFacade}.
	 * 
	 * @param sfkSession
	 *            the locally running {@link StatefulKnowledgeSession}
	 * @param config
	 *            the local {@link AlphadocConfig}
	 */
	public AlphaOffSyncFacade(final StatefulKnowledgeSession sfkSession,
			final AlphadocConfig config) {

		String ssl;
		if (config.isSsl()) {
			ssl = "javax.net.ssl.SSLSocketFactory";
		} else {
			ssl = new String("");
		}
		this.auth = new MailAuthenticator(config.getUsername(),
				config.getPassword(), config.getSmtpHost(),
				config.getSmtpPort(), ssl, "true", config.getSmtpPort(),
				config.getImapMode(), config.getImapHost(),
				config.getMailAddress(), config.getImapPort());

		if (config.isSecureCommunication()) {
			this.secUtil = new OpenPGPSecurityUtility(config.getOwnKPPath(),
					config.getHomePath() + "/publicKeyRing.asc",
					config.getOwnKPPass());
		} else {
			this.secUtil = null;
		}

		this.queue = new LinkedBlockingQueue<MessageInformation>();

		this.ovnSender = new SMTPOvernetSender(config, this.auth, this.secUtil,
				this.queue);

		// create a mailbox monitor if specified in the config file
		if (config.isUseSpecificMailbox()) {
			this.monitor = new MailboxMonitor(config, "INBOX",
					config.getMyEpisodeId(), this.auth);
			final Thread monitorThread = new Thread(this.monitor);
			monitorThread.start();
		}

		this.ovnReceiver = new IMAPOvernetReceiver(this, config, this.auth,
				this.secUtil);

		this.pipeManager = new PipelineManagerImpl(sfkSession,
				new ResultHandlerImpl());

		sfkSession.setGlobal("ovnFacade", this);

		this.senderThread = new Thread((Runnable) this.ovnSender);
		this.senderThread.start();

		this.receiverThread = new Thread((Runnable) this.ovnReceiver);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#receiveUpdates()
	 */
	@Override
	public void receiveUpdates() {

		this.receiverThread.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#sendUpdate(java.lang.Object,
	 * java.util.Set)
	 */
	@Override
	public boolean sendUpdate(final Object object,
			final Set<EndpointID> recipients) {

		try {
			final List<Object> updates = new ArrayList<Object>();
			updates.add(object);
			this.queue.put(new MessageInformation(updates, recipients, null,
					null, true, true));
		} catch (final InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#delegateUpdate(java.lang.Object)
	 */
	@Override
	public void delegateUpdate(final Object update) {

		this.pipeManager.delegateUpdate(update);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#shutdown()
	 */
	@Override
	public void shutdown() {

		while (this.senderThread.isAlive() && (this.queue.size() > 0)) {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.senderThread.interrupt();
		if (this.monitor != null) {
			this.monitor.stopMonitoring();
		}
		this.ovnReceiver.shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#sendUpdate(java.util.List,
	 * java.util.Set, java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public boolean sendUpdate(final List<Object> objects,
			final Set<EndpointID> recipients, final String subject,
			final String messageContent, final boolean encrypt,
			final boolean sign) {

		try {
			this.queue.put(new MessageInformation(objects, recipients, subject,
					messageContent, encrypt, sign));
		} catch (final InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.facades.AlphaOvernetFacade#importCryptographyMetadata(java.io.
	 * InputStream)
	 */
	@Override
	public void importCryptographyMetadata(final InputStream input) {
		if (this.secUtil != null) {
			this.secUtil.importCryptographyMetadata(input);
		}
	}

}
