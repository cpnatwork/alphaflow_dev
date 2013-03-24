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
package alpha.forms.form.event;

/**
 * The Interface Action.
 */
public interface Action {

	/**
	 * Execute.
	 * 
	 * @param event
	 *            the event
	 */
	public void execute(Event event);

	/**
	 * Creates the memento.
	 * 
	 * @return the action memento
	 */
	public ActionMemento createMemento();

	/**
	 * Sets the memento.
	 * 
	 * @param m
	 *            the new memento
	 */
	public void setMemento(ActionMemento m);
}
