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
 * $Id: APAPayload.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.apa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;

/**
 * The Class APAPayload.
 *
 *
 * This class is the data object representing the APA-Payload document
 *
 * @author cpn
 * @version $Id: APAPayload.java 3583 2012-02-16 01:52:45Z cpn $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "contentCardAdornmentPrototype" })
@XmlRootElement(name = "apaPayload")
public class APAPayload extends Payload {

	/**
	 * The content card adornment prototype.
	 * 
	 * @aggregation
	 */
	@XmlElement(required = true)
	protected AlphaCardDescriptor contentCardAdornmentPrototype;

	/**
	 * Gets the content card adornment prototype.
	 *
	 * @return the contentCardAdornmentPrototype
	 */
	public AlphaCardDescriptor getContentCardAdornmentPrototype() {
		return this.contentCardAdornmentPrototype;
	}

	/**
	 * Sets the content card adornment prototype.
	 *
	 * @param contentCardAdornmentPrototype
	 *            the contentCardAdornmentPrototype to set
	 */
	public void setContentCardAdornmentPrototype(
			final AlphaCardDescriptor contentCardAdornmentPrototype) {
		this.contentCardAdornmentPrototype = contentCardAdornmentPrototype;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "APAPayload [ContentCardAdornmentPrototype="
				+ this.contentCardAdornmentPrototype + "]";
	}

}
