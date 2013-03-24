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
package alpha.forms.propertyEditor.component;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import alpha.forms.form.event.DefaultEvent;
import alpha.forms.form.event.Event;
import alpha.forms.form.validation.ValidatorGroup;
import alpha.forms.propertyEditor.PropertyEditorPanel;
import alpha.forms.propertyEditor.model.PropertyEditorModel;
import alpha.forms.propertyEditor.model.WidgetProperty;
import alpha.forms.propertyEditor.tableCell.CheckboxTableEditor;
import alpha.forms.propertyEditor.tableCell.CheckboxTableRenderer;
import alpha.forms.propertyEditor.tableCell.DropDownTableEditor;
import alpha.forms.propertyEditor.tableCell.DropDownTableRenderer;
import alpha.forms.propertyEditor.tableCell.EventTableEditor;
import alpha.forms.propertyEditor.tableCell.ListTableEditor;
import alpha.forms.propertyEditor.tableCell.ValidationTableEditor;

/**
 * The Class PropertyTable.
 */
public class PropertyTable extends JTable {

	/** The property editor panel. */
	private final PropertyEditorPanel propertyEditorPanel;

	/** The editor model. */
	private final PropertyEditorModel editorModel;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new property table.
	 * 
	 * @param propertyEditorPanel
	 *            the property editor panel
	 * @param tm
	 *            the tm
	 */
	public PropertyTable(final PropertyEditorPanel propertyEditorPanel,
			final PropertyEditorModel tm) {
		super(tm);
		this.editorModel = tm;
		this.propertyEditorPanel = propertyEditorPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#getCellEditor(int, int)
	 */
	@Override
	public TableCellEditor getCellEditor(final int row, final int column) {
		final WidgetProperty p = this.editorModel.getWidgetPropertyForRow(row);
		if ((column > 0) && p.getType().isEnum())
			return new DropDownTableEditor(p.getType().getEnumConstants());
		else if ((column > 0) && p.getType().equals(boolean.class))
			return new CheckboxTableEditor();
		else if ((column > 0) && p.getType().equals(List.class))
			return new ListTableEditor(this.editorModel,
					(List<Object>) p.getValue(), p.getGenericType());
		else if ((column > 0) && p.getType().equals(Event.class))
			return new EventTableEditor(this.editorModel,
					(DefaultEvent) p.getValue());
		else if ((column > 0) && p.getType().equals(ValidatorGroup.class))
			return new ValidationTableEditor(this.editorModel,
					(ValidatorGroup) p.getValue());
		else
			return super.getCellEditor(row, column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#getCellRenderer(int, int)
	 */
	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column) {
		final WidgetProperty p = this.editorModel.getWidgetPropertyForRow(row);
		if ((column > 0) && p.getType().isEnum())
			return new DropDownTableRenderer(p.getType().getEnumConstants());
		else if ((column > 0) && p.getType().equals(boolean.class))
			return new CheckboxTableRenderer();
		else if ((column > 0) && p.getType().equals(List.class))
			return new ListTableEditor(this.editorModel,
					(List<Object>) p.getValue(), p.getGenericType());
		else if ((column > 0) && p.getType().equals(Event.class))
			return new EventTableEditor(this.editorModel,
					(DefaultEvent) p.getValue());
		else if ((column > 0) && p.getType().equals(ValidatorGroup.class))
			return new ValidationTableEditor(this.editorModel,
					(ValidatorGroup) p.getValue());
		else
			return super.getCellRenderer(row, column);
	}

}