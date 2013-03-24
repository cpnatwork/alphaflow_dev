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
package alpha.editor.threads;

import java.awt.Desktop;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.CorpusGenericus;
import alpha.model.identification.AlphaCardID;

/**
 * The Class FileOpenThread.
 * 
 * This thread is started when an AlphaPanel is double clicked. Then it tries to
 * open the Payload of the double clicked AlphaCardDescriptor.
 */
@Scope("prototype")
@Component
public class FileOpenThread extends Thread {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(FileOpenThread.class.getName());

	/** The apf. */
	@Autowired
	private AlphaPropsFacade apf;

	/** The home path. */
	private String homePath;

	/** The aci. */
	private final AlphaCardID aci;

	/**
	 * Instantiates a new file open thread.
	 * 
	 * @param absPath
	 *            the abs path
	 * @param aci
	 *            the aci
	 */
	public FileOpenThread(final String absPath, final AlphaCardID aci) {
		this.homePath = absPath;
		this.aci = aci;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		String spType = null;

		AlphaCardDescriptor ac = null;
		try {
			ac = this.apf.getAlphaCard(this.aci);
			spType = ac.readAdornment(
					CorpusGenericus.SYNTACTICPAYLOADTYPE.value()).getValue();
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Could not find AlphaCardDescriptor");
		}
		if (spType == null) {
			JOptionPane.showMessageDialog(new JFrame(), "No Payload available");
			return;
		}
		/* create Payload path */
		/*
		 * homePath += "/" + aci.getEpisodeID() + "/" + aci.getCardID() + "/" +
		 * ac.getAdaptiveAdornment(CorpusGenericus.VERSION.value()) .getValue()
		 * + "/pl" + "/Payload." + spType;
		 */

		// TODO: revert to code in comment above once vvs is properly
		// implemented
		this.homePath += this.aci.getEpisodeID() + "/" + this.aci.getCardID()
				+ "/pl/Payload." + spType;

		if (!Desktop.isDesktopSupported()) {
			JOptionPane
					.showMessageDialog(new JFrame(),
							"Opening Payload failed:\nDesktop interface is not supported by your platform");
			return;
		}

		try {
			final File fileToOpen = new File(this.homePath).getCanonicalFile();
			FileOpenThread.LOGGER.info("Open File: " + fileToOpen.getPath());
			Desktop.getDesktop().open(fileToOpen);
		} catch (final Exception e1) {
			JOptionPane.showMessageDialog(
					new JFrame(),
					"Opening Payload failed\n" + this.homePath + "\n"
							+ e1.toString());
		} catch (final Throwable t) {
			JOptionPane.showMessageDialog(
					new JFrame(),
					"Opening Payload failed\n" + this.homePath + "\n"
							+ t.toString());
		}
	}
}
