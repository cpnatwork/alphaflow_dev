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

import alpha.forms.form.AlphaForm;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class ValidationContextImpl.
 */
public class ValidationContextImpl implements ValidationContext {

	/** The form. */
	private final AlphaForm form;

	/** The widget. */
	private final FormWidget widget;

	/**
	 * Instantiates a new validation context impl.
	 * 
	 * @param form
	 *            the form
	 * @param w
	 *            the w
	 */
	public ValidationContextImpl(final AlphaForm form, final FormWidget w) {
		this.form = form;
		this.widget = w;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.ValidationContext#getForm()
	 */
	@Override
	public AlphaForm getForm() {
		return this.form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.validation.ValidationContext#getWidget(java.lang.String)
	 */
	@Override
	public FormWidget getWidget(final String name) {
		return (this.form == null) ? null : this.form.getWidget(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.ValidationContext#getFormState()
	 */
	@Override
	public String getFormState() {
		return (this.form == null) ? null : this.form.getActiveDocumentState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.ValidationContext#getWidget()
	 */
	@Override
	public FormWidget getWidget() {
		return this.widget;
	}

}
