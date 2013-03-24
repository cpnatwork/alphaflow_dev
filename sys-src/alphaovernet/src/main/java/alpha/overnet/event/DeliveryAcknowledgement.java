package alpha.overnet.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.cra.Participant;
import alpha.model.deliveryacknowledgment.LocalTimestamp;
import alpha.model.identification.AlphaCardID;
import alpha.model.versionmap.VersionMap;

/**
 * The message that acknowledges the delivery of a message.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "acid", "versionMap", "senderTimestamp",
		"sender", "propagateChange" })
@XmlRootElement()
public class DeliveryAcknowledgement {

	/**
	 * The acid of the original message that is to be acknowledged.
	 */
	@XmlElement(required = true)
	private AlphaCardID acid;

	/**
	 * The sending participant.
	 */
	@XmlElement(required = true)
	private Participant sender;

	/**
	 * True, if message should be propagated.
	 */
	@XmlElement(required = true)
	private boolean propagateChange = true;

	/**
	 * The local timestamp when original message arrived.
	 */
	@XmlElement(required = true)
	private LocalTimestamp senderTimestamp;

	/** The Version map of the original ac to be acknowledged. */
	@XmlElement(required = true)
	private VersionMap versionMap;

	/**
	 * Instantiates a new delivery acknowledgement.
	 */
	public DeliveryAcknowledgement() {
	}

	/**
	 * Instantiates a new {@link DeliveryAcknowledgement}.
	 * 
	 * @param acd
	 *            the acd
	 * @param versionMap
	 *            the version map
	 * @param timestamp
	 *            the timestamp
	 * @param participant
	 *            the participant
	 */
	public DeliveryAcknowledgement(AlphaCardID acd, VersionMap versionMap,
			LocalTimestamp timestamp, Participant participant) {
		super();
		this.acid = acd;
		this.versionMap = versionMap;
		this.senderTimestamp = timestamp;
		this.sender = participant;
	}

	/**
	 * Gets the acid.
	 * 
	 * @return the acid
	 */
	public AlphaCardID getAcid() {
		return acid;
	}

	/**
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	public Participant getSender() {
		return sender;
	}

	/**
	 * Checks if is propagate change.
	 * 
	 * @return true, if is propagate change
	 */
	public boolean isPropagateChange() {
		return propagateChange;
	}

	/**
	 * Sets the propagate change.
	 * 
	 * @param propagateChange
	 *            the new propagate change
	 */
	public void setPropagateChange(boolean propagateChange) {
		this.propagateChange = propagateChange;
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
	 * Gets the version map.
	 * 
	 * @return the version map
	 */
	public VersionMap getVersionMap() {
		return versionMap;
	}

	/**
	 * Sets the acid.
	 * 
	 * @param acd
	 *            the new acid
	 */
	public void setAcid(AlphaCardID acd) {
		this.acid = acd;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param participant
	 *            the new sender
	 */
	public void setSender(Participant participant) {
		this.sender = participant;
	}

	/**
	 * Sets the sender timestamp.
	 * 
	 * @param timestamp
	 *            the new sender timestamp
	 */
	public void setSenderTimestamp(LocalTimestamp timestamp) {
		this.senderTimestamp = timestamp;
	}

	/**
	 * Sets the version map.
	 * 
	 * @param versionMap
	 *            the new version map
	 */
	public void setVersionMap(VersionMap versionMap) {
		this.versionMap = versionMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeliveryAcknowledgement [acd=" + acid + ", versionMap="
				+ versionMap + ", timestamp=" + senderTimestamp
				+ ", participant=" + sender + "]";
	}

}
