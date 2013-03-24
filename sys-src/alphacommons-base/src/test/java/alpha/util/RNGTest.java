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
 * $Id: RNGTest.java 3579 2012-02-15 10:59:19Z cpn $
 *************************************************************************/
package alpha.util;

import org.testng.annotations.Test;

/**
 * The Class RNGTest.
 * 
 */
public class RNGTest {

	/** The rng. */
	private RNG rng = null;

	/**
	 * Test method for {@link alpha.util.RNG#RNG()}.
	 */
	@Test
	public final void testRNG() {
		this.rng = new RNG();
	}

	/**
	 * Test method for {@link alpha.util.RNG#RNG(long)}.
	 */
	@Test
	public final void testRNGLong() {
		this.rng = new RNG(4711);
	}

	/**
	 * Test method for {@link alpha.util.RNG#generate()}.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public final void testGenerate() throws Exception {
		this.rng = new RNG();
		final int rn = this.rng.generate();
		if (rn < 0)
			throw new Exception("Generated number in invalid range");
	}

}
