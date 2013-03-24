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
 * $Id: MailAuthenticator.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.offsync.security;

import javax.mail.Authenticator;

/**
 * Encapsulates all necessary communication credentials.
 */
public class MailAuthenticator extends Authenticator {

	/** The username. */
	String username;

	/** The password. */
	String password;

	/** The smtp host. */
	String smtpHost;

	/** The smtp socket factory port. */
	String smtpSocketFactoryPort;

	/** The smtp socket factory class. */
	String smtpSocketFactoryClass;

	/** The smtp auth. */
	String smtpAuth;

	/** The smtp port. */
	String smtpPort;

	/** The imap mode. */
	String imapMode;

	/** The imap port. */
	String imapPort;

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
	 * Gets the smtp socket factory port.
	 * 
	 * @return the smtp socket factory port
	 */
	public String getSmtpSocketFactoryPort() {
		return this.smtpSocketFactoryPort;
	}

	/**
	 * Sets the smtp socket factory port.
	 * 
	 * @param smtpSocketFactoryPort
	 *            the new smtp socket factory port
	 */
	public void setSmtpSocketFactoryPort(final String smtpSocketFactoryPort) {
		this.smtpSocketFactoryPort = smtpSocketFactoryPort;
	}

	/**
	 * Gets the smtp socket factory class.
	 * 
	 * @return the smtp socket factory class
	 */
	public String getSmtpSocketFactoryClass() {
		return this.smtpSocketFactoryClass;
	}

	/**
	 * Sets the smtp socket factory class.
	 * 
	 * @param smtpSocketFactoryClass
	 *            the new smtp socket factory class
	 */
	public void setSmtpSocketFactoryClass(final String smtpSocketFactoryClass) {
		this.smtpSocketFactoryClass = smtpSocketFactoryClass;
	}

	/**
	 * Gets the smtp auth.
	 * 
	 * @return the smtp auth
	 */
	public String getSmtpAuth() {
		return this.smtpAuth;
	}

	/**
	 * Sets the smtp auth.
	 * 
	 * @param smtpAuth
	 *            the new smtp auth
	 */
	public void setSmtpAuth(final String smtpAuth) {
		this.smtpAuth = smtpAuth;
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

	/** The imap host. */
	String imapHost;

	/** The mail address. */
	String mailAddress;

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
	 * Instantiates a new {@link MailAuthenticator}.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param smtpHost
	 *            the smtp host
	 * @param smtpSocketFactoryPort
	 *            the smtp socket factory port
	 * @param smtpSocketFactoryClass
	 *            the smtp socket factory class
	 * @param smtpAuth
	 *            the smtp auth
	 * @param smtpPort
	 *            the smtp port
	 * @param imapMode
	 *            the imap mode
	 * @param imapHost
	 *            the imap host
	 * @param mailAddress
	 *            the mail address
	 * @param imapPort
	 *            the imap port
	 */
	public MailAuthenticator(final String username, final String password,
			final String smtpHost, final String smtpSocketFactoryPort,
			final String smtpSocketFactoryClass, final String smtpAuth,
			final String smtpPort, final String imapMode,
			final String imapHost, final String mailAddress,
			final String imapPort) {

		this.username = username;
		this.password = password;
		this.smtpHost = smtpHost;
		this.smtpSocketFactoryPort = smtpSocketFactoryPort;
		this.smtpSocketFactoryClass = smtpSocketFactoryClass;
		this.smtpAuth = smtpAuth;
		this.smtpPort = smtpPort;
		this.imapMode = imapMode;
		this.imapHost = imapHost;
		this.mailAddress = mailAddress;
		this.imapPort = imapPort;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	@Override
	public javax.mail.PasswordAuthentication getPasswordAuthentication() {
		return new javax.mail.PasswordAuthentication(this.username,
				this.password);
	}

}
