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
package alpha.editor;

/**
 * The Class AlphaEditorException.
 * 
 */
public class AlphaEditorException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4482329848247101L;

	/**
	 * Instantiates a new alpha editor exception.
	 */
	public AlphaEditorException() {

	}

	/**
	 * Instantiates a new alpha editor exception.
	 * 
	 * @param message
	 *            the message
	 */
	public AlphaEditorException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new alpha editor exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public AlphaEditorException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new alpha editor exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public AlphaEditorException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
