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
package alpha.facades.exceptions;

/**
 * The Class VerVarStoreException.
 * 
 */
public class VerVarStoreException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3985864107600885210L;

	/**
	 * Instantiates a new ver var store exception.
	 */
	public VerVarStoreException() {

	}

	/**
	 * Instantiates a new ver var store exception.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	public VerVarStoreException(final String arg0) {
		super(arg0);
	}

	/**
	 * Instantiates a new ver var store exception.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	public VerVarStoreException(final Throwable arg0) {
		super(arg0);
	}

	/**
	 * Instantiates a new ver var store exception.
	 * 
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 */
	public VerVarStoreException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

}
