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
import java.util.List;

/**
 * A factory for creating Validator objects.
 */
public class ValidatorFactory {

	/** The validator list. */
	protected static List<Validator> validatorList = new ArrayList<Validator>();

	/**
	 * Setup.
	 */
	public static void setup() {
		ValidatorFactory.validatorList.add(new NotNullValidator());
		ValidatorFactory.validatorList.add(new NumberValidator());
	}

	/**
	 * Gets the available validators.
	 * 
	 * @return the available validators
	 */
	public static List<Validator> getAvailableValidators() {
		final List<Validator> vs = new ArrayList<Validator>();
		for (final Validator v : ValidatorFactory.validatorList) {
			vs.add(v.clone());
		}
		return vs;
	}

	/**
	 * Find validator.
	 * 
	 * @param handle
	 *            the handle
	 * @return the validator
	 */
	public static Validator findValidator(final String handle) {
		for (final Validator v : ValidatorFactory.validatorList) {
			if (v.getHandle().equals(handle))
				return v.clone();
		}
		return null;
	}
}
