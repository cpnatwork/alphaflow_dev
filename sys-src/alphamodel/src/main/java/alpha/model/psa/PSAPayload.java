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
package alpha.model.psa;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.Payload;
import alpha.model.identification.AlphaCardID;

/**
 * The Class PSAPayload.
 *
 * This class is the data object representing the PSA-Payload document
 *
 * @author cpn
 * @version $Id: $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "listOfTodoItems", "listOfTodoRelationships" })
@XmlRootElement(name = "psaPayload")
public class PSAPayload extends Payload {

	/**
	 * The Lo todo items.
	 * 
	 * @aggregation
	 */
	@XmlElement(name = "TodoItem", required = true)
	@XmlElementWrapper(name = "listOfTodoItems")
	protected Set<AlphaCardID> listOfTodoItems;

	/**
	 * The Lo todo relationships.
	 * 
	 * @composition
	 */
	@XmlElement(name = "TodoRelationships", required = true)
	@XmlElementWrapper(name = "listOfTodoRelationships")
	protected Set<AlphaCardRelationship> listOfTodoRelationships;

	/**
	 * Instantiates a new pSA payload.
	 */
	public PSAPayload() {
	}

	/**
	 * Gets the lo todo items.
	 *
	 * @return the loTodoItems
	 */
	public Set<AlphaCardID> getListOfTodoItems() {
		if (this.listOfTodoItems == null) {
			this.listOfTodoItems = new LinkedHashSet<AlphaCardID>();
		}
		return this.listOfTodoItems;
	}

	/**
	 * Sets the lo todo items.
	 *
	 * @param loTodoItems
	 *            the loTodoItems to set
	 */
	public void setListOfTodoItems(final Set<AlphaCardID> loTodoItems) {
		this.listOfTodoItems = loTodoItems;
	}

	/**
	 * Gets the lo todo relationships.
	 *
	 * @return the loTodoRelationships
	 */
	public Set<AlphaCardRelationship> getListOfTodoRelationships() {
		if (this.listOfTodoRelationships == null) {
			this.listOfTodoRelationships = new LinkedHashSet<AlphaCardRelationship>();
		}
		return this.listOfTodoRelationships;
	}

	/**
	 * Sets the lo todo relationships.
	 *
	 * @param loTodoRelationships
	 *            the loTodoRelationships to set
	 */
	public void setListOfTodoRelationships(
			final Set<AlphaCardRelationship> loTodoRelationships) {
		this.listOfTodoRelationships = loTodoRelationships;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "PSAPayload [listOfTodoItems=" + this.listOfTodoItems
				+ ", listOfTodoRelationships=" + this.listOfTodoRelationships
				+ "]";
	}

}
