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
package alpha.editor.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import java.util.logging.Logger;


import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaDoc;
import alpha.model.docconfig.AlphadocConfig;
import alpha.util.XmlBinder;

/**
 * This class is called if the AlphaEditor is terminated. Before exiting the
 * editor the AlphaDoc object and die AlphadocConfig object are stored in the
 * file system. Additionally the AlphaProperties module is shut down. After that
 * the application terminates.
 * 
 */
public class EditorWindowListener implements WindowListener {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(EditorWindowListener.class.getName());

	/** The home path. */
	private final String homePath;

	/** The apf. */
	private final AlphaPropsFacade apf;

	/** The config. */
	private final AlphadocConfig config;

	/**
	 * Instantiates a new editor window listener.
	 * 
	 * @param homePath
	 *            the home path
	 * @param apf
	 *            the apf
	 * @param config
	 *            the config
	 */
	public EditorWindowListener(final String homePath,
			final AlphaPropsFacade apf, final AlphadocConfig config) {
		this.homePath = homePath;
		this.apf = apf;
		this.config = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(final WindowEvent e) {
		/* not yet needed here */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(final WindowEvent e) {
		/* not yet needed here */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(final WindowEvent e) {
		final AlphaDoc doc = this.apf.getAlphaDoc();
		XmlBinder xml = new XmlBinder();
		try {
			xml.store(doc, this.homePath + "/alphaDoc.xml", "alpha.model");
			xml = new XmlBinder();
			xml.store(this.config, this.homePath + "/alphaconfig.xml",
					"alpha.model.docconfig");
		} catch (final IOException ioe) {
			EditorWindowListener.LOGGER.severe("IOError:" + ioe);
		}

		this.apf.shutdown();
		System.exit(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent
	 * )
	 */
	@Override
	public void windowDeactivated(final WindowEvent e) {
		/* not yet needed here */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent
	 * )
	 */
	@Override
	public void windowDeiconified(final WindowEvent e) {
		/* not yet needed here */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(final WindowEvent e) {
		/* not yet needed here */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(final WindowEvent e) {
		/* not yet needed here */
	}

}
