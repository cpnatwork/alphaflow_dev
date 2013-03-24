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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.logging.Logger;


import alpha.model.adornment.AdpAdornment;

// TODO: Auto-generated Javadoc
/**
 * This class defines the "object under consideration" (OC). <br>
 * The OC is an adornment. In healthcare the OC will be the patient.
 *
 * @author cpn
 * @version $Id: $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "adornments" })
@XmlRootElement(name = "OC")
public class ObjectUnderConsideration {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(ObjectUnderConsideration.class.getName());

	/** The id. */
	@XmlElement(name = "id")
	protected String id;

	/** The list of adornments. */
	@XmlElementWrapper(name = "adornments")
	@XmlElement(name = "adornment", required = true)
	protected List<AdpAdornment> adornments;

	/**
	 * default constructor.
	 */
	public ObjectUnderConsideration() {
	}

	/**
	 * Instantiates a new object id.
	 *
	 * @param ocId
	 *            the id
	 */
	public ObjectUnderConsideration(final String ocId) {
		this.id = ocId;
		this.adornments = new ArrayList<AdpAdornment>();
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "ObjectUnderConsideration [id=" + this.id + ", [adornments "
				+ this.adornments.toString() + "] ]\n";
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
				+ ((this.id == null) ? 0 : this.id.hashCode());
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
		final ObjectUnderConsideration other = (ObjectUnderConsideration) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Gets the alphaAdornment.
	 *
	 * @param name
	 *            the name
	 * @return a certain alphaAdornment of the list
	 */
	public AdpAdornment readAdornment(final String name) {
		for (final AdpAdornment a : this.adornments) {
			if (a.getName().equals(name))
				return a;
		}
		// LOGGER.finer("No alphaAdornment found with name '" + name + "'.");
		return null;
	}

	/**
	 * Sets the alphaAdornment.
	 *
	 * @param adornment
	 *            the adornment
	 */
	public void updateOrCreateAdornment(final AdpAdornment adornment) {
		final AdpAdornment a = this.readAdornment(adornment.getName());
		if (a != null) {
			final int pos = this.adornments.indexOf(a);
			// removeAlphaAdornment(a.getName());
			// adornments.add(pos, adornment);
			this.adornments.set(pos, adornment);
		} else {
			this.adornments.add(adornment);
		}
		ObjectUnderConsideration.LOGGER.finer("Added alphaAdornment '"
				+ adornment + "'.");

	}

	/**
	 * Gets the alphaAdornment.
	 *
	 * @return the alphaAdornment
	 */
	public Collection<AdpAdornment> readAdornments() {
		Collections.sort(this.adornments);
		return this.adornments;
	}

	/**
	 * Removes the alphaAdornment.
	 *
	 * @param name
	 *            the name of the alphaAdornment to remove
	 */
	public void deleteAdornment(final String name) {
		final AdpAdornment a = this.readAdornment(name);
		if (a != null) {
			ObjectUnderConsideration.LOGGER.finer("Remove Adornment '" + a
					+ "'.");
			this.adornments.remove(a);
		}
	}

}
