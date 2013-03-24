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
package alpha.forms.propertyEditor;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import alpha.forms.form.AlphaForm;
import alpha.forms.propertyEditor.component.InfoPanel;
import alpha.forms.propertyEditor.component.PropertyTable;
import alpha.forms.propertyEditor.model.PropertyEditorModel;
import alpha.forms.propertyEditor.model.WidgetProperty;
import alpha.forms.signal.model.SelectionChangedSignal;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class PropertyEditorPanel.
 */
public class PropertyEditorPanel extends JPanel implements Subscriber {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private final AlphaForm model;

	/** The selected components. */
	private List<FormWidget> selectedComponents = null;

	/** The info. */
	private final InfoPanel info;

	/** The table. */
	private final PropertyTable table;

	/** The editor model. */
	private final PropertyEditorModel editorModel;

	/**
	 * Instantiates a new property editor panel.
	 * 
	 * @param model
	 *            the model
	 */
	public PropertyEditorPanel(final AlphaForm model) {
		this.model = model;

		this.setBorder(BorderFactory.createTitledBorder("Property Editor"));
		this.setLayout(new BorderLayout());

		this.editorModel = new PropertyEditorModel(model, this);

		this.editorModel.updateDataModel(null);

		this.table = new PropertyTable(this, this.editorModel);
		this.info = new InfoPanel();

		this.table.setRowHeight(23);
		this.table.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(final MouseEvent ev) {
				final WidgetProperty p = PropertyEditorPanel.this.editorModel
						.getWidgetPropertyForRow(PropertyEditorPanel.this.table
								.getSelectedRow());
				PropertyEditorPanel.this.info.updateWith(p);
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

		this.add(new JScrollPane(this.table), BorderLayout.CENTER);
		this.add(this.info, BorderLayout.SOUTH);

		SignalManager.getInstance().subscribeSink(this, "formCanvas");
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
		if (s instanceof SelectionChangedSignal) {
			this.selectedComponents = ((SelectionChangedSignal) s)
					.getSelection();
			this.editorModel.updateDataModel(this.selectedComponents);
		}
	}

	/**
	 * Stop table editing.
	 */
	public void stopTableEditing() {
		if ((this.table != null) && this.table.isEditing()) {
			this.table.getCellEditor().cancelCellEditing();
		}
	}

}
