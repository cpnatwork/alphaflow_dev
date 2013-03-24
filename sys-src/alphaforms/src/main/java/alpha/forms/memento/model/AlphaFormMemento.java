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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.util.WidgetFactory;

/**
 * The Class AlphaFormMemento.
 */
public class AlphaFormMemento implements XMLSerializeableMemento {

	/** The title. */
	protected String title;

	/** The widgets. */
	protected List<FormWidget> widgets = new ArrayList<FormWidget>();

	/** The widget states. */
	protected Map<String, List<WidgetMemento>> widgetStates = new HashMap<String, List<WidgetMemento>>();

	/** The attributes. */
	protected Map<String, Object> attributes = new HashMap<String, Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.XMLSerializeableMemento#getXML()
	 */
	@Override
	public String getXML() {
		final List<WidgetMemento> widgetMementoList = new ArrayList<WidgetMemento>();
		for (final FormWidget w : this.widgets) {
			if (w instanceof MementoOriginator) {
				final MementoOriginator o = (MementoOriginator) w;
				widgetMementoList.add(o.createWidgetMemento());
			}
		}
		return this.compileXML(widgetMementoList, null, null);
	}

	/**
	 * Gets the xML.
	 * 
	 * @param state
	 *            the state
	 * @return the xML
	 */
	public String getXML(final String state) {
		final List<WidgetMemento> widgetMementoList = this.widgetStates
				.get(state);
		final List<DynamicAttributeMemento> deltaList = this
				.getAttributeDelta(state);
		final List<ValueMemento> valueList = new ArrayList<ValueMemento>();
		for (final FormWidget w : this.widgets) {
			final MementoOriginator o = (MementoOriginator) w;
			final ValueMemento vm = o.createValueMemento();
			if (vm != null) {
				valueList.add(vm);
			}
		}
		return this.compileXML(widgetMementoList, deltaList, valueList);
	}

	/**
	 * Compile xml.
	 * 
	 * @param widgetList
	 *            the widget list
	 * @param deltaList
	 *            the delta list
	 * @param valueList
	 *            the value list
	 * @return the string
	 */
	protected String compileXML(final List<WidgetMemento> widgetList,
			final List<DynamicAttributeMemento> deltaList,
			final List<ValueMemento> valueList) {
		final StringBuilder out = new StringBuilder();
		out.append(new XMLFragment(new XMLFragment(this.title).wrapIn("title"))
				.wrapIn("meta"));
		String pbox = "";
		for (final WidgetMemento wm : widgetList) {
			pbox += wm.getXML();
		}
		out.append(new XMLFragment(pbox).wrapIn("pbox"));

		if (deltaList != null) {
			String sbox = "";
			for (final DynamicAttributeMemento dam : deltaList) {
				sbox += dam.getXML();
			}
			out.append(new XMLFragment(sbox).wrapIn("sbox"));
		}

		if (valueList != null) {
			String vbox = "";
			for (final ValueMemento vm : valueList) {
				vbox += vm.getXML();
			}
			out.append(new XMLFragment(vbox).wrapIn("vbox"));
		}

		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
				+ new XMLFragment(out.toString()).wrapIn("alphaForm")
						.withAttributes(this.attributes);
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
		if (xml.getSectionName().equals("alphaForm")) {
			final XMLDocumentSection metaSection = xml
					.getSingleSection("meta[1]");
			if (metaSection != null) {
				this.title = metaSection.getNodeValue("title");
			}
			for (final Entry<String, String> e : xml.getAttributes().entrySet()) {
				this.attributes.put(e.getKey(), e.getValue());
			}
			final List<XMLDocumentSection> widgetSectionList = xml
					.getDocumentSections("pbox/widget");
			for (final XMLDocumentSection widgetSection : widgetSectionList) {
				final String widgetName = widgetSection.getAttribute("name");
				final String widgetClass = widgetSection.getAttribute("type");
				final FormWidget w = WidgetFactory.createWidget(widgetClass,
						widgetName);

				final WidgetMemento m = ((MementoOriginator) w)
						.createWidgetMemento();
				m.loadXML(widgetSection);

				((MementoOriginator) w).setWidgetMemento(m);
				this.widgets.add(w);
			}
			final XMLDocumentSection xmlSBox = xml.getSingleSection("sbox");
			if (xmlSBox != null) {
				final List<XMLDocumentSection> sboxList = xmlSBox
						.getDocumentSections("smemento");
				for (final XMLDocumentSection smem : sboxList) {
					final String wname = smem.getAttribute("for");
					final FormWidget w = this.getWidgetByName(wname);
					if (w != null) {
						final DynamicAttributeMemento m = new DynamicAttributeMemento();
						m.loadXML(smem);
						((MementoOriginator) w).setDynamicMemento(m);
					}
				}

			}
			final XMLDocumentSection xmlVBox = xml.getSingleSection("vbox");
			if (xmlVBox != null) {
				final List<XMLDocumentSection> vboxList = xmlVBox
						.getDocumentSections("vmemento");
				for (final XMLDocumentSection vmem : vboxList) {
					final String wname = vmem.getAttribute("for");
					final FormWidget w = this.getWidgetByName(wname);
					if (w != null) {
						final ValueMemento m = ((MementoOriginator) w)
								.createValueMemento();
						m.loadXML(vmem);
						((MementoOriginator) w).setValueMemento(m);
					}
				}

			}
		}
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Sets the attribute.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void setAttribute(final String key, final Object value) {
		this.attributes.put(key, value);
	}

	/**
	 * Gets the attribute.
	 * 
	 * @param key
	 *            the key
	 * @return the attribute
	 */
	public Object getAttribute(final String key) {
		return this.attributes.get(key);
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
	 * Gets the widgets.
	 * 
	 * @return the widgets
	 */
	public List<FormWidget> getWidgets() {
		return this.widgets;
	}

	/**
	 * Sets the widgets.
	 * 
	 * @param widgets
	 *            the new widgets
	 */
	public void setWidgets(final List<FormWidget> widgets) {
		this.widgets = widgets;
	}

	/**
	 * Gets the widget by name.
	 * 
	 * @param name
	 *            the name
	 * @return the widget by name
	 */
	protected FormWidget getWidgetByName(final String name) {
		for (final FormWidget w : this.widgets) {
			if (w.getName().equals(name))
				return w;
		}
		return null;
	}

	/**
	 * Gets the widget states.
	 * 
	 * @return the widget states
	 */
	public Map<String, List<WidgetMemento>> getWidgetStates() {
		return this.widgetStates;
	}

	/**
	 * Sets the widget states.
	 * 
	 * @param widgetStates
	 *            the widget states
	 */
	public void setWidgetStates(
			final Map<String, List<WidgetMemento>> widgetStates) {
		this.widgetStates = widgetStates;
	}

	/**
	 * Calculates the delta between the current AlphaForm widgets and previous
	 * saved state identified by the name parameter.
	 * 
	 * @param name
	 *            identifier for the state against which the delta is calculated
	 * @return list of DynamicAttributeMemento objects
	 */
	public List<DynamicAttributeMemento> getAttributeDelta(final String name) {
		final List<WidgetMemento> oldList = this.widgetStates.get(name);
		final List<DynamicAttributeMemento> deltaList = new ArrayList<DynamicAttributeMemento>();
		for (final FormWidget w : this.widgets) {
			final WidgetMemento mCur = ((MementoOriginator) w)
					.createWidgetMemento();
			for (final WidgetMemento mOld : oldList) {
				if (mCur.getName().equals(mOld.getName())) {
					final DynamicAttributeMemento delta = new DynamicAttributeMemento();
					delta.setName(mCur.getName());
					final Map<String, Object> mCurAttributes = mCur
							.getAttributes();
					for (final Entry<String, Object> eOld : mOld
							.getAttributes().entrySet()) {
						if (!eOld.getValue().equals(
								mCurAttributes.get(eOld.getKey()))) {
							delta.addAttribute(eOld.getKey(),
									mCurAttributes.get(eOld.getKey()));
						}
					}
					if (!delta.getAttributes().isEmpty()) {
						deltaList.add(delta);
					}
				}
			}
		}
		return deltaList;
	}

}
