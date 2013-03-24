/**************************************************************************
 * alpha-Forms: self-editable formulars in form of an active document
 * (originally for the alpha-Flow project)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Florian Wagner
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
package alpha.forms.startup;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.CodeSource;

import javax.swing.JFrame;
import javax.swing.UIManager;

import alpha.forms.AlphaFormsFacade;
import alpha.forms.application.util.AlphaFormsFactory;
import alpha.forms.util.FormSaveListener;

/**
 * The Class OneJarStartup.
 */
public class OneJarStartup {

	/** The Constant DEFAULT_FORM_NAME. */
	protected final static String DEFAULT_FORM_NAME = "default.a-form.xml";

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {

		final CodeSource codeSource = OneJarStartup.class.getProtectionDomain()
				.getCodeSource();
		System.out.println("alphaForms OneJar");
		String jarDir = null;
		String jarFileName = null;
		try {
			final String jar = codeSource.getLocation().toURI().toString()
					.replace("jar:", "").replace("file:", "")
					.replaceFirst("!/.*", "");
			final File jarFile = new File(jar);
			jarFileName = jarFile.getName();
			jarDir = jarFile.getParentFile().getPath();
		} catch (final Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception e) {

		}

		final AlphaFormsFacade alphaForms = AlphaFormsFactory
				.createAlphaFormsApplication();
		final String alphaFormFile;

		if (args.length > 0) {
			final String name = args[0];
			final File file = new File(name);
			try {
				final FileInputStream fs = new FileInputStream(file);
				alphaForms.start(fs);
			} catch (final FileNotFoundException e) {
				alphaForms.start();
			}
			alphaFormFile = name;
		} else {
			alphaFormFile = jarDir
					+ File.separator
					+ ((jarFileName == null) ? OneJarStartup.DEFAULT_FORM_NAME
							: jarFileName.replaceFirst(".jar$", "")
									+ ".a-form.xml");
			final File file = new File(alphaFormFile);
			try {
				final FileInputStream fs = new FileInputStream(file);
				alphaForms.start(fs);
			} catch (final FileNotFoundException e) {
				alphaForms.start();
			}
		}

		alphaForms.registerSaveListener(new FormSaveListener() {

			@Override
			public void save(final ByteArrayOutputStream form) {
				try {
					final File out = new File(alphaFormFile);
					final FileOutputStream fs = new FileOutputStream(out);
					form.writeTo(fs);
				} catch (final FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
		System.out.println("alpha-form file: " + alphaFormFile);

		JFrame.setDefaultLookAndFeelDecorated(true);

		final JFrame window = new JFrame("alphaForms");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(new BorderLayout());

		window.getContentPane().add(alphaForms.getView());
		window.setSize(alphaForms.getView().getMinimumSize());
		window.setVisible(true);
	}

}
