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
package alpha.forms.designer.view.popup;

import java.awt.Component;
import java.awt.Point;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import alpha.forms.designer.action.ChangeGridSizeAction;
import alpha.forms.designer.action.DeleteWidgetAction;
import alpha.forms.designer.action.SaveAsTemplateAction;
import alpha.forms.designer.action.ShrinkToFitAction;
import alpha.forms.designer.action.ToggleGridAction;
import alpha.forms.designer.view.FormCanvas;
import alpha.forms.form.AlphaForm;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class FormPopupMenu.
 */
public class FormPopupMenu extends JPopupMenu {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The form. */
	private final AlphaForm form;

	/** The canvas. */
	private final FormCanvas canvas;

	/** The widget. */
	private FormWidget widget;

	/** The delete widget. */
	private final DeleteWidgetAction deleteWidget;

	/** The save template. */
	private final SaveAsTemplateAction saveTemplate;

	/**
	 * Instantiates a new form popup menu.
	 * 
	 * @param form
	 *            the form
	 * @param canvas
	 *            the canvas
	 */
	public FormPopupMenu(final AlphaForm form, final FormCanvas canvas) {
		this.form = form;
		this.canvas = canvas;
		this.deleteWidget = new DeleteWidgetAction("Delete Widget", form,
				canvas);
		final JMenuItem deleteWidgetItem = new JMenuItem(this.deleteWidget);
		this.add(deleteWidgetItem);
		this.saveTemplate = new SaveAsTemplateAction("Save as Template...",
				form, canvas);
		final JMenuItem saveTemplateItem = new JMenuItem(this.saveTemplate);
		this.add(saveTemplateItem);
		this.addSeparator();
		final JMenuItem shrinkForm = new JMenuItem(new ShrinkToFitAction(
				"Shrink To Fit", form, canvas));
		this.add(shrinkForm);
		this.addSeparator();
		final JCheckBoxMenuItem toggleGrid = new JCheckBoxMenuItem(
				new ToggleGridAction("Show Grid", form, canvas));
		this.add(toggleGrid);
		final JMenu gridSizeMenu = new JMenu("Grid Size");
		final ButtonGroup gridSizeGroup = new ButtonGroup();
		final JRadioButtonMenuItem gridSize5 = new JRadioButtonMenuItem(
				new ChangeGridSizeAction("5x5 Pixels", 5, form, canvas));
		gridSizeMenu.add(gridSize5);
		gridSizeGroup.add(gridSize5);
		final JRadioButtonMenuItem gridSize10 = new JRadioButtonMenuItem(
				new ChangeGridSizeAction("10x10 Pixels", 10, form, canvas));
		gridSizeMenu.add(gridSize10);
		gridSizeGroup.add(gridSize10);
		final JRadioButtonMenuItem gridSize15 = new JRadioButtonMenuItem(
				new ChangeGridSizeAction("15x15 Pixels", 15, form, canvas));
		gridSizeMenu.add(gridSize15);
		gridSizeGroup.add(gridSize15);
		final JRadioButtonMenuItem gridSize20 = new JRadioButtonMenuItem(
				new ChangeGridSizeAction("20x20 Pixels", 20, form, canvas));
		gridSizeMenu.add(gridSize20);
		gridSizeGroup.add(gridSize20);
		this.add(gridSizeMenu);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JPopupMenu#show(java.awt.Component, int, int)
	 */
	@Override
	public void show(final Component cmp, final int x, final int y) {
		this.widget = this.canvas.findWidgetDeep(new Point(x, y));

		this.deleteWidget.setWidget(this.widget);
		this.saveTemplate.setEnabled(this.widget != null);
		super.show(cmp, x, y);
	}

}
