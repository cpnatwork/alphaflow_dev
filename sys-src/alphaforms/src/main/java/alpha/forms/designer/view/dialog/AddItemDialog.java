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
package alpha.forms.designer.view.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import alpha.forms.form.validation.NumberInputVerifier;

/**
 * The Class AddItemDialog.
 */
public class AddItemDialog extends JDialog implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7078965132120782432L;

	/** The items. */
	private final List<Item> items = new ArrayList<Item>();

	/** The panel. */
	private final JPanel panel;

	/** The save. */
	private final JButton save;

	/** The cancel. */
	private final JButton cancel;

	/** The was cancled. */
	private boolean wasCancled;

	/** The was saved. */
	private boolean wasSaved;

	/**
	 * Creates the.
	 * 
	 * @param type
	 *            the type
	 * @param instance
	 *            the instance
	 * @return the adds the item dialog
	 */
	public static AddItemDialog create(final Class type, final Object instance) {
		final AddItemDialog dialog = new AddItemDialog();

		for (final Method m : type.getMethods()) {
			if (m.getName().startsWith("set")
					&& (m.getParameterTypes().length == 1)) {
				final String name = m.getName().substring(3);

				Method g = null;

				try {
					g = type.getMethod("get" + name);
				} catch (final Exception e) {
				}
				if (g == null) {
					try {
						g = type.getMethod("is" + name);
					} catch (final Exception e) {
					}
				}
				if (g == null) {
					continue;
				}

				Object value = null;
				try {
					value = g.invoke(instance);
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				dialog.addItem(name, value);
			}
		}

		return dialog;
	}

	/**
	 * Instantiates a new adds the item dialog.
	 */
	public AddItemDialog() {
		super((Frame) null, "Add item:", true);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.panel = new JPanel(new GridBagLayout());
		this.panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		final JPanel buttonPanel = new JPanel(new FlowLayout());

		this.save = new JButton("Save");
		this.save.addActionListener(this);
		this.save.setActionCommand("save");
		this.cancel = new JButton("Cancel");
		this.cancel.addActionListener(this);
		this.cancel.setActionCommand("cancel");

		buttonPanel.add(this.save);
		buttonPanel.add(this.cancel);

		this.add(this.panel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(this.save);
	}

	/**
	 * Adds the item.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public void addItem(final String name, final Object value) {
		final Item i = new Item();
		i.name = name;
		i.value = value;
		this.items.add(i);
	}

	/**
	 * Adds the item.
	 * 
	 * @param name
	 *            the name
	 */
	public void addItem(final String name) {
		this.addItem(name, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean flag) {
		if (flag == true) {
			this.panel.removeAll();
			for (final Item i : this.items) {
				i.createUI(this.panel);
			}
			this.pack();
			this.wasCancled = false;
			this.wasSaved = false;
		}
		super.setVisible(flag);
	}

	/**
	 * Was cancled.
	 * 
	 * @return true, if successful
	 */
	public boolean wasCancled() {
		return this.wasCancled;
	}

	/**
	 * Was saved.
	 * 
	 * @return true, if successful
	 */
	public boolean wasSaved() {
		return this.wasSaved;
	}

	/**
	 * Gets the value map.
	 * 
	 * @return the value map
	 */
	public Map<String, Object> getValueMap() {
		final Map<String, Object> out = new HashMap<String, Object>();
		for (final Item i : this.items) {
			out.put(i.name, i.getValue());
		}
		return out;
	}

	/**
	 * The Class Item.
	 */
	protected class Item {

		/** The name. */
		public String name;

		/** The value. */
		public Object value;

		/** The component. */
		private JComponent component;

		/**
		 * Creates the ui.
		 * 
		 * @param parent
		 *            the parent
		 */
		public void createUI(final JPanel parent) {
			final GridBagConstraints c = new GridBagConstraints();
			final JLabel label = new JLabel(this.name);

			if ((this.value != null)
					&& (this.value.getClass().equals(boolean.class) || this.value
							.getClass().equals(Boolean.class))) {
				final JCheckBox checkBox = new JCheckBox();
				checkBox.setSelected((Boolean) this.value);
				this.component = checkBox;
			} else if ((this.value != null) && this.value.getClass().isEnum()) {
				this.component = new JComboBox(this.value.getClass()
						.getEnumConstants());
			} else {
				final JTextField textField = new JTextField();
				textField.setPreferredSize(new Dimension(200, 22));
				textField.setText(this.value.toString());
				if ((this.value != null)
						&& (this.value.getClass().equals(Integer.class)
								|| this.value.getClass().equals(Double.class) || this.value
								.getClass().equals(Float.class))) {
					textField.setInputVerifier(new NumberInputVerifier());
				}
				this.component = textField;
			}

			c.gridx = 0;
			c.anchor = GridBagConstraints.LINE_START;
			c.weightx = 0;
			c.insets = new Insets(4, 4, 4, 4);

			parent.add(label, c);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.weightx = 0.2;
			c.insets = new Insets(4, 4, 4, 4);

			parent.add(this.component, c);
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public Object getValue() {
			if ((this.component != null)
					&& (this.component instanceof JTextField)) {
				final String val = ((JTextField) this.component).getText();
				if ((this.value != null)
						&& this.value.getClass().equals(Integer.class))
					return Integer.parseInt(val);
				else if ((this.value != null)
						&& this.value.getClass().equals(Double.class)) {
					Double.parseDouble(val);
				} else if ((this.value != null)
						&& this.value.getClass().equals(Float.class)) {
					Float.parseFloat(val);
				}
				return val;
			} else if ((this.component != null)
					&& (this.component instanceof JCheckBox))
				return ((JCheckBox) this.component).isSelected();
			else if ((this.component != null)
					&& (this.component instanceof JComboBox))
				return ((JComboBox) this.component).getSelectedItem();
			return new String();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("save")) {
			this.setVisible(false);
			this.wasSaved = true;
		} else if (e.getActionCommand().equals("cancel")) {
			this.setVisible(false);
			this.wasCancled = true;
		}

	}
}
