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
 * $Id: AlphaTemplate.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.template;

import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.AlphaCardDescriptor;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.APAPayload;
import alpha.model.apa.CorpusGenericus;
import alpha.model.cra.CRAPayload;
import alpha.model.psa.PSAPayload;

/**
 * The Class AlphaTemplate. Collects needed data structures and adds some
 * functionality
 * 
 * @author cpn
 * @version $Id: AlphaTemplate.java 3583 2012-02-16 01:52:45Z cpn $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "psaPayload", "apaPayload", "craPayload",
		"descriptorList" })
@XmlRootElement(name = "alphaTemplate")
public class AlphaTemplate {

	/** The psa payload. */
	@XmlElement(name = "psaPayload", required = true)
	private PSAPayload psaPayload = null;

	/** The apa payload. */
	@XmlElement(name = "apaPayload", required = true)
	private APAPayload apaPayload = null;

	/**
	 * Gets the list of alpha card descriptors.
	 * 
	 * @return the list of alpha card descriptors
	 */
	public List<AlphaCardDescriptor> getDescriptorList() {
		return this.descriptorList;
	}

	/**
	 * Sets the list of alpha card descriptors.
	 * 
	 * @param descriptorList
	 *            the new list of alpha card descriptors
	 */
	public void setDescriptorList(final List<AlphaCardDescriptor> descriptorList) {
		this.descriptorList = descriptorList;
	}

	/** The cra payload. */
	@XmlElement(name = "craPayload", required = true)
	private CRAPayload craPayload = null;

	/** The list of alpha card descriptors. */
	@XmlElementWrapper(name = "descriptorList")
	@XmlElement(name = "alphaCardDescriptor", required = true)
	private List<AlphaCardDescriptor> descriptorList = null;

	/**
	 * Gets the apa payload.
	 * 
	 * @return the apa payload
	 */
	public APAPayload getApaPayload() {
		return this.apaPayload;
	}

	/**
	 * Sets the apa payload.
	 * 
	 * @param apaPayload
	 *            the new apa payload
	 */
	public void setApaPayload(final APAPayload apaPayload) {
		this.apaPayload = apaPayload;
	}

	/**
	 * Gets the psa payload.
	 * 
	 * @return the psa payload
	 */
	public PSAPayload getPsaPayload() {
		return this.psaPayload;
	}

	/**
	 * Sets the psa payload.
	 * 
	 * @param psaPayload
	 *            the new psa payload
	 */
	public void setPsaPayload(final PSAPayload psaPayload) {
		this.psaPayload = psaPayload;
	}

	/**
	 * Sets the cra payload.
	 * 
	 * @param craPayload
	 *            the new cra payload
	 */
	public void setCraPayload(final CRAPayload craPayload) {
		this.craPayload = craPayload;
	}

	/**
	 * Gets the cra payload.
	 * 
	 * @return the cra payload
	 */
	public CRAPayload getCraPayload() {
		return this.craPayload;
	}

	/**
	 * this methods adds the missing adornments (which were not exported) to the
	 * acd + changes actor name to current user, if the actor is not present.
	 * 
	 * @param descriptor
	 *            the alpha card descriptor to be completed
	 * @param descriptor_complete
	 *            the acd_complete
	 * @param actorNames
	 *            the actor names
	 * @param currentUserName
	 *            the current user name
	 * @return the completed alpha card descriptor
	 */
	public AlphaCardDescriptor completeAdornments(
			final AlphaCardDescriptor descriptor,
			final AlphaCardDescriptor descriptor_complete,
			final Set<String> actorNames, final String currentUserName) {

		for (final PrototypedAdornment aa : descriptor.readAdornments()) {
			descriptor_complete.updateOrCreateAdornment(aa);
		}

		// if cards belong to an actor that is not present in the merged
		// Alpha-Doc, set current user as owner to be able to edit it
		final PrototypedAdornment pa = descriptor_complete
				.readAdornment(CorpusGenericus.ACTOR.value());
		if (!actorNames.contains(pa.getValue())) {
			pa.setValue("+" + currentUserName);
			descriptor_complete.updateOrCreateAdornment(pa);
		}

		descriptor_complete.setId(descriptor.getId());
		return descriptor_complete;
	}

}
