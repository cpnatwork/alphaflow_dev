package alpha.model.deliveryacknowledgment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.cra.NodeID;

/**
 * The Class AcknowledgementStructureItem. Holds the information which sender
 * sent an acknowledgment to an receiver at what sending and receiving time.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sender", "senderTimestamp", "receiver",
		"receiverTimestamp" })
@XmlRootElement()
public class AcknowledgementStructureItem {

	/**
	 * The sending participant.
	 */
	@XmlElement(required = true)
	private NodeID sender;

	/**
	 * The local timestamp when original message arrived.
	 */
	@XmlElement(required = true)
	private LocalTimestamp senderTimestamp;

	/**
	 * The sending participant.
	 */
	@XmlElement(required = true)
	private NodeID receiver;

	/**
	 * The local timestamp when ack message arrived.
	 */
	@XmlElement(required = true)
	private LocalTimestamp receiverTimestamp;

	/**
	 * Instantiates a new acknowledgement structure item.
	 */
	public AcknowledgementStructureItem() {
	}

	/**
	 * Instantiates a new acknowledgement structure item.
	 * 
	 * @param sender
	 *            the sender
	 * @param receiver
	 *            the receiver
	 * @param senderTimestamp
	 *            the sender timestamp
	 * @param receiverTimestamp
	 *            the receiver timestamp
	 */
	public AcknowledgementStructureItem(NodeID sender, NodeID receiver,
			LocalTimestamp senderTimestamp, LocalTimestamp receiverTimestamp) {
		this.sender = sender;
		this.receiver = receiver;
		this.senderTimestamp = senderTimestamp;
		this.receiverTimestamp = receiverTimestamp;
	}

	/**
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	public NodeID getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param sender
	 *            the new sender
	 */
	public void setSender(NodeID sender) {
		this.sender = sender;
	}

	/**
	 * Gets the sender timestamp.
	 * 
	 * @return the sender timestamp
	 */
	public LocalTimestamp getSenderTimestamp() {
		return senderTimestamp;
	}

	/**
	 * Sets the sender timestamp.
	 * 
	 * @param senderTimestamp
	 *            the new sender timestamp
	 */
	public void setSenderTimestamp(LocalTimestamp senderTimestamp) {
		this.senderTimestamp = senderTimestamp;
	}

	/**
	 * Gets the receiver.
	 * 
	 * @return the receiver
	 */
	public NodeID getReceiver() {
		return receiver;
	}

	/**
	 * Sets the receiver.
	 * 
	 * @param receiver
	 *            the new receiver
	 */
	public void setReceiver(NodeID receiver) {
		this.receiver = receiver;
	}

	/**
	 * Gets the receiver timestamp.
	 * 
	 * @return the receiver timestamp
	 */
	public LocalTimestamp getReceiverTimestamp() {
		return receiverTimestamp;
	}

	/**
	 * Sets the receiver timestamp.
	 * 
	 * @param receiverTimestamp
	 *            the new receiver timestamp
	 */
	public void setReceiverTimestamp(LocalTimestamp receiverTimestamp) {
		this.receiverTimestamp = receiverTimestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AsItem [sender=" + sender.getContributor().getActor()
				+ ", senderTimestamp=" + senderTimestamp +
				// ", receiver=" + receiver.getContributor() +
				", receiverTimestamp=" + receiverTimestamp + "]";
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
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AcknowledgementStructureItem other = (AcknowledgementStructureItem) obj;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}

}
