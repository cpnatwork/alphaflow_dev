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
package alpha.model.apa;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This defines the adornment "validity". Possible values: valid or invalid
 *
 * @author cpn
 * @version $Id: $
 */
@XmlEnum
@XmlRootElement(name = "validity")
public enum Validity {

	/** The INVALID. */
	@XmlEnumValue("INVALID")
	INVALID("INVALID"),
	/** The VALID. */
	@XmlEnumValue("VALID")
	VALID("VALID");

	/** The value. */
	private final String value;

	/**
	 * Instantiates a new validity.
	 * 
	 * @param v
	 *            the v
	 */
	Validity(final String v) {
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
	 * @return the validity
	 */
	public static Validity fromValue(final String v) {
		for (final Validity c : Validity.values()) {
			if (c.value.equalsIgnoreCase(v))
				return c;
		}
		throw new IllegalArgumentException(v.toString());
	}

	/**
	 * String values.
	 *
	 * @return the string[]
	 */
	public static String[] stringValues() {
		final List<String> list = new LinkedList<String>();
		for (final Validity v : Validity.values()) {
			list.add(v.value);
		}
		return list.toArray(new String[list.size()]);
	}
}
