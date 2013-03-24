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

import java.util.ArrayList;
import java.util.List;

import alpha.forms.form.AlphaForm;
import alpha.forms.form.util.AlphaFormProvider;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class DefaultEvent.
 */
public class DefaultEvent implements Event {

	/** The source. */
	protected FormWidget source;

	/** The form. */
	protected AlphaForm form;

	/** The actions. */
	protected List<Action> actions = new ArrayList<Action>();

	/** The is propagation stopped. */
	protected boolean isPropagationStopped = false;

	/**
	 * Instantiates a new default event.
	 * 
	 * @param source
	 *            the source
	 * @param form
	 *            the form
	 */
	public DefaultEvent(final FormWidget source, final AlphaForm form) {
		this.source = source;
		this.form = form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.event.Event#getSource()
	 */
	@Override
	public FormWidget getSource() {
		return this.source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.event.Event#getAlphaForm()
	 */
	@Override
	public AlphaForm getAlphaForm() {
		return AlphaFormProvider.getForm();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.event.Event#stopPropagation()
	 */
	@Override
	public void stopPropagation() {
		this.isPropagationStopped = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.event.Event#addAction(alpha.forms.form.event.Action)
	 */
	@Override
	public void addAction(final Action action) {
		this.actions.add(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.event.Event#removeAction(alpha.forms.form.event.Action)
	 */
	@Override
	public void removeAction(final Action action) {
		this.actions.remove(action);
	}

	/**
	 * Gets the actions.
	 * 
	 * @return the actions
	 */
	public List<Action> getActions() {
		return this.actions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.event.Event#fire()
	 */
	@Override
	public void fire() {
		for (final Action a : this.actions) {
			if (!this.isPropagationStopped) {
				a.execute(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.event.Event#createMemento()
	 */
	@Override
	public EventMemento createMemento() {
		final EventMemento ev = new EventMemento();
		for (final Action a : this.actions) {
			ev.addAction(a.createMemento());
		}
		return ev;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.event.Event#setMemento(alpha.forms.form.event.EventMemento
	 * )
	 */
	@Override
	public void setMemento(final EventMemento m) {
		this.actions.clear();
		for (final ActionMemento am : m.getActions()) {
			final Action a = ActionFactory.getInstance().createScriptedAction();
			a.setMemento(am);
			this.actions.add(a);
		}
	}

}
