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
 * $Id: PatientContact.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.cra;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Enum PatientContact.
 *
 * @author cpn
 * @version $Id: PatientContact.java 3583 2012-02-16 01:52:45Z cpn $
 */
@XmlEnum
@XmlRootElement(name = "patientContact")
public enum PatientContact {
	/** The PATIENT_CONTACT. */
	@XmlEnumValue("PATIENT_CONTACT")
	PATIENT_CONTACT("PATIENT_CONTACT"),
	/** The NON_PATIENT_CONTACT. */
	@XmlEnumValue("NON_DPATIENT_CONTACT")
	NON_PATIENT_CONTACT("NON_PATIENT_CONTACT");

	/** The value. */
	private final String value;

	/**
	 * Instantiates a new PatientContact.
	 * 
	 * @param v
	 *            the v
	 */
	PatientContact(final String v) {
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
	 * @return the PatientContact
	 */
	public static PatientContact fromValue(final String v) {
		for (final PatientContact c : PatientContact.values()) {
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
		for (final PatientContact v : PatientContact.values()) {
			list.add(v.value);
		}
		return list.toArray(new String[list.size()]);
	}
}
