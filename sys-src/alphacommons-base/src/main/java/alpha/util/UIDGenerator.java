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
 * $Id: UIDGenerator.java 3579 2012-02-15 10:59:19Z cpn $
 *************************************************************************/
package alpha.util;

import java.util.UUID;

/**
 * Generates UIDs for uniquely identifying process artifacts and other relevant
 * objects from the domain model.
 */
public class UIDGenerator {

	/**
	 * Generates a new UID.
	 * 
	 * @return the generated UID
	 */
	public static String generateUID() {

		// Generate random number as UID
		// return String.valueOf(new RNG().generate());

		// Generate UUID as UID
		return UUID.randomUUID().toString();
	}
}
