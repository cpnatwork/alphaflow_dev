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

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.container.ContainerWidget;

/**
 * The Class ScriptedAction.
 */
public class ScriptedAction implements Action {

	/** The script code. */
	protected String scriptCode = "";

	/** The js engine. */
	protected ScriptEngine jsEngine;

	/**
	 * Instantiates a new scripted action.
	 * 
	 * @param jsEngine
	 *            the js engine
	 */
	public ScriptedAction(final ScriptEngine jsEngine) {
		this.jsEngine = jsEngine;
	}

	/**
	 * Gets the script code.
	 * 
	 * @return the script code
	 */
	public String getScriptCode() {
		return this.scriptCode;
	}

	/**
	 * Sets the script code.
	 * 
	 * @param scriptCode
	 *            the new script code
	 */
	public void setScriptCode(final String scriptCode) {
		this.scriptCode = scriptCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.event.Action#execute(alpha.forms.form.event.Event)
	 */
	@Override
	public void execute(final Event event) {
		final String funcName = "action_" + this.hashCode();
		for (final FormWidget w : event.getAlphaForm().getWidgets()) {
			this.jsEngine.put(w.getName(), w);
			if (w instanceof ContainerWidget) {
				final ContainerWidget co = (ContainerWidget) w;
				for (final FormWidget cw : co.getChildren()) {
					this.jsEngine.put(w.getName() + "$" + cw.getName(), cw);
				}
			}
		}
		if (this.jsEngine != null) {
			try {
				final StringBuilder sb = new StringBuilder();
				// afe__execute is a wrapper to make "this" point to the widget
				// in the action's javascript.
				// ctx is the AlphaForm
				sb.append("function afe__execute(funcName, widget, context) {"
						+ "this[funcName].call(widget, context);" + "}");
				sb.append("function " + funcName + "(ctx) {" + this.scriptCode
						+ "}");
				this.jsEngine.eval(sb.toString());
				((Invocable) this.jsEngine).invokeFunction("afe__execute",
						funcName, event.getSource(), event.getAlphaForm());
			} catch (final ScriptException e) {
				e.printStackTrace();
			} catch (final NoSuchMethodException e) {
				e.printStackTrace();
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "action_" + this.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.event.Action#createMemento()
	 */
	@Override
	public ActionMemento createMemento() {
		final ActionMemento m = new ActionMemento();
		m.setName(this.toString());
		m.setScriptCode(this.scriptCode);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.event.Action#setMemento(alpha.forms.form.event.ActionMemento
	 * )
	 */
	@Override
	public void setMemento(final ActionMemento m) {
		this.scriptCode = m.getScriptCode();
	}

}
