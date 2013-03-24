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

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import alpha.forms.designer.view.dialog.ValidatorDialog;
import alpha.forms.form.validation.Validator;
import alpha.forms.form.validation.ValidatorGroup;
import alpha.forms.propertyEditor.model.PropertyEditorModel;

/**
 * The Class ValidationTableEditor.
 */
public class ValidationTableEditor extends AbstractCellEditor implements
		TableCellRenderer, TableCellEditor {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The property editor model. */
	private final PropertyEditorModel propertyEditorModel;

	/** The panel. */
	private final JPanel panel;

	/** The label. */
	private final JLabel label;

	/** The dialog. */
	private final JDialog dialog;

	/** The group. */
	private final ValidatorGroup group;

	/**
	 * Instantiates a new validation table editor.
	 * 
	 * @param propertyEditorModel
	 *            the property editor model
	 * @param group
	 *            the group
	 */
	public ValidationTableEditor(final PropertyEditorModel propertyEditorModel,
			final ValidatorGroup group) {
		this.propertyEditorModel = propertyEditorModel;
		this.group = group;
		this.panel = new JPanel();
		this.label = new JLabel("(validators)");

		final JButton edit = new JButton("...");
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				ValidationTableEditor.this.dialog.setVisible(true);
				ValidationTableEditor.this.fireEditingStopped();
			}

		});
		this.panel.setLayout(new BorderLayout());
		this.panel.add(this.label, BorderLayout.CENTER);
		this.panel.add(edit, BorderLayout.EAST);

		this.dialog = new JDialog((JFrame) null, "Action List", true);
		this.dialog.setLocationRelativeTo(this.propertyEditorModel.getPanel());
		this.dialog.setLayout(new BorderLayout());
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		final DefaultListModel lm = new DefaultListModel();

		for (final Validator v : group.getValidators()) {
			if (v instanceof Validator) {
				lm.addElement(v);
			}
		}

		final JList list = new JList(lm);
		panel.add(new JScrollPane(list), BorderLayout.CENTER);

		final JButton remove = new JButton("Remove");
		remove.setEnabled(false);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				final Validator v = (Validator) list.getSelectedValue();
				group.removeValidator(v);
				lm.removeElement(v);
			}
		});

		final JButton add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {

				Validator validator = null; // =
											// ActionFactory.getInstance().createScriptedAction();

				final ValidatorDialog input = ValidatorDialog.create(validator,
						group,
						propertyEditorModel.getSelectedComponents().get(0));

				input.setVisible(true);
				if (input.wasSaved()) {
					validator = input.getValidator();
					group.addValidator(validator);
					lm.addElement(validator);
				}
			}
		});

		final JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent ev) {
				ValidationTableEditor.this.dialog.setVisible(false);
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
					final Validator validator = (Validator) list.getModel()
							.getElementAt(index);
					System.out.println(validator);
					final ValidatorDialog dialog = ValidatorDialog.create(
							validator, group, propertyEditorModel
									.getSelectedComponents().get(0));
					dialog.setVisible(true);
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
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.group;
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
		return this.panel;
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
		return this.panel;
	}

}