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
package alpha.forms.template.view;

import java.awt.Component;

import javax.swing.JList;

import alpha.forms.designer.view.WidgetPalette;
import alpha.forms.template.model.WidgetTemplate;
import alpha.forms.template.model.WidgetTemplateMngr;

/**
 * The Class TemplatePalette.
 */
public class TemplatePalette extends WidgetPalette {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The list model. */
	private final WidgetTemplateMngr listModel = WidgetTemplateMngr
			.getInstance();

	/**
	 * Instantiates a new template palette.
	 */
	public TemplatePalette() {
		super();
		this.list.setCellRenderer(new TemplateListCellRenderer());
		this.list.setModel(this.listModel);
	}

	/**
	 * The Class TemplateListCellRenderer.
	 */
	protected class TemplateListCellRenderer extends WidgetListCellRenderer {

		/*
		 * (non-Javadoc)
		 * 
		 * @see alpha.forms.designer.view.WidgetPalette.WidgetListCellRenderer#
		 * getListCellRendererComponent(javax.swing.JList, java.lang.Object,
		 * int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(final JList list,
				final Object object, final int index, final boolean isSelected,
				final boolean hasFocus) {
			final WidgetTemplate wt = (WidgetTemplate) object;
			return super.getListCellRendererComponent(list,
					wt.createWidgetFromTemplate(), index, isSelected, hasFocus);
		}
	}

}
