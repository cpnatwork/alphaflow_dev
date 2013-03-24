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

import alpha.forms.widget.model.FormWidget;

/**
 * The Class ValidationFailure.
 */
public class ValidationFailure {

	/** The source. */
	private FormWidget source;

	/** The validator. */
	private Validator validator;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new validation failure.
	 * 
	 * @param source
	 *            the source
	 * @param validator
	 *            the validator
	 */
	public ValidationFailure(final FormWidget source, final Validator validator) {
		super();
		this.source = source;
		this.validator = validator;
	}

	/**
	 * Instantiates a new validation failure.
	 * 
	 * @param source
	 *            the source
	 * @param validator
	 *            the validator
	 * @param message
	 *            the message
	 */
	public ValidationFailure(final FormWidget source,
			final Validator validator, final String message) {
		super();
		this.source = source;
		this.validator = validator;
		this.message = message;
	}

	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public FormWidget getSource() {
		return this.source;
	}

	/**
	 * Sets the source.
	 * 
	 * @param source
	 *            the new source
	 */
	public void setSource(final FormWidget source) {
		this.source = source;
	}

	/**
	 * Gets the validator.
	 * 
	 * @return the validator
	 */
	public Validator getValidator() {
		return this.validator;
	}

	/**
	 * Sets the validator.
	 * 
	 * @param validator
	 *            the new validator
	 */
	public void setValidator(final Validator validator) {
		this.validator = validator;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.source.getName()).append(".")
				.append(this.validator.getHandle());
		sb.append(" (").append(this.source.getClass().getSimpleName())
				.append("): ");
		sb.append(this.message);
		return sb.toString();
	}

}
