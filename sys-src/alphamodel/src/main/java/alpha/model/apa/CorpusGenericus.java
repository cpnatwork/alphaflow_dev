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
 * $Id: CorpusGenericus.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.apa;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * This defines the generic adornment names of an alphaCardDescriptor.
 *
 * @author cpn
 * @version $Id: CorpusGenericus.java 3583 2012-02-16 01:52:45Z cpn $
 */
@XmlEnum
public enum CorpusGenericus {

	/** The TITLE. */
	@XmlEnumValue("alphaCard title")
	TITLE("AlphaCard Title"),
	/** The INSTITUTION. */
	@XmlEnumValue("institutionId")
	INSTITUTION("Institution ID"),
	/** The ROLE. */
	@XmlEnumValue("roleId")
	ROLE("Role ID"),
	/** The ACTOR. */
	@XmlEnumValue("actorId")
	ACTOR("Actor ID"),
	/** The OCID. */
	@XmlEnumValue("ocId")
	OCID("OC ID"),
	/** The VALIDITY. */
	@XmlEnumValue("validity")
	VALIDITY("Validity"),
	/** The VISIBILITY. */
	@XmlEnumValue("visibility")
	VISIBILITY("Visibility"),
	/** The VERSION. */
	@XmlEnumValue("version")
	VERSION("Version"),
	/** The VERSIONCONTROL. */
	@XmlEnumValue("versionControl")
	VERSIONCONTROL("Version Control"),
	/** The VARIANT. */
	@XmlEnumValue("variant")
	VARIANT("Variant"),
	/** The FUNDAMENTALSEMANTICTYPE. */
	@XmlEnumValue("fundamentalSemanticType")
	FUNDAMENTALSEMANTICTYPE("Fund. Semantic Type"),
	/** The SYNTACTICPAYLOADTYPE. */
	@XmlEnumValue("SyntacticPayloadType")
	SYNTACTICPAYLOADTYPE("Syn. Payload Type"),
	/** The ALPHACARDTYPE. */
	@XmlEnumValue("alphaCardType")
	ALPHACARDTYPE("AlphaCard Type"),
	/** The DUEDATE. */
	@XmlEnumValue("dueDate")
	DUEDATE("Due Date"),
	/** The DEFERRED. */
	@XmlEnumValue("deferred")
	DEFERRED("Deferred"),
	/** The DELETED. */
	@XmlEnumValue("deleted")
	DELETED("Deleted"),
	/** The PRIORITY. */
	@XmlEnumValue("priority")
	PRIORITY("Priority");

	/** The value. */
	private final String value;

	/**
	 * Instantiates a new corpus genericus.
	 * 
	 * @param v
	 *            the v
	 */
	CorpusGenericus(final String v) {
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
	 * @return the corpus genericus
	 */
	public static CorpusGenericus fromValue(final String v) {
		for (final CorpusGenericus c : CorpusGenericus.values()) {
			if (c.value.equals(v))
				return c;
		}
		throw new IllegalArgumentException(v.toString());
	}
}
