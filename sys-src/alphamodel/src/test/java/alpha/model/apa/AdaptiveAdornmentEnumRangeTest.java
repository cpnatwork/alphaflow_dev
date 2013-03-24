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
 * $Id: AdaptiveAdornmentEnumRangeTest.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.model.apa;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import alpha.model.adornment.AdornmentEnumRange;

/**
 * The Class AdaptiveAdornmentEnumRangeTest.
 */
public class AdaptiveAdornmentEnumRangeTest {

	/** The my adaptive adornment enum range. */
	private AdornmentEnumRange myAdaptiveAdornmentEnumRange;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	public void setUp() throws Exception {
		this.myAdaptiveAdornmentEnumRange = new AdornmentEnumRange(
				new String[] { "foo", "Bar", "true", "FALSE" });
	}

	// @Test
	// public void testConstructor() {
	//
	// }

	/**
	 * Test values.
	 */
	@Test
	public void testValues() {
		AssertJUnit.assertEquals("All Values", "[FOO, BAR, TRUE, FALSE]",
				this.myAdaptiveAdornmentEnumRange.values().toString());
	}

	/**
	 * Test exists.
	 */
	@Test
	public void testExists() {
		AssertJUnit.assertTrue(this.myAdaptiveAdornmentEnumRange.exists("fOo"));
		AssertJUnit.assertFalse(this.myAdaptiveAdornmentEnumRange
				.exists("HELLO"));
	}

	/**
	 * Test value of.
	 */
	@Test
	public void testValueOf() {
		AssertJUnit.assertEquals("BAR",
				this.myAdaptiveAdornmentEnumRange.valueOf("Bar"));
	}

	/**
	 * Test ordinal.
	 */
	@Test
	public void testOrdinal() {
		AssertJUnit
				.assertTrue(this.myAdaptiveAdornmentEnumRange.ordinal("bar") == 1);
		AssertJUnit.assertFalse(this.myAdaptiveAdornmentEnumRange
				.ordinal("false") == 4);
	}

	/**
	 * Test range.
	 */
	@Test
	public void testRange() {
		AssertJUnit.assertEquals("[BAR, TRUE]",
				this.myAdaptiveAdornmentEnumRange.range("bar", "trUe")
						.toString());
	}

	/**
	 * Test compare.
	 */
	@Test
	public void testCompare() {
		AssertJUnit.assertTrue(this.myAdaptiveAdornmentEnumRange.compare("bar",
				"BAR") == 0);
		AssertJUnit.assertTrue(this.myAdaptiveAdornmentEnumRange.compare("foo",
				"BAR") > 0);
		AssertJUnit.assertTrue(this.myAdaptiveAdornmentEnumRange.compare(
				"false", "foo") < 0);
	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@AfterMethod
	public void tearDown() throws Exception {
	}

}
