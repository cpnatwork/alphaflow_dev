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
package alpha.model.docconfig;

import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.cra.NodeID;

/**
 * The Class AlphadocConfig.
 * 
 * 
 * This class is the data object representing the alphaconfig document
 * 
 * @author cpn
 * @version $Id: $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "homePath", "myEpisodeId",
		"myCurrentlyActiveCardId", "mailAddress", "username", "password",
		"smtpHost", "smtpPort", "imapMode", "imapHost", "imapPort",
		"useSpecificMailbox", "ssl", "ownKPPath", "ownKPPass", "pkPath",
		"imapUidValidity", "nextImapUid", "monitorImapUidValidity",
		"nextMonitorImapUid", "isInvitation", "secureCommunication",
		"localNodeID", "acknowledgeDelivery" })
@XmlRootElement(name = "alphadocconfig")
public class AlphadocConfig implements Cloneable {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphadocConfig.class.getName());

	/** The local home path. */
	@XmlElement(required = true)
	protected String homePath = "";

	/** The my episode id. */
	@XmlElement(required = true)
	protected String myEpisodeId = "";

	/** The my currently active card id. */
	@XmlElement(required = true)
	protected String myCurrentlyActiveCardId = "";

	/** The next imap uid. */
	@XmlElement(required = false)
	protected long nextImapUid;

	/** The next monitor imap uid. */
	@XmlElement(required = false)
	protected long nextMonitorImapUid;

	/** The monitor imap uid validity. */
	@XmlElement(required = false)
	protected long monitorImapUidValidity;

	/** The own kp path. */
	@XmlElement(required = false)
	protected String ownKPPath;

	/** The own kp pass. */
	@XmlElement(required = false)
	protected String ownKPPass;

	/** The pk path. */
	@XmlElement(required = false)
	protected String pkPath;

	/** The mail address. */
	@XmlElement(required = true)
	protected String mailAddress;

	/** The username. */
	@XmlElement(required = true)
	protected String username;

	/** The password. */
	@XmlElement(required = true)
	protected String password;

	/** The smtp host. */
	@XmlElement(required = true)
	protected String smtpHost;

	/** The smtp port. */
	@XmlElement(required = true)
	protected String smtpPort;

	/** The imap mode. */
	@XmlElement(required = true)
	protected String imapMode;

	/** The imap host. */
	@XmlElement(required = true)
	protected String imapHost;

	/** The imap port. */
	@XmlElement(required = true)
	protected String imapPort;

	/** The is invitation. */
	@XmlElement(required = true)
	protected boolean isInvitation = false;

	/** The secure communication. */
	@XmlElement(required = true)
	protected boolean secureCommunication = false;

	/** The ssl. */
	@XmlElement(required = true)
	protected boolean ssl = true;

	/** The use specific mailbox. */
	@XmlElement(required = true)
	protected boolean useSpecificMailbox = false;

	/**
	 * Indicates whether the reception of a new version of an alpha card should
	 * be acknowledged
	 */
	@XmlElement(required = true)
	boolean acknowledgeDelivery = true;

	/**
	 * Checks if is acknowledge delivery.
	 * 
	 * @return the acknowledgeDelivery
	 */
	public boolean isAcknowledgeDelivery() {
		return acknowledgeDelivery;
	}

	/**
	 * Sets the acknowledge delivery.
	 * 
	 * @param acknowledgeDelivery
	 *            the acknowledgeDelivery to set
	 */
	public void setAcknowledgeDelivery(boolean acknowledgeDelivery) {
		this.acknowledgeDelivery = acknowledgeDelivery;
	}

	/**
	 * Checks if is ssl.
	 * 
	 * @return true, if is ssl
	 */
	public boolean isSsl() {
		return this.ssl;
	}

	/**
	 * Sets the ssl.
	 * 
	 * @param ssl
	 *            the new ssl
	 */
	public void setSsl(final boolean ssl) {
		this.ssl = ssl;
	}

	/**
	 * Checks if is secure communication.
	 * 
	 * @return true, if is secure communication
	 */
	public boolean isSecureCommunication() {
		return this.secureCommunication;
	}

	/**
	 * Sets the secure communication.
	 * 
	 * @param secureCommunication
	 *            the new secure communication
	 */
	public void setSecureCommunication(final boolean secureCommunication) {
		this.secureCommunication = secureCommunication;
	}

	/**
	 * Checks if is invitation.
	 * 
	 * @return true, if is invitation
	 */
	public boolean isInvitation() {
		return this.isInvitation;
	}

	/**
	 * Sets the invitation.
	 * 
	 * @param isInvitation
	 *            the new invitation
	 */
	public void setInvitation(final boolean isInvitation) {
		this.isInvitation = isInvitation;
	}

	private NodeID localNodeID;

	/**
	 * Gets the local node id.
	 * 
	 * @return the localNodeID
	 */
	public NodeID getLocalNodeID() {
		return localNodeID;
	}

	/**
	 * Sets the local node id.
	 * 
	 * @param localNodeID
	 *            the localNodeID to set
	 */
	public void setLocalNodeID(NodeID localNodeID) {
		this.localNodeID = localNodeID;
	}

	/**
	 * Gets the imap port.
	 * 
	 * @return the imap port
	 */
	public String getImapPort() {
		return this.imapPort;
	}

	/**
	 * Sets the imap port.
	 * 
	 * @param imapPort
	 *            the new imap port
	 */
	public void setImapPort(final String imapPort) {
		this.imapPort = imapPort;
	}

	/**
	 * Gets the my episode id.
	 * 
	 * @return the myEpisodeId
	 */
	public String getMyEpisodeId() {
		return this.myEpisodeId;
	}

	/**
	 * Sets the my episode id.
	 * 
	 * @param epId
	 *            the new my episode id
	 */
	public void setMyEpisodeId(final String epId) {
		this.myEpisodeId = epId;
	}

	/**
	 * Gets the my currently active card id.
	 * 
	 * @return the myCurrentlyActiveCardId
	 */
	public String getMyCurrentlyActiveCardId() {
		return this.myCurrentlyActiveCardId;
	}

	/**
	 * Sets the my currently active card id.
	 * 
	 * @param cardId
	 *            the new my currently active card id
	 */
	public void setMyCurrentlyActiveCardId(final String cardId) {
		this.myCurrentlyActiveCardId = cardId;
	}

	/**
	 * Gets the local home path.
	 * 
	 * @return the local home path
	 */
	public String getHomePath() {
		return this.homePath;
	}

	/**
	 * Sets the local home path.
	 * 
	 * @param homePath
	 *            the new local home path
	 */
	public void setHomePath(final String homePath) {
		this.homePath = homePath;
	}

	// FIXME: why is cloning used?
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	/** {@inheritDoc} */
	@Override
	public AlphadocConfig clone() {
		try {
			super.clone();
		} catch (final CloneNotSupportedException e) {
			AlphadocConfig.LOGGER.severe("" + e);
		}
		final AlphadocConfig config = new AlphadocConfig();
		// config.setLocalParticipant(this.getLocalParticipant());
		config.setLocalNodeID(this.localNodeID);
		// config.getMyCurrentUser().setName(this.myCurrentUser.getName());
		config.setMyEpisodeId(this.myEpisodeId);
		config.setMyCurrentlyActiveCardId(this.myCurrentlyActiveCardId);
		// config.getMyCurrentUser().setIp(myCurrentUser.getIp());
		// config.getMyCurrentUser().setPort(myCurrentUser.getPort());
		return config;
	}

	/**
	 * Gets the own kp path.
	 * 
	 * @return the own kp path
	 */
	public String getOwnKPPath() {
		return this.ownKPPath;
	}

	/**
	 * Sets the own kp path.
	 * 
	 * @param ownKPPath
	 *            the new own kp path
	 */
	public void setOwnKPPath(final String ownKPPath) {
		this.ownKPPath = ownKPPath;
	}

	/**
	 * Gets the own kp pass.
	 * 
	 * @return the own kp pass
	 */
	public String getOwnKPPass() {
		return this.ownKPPass;
	}

	/**
	 * Sets the own kp pass.
	 * 
	 * @param ownKPPass
	 *            the new own kp pass
	 */
	public void setOwnKPPass(final String ownKPPass) {
		this.ownKPPass = ownKPPass;
	}

	/**
	 * Gets the pk path.
	 * 
	 * @return the pk path
	 */
	public String getPkPath() {
		return this.pkPath;
	}

	/**
	 * Sets the pk path.
	 * 
	 * @param pkpath
	 *            the new pk path
	 */
	public void setPkPath(final String pkpath) {
		this.pkPath = pkpath;
	}

	/**
	 * Gets the next imap uid.
	 * 
	 * @return the next imap uid
	 */
	public long getNextImapUid() {
		return this.nextImapUid;
	}

	/**
	 * Sets the next imap uid.
	 * 
	 * @param nextImapUid
	 *            the new next imap uid
	 */
	public void setNextImapUid(final long nextImapUid) {
		this.nextImapUid = nextImapUid;
	}

	/**
	 * Gets the imap uid validity.
	 * 
	 * @return the imap uid validity
	 */
	public long getImapUidValidity() {
		return this.imapUidValidity;
	}

	/**
	 * Sets the imap uid validity.
	 * 
	 * @param imapUidValidity
	 *            the new imap uid validity
	 */
	public void setImapUidValidity(final long imapUidValidity) {
		this.imapUidValidity = imapUidValidity;
	}

	/**
	 * Gets the mail address.
	 * 
	 * @return the mail address
	 */
	public String getMailAddress() {
		return this.mailAddress;
	}

	/**
	 * Sets the mail address.
	 * 
	 * @param mailAddress
	 *            the new mail address
	 */
	public void setMailAddress(final String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Gets the smtp host.
	 * 
	 * @return the smtp host
	 */
	public String getSmtpHost() {
		return this.smtpHost;
	}

	/**
	 * Sets the smtp host.
	 * 
	 * @param smtpHost
	 *            the new smtp host
	 */
	public void setSmtpHost(final String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * Gets the smtp port.
	 * 
	 * @return the smtp port
	 */
	public String getSmtpPort() {
		return this.smtpPort;
	}

	/**
	 * Sets the smtp port.
	 * 
	 * @param smtpPort
	 *            the new smtp port
	 */
	public void setSmtpPort(final String smtpPort) {
		this.smtpPort = smtpPort;
	}

	/**
	 * Gets the imap mode.
	 * 
	 * @return the imap mode
	 */
	public String getImapMode() {
		return this.imapMode;
	}

	/**
	 * Sets the imap mode.
	 * 
	 * @param imapMode
	 *            the new imap mode
	 */
	public void setImapMode(final String imapMode) {
		this.imapMode = imapMode;
	}

	/**
	 * Gets the imap host.
	 * 
	 * @return the imap host
	 */
	public String getImapHost() {
		return this.imapHost;
	}

	/**
	 * Sets the imap host.
	 * 
	 * @param imapHost
	 *            the new imap host
	 */
	public void setImapHost(final String imapHost) {
		this.imapHost = imapHost;
	}

	/**
	 * Checks if is use specific mailbox.
	 * 
	 * @return true, if is use specific mailbox
	 */
	public boolean isUseSpecificMailbox() {
		return this.useSpecificMailbox;
	}

	/**
	 * Sets the use specific mailbox.
	 * 
	 * @param useSpecificMailbox
	 *            the new use specific mailbox
	 */
	public void setUseSpecificMailbox(final boolean useSpecificMailbox) {
		this.useSpecificMailbox = useSpecificMailbox;
	}

	/**
	 * Gets the next monitor imap uid.
	 * 
	 * @return the next monitor imap uid
	 */
	public long getNextMonitorImapUid() {
		return this.nextMonitorImapUid;
	}

	/**
	 * Sets the next monitor imap uid.
	 * 
	 * @param nextMonitorImapUid
	 *            the new next monitor imap uid
	 */
	public void setNextMonitorImapUid(final long nextMonitorImapUid) {
		this.nextMonitorImapUid = nextMonitorImapUid;
	}

	/**
	 * Gets the monitor imap uid validity.
	 * 
	 * @return the monitor imap uid validity
	 */
	public long getMonitorImapUidValidity() {
		return this.monitorImapUidValidity;
	}

	/**
	 * Sets the monitor imap uid validity.
	 * 
	 * @param monitorImapUidValidity
	 *            the new monitor imap uid validity
	 */
	public void setMonitorImapUidValidity(final long monitorImapUidValidity) {
		this.monitorImapUidValidity = monitorImapUidValidity;
	}

	/** The imap uid validity. */
	@XmlElement(required = false)
	protected long imapUidValidity;
}
