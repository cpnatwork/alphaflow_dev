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
package alpha.facades;

import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.identification.AlphaCardID;

/**
 * This interface defines the methods, that can be called from outside the
 * alphavvs module.
 * 
 * 
 */
public interface VerVarStoreFacade {

	/**
	 * This method stores the Payload object to the path created by homePath,
	 * aci and the value of version from the constructor.
	 * 
	 * @param aci
	 *            the aci
	 * @param payload
	 *            the payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public void store(AlphaCardID aci, Payload payload)
			throws VerVarStoreException;

	/**
	 * This method stores the Payload object to the path created by homepath,
	 * aci and version.
	 * 
	 * @param aci
	 *            the aci
	 * @param payload
	 *            the payload
	 * @param version
	 *            the version
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public void store(AlphaCardID aci, Payload payload, String version)
			throws VerVarStoreException;

	/**
	 * This method stores the alpha card descriptor to the right directory in
	 * the file system.
	 * 
	 * @param acd
	 *            the alpha card descriptor
	 */
	public void store(AlphaCardDescriptor acd);

	/**
	 * This method loads a Payload specified by aci.
	 * 
	 * @param aci
	 *            the aci
	 * @return the payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public Payload load(AlphaCardID aci) throws VerVarStoreException;

	/**
	 * This method loads a Payload specified by aci, version.
	 * 
	 * @param aci
	 *            the aci
	 * @param version
	 *            the version
	 * @return the payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public Payload load(AlphaCardID aci, String version)
			throws VerVarStoreException;

	/**
	 * This method gets a Payload instance specified by the aci from the
	 * internal buffer.
	 * 
	 * @param aci
	 *            the aci
	 * @return the payload
	 */
	public Payload getPayload(AlphaCardID aci);

	/**
	 * This method puts a Payload instance into the internal buffer.
	 * 
	 * @param aci
	 *            the aci
	 * @param payload
	 *            the payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	public void putPayload(AlphaCardID aci, Payload payload)
			throws VerVarStoreException;

	/**
	 * Setter for sptype.
	 * <p>
	 * FIXME: Grotesk! Das darf nicht noetig sein (ist a-Card abhaengig).
	 * 
	 * @param sptype
	 *            the sptype to set
	 */
	@Deprecated
	public void setSptype(String sptype);

	/**
	 * Setter for version.
	 * <p>
	 * FIXME: Grotesk! Das darf nicht noetig sein (ist a-Card abhaengig).
	 * 
	 * @param version
	 *            the version to set
	 */
	@Deprecated
	public void setVersion(String version);

}
