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
 * $Id: OpenPGPSecurityUtility.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.offsync.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.util.io.Streams;
import java.util.logging.Logger;


import alpha.overnet.security.SecurityUtility;

/**
 * OpenPGP-based implementation of {@link SecurityUtility}.
 */
public class OpenPGPSecurityUtility extends SecurityUtility {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(alpha.offsync.security.OpenPGPSecurityUtility.class.getName());

	/** The secret keyring. */
	private final File secretKeyRing;

	/** The public keyring. */
	private final File publicKeyRing;

	/** The secret keyring password. */
	private final char[] secretKeyRingPassword;

	/**
	 * Instantiates a new {@link OpenPGPSecurityUtility}.
	 * 
	 * @param secretKeyRingFile
	 *            the secret keyring file
	 * @param publicKeyRingFile
	 *            the public keyring file
	 * @param secretKeyRingPassword
	 *            the secret keyring password
	 */
	public OpenPGPSecurityUtility(final String secretKeyRingFile,
			final String publicKeyRingFile, final String secretKeyRingPassword) {
		this.secretKeyRing = new File(secretKeyRingFile);
		this.publicKeyRing = new File(publicKeyRingFile);
		this.secretKeyRingPassword = secretKeyRingPassword.toCharArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.security.SecurityUtility#encrypt(java.io.OutputStream,
	 * java.io.InputStream, java.lang.String)
	 */
	@Override
	public void encrypt(final OutputStream outputStream,
			final InputStream inputStream, final String[] keyInfo) {

		try {
			// =
			// this.readPublicKey(this.publicKeyRing);

			final ArmoredOutputStream out = new ArmoredOutputStream(
					outputStream);

			try {
				final BcPGPDataEncryptorBuilder builder = new BcPGPDataEncryptorBuilder(
						SymmetricKeyAlgorithmTags.CAST5);
				builder.setSecureRandom(new SecureRandom());
				final PGPEncryptedDataGenerator cPk = new PGPEncryptedDataGenerator(
						builder, true);
				for (final String info : keyInfo) {
					final PGPPublicKey encKey = this.getEncryptionKey(info);
					if (encKey != null) {
						cPk.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(
								encKey));
					} else {
						OpenPGPSecurityUtility.LOGGER
								.info("Encryption key for recipient " + info
										+ " could not be found!");
					}

				}

				final OutputStream cOut = cPk.open(out, new byte[1 << 16]);

				final PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(
						CompressionAlgorithmTags.ZIP);

				final PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
				final byte[] buffer = new byte[1 << 16];
				final OutputStream pOut = lData.open(comData.open(cOut),
						PGPLiteralData.BINARY, "", new Date(), buffer);

				final byte[] buf = new byte[buffer.length];
				int len;

				while ((len = inputStream.read(buf)) > 0) {
					pOut.write(buf, 0, len);
				}

				lData.close();
				inputStream.close();

				comData.close();

				cOut.close();

				out.close();

			} catch (final PGPException e) {
				System.err.println(e);
				if (e.getUnderlyingException() != null) {
					e.getUnderlyingException().printStackTrace();
				}
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.security.SecurityUtility#decrypt(java.io.OutputStream,
	 * java.io.InputStream)
	 */
	@Override
	public void decrypt(final OutputStream outputStream,
			final InputStream inputStream) {

		try {
			final File keyFile = this.secretKeyRing;
			final char[] passwd = this.secretKeyRingPassword;

			final InputStream in = PGPUtil.getDecoderStream(inputStream);

			try {
				final PGPObjectFactory pgpF = new PGPObjectFactory(in);
				PGPEncryptedDataList enc;

				final Object o = pgpF.nextObject();

				if (o instanceof PGPEncryptedDataList) {
					enc = (PGPEncryptedDataList) o;
				} else {
					enc = (PGPEncryptedDataList) pgpF.nextObject();
				}

				final Iterator it = enc.getEncryptedDataObjects();
				PGPPrivateKey sKey = null;
				PGPPublicKeyEncryptedData pbe = null;
				final PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
						PGPUtil.getDecoderStream(new FileInputStream(keyFile)));

				while ((sKey == null) && it.hasNext()) {
					pbe = (PGPPublicKeyEncryptedData) it.next();

					sKey = this.findSecretKey(pgpSec, pbe.getKeyID(), passwd);
				}

				if (sKey == null)
					throw new IllegalArgumentException(
							"secret key for message not found.");

				final InputStream clear = pbe
						.getDataStream(new BcPublicKeyDataDecryptorFactory(sKey));

				final PGPObjectFactory plainFact = new PGPObjectFactory(clear);

				final PGPCompressedData cData = (PGPCompressedData) plainFact
						.nextObject();

				final InputStream compressedStream = new BufferedInputStream(
						cData.getDataStream());
				final PGPObjectFactory pgpFact = new PGPObjectFactory(
						compressedStream);

				final Object message = pgpFact.nextObject();

				if (message instanceof PGPLiteralData) {
					final PGPLiteralData ld = (PGPLiteralData) message;

					final InputStream unc = ld.getInputStream();
					final OutputStream fOut = new BufferedOutputStream(
							outputStream);

					Streams.pipeAll(unc, fOut);

					fOut.close();
				} else if (message instanceof PGPOnePassSignatureList)
					throw new PGPException(
							"encrypted message contains a signed message - not literal data.");
				else
					throw new PGPException(
							"message is not a simple encrypted file - type unknown.");
			} catch (final PGPException e) {
				System.err.println(e);
				if (e.getUnderlyingException() != null) {
					e.getUnderlyingException().printStackTrace();
				}
			}
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// /**
	// * Reads public key.
	// *
	// * @param publicKeyRing the public key ring
	// * @return the pGP public key
	// * @throws IOException Signals that an I/O exception has occurred.
	// * @throws PGPException the pGP exception
	// */
	// public PGPPublicKey readPublicKey(File publicKeyRing) throws IOException,
	// PGPException {
	// PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(
	// PGPUtil.getDecoderStream(new FileInputStream(publicKeyRing)));
	//
	// Iterator keyRingIter = pgpPub.getKeyRings();
	// while (keyRingIter.hasNext()) {
	// PGPPublicKeyRing keyRing = (PGPPublicKeyRing) keyRingIter.next();
	//
	// Iterator keyIter = keyRing.getPublicKeys();
	// while (keyIter.hasNext()) {
	// PGPPublicKey key = (PGPPublicKey) keyIter.next();
	//
	// if (key.isEncryptionKey()) {
	// return key;
	// }
	// }
	// }
	//
	// throw new IllegalArgumentException(
	// "Can't find encryption key in key ring.");
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.security.SecurityUtility#sign(java.io.OutputStream,
	 * java.io.InputStream, java.lang.String)
	 */
	@Override
	public void sign(final OutputStream outputStream,
			final InputStream inputStream, final String keyInfo) {
		try {
			final File keyFile = this.secretKeyRing;
			final char[] pass = this.secretKeyRingPassword;

			final ArmoredOutputStream out = new ArmoredOutputStream(
					outputStream);

			final PGPSecretKey pgpSec = this.getSignKey(keyInfo); // readSecretKey(new
			// FileInputStream(keyFile));
			final PGPPrivateKey pgpPrivKey = pgpSec
					.extractPrivateKey(new BcPBESecretKeyDecryptorBuilder(
							new BcPGPDigestCalculatorProvider()).build(pass));
			final PGPSignatureGenerator sGen = new PGPSignatureGenerator(
					new BcPGPContentSignerBuilder(pgpSec.getPublicKey()
							.getAlgorithm(), HashAlgorithmTags.SHA1));

			sGen.init(PGPSignature.BINARY_DOCUMENT, pgpPrivKey);

			final Iterator it = pgpSec.getPublicKey().getUserIDs();
			if (it.hasNext()) {
				final PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();

				spGen.setSignerUserID(false, (String) it.next());
				sGen.setHashedSubpackets(spGen.generate());
			}

			final PGPCompressedDataGenerator cGen = new PGPCompressedDataGenerator(
					CompressionAlgorithmTags.ZLIB);

			final BCPGOutputStream bOut = new BCPGOutputStream(cGen.open(out));

			sGen.generateOnePassVersion(false).encode(bOut);

			final PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
			final byte[] buffer = new byte[1 << 16];
			final OutputStream lOut = lGen.open(bOut, PGPLiteralData.BINARY,
					"", new Date(), buffer);
			int ch = 0;

			while ((ch = inputStream.read()) >= 0) {
				lOut.write(ch);
				sGen.update((byte) ch);
			}

			lGen.close();

			sGen.generate().encode(bOut);
			cGen.close();

			out.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final PGPException e) {
			e.printStackTrace();
		} catch (final SignatureException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.overnet.security.SecurityUtility#verify(java.io.OutputStream,
	 * java.io.InputStream)
	 */
	@Override
	public void verify(OutputStream outputStream, final InputStream inputStream) {

		try {
			final File keyFile = this.publicKeyRing;

			final InputStream in = PGPUtil.getDecoderStream(inputStream);

			PGPObjectFactory pgpFact = new PGPObjectFactory(in);

			final PGPCompressedData c1 = (PGPCompressedData) pgpFact
					.nextObject();

			pgpFact = new PGPObjectFactory(c1.getDataStream());

			final PGPOnePassSignatureList p1 = (PGPOnePassSignatureList) pgpFact
					.nextObject();

			final PGPOnePassSignature ops = p1.get(0);

			final PGPLiteralData p2 = (PGPLiteralData) pgpFact.nextObject();

			final InputStream dIn = p2.getInputStream();
			int ch;
			final PGPPublicKeyRingCollection pgpRing = new PGPPublicKeyRingCollection(
					PGPUtil.getDecoderStream(new FileInputStream(keyFile)));

			final PGPPublicKey key = pgpRing.getPublicKey(ops.getKeyID());

			ops.init(new BcPGPContentVerifierBuilderProvider(), key);

			while ((ch = dIn.read()) >= 0) {
				ops.update((byte) ch);
				outputStream.write(ch);
			}

			outputStream.close();

			final PGPSignatureList p3 = (PGPSignatureList) pgpFact.nextObject();

			if (!ops.verify(p3.get(0))) {
				outputStream = null;
			}
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// /**
	// * Read secret key.
	// *
	// * @param input the input
	// * @return the pGP secret key
	// * @throws IOException Signals that an I/O exception has occurred.
	// * @throws PGPException the pGP exception
	// */
	// private PGPSecretKey readSecretKey(InputStream input) throws IOException,
	// PGPException {
	// PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
	// PGPUtil.getDecoderStream(input));
	//
	// Iterator keyRingIter = pgpSec.getKeyRings();
	// while (keyRingIter.hasNext()) {
	// PGPSecretKeyRing keyRing = (PGPSecretKeyRing) keyRingIter.next();
	//
	// Iterator keyIter = keyRing.getSecretKeys();
	// while (keyIter.hasNext()) {
	// PGPSecretKey key = (PGPSecretKey) keyIter.next();
	//
	// if (key.isSigningKey()) {
	// return key;
	// }
	// }
	// }
	//
	// throw new IllegalArgumentException(
	// "Can't find signing key in key ring.");
	// }

	/**
	 * Finds the secret key of a {@link PGPSecretKeyRingCollection}.
	 * 
	 * @param pgpSec
	 *            the {@link PGPSecretKeyRingCollection}
	 * @param keyID
	 *            the key id
	 * @param pass
	 *            the secret key password
	 * @return the {@link PGPPrivateKey}
	 * @throws PGPException
	 *             thrown if an error is encountered
	 */
	private PGPPrivateKey findSecretKey(
			final PGPSecretKeyRingCollection pgpSec, final long keyID,
			final char[] pass) throws PGPException {
		final PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);

		if (pgpSecKey == null)
			return null;

		return pgpSecKey.extractPrivateKey(new BcPBESecretKeyDecryptorBuilder(
				new BcPGPDigestCalculatorProvider())
				.build(this.secretKeyRingPassword));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.overnet.security.SecurityUtility#importCryptographyMetadata(java
	 * .io.InputStream)
	 */
	@Override
	public void importCryptographyMetadata(final InputStream input) {

		OpenPGPSecurityUtility.LOGGER.info("Importing cryptography metadata");
		try {
			PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(
					PGPUtil.getDecoderStream(new FileInputStream(
							this.publicKeyRing)));

			final PGPPublicKeyRingCollection pgpPubIncoming = new PGPPublicKeyRingCollection(
					PGPUtil.getDecoderStream(input));

			PGPPublicKeyRing ppKr;
			final Iterator<PGPPublicKeyRing> it = pgpPubIncoming.getKeyRings();
			while (it.hasNext()) {
				ppKr = it.next();
				if (!pgpPub.contains(ppKr.getPublicKey().getKeyID())) {
					pgpPub = PGPPublicKeyRingCollection.addPublicKeyRing(
							pgpPub, ppKr);
				}
			}

			pgpPub.encode(new FileOutputStream(new File(this.publicKeyRing
					.getAbsolutePath())));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final PGPException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the correct encryption key from local public keyring using the
	 * supplied key information.
	 * 
	 * @param keyInfo
	 *            the supplied key information
	 * @return the correct encryption key
	 */
	public PGPPublicKey getEncryptionKey(final String keyInfo) {
		PGPPublicKeyRingCollection pgpPub;
		try {
			pgpPub = new PGPPublicKeyRingCollection(
					PGPUtil.getDecoderStream(new FileInputStream(
							this.publicKeyRing)));

			final Iterator<PGPPublicKeyRing> keyRingIter = pgpPub.getKeyRings();
			while (keyRingIter.hasNext()) {
				final PGPPublicKeyRing keyRing = keyRingIter.next();
				final Iterator keyIter = keyRing.getPublicKeys();

				while (keyIter.hasNext()) {
					final PGPPublicKey key = (PGPPublicKey) keyIter.next();

					final Iterator idIter = key.getUserIDs();
					while (idIter.hasNext()) {
						final String userID = idIter.next().toString();
						if (userID.contains(keyInfo) && key.isEncryptionKey())
							return key;
					}

				}
			}

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final PGPException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gets the correct signing key from local secret keyring using the supplied
	 * key information.
	 * 
	 * @param keyInfo
	 *            the supplied key information
	 * @return the correct signing key
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PGPException
	 *             thrown if an error is encountered
	 */
	public PGPSecretKey getSignKey(final String keyInfo) throws IOException,
			PGPException {
		final PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
				PGPUtil.getDecoderStream(new FileInputStream(this.secretKeyRing)));

		final Iterator keyRingIter = pgpSec.getKeyRings();
		while (keyRingIter.hasNext()) {
			final PGPSecretKeyRing keyRing = (PGPSecretKeyRing) keyRingIter
					.next();

			final Iterator keyIter = keyRing.getSecretKeys();
			while (keyIter.hasNext()) {
				final PGPSecretKey key = (PGPSecretKey) keyIter.next();

				final Iterator idIter = key.getUserIDs();
				while (idIter.hasNext()) {
					final String userID = idIter.next().toString();
					if (userID.contains(keyInfo) && key.isSigningKey())
						return key;
				}

			}
		}

		return null;
	}

}
