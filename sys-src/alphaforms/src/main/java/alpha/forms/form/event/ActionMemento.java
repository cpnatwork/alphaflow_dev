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
package alpha.forms.form.event;

import java.util.HashMap;
import java.util.Map;

import alpha.forms.memento.model.XMLSerializeableMemento;
import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;

/**
 * The Class ActionMemento.
 */
public class ActionMemento implements XMLSerializeableMemento {

	/** The name. */
	private String name;

	/** The script code. */
	private String scriptCode;

	/** The attributes. */
	private final Map<String, Object> attributes = new HashMap<String, Object>();

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

	/**
	 * Gets the script code.
	 * 
	 * @return the script code
	 */
	public String getScriptCode() {
		return this.scriptCode;
	}

	/**
	 * Sets the script code.
	 * 
	 * @param scriptCode
	 *            the new script code
	 */
	public void setScriptCode(final String scriptCode) {
		this.scriptCode = scriptCode;
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
	 * Adds the attribute.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void addAttribute(final String key, final Object value) {
		this.attributes.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.XMLSerializeableMemento#getXML()
	 */
	@Override
	public String getXML() {
		return new XMLFragment(this.scriptCode).wrapInCDATA().wrapIn("action")
				.withAttribute("name", this.name)
				.withAttributes(this.attributes).toString();
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
		this.name = xml.getAttribute("name");
		this.scriptCode = xml.getCDATAContent();
	}

}
