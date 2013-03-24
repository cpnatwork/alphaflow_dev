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

import java.util.List;

import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.Group;

/**
 * The Class GroupValueMemento.
 */
public class GroupValueMemento extends ContainerValueMemento implements
		XMLSerializeableMemento {

	/**
	 * Instantiates a new group value memento.
	 * 
	 * @param parent
	 *            the parent
	 */
	public GroupValueMemento(final Group parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.ValueMemento#getXML()
	 */
	@Override
	public String getXML() {
		final List<FormWidget> childList = (List<FormWidget>) this.value;
		final StringBuilder sb = new StringBuilder();
		for (final FormWidget w : childList) {
			final ValueMemento m = ((MementoOriginator) w).createValueMemento();
			sb.append(m.getXML());
		}
		return new XMLFragment(
				new XMLFragment(sb.toString()).wrapIn("children"))
				.wrapIn("vmemento").withAttribute("for", this.name).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.ValueMemento#loadXML(alpha.forms.util.xml.
	 * XMLDocumentSection)
	 */
	@Override
	public void loadXML(final XMLDocumentSection xml) {
		if (this.parent == null)
			return;
		final List<XMLDocumentSection> valueSectionList = xml
				.getDocumentSections("children/vmemento");
		for (final XMLDocumentSection widgetSection : valueSectionList) {
			final String widgetName = widgetSection.getAttribute("for");
			final FormWidget w = this.parent.getWidgetByName(widgetName);

			final ValueMemento m = ((MementoOriginator) w).createValueMemento();
			m.loadXML(widgetSection);

			((MementoOriginator) w).setValueMemento(m);
		}
	}

}
