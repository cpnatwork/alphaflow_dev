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
package alpha.model.psa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.identification.AlphaCardID;

/**
 * This class defines a single relationship object. Each relationship has a
 * source and a destination alphaCardDescriptor and a predefined type.
 *
 * @author cpn
 * @version $Id: $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "srcID", "dstID", "type" })
@XmlRootElement(name = "alphaCardRelationship")
public class AlphaCardRelationship {

	/** The src id. */
	@XmlElement(required = true)
	protected AlphaCardID srcID;

	/** The dst id. */
	@XmlElement(required = true)
	protected AlphaCardID dstID;

	/**
	 * The type.
	 * 
	 * @aggregation
	 */
	@XmlElement(required = true)
	protected AlphaCardRelationshipType type;

	/**
	 * Instantiates a new alpha card relationship.
	 *
	 * @param srcID
	 *            the src id
	 * @param dstID
	 *            the dst id
	 * @param type
	 *            the type
	 */
	public AlphaCardRelationship(final AlphaCardID srcID,
			final AlphaCardID dstID, final AlphaCardRelationshipType type) {
		super();
		this.srcID = srcID;
		this.dstID = dstID;
		this.type = type;
	}

	/**
	 * Instantiates a new alpha card relationship.
	 */
	public AlphaCardRelationship() {

	}

	/**
	 * Gets the src id.
	 *
	 * @return the src id
	 */
	public AlphaCardID getSrcID() {
		if (this.srcID == null) {
			this.srcID = new AlphaCardID();
		}
		return this.srcID;
	}

	/**
	 * Sets the src id.
	 *
	 * @param srcID
	 *            the srcID to set
	 */
	public void setSrcID(final AlphaCardID srcID) {
		this.srcID = srcID;
	}

	/**
	 * Gets the dst id.
	 *
	 * @return the dst id
	 */
	public AlphaCardID getDstID() {
		if (this.dstID == null) {
			this.dstID = new AlphaCardID();
		}
		return this.dstID;
	}

	/**
	 * Sets the dst id.
	 *
	 * @param dstID
	 *            the dstID to set
	 */
	public void setDstID(final AlphaCardID dstID) {
		this.dstID = dstID;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public AlphaCardRelationshipType getType() {
		return this.type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the type to set
	 */
	public void setType(final AlphaCardRelationshipType type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "AlphaCardRelationship [srcID=" + this.srcID + ", dstID="
				+ this.dstID + ", type=" + this.type + "]";
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
				+ ((this.dstID == null) ? 0 : this.dstID.hashCode());
		result = (prime * result)
				+ ((this.srcID == null) ? 0 : this.srcID.hashCode());
		result = (prime * result)
				+ ((this.type == null) ? 0 : this.type.hashCode());
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
		final AlphaCardRelationship other = (AlphaCardRelationship) obj;
		if (this.dstID == null) {
			if (other.dstID != null)
				return false;
		} else if (!this.dstID.equals(other.dstID))
			return false;
		if (this.srcID == null) {
			if (other.srcID != null)
				return false;
		} else if (!this.srcID.equals(other.srcID))
			return false;
		if (this.type == null) {
			if (other.type != null)
				return false;
		} else if (!this.type.equals(other.type))
			return false;
		return true;
	}

}
