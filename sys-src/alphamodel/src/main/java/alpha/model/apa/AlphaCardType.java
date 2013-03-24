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
 * This defines the possible alphaCardDescriptor types of a ContentAlphaCard.
 * Possible values: Referral Voucher or Results Report. For all other cards the
 * value is null.
 *
 * @author cpn
 * @version $Id: $
 */
@XmlEnum
@XmlRootElement(name = "alphaCardType")
public enum AlphaCardType {

	/** The DOCUMENTATION. */
	@XmlEnumValue("documentation")
	DOCUMENTATION("DOCUMENTATION"),

	/** The REFERRA l_ voucher. */
	@XmlEnumValue("referral_voucher")
	REFERRAL_VOUCHER("REFERRAL_VOUCHER"),

	/** The RESULT s_ report. */
	@XmlEnumValue("result_report")
	RESULTS_REPORT("RESULTS_REPORT");

	/** The value. */
	private final String value;

	/**
	 * Instantiates a new alpha card type.
	 * 
	 * @param v
	 *            the v
	 */
	AlphaCardType(final String v) {
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
	 * @return the alpha card type
	 */
	public static AlphaCardType fromValue(final String v) {
		for (final AlphaCardType c : AlphaCardType.values()) {
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
		for (final AlphaCardType a : AlphaCardType.values()) {
			list.add(a.value);
		}
		return list.toArray(new String[list.size()]);
	}
}
