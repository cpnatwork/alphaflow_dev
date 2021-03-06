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
 * $Id: StandardTokens.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.cra;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * This defines the generic token names.
 *
 * @author cpn
 * @version $Id: StandardTokens.java 3583 2012-02-16 01:52:45Z cpn $
 */
@XmlEnum
public enum StandardTokens {

	/** The DOYEN. */
	@XmlEnumValue("DOYEN")
	DOYEN("DOYEN TOKEN"),

	/** The INITIATOR. */
	@XmlEnumValue("INITIATOR")
	INITIATOR("INITIATOR"),

	/** The PATIENT CONTACT. */
	@XmlEnumValue("PATIENT_CONTACT")
	PATIENT_CONTACT("PATIENT CONTACT");

	/** The value. */
	private final String value;

	/**
	 * Instantiates a new StandardToken.
	 * 
	 * @param v
	 *            the v
	 */
	StandardTokens(final String v) {
		this.value = v;
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
	 * @param v
	 *            the v
	 * @return the StandardToken
	 */
	public static StandardTokens fromValue(final String v) {
		for (final StandardTokens c : StandardTokens.values()) {
			if (c.value.equals(v))
				return c;
		}
		throw new IllegalArgumentException(v.toString());
	}
}
