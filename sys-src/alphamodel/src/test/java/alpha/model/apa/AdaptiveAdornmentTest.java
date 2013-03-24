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
 * $Id: AdaptiveAdornmentTest.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.model.apa;

import java.util.logging.Logger;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.AdornmentEnumRange;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;

/**
 * The Class AdaptiveAdornmentTest.
 * 
 */
public class AdaptiveAdornmentTest {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AdaptiveAdornmentTest.class.getName());

	/** The my dyn ad. */
	private PrototypedAdornment myDynAd;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	public void setUp() throws Exception {
		this.myDynAd = new PrototypedAdornment();
		AdaptiveAdornmentTest.LOGGER
				.finer("***** errors in debug output are no indicator for failure *****");
	}

	/**
	 * Test constructor and getter.
	 */
	@Test
	public void testConstructorAndGetter() {
		this.myDynAd = new PrototypedAdornment("Dynamic Test Adornment",
				ConsensusScope.DOMAIN_STD, AdornmentDataType.STRING);
		this.myDynAd.setValue("short description text");
		AssertJUnit.assertEquals("Get Adornment Type",
				this.myDynAd.getDataType(), AdornmentDataType.STRING);
		AssertJUnit.assertEquals("Get Adornment Name", this.myDynAd.getName(),
				"Dynamic Test Adornment");
		AssertJUnit.assertEquals("Get Adornment Value",
				this.myDynAd.getValue(), "short description text");
	}

	/**
	 * Test constructor and getter2.
	 */
	@Test
	public void testConstructorAndGetter2() {
		final String[] s = { "FOO", "BAR" };
		this.myDynAd = new PrototypedAdornment("Test Dynamic Enum",
				ConsensusScope.DOMAIN_STD, AdornmentDataType.ENUM);
		this.myDynAd.setEnumRange(new AdornmentEnumRange(s));
		this.myDynAd.setValue("FOO");
		AssertJUnit.assertEquals("Get Adornment Type",
				this.myDynAd.getDataType(), AdornmentDataType.ENUM);
		AssertJUnit.assertEquals("Get Adornment Name", this.myDynAd.getName(),
				"Test Dynamic Enum");
		AssertJUnit.assertEquals("Get Adornment Value",
				this.myDynAd.getValue(), "FOO");
	}

	/**
	 * Test setter.
	 */
	@Test
	public void testSetter() {
		this.myDynAd.setDataType(AdornmentDataType.INTEGER);
		AssertJUnit.assertEquals("Set Adornment Type",
				this.myDynAd.getDataType(), AdornmentDataType.INTEGER);
		this.myDynAd.setName("Dynamic Number Adornment");
		AssertJUnit.assertEquals("Set Adornment Name", this.myDynAd.getName(),
				"Dynamic Number Adornment");
		this.myDynAd.setValue("37.9");
		AssertJUnit.assertEquals("Set Adornment Value", null,
				this.myDynAd.getValue());
	}

	/**
	 * Test validation.
	 */
	@Test
	public void testValidation() {
		final PrototypedAdornment myDynAd2 = new PrototypedAdornment(
				"Dynamic Date Adornment", ConsensusScope.DOMAIN_STD,
				AdornmentDataType.TIMESTAMP);
		myDynAd2.setValue("11.01.1999 00:00"); // gets accepted

		myDynAd2.setValue("12.13.1999 00:00"); // wrong: month
		AssertJUnit.assertEquals("Set invalid Date", "11.01.1999 00:00",
				myDynAd2.getValue());
		myDynAd2.setValue("11.01.2000"); // wrong: HH:mm is required
		AssertJUnit.assertEquals("Set invalid Date", "11.01.1999 00:00",
				myDynAd2.getValue());

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
