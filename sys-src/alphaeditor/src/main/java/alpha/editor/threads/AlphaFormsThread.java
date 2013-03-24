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

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import alpha.facades.AlphaPropsFacade;
import alpha.forms.AlphaFormsFacade;
import alpha.forms.application.util.AlphaFormsFactory;
import alpha.forms.util.FormSaveListener;
import alpha.model.Payload;
import alpha.model.identification.AlphaCardID;

/**
 * The Class AlphaFormsThread.
 */
@Scope("prototype")
@Component
public class AlphaFormsThread extends Thread {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphaFormsThread.class.getName());

	/** The aci. */
	private final AlphaCardID aci;

	/** The path. */
	private final String path;

	/** The apf. */
	@Autowired
	private AlphaPropsFacade apf;

	/** The aforms. */
	private final AlphaFormsFacade aforms;

	/**
	 * Instantiates a new alpha forms thread.
	 * 
	 * @param aci
	 *            the aci
	 * @param absPath
	 *            the abs path
	 */
	public AlphaFormsThread(final AlphaCardID aci, final String absPath) {
		this.aci = aci;
		this.aforms = AlphaFormsFactory.createAlphaFormsApplication();
		this.path = absPath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {

			this.aforms.registerSaveListener(new FormSaveListener() {

				@Override
				public void save(final ByteArrayOutputStream form) {
					AlphaFormsThread.LOGGER
							.info("alphaForms: saving form data");
					AlphaFormsThread.LOGGER.info(form.toString());
					final Payload p = new Payload();
					p.setContent(form.toByteArray());
					AlphaFormsThread.this.apf.setPayload(
							AlphaFormsThread.this.aci, p, null);
				}

			});

			// TODO: revert to code in comment above once vvs is properly
			// implemented
			final String formPath = this.path + this.aci.getEpisodeID() + "/"
					+ this.aci.getCardID() + "/pl/Payload.a-form.xml";

			AlphaFormsThread.LOGGER.info("Trying to load alphaForm from file: "
					+ formPath);

			final FileInputStream fin = new FileInputStream(new File(formPath));
			this.aforms.start(fin);
			fin.close();

			JFrame.setDefaultLookAndFeelDecorated(true);
			final JFrame window = new JFrame("alphaForms");
			window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			window.getContentPane().setLayout(new BorderLayout());
			window.getContentPane().add(this.aforms.getView());
			window.setSize(this.aforms.getView().getMinimumSize());
			window.setVisible(true);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
