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
 * $Id: CoordCardType.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.identification;

import alpha.model.apa.APAPayload;
import alpha.model.cra.CRAPayload;
import alpha.model.psa.PSAPayload;

/**
 * The Enum CoordCardType.
 *
 * @author cpn
 * @version $Id: CoordCardType.java 3583 2012-02-16 01:52:45Z cpn $
 */
public enum CoordCardType {

	/** The PSA. */
	PSA("PSA") {
		@Override
		public String getModel() {
			return PSAPayload.class.getPackage().getName();
		}
	},

	/** The CRA. */
	CRA("CRA") {
		@Override
		public String getModel() {
			return CRAPayload.class.getPackage().getName();
		}
	},

	/** The APA. */
	APA("APA") {
		@Override
		public String getModel() {
			return APAPayload.class.getPackage().getName();
		}
	};

	/** The value. */
	private final String value;

	/**
	 * Instantiates a new coord card type.
	 * 
	 * @param v
	 *            the v
	 */
	CoordCardType(final String v) {
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
	 * The '$'-prefix indicates a reserved keyword in the alpha-Flow context.
	 *
	 * @return the string
	 */
	public String id() {
		return "$" + this.value;
	}

	/**
	 * From value.
	 *
	 * @param v
	 *            the v
	 * @return the coord card type
	 */
	public static CoordCardType fromValue(final String v) {
		for (final CoordCardType c : CoordCardType.values()) {
			if (c.value.equalsIgnoreCase(v))
				return c;
		}
		throw new IllegalArgumentException(v.toString());
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public abstract String getModel();

}
