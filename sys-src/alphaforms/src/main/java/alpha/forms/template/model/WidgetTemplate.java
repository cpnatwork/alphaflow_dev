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
package alpha.forms.template.model;

import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.util.WidgetFactory;

/**
 * The Class WidgetTemplate.
 */
public class WidgetTemplate {

	/** The template name. */
	protected String templateName;

	/** The widget prototype memento. */
	protected WidgetMemento widgetPrototypeMemento;

	/**
	 * Creates the from xml.
	 * 
	 * @param xml
	 *            the xml
	 * @return the widget template
	 */
	public static WidgetTemplate createFromXML(final XMLDocumentSection xml) {
		final String name = xml.getAttribute("name");
		final XMLDocumentSection widgetSection = xml.getSingleSection("widget");
		final String widgetName = widgetSection.getAttribute("name");
		final String widgetClass = widgetSection.getAttribute("type");
		final FormWidget w = WidgetFactory
				.createWidget(widgetClass, widgetName);

		final WidgetMemento widgetPrototypeMemento = ((MementoOriginator) w)
				.createWidgetMemento();
		widgetPrototypeMemento.loadXML(widgetSection);
		((MementoOriginator) w).setWidgetMemento(widgetPrototypeMemento);
		return new WidgetTemplate(w, name);
	}

	/**
	 * Instantiates a new widget template.
	 * 
	 * @param w
	 *            the w
	 * @param templateName
	 *            the template name
	 */
	public WidgetTemplate(final FormWidget w, final String templateName) {
		this.templateName = templateName;
		this.widgetPrototypeMemento = ((MementoOriginator) w)
				.createWidgetMemento();
	}

	/**
	 * Creates the widget from template.
	 * 
	 * @return the form widget
	 */
	public FormWidget createWidgetFromTemplate() {
		return this.createWidgetFromTemplate("temp");
	}

	/**
	 * Creates the widget from template.
	 * 
	 * @param name
	 *            the name
	 * @return the form widget
	 */
	public FormWidget createWidgetFromTemplate(final String name) {
		final FormWidget w = WidgetFactory.createWidget(
				this.widgetPrototypeMemento.getType().getName(), name);
		((MementoOriginator) w).setWidgetMemento(this.widgetPrototypeMemento);
		return w;
	}

	/**
	 * Gets the template name.
	 * 
	 * @return the template name
	 */
	public String getTemplateName() {
		return this.templateName;
	}

	/**
	 * Sets the template name.
	 * 
	 * @param templateName
	 *            the new template name
	 */
	public void setTemplateName(final String templateName) {
		this.templateName = templateName;
	}

	/**
	 * Load from xml.
	 * 
	 * @param xml
	 *            the xml
	 */
	public void loadFromXML(final XMLDocumentSection xml) {
		this.templateName = xml.getAttribute("name");
		final XMLDocumentSection widgetSection = xml.getSingleSection("widget");
		final String widgetName = widgetSection.getAttribute("name");
		final String widgetClass = widgetSection.getAttribute("type");
		final FormWidget w = WidgetFactory
				.createWidget(widgetClass, widgetName);

		final WidgetMemento widgetPrototypeMemento = ((MementoOriginator) w)
				.createWidgetMemento();
		widgetPrototypeMemento.loadXML(widgetSection);

	}

	/**
	 * Save as xml.
	 * 
	 * @return the string
	 */
	public String saveAsXML() {
		return new XMLFragment(this.widgetPrototypeMemento.getXML())
				.wrapIn("widgetTemplate")
				.withAttribute("name", this.templateName).toString();
	}

}
