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
 * $Id: ConsensusScope.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.adornment;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This defines the possible consensus scopes an adornment can be part of.
 *
 * @author cpn
 * @version $Id: ConsensusScope.java 3583 2012-02-16 01:52:45Z cpn $
 */
@XmlEnum
@XmlRootElement(name = "consensusScope")
public enum ConsensusScope {

	/** The GENERIC STANDARD. */
	@XmlEnumValue("generic_std")
	GENERIC_STD("GENERIC_STD"),
	/** The EPISODE STANDARD. */
	@XmlEnumValue("episode_std")
	EPISODE_STD("EPISODE_STD"),
	/** The INSTITUTION STANDARD. */
	@XmlEnumValue("institution_std")
	INSTITUTION_STD("INSTITUTION_STD"),
	/** The DOMAIN STANDARD. */
	@XmlEnumValue("domain_std")
	DOMAIN_STD("DOMAIN_STD");

	/** The value. */
	private final String value;

	/**
	 * Instantiates a new scope for an adaptive adornment.
	 * 
	 * @param name
	 *            the name of the scope
	 */
	ConsensusScope(final String name) {
		this.value = name;
	}

	/**
	 * Value.
	 *
	 * @return the string
	 */
	public String value() {
		return this.value;
	}

	/**
	 * From value.
	 *
	 * @param name
	 *            the corresponding name to a consensus scope
	 * @return the related scope of the adaptive adornment
	 */
	public static ConsensusScope fromValue(final String name) {
		for (final ConsensusScope c : ConsensusScope.values()) {
			if (c.value.equalsIgnoreCase(name))
				return c;
		}
		throw new IllegalArgumentException(name.toString());
	}
}
