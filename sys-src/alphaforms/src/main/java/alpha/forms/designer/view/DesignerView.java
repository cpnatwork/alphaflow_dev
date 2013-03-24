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
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import alpha.forms.application.controller.ApplicationController;
import alpha.forms.application.view.panel.FormView;
import alpha.forms.designer.dnd.ComponentDragDrop;
import alpha.forms.form.AlphaForm;
import alpha.forms.propertyEditor.PropertyEditorPanel;
import alpha.forms.signal.model.SelectionChangedSignal;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.signal.model.SwitchViewSignal;
import alpha.forms.signal.model.UpdateStatusSignal;
import alpha.forms.template.model.WidgetTemplateMngr;
import alpha.forms.template.util.TemplateFileFilter;
import alpha.forms.template.view.TemplatePalette;
import alpha.forms.widget.model.AlphaList;
import alpha.forms.widget.model.Button;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.Group;
import alpha.forms.widget.model.Heading;
import alpha.forms.widget.model.Option;
import alpha.forms.widget.model.TextField;

/**
 * The Class DesignerView.
 */
public class DesignerView extends FormView implements Subscriber {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private final AlphaForm model;

	/** The status label. */
	private final JLabel statusLabel;

	/** The template palette. */
	private final TemplatePalette templatePalette;

	/** The form panel. */
	private final FormDesignerPanel formPanel;

	/**
	 * Instantiates a new designer view.
	 * 
	 * @param form
	 *            the form
	 * @param controller
	 *            the controller
	 */
	public DesignerView(final AlphaForm form,
			final ApplicationController controller) {
		super(controller);
		this.model = form;

		this.setLayout(new BorderLayout());

		final JSplitPane jsp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		final JSplitPane jsp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jsp1.setBorder(null);
		jsp2.setBorder(null);

		final Component parentComponent = this;
		final JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		final JButton button1 = new JButton("Save");

		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.persistForm();
			}

		});
		final JButton button2 = new JButton("Go to Fill-in mode");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				final SwitchViewSignal s = new SwitchViewSignal();
				s.setFromView(SwitchViewSignal.DESIGNER);
				s.setToView(SwitchViewSignal.VIEWER);
				SignalManager.getInstance().sendSignal(s, "designer");
			}
		});
		toolbar.add(button1);
		toolbar.add(button2);
		toolbar.addSeparator();

		final JButton btnStoreTemplates = new JButton("Export Templates");
		btnStoreTemplates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				final JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(false);
				fc.setFileFilter(new TemplateFileFilter());
				final int ret = fc.showSaveDialog(parentComponent);
				if (ret == JFileChooser.APPROVE_OPTION) {
					final File f = fc.getSelectedFile();
					try {
						final FileOutputStream fout = new FileOutputStream(f);
						WidgetTemplateMngr.getInstance().storeTemplates(fout);
						fout.close();
					} catch (final FileNotFoundException e) {
						e.printStackTrace();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		final JButton btnLoadTemplates = new JButton("Import Templates");
		btnLoadTemplates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ev) {
				int ret = JOptionPane
						.showConfirmDialog(
								parentComponent,
								"This will replace all current templates in the template library. Continue?",
								"Replace templates?",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (ret == JOptionPane.YES_OPTION) {
					final JFileChooser fc = new JFileChooser();
					fc.setMultiSelectionEnabled(false);
					fc.setFileFilter(new TemplateFileFilter());
					ret = fc.showOpenDialog(parentComponent);
					if (ret == JFileChooser.APPROVE_OPTION) {
						final File f = fc.getSelectedFile();
						try {
							final FileInputStream fin = new FileInputStream(f);
							WidgetTemplateMngr.getInstance().loadTemplates(fin);
							fin.close();
						} catch (final FileNotFoundException e) {
							e.printStackTrace();
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		toolbar.add(btnStoreTemplates);
		toolbar.add(btnLoadTemplates);

		final JToolBar status = new JToolBar();
		status.setFloatable(false);
		this.statusLabel = new JLabel("Selected: None.");
		status.addSeparator();
		status.add(this.statusLabel);

		final WidgetPalette widgetPalette = new WidgetPalette();
		widgetPalette.setMinimumSize(new Dimension(220, 300));
		widgetPalette.registerWidgetClass(TextField.class);
		widgetPalette.registerWidgetClass(AlphaList.class);
		widgetPalette.registerWidgetClass(Option.class);
		widgetPalette.registerWidgetClass(Group.class);
		widgetPalette.registerWidgetClass(Button.class);
		widgetPalette.registerWidgetClass(Heading.class);

		this.templatePalette = new TemplatePalette();
		this.templatePalette.setMinimumSize(new Dimension(220, 300));

		final JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Widgets", widgetPalette);
		tabPane.addTab("Widget Templates", this.templatePalette);

		final PropertyEditorPanel propertyPanel = new PropertyEditorPanel(
				this.model);
		propertyPanel.setMinimumSize(new Dimension(220, 300));

		this.formPanel = new FormDesignerPanel(this.model);

		// jsp1.setLeftComponent(widgetPalette);
		jsp1.setLeftComponent(tabPane);
		jsp1.setRightComponent(jsp2);
		jsp2.setLeftComponent(this.formPanel);
		jsp2.setRightComponent(propertyPanel);

		this.add(toolbar, BorderLayout.NORTH);
		this.add(status, BorderLayout.SOUTH);
		this.add(jsp1, BorderLayout.CENTER);

		SignalManager.getInstance().subscribeSink(this, "formCanvas");

		final ComponentDragDrop dndListener = new ComponentDragDrop();
		// TemplateDragDrop dndTemplate = new TemplateDragDrop();
		final DragSource dragSource1 = new DragSource();
		final DragSource dragSource2 = new DragSource();
		final DropTarget dropTarget1 = new DropTarget(
				this.formPanel.getDropTargetComponent(),
				DnDConstants.ACTION_COPY, dndListener);
		// DropTarget dropTarget2 = new
		// DropTarget(formPanel.getDropTargetComponent(),
		// DnDConstants.ACTION_COPY, dndTemplate);
		final DragGestureRecognizer dndrec1 = dragSource1
				.createDefaultDragGestureRecognizer(
						widgetPalette.getDragSource(),
						DnDConstants.ACTION_COPY, dndListener);
		final DragGestureRecognizer dndrec2 = dragSource2
				.createDefaultDragGestureRecognizer(
						this.templatePalette.getDragSource(),
						DnDConstants.ACTION_COPY, dndListener);

	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(final String status) {
		this.statusLabel.setText(status);
	}

	/**
	 * Creates the template.
	 */
	protected void createTemplate() {
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
		if (s instanceof UpdateStatusSignal) {
			this.setStatus(((UpdateStatusSignal) s).getStatus());
		} else if (s instanceof SelectionChangedSignal) {
			String status = "Selected: ";
			final List<FormWidget> sl = ((SelectionChangedSignal) s)
					.getSelection();
			if (sl == null) {
				status += "None";
			} else if (sl.size() == 1) {
				status += sl.get(0).getName();
			} else {
				status += sl.size() + " items";
			}
			this.setStatus(status);
		}
	}

}
