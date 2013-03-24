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
 * $Id: StringWrapper.java 3579 2012-02-15 10:59:19Z cpn $
 *************************************************************************/
package alpha.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Helper class for the conversion of the adornments from String to other
 * primitive data types.
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "newValue" })
@XmlRootElement(name = "stringWrapper")
public class StringWrapper {

	/** The new value. */
	public String newValue;

	/**
	 * Instantiates a new string wrapper.
	 */
	public StringWrapper() {
	}

	/**
	 * Instantiates a new string wrapper.
	 * 
	 * @param newValue
	 *            the new value
	 */
	public StringWrapper(final String newValue) {
		super();
		this.newValue = newValue;
	}

	/**
	 * Gets the new value.
	 * 
	 * @return the new value
	 */
	public String getNewValue() {
		return this.newValue;
	}

	/**
	 * Sets the new value.
	 * 
	 * @param newValue
	 *            the new new value
	 */
	public void setNewValue(final String newValue) {
		this.newValue = newValue;
	}
}
