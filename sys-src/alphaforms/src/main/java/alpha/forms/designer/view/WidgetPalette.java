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
package alpha.forms.designer.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import alpha.forms.signal.model.SelectionChangedSignal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.template.model.WidgetTemplate;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class WidgetPalette.
 */
public class WidgetPalette extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The list. */
	protected JList list;

	/**
	 * Instantiates a new widget palette.
	 */
	public WidgetPalette() {
		this.setBorder(BorderFactory.createTitledBorder("Widget Palette"));
		this.setLayout(new BorderLayout());
		this.list = new JList(new WidgetListModel());
		this.list.setCellRenderer(new WidgetListCellRenderer());
		// list.setFixedCellWidth(200);
		final JScrollPane scroll = new JScrollPane(this.list);
		this.add(scroll, BorderLayout.CENTER);
		final InfoPanel info = new InfoPanel();
		this.add(info, BorderLayout.SOUTH);
		this.list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(final ListSelectionEvent ev) {
				final JList list = (JList) ev.getSource();
				final Object obj = list.getSelectedValue();
				if (obj instanceof FormWidget) {
					info.updateWith((FormWidget) obj);
				} else if (obj instanceof WidgetTemplate) {
					info.updateWith(((WidgetTemplate) obj)
							.createWidgetFromTemplate());
				}
				final SelectionChangedSignal s = new SelectionChangedSignal();
				s.setSource(this);
				s.setSelection(null);
				SignalManager.getInstance().sendSignal(s, "canvas");
			}

		});
	}

	/**
	 * Register widget class.
	 * 
	 * @param widget
	 *            the widget
	 */
	public void registerWidgetClass(final Class<? extends FormWidget> widget) {
		try {
			final Constructor<? extends FormWidget> c = widget
					.getConstructor(String.class);
			final FormWidget fw = c.newInstance(widget.getSimpleName());
			this.addWidget(fw);
		} catch (final SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds the widget.
	 * 
	 * @param fw
	 *            the fw
	 */
	protected void addWidget(final FormWidget fw) {
		((WidgetListModel) this.list.getModel()).addWidget(fw);

	}

	/**
	 * Gets the drag source.
	 * 
	 * @return the drag source
	 */
	public Component getDragSource() {
		return this.list;
	}

	/**
	 * The Class WidgetListModel.
	 */
	protected class WidgetListModel extends AbstractListModel {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -1606264013267122494L;
		/** The widgets. */
		private final Vector<FormWidget> widgets = new Vector<FormWidget>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		@Override
		public Object getElementAt(final int index) {
			return this.widgets.get(index);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ListModel#getSize()
		 */
		@Override
		public int getSize() {
			return this.widgets.size();
		}

		/**
		 * Adds the widget.
		 * 
		 * @param w
		 *            the w
		 */
		public void addWidget(final FormWidget w) {
			this.widgets.add(w);
			this.fireIntervalAdded(this, this.widgets.indexOf(w),
					this.widgets.indexOf(w));
		}

	}

	/**
	 * The Class WidgetListCellRenderer.
	 */
	protected class WidgetListCellRenderer implements ListCellRenderer {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
		 * .JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(final JList list,
				final Object object, final int index, final boolean isSelected,
				final boolean hasFocus) {

			final FormWidget w = (FormWidget) object;
			final JPanel comp = new JPanel();
			comp.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

			if (isSelected) {
				comp.setOpaque(true);
				comp.setBackground(SystemColor.textHighlight);
			} else {
				comp.setOpaque(true);
			}

			final Image im = w.getUi().getAsImage();

			final Image im_100w = im.getScaledInstance(150, -1,
					Image.SCALE_SMOOTH);
			ImageIcon icon = null;
			if (im_100w.getHeight(null) > 50) {
				icon = new ImageIcon(im_100w.getScaledInstance(-1, 50,
						Image.SCALE_SMOOTH));
			} else {
				icon = new ImageIcon(im_100w);
			}
			final JLabel img = new JLabel(icon);
			// img.setPreferredSize(new Dimension(icon.getIconWidth(),
			// icon.getIconHeight()));

			comp.add(img);
			// comp.setPreferredSize(new Dimension(icon.getIconWidth(),
			// icon.getIconHeight()));
			return comp;
		}

	}

	/**
	 * The Class InfoPanel.
	 */
	protected class InfoPanel extends JPanel {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The img. */
		private final JLabel img = new JLabel();

		/** The title. */
		private final JLabel title = new JLabel();

		/** The description. */
		private final JLabel description = new JLabel();

		/**
		 * Instantiates a new info panel.
		 */
		public InfoPanel() {
			this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			this.setLayout(new GridLayout(3, 1, 4, 4));
			this.title.setFont(this.title.getFont().deriveFont(15.0f));
			this.add(this.title);
			this.add(this.img);
			this.add(this.description);
		}

		/**
		 * Update with.
		 * 
		 * @param w
		 *            the w
		 */
		public void updateWith(final FormWidget w) {
			this.title.setText(w.getName() + " widget");

			final Image im = w.getUi().getAsImage();
			final Image im_100w = im.getScaledInstance(100, -1,
					Image.SCALE_SMOOTH);
			ImageIcon icon = null;
			if (im_100w.getHeight(null) > 50) {
				icon = new ImageIcon(im_100w.getScaledInstance(-1, 50,
						Image.SCALE_SMOOTH));
			} else {
				icon = new ImageIcon(im_100w);
			}
			this.img.setIcon(icon);
			this.description.setText(w.getDescription());
			this.updateUI();
		}

	}

}
