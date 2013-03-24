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

import java.io.IOException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import alpha.model.docconfig.AlphadocConfig;
import alpha.util.XmlBinder;

/**
 * The StoreConfigurationThread saves the AlphadocConfig into the
 * alphaconfig.xml
 * 
 * 
 */
@Scope("prototype")
@Component
public class StoreConfigurationThread {

	/** The config. */
	private final AlphadocConfig config;

	/** The home path. */
	private final String homePath;

	/**
	 * Instantiates a new store configuration thread.
	 * 
	 * @param config
	 *            the config
	 * @param homePath
	 *            the home path
	 */
	public StoreConfigurationThread(final AlphadocConfig config,
			final String homePath) {
		this.config = config;
		this.homePath = homePath;
	}

	/**
	 * Run.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void run() throws IOException {
		final XmlBinder xb = new XmlBinder();
		xb.store(this.config, this.homePath + "/alphaconfig.xml",
				"alpha.model.docconfig");
	}
}
