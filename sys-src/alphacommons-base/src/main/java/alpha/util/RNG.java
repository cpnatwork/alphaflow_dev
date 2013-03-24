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
 * $Id: RNG.java 3579 2012-02-15 10:59:19Z cpn $
 *************************************************************************/
package alpha.util;

import java.util.Random;

/**
 * This class is used to create pseudo random numbers utilizing the Random
 * class.
 * 
 */
public class RNG {

	/** The ran. */
	private Random ran = null;

	/**
	 * default constructor creates a Random instance without a seed.
	 */
	public RNG() {
		this.ran = new Random();
	}

	/**
	 * constructor creates a Random instance with a seed.
	 * 
	 * @param seed
	 *            the seed
	 */
	public RNG(final long seed) {
		this.ran = new Random(seed);
	}

	/**
	 * The method generate returns a positive pseudo random number.
	 * 
	 * @return the int
	 */
	public int generate() {
		int rn = this.ran.nextInt();
		rn = rn < 0 ? -rn : rn;
		return rn;
	}
}
