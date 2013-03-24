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
package alpha.model.adornment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class defines a dynamic Adornment of an alphaCardDescriptor. The
 * implementation is based on the EAV (Entity-Attribute-Value) model
 *
 * @author cpn
 * @version $Id: $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "instance" })
@XmlRootElement(name = "prototypedAdornment")
public class PrototypedAdornment extends AdpAdornment {

	/** The instance flag. */
	@XmlElement(required = true)
	protected boolean instance = false;

	/* default constructor needed for xml binding */
	/**
	 * Instantiates a new prototyped adornment.
	 */
	public PrototypedAdornment() {
	}

	/**
	 * Instantiates a new prototyped adornment.
	 *
	 * @param name
	 *            the name
	 * @param consensusScope
	 *            the adaptive adornment consensusScope
	 * @param dataType
	 *            the data type
	 */
	public PrototypedAdornment(final String name,
			final ConsensusScope consensusScope,
			final AdornmentDataType dataType) {
		super(name, consensusScope, dataType);
	}

	/**
	 * Checks if is the instance flag.
	 *
	 * @return the instance
	 */
	public boolean isInstance() {
		return this.instance;
	}

	/**
	 * Sets the instance flag.
	 *
	 * @param instance
	 *            the instance to set
	 */
	public void setInstance(final boolean instance) {
		this.instance = instance;
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
				+ this.consensusScope + ", instance=" + this.instance + "}";
	}

}
