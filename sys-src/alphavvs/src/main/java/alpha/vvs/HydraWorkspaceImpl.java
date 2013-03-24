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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.logging.Logger;


import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.APAPayload;
import alpha.model.cra.CRAPayload;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.psa.PSAPayload;
import alpha.util.XmlBinder;

//TODO: Verify loadDescriptor Functionality
//TODO: Create alpha.facades.WorkspaceFacade
//TODO: Create alpha.facades.exception.WorkspaceException
//TODO: Test Implementaion
//TODO: Consider Method Names (just "load" and "store")?

/**
 * This class provides an implementation for the workspace facade which is
 * designed to work inconjunction with the HydraFacade in the A-Flow System.
 * 
 * @author Scott A. Hady
 * @since 1.0
 */
public class HydraWorkspaceImpl {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(HydraWorkspaceImpl.class.getName());

	/** The workspace. */
	private final File workspace;

	/**
	 * Specialized Constructor, receives a file indicating where the workspace
	 * for Hydra is maintained.
	 * 
	 * @param workspace
	 *            File.
	 */
	public HydraWorkspaceImpl(final File workspace) {
		this.workspace = workspace;
	}

	/**
	 * Gets the descriptor file.
	 * 
	 * @param aci
	 *            the aci
	 * @return the descriptor file
	 */
	public File getDescriptorFile(final AlphaCardID aci) {
		return new File(this.workspace, this.getACardDirectoryName(aci)
				+ "/desc/Descriptor.xml");
	}

	/**
	 * Gets the payload file.
	 * 
	 * @param aci
	 *            the aci
	 * @param fileExtension
	 *            the file extension
	 * @return the payload file
	 */
	public File getPayloadFile(final AlphaCardID aci, final String fileExtension) {
		return new File(this.workspace, this.getACardDirectoryName(aci)
				+ "/pl/Payload." + fileExtension);
	}

	/**
	 * Get the logical unit's name associated with the aCard Identifier. This
	 * name is the name of the directory within the workspace where the aCard's
	 * current payload and descriptor will be maintained.
	 * 
	 * @param aci
	 *            AlphaCardID.
	 * @return luName - String.
	 */
	public String getACardDirectoryName(final AlphaCardID aci) {
		return aci.getCardID();
	}

	/**
	 * Set the provided aCard's payload and descriptor to the content stored in
	 * the given aCard's workspace. If the content of the workspace is unable to
	 * support the operation then the item is set to null.
	 * 
	 * @param aci
	 *            AlphaCardID.
	 * @param payload
	 *            Payload.
	 * @param fileExtension
	 *            String.
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @return success.
	 * @throws Exception
	 *             the exception
	 */
	public boolean load(final AlphaCardID aci, Payload payload,
			final String fileExtension, AlphaCardDescriptor acd)
			throws Exception {
		payload = this.loadPayload(aci, fileExtension);
		acd = this.loadDescriptor(aci);
		return (payload != null) && (acd != null);
	}

	/**
	 * Create and return the aCard's descriptor from the content stored in the
	 * workspace.
	 * 
	 * @param aci
	 *            AlphaCardID.
	 * @return acd - AlphaCardDescriptor.
	 * @throws Exception
	 *             the exception
	 */
	public AlphaCardDescriptor loadDescriptor(final AlphaCardID aci)
			throws Exception {
		AlphaCardDescriptor acd = null;
		final File descriptorFile = new File(this.workspace,
				this.getACardDirectoryName(aci) + "/desc/Descriptor.xml");
		final XmlBinder xmlb = new XmlBinder();
		acd = (AlphaCardDescriptor) xmlb.load(descriptorFile.getPath(),
				"alpha.model");
		return acd;
	}

	/**
	 * Create and return the aCard's payload from the content stored in the
	 * workspace.
	 * 
	 * @param aci
	 *            AlphaCardID.
	 * @param fileExtension
	 *            String.
	 * @return payload - Payload.
	 * @throws Exception
	 *             the exception
	 */
	public Payload loadPayload(final AlphaCardID aci, final String fileExtension)
			throws Exception {
		Payload payload = null;
		File payloadFile = null;
		if (aci.getCardID().equals(CoordCardType.PSA.id())
				|| aci.getCardID().equals(CoordCardType.CRA.id())
				|| aci.getCardID().equals(CoordCardType.APA.id())) {
			payloadFile = new File(this.workspace,
					this.getACardDirectoryName(aci) + "/pl/Payload.xml");
			final CoordCardType type = CoordCardType.fromValue(aci.getCardID()
					.substring(1));
			final XmlBinder xmlb = new XmlBinder();
			payload = (Payload) xmlb.load(payloadFile.getPath(),
					type.getModel());
		} else {
			payloadFile = new File(this.workspace,
					this.getACardDirectoryName(aci) + "/pl/Payload."
							+ fileExtension);
			payload = new Payload();
			final byte[] fileContent = new byte[(int) payloadFile.length()];
			BufferedInputStream bis = null;
			try {
				bis = new BufferedInputStream(new FileInputStream(payloadFile));
				bis.read(fileContent);
			} catch (final Exception e) {
				this.processException(e);
			} finally {
				try {
					if (bis != null) {
						bis.close();
					}
				} catch (final Exception e) {
					this.processException(e);
				}
			}
			payload.setContent(fileContent);
		}
		return payload;
	}

	/**
	 * Stores an aCard's payload and descriptor as the current contents of the
	 * designated aCard folder in the workspace.
	 * 
	 * @param aci
	 *            AlphaCardID.
	 * @param payload
	 *            Payload.
	 * @param fileExtension
	 *            String.
	 * @param acd
	 *            the acd
	 * @return success - boolean.
	 * @throws Exception
	 *             the exception
	 */
	public boolean store(final AlphaCardID aci, final Payload payload,
			final String fileExtension, final AlphaCardDescriptor acd)
			throws Exception {
		boolean success = true;
		// Store Payload File
		if (payload != null) {
			success = this.storePayload(aci, payload, fileExtension);
		}
		// Store Descriptor
		if (acd != null) {
			success = this.storeDescriptor(aci, acd) && success;
		}
		return success;
	}

	/**
	 * Stores an aCard's descriptor as the current contents of the designated
	 * aCard folder in the workspace.
	 * 
	 * @param aci
	 *            AlphaCardID.
	 * @param acd
	 *            the acd
	 * @return success - boolean.
	 * @throws Exception
	 *             the exception
	 */
	public boolean storeDescriptor(final AlphaCardID aci,
			final AlphaCardDescriptor acd) throws Exception {
		boolean success = true;
		final File descriptorFile = new File(this.workspace,
				this.getACardDirectoryName(aci) + "/desc/Descriptor.xml");
		final XmlBinder xmlb = new XmlBinder();
		try {
			final File dir = descriptorFile.getParentFile();
			dir.mkdirs();
			xmlb.store(acd, descriptorFile.getPath(), "alpha.model");
		} catch (final Exception e) {
			success = false;
			this.processException(e);
		}
		return success;
	}

	/**
	 * Stores an aCard's payload as the current contents of the designated aCard
	 * folder in the workspace.
	 * 
	 * @param aci
	 *            AlphaCardID.
	 * @param payload
	 *            Payload.
	 * @param fileExtension
	 *            String.
	 * @return success - boolean.
	 * @throws Exception
	 *             the exception
	 */
	public boolean storePayload(final AlphaCardID aci, final Payload payload,
			final String fileExtension) throws Exception {
		boolean success = true;
		File payloadFile;
		final byte[] payloadContent = payload.getContent();
		if (payloadContent != null) {
			payloadFile = new File(this.workspace,
					this.getACardDirectoryName(aci) + "/pl/Payload."
							+ fileExtension);
			BufferedOutputStream bos = null;
			try {
				final File dir = payloadFile.getParentFile();
				dir.mkdirs();
				bos = new BufferedOutputStream(
						new FileOutputStream(payloadFile));
				bos.write(payloadContent);
				bos.flush();
				bos.close();
			} catch (final Exception e) {
				success = false;
				this.processException(e);
			} finally {
				try {
					if (bos != null) {
						bos.flush();
						bos.close();
					}
				} catch (final Exception e) {
					success = false;
					this.processException(e);
				}
			}
		} else if ((payload instanceof PSAPayload)
				|| (payload instanceof CRAPayload)
				|| (payload instanceof APAPayload)) {
			payloadFile = new File(this.workspace,
					this.getACardDirectoryName(aci) + "/pl/Payload.xml");
			final CoordCardType type = CoordCardType.fromValue(aci.getCardID()
					.substring(1));
			final XmlBinder xmlb = new XmlBinder();
			try {
				xmlb.store(payload, payloadFile.getPath(), type.getModel());
			} catch (final Exception e) {
				success = false;
				this.processException(e);
			}
		}
		return success;
	}

	/**
	 * Simplification method to log and throw exceptions.
	 * 
	 * @param e
	 *            the e
	 * @throws Exception
	 *             the exception
	 */
	private void processException(final Exception e) throws Exception {
		JOptionPane.showMessageDialog(new JFrame(), "FAIL: " + e.toString());
		HydraWorkspaceImpl.LOGGER.severe("ERROR: " + e);
		throw new Exception(e);
	}

}
