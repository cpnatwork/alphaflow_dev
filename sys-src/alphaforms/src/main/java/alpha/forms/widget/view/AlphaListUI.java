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
package alpha.forms.widget.view;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import alpha.forms.designer.view.SelectionState;
import alpha.forms.widget.model.AlphaList;
import alpha.forms.widget.model.AlphaList.ListItem;
import alpha.forms.widget.util.WidgetLabelPosition;

/**
 * The Class AlphaListUI.
 */
public class AlphaListUI extends FormWidgetUI {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	protected AlphaList model;

	/** The label. */
	private JLabel label;

	/** The list. */
	private JComponent list;

	/** The hgap. */
	private int hgap = 5;

	/**
	 * Instantiates a new alpha list ui.
	 * 
	 * @param model
	 *            the model
	 */
	public AlphaListUI(final AlphaList model) {
		super(model);
		this.model = model;
		this.compose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#doLayout()
	 */
	@Override
	public void doLayout() {
		super.doLayout();
		final Dimension d = this.model.getSize();
		super.setSize(d);
		this.remove(this.list);
		if (this.model.isMultipleSelection()) {
			this.list = new JList(this.model.getItems().toArray());
			final JList jlist = (JList) this.list;
			jlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			final List<Integer> selectedItems = new ArrayList<Integer>();
			for (int i = 0; i < this.model.getItems().size(); i++) {
				if (this.model.getItems().get(i).isSelected()) {
					selectedItems.add(i);
				}
			}

			final int[] selectedIndices = new int[selectedItems.size()];
			for (int i = 0; i < selectedItems.size(); i++) {
				selectedIndices[i] = selectedItems.get(i);
			}
			jlist.setSelectedIndices(selectedIndices);

			jlist.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						@Override
						public void valueChanged(final ListSelectionEvent ev) {
							for (final ListItem i : AlphaListUI.this.model
									.getItems()) {
								i.setSelected(false);
							}
							for (final int i : ((JList) AlphaListUI.this.list)
									.getSelectedIndices()) {
								AlphaListUI.this.model.getItems().get(i)
										.setSelected(true);
							}
							AlphaListUI.this.model.getOnSelectionChanged()
									.fire();
						}

					});
		} else {
			this.list = new JComboBox(new DefaultComboBoxModel(this.model
					.getItems().toArray()));
			if (this.model.isEditable()) {
				((JComboBox) this.list).setEditable(true);
			}
			for (final ListItem item : this.model.getItems()) {
				if (item.isSelected()) {
					((JComboBox) this.list).setSelectedItem(item);
					break;
				}
			}

			((JComboBox) this.list).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent ev) {
					final int index = ((JComboBox) AlphaListUI.this.list)
							.getSelectedIndex();
					int j = 0;
					for (final ListItem item : AlphaListUI.this.model
							.getItems()) {
						item.setSelected(j == index);
						j++;
					}
					AlphaListUI.this.model.getOnSelectionChanged().fire();
				}
			});
		}
		this.add(this.list);
		if ((this.label != null)
				&& (this.model.getShowLabel() == WidgetLabelPosition.LEFT)) {
			this.label.setText(this.model.getLabel());
			final Dimension dl = this.label.getPreferredSize();
			this.label.setSize(dl);
			this.label.setLocation(0, (d.height / 2) - (dl.height / 2));
			this.list.setLocation(dl.width + this.hgap, 0);
			this.list.setSize(d.width - this.hgap - dl.width, d.height);
		} else if ((this.label != null)
				&& (this.model.getShowLabel() == WidgetLabelPosition.RIGHT)) {
			this.label.setText(this.model.getLabel());
			final Dimension dl = this.label.getPreferredSize();
			this.label.setSize(dl);
			this.label.setLocation(d.width - dl.width, (d.height / 2)
					- (dl.height / 2));
			this.list.setLocation(0, 0);
			this.list.setSize(d.width - this.hgap - dl.width, d.height);
		} else {
			this.list.setLocation(0, 0);
			this.list.setSize(d.width, d.height);
			this.label.setSize(0, 0);
		}
		this.list.doLayout();
		this.setVisible(this.model.isVisible());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#getSubselection()
	 */
	@Override
	public Rectangle getSubselection() {
		if ((this.model.getShowLabel() == WidgetLabelPosition.LEFT)
				|| (this.model.getShowLabel() == WidgetLabelPosition.RIGHT)) {
			final Rectangle r = this.list.getBounds();
			r.translate(this.getLocation().x, this.getLocation().y);
			return r;
		} else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#isSubselectionResizable(int)
	 */
	@Override
	public boolean isSubselectionResizable(final int direction) {
		return ((direction == SelectionState.WEST) && (this.model
				.getShowLabel() == WidgetLabelPosition.LEFT))
				|| ((direction == SelectionState.EAST) && (this.model
						.getShowLabel() == WidgetLabelPosition.RIGHT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#updateSubselectionSize(int,
	 * int)
	 */
	@Override
	public void updateSubselectionSize(final int delta, final int direction) {
		if ((direction == SelectionState.WEST)
				&& (this.model.getShowLabel() == WidgetLabelPosition.LEFT)) {
			final int tmpgap = this.hgap - delta;
			if ((tmpgap >= 5)
					&& ((tmpgap + this.label.getWidth()) < this.model.getSize().width)) {
				this.hgap = tmpgap;
			}
		} else if ((direction == SelectionState.EAST)
				&& (this.model.getShowLabel() == WidgetLabelPosition.RIGHT)) {
			final int tmpgap = this.hgap - (delta - this.list.getWidth());
			if ((tmpgap >= 5)
					&& ((tmpgap + this.label.getWidth()) < this.model.getSize().width)) {
				this.hgap = tmpgap;
			}
		}
		this.doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#compose()
	 */
	@Override
	protected void compose() {
		super.compose();
		super.setSize(this.model.getSize());
		this.setLayout(null);
		if (this.model.isMultipleSelection()) {
			this.list = new JList(this.model.getItems().toArray());
			final JList jlist = (JList) this.list;
			jlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			final List<Integer> selectedItems = new ArrayList<Integer>();
			for (int i = 0; i < this.model.getItems().size(); i++) {
				if (this.model.getItems().get(i).isSelected()) {
					selectedItems.add(i);
				}
			}

			final int[] selectedIndices = new int[selectedItems.size()];
			for (int i = 0; i < selectedItems.size(); i++) {
				selectedIndices[i] = selectedItems.get(i);
			}
			jlist.setSelectedIndices(selectedIndices);

			jlist.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						@Override
						public void valueChanged(final ListSelectionEvent ev) {
							for (final ListItem i : AlphaListUI.this.model
									.getItems()) {
								i.setSelected(false);
							}
							for (final int i : ((JList) AlphaListUI.this.list)
									.getSelectedIndices()) {
								AlphaListUI.this.model.getItems().get(i)
										.setSelected(true);
							}
							AlphaListUI.this.model.getOnSelectionChanged()
									.fire();
						}

					});
		} else {
			this.list = new JComboBox(new DefaultComboBoxModel(this.model
					.getItems().toArray()));
			if (this.model.isEditable()) {
				((JComboBox) this.list).setEditable(true);
			}
			((JComboBox) this.list).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent ev) {
					final int index = ((JComboBox) AlphaListUI.this.list)
							.getSelectedIndex();
					int j = 0;
					for (final ListItem item : AlphaListUI.this.model
							.getItems()) {
						item.setSelected(j == index);
						j++;
					}
					AlphaListUI.this.model.getOnSelectionChanged().fire();
				}
			});
		}
		this.list.doLayout();
		this.list.setOpaque(this.isOpaque());
		if (this.model.getShowLabel() == WidgetLabelPosition.LEFT) {
			this.label = new JLabel(this.model.getLabel());
			final Dimension d = this.label.getPreferredSize();
			final Dimension dt = this.list.getPreferredSize();
			System.out.println(dt.width);
			this.add(this.label);
			this.add(this.list);
			this.label.setSize(d.width, d.height);
			this.label.setLocation(0, (dt.height / 2) - (d.height / 2));
			this.list.setSize(
					this.model.getMiniumWidth() - d.width - this.hgap,
					dt.height);
			this.list.setLocation(d.width + this.hgap, 0);

			this.minimumHeight = dt.height;
			this.minimumWidth = d.width + this.hgap + dt.width;
		} else if (this.model.getShowLabel() == WidgetLabelPosition.RIGHT) {
			this.label = new JLabel(this.model.getLabel());
			final Dimension d = this.label.getMinimumSize();
			final Dimension dt = this.list.getMinimumSize();
			this.list.setBounds(0, 0, this.model.getMiniumWidth() - d.width
					- this.hgap, dt.height);
			this.label.setBounds(this.model.getMiniumWidth() + this.hgap,
					(dt.height / 2) - (d.height / 2), d.width, d.height);
			this.add(this.list);
			this.add(this.label);
			this.minimumHeight = dt.height;
			this.minimumWidth = this.model.getMiniumWidth();
		} else {
			final Dimension dt = this.list.getMinimumSize();
			this.list.setBounds(0, 0, this.model.getMiniumWidth(), dt.height);
			this.add(this.list);
			this.minimumHeight = dt.height;
			this.minimumWidth = this.model.getMiniumWidth();
		}
		this.list.doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	@Override
	public void setSize(final Dimension d) {
		super.setSize(d);
		this.doLayout();
	}

}
