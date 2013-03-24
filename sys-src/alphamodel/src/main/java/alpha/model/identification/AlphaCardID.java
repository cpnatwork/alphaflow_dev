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
package alpha.model.identification;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.io.Serializable;
import java.util.logging.Logger;


/**
 * This class defines the alphaCardDescriptor identifier. It is composed of the
 * alphaEpisode(a.k.a alphaDocument)-ID, the Institution-ID, the Doctor-ID and a
 * unique DHT-value.
 *
 * @author cpn
 * @version $Id: $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "episodeID", "cardID" })
@XmlRootElement(name = "alphaCardIdentifier")
public class AlphaCardID implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4492719391146261857L;

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphaCardID.class.getName());

	/** The episode id. */
	@XmlElement(required = true)
	protected String episodeID;

	/** partial key. */
	@XmlElement(required = true)
	protected String cardID;

	/**
	 * Instantiates a new alpha card identifier.
	 *
	 * @param episodeID
	 *            the episode id
	 * @param cardID
	 *            the card id
	 */
	public AlphaCardID(final String episodeID, final String cardID) {
		super();
		this.episodeID = episodeID;
		this.cardID = cardID;
	}

	/**
	 * Instantiates a new alpha card identifier.
	 *
	 * @param episodeID
	 *            the episode id
	 * @param coordCardType
	 *            the coord card type
	 */
	public AlphaCardID(final String episodeID, final CoordCardType coordCardType) {
		this(episodeID, coordCardType.id());
	}

	/**
	 * Instantiates a new alpha card identifier.
	 */
	public AlphaCardID() {

	}

	/**
	 * Gets the episode id.
	 *
	 * @return the episode id
	 */
	public String getEpisodeID() {
		return this.episodeID;
	}

	/**
	 * Sets the episode id.
	 *
	 * @param episodeID
	 *            the episodeID to set
	 */
	public void setEpisodeID(final String episodeID) {
		this.episodeID = episodeID;
	}

	/**
	 * Gets the card id.
	 *
	 * @return the card id
	 */
	public String getCardID() {
		return this.cardID;
	}

	/**
	 * Sets the card id.
	 *
	 * @param cardID
	 *            the cardID to set
	 */
	public void setCardID(final String cardID) {
		this.cardID = cardID;
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
				+ ((this.episodeID == null) ? 0 : this.episodeID.hashCode());
		result = (prime * result)
				+ ((this.cardID == null) ? 0 : this.cardID.hashCode());
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
		final AlphaCardID other = (AlphaCardID) obj;
		if (this.episodeID == null) {
			if (other.episodeID != null)
				return false;
		} else if (!this.episodeID.equals(other.episodeID))
			return false;
		if (this.cardID == null) {
			if (other.cardID != null)
				return false;
		} else if (!this.cardID.equals(other.cardID))
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
		return "AlphaCardID [episodeID=" + this.episodeID + ", cardID="
				+ this.cardID + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	/** {@inheritDoc} */
	@Override
	public AlphaCardID clone() {
		try {
			super.clone();
		} catch (final CloneNotSupportedException e) {
			AlphaCardID.LOGGER.severe("" + e);
		}
		final AlphaCardID id = new AlphaCardID(this.episodeID, this.cardID);
		return id;
	}

}
