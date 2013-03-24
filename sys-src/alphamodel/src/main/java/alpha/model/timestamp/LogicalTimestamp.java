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
package alpha.model.timestamp;

/**
 * Represents a generic logical timestamp.
 *
 * @author cpn
 * @version $Id: $
 */
public interface LogicalTimestamp {

	/**
	 * Put an entry (actor, count) into the timestamp. If an entry with the
	 * supplied actor already exists its count is updated. Otherwise the entry
	 * will be created.
	 *
	 * @param actor
	 *            the actor name
	 * @param count
	 *            the count
	 */
	public void putEntry(String actor, long count);

	/**
	 * Gets the total number of modifications conducted on the artifact to which
	 * the timestamp is attached.
	 *
	 * @return the total number of modifications
	 */
	public long getNumberOfModifications();

	/**
	 * Gets the number of modifications conducted by the supplied actor.
	 *
	 * @param actor
	 *            the actor
	 * @return the number of modifications by the supplied actor
	 */
	public long getNumberOfModifications(String actor);

	/**
	 * Gets the distance between two logical timestamps.
	 *
	 * @param ts
	 *            the timestamp
	 * @return the distance between the two timestamps
	 */
	public long getDistance(LogicalTimestamp ts);

	/**
	 * Compares two timestamps.
	 *
	 * @param ts
	 *            the timestamp
	 * @return the causal relation between the two timestamps
	 */
	public Occurrence compare(LogicalTimestamp ts);

	/**
	 * Merges two timestamps into a new one.
	 *
	 * @param ts
	 *            the timestamp
	 * @return the newly created logical timestamp representing the result of
	 *         the merge
	 */
	public LogicalTimestamp merge(LogicalTimestamp ts);

}
