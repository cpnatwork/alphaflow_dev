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
 * $Id: XmlBinder.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.util.logging.Logger;


/**
 * The Class XmlBinder.
 * 
 * 
 * This class provides schema dependent and schema independent methods to bind
 * xml-files on data objects using JAXB.
 */
public class XmlBinder {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(XmlBinder.class.getName());

	/**
	 * Load.
	 * 
	 * @param path
	 *            the path
	 * @param schema
	 *            the schema
	 * @return the object
	 */
	public Object load(final File path, final String schema) {
		return this.load(path.toString(), schema);
	}

	/**
	 * This method binds a xml-file to the schema that is specified by the
	 * second call parameter. The first call parameter contains the path of the
	 * document.
	 * 
	 * @param path
	 *            the path
	 * @param schema
	 *            the schema
	 * @return the object
	 */
	public Object load(final String path, final String schema) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(schema);
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
			return null;
		}
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
		} catch (final FileNotFoundException fnfe) {
			XmlBinder.LOGGER.severe("Error: " + fnfe);
			return null;
		}
		return this.load(fis, jc);
	}

	/**
	 * Load.
	 * 
	 * @param in
	 *            the in
	 * @param schema
	 *            the schema
	 * @return the object
	 */
	public Object load(final InputStream in, final String schema) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(schema);
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
			return null;
		}
		return this.load(in, jc);
	}

	/**
	 * Load.
	 * 
	 * @param path
	 *            the path
	 * @param schema
	 *            the schema
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public Object load(final File path, final Class[] schema) {
		return this.load(path.toString(), schema);
	}

	/**
	 * Load.
	 * 
	 * @param path
	 *            the path
	 * @param schema
	 *            the schema
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public Object load(final String path, final Class[] schema) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(schema);
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
			return null;
		}
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
		} catch (final FileNotFoundException fnfe) {
			XmlBinder.LOGGER.severe("Error: " + fnfe);
			return null;
		}
		return this.load(fis, jc);
	}

	/**
	 * Load.
	 * 
	 * @param in
	 *            the in
	 * @param schema
	 *            the schema
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public Object load(final InputStream in, final Class[] schema) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(schema);
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
			return null;
		}
		return this.load(in, jc);
	}

	/**
	 * Load.
	 * 
	 * @param in
	 *            the in
	 * @param jc
	 *            the jc
	 * @return the object
	 */
	private Object load(final InputStream in, final JAXBContext jc) {
		Object object = null;
		Unmarshaller u;
		try {
			u = jc.createUnmarshaller();
			object = u.unmarshal(in);
			return object;
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
		}
		return null;
	}

	/**
	 * This method stores the object data into the xml-file that is specified by
	 * the call parameters.
	 * 
	 * @param object
	 *            the object
	 * @param path
	 *            the path
	 * @param schema
	 *            the schema
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public boolean store(final Object object, final String path,
			final String schema) throws IOException {
		JAXBContext jc;
		final FileOutputStream fos = new FileOutputStream(path);
		try {
			jc = JAXBContext.newInstance(schema);
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
			return false;
		}
		return this.store(object, fos, jc);
	}

	/**
	 * Store.
	 * 
	 * @param object
	 *            the object
	 * @param path
	 *            the path
	 * @param schema
	 *            the schema
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public boolean store(final Object object, final String path,
			final Class[] schema) throws IOException {
		JAXBContext jc;
		final FileOutputStream fos = new FileOutputStream(path);
		try {
			jc = JAXBContext.newInstance(schema);
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
			return false;
		}
		return this.store(object, fos, jc);
	}

	/**
	 * Store.
	 * 
	 * @param object
	 *            the object
	 * @param out
	 *            the out
	 * @param schema
	 *            the schema
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public boolean store(final Object object, final OutputStream out,
			final String schema) throws IOException {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(schema);
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
			return false;
		}
		return this.store(object, out, jc);
	}

	/**
	 * Store.
	 * 
	 * @param object
	 *            the object
	 * @param out
	 *            the out
	 * @param schema
	 *            the schema
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public boolean store(final Object object, final OutputStream out,
			final Class[] schema) throws IOException {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(schema);
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
			return false;
		}
		return this.store(object, out, jc);
	}

	/**
	 * Store.
	 * 
	 * @param object
	 *            the object
	 * @param out
	 *            the out
	 * @param jc
	 *            the jc
	 * @return true, if successful
	 */
	private boolean store(final Object object, final OutputStream out,
			final JAXBContext jc) {
		boolean status = true;
		Marshaller m;
		try {
			m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(object, out);
		} catch (final JAXBException je) {
			XmlBinder.LOGGER.severe("Error: " + je);
			status = false;
		}
		return status;
	}

	/**
	 * Checks if name of root element is right by trying to unmarshal.
	 * 
	 * @param path
	 *            the path to the xml-file
	 * @param schema
	 *            the schema
	 * @return true, if xml-file has the root element the schema defines
	 */
	public boolean validateRoot(final String path, final String schema) {

		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(schema);
			final FileInputStream fis = new FileInputStream(path);
			final Unmarshaller u = jc.createUnmarshaller();
			u.unmarshal(fis);
		} catch (final JAXBException je) {
			return false;
		} catch (final FileNotFoundException e) {
			XmlBinder.LOGGER.severe("Error: " + e);
			return false;
		}

		return true;
	}
}
