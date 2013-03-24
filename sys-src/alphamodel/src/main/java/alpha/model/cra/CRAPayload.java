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

import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.Payload;

// TODO: Auto-generated Javadoc
/**
 * The Class CRAPayload.
 *
 *
 * This class is the data object representing the CRA-Payload document
 *
 * @author cpn
 * @version $Id: $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "listOfParticipants",
		"objectUnderConsideration" })
@XmlRootElement(name = "craPayload")
public class CRAPayload extends Payload {

	/**
	 * The Lo participants.
	 * 
	 * @composition
	 */
	@XmlElement(name = "ParticipantNode", required = true)
	@XmlElementWrapper(name = "listOfParticipants")
	protected Set<Participant> listOfParticipants;

	/** The object under consideration. @composition */
	@XmlElement(required = true)
	protected ObjectUnderConsideration objectUnderConsideration;

	/**
	 * Gets the oc.
	 *
	 * @return the oc
	 */
	public ObjectUnderConsideration getOc() {
		return this.objectUnderConsideration;
	}

	/**
	 * Sets the oc.
	 *
	 * @param oc
	 *            the new oc
	 */
	public void setOc(final ObjectUnderConsideration oc) {
		this.objectUnderConsideration = oc;
	}

	/**
	 * Gets the lo participants.
	 *
	 * @return the loParticipants
	 */
	public Set<Participant> getListOfParticipants() {
		if (this.listOfParticipants == null) {
			this.listOfParticipants = new LinkedHashSet<Participant>();
		}
		return this.listOfParticipants;
	}

	/**
	 * Gets the lo contributors.
	 *
	 * @return the loParticipants
	 */
	public Set<ContributorID> getLoContributors() {
		final LinkedHashSet<ContributorID> loContributors = new LinkedHashSet<ContributorID>();
		for (final Participant participantNode : this.getListOfParticipants()) {
			loContributors.add(participantNode.getContributor());
		}
		return loContributors;
	}

	/**
	 * Gets the lo nodes.
	 *
	 * @return the loParticipants
	 */
	public Set<EndpointID> getLoNodes() {
		final LinkedHashSet<EndpointID> loContributors = new LinkedHashSet<EndpointID>();
		for (final Participant participantNode : this.getListOfParticipants()) {
			loContributors.add(participantNode.getNode());
		}
		return loContributors;
	}

	/**
	 * Sets the lo participants.
	 *
	 * @param loParticipants
	 *            the loParticipants to set
	 */
	public void setListOfParticipants(final Set<Participant> loParticipants) {
		this.listOfParticipants = loParticipants;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "CRAPayload [listOfParticipants=" + this.listOfParticipants
				+ "]";
	}

	/**
	 * Gets the actor names.
	 *
	 * @return the actor names
	 */
	public Set<String> getActorNames() {
		final LinkedHashSet<String> actorNames = new LinkedHashSet<String>();
		for (final ContributorID c : this.getLoContributors()) {
			actorNames.add(c.getActor());
		}

		return actorNames;
	}

}
