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
 * $Id: LogicalTimestampHistoryUtility.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.offsync.time;

import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;

/**
 * Provides a generic interface for inserting incoming.
 * 
 * {@link AlphaCardDescriptor} and {@link Payload} objects into the locally
 * maintained version history.
 */
public interface LogicalTimestampHistoryUtility {

	/**
	 * Insert the supplied {@link AlphaCardDescriptor} and {@link Payload} into
	 * the locally maintained version history. After insertion the current local
	 * version of the inserted {@link AlphaCardDescriptor} is returned.
	 * 
	 * @param incomingAcd
	 *            the incoming {@link AlphaCardDescriptor}
	 * @param payload
	 *            the incoming {@link Payload}
	 * @return the current local version of the inserted
	 * @throws Exception
	 *             indicates that an error was encountered during insertion
	 *             {@link AlphaCardDescriptor}
	 */
	public AlphaCardDescriptor insertIntoHistory(
			AlphaCardDescriptor incomingAcd, Payload payload) throws Exception;

}
