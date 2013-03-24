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
package alpha.overnet.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.AlphaCardDescriptor;

/**
 * A POJO for the technical event for adding an {@link AlphaCardDescriptor}.
 * 
 */
@ObservableEvent
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "alphaCardDescriptor", "propagateChange" })
@XmlRootElement(name = "addAlphaCardEvent")
public class AddAlphaCardEvent {

	/** The alpha card descriptor. */
	@XmlElement(required = true)
	private AlphaCardDescriptor alphaCardDescriptor;

	/**
	 * Gets the alpha card descriptor.
	 * 
	 * @return the alpha card descriptor
	 */
	public AlphaCardDescriptor getAlphaCardDescriptor() {
		return this.alphaCardDescriptor;
	}

	/**
	 * Sets the alpha card descriptor.
	 * 
	 * @param alphaCardDescriptor
	 *            the new alpha card descriptor
	 */
	public void setAlphaCardDescriptor(
			final AlphaCardDescriptor alphaCardDescriptor) {
		this.alphaCardDescriptor = alphaCardDescriptor;
	}

	/** The propagate change. */
	@XmlElement(required = true)
	private boolean propagateChange;

	/**
	 * Gets the propagate change.
	 * 
	 * @return the propagate change
	 */
	public boolean getPropagateChange() {
		return this.propagateChange;
	}

	/**
	 * Sets the propagate change.
	 * 
	 * @param propagateChange
	 *            the new propagate change
	 */
	public void setPropagateChange(final boolean propagateChange) {
		this.propagateChange = propagateChange;
	}

	/**
	 * Instantiates a new adds the alpha card event.
	 * 
	 * @param alphaCardDescriptor
	 *            the alpha card descriptor
	 */
	public AddAlphaCardEvent(final AlphaCardDescriptor alphaCardDescriptor) {
		super();
		this.alphaCardDescriptor = alphaCardDescriptor;
	}

	/**
	 * Instantiates a new adds the alpha card event.
	 */
	public AddAlphaCardEvent() {
		super();
	}

	/**
	 * Gets the alpha card.
	 * 
	 * @return the alpha card
	 */
	public AlphaCardDescriptor getAlphaCard() {
		return this.alphaCardDescriptor;
	}

	/**
	 * Sets the alpha card.
	 * 
	 * @param alphaCardDescriptor
	 *            the new alpha card
	 */
	public void setAlphaCard(final AlphaCardDescriptor alphaCardDescriptor) {
		this.alphaCardDescriptor = alphaCardDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.alphaCardDescriptor == null) ? 0
						: this.alphaCardDescriptor.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final AddAlphaCardEvent other = (AddAlphaCardEvent) obj;
		if (this.alphaCardDescriptor == null) {
			if (other.alphaCardDescriptor != null)
				return false;
		} else if (!this.alphaCardDescriptor.equals(other.alphaCardDescriptor))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AddAlphaCardEvent [alphaCardDescriptor="
				+ this.alphaCardDescriptor + "]";
	}

}
