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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.Unmarshaller;

import org.drools.WorkingMemory;
import org.drools.concurrent.FireAllRules;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.pipeline.Pipeline;
import org.drools.runtime.pipeline.ResultHandler;
import java.util.logging.Logger;


import alpha.commons.drools.ResultHandlerImpl;
import alpha.facades.AlphaPropsFacade;

/**
 * The Class TCPUpdateServiceReceiver.
 * 
 * @deprecated Socket-based implementation of the {@link OvernetReceiver}
 *             interface.
 */
@Deprecated
public class TCPUpdateServiceReceiver implements OvernetReceiver, Runnable {

	/** Global LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(TCPUpdateServiceReceiver.class.getName());

	/** Port the receiver listens to. */
	protected int listeningPort;

	/** Lock for coordination concurrent modifications. */
	final Lock lock = new ReentrantLock();

	/** The pipeline to the current drools session. */
	protected Pipeline pipeline;

	/** The result handler. */
	protected ResultHandler resultHandler;

	/** The unmarshaller. */
	protected Unmarshaller unmarshaller;

	/** The session. */
	private final StatefulKnowledgeSession sfkSession;

	/**
	 * Instantiates a new tCP update service receiver.
	 * 
	 * @param listeningPort
	 *            the listening port
	 * @param sfkSession
	 *            the sfk session
	 * @param pipeline
	 *            the pipeline
	 * @param resultHandler
	 *            the result handler
	 */
	public TCPUpdateServiceReceiver(final int listeningPort,
			final StatefulKnowledgeSession sfkSession, final Pipeline pipeline,
			final ResultHandler resultHandler) {
		this.listeningPort = listeningPort;
		this.sfkSession = sfkSession;
		this.pipeline = pipeline;
		this.resultHandler = resultHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.UpdateServiceReceiver#setPort(int)
	 */
	/**
	 * Sets the port.
	 * 
	 * @param port
	 *            the new port
	 */
	public void setPort(final int port) {
		this.listeningPort = port;
	}

	/**
	 * This method accepts the incoming socket connection and puts the received
	 * object onto the pipeline. Subsequently {@link FireAllRules} is invoked on
	 * the new state of the {@link WorkingMemory}. Eventually the socket is
	 * closed. The insertion into the {@link StatefulKnowledgeSession} and the
	 * triggering of the rules is handled in a {@link Lock}.
	 * 
	 */
	@Override
	public void receiveUpdates() {
		try {
			TCPUpdateServiceReceiver.LOGGER.finer("Creating server socket...");
			final ServerSocket serverSocket = new ServerSocket(
					this.listeningPort);

			while (true) {
				TCPUpdateServiceReceiver.LOGGER.finer("Listening....");
				final Socket clientSocket = serverSocket.accept();
				TCPUpdateServiceReceiver.LOGGER
						.finer("Accepted incoming update connection");

				this.lock.lock();
				try {
					this.pipeline.insert(ResourceFactory
							.newInputStreamResource(clientSocket
									.getInputStream()), this.resultHandler);
					this.sfkSession.fireAllRules();
				} finally {
					this.lock.unlock();
				}
				TCPUpdateServiceReceiver.LOGGER
						.finer("Sent incoming event onto pipeline."
								+ ((ResultHandlerImpl) this.resultHandler)
										.getObject().getClass());
				clientSocket.close();

				// FIXME
				// First idea was to open a new thread for each accepted
				// connection, in order for the main thread to stay free for new
				// incoming connections
				// Problems with race conditions occurred..

				// Thread[] theThreads = new Thread[10000];
				// for (int counter = 0; counter <= theThreads.length;
				// counter++) {
				// Thread t = new Thread(new Runnable() {
				// @Override
				// public void run() {
				// try {
				// lock.lock();
				// pipeline.insert(ResourceFactory.newInputStreamResource(clientSocket.getInputStream()),
				// resultHandler);
				// session.fireAllRules();
				// lock.unlock();
				// LOGGER.finer("Sent incoming event onto pipeline." +
				// ((ResultHandlerImpl) resultHandler).getObject().getClass());
				// clientSocket.close();
				// } catch (IOException e) {
				// LOGGER.severe("Error: " +e);
				// }
				// }
				// });
				//
				// theThreads[counter] = t;
				// }
				//
				// for (Thread thread : theThreads) {
				// thread.start();
				// }
			}

		} catch (final IOException e) {
			TCPUpdateServiceReceiver.LOGGER.severe("Error: " + e);
			// throw new RuntimeException(e);
		}
	}

	/**
	 * This method sets up {@link ResultHandler} needed for the pipeline.
	 * 
	 * @param resultHandler
	 *            the custom implementation of a {@link ResultHandler} passed
	 *            from the {@link AlphaPropsFacade}
	 */
	public void setResultHandler(final ResultHandler resultHandler) {
		this.resultHandler = resultHandler;
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
		// TODO Auto-generated method stub

	}

}
