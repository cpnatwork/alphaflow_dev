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
package alpha.forms.widget.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alpha.forms.form.event.Event;
import alpha.forms.form.event.EventFactory;
import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.memento.model.DynamicAttributeMemento;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.OptionValueMemento;
import alpha.forms.memento.model.OptionWidgetMemento;
import alpha.forms.memento.model.ValueMemento;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.util.OptionLayout;
import alpha.forms.widget.view.OptionUI;

/**
 * The Class Option.
 */
public class Option extends FormWidget implements MementoOriginator {

	/** The options. */
	@WidgetProperty()
	private List<OptionItem> options = new ArrayList<OptionItem>();

	/** The is multiselect. */
	@WidgetProperty()
	private boolean isMultiselect = false;

	/** The layout. */
	@WidgetProperty()
	private OptionLayout layout = OptionLayout.HORIZONTAL;

	/** The gap. */
	@WidgetProperty()
	private int gap = 5;

	/** The on selection changed. */
	@WidgetProperty()
	private Event onSelectionChanged = EventFactory.getInstance()
			.createDefaultEvent(this);

	/**
	 * Instantiates a new option.
	 * 
	 * @param name
	 *            the name
	 */
	public Option(final String name) {
		super(name);
		this.width = 200;
		this.height = 22;
		this.options.add(new OptionItem("Option 1", false));
		this.options.add(new OptionItem("Option 2", false));
		this.ui = new OptionUI(this);
	}

	/**
	 * Gets the options.
	 * 
	 * @return the options
	 */
	public List<OptionItem> getOptions() {
		return this.options;
	}

	/**
	 * Sets the options.
	 * 
	 * @param options
	 *            the new options
	 */
	public void setOptions(final List<OptionItem> options) {
		this.options = options;
	}

	/**
	 * Find option by name.
	 * 
	 * @param name
	 *            the name
	 * @return the option item
	 */
	public OptionItem findOptionByName(final String name) {
		for (final OptionItem option : this.options) {
			if (option.getName().equals(name))
				return option;
		}
		return null;
	}

	/**
	 * Checks if is multiselect.
	 * 
	 * @return true, if is multiselect
	 */
	public boolean isMultiselect() {
		return this.isMultiselect;
	}

	/**
	 * Sets the multiselect.
	 * 
	 * @param isMultiselect
	 *            the new multiselect
	 */
	public void setMultiselect(final boolean isMultiselect) {
		this.isMultiselect = isMultiselect;
	}

	/**
	 * Gets the on selection changed.
	 * 
	 * @return the on selection changed
	 */
	public Event getOnSelectionChanged() {
		return this.onSelectionChanged;
	}

	/**
	 * Sets the on selection changed.
	 * 
	 * @param onSelectionChanged
	 *            the new on selection changed
	 */
	public void setOnSelectionChanged(final Event onSelectionChanged) {
		this.onSelectionChanged = onSelectionChanged;
	}

	/**
	 * Gets the layout.
	 * 
	 * @return the layout
	 */
	public OptionLayout getLayout() {
		return this.layout;
	}

	/**
	 * Sets the layout.
	 * 
	 * @param layout
	 *            the new layout
	 */
	public void setLayout(final OptionLayout layout) {
		this.layout = layout;
	}

	/**
	 * Gets the gap.
	 * 
	 * @return the gap
	 */
	public int getGap() {
		return this.gap;
	}

	/**
	 * Sets the gap.
	 * 
	 * @param gap
	 *            the new gap
	 */
	public void setGap(final int gap) {
		this.gap = gap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMinimumHeight()
	 */
	@Override
	public int getMinimumHeight() {
		return this.ui.getMinimumSize().height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMiniumWidth()
	 */
	@Override
	public int getMiniumWidth() {
		return 200;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getDescription()
	 */
	@Override
	public String getDescription() {
		return "A option group.";
	}

	/**
	 * The Class OptionItem.
	 */
	public static class OptionItem {

		/** The name. */
		private String name = "";

		/** The value. */
		private boolean value = false;

		/**
		 * Instantiates a new option item.
		 */
		public OptionItem() {
			super();
		}

		/**
		 * Instantiates a new option item.
		 * 
		 * @param name
		 *            the name
		 */
		public OptionItem(final String name) {
			super();
			this.name = name;
		}

		/**
		 * Instantiates a new option item.
		 * 
		 * @param name
		 *            the name
		 * @param value
		 *            the value
		 */
		public OptionItem(final String name, final boolean value) {
			super();
			this.name = name;
			this.value = value;
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

		/**
		 * Checks if is value.
		 * 
		 * @return the boolean
		 */
		public Boolean isValue() {
			return this.value;
		}

		/**
		 * Sets the value.
		 * 
		 * @param value
		 *            the new value
		 */
		public void setValue(final Boolean value) {
			this.value = value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.name + ": " + this.value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		protected OptionItem clone() {
			final OptionItem oi = new OptionItem();
			oi.name = this.name;
			oi.value = this.value;
			return oi;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createWidgetMemento()
	 */
	@Override
	public WidgetMemento createWidgetMemento() {
		final WidgetMemento m = new OptionWidgetMemento();
		m.setName(this.name);
		m.setType(this.getClass());
		final List<OptionItem> copy = new ArrayList<OptionItem>();
		for (final OptionItem o : this.options) {
			copy.add(o.clone());
		}
		m.setValue(copy);
		m.addAttribute("isMultiselect", this.isMultiselect);
		m.addAttribute("layout", this.layout);
		m.addAttribute("gap", this.gap);
		m.addAttribute("x", this.x);
		m.addAttribute("y", this.y);
		m.addAttribute("width", this.width);
		m.addAttribute("height", this.height);
		m.addAttribute("ui", this.ui.getClass().getName());
		m.setValidators(this.validators.createMemento());
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setWidgetMemento(alpha.forms
	 * .memento.model.WidgetMemento)
	 */
	@Override
	public void setWidgetMemento(final WidgetMemento m) {
		if (m != null) {
			this.name = m.getName();
			final Map<String, Object> attributes = m.getAttributes();
			this.gap = Integer.parseInt(attributes.get("gap").toString());
			this.isMultiselect = Boolean.parseBoolean(attributes.get(
					"isMultiselect").toString());
			this.layout = OptionLayout.valueOf(attributes.get("layout")
					.toString());
			this.x = Integer.parseInt(attributes.get("x").toString());
			this.y = Integer.parseInt(attributes.get("y").toString());
			this.width = Integer.parseInt(attributes.get("width").toString());
			this.height = Integer.parseInt(attributes.get("height").toString());
			this.setSize(this.width, this.height);
			this.setX(this.x);
			this.setY(this.y);
			this.options = (List<OptionItem>) m.getValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#createDynamicAttributeMemento
	 * (alpha.forms.memento.model.WidgetMemento)
	 */
	@Override
	public DynamicAttributeMemento createDynamicAttributeMemento(
			final WidgetMemento ref) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setDynamicMemento(alpha.forms
	 * .memento.model.DynamicAttributeMemento)
	 */
	@Override
	public void setDynamicMemento(final DynamicAttributeMemento m) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createValueMemento()
	 */
	@Override
	public ValueMemento createValueMemento() {
		final OptionValueMemento m = new OptionValueMemento();
		m.setName(this.name);
		final List<OptionItem> copy = new ArrayList<OptionItem>();
		for (final OptionItem o : this.options) {
			copy.add(o.clone());
		}
		m.setValue(copy);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setValueMemento(alpha.forms
	 * .memento.model.ValueMemento)
	 */
	@Override
	public void setValueMemento(final ValueMemento m) {
		this.options = (List<OptionItem>) m.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#validate()
	 */
	@Override
	public Set<ValidationFailure> validate() {
		return this.validateValue(this.options);
	}

}
