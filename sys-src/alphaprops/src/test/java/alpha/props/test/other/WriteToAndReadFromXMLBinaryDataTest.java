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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.util.logging.Logger;

import org.testng.annotations.Test;

import alpha.model.Payload;

/**
 * The Class WriteToAndReadFromXMLBinaryDataTest.
 */
public class WriteToAndReadFromXMLBinaryDataTest {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(WriteToAndReadFromXMLBinaryDataTest.class.getName());

	/** The classes. */
	@SuppressWarnings("unchecked")
	private final Class[] classes = new Class[] { Payload.class };

	// the binary data source
	/** The Constant TESTSOURCE. */
	private final static String TESTSOURCE = "attachments/Result_Report_Anesthesia.pdf";
	// may be checked with diff on some terminal
	/** The Constant TESTDESTINATION. */
	private final static String TESTDESTINATION = "/tmp/Result_Report_Anesthesia.pdf";
	// the marshalled and to be unmarshalled xml file
	/** The Constant TESTFILENAME. */
	private final static String TESTFILENAME = "/tmp/ResultReport.xml";

	/**
	 * Write.
	 */
	@Test
	public void write() {
		try {
			final JAXBContext context = JAXBContext.newInstance(this.classes,
					null);
			final Marshaller marshaller = context.createMarshaller();

			final Payload payload = new Payload();
			// payload.setCra(false);
			// payload.setTsa(false);
			payload.setContent(this.readTestFileAsByteArray());

			marshaller.marshal(payload, new File(
					WriteToAndReadFromXMLBinaryDataTest.TESTFILENAME));

		} catch (final JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read.
	 */
	@Test(dependsOnMethods = { "write" })
	public void read() {
		try {
			final JAXBContext context = JAXBContext.newInstance(this.classes,
					null);
			final Unmarshaller unmarshaller = context.createUnmarshaller();

			final Payload payload = (Payload) unmarshaller.unmarshal(new File(
					WriteToAndReadFromXMLBinaryDataTest.TESTFILENAME));
			WriteToAndReadFromXMLBinaryDataTest.LOGGER.info(payload.toString());
			this.writeByteArrayToFile(payload.getContent());
		} catch (final JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read test file as byte array.
	 * 
	 * @return the byte[]
	 */
	protected byte[] readTestFileAsByteArray() {
		final File file = new File(
				WriteToAndReadFromXMLBinaryDataTest.TESTSOURCE);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final byte[] buffer = new byte[4096];
			int len = 0;

			while ((len = fis.read(buffer)) > 0) {
				baos.write(buffer, 0, len);
			}

			fis.close();
			return baos.toByteArray();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Write byte array to file.
	 * 
	 * @param content
	 *            the content
	 */
	protected void writeByteArrayToFile(final byte[] content) {
		final ByteArrayInputStream bais = new ByteArrayInputStream(content);
		final File file = new File(
				WriteToAndReadFromXMLBinaryDataTest.TESTDESTINATION);
		try {
			final FileOutputStream fos = new FileOutputStream(file);
			final byte[] buffer = new byte[4096];
			int len = 0;

			while ((len = bais.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

			fos.flush();
			fos.close();
			bais.close();

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

}
