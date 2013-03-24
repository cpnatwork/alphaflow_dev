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
 * $Id: Occurrence.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.timestamp;

/**
 * Contains all possibilities for relationships between two events occurring in
 * a distributed system.
 *
 * @author cpn
 * @version $Id: Occurrence.java 3583 2012-02-16 01:52:45Z cpn $
 */
public enum Occurrence {

	/** One event precedes another one. */
	PRECEDING,

	/** One event follows another one. */
	FOLLOWING,

	/** Both events happen at the same logical time. */
	CONCURRENT,

	/** Both events are identical. */
	IDENTICAL,

	/** Causal relationship could not be determined *. */
	UNDEFINED
}
