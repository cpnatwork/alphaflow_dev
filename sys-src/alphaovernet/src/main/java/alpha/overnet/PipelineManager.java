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
package alpha.overnet;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.pipeline.Pipeline;

/**
 * Provides generic methods for accessing a {@link StatefulKnowledgeSession}.
 */
public interface PipelineManager {

	/**
	 * Creates a new pipeline.
	 * 
	 * @param sfkSession
	 *            the locally running {@link StatefulKnowledgeSession}
	 * @return the created {@link Pipeline}
	 */
	public Pipeline createPipeline(StatefulKnowledgeSession sfkSession);

	/**
	 * Delegates an incoming update into the locally running.
	 * 
	 * @param update
	 *            the update to be delegated {@link StatefulKnowledgeSession}
	 *            using a {@link Pipeline}.
	 */
	public void delegateUpdate(Object update);

}
