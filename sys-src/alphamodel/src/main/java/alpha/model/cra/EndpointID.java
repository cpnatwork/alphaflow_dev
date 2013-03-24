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
package alpha.model.cra;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class defines the EndpointID.
 *
 * @author cpn
 * @version $Id: $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "host", "ip", "port", "emailAddress" })
@XmlRootElement(name = "endpointID")
public class EndpointID {

	/** The host. */
	protected String host;

	/** The ip. */
	protected String ip;

	/** The port. */
	protected String port;

	/** The email address. */
	protected String emailAddress;

	/**
	 * Gets the email address.
	 *
	 * @return the email address
	 */
	public String getEmailAddress() {
		return this.emailAddress;
	}

	/**
	 * Sets the email address.
	 *
	 * @param emailAddress
	 *            the new email address
	 */
	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * default constructor.
	 */
	public EndpointID() {

	}

	/**
	 * Instantiates a new node id.
	 *
	 * @param host
	 *            the host
	 * @param ip
	 *            the ip
	 * @param port
	 *            the port
	 * @param eMailAddress
	 *            the e mail address
	 */
	public EndpointID(final String host, final String ip, final String port,
			final String eMailAddress) {
		this.host = host;
		this.ip = ip;
		this.port = port;
		this.emailAddress = eMailAddress;
	}

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * Sets the host.
	 *
	 * @param host
	 *            the host to set
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIp() {
		return this.ip;
	}

	/**
	 * Sets the ip.
	 *
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(final String ip) {
		this.ip = ip;
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public String getPort() {
		return this.port;
	}

	/**
	 * Sets the port.
	 *
	 * @param port
	 *            the port to set
	 */
	public void setPort(final String port) {
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "NodeID [host=" + this.host + ", ip=" + this.ip + ", port="
				+ this.port + ", mailAddress=" + this.emailAddress + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.emailAddress == null) ? 0 : this.emailAddress
						.hashCode());
		result = (prime * result)
				+ ((this.host == null) ? 0 : this.host.hashCode());
		result = (prime * result)
				+ ((this.ip == null) ? 0 : this.ip.hashCode());
		result = (prime * result)
				+ ((this.port == null) ? 0 : this.port.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final EndpointID other = (EndpointID) obj;
		if (this.emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!this.emailAddress.equals(other.emailAddress))
			return false;
		if (this.host == null) {
			if (other.host != null)
				return false;
		} else if (!this.host.equals(other.host))
			return false;
		if (this.ip == null) {
			if (other.ip != null)
				return false;
		} else if (!this.ip.equals(other.ip))
			return false;
		if (this.port == null) {
			if (other.port != null)
				return false;
		} else if (!this.port.equals(other.port))
			return false;
		return true;
	}

}
