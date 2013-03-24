package alpha.model.cra;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class NodeID encapsulates the contributor and his communication endpoints
 * within the participant.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "contributor", "endpointIDs" })
@XmlRootElement(name = "nodeID")
public class NodeID {

	/**
	 * The contributor.
	 * 
	 * @aggregation
	 */
	@XmlElement(required = true)
	protected ContributorID contributor;

	/**
	 * The node.
	 * 
	 * @aggregation
	 */
	@XmlElement(required = true)
	protected List<EndpointID> endpointIDs = new LinkedList<EndpointID>();

	/**
	 * Gets the contributor.
	 * 
	 * @return the contributor
	 */
	public ContributorID getContributor() {
		return this.contributor;
	}

	/**
	 * Sets the contributor.
	 * 
	 * @param contributor
	 *            the contributor to set
	 */
	public void setContributor(final ContributorID contributor) {
		this.contributor = contributor;
	}

	/**
	 * Gets the node.
	 * 
	 * @return the node
	 */
	public EndpointID getNode() {
		return this.endpointIDs.get(0);
	}

	/**
	 * Sets the node.
	 * 
	 * @param node
	 *            the node to set
	 */
	public void setNode(final EndpointID node) {
		if (this.endpointIDs.size() == 0) {
			this.endpointIDs.add(node);
		} else {
			this.endpointIDs.set(0, node);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contributor == null) ? 0 : contributor.hashCode());
		result = prime * result
				+ ((endpointIDs == null) ? 0 : endpointIDs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeID other = (NodeID) obj;
		if (contributor == null) {
			if (other.contributor != null)
				return false;
		} else if (!contributor.equals(other.contributor))
			return false;
		if (endpointIDs == null) {
			if (other.endpointIDs != null)
				return false;
		} else if (!endpointIDs.equals(other.endpointIDs))
			return false;
		return true;
	}

}
