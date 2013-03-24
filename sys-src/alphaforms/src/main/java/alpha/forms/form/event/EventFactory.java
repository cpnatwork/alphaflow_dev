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

import alpha.forms.form.util.AlphaFormProvider;
import alpha.forms.widget.model.FormWidget;

/**
 * A factory for creating Event objects.
 */
public class EventFactory {

	/** The factory. */
	protected static EventFactory factory;

	/**
	 * Gets the single instance of EventFactory.
	 * 
	 * @return single instance of EventFactory
	 */
	public static EventFactory getInstance() {
		if (EventFactory.factory == null) {
			EventFactory.factory = new EventFactory();
		}
		return EventFactory.factory;
	}

	/**
	 * Instantiates a new event factory.
	 */
	private EventFactory() {
	}

	/**
	 * Creates a new Event object.
	 * 
	 * @param widget
	 *            the widget
	 * @return the default event
	 */
	public DefaultEvent createDefaultEvent(final FormWidget widget) {
		return new DefaultEvent(widget, AlphaFormProvider.getForm());
	}
}
