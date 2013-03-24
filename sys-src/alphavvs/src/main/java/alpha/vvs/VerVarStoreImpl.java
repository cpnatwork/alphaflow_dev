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
package alpha.vvs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.logging.Logger;


import alpha.facades.AlphaPropsFacade;
import alpha.facades.VerVarStoreFacade;
import alpha.facades.exceptions.VerVarStoreException;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.APAPayload;
import alpha.model.cra.CRAPayload;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.PSAPayload;
import alpha.util.XmlBinder;

/**
 * This class supplies methods to load and store documents from/in the file
 * system. Moreover it supplies a buffer that contains the object representation
 * of every Payload document, that was loaded during the lifetime of this class.
 * 
 * 
 */
public class VerVarStoreImpl implements VerVarStoreFacade {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(VerVarStoreImpl.class.getName());

	/** The sptype. */
	private String sptype;

	/** The version. */
	private String version = "1.0";

	/** The home path. */
	private String homePath = "";

	/** The memory-cached Payload objects. */
	protected LinkedHashMap<AlphaCardID, Payload> memoryCache = null;

	// FIXME: use Spring dependency injection (eliminate from constructor)
	/** The apf. */
	private AlphaPropsFacade apf = null;

	/**
	 * This constructor initializes vvs and gets homePath as a parameter.
	 * 
	 * @param homePath
	 *            the home path
	 * @param apf
	 *            the apf
	 */
	public VerVarStoreImpl(final String homePath, final AlphaPropsFacade apf) {
		this.memoryCache = new LinkedHashMap<AlphaCardID, Payload>();
		this.apf = apf;
		// FIXME: remove homePath as config-param
		this.homePath = homePath;
	}

	/**
	 * Constructor for Test-classes, only.
	 * <p>
	 * FIXME: It should be possible to eliminate usage of this constructor.
	 * 
	 * @param homePath
	 *            the home path
	 */
	@Deprecated
	VerVarStoreImpl(final String homePath) {
		this.memoryCache = new LinkedHashMap<AlphaCardID, Payload>();
		// FIXME: remove homePath as config-param
		this.homePath = homePath;
		this.sptype = "xml";
	}

	/**
	 * This method stores the Payload object to the path created by homePath,
	 * aci and the value of version from the constructor.
	 * 
	 * @param aci
	 *            the aci
	 * @param payload
	 *            the payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	@Override
	public void store(final AlphaCardID aci, final Payload payload)
			throws VerVarStoreException {
		String filename = "";
		if (payload.getContent() != null) {
			final byte[] content = payload.getContent();
			try {
				filename = this.homePath + "/" + aci.getEpisodeID() + "/"
						+ aci.getCardID() + "/pl/Payload." + this.sptype;
				final File file = new File(filename);
				if (file.getParentFile().mkdirs() == false) {
					VerVarStoreImpl.LOGGER.warning("Unable to create directory "
							+ file.getParentFile());
				}
				if (file.createNewFile() == false) {
					VerVarStoreImpl.LOGGER.warning("The file " + file
							+ " already exists.");
				}
				final FileOutputStream fos = new FileOutputStream(filename);
				fos.write(content);
				fos.flush();
				fos.close();

			} catch (final IOException e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"FAIL: " + e.toString());
				VerVarStoreImpl.LOGGER.severe("Error: " + e);
				throw new VerVarStoreException(e);
			}
		} else if ((payload instanceof PSAPayload)
				|| (payload instanceof CRAPayload)
				|| (payload instanceof APAPayload)) {
			final CoordCardType type = CoordCardType.fromValue(aci.getCardID()
					.substring(1));
			filename = this.homePath + "/" + aci.getEpisodeID() + "/"
					+ aci.getCardID() + "/pl/Payload" + ".xml";
			final File file = new File(filename);
			if (file.getParentFile().mkdirs() == false) {
				VerVarStoreImpl.LOGGER.warning("Unable to create directory "
						+ file.getParentFile());
			}
			final XmlBinder xmlb = new XmlBinder();
			try {
				xmlb.store(payload, filename, type.getModel());
			} catch (final IOException e) {
				VerVarStoreImpl.LOGGER.severe("IOError:" + e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.VerVarStoreFacade#store(alpha.model.AlphaCardDescriptor)
	 */
	@Override
	public void store(final AlphaCardDescriptor acd) {

		final XmlBinder xb = new XmlBinder();
		try {

			final String filename = this.homePath + acd.getId().getEpisodeID()
					+ "/" + acd.getId().getCardID() + "/desc/Descriptor.xml";
			final File dir = (new File(filename)).getParentFile();

			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					VerVarStoreImpl.LOGGER.info("Cannot create directory "
							+ dir.getAbsolutePath()
							+ ". Serialization will fail.");
				}
			}
			xb.store(acd, filename, "alpha.model");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method loads a Payload specified by aci.
	 * 
	 * @param aci
	 *            the aci
	 * @return Payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	@Override
	public Payload load(final AlphaCardID aci) throws VerVarStoreException {
		return this.load(aci, null);
	}

	/**
	 * This method loads a Payload specified by aci, version.
	 * 
	 * @param aci
	 *            the aci
	 * @param ver
	 *            the ver
	 * @return Payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	@Override
	public Payload load(final AlphaCardID aci, final String ver)
			throws VerVarStoreException {
		if (ver != null) {
			this.version = ver;
		}
		if (this.sptype == null)
			return null;
		Payload payload = null;

		final String path = this.homePath + "/" + aci.getEpisodeID() + "/"
				+ aci.getCardID() + "/pl/Payload." + this.sptype;

		final File pl = new File(path);

		if (aci.getCardID().equals(CoordCardType.PSA.id())
				|| aci.getCardID().equals(CoordCardType.CRA.id())
				|| aci.getCardID().equals(CoordCardType.APA.id())) {

			final CoordCardType type = CoordCardType.fromValue(aci.getCardID()
					.substring(1));
			final XmlBinder xmlb = new XmlBinder();
			payload = (Payload) xmlb.load(
					this.homePath + "/" + aci.getEpisodeID() + "/"
							+ aci.getCardID() + "/pl" + "/" + "Payload"
							+ ".xml", type.getModel());
		} else {
			payload = new Payload();
			FileInputStream fis = null;
			byte[] content = null;

			try {
				content = new byte[(int) pl.length()];
				fis = new FileInputStream(pl);
				fis.read(content);
				fis.close();
			} catch (final FileNotFoundException fnfe) {
				JOptionPane.showMessageDialog(new JFrame(),
						"FAIL: " + fnfe.toString());
				VerVarStoreImpl.LOGGER.severe("Error: " + fnfe);
				throw new VerVarStoreException(fnfe);
			} catch (final IOException ioe) {
				JOptionPane.showMessageDialog(new JFrame(),
						"FAIL: " + ioe.toString());
				VerVarStoreImpl.LOGGER.severe("Error: " + ioe);
				throw new VerVarStoreException(ioe);
			}
			payload.setContent(content);

		}
		this.memoryCache.put(aci, payload);
		return payload;
	}

	/**
	 * This method gets a Payload instance specified by the aci from the
	 * internal buffer.
	 * 
	 * @param aci
	 *            the aci
	 * @return the payload
	 */
	@Override
	public Payload getPayload(final AlphaCardID aci) {
		return this.memoryCache.get(aci);
	}

	/**
	 * This method puts a Payload instance into the internal buffer.
	 * 
	 * @param aci
	 *            the aci
	 * @param payload
	 *            the payload
	 * @throws VerVarStoreException
	 *             the ver var store exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.VerVarStoreFacade#putPayload(alpha.model.identification
	 * .AlphaCardIdentifier, alpha.model.payload.Payload)
	 */
	@Override
	public void putPayload(final AlphaCardID aci, final Payload payload)
			throws VerVarStoreException {
		this.memoryCache.put(aci, payload);
		this.store(aci, payload);
		try {
			// FIXME: CPN does not understand the next line (seems to be a hack)
			// +-> why do we (re-)store the descriptor on payload change?
			// +-> the only putPayload(..) references come from the DRL-file
			this.store(this.apf.getAlphaCard(aci));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VerVarStoreImpl [memoryCache=" + this.memoryCache + "]";
	}

	/**
	 * Gets the sptype.
	 * 
	 * @return the sptype
	 */
	public String getSptype() {
		return this.sptype;
	}

	/**
	 * Setter for sptype.
	 * 
	 * @param sptype
	 *            the sptype to set
	 */
	@Override
	@Deprecated
	public void setSptype(final String sptype) {
		this.sptype = sptype;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * Setter for version.
	 * 
	 * @param version
	 *            the version to set
	 */
	@Override
	@Deprecated
	public void setVersion(final String version) {
		this.version = version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.VerVarStoreFacade#store(alpha.model.identification.AlphaCardID
	 * , alpha.model.Payload, java.lang.String)
	 */
	@Override
	public void store(final AlphaCardID aci, final Payload payload,
			final String version) throws VerVarStoreException {
		/*
		 * mutmasslich liesse sich dies per setzen von this.version auf version
		 * erledigen, plus Delegation an store(A.C.Id, Payload)!? Aber
		 * eigentlich muss die Verwendung von this.verision ganz eleminiert
		 * werden.
		 */
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * ** ADDED FOR COMPILING - NULL IMPLEMENATIONS
	 * ********************************.
	 * 
	 * @param vvs
	 *            the vvs
	 */

	public void setVvs(final LinkedHashMap<AlphaCardID, Payload> vvs) {
	}

	/**
	 * Gets the vvs.
	 * 
	 * @return the vvs
	 */
	public LinkedHashMap<AlphaCardID, Payload> getVvs() {
		return new LinkedHashMap<AlphaCardID, Payload>();
	}

	/**
	 * Load.
	 * 
	 * @param aci
	 *            the aci
	 * @param s1
	 *            the s1
	 * @param s2
	 *            the s2
	 * @return the payload
	 */
	public Payload load(final AlphaCardID aci, final String s1, final String s2) {
		return new Payload();
	}

	/**
	 * Store.
	 * 
	 * @param aci
	 *            the aci
	 * @param p
	 *            the p
	 * @param s1
	 *            the s1
	 * @param s2
	 *            the s2
	 */
	public void store(final AlphaCardID aci, final Payload p, final String s1,
			final String s2) {
	}

}
