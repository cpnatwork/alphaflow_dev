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
package alpha.forms.propertyEditor.tableCell;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import alpha.forms.designer.view.dialog.AddItemDialog;
import alpha.forms.propertyEditor.model.PropertyEditorModel;

/**
 * The Class ListTableEditor.
 */
public class ListTableEditor extends AbstractCellEditor implements
		TableCellRenderer, TableCellEditor {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -519483586940768013L;

	/** The property editor model. */
	private final PropertyEditorModel propertyEditorModel;

	/** The panel. */
	private JPanel panel;

	/** The label. */
	private JLabel label;

	/** The dialog. */
	private JDialog dialog;

	/** The contents. */
	private final List<Object> contents = new ArrayList<Object>();

	/** The generic type. */
	private Class genericType;

	/**
	 * Instantiates a new list table editor.
	 * 
	 * @param propertyEditorModel
	 *            the property editor model
	 * @param values
	 *            the values
	 * @param genericType
	 *            the generic type
	 */
	public ListTableEditor(final PropertyEditorModel propertyEditorModel,
			final List<Object> values, final Class genericType) {

		this.propertyEditorModel = propertyEditorModel;
		this.genericType = (genericType == null) ? String.class : genericType;

		this.panel = new JPanel();
		this.label = new JLabel("...");
		final JButton edit = new JButton("...");
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				ListTableEditor.this.dialog.setVisible(true);
				ListTableEditor.this.fireEditingStopped();
			}

		});
		this.panel.setLayout(new BorderLayout());
		this.panel.add(this.label, BorderLayout.CENTER);
		this.panel.add(edit, BorderLayout.EAST);

		for (final Object o : values) {
			this.contents.add(o);
		}
		this.dialog = new JDialog((JFrame) null, "List Editor", true);
		this.dialog.setLocationRelativeTo(this.propertyEditorModel.getPanel());
		this.dialog.setLayout(new BorderLayout());
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		final DefaultListModel lm = new DefaultListModel();

		for (final Object s : this.contents) {
			lm.addElement(s);
		}

		final JList list = new JList(lm);
		panel.add(new JScrollPane(list), BorderLayout.CENTER);

		final JButton remove = new JButton("Remove");
		remove.setEnabled(false);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				final Object o = list.getSelectedValue();
				ListTableEditor.this.contents.remove(o);
				lm.removeElement(o);
			}
		});

		final JButton add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				if (genericType.equals(String.class)) {
					final String s = JOptionPane
							.showInputDialog("Insert new item:");
					if ((s != null) && !s.isEmpty()) {
						ListTableEditor.this.contents.add(s);
						lm.addElement(s);
					}
				} else {

					Object itemObject = null;

					try {
						final Constructor c = genericType.getConstructor();

						itemObject = c.newInstance();
					} catch (final Exception e) {
						System.err
								.print("Unable to instantiate generic type class: "
										+ e.getMessage());
						return;
					}
					if (itemObject == null)
						return;

					final AddItemDialog input = AddItemDialog.create(
							genericType, itemObject);

					input.setVisible(true);
					if (input.wasSaved()) {
						final Map<String, Object> values = input.getValueMap();
						for (final String name : values.keySet()) {
							Method setter = null;
							try {
								setter = genericType.getMethod("set" + name,
										values.get(name).getClass());
								System.out.println(values.get(name).getClass());
								setter.invoke(itemObject, values.get(name));
							} catch (final Exception e) {
								e.printStackTrace();
								continue;
							}
						}
						ListTableEditor.this.contents.add(itemObject);
						lm.addElement(itemObject);
					}
				}
			}
		});

		final JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent ev) {
				ListTableEditor.this.dialog.setVisible(false);
			}

		});

		final JPanel btnPanel = new JPanel(new FlowLayout());
		btnPanel.add(add);
		btnPanel.add(remove);
		btnPanel.add(ok);

		panel.add(btnPanel, BorderLayout.SOUTH);

		list.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(final MouseEvent ev) {
				if (ev.getClickCount() >= 2) {
					final int index = list.locationToIndex(ev.getPoint());
					final Object itemObject = list.getModel().getElementAt(
							index);
					final AddItemDialog dialog = AddItemDialog.create(
							genericType, itemObject);
					dialog.setVisible(true);
					if (dialog.wasSaved()) {
						final Map<String, Object> values = dialog.getValueMap();
						for (final String name : values.keySet()) {
							Method setter = null;
							try {
								setter = genericType.getMethod("set" + name,
										values.get(name).getClass());
								System.out.println(values.get(name).getClass());
								setter.invoke(itemObject, values.get(name));
							} catch (final Exception e) {
								e.printStackTrace();
								continue;
							}
						}
					}
				}
			}

			@Override
			public void mouseEntered(final MouseEvent arg0) {
			}

			@Override
			public void mouseExited(final MouseEvent arg0) {
			}

			@Override
			public void mousePressed(final MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(final MouseEvent arg0) {
			}

		});

		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent ev) {

				if (list.getSelectedIndex() != -1) {
					remove.setEnabled(true);
				} else {
					remove.setEnabled(false);
				}

			}
		});

		this.dialog.add(panel, BorderLayout.CENTER);
		this.dialog.pack();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
	 * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(final JTable table,
			final Object value, final boolean isSelected,
			final boolean hasFocus, final int row, final int col) {
		if (isSelected) {
			this.panel.setBackground(table.getSelectionBackground());
			this.panel.setForeground(table.getSelectionForeground());
		} else {
			this.panel.setBackground(table.getBackground());
			this.panel.setForeground(table.getForeground());
		}
		this.panel.setOpaque(true);
		if (((List) value).isEmpty()) {
			this.label.setText("(empty)");
		} else {
			this.label.setText(((List) value).toString());
		}
		return this.panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.contents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing
	 * .JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(final JTable table,
			final Object value, final boolean isSelected, final int arg3,
			final int arg4) {
		this.panel.setOpaque(true);
		if (((List) value).isEmpty()) {
			this.label.setText("(empty)");
		} else {
			this.label.setText(((List) value).toString());
		}
		return this.panel;
	}

}