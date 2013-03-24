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
package alpha.forms.util;

import java.io.ByteArrayOutputStream;

/**
 * The listener interface for receiving formSave events. The class that is
 * interested in processing a formSave event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addFormSaveListener<code> method. When
 * the formSave event occurs, that object's appropriate
 * method is invoked.
 * 
 */
public interface FormSaveListener {

	/**
	 * Save.
	 * 
	 * @param form
	 *            the form
	 */
	public void save(ByteArrayOutputStream form);
}
