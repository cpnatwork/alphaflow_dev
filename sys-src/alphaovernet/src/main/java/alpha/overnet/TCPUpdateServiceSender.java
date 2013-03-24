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
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import java.util.logging.Logger;


import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.APAPayload;
import alpha.model.apa.Priority;
import alpha.model.cra.ContributorID;
import alpha.model.cra.EndpointID;
import alpha.model.cra.ObjectUnderConsideration;
import alpha.model.cra.Participant;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;
import alpha.overnet.event.AddAlphaCardEvent;
import alpha.overnet.event.ChangeAlphaCardDescriptorEvent;
import alpha.overnet.event.ChangePayloadEvent;
import alpha.overnet.event.ParallelJoinCallback;
import alpha.overnet.event.ParallelJoinSynchronisation;
import alpha.overnet.event.ParticipantJoinEvent;
import alpha.overnet.event.SequentialJoinCallback;
import alpha.overnet.event.SequentialJoinSynchronisation;
import alpha.util.StringWrapper;
import alpha.util.XmlBinder;

/**
 * The Class TCPUpdateServiceSender.
 * 
 * @deprecated Socket-based implementation of the {@link OvernetSender}
 *             interface.
 */
@Deprecated
public class TCPUpdateServiceSender implements OvernetSender {

	/** Global LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(TCPUpdateServiceSender.class.getName());

	/** The classes of which instances can be sent to the network. */
	@SuppressWarnings({ "unchecked" })
	private final Class[] classes = new Class[] { AlphaCardDescriptor.class,
			AddAlphaCardEvent.class, ParticipantJoinEvent.class,
			ChangeAlphaCardDescriptorEvent.class, ChangePayloadEvent.class,
			Participant.class, AlphaCardID.class, AlphaCardRelationship.class,
			Payload.class, Priority.class, ContributorID.class,
			ObjectUnderConsideration.class, StringWrapper.class,
			SequentialJoinCallback.class, SequentialJoinSynchronisation.class,
			ParallelJoinCallback.class, ParallelJoinSynchronisation.class,
			APAPayload.class, PSAPayload.class };

	/**
	 * Instantiates a new TCPUpdateServiceSender.
	 */
	public TCPUpdateServiceSender() {
	}

	/**
	 * This method is a general method for sending an object onto the socket and
	 * to the defined peers. The sending peer itself is ignored, if it is in the
	 * set of recipients. For sending the POJOs are transformed into XML. The
	 * POJOs are: {@link AlphaCardDescriptor} or {@link ChangePayloadEvent}),
	 * but these include the marshaling of the following as well:
	 * 
	 * @param object
	 *            the {@link Object} to be sent (can be
	 * @param nodeIDs
	 *            set of the network information {@link EndpointID} of each
	 *            recipient the update is to be sent to
	 * @return true, if successful {@link Participant}, {@link AlphaCardID},
	 *         {@link AlphaCardRelationship}, {@link Payload}, {@link Priority},
	 *         {@link ContributorID}, {@link ObjectUnderConsideration},
	 *         {@link StringWrapper} {@link AlphaCardDescriptor} or
	 *         {@link ChangePayloadEvent})
	 */
	@Override
	public boolean sendUpdate(final Object object, final Set<EndpointID> nodeIDs) {

		try {
			final InetAddress address = InetAddress.getLocalHost();
			final XmlBinder xmlBinder = new XmlBinder();

			for (final EndpointID nodeId : nodeIDs) {

				if (address.getHostName().contains(nodeId.getHost())) {
					// LOGGER.finer("################################### I am destHost "
					// + nodeId.getHost());
					// LOGGER.finer("################################### I am local "
					// + address.getHostName());
				} else {
					// LOGGER.finer("################################### I am remote"
					// + nodeId.getHost());
					// LOGGER.finer("################################### I am local "
					// + address.getHostName());
					final Socket socket = this.getConnection(nodeId.getHost(),
							Integer.valueOf(nodeId.getPort()));

					try {
						if (socket == null) {
							TCPUpdateServiceSender.LOGGER
									.severe("Broadcast message: This node is currently not available: "
											+ nodeId.getHost());
						} else {
							TCPUpdateServiceSender.LOGGER
									.finer("Transforming and then sending....");
							xmlBinder.store(object, socket.getOutputStream(),
									this.classes);
							TCPUpdateServiceSender.LOGGER
									.finer("Transforming and then sending.... done.");
							socket.close();
						}
					} catch (final IOException e) {
						TCPUpdateServiceSender.LOGGER.severe("Error: " + e);
					}
				}
			}

			/*
			 * // FIXME try { InetAddress local = InetAddress.getLocalHost();
			 * 
			 * for (NodeID nodeId : nodeIDs) { InetAddress adr =
			 * InetAddress.getByName(nodeId.getHost());
			 * 
			 * if(adr.equals(local)) { //
			 * LOGGER.finer("################################### I am destHost "
			 * + nodeId.getHost()); //
			 * LOGGER.finer("################################### I am local " +
			 * local.getHostName()); } else { //
			 * LOGGER.finer("################################### I am remote" +
			 * nodeId.getHost()); //
			 * LOGGER.finer("################################### I am local " +
			 * local.getHostName()); Socket socket =
			 * getConnection(nodeId.getHost(),
			 * Integer.valueOf(nodeId.getPort()));
			 * 
			 * try { if(socket == null) {LOGGER.severe(
			 * "Broadcast message: This node is currently not available: " +
			 * nodeId.getHost());setErrorMessage(
			 * "Broadcast message: This node is currently not available: " +
			 * nodeId.getHost()); } else { transformAndSend(object,
			 * socket.getOutputStream()); socket.close(); } } catch (IOException
			 * e) { LOGGER.severe("Error: " +e); } } }
			 */
		} catch (final Exception e) {
			TCPUpdateServiceSender.LOGGER.severe("Error: " + e);
		}

		return true;
	}

	/**
	 * This method sets up the socket.
	 * 
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @return the connection {@link AlphaCardDescriptor} or
	 *         {@link ChangePayloadEvent})
	 */
	protected Socket getConnection(final String host, final int port) {
		try {
			final Socket socket = new Socket(host, port);
			TCPUpdateServiceSender.LOGGER.finer("Successfully connected to...."
					+ host + " : " + port);
			return socket;
		} catch (final UnknownHostException e) {
			TCPUpdateServiceSender.LOGGER
					.severe("Connection failed: The IP address of the host could not be determined!");
		} catch (final ConnectException e) {
			TCPUpdateServiceSender.LOGGER
					.severe("Connection refused: Server not responding, node '"
							+ host + "' is unreachable!");
		} catch (final IOException e) {
			TCPUpdateServiceSender.LOGGER.severe("Error: " + e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.OvernetSender#sendUpdate(java.util.List,
	 * java.util.Set, java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public boolean sendUpdate(final List<Object> objects,
			final Set<EndpointID> recipients, final String subject,
			final String messageContent, final boolean encrypt,
			final boolean sign) {
		for (final Object obj : objects) {
			this.sendUpdate(obj, recipients);
		}
		return true;
	}
}
