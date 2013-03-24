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
package alpha.forms.template;

import java.io.InputStream;
import java.io.OutputStream;

import alpha.forms.template.exception.TemplateWithNameExistsException;
import alpha.forms.widget.model.FormWidget;

/**
 * The Interface WidgetTemplateManager.
 */
public interface WidgetTemplateManager {

	/**
	 * Save as template.
	 * 
	 * @param widget
	 *            the widget
	 * @param templateName
	 *            the template name
	 * @param forceOverwrite
	 *            the force overwrite
	 * @throws TemplateWithNameExistsException
	 *             the template with name exists exception
	 */
	public void saveAsTemplate(FormWidget widget, String templateName,
			boolean forceOverwrite) throws TemplateWithNameExistsException;

	/**
	 * Gets the widget from template.
	 * 
	 * @param templateName
	 *            the template name
	 * @return the widget from template
	 */
	public FormWidget getWidgetFromTemplate(String templateName);

	/**
	 * Store templates.
	 * 
	 * @param out
	 *            the out
	 */
	public void storeTemplates(OutputStream out);

	/**
	 * Load templates.
	 * 
	 * @param in
	 *            the in
	 */
	public void loadTemplates(InputStream in);
}
