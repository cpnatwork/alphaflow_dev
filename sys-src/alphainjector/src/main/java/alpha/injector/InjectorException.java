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
package alpha.injector;

/**
 * The InjectorException is derived from the standard Exception class and can be
 * used accordingly.
 * 
 * 
 */
public class InjectorException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7624515798650667420L;

	/**
	 * Instantiates a new injector exception.
	 */
	public InjectorException() {

	}

	/**
	 * Instantiates a new injector exception.
	 * 
	 * @param message
	 *            the message
	 */
	public InjectorException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new injector exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public InjectorException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new injector exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public InjectorException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
