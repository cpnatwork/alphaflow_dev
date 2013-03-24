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

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.identification.AlphaCardID;

/**
 * A POJO for the technical event that causes the triggering of the following:
 * change the payload of an {@link AlphaCardDescriptor}.
 * 
 */
@ObservableEvent
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "alphaCardID", "payloadContainer",
		"alphaCardDescriptor", "propagateChange" })
@XmlRootElement(name = "changePayloadEvent")
public class ChangePayloadEvent {

	/** The alpha card id. */
	@XmlElement(required = true)
	private AlphaCardID alphaCardID;

	/** The obj. */
	// @XmlAnyElement(lax = true)
	// private Object obj;

	/** The propagate change. */
	@XmlElement(required = true)
	private boolean propagateChange;

	/** The payload container. */
	@XmlElement(required = true)
	private PayloadContainer payloadContainer;

	/**
	 * Gets the payload container.
	 * 
	 * @return the payload container
	 */
	public PayloadContainer getPayloadContainer() {
		return this.payloadContainer;
	}

	/**
	 * Sets the payload container.
	 * 
	 * @param payloadContainer
	 *            the new payload container
	 */
	public void setPayloadContainer(final PayloadContainer payloadContainer) {
		this.payloadContainer = payloadContainer;
	}

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

	/** The alpha card descriptor. */
	@XmlElement(required = true)
	private AlphaCardDescriptor alphaCardDescriptor;

	/**
	 * Instantiates a new change payload event.
	 */
	public ChangePayloadEvent() {
		super();
	}

	/**
	 * Instantiates a new change payload event.
	 * 
	 * @param alphaCardID
	 *            the alpha card id
	 * @param obj
	 *            the obj
	 * @param apf
	 *            the apf
	 */
	public ChangePayloadEvent(final AlphaCardID alphaCardID, final Object obj,
			final AlphaPropsFacade apf) {
		super();
		this.alphaCardID = alphaCardID;
		try {
			this.setAlphaCardDescriptor(apf.getAlphaCard(alphaCardID));
			// System.err.println("adding piggybacked ac" +
			// this.getAlphaCardDescriptor().toString());
			this.payloadContainer = new PayloadContainer();
			this.payloadContainer.setObj(obj);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.obj = obj;
	}

	/**
	 * Instantiates a new change payload event.
	 * 
	 * @param alphaCardID
	 *            the alpha card id
	 * @param obj
	 *            the obj
	 * @param acd
	 *            the acd
	 */
	public ChangePayloadEvent(final AlphaCardID alphaCardID, final Object obj,
			final AlphaCardDescriptor acd) {
		super();
		this.alphaCardID = alphaCardID;
		try {
			this.setAlphaCardDescriptor(acd);
			// System.err.println("adding piggybacked ac" +
			// this.getAlphaCardDescriptor().toString());
			this.payloadContainer = new PayloadContainer();
			this.payloadContainer.setObj(obj);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.obj = obj;
	}

	/**
	 * Instantiates a new change payload event.
	 * 
	 * @param alphaCardID
	 *            the alpha card id
	 * @param obj
	 *            the obj
	 */
	public ChangePayloadEvent(final AlphaCardID alphaCardID, final Object obj) {
		super();
		this.alphaCardID = alphaCardID;
		this.setAlphaCardDescriptor(null);
		this.payloadContainer = new PayloadContainer();
		this.payloadContainer.setObj(obj);
		// this.obj = obj;
	}

	/**
	 * Gets the alpha card id.
	 * 
	 * @return the alpha card id
	 */
	public AlphaCardID getAlphaCardID() {
		return this.alphaCardID;
	}

	/**
	 * Sets the alpha card id.
	 * 
	 * @param alphaCardID
	 *            the new alpha card id
	 */
	public void setAlphaCardID(final AlphaCardID alphaCardID) {
		this.alphaCardID = alphaCardID;
	}

	/**
	 * Gets the obj.
	 * 
	 * @return the obj
	 */
	// public Object getObj() {
	// return obj;
	// }

	/**
	 * Sets the obj.
	 * 
	 * @param obj
	 *            the new obj
	 */
	// public void setObj(Object obj) {
	// this.obj = obj;
	// }

	/**
	 * Checks if is propagate change.
	 * 
	 * @return true, if is propagate change
	 */
	public boolean isPropagateChange() {
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
				+ ((this.alphaCardID == null) ? 0 : this.alphaCardID.hashCode());
		result = (prime * result)
				+ ((this.getPayloadContainer().getObj() == null) ? 0 : this
						.getPayloadContainer().getObj().hashCode());
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
		final ChangePayloadEvent other = (ChangePayloadEvent) obj;
		if (this.alphaCardID == null) {
			if (other.alphaCardID != null)
				return false;
		} else if (!this.alphaCardID.equals(other.alphaCardID))
			return false;
		if (this.getPayloadContainer().getObj() == null) {
			if (other.getPayloadContainer().getObj() != null)
				return false;
		} else if (!this.getPayloadContainer().getObj()
				.equals(other.getPayloadContainer().getObj()))
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
		return "ChangePayloadEvent [alphaCardID=" + this.alphaCardID + ", obj="
				+ this.getPayloadContainer().getObj() + ", propagateChange="
				+ this.propagateChange + "]";
	}

}
