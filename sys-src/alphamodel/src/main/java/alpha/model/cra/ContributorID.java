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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class defines the contributor.
 *
 * @author cpn
 * @version $Id: $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "institutionID", "roleID", "actorID" })
@XmlRootElement(name = "contributor")
public class ContributorID {

	/** The institution. */
	protected String institutionID;

	/** The role. */
	protected String roleID;

	/** The actor. */
	protected String actorID;

	/**
	 * Instantiates a new contributor.
	 *
	 * @param institution
	 *            the institution
	 * @param role
	 *            the role
	 * @param actor
	 *            the actor
	 */
	public ContributorID(final String institution, final String role,
			final String actor) {
		this.institutionID = institution;
		this.roleID = role;
		this.actorID = actor;
	}

	/**
	 * default constructor needed for xml binding.
	 */
	public ContributorID() {

	}

	/**
	 * Gets the institution.
	 *
	 * @return the institution
	 */
	public String getInstitution() {
		return this.institutionID;
	}

	/**
	 * Sets the institution.
	 *
	 * @param institution
	 *            the institution to set
	 */
	public void setInstitution(final String institution) {
		this.institutionID = institution;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return this.roleID;
	}

	/**
	 * Sets the role.
	 *
	 * @param role
	 *            the role to set
	 */
	public void setRole(final String role) {
		this.roleID = role;
	}

	/**
	 * Gets the actor.
	 *
	 * @return the actor
	 */
	public String getActor() {
		return this.actorID;
	}

	/**
	 * Sets the actor.
	 *
	 * @param actor
	 *            the actor to set
	 */
	public void setActor(final String actor) {
		this.actorID = actor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Contributor [actor=" + this.actorID + ", institution="
				+ this.institutionID + ", role=" + this.roleID + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.actorID == null) ? 0 : this.actorID.hashCode());
		result = (prime * result)
				+ ((this.institutionID == null) ? 0 : this.institutionID
						.hashCode());
		result = (prime * result)
				+ ((this.roleID == null) ? 0 : this.roleID.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final ContributorID other = (ContributorID) obj;
		if (this.actorID == null) {
			if (other.actorID != null)
				return false;
		} else if (!this.actorID.equals(other.actorID))
			return false;
		if (this.institutionID == null) {
			if (other.institutionID != null)
				return false;
		} else if (!this.institutionID.equals(other.institutionID))
			return false;
		if (this.roleID == null) {
			if (other.roleID != null)
				return false;
		} else if (!this.roleID.equals(other.roleID))
			return false;
		return true;
	}

}
