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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import alpha.forms.form.AlphaForm;
import alpha.forms.signal.model.PropertyUpdatedSignal;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.container.ContainerWidget;

/**
 * The Class FormDesignerPanel.
 */
public class FormDesignerPanel extends JPanel implements ComponentListener,
		Subscriber {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private final AlphaForm model;

	/** The form title. */
	final private JLabel formTitle = new JLabel();

	/** The document states. */
	private JComboBox documentStates = new JComboBox();

	/** The header. */
	private final JPanel header;

	/** The canvas. */
	private final FormCanvas canvas;

	/**
	 * Instantiates a new form designer panel.
	 * 
	 * @param model
	 *            the model
	 */
	public FormDesignerPanel(final AlphaForm model) {
		this.model = model;
		this.setBorder(BorderFactory.createTitledBorder("Form"));
		this.setMinimumSize(new Dimension(40, 40));
		this.setLayout(new BorderLayout());

		this.header = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.2;

		this.formTitle.setFont(this.formTitle.getFont().deriveFont(18.0f));

		this.header.add(this.formTitle, c);

		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0.0;

		if (model.getDocumentStates().isEmpty()) {
			this.documentStates = new JComboBox(new String[] { "NONE" });
			this.documentStates.setEditable(false);
			this.documentStates.setEnabled(false);
		} else {
			this.documentStates = new JComboBox(model.getDocumentStates()
					.toArray());
			this.documentStates.setEditable(false);
		}
		this.documentStates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				if (FormDesignerPanel.this.documentStates.getSelectedItem() != null) {
					model.setActiveDocumentState(FormDesignerPanel.this.documentStates
							.getSelectedItem().toString());
				}
			}
		});

		this.header.add(this.documentStates, c);

		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		final JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

		this.header.add(separator, c);

		this.add(this.header, BorderLayout.NORTH);

		this.canvas = new FormCanvas(this, model);
		final JScrollPane scroll = new JScrollPane(this.canvas);
		scroll.setBorder(null);
		this.add(scroll, BorderLayout.CENTER);

		SignalManager.getInstance().subscribeSink(this, "propertyEditor");

		this.updateUI();
		// setup standard form elements
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
	public void componentHidden(final ComponentEvent ev) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent
	 * )
	 */
	@Override
	public void componentMoved(final ComponentEvent ev) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
	public void componentResized(final ComponentEvent ev) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent
	 * )
	 */
	@Override
	public void componentShown(final ComponentEvent ev) {
		// TODO Auto-generated method stub

	}

	/**
	 * Adds the widget.
	 * 
	 * @param w
	 *            the w
	 * @param p
	 *            the p
	 */
	public void addWidget(final FormWidget w, final Point p) {
		final FormWidget container = this.canvas.findWidget(p);
		if ((container != null) && (container instanceof ContainerWidget)
				&& ((ContainerWidget) container).doesAcceptWidget(w)) {
			final Point gl = this.canvas.translatePoint(p);
			w.setX(gl.x - container.getX());
			w.setY(gl.y - container.getY());
			// w.setWidth(w.getMiniumWidth());
			// w.setHeight(w.getMinimumHeight());
			((ContainerWidget) container).addChild(w);
			w.setParent(container);
			this.canvas.stopContainerHighlight();
		} else {
			final Point gl = this.canvas.translatePoint(p);
			w.setX(gl.x);
			w.setY(gl.y);
			// w.setWidth(w.getMiniumWidth());
			// w.setHeight(w.getMinimumHeight());
			this.model.addWidget(w);
		}
	}

	/**
	 * Gets the drop area.
	 * 
	 * @return the drop area
	 */
	public Rectangle getDropArea() {
		return this.canvas.getBounds();
	}

	/**
	 * Gets the drop target component.
	 * 
	 * @return the drop target component
	 */
	public Component getDropTargetComponent() {
		return this.canvas;
	}

	/**
	 * Gets the selected items.
	 * 
	 * @return the selected items
	 */
	public List<FormWidget> getSelectedItems() {
		return this.canvas.getSelectedItems();
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
		if (s instanceof PropertyUpdatedSignal) {
			this.updateUI();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JPanel#updateUI()
	 */
	@Override
	public void updateUI() {
		if (this.model != null) {
			this.formTitle.setText(this.model.getTitle());
			if (this.model.getDocumentStates().isEmpty()) {
				this.documentStates = new JComboBox(new String[] { "NONE" });
				this.documentStates.setEditable(false);
				this.documentStates.setEnabled(false);
			} else {
				this.documentStates = new JComboBox(this.model
						.getDocumentStates().toArray());
				this.documentStates.setEditable(false);
			}
		}
		super.updateUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(500, 600);
	}

}
