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
 * $Id: PayloadCreator.java 3786 2012-05-04 08:00:25Z uj32uvac $
 *************************************************************************/
package alpha.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.CorpusGenericus;

/**
 * Create a new {@link Payload} from a {@link File}.
 */
public class PayloadCreator {

	/**
	 * Instantiates a new {@link PayloadCreator}.
	 * 
	 * @param acd
	 *            the AlphaCard to which the created payload is added
	 * @param parent
	 *            the parent {@link JPanel} gui component
	 * @param apf
	 *            the global {@link AlphaPropsFacade}
	 */
	public PayloadCreator(final AlphaCardDescriptor acd, final JPanel parent,
			final AlphaPropsFacade apf) {
		String cardOwner = new String();
		try {
			cardOwner = acd.readAdornment(CorpusGenericus.ACTOR.value())
					.getValue().substring(1);
		} catch (final Exception e2) {
			e2.printStackTrace();
		}
		if (!cardOwner.equals(apf.getAlphaConfig().getLocalNodeID()
				.getContributor().getActor())) {
			JOptionPane.showMessageDialog(new JFrame(),
					"You are not allowed to add a payload to this AlphaCard.");
			return;
		}

		final JFileChooser fc = new JFileChooser();
		final int retVal = fc.showOpenDialog(parent);

		if (retVal == JFileChooser.APPROVE_OPTION) {
			try {
				final File file = fc.getSelectedFile();

				final long fsize = file.length();
				final byte[] fdump = new byte[(int) fsize];

				FileInputStream fis;
				fis = new FileInputStream(file);
				fis.read(fdump);
				fis.close();

				final Payload pl = new Payload();
				pl.setContent(fdump);

				final HashMap<String, String> adornmentChangeRequests = new HashMap<String, String>();
				final int last = file.getAbsolutePath().lastIndexOf(".");
				final String type = file.getAbsolutePath().substring(last + 1);
				adornmentChangeRequests.put(
						CorpusGenericus.SYNTACTICPAYLOADTYPE.value(), type);
				apf.setPayload(acd.getId(), pl, adornmentChangeRequests);

			} catch (final FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (final IOException e1) {

				e1.printStackTrace();
			}
		}

	}
}
