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
package alpha.editor.handlers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alpha.editor.ContentCardCreator;
import alpha.editor.EditorData;
import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.AlphaCardType;
import alpha.model.apa.CorpusGenericus;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationshipType;

/**
 * This class allows the user to drop strings onto the affiliated widget. If the
 * string contains the value "one" or "two", one or two new AlphaCards are
 * created and inserted into the AlphaDoc.
 * 
 * 
 */
@Component
public class JLabelTransferHandler extends TransferHandler {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(JLabelTransferHandler.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5499078779446550021L;

	/** The apf. */
	@Autowired
	private AlphaPropsFacade apf;

	/** The ed. */
	@Autowired
	private EditorData ed;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent,
	 * java.awt.datatransfer.DataFlavor[])
	 */
	@Override
	@SuppressWarnings("deprecation")
	public boolean canImport(final JComponent arg0, final DataFlavor[] arg1) {
		for (final DataFlavor flavor : arg1) {
			if (flavor.equals(DataFlavor.javaJVMLocalObjectMimeType)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.TransferHandler#importData(javax.swing.JComponent,
	 * java.awt.datatransfer.Transferable)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public boolean importData(final JComponent comp, final Transferable t) {
		final DataFlavor[] flavors = t.getTransferDataFlavors();
		int state = 0;
		AlphaCardID aci1 = null;
		AlphaCardID aci2 = null;
		for (final DataFlavor flavor : flavors) {
			if (flavor.equals(DataFlavor.javaJVMLocalObjectMimeType)) {
				try {
					final String data = (String) t.getTransferData(flavor);

					/* if one or two create one AlphaCardDescriptor */
					if (data.toLowerCase().equals("one")
							|| data.toLowerCase().equals("two")) {
						if (data.toLowerCase().equals("one")) {
							state = 0;
						} else {
							state = 1;
						}

						final ContentCardCreator acc = new ContentCardCreator(
								this.apf);
//						acc.setUser(this.ed.getConfig().getLocalParticipant());
						acc.setUser(this.apf.getParticipantByActor(this.ed.getConfig().getLocalNodeID().getContributor().getActor()));
						acc.setCra(this.apf.getCRA());
						final AlphaCardDescriptor ac = acc.create(state);
						aci1 = ac.getId();

						if (state == 1) {
							ac.readAdornment(
									CorpusGenericus.ALPHACARDTYPE.value())
									.setValue(
											AlphaCardType.REFERRAL_VOUCHER
													.value());
						}

						try {
							this.apf.addAlphaCard(ac);
						} catch (final Exception e1) {
							JLabelTransferHandler.LOGGER.severe("Error: " + e1);
							final String trace = JLabelTransferHandler.join(
									e1.getStackTrace(), '\n');
							JOptionPane.showMessageDialog(
									new JFrame(),
									"FAIL: " + e1.toString() + "\n"
											+ e1.getLocalizedMessage() + "\n"
											+ trace + "\n");
						}
					}

					/* if case "two" create second AlphaCardDescriptor */
					if (data.toLowerCase().equals("two")) {
						final ContentCardCreator acc = new ContentCardCreator(
								this.apf);
						final AlphaCardDescriptor ac = acc.create(state);
						aci2 = ac.getId();

						ac.readAdornment(CorpusGenericus.ALPHACARDTYPE.value())
								.setValue(AlphaCardType.RESULTS_REPORT.value());

						try {
							this.apf.addAlphaCard(ac);
							this.apf.addRelationship(aci1, aci2,
									AlphaCardRelationshipType.NEEDS);
						} catch (final Exception e1) {
							JLabelTransferHandler.LOGGER.severe("Error: " + e1);

							final StackTraceElement[] stack = e1
									.getStackTrace();
							final StringBuffer buf = new StringBuffer();
							for (final StackTraceElement stackTraceElement : stack) {
								buf.append(stackTraceElement.toString() + "\n");
							}
							final String trace = buf.toString();
							JOptionPane.showMessageDialog(
									new JFrame(),
									"FAIL: " + e1.toString() + "\n"
											+ e1.getLocalizedMessage() + "\n"
											+ trace);
						}

					}
					return true;
				} catch (final UnsupportedFlavorException e) {
					JLabelTransferHandler.LOGGER.severe("Error: " + e);
				} catch (final IOException e) {
					JLabelTransferHandler.LOGGER.severe("Error: " + e);
				} catch (final Throwable e) {
					JLabelTransferHandler.LOGGER.severe("Error: " + e);
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing
	 * the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty
	 * strings within the array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.join(null, *)               = null
	 * StringUtils.join([], *)                 = ""
	 * StringUtils.join([null], *)             = ""
	 * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
	 * StringUtils.join(["a", "b", "c"], null) = "abc"
	 * StringUtils.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * 
	 * @param array
	 *            the array of values to join together, may be null
	 * @param separator
	 *            the separator character to use
	 * @return the joined String, {@code null} if null array input
	 * @since 2.0
	 */
	public static String join(final Object[] array, final char separator) {
		if (array == null) {
			return null;
		}

		return JLabelTransferHandler.join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing
	 * the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty
	 * strings within the array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.join(null, *)               = null
	 * StringUtils.join([], *)                 = ""
	 * StringUtils.join([null], *)             = ""
	 * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
	 * StringUtils.join(["a", "b", "c"], null) = "abc"
	 * StringUtils.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * 
	 * @param array
	 *            the array of values to join together, may be null
	 * @param separator
	 *            the separator character to use
	 * @param startIndex
	 *            the first index to start joining from. It is an error to pass
	 *            in an end index past the end of the array
	 * @param endIndex
	 *            the index to stop joining from (exclusive). It is an error to
	 *            pass in an end index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 * @since 2.0
	 */
	public static String join(final Object[] array, final char separator,
			final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		final int noOfItems = (endIndex - startIndex);
		if (noOfItems <= 0) {
			return "";
		}

		final StringBuilder buf = new StringBuilder(noOfItems * 16);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}
}
