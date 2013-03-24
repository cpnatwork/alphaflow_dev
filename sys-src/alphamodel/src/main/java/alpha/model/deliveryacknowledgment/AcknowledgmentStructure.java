package alpha.model.deliveryacknowledgment;

import java.util.HashSet;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.identification.AlphaCardID;
import alpha.model.versionmap.VersionMap;

/**
 * The Class AcknowledgmentStructure. Holds a set of
 * AcknowledgementStructureItems belonging to a certain AlphaCardId and Version
 * received by the participant.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "acid", "versionMap", "acknowledgements" })
@XmlRootElement()
public class AcknowledgmentStructure {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AcknowledgmentStructure.class.getName());

	/** The acid. */
	@XmlElement(required = true)
	private AlphaCardID acid;

	/** The version map. */
	@XmlElement(required = true)
	private VersionMap versionMap = new VersionMap();

	/** The acknowledgements. */
	@XmlElementWrapper(name = "acknowledgements")
	@XmlElement(name = "ASI", required = true)
	private HashSet<AcknowledgementStructureItem> acknowledgements = new HashSet<AcknowledgementStructureItem>();

	/**
	 * Instantiates a new acknowledgment structure.
	 */
	public AcknowledgmentStructure() {
	}

	/**
	 * Instantiates a new acknowledgment structure.
	 * 
	 * @param acid
	 *            the acid
	 * @param versionMap
	 *            the version map
	 */
	public AcknowledgmentStructure(AlphaCardID acid, VersionMap versionMap) {
		this.acid = acid;
		this.versionMap = versionMap;
	}

	/**
	 * Gets the acknowledgements.
	 * 
	 * @return the acknowledgements
	 */
	public HashSet<AcknowledgementStructureItem> getAcknowledgements() {
		return acknowledgements;
	}

	/**
	 * Sets the acknowledgements.
	 * 
	 * @param acknowledgements
	 *            the new acknowledgements
	 */
	public void setAcknowledgements(
			HashSet<AcknowledgementStructureItem> acknowledgements) {
		this.acknowledgements = acknowledgements;
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
	 * Sets the acid.
	 * 
	 * @param acid
	 *            the new acid
	 */
	public void setAcid(AlphaCardID acid) {
		this.acid = acid;
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
	 * Sets the version map.
	 * 
	 * @param versionMap
	 *            the new version map
	 */
	public void setVersionMap(VersionMap versionMap) {
		this.versionMap = versionMap;
	}

	/**
	 * Adds the item to the set.
	 * 
	 * @param asi
	 *            the asi
	 */
	public void addItem(AcknowledgementStructureItem asi) {
		if (this.acknowledgements.contains(asi)) {
			this.acknowledgements.remove(asi);
			LOGGER.finer("ack already existing");
		}
		this.acknowledgements.add(asi);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder ret = new StringBuilder();
		ret.append("AcknowledgmentStructure [acid=" + acid.getCardID()
				+ ", versionMap=" + versionMap + ", acknowledgements=\n");
		for (AcknowledgementStructureItem asi : this.acknowledgements) {
			ret.append(asi.toString() + "\n");
		}
		ret.append("]");

		return ret.toString();
	}

}
