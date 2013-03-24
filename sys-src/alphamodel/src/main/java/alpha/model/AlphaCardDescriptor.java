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
package alpha.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.adornment.PrototypedAdornment;
import alpha.model.identification.AlphaCardID;
import alpha.model.versionmap.VersionMap;

/**
 * This abstract class defines an alpha-Card Descriptor. There are two possible
 * extensions of this class: the CoordinationAlphaCard (descriptor) and the
 * ContentAlphaCard (descriptor).
 * 
 * @author cpn
 * @version $Id: $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "versionMap", "adornments" })
@XmlRootElement(name = "alphaCardDescriptor")
public class AlphaCardDescriptor {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphaCardDescriptor.class.getName());

	/** The id. */
	@XmlElement(required = true)
	protected AlphaCardID id;

	/**
	 * The list of adornments.
	 * 
	 * @composition
	 */
	@XmlElementWrapper(name = "adornments")
	@XmlElement(name = "adornment", required = true)
	protected List<PrototypedAdornment> adornments;

	/** The {@link VersionMap} of the {@link AlphaCardDescriptor}. */
	@XmlElement(required = true)
	protected VersionMap versionMap = new VersionMap();

	/**
	 * Gets the {@link VersionMap}.
	 * 
	 * @return the {@link VersionMap}
	 */
	public VersionMap getVersionMap() {
		return this.versionMap;
	}

	/**
	 * Sets the {@link VersionMap}.
	 * 
	 * @param versionMap
	 *            the new {@link VersionMap}
	 */
	public void setVersionMap(final VersionMap versionMap) {
		this.versionMap = versionMap;
	}

	/**
	 * Instantiates a new alpha card descriptor.
	 */
	public AlphaCardDescriptor() {
		this.adornments = new ArrayList<PrototypedAdornment>();
	}

	/**
	 * Gets the alphaAdornment.
	 * 
	 * @param name
	 *            the name
	 * @return a certain alphaAdornment of the list
	 */
	public PrototypedAdornment readAdornment(final String name) {
		for (final PrototypedAdornment a : this.adornments) {
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
	public void updateOrCreateAdornment(final PrototypedAdornment adornment) {
		final PrototypedAdornment a = this.readAdornment(adornment.getName());
		if (a != null) {
			final int pos = this.adornments.indexOf(a);
			// removeAlphaAdornment(a.getName());
			// adornments.add(pos, adornment);
			this.adornments.set(pos, adornment);
		} else {
			this.adornments.add(adornment);
		}
		AlphaCardDescriptor.LOGGER.finer("Added alphaAdornment '" + adornment
				+ "'.");

	}

	/**
	 * Gets the alphaAdornment.
	 * 
	 * @return the alphaAdornment
	 */
	public Collection<PrototypedAdornment> readAdornments() {
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
		final PrototypedAdornment a = this.readAdornment(name);
		if (a != null) {
			AlphaCardDescriptor.LOGGER.finer("Remove Adornment '" + a + "'.");
			this.adornments.remove(a);
		}
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public AlphaCardID getId() {
		if (this.id == null) {
			this.id = new AlphaCardID();
		}
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param value
	 *            the new id
	 */
	public void setId(final AlphaCardID value) {
		this.id = value;
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
		final AlphaCardDescriptor other = (AlphaCardDescriptor) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
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
		return "AlphaCardDescriptor [id=" + this.id + ", [adornments "
				+ this.adornments.toString() + "] ]\n";
	}

}
