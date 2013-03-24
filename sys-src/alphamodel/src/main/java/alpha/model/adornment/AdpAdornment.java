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
 * $Id: AdpAdornment.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.model.adornment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.logging.Logger;


/**
 * This class defines a dynamic Adornment of an alphaCardDescriptor. The
 * implementation is based on the EAV (Entity-Attribute-Value) model
 *
 * @author cpn
 * @version $Id: AdpAdornment.java 3597 2012-02-20 15:13:03Z cpn $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "name", "dataType", "enumRange", "value",
		"consensusScope" })
@XmlRootElement(name = "adaptiveAdornment")
public class AdpAdornment implements Comparable<AdpAdornment> {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AdpAdornment.class.getName());

	/** The name. */
	@XmlElement(required = true)
	protected String name;

	/**
	 * The data type.
	 * 
	 * @aggregation
	 */
	@XmlElement(required = true)
	protected AdornmentDataType dataType;

	/**
	 * The enum range.
	 * 
	 * @composition
	 */
	@XmlElement(required = true)
	protected AdornmentEnumRange enumRange;

	/** The value. */
	@XmlElement(required = true)
	protected String value;

	/**
	 * The consensusScope.
	 * 
	 * @composition
	 * */
	@XmlElement(required = true)
	protected ConsensusScope consensusScope;

	/* default constructor needed for xml binding */
	/**
	 * Instantiates a new adp adornment.
	 */
	public AdpAdornment() {
	}

	/**
	 * Instantiates a new adaptive adornment.
	 *
	 * @param name
	 *            the name
	 * @param consensusScope
	 *            the adaptive adornment consensusScope
	 * @param dataType
	 *            the data type
	 */
	public AdpAdornment(final String name, final ConsensusScope consensusScope,
			final AdornmentDataType dataType) {
		this.name = name;
		this.consensusScope = consensusScope;
		this.dataType = dataType;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name of the dynamic Adornment to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets the data type.
	 *
	 * @return the dataType
	 */
	public AdornmentDataType getDataType() {
		return this.dataType;
	}

	/**
	 * Sets the data type.
	 *
	 * @param dataType
	 *            the data type of the dynamic Adornment to set
	 */
	public void setDataType(final AdornmentDataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the value of the dynamic Adornment to set
	 */
	public void setValue(final String value) {
		if (value != null) {
			if (this.dataType.validate(value, this.enumRange)) {
				if (this.dataType.equals(AdornmentDataType.ENUM)) {
					this.value = this.enumRange.valueOf(value);
				} else {
					this.value = value;
				}
				AdpAdornment.LOGGER.finer("Set value '" + this.value
						+ "« to Adornment: '" + this.name + "«.");
			} else {
				AdpAdornment.LOGGER.finer("Invalid Value '" + this.value
						+ "' for Adornment '" + this.name + "' of type '"
						+ this.dataType + "'.");
			}
		} else {
			this.value = value;
			AdpAdornment.LOGGER.finer("Set value '" + this.value
					+ "« to Adornment: '" + this.name + "«.");
		}
	}

	/**
	 * Gets the enum range.
	 *
	 * @return the enumRange
	 */
	public AdornmentEnumRange getEnumRange() {
		return this.enumRange;
	}

	/**
	 * Sets the enum range.
	 *
	 * @param enumRange
	 *            the new enum range
	 */
	public void setEnumRange(final AdornmentEnumRange enumRange) {
		this.enumRange = enumRange;
	}

	/**
	 * Gets the consensusScope.
	 *
	 * @return the consensusScope
	 */
	public ConsensusScope getConsensusScope() {
		return this.consensusScope;
	}

	/**
	 * Sets the consensusScope.
	 *
	 * @param consensusScope
	 *            the new consensusScope
	 */
	public void setConsensusScope(final ConsensusScope consensusScope) {
		this.consensusScope = consensusScope;
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
				+ ((this.name == null) ? 0 : this.name.hashCode());
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
		final AdpAdornment other = (AdpAdornment) obj;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
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
		return "AdpAdornment {name=" + this.name + ", dataType="
				+ this.dataType + ", value=" + this.value + ", consensusScope="
				+ this.consensusScope + "}";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public int compareTo(final AdpAdornment a) {
		int result = 0;
		if (this.consensusScope.ordinal() > a.consensusScope.ordinal()) {
			result = 1;
		} else if (this.consensusScope.ordinal() < a.consensusScope.ordinal()) {
			result = -1;
		}
		return result;
	}

}
