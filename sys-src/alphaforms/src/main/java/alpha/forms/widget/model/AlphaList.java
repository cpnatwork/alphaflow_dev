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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alpha.forms.form.event.Event;
import alpha.forms.form.event.EventFactory;
import alpha.forms.form.event.EventMemento;
import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.memento.model.DynamicAttributeMemento;
import alpha.forms.memento.model.ListValueMemento;
import alpha.forms.memento.model.ListWidgetMemento;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.ValueMemento;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.util.WidgetLabelPosition;
import alpha.forms.widget.view.AlphaListUI;

/**
 * The Class AlphaList.
 */
public class AlphaList extends FormWidget implements MementoOriginator {

	/** The items. */
	@WidgetProperty(description = "List of items in the combo box")
	private List<ListItem> items = new ArrayList<ListItem>();

	/** The is editable. */
	@WidgetProperty(description = "If isEditable equals true, new items can be added to the list by the user.")
	private boolean isEditable = false;

	/** The label. */
	@WidgetProperty(description = "The label text.")
	protected String label;

	/** The show label. */
	@WidgetProperty(name = "labelOrientation", description = "Determines the position of the label in relation to the text input field.")
	protected WidgetLabelPosition showLabel = WidgetLabelPosition.LEFT;

	/** The is multiple selection. */
	@WidgetProperty(name = "multipleSelection", description = "Determines if multiple selection is allowed.")
	protected boolean isMultipleSelection = false;

	/** The on selection changed. */
	@WidgetProperty(description = "This event fire when the selection is changed.")
	protected Event onSelectionChanged;

	/** The selected items. */
	protected Set<ListItem> selectedItems = new HashSet<ListItem>();

	/**
	 * Instantiates a new alpha list.
	 * 
	 * @param name
	 *            the name
	 */
	public AlphaList(final String name) {
		super(name);
		this.onSelectionChanged = EventFactory.getInstance()
				.createDefaultEvent(this);
		this.width = 300;
		this.height = 27;
		this.label = name;
		this.ui = new AlphaListUI(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#validate()
	 */
	@Override
	public Set<ValidationFailure> validate() {
		return this.validateValue(this.items);
	}

	/**
	 * Checks if is multiple selection.
	 * 
	 * @return true, if is multiple selection
	 */
	public boolean isMultipleSelection() {
		return this.isMultipleSelection;
	}

	/**
	 * Sets the multiple selection.
	 * 
	 * @param isMultipleSelection
	 *            the new multiple selection
	 */
	public void setMultipleSelection(final boolean isMultipleSelection) {
		this.isMultipleSelection = isMultipleSelection;
	}

	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            the new label
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * Gets the show label.
	 * 
	 * @return the show label
	 */
	public WidgetLabelPosition getShowLabel() {
		return this.showLabel;
	}

	/**
	 * Sets the show label.
	 * 
	 * @param showLabel
	 *            the new show label
	 */
	public void setShowLabel(final WidgetLabelPosition showLabel) {
		this.showLabel = showLabel;
	}

	/**
	 * Gets the items.
	 * 
	 * @return the items
	 */
	public List<ListItem> getItems() {
		return this.items;
	}

	/**
	 * Sets the items.
	 * 
	 * @param items
	 *            the new items
	 */
	public void setItems(final List<ListItem> items) {
		this.items = items;
	}

	/**
	 * Adds the item.
	 * 
	 * @param o
	 *            the o
	 */
	public void addItem(final ListItem o) {
		this.items.add(o);
	}

	/**
	 * Removes the item.
	 * 
	 * @param o
	 *            the o
	 */
	public void removeItem(final ListItem o) {
		this.items.remove(o);
	}

	/**
	 * Checks if is editable.
	 * 
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return this.isEditable;
	}

	/**
	 * Sets the editable.
	 * 
	 * @param isEditable
	 *            the new editable
	 */
	public void setEditable(final boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * Gets the selected items.
	 * 
	 * @return the selected items
	 */
	public List<ListItem> getSelectedItems() {
		final List<ListItem> out = new ArrayList<ListItem>();
		for (final ListItem item : this.items) {
			if (item.isSelected()) {
				out.add(item);
			}
		}
		return out;
	}

	/**
	 * Gets the selected item.
	 * 
	 * @return the selected item
	 */
	public ListItem getSelectedItem() {
		if (this.getSelectedItemCount() == 1) {
			for (final ListItem item : this.items) {
				if (item.isSelected())
					return item;
			}
		}
		return null;
	}

	/**
	 * Gets the selected item count.
	 * 
	 * @return the selected item count
	 */
	public int getSelectedItemCount() {
		int count = 0;
		for (final ListItem item : this.items) {
			if (item.isSelected()) {
				count++;
			}
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getDescription()
	 */
	@Override
	public String getDescription() {
		return "A combo box.";
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
		return 300;
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
	 * The Class ListItem.
	 */
	public static class ListItem {

		/** The id. */
		protected String id;

		/** The label. */
		protected String label;

		/** The selected. */
		protected boolean selected = false;

		/**
		 * Instantiates a new list item.
		 */
		public ListItem() {
			super();
			this.id = "0";
			this.label = "";
		}

		/**
		 * Instantiates a new list item.
		 * 
		 * @param id
		 *            the id
		 * @param label
		 *            the label
		 */
		public ListItem(final String id, final String label) {
			super();
			this.id = id;
			this.label = label;
		}

		/**
		 * Gets the id.
		 * 
		 * @return the id
		 */
		public String getId() {
			return this.id;
		}

		/**
		 * Sets the id.
		 * 
		 * @param id
		 *            the new id
		 */
		public void setId(final String id) {
			this.id = id;
		}

		/**
		 * Gets the label.
		 * 
		 * @return the label
		 */
		public String getLabel() {
			return this.label;
		}

		/**
		 * Sets the label.
		 * 
		 * @param label
		 *            the new label
		 */
		public void setLabel(final String label) {
			this.label = label;
		}

		/**
		 * Checks if is selected.
		 * 
		 * @return the boolean
		 */
		public Boolean isSelected() {
			return this.selected;
		}

		/**
		 * Sets the selected.
		 * 
		 * @param selected
		 *            the new selected
		 */
		public void setSelected(final Boolean selected) {
			this.selected = selected;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.label;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		protected ListItem clone() {
			final ListItem i = new ListItem();
			i.id = this.id;
			i.label = this.label;
			i.selected = this.selected;
			return i;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createWidgetMemento()
	 */
	@Override
	public WidgetMemento createWidgetMemento() {
		final WidgetMemento m = new ListWidgetMemento();
		m.setName(this.name);
		m.setType(this.getClass());
		final List<ListItem> copy = new ArrayList<ListItem>();
		for (final ListItem item : this.items) {
			copy.add(item.clone());
		}
		m.setValue(copy);
		m.addAttribute("isMultiselect", this.isMultipleSelection);
		m.addAttribute("isEditable", this.isEditable);
		m.addAttribute("label", this.label);
		m.addAttribute("showLabel", this.showLabel);
		m.addAttribute("x", this.x);
		m.addAttribute("y", this.y);
		m.addAttribute("width", this.width);
		m.addAttribute("height", this.height);
		m.addAttribute("ui", this.ui.getClass().getName());
		m.setValidators(this.validators.createMemento());
		final List<EventMemento> events = new ArrayList<EventMemento>();
		final EventMemento ev = this.onSelectionChanged.createMemento();
		ev.setEventName("onSelectionChanged");
		events.add(ev);
		m.setEvents(events);
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
			this.isMultipleSelection = Boolean.parseBoolean(attributes.get(
					"isMultiselect").toString());
			this.isEditable = Boolean.parseBoolean(attributes.get("isEditable")
					.toString());
			this.label = attributes.get("label").toString();
			this.showLabel = WidgetLabelPosition.valueOf(attributes.get(
					"showLabel").toString());
			this.x = Integer.parseInt(attributes.get("x").toString());
			this.y = Integer.parseInt(attributes.get("y").toString());
			this.width = Integer.parseInt(attributes.get("width").toString());
			this.height = Integer.parseInt(attributes.get("height").toString());
			this.setSize(this.width, this.height);
			this.setX(this.x);
			this.setY(this.y);
			this.items = (List<ListItem>) m.getValue();
			this.validators.setMemento(m.getValidators());
			for (final EventMemento em : m.getEvents()) {
				if (em.getEventName().equals("onSelectionChanged")) {
					this.onSelectionChanged.setMemento(em);
				}
			}
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
		final ListValueMemento vm = new ListValueMemento();
		vm.setName(this.name);
		final List<ListItem> copy = new ArrayList<ListItem>();
		for (final ListItem item : this.items) {
			copy.add(item.clone());
		}
		vm.setValue(copy);
		return vm;
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
		this.items = (List<ListItem>) m.getValue();
	}

}
