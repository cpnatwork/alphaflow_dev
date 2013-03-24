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
 * $Id: ClassObjectFilter.java 3580 2012-02-15 11:12:32Z cpn $
 *************************************************************************/
package alpha.commons.drools;

import org.drools.ObjectFilter;

/**
 * Filters Objects by Class, only accepting Classes of the specified type.
 * 
 * 
 */
public class ClassObjectFilter implements ObjectFilter {

	/** The clazz. */

	@SuppressWarnings("unchecked")
	private final Class clazz;

	/**
	 * The Allowed Class type.
	 * 
	 * @param clazz
	 *            the clazz
	 */
	@SuppressWarnings("unchecked")
	public ClassObjectFilter(final Class clazz) {
		this.clazz = clazz;
	}

	/**
	 * Returning true means the Iterator accepts, and thus returns, the current
	 * Object's Class type.
	 * 
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean accept(final Object object) {
		return this.clazz.isAssignableFrom(object.getClass());
	}

}
