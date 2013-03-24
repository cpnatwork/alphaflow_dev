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
package alpha.props.test.other;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.Test;

import alpha.model.AlphaCardDescriptor;
import alpha.model.cra.Participant;
import alpha.overnet.event.ChangePayloadEvent;

/**
 * The Class ReadChangePayloadFromXMLTest.
 */
public class ReadChangePayloadFromXMLTest {

	/** The classes. */
	@SuppressWarnings("unchecked")
	private final Class[] classes = new Class[] { AlphaCardDescriptor.class,
			ChangePayloadEvent.class, Participant.class };

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(ReadChangePayloadFromXMLTest.class.getName());

	/**
	 * Read.
	 */
	@Test
	public void read() {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(this.classes, null);
			final Unmarshaller unmarshaller = context.createUnmarshaller();
			ReadChangePayloadFromXMLTest.LOGGER
					.info("Setting the unmarshaller....");
			final Object o = unmarshaller.unmarshal(new File(
					"src/test/resources/ChangePayloadEvent.xml"));
			ReadChangePayloadFromXMLTest.LOGGER.info(o.toString());
			Assert.assertNotNull(((ChangePayloadEvent) o).getPayloadContainer()
					.getObj());
			final Object part = ((ChangePayloadEvent) o).getPayloadContainer()
					.getObj();
			ReadChangePayloadFromXMLTest.LOGGER.info(part.toString());
		} catch (final JAXBException e) {
			e.printStackTrace();
		}
	}

}
