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

import java.util.ArrayList;
import java.util.List;

import alpha.forms.memento.model.XMLSerializeableMemento;
import alpha.forms.util.xml.XMLDocumentSection;

/**
 * The Class EventMemento.
 */
public class EventMemento implements XMLSerializeableMemento {

	/** The actions. */
	private final List<ActionMemento> actions = new ArrayList<ActionMemento>();

	/** The event name. */
	private String eventName;

	/**
	 * Gets the event name.
	 * 
	 * @return the event name
	 */
	public String getEventName() {
		return this.eventName;
	}

	/**
	 * Sets the event name.
	 * 
	 * @param eventName
	 *            the new event name
	 */
	public void setEventName(final String eventName) {
		this.eventName = eventName;
	}

	/**
	 * Gets the actions.
	 * 
	 * @return the actions
	 */
	public List<ActionMemento> getActions() {
		return this.actions;
	}

	/**
	 * Adds the action.
	 * 
	 * @param m
	 *            the m
	 */
	public void addAction(final ActionMemento m) {
		this.actions.add(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.XMLSerializeableMemento#getXML()
	 */
	@Override
	public String getXML() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<event name=\"").append(this.eventName).append("\"");
		if (this.actions.size() > 0) {
			sb.append(">");
			for (final ActionMemento m : this.actions) {
				sb.append(m.getXML());
			}
			sb.append("</event>");
		} else {
			sb.append(" />\n");
		}
		return sb.toString();
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
		this.eventName = xml.getAttribute("name");
		final List<XMLDocumentSection> xmlActionList = xml
				.getDocumentSections("action");
		for (final XMLDocumentSection xmlAction : xmlActionList) {
			final ActionMemento m = new ActionMemento();
			m.loadXML(xmlAction);
			this.actions.add(m);
		}
	}

}
