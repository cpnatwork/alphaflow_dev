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
 * $Id: ResultHandlerImpl.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.commons.drools;

import org.drools.runtime.pipeline.ResultHandler;
import java.util.logging.Logger;


/**
 * This class defines a {@link ResultHandler} implementation, needed for the
 * insertion of objects into the pipeline.
 * 
 * 
 */
public class ResultHandlerImpl implements ResultHandler {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(ResultHandlerImpl.class.getName());

	/** The object. */
	protected Object object;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.drools.runtime.pipeline.ResultHandler#handleResult(java.lang.Object)
	 */
	@Override
	public void handleResult(final Object object) {
		ResultHandlerImpl.LOGGER.finer("handle incoming object: "
				+ object.getClass());
		this.object = object;
	}

	/**
	 * Gets the object.
	 * 
	 * @return the object
	 */
	public Object getObject() {
		return this.object;
	}
}
