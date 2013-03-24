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
package alpha.forms;

import java.io.InputStream;
import java.util.Collection;

import alpha.forms.application.view.AlphaFormsView;
import alpha.forms.util.FormSaveListener;

/**
 * The Interface AlphaFormsFacade.
 */
public interface AlphaFormsFacade {

	/**
	 * Start.
	 * 
	 * @return the alpha forms view
	 */
	public AlphaFormsView start();

	/**
	 * Start.
	 * 
	 * @param form
	 *            the form
	 * @return the alpha forms view
	 */
	public AlphaFormsView start(InputStream form);

	/**
	 * Gets the view.
	 * 
	 * @return the view
	 */
	public AlphaFormsView getView();

	/**
	 * Register save listener.
	 * 
	 * @param l
	 *            the l
	 */
	public void registerSaveListener(FormSaveListener l);

	/**
	 * Sets the document states.
	 * 
	 * @param states
	 *            the new document states
	 */
	public void setDocumentStates(Collection<String> states);

	/**
	 * Adds the document state.
	 * 
	 * @param state
	 *            the state
	 */
	public void addDocumentState(String state);

}
