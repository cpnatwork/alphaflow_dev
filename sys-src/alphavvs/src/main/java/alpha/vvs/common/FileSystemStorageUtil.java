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
package alpha.vvs.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.identification.CoordCardType;
import alpha.util.XmlBinder;

/**
 * Static encapsulation of commonly used Filesystem functionality to store or
 * load descriptors and payloads to files.
 * 
 * @author Scott A. Hady
 * @version 1.0
 * @since 1.0
 */
public class FileSystemStorageUtil {

	/**
	 * Utility Method capable of loading an alpha-Card Descriptor from any
	 * designated file.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param descriptorFile
	 *            File.
	 * @return pathThroughACD - AlphaCardDescriptor.
	 */
	public static AlphaCardDescriptor loadDescriptorFromFile(
			final AlphaCardDescriptor acd, final File descriptorFile) {
		final XmlBinder xmlb = new XmlBinder();
		final AlphaCardDescriptor loadedACD = (AlphaCardDescriptor) xmlb.load(
				descriptorFile.getPath(), "alpha.model");
		return loadedACD;
	}

	/**
	 * Load descriptor from stream.
	 * 
	 * @param acd
	 *            the acd
	 * @param descriptorStream
	 *            the descriptor stream
	 * @return the alpha card descriptor
	 */
	public static AlphaCardDescriptor loadDescriptorFromStream(
			final AlphaCardDescriptor acd, final InputStream descriptorStream) {
		final XmlBinder xmlb = new XmlBinder();
		final AlphaCardDescriptor loadedACD = (AlphaCardDescriptor) xmlb.load(
				descriptorStream, "alpha.model");
		return loadedACD;

	}

	/**
	 * Utility Method capable of loading an alpha-Card Payload from any
	 * designated file.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param payload
	 *            Payload.
	 * @param payloadFile
	 *            File.
	 * @return pathThroughPayload - Payload.
	 */
	public static Payload loadPayloadFromFile(final AlphaCardDescriptor acd,
			Payload payload, final File payloadFile) {
		BufferedInputStream payloadBIS = null;
		try {
			payloadBIS = new BufferedInputStream(new FileInputStream(
					payloadFile));
			payload = FileSystemStorageUtil.loadPayloadFromStream(acd, payload,
					payloadBIS, payloadFile.length());
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (payloadBIS != null) {
				try {
					payloadBIS.close();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		return payload;
	}

	/**
	 * Load payload from stream.
	 * 
	 * @param acd
	 *            the acd
	 * @param payload
	 *            the payload
	 * @param payloadIS
	 *            the payload is
	 * @param payloadLength
	 *            the payload length
	 * @return the payload
	 */
	public static Payload loadPayloadFromStream(final AlphaCardDescriptor acd,
			Payload payload, final InputStream payloadIS,
			final long payloadLength) {
		if (FileSystemStorageUtil.isCoordinationCard(acd)) {
			payload = FileSystemStorageUtil.loadCoordinationPayloadFromStream(
					acd, payload, payloadIS, payloadLength);
		} else {
			payload = FileSystemStorageUtil.loadContentPayloadFromStream(acd,
					payload, payloadIS, payloadLength);
		}
		return payload;
	}

	/**
	 * Checks if is coordination card.
	 * 
	 * @param acd
	 *            the acd
	 * @return true, if is coordination card
	 */
	private static boolean isCoordinationCard(final AlphaCardDescriptor acd) {
		final String cardId = acd.getId().getCardID();
		return (cardId.equals(CoordCardType.PSA.id())
				|| cardId.equals(CoordCardType.CRA.id()) || cardId
					.equals(CoordCardType.APA.id()));
	}

	/**
	 * Load coordination payload from stream.
	 * 
	 * @param acd
	 *            the acd
	 * @param payload
	 *            the payload
	 * @param payloadIS
	 *            the payload is
	 * @param payloadLength
	 *            the payload length
	 * @return the payload
	 */
	private static Payload loadCoordinationPayloadFromStream(
			final AlphaCardDescriptor acd, Payload payload,
			final InputStream payloadIS, final long payloadLength) {
		final CoordCardType ccType = CoordCardType.fromValue(acd.getId()
				.getCardID().substring(1));
		final XmlBinder xmlb = new XmlBinder();
		payload = (Payload) xmlb.load(payloadIS, ccType.getModel());
		return payload;
	}

	/**
	 * Load content payload from stream.
	 * 
	 * @param acd
	 *            the acd
	 * @param payload
	 *            the payload
	 * @param payloadIS
	 *            the payload is
	 * @param payloadLength
	 *            the payload length
	 * @return the payload
	 */
	private static Payload loadContentPayloadFromStream(
			final AlphaCardDescriptor acd, final Payload payload,
			final InputStream payloadIS, final long payloadLength) {
		final byte[] fileContent = new byte[(int) payloadLength];
		final BufferedInputStream bis = null;
		try {
			payloadIS.read(fileContent);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		payload.setContent(fileContent);
		return payload;
	}

	/**
	 * Utility Method capable of storing an alpha-Card Descriptor to any
	 * designated file.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param descriptorFile
	 *            File.
	 * @return success - boolean.
	 */
	public static boolean storeDescriptorToFile(final AlphaCardDescriptor acd,
			final File descriptorFile) {
		boolean success = true;
		final XmlBinder xmlb = new XmlBinder();
		try {
			final File parentDirectory = descriptorFile.getParentFile();
			parentDirectory.mkdirs();
			xmlb.store(acd, descriptorFile.getPath(), "alpha.model");
		} catch (final Exception e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * Utility Method capable of storing an alpha-Card Payload to any designated
	 * file.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor.
	 * @param payload
	 *            Payload.
	 * @param payloadFile
	 *            File.
	 * @return success - boolean.
	 */
	public static boolean storePayloadToFile(final AlphaCardDescriptor acd,
			final Payload payload, final File payloadFile) {
		boolean success = true;
		if (FileSystemStorageUtil.isCoordinationCard(acd)) {
			success = FileSystemStorageUtil.storeCoordinationPayloadToFile(acd,
					payload, payloadFile);
		} else {
			success = FileSystemStorageUtil.storeContentPayloadToFile(acd,
					payload, payloadFile);
		}
		return success;
	}

	/**
	 * Store coordination payload to file.
	 * 
	 * @param acd
	 *            the acd
	 * @param payload
	 *            the payload
	 * @param payloadFile
	 *            the payload file
	 * @return true, if successful
	 */
	private static boolean storeCoordinationPayloadToFile(
			final AlphaCardDescriptor acd, final Payload payload,
			final File payloadFile) {
		boolean success = true;
		final CoordCardType ccType = CoordCardType.fromValue(acd.getId()
				.getCardID().substring(1));
		final XmlBinder xmlb = new XmlBinder();
		try {
			xmlb.store(payload, payloadFile.getPath(), ccType.getModel());
		} catch (final Exception e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * Store content payload to file.
	 * 
	 * @param acd
	 *            the acd
	 * @param payload
	 *            the payload
	 * @param payloadFile
	 *            the payload file
	 * @return true, if successful
	 */
	private static boolean storeContentPayloadToFile(
			final AlphaCardDescriptor acd, final Payload payload,
			final File payloadFile) {
		boolean success = true;
		if (payload.getContent() != null) {
			BufferedOutputStream bos = null;
			try {
				final File parentDirectory = payloadFile.getParentFile();
				parentDirectory.mkdirs();
				bos = new BufferedOutputStream(
						new FileOutputStream(payloadFile));
				bos.write(payload.getContent());
				bos.flush();
				bos.close();
			} catch (final Exception e) {
				success = false;
				e.printStackTrace();
			} finally {
				try {
					if (bos != null) {
						bos.flush();
						bos.close();
					}
				} catch (final Exception e) {
					success = false;
					e.printStackTrace();
				}
			}
		} else {
			// Do Nothing - Do Not Create Empty File ?
		}
		return success;
	}

}
