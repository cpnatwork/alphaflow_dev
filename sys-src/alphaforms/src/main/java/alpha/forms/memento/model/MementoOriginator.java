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
package alpha.forms.memento.model;

/**
 * The Interface MementoOriginator.
 */
public interface MementoOriginator {

	/**
	 * Creates the widget memento.
	 * 
	 * @return the widget memento
	 */
	public WidgetMemento createWidgetMemento();

	/**
	 * Sets the widget memento.
	 * 
	 * @param m
	 *            the new widget memento
	 */
	public void setWidgetMemento(WidgetMemento m);

	/**
	 * Creates the dynamic attribute memento.
	 * 
	 * @param ref
	 *            the ref
	 * @return the dynamic attribute memento
	 */
	public DynamicAttributeMemento createDynamicAttributeMemento(
			WidgetMemento ref);

	/**
	 * Sets the dynamic memento.
	 * 
	 * @param m
	 *            the new dynamic memento
	 */
	public void setDynamicMemento(DynamicAttributeMemento m);

	/**
	 * Creates the value memento.
	 * 
	 * @return the value memento
	 */
	public ValueMemento createValueMemento();

	/**
	 * Sets the value memento.
	 * 
	 * @param m
	 *            the new value memento
	 */
	public void setValueMemento(ValueMemento m);
}
