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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import alpha.forms.template.WidgetTemplateManager;
import alpha.forms.template.exception.TemplateWithNameExistsException;
import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.util.WidgetFactory;

/**
 * The Class WidgetTemplateMngr.
 */
public class WidgetTemplateMngr extends DefaultListModel implements
		WidgetTemplateManager {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1262254853690049777L;

	/** The widgets. */
	private final Vector<WidgetTemplate> widgets = new Vector<WidgetTemplate>();

	/** The instance. */
	private static WidgetTemplateMngr instance = null;

	/**
	 * Gets the single instance of WidgetTemplateMngr.
	 * 
	 * @return single instance of WidgetTemplateMngr
	 */
	public static synchronized WidgetTemplateMngr getInstance() {
		if (WidgetTemplateMngr.instance == null) {
			WidgetTemplateMngr.instance = new WidgetTemplateMngr();
		}
		return WidgetTemplateMngr.instance;
	}

	/**
	 * Instantiates a new widget template mngr.
	 */
	private WidgetTemplateMngr() {
		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultListModel#getElementAt(int)
	 */
	@Override
	public Object getElementAt(final int index) {
		return this.widgets.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultListModel#getSize()
	 */
	@Override
	public int getSize() {
		return this.widgets.size();
	}

	/**
	 * Gets the.
	 * 
	 * @param name
	 *            the name
	 * @return the widget template
	 */
	public WidgetTemplate get(final String name) {
		for (final WidgetTemplate t : this.widgets) {
			if (t.getTemplateName().equals(name))
				return t;
		}
		return null;
	}

	/**
	 * Adds the template.
	 * 
	 * @param w
	 *            the w
	 */
	public void addTemplate(final WidgetTemplate w) {
		this.widgets.add(w);
		this.fireIntervalAdded(this, this.widgets.indexOf(w),
				this.widgets.indexOf(w));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.template.WidgetTemplateManager#saveAsTemplate(alpha.forms
	 * .widget.model.FormWidget, java.lang.String, boolean)
	 */
	@Override
	public void saveAsTemplate(final FormWidget widget,
			final String templateName, final boolean forceOverwrite)
			throws TemplateWithNameExistsException {
		final FormWidget clone = WidgetFactory.cloneWidget(widget);
		if (clone != null) {
			final WidgetTemplate wt = new WidgetTemplate(clone, templateName);
			wt.setTemplateName(templateName);
			if ((forceOverwrite == false) && (this.get(templateName) != null))
				throw new TemplateWithNameExistsException(
						"A template with the name " + templateName
								+ " already exists.");
			this.addTemplate(wt);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.template.WidgetTemplateManager#getWidgetFromTemplate(java
	 * .lang.String)
	 */
	@Override
	public FormWidget getWidgetFromTemplate(final String templateName) {
		final WidgetTemplate wt = this.get(templateName);
		return wt.createWidgetFromTemplate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.template.WidgetTemplateManager#storeTemplates(java.io.
	 * OutputStream)
	 */
	@Override
	public void storeTemplates(final OutputStream out) {
		final BufferedOutputStream bout = new BufferedOutputStream(out);
		String templateOut = "";
		for (final WidgetTemplate wt : this.widgets) {
			templateOut += wt.saveAsXML();
		}
		final String data = new XMLFragment(templateOut).wrapIn(
				"alphaFormTemplates").toString();
		try {
			bout.write(data.getBytes("UTF-8"));
			bout.close();
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.template.WidgetTemplateManager#loadTemplates(java.io.InputStream
	 * )
	 */
	@Override
	public void loadTemplates(final InputStream in) {
		try {
			final Document xmlDoc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(in);
			xmlDoc.getDocumentElement().normalize();

			final XMLDocumentSection templateContainer = new XMLDocumentSection(
					xmlDoc.getDocumentElement(), xmlDoc);
			for (final XMLDocumentSection sec : templateContainer
					.getDocumentSections("widgetTemplate")) {
				this.addTemplate(WidgetTemplate.createFromXML(sec));
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
