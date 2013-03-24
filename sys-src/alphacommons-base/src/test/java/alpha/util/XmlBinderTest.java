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
 * $Id: XmlBinderTest.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.util;

import java.io.IOException;
import java.net.URL;

import java.util.logging.Logger;

import org.testng.annotations.Test;

/**
 * The Class XmlBinderTest.
 * 
 */
public class XmlBinderTest {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(XmlBinderTest.class.getName());

	/** The xmlb. */
	XmlBinder xmlb = null;

	/**
	 * Test method for
	 * {@link alpha.util.XmlBinder#store(Object, String, String)}.
	 */
	@Test
	public final void testStoreXml() {
		final String file = "alphaDoc.xml";
		final URL url = this.getClass().getClassLoader().getResource(file);
		// xmlb = new XmlBinder("alphaDoc.xml");
		this.xmlb = new XmlBinder();
		try {
			this.xmlb.store(this.xmlb.load(url.getFile(), "alpha.model"),
					url.getFile(), "alpha.model");
		} catch (final IOException e) {
			XmlBinderTest.LOGGER.severe("IOError:" + e);
		}
	}

	/**
	 * Test method for {@link alpha.util.XmlBinder#load(String, String)}.
	 */
	@Test
	public final void testReadXml() {
		final String file = "alphaDoc.xml";
		final URL url = this.getClass().getClassLoader().getResource(file);
		this.xmlb = new XmlBinder();
		System.out.println(url);
		this.xmlb.load(url.getFile(), "alpha.model");
	}

	// /**
	// * Test method for {@link alpha.util.XmlBinder#store()}.
	// */
	// @Test
	// public final void testStoreTsaPayload() {
	// String file = "psa.xml";
	// URL url = this.getClass().getClassLoader().getResource(file);
	// xmlb = new XmlBinder();
	// try {
	// xmlb.store(xmlb.load(url.getFile(), CoordCardType.PSA.getModel()), url
	// .getFile(), CoordCardType.PSA.getModel());
	// } catch (IOException e) {
	// LOGGER.severe("IOError:" +e);
	// }
	// }

	// /**
	// * Test method for {@link alpha.util.XmlBinder#store()}.
	// */
	// @Test
	// public final void testStoreCraPayload() {
	// String file = "cra.xml";
	// URL url = this.getClass().getClassLoader().getResource(file);
	// xmlb = new XmlBinder();
	// try {
	// xmlb.store(xmlb.load(url.getFile(), CoordCardType.CRA.getModel()), url
	// .getFile(), CoordCardType.CRA.getModel());
	// } catch (IOException e) {
	// LOGGER.severe("IOError:" +e);
	// }
	// }

	// /**
	// * Test method for {@link alpha.util.XmlBinder#load()}.
	// */
	// @Test
	// public final void testLoadTsaPayload() {
	// String file = "psa.xml";
	// URL url = this.getClass().getClassLoader().getResource(file);
	// xmlb = new XmlBinder();
	// xmlb.load(url.getFile(), CoordCardType.PSA.getModel());
	// }

	// /**
	// * Test method for {@link alpha.util.XmlBinder#load()}.
	// */
	// @Test
	// public final void testLoadCraPayload() {
	// String file = "cra.xml";
	// URL url = this.getClass().getClassLoader().getResource(file);
	// xmlb = new XmlBinder();
	// xmlb.load(url.getFile(), CoordCardType.CRA.getModel());
	// }
}
