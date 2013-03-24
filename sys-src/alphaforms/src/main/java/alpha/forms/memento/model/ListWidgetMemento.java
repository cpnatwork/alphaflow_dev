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

import java.util.ArrayList;
import java.util.List;

import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;
import alpha.forms.widget.model.AlphaList.ListItem;

/**
 * The Class ListWidgetMemento.
 */
public class ListWidgetMemento extends WidgetMemento {

	/**
	 * Instantiates a new list widget memento.
	 */
	public ListWidgetMemento() {
		super();
	}

	/**
	 * Instantiates a new list widget memento.
	 * 
	 * @param m
	 *            the m
	 */
	public ListWidgetMemento(final WidgetMemento m) {
		this.name = m.name;
		this.type = m.type;
		this.value = m.value;
		this.attributes = m.attributes;
		this.events = m.events;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.WidgetMemento#renderValue()
	 */
	@Override
	protected String renderValue() {
		String out = "";
		for (final ListItem item : (List<ListItem>) this.value) {
			out += new XMLFragment(item.getLabel()).wrapIn("item")
					.withAttribute("selected", item.isSelected())
					.withAttribute("id", item.getId()).toString();
		}
		return new XMLFragment(out).wrapIn("items").toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.WidgetMemento#loadValue(alpha.forms.util.xml
	 * .XMLDocumentSection)
	 */
	@Override
	protected void loadValue(final XMLDocumentSection xml) {
		if (xml != null) {
			final List<ListItem> itemList = new ArrayList<ListItem>();
			final List<XMLDocumentSection> xmlItemList = xml
					.getDocumentSections("items/item");
			for (final XMLDocumentSection xmlItem : xmlItemList) {
				final ListItem item = new ListItem();
				item.setLabel(xmlItem.getTextContent());
				item.setSelected(Boolean.parseBoolean(xmlItem
						.getAttribute("selected")));
				item.setId(xmlItem.getAttribute("id"));
				itemList.add(item);
			}
			this.value = itemList;
		}
	}

}
