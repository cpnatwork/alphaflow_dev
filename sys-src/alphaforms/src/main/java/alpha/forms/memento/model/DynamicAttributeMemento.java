/**************************************************************************
 * alpha-Forms: self-editable formulars in form of an active document
 * (originally for the alpha-Flow project)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Florian Wagner
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
package alpha.forms.memento.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;

/**
 * The Class DynamicAttributeMemento.
 */
public class DynamicAttributeMemento implements XMLSerializeableMemento {

	/** The name. */
	protected String name;

	/** The attributes. */
	protected Map<String, Object> attributes = new HashMap<String, Object>();

	/**
	 * Adds the attribute.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public void addAttribute(final String name, final Object value) {
		this.attributes.put(name, value);
	}

	/**
	 * Gets the attribute.
	 * 
	 * @param name
	 *            the name
	 * @return the attribute
	 */
	public Object getAttribute(final String name) {
		return this.attributes.get(name);
	}

	/**
	 * Checks if is stored.
	 * 
	 * @param name
	 *            the name
	 * @return true, if is stored
	 */
	public boolean isStored(final String name) {
		return (this.attributes.get(name) != null);
	}

	/**
	 * Gets the attributes.
	 * 
	 * @return the attributes
	 */
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.XMLSerializeableMemento#getXML()
	 */
	@Override
	public String getXML() {
		String out = "";
		for (final Entry<String, Object> e : this.attributes.entrySet()) {
			out += new XMLFragment(e.getValue()).wrapIn("attribute")
					.withAttribute("name", e.getKey());

		}
		return new XMLFragment(out).wrapIn("smemento")
				.withAttribute("for", this.name).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.XMLSerializeableMemento#loadXML(alpha.forms
	 * .util.xml.XMLDocumentSection)
	 */
	@Override
	public void loadXML(final XMLDocumentSection xml) {
		// TODO Auto-generated method stub

	}

}
