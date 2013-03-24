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
 * $Id: Doyen.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.cra;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Enum Doyen.
 *
 * @author cpn
 * @version $Id: Doyen.java 3583 2012-02-16 01:52:45Z cpn $
 */
@XmlEnum
@XmlRootElement(name = "doyen")
public enum Doyen {

	/** The DOYEN. */
	@XmlEnumValue("DOYEN")
	DOYEN("DOYEN"),
	/** The NON_DOYEN. */
	@XmlEnumValue("NON_DOYEN")
	NON_DOYEN("NON_DOYEN");

	/** The value. */
	private final String value;

	/**
	 * Instantiates a new Doyen.
	 * 
	 * @param v
	 *            the v
	 */
	Doyen(final String v) {
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
	 * @return the Doyen
	 */
	public static Doyen fromValue(final String v) {
		for (final Doyen c : Doyen.values()) {
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
		for (final Doyen v : Doyen.values()) {
			list.add(v.value);
		}
		return list.toArray(new String[list.size()]);
	}
}
