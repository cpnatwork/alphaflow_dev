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
package alpha.forms.form.validation;

import java.util.Map;

import javax.swing.JPanel;

import alpha.forms.memento.model.ValidatorMemento;
import alpha.forms.widget.model.FormWidget;

/**
 * The Interface Validator.
 */
public interface Validator {

	/**
	 * Validate.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public boolean validate(ValidationContext ctx, Object value);

	/**
	 * Gets the option ui.
	 * 
	 * @return the option ui
	 */
	public JPanel getOptionUI();

	/**
	 * Gets the settings map.
	 * 
	 * @return the settings map
	 */
	public Map<String, String> getSettingsMap();

	/**
	 * Checks if is compatible with.
	 * 
	 * @param widgetType
	 *            the widget type
	 * @return true, if is compatible with
	 */
	public boolean isCompatibleWith(Class<? extends FormWidget> widgetType);

	/**
	 * Clone.
	 * 
	 * @return the validator
	 */
	public Validator clone();

	/**
	 * Gets the handle.
	 * 
	 * @return the handle
	 */
	public String getHandle();

	/**
	 * Gets the error.
	 * 
	 * @return the error
	 */
	public String getError();

	/**
	 * Creates the memento.
	 * 
	 * @return the validator memento
	 */
	public ValidatorMemento createMemento();

	/**
	 * Sets the memento.
	 * 
	 * @param m
	 *            the new memento
	 */
	public void setMemento(ValidatorMemento m);
}
