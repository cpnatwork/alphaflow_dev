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
package alpha.forms.propertyEditor.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import alpha.forms.form.AlphaForm;
import alpha.forms.form.naming.WidgetNameExistsException;
import alpha.forms.propertyEditor.PropertyEditorPanel;
import alpha.forms.signal.model.PropertyUpdatedSignal;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class PropertyEditorModel.
 */
public class PropertyEditorModel implements TableModel, Subscriber {

	/** The form. */
	private final AlphaForm form;

	/** The view. */
	private final PropertyEditorPanel view;

	/** The data map. */
	private final Map<String, WidgetProperty> dataMap = new HashMap<String, WidgetProperty>();

	/** The prop names. */
	private final List<String> propNames = new ArrayList<String>();

	/** The model listeners. */
	private final List<TableModelListener> modelListeners = new ArrayList<TableModelListener>();

	/** The selected components. */
	private List<FormWidget> selectedComponents = null;

	/**
	 * Instantiates a new property editor model.
	 * 
	 * @param form
	 *            the form
	 * @param view
	 *            the view
	 */
	public PropertyEditorModel(final AlphaForm form,
			final PropertyEditorPanel view) {
		super();
		this.form = form;
		this.view = view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.signal.model.Subscriber#signalReceived(alpha.forms.signal
	 * .model.Signal)
	 */
	@Override
	public void signalReceived(final Signal s) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#addTableModelListener(javax.swing.event.
	 * TableModelListener)
	 */
	@Override
	public void addTableModelListener(final TableModelListener l) {
		this.modelListeners.add(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(final int col) {
		if (col == 0)
			return String.class;
		else if (col == 1)
			return Object.class;
		else
			return Object.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 2;
	}

	/**
	 * Gets the property for row.
	 * 
	 * @param row
	 *            the row
	 * @return the property for row
	 */
	private WidgetProperty getPropertyForRow(final int row) {
		return this.dataMap.get(this.propNames.get(row));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(final int col) {
		if (col == 0)
			return "Name";
		else if (col == 1)
			return "Value";
		else
			return "-";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return this.dataMap.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(final int row, final int col) {
		if (col == 0)
			return this.propNames.get(row);
		else if (col == 1)
			return this.dataMap.get(this.propNames.get(row)).getValue();
		else
			return "-";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(final int row, final int col) {
		return col == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#removeTableModelListener(javax.swing.event
	 * .TableModelListener)
	 */
	@Override
	public void removeTableModelListener(final TableModelListener l) {
		this.modelListeners.remove(l);
	}

	/**
	 * Fire table data changed.
	 */
	public void fireTableDataChanged() {
		for (final TableModelListener l : this.modelListeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object value, final int row, final int col) {
		final String key = this.propNames.get(row);
		final WidgetProperty p = this.dataMap.get(key);
		p.setValue(value);
		Object o;
		if ((this.selectedComponents != null)
				&& !this.selectedComponents.isEmpty()) {
			o = this.selectedComponents.get(0);
		} else {
			o = this.form;
		}
		try {
			if (p.getType().equals(int.class)
					|| p.getType().equals(Integer.class)) {
				value = Integer.parseInt((String) value);
			} else if (p.getType().isEnum()) {
				// value = Enum.valueOf(p.getType(), (String)value);
			}
			p.getSetter().invoke(o, value);
		} catch (final IllegalArgumentException e) {
		} catch (final IllegalAccessException e) {
		} catch (final InvocationTargetException e) {
			this.updateDataModel(this.selectedComponents);
			if (e.getCause() instanceof WidgetNameExistsException) {
				JOptionPane.showMessageDialog(null, e.getCause().getMessage(),
						"Error while renaming widget",
						JOptionPane.ERROR_MESSAGE);
			}
			e.printStackTrace();
		}

		final PropertyUpdatedSignal s = new PropertyUpdatedSignal();
		s.setSource(this);
		s.setPropertyName(key);
		SignalManager.getInstance().sendSignal(s, "propertyEditor");
	}

	/**
	 * Gets the widget property for row.
	 * 
	 * @param row
	 *            the row
	 * @return the widget property for row
	 */
	public WidgetProperty getWidgetPropertyForRow(final int row) {
		return this.dataMap.get(this.propNames.get(row));
	}

	/**
	 * Gets the panel.
	 * 
	 * @return the panel
	 */
	public PropertyEditorPanel getPanel() {
		return this.view;
	}

	/**
	 * Gets the selected components.
	 * 
	 * @return the selected components
	 */
	public List<FormWidget> getSelectedComponents() {
		return this.selectedComponents;
	}

	/**
	 * Update data model.
	 * 
	 * @param widgets
	 *            the widgets
	 */
	public void updateDataModel(final List<FormWidget> widgets) {
		this.selectedComponents = widgets;
		this.dataMap.clear();
		this.propNames.clear();
		Class c = null;
		Object o = null;
		if ((widgets != null) && !widgets.isEmpty()) {
			c = widgets.get(0).getClass();
			o = widgets.get(0);
		} else {
			c = this.form.getClass();
			o = this.form;
		}
		this.findProperties(c, o);
		this.view.stopTableEditing();
		this.fireTableDataChanged();
	}

	/**
	 * Find properties.
	 * 
	 * @param c
	 *            the c
	 * @param o
	 *            the o
	 */
	private void findProperties(Class c, final Object o) {
		while (c != null) {
			for (final Field f : c.getDeclaredFields()) {
				f.setAccessible(true);
				final alpha.forms.propertyEditor.model.annotation.WidgetProperty ap = f
						.getAnnotation(alpha.forms.propertyEditor.model.annotation.WidgetProperty.class);
				if ((ap != null) && ap.display()) {
					final WidgetProperty p = new WidgetProperty();
					p.setName((ap.name().isEmpty()) ? f.getName() : ap.name());
					p.setCategory(ap.category());
					p.setDescription(ap.description());
					try {
						p.setValue(f.get(o));
					} catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (f.getType().equals(boolean.class)) {

					}
					String setterName = "set"
							+ f.getName().substring(0, 1).toUpperCase()
							+ f.getName().substring(1);
					String getterName = "get"
							+ f.getName().substring(0, 1).toUpperCase()
							+ f.getName().substring(1);
					if (f.getType().equals(boolean.class)) {
						setterName = "set"
								+ f.getName().substring(0, 1).toUpperCase()
								+ f.getName().substring(1);
						getterName = "is"
								+ f.getName().substring(0, 1).toUpperCase()
								+ f.getName().substring(1);
					}
					if (f.getType().equals(boolean.class)
							&& f.getName().startsWith("is")) {
						setterName = "set"
								+ f.getName().substring(2, 3).toUpperCase()
								+ f.getName().substring(3);
						getterName = f.getName();
					}
					Method setter = null;
					Method getter = null;
					try {
						setter = c.getMethod(setterName, f.getType());
						getter = c.getMethod(getterName);
					} catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					p.setSetter(setter);
					p.setGetter(getter);
					p.setType(f.getType());
					if ((f.getGenericType() != null)
							&& (f.getGenericType() instanceof ParameterizedType)) {
						final ParameterizedType gt = (ParameterizedType) f
								.getGenericType();
						if ((gt.getActualTypeArguments() != null)
								&& (gt.getActualTypeArguments().length > 0)) {
							p.setGenericType((Class) gt
									.getActualTypeArguments()[0]);
							p.setType((Class) gt.getRawType());
						}
					}
					if (setter == null) {
						p.setReadonly(true);
					}
					this.propNames.add(p.getName());
					this.dataMap.put(p.getName(), p);
				}
			}
			c = c.getSuperclass();
		}
	}

}
