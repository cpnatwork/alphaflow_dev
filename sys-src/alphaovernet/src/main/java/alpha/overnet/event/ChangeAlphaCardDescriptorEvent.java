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
import alpha.model.identification.AlphaCardID;

/**
 * A POJO for the technical event that causes the triggering of the following:
 * change an {@link AlphaCardDescriptor}.
 * 
 */
@ObservableEvent
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "alphaCardID", "alphaCardDescriptor",
		"propagateChange" })
@XmlRootElement(name = "changeAlphaCardDescriptorEvent")
public class ChangeAlphaCardDescriptorEvent {

	/** The {@link AlphaCardID}. */
	@XmlElement(required = true)
	private AlphaCardID alphaCardID;

	/** The {@link AlphaCardDescriptor}. */
	@XmlElement(required = true)
	private AlphaCardDescriptor alphaCardDescriptor;

	/**
	 * The flag that indicates, of the changes should be propagated to the other
	 * participants.
	 */
	@XmlElement(required = true)
	private boolean propagateChange;

	/**
	 * Instantiates a new {@link ChangeAlphaCardDescriptorEvent}.
	 */
	public ChangeAlphaCardDescriptorEvent() {
		super();
	}

	/**
	 * Instantiates a new {@link ChangeAlphaCardDescriptorEvent}.
	 * 
	 * @param alphaCardID
	 *            the {@link AlphaCardID}
	 * @param newAlphaCardDescriptor
	 *            the {@link AlphaCardDescriptor}
	 */
	public ChangeAlphaCardDescriptorEvent(final AlphaCardID alphaCardID,
			final AlphaCardDescriptor newAlphaCardDescriptor) {
		super();
		this.alphaCardID = alphaCardID;
		this.alphaCardDescriptor = newAlphaCardDescriptor;
	}

	/**
	 * Gets the {@link AlphaCardID}.
	 * 
	 * @return the {@link AlphaCardID}
	 */
	public AlphaCardID getAlphaCardID() {
		return this.alphaCardID;
	}

	/**
	 * Sets the {@link AlphaCardID}.
	 * 
	 * @param alphaCardID
	 *            the new {@link AlphaCardID}
	 */
	public void setAlphaCardID(final AlphaCardID alphaCardID) {
		this.alphaCardID = alphaCardID;
	}

	/**
	 * Gets the {@link AlphaCardDescriptor}.
	 * 
	 * @return the {@link AlphaCardDescriptor}
	 */
	public AlphaCardDescriptor getAlphaCardDescriptor() {
		return this.alphaCardDescriptor;
	}

	/**
	 * Sets the {@link AlphaCardDescriptor}.
	 * 
	 * @param alphaCardDescriptor
	 *            the new {@link AlphaCardDescriptor}
	 */
	public void setAlphaCardDescriptor(
			final AlphaCardDescriptor alphaCardDescriptor) {
		this.alphaCardDescriptor = alphaCardDescriptor;
	}

	/**
	 * Checks if the propagateChange flag is set.
	 * 
	 * @return true, if propagateChange is set
	 */
	public boolean isPropagateChange() {
		return this.propagateChange;
	}

	/**
	 * Sets the propagateChange flag.
	 * 
	 * @param propagateChange
	 *            the new value for the propagateChange flag
	 */
	public void setPropagateChange(final boolean propagateChange) {
		this.propagateChange = propagateChange;
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
		result = (prime * result)
				+ ((this.alphaCardID == null) ? 0 : this.alphaCardID.hashCode());
		result = (prime * result) + (this.propagateChange ? 1231 : 1237);
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
		final ChangeAlphaCardDescriptorEvent other = (ChangeAlphaCardDescriptorEvent) obj;
		if (this.alphaCardID == null) {
			if (other.alphaCardID != null)
				return false;
		} else if (!this.alphaCardID.equals(other.alphaCardID))
			return false;
		if (this.alphaCardDescriptor == null) {
			if (other.alphaCardDescriptor != null)
				return false;
		} else if (!this.alphaCardDescriptor.equals(other.alphaCardDescriptor))
			return false;
		if (this.propagateChange != other.propagateChange)
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
		return "ChangeAdornmentEvent [alphaCardID=" + this.alphaCardID
				+ ", alphaCardDescriptor=" + this.alphaCardDescriptor
				+ ", propagateChange=" + this.propagateChange + "]";
	}

}
