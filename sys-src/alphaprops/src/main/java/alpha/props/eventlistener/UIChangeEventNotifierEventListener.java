/**************************************************************************
 * alpha-Flow: distributed case files in form of active documents
 * (supporting knowledge-driven ad-hoc processes in healthcare)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
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
package alpha.props.eventlistener;

import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.ObjectRetractedEvent;
import org.drools.event.rule.ObjectUpdatedEvent;
import java.util.logging.Logger;


import alpha.overnet.event.ObservableEvent;

/**
 * Checks if event holds object, which has to be sent through update within
 * observers, only if the object is annotated with {@link ObservableEvent}.
 * 
 */
public class UIChangeEventNotifierEventListener extends UINotifierEventListener {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(UIChangeEventNotifierEventListener.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.drools.event.rule.WorkingMemoryEventListener#objectInserted(org.drools
	 * .event.rule.ObjectInsertedEvent)
	 */
	@Override
	public void objectInserted(final ObjectInsertedEvent event) {
		UIChangeEventNotifierEventListener.LOGGER.finer("---INSERT: "
				+ event.getObject().toString() + "\n");
		if (event.getObject().getClass()
				.isAnnotationPresent(ObservableEvent.class)) {
			this.setChanged();
			this.notifyObservers(event.getObject());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.drools.event.rule.WorkingMemoryEventListener#objectRetracted(org.
	 * drools.event.rule.ObjectRetractedEvent)
	 */
	@Override
	public void objectRetracted(final ObjectRetractedEvent event) {
		UIChangeEventNotifierEventListener.LOGGER.finer("---RETRACT: "
				+ event.getOldObject().toString() + "\n");
		if (event.getOldObject().getClass()
				.isAnnotationPresent(ObservableEvent.class)) {
			this.setChanged();
			this.notifyObservers(event.getOldObject());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.drools.event.rule.WorkingMemoryEventListener#objectUpdated(org.drools
	 * .event.rule.ObjectUpdatedEvent)
	 */
	@Override
	public void objectUpdated(final ObjectUpdatedEvent event) {
		UIChangeEventNotifierEventListener.LOGGER.finer("---UPDATE: "
				+ event.getObject().getClass() + "\n");
		if (event.getObject().getClass()
				.isAnnotationPresent(ObservableEvent.class)) {
			this.setChanged();
			this.notifyObservers(event.getObject());
		}
	}
}
