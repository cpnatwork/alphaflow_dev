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
package alpha.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class Payload.
 *
 *
 * This class is the data object representing a Payload document
 *
 * @author cpn
 * @version $Id: $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "content" })
@XmlRootElement(name = "payload")
public class Payload {

	/** The content. */
	@XmlElement
	protected byte[] content;

	// @XmlElement(required=true)
	// private boolean tsa = false;
	// @XmlElement(required=true)
	// private boolean cra = false;

	// /**
	// * @return the tsa
	// */
	// public boolean isTsa() {
	// return tsa;
	// }
	//
	// /**
	// * @param tsa the tsa to set
	// */
	// public void setTsa(boolean tsa) {
	// this.tsa = tsa;
	// }

	// /**
	// * @return the cra
	// */
	// public boolean isCra() {
	// return cra;
	// }
	//
	// /**
	// * @param cra the cra to set
	// */
	// public void setCra(boolean cra) {
	// this.cra = cra;
	// }

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public byte[] getContent() {
		return this.content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content
	 *            the content to set
	 */
	public void setContent(final byte[] content) {
		this.content = content;
	}

}
