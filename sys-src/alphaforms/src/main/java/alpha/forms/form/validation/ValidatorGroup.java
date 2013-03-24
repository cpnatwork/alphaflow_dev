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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import alpha.forms.memento.model.ValidatorMemento;

/**
 * The Class ValidatorGroup.
 */
public class ValidatorGroup {

	/** The validators. */
	protected List<Validator> validators = new ArrayList<Validator>();

	/**
	 * Adds the validator.
	 * 
	 * @param v
	 *            the v
	 */
	public void addValidator(final Validator v) {
		this.validators.add(v);
	}

	/**
	 * Removes the validator.
	 * 
	 * @param v
	 *            the v
	 */
	public void removeValidator(final Validator v) {
		this.validators.remove(v);
	}

	/**
	 * Gets the validators.
	 * 
	 * @return the validators
	 */
	public List<Validator> getValidators() {
		return this.validators;
	}

	/**
	 * Validate all.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param value
	 *            the value
	 * @return the sets the
	 */
	public Set<ValidationFailure> validateAll(final ValidationContext ctx,
			final Object value) {
		final Set<ValidationFailure> failures = new HashSet<ValidationFailure>();
		for (final Validator v : this.validators) {
			if (!v.validate(ctx, value)) {
				failures.add(new ValidationFailure(ctx.getWidget(), v, v
						.getError()));
			}
		}
		return failures;
	}

	/**
	 * Contains.
	 * 
	 * @param validator
	 *            the validator
	 * @return true, if successful
	 */
	public boolean contains(final Validator validator) {
		for (final Validator v : this.validators) {
			if (v.getClass().equals(validator.getClass()))
				return true;
		}
		return false;
	}

	/**
	 * Creates the memento.
	 * 
	 * @return the list
	 */
	public List<ValidatorMemento> createMemento() {
		final List<ValidatorMemento> m = new ArrayList<ValidatorMemento>();
		for (final Validator v : this.validators) {
			m.add(v.createMemento());
		}
		return m;
	}

	/**
	 * Sets the memento.
	 * 
	 * @param validatorMementos
	 *            the new memento
	 */
	public void setMemento(final List<ValidatorMemento> validatorMementos) {
		for (final ValidatorMemento m : validatorMementos) {
			System.out.print("findValidator: ");
			final Validator v = ValidatorFactory.findValidator(m.getHandle());
			System.out.println(v);
			this.validators.clear();
			if (v != null) {
				v.setMemento(m);
				this.validators.add(v);
			}
		}
	}

}
