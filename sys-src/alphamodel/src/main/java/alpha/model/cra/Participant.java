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
package alpha.model.cra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

// TODO: Auto-generated Javadoc
/**
 * The Class Participant.
 * 
 * 
 * This class defines the participant. It is the technical perspective in terms
 * of Node/Desktop/a-Doc-Replication.
 * 
 * @author cpn
 * @version $Id: $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "nodeID", "tokens" })
@XmlRootElement(name = "participant")
public class Participant {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(Participant.class.getName());

	/** The node id. */
	@XmlElement(required = true)
	protected NodeID nodeID = new NodeID();

	/**
	 * The list of tokens.
	 * 
	 * @composition
	 */
	@XmlElementWrapper(name = "tokens")
	@XmlElement(name = "token", required = true)
	protected List<Token> tokens;

	/**
	 * Instantiates a new participant node.
	 */
	public Participant() {
		super();
		this.tokens = new ArrayList<Token>();
	}

	/**
	 * Gets the node id.
	 *
	 * @return the nodeID
	 */
	public NodeID getNodeID() {
		return nodeID;
	}

	/**
	 * Sets the node id.
	 *
	 * @param nodeID the nodeID to set
	 */
	public void setNodeID(NodeID nodeID) {
		this.nodeID = nodeID;
	}

	/**
	 * Gets the contributor.
	 * 
	 * @return the contributor
	 */
	public ContributorID getContributor() {
		return this.nodeID.getContributor();
	}

	/**
	 * Sets the contributor.
	 * 
	 * @param contributor
	 *            the contributor to set
	 */
	public void setContributor(final ContributorID contributor) {
		this.nodeID.setContributor(contributor);
		;
	}

	/**
	 * Gets the node.
	 * 
	 * @return the node
	 */
	public EndpointID getNode() {
		return this.nodeID.getNode();
	}

	/**
	 * Sets the node.
	 * 
	 * @param node
	 *            the node to set
	 */
	public void setNode(final EndpointID node) {
		this.nodeID.setNode(node);
	}

	/**
	 * Gets the token.
	 * 
	 * @param name
	 *            the name
	 * @return a certain token of the list
	 */
	public Token readToken(final String name) {
		for (final Token a : this.tokens) {
			if (a.getName().equals(name))
				return a;
		}
		LOGGER.finer("No token found with name '" + name + "'.");
		return null;
	}

	/**
	 * Sets the token.
	 * 
	 * @param token
	 *            the token
	 */
	public void updateOrCreateToken(final Token token) {
		final Token a = this.readToken(token.getName());
		if (a != null) {
			final int pos = this.tokens.indexOf(a);
			this.tokens.set(pos, token);
		} else {
			this.tokens.add(token);
		}
		LOGGER.finer("Added token '" + token + "'.");
	}

	/**
	 * Gets the tokens.
	 * 
	 * @return the tokens
	 */
	public List<Token> readTokens() {
		Collections.sort(this.tokens);
		return this.tokens;
	}

	/**
	 * Removes the token.
	 * 
	 * @param name
	 *            the name of the token to remove
	 */
	public void deleteToken(final String name) {
		final Token a = this.readToken(name);
		if (a != null) {
			Participant.LOGGER.finer("Remove Token '" + a + "'.");
			this.tokens.remove(a);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeID == null) ? 0 : nodeID.hashCode());
		result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participant other = (Participant) obj;
		if (nodeID == null) {
			if (other.nodeID != null)
				return false;
		} else if (!nodeID.equals(other.nodeID))
			return false;
		// if (tokens == null) {
		// if (other.tokens != null)
		// return false;
		// } else if (!tokens.equals(other.tokens))
		// return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "ParticipantNode [" + this.nodeID.getContributor() + ", "
				+ this.nodeID.getNode() + "]";
	}
}
