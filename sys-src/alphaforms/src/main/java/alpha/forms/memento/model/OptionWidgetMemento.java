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
import alpha.forms.widget.model.Option.OptionItem;

/**
 * The Class OptionWidgetMemento.
 */
public class OptionWidgetMemento extends WidgetMemento {

	/**
	 * Instantiates a new option widget memento.
	 */
	public OptionWidgetMemento() {
		super();
	}

	/**
	 * Instantiates a new option widget memento.
	 * 
	 * @param m
	 *            the m
	 */
	public OptionWidgetMemento(final WidgetMemento m) {
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
		for (final OptionItem item : (List<OptionItem>) this.value) {
			out += new XMLFragment(item.getName()).wrapIn("option")
					.withAttribute("value", item.isValue()).toString();
		}
		return new XMLFragment(out).wrapIn("options").toString();
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
			final List<OptionItem> optionList = new ArrayList<OptionItem>();
			final List<XMLDocumentSection> xmlOptionList = xml
					.getDocumentSections("options/option");
			for (final XMLDocumentSection xmlOption : xmlOptionList) {
				final OptionItem option = new OptionItem();
				option.setName(xmlOption.getTextContent());
				option.setValue(Boolean.parseBoolean(xmlOption
						.getAttribute("value")));
				optionList.add(option);
			}
			this.value = optionList;
		}
	}

}
