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
package alpha.forms.form.naming;

import java.util.HashMap;
import java.util.Map;

import alpha.forms.template.model.WidgetTemplate;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class WidgetNameGenerator.
 */
public class WidgetNameGenerator {

	/** The nums. */
	private static Map<String, Integer> nums = new HashMap<String, Integer>();

	/**
	 * Gets the name.
	 * 
	 * @param w
	 *            the w
	 * @return the name
	 */
	public static String getName(final FormWidget w) {
		return WidgetNameGenerator.getName(w.getClass());
	}

	/**
	 * Gets the name.
	 * 
	 * @param clazz
	 *            the clazz
	 * @return the name
	 */
	public static String getName(final Class<? extends FormWidget> clazz) {
		final String className = clazz.getSimpleName();
		Integer num = WidgetNameGenerator.nums.get(className);
		if (num == null) {
			num = new Integer(1);
		}
		final String name = className + num.toString();
		WidgetNameGenerator.nums.put(className, ++num);
		return name;
	}

	/**
	 * Gets the name.
	 * 
	 * @param template
	 *            the template
	 * @return the name
	 */
	public static String getName(final WidgetTemplate template) {
		final String templName = template.getTemplateName();
		Integer num = WidgetNameGenerator.nums.get(templName);
		if (num == null) {
			num = new Integer(1);
		}
		final String name = templName + num.toString();
		WidgetNameGenerator.nums.put(templName, ++num);
		return name;
	}
}
