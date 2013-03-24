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
package alpha.editor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import alpha.editor.panels.AlphaCardAdornmentInstancesPanel;
import alpha.editor.panels.AlphaCardAdornmentSchemaPanel;
import alpha.editor.panels.ContentCardAdornmentPrototypePanel;
import alpha.model.docconfig.AlphadocConfig;
import alpha.model.identification.AlphaCardID;

/**
 * This class contains the GUI widgets and other important objects that the
 * class Editor needs.
 * 
 * Design goal (sipeschw): Editor is used to determine the user interface
 * structure, but to hold handles to any graphical components inside EditorData.
 * 
 */
@Component
public class EditorData {

	/** The Constant HSIZE. */
	public final static int HSIZE = 1400;

	/** The Constant VSIZE. */
	public final static int VSIZE = 1024;

	/** The current. */
	private AlphaCardID current;

	/** The config. */
	private AlphadocConfig config;

	/** The basic. */
	protected JPanel basic;

	/* Card overview */
	/** The gbc. */
	protected GridBagConstraints gbc;

	/** The home path. */
	protected String homePath;

	/* cyclic-dependencies: use PostConstruct with ApplicationContext */

	/** The application context. */
	@Autowired
	private ApplicationContext applicationContext;

	/** The ad prototype panel. */
	private ContentCardAdornmentPrototypePanel adPrototypePanel;

	/** The ad schema panel. */
	private AlphaCardAdornmentSchemaPanel adSchemaPanel;

	/** The ad instance panel. */
	private AlphaCardAdornmentInstancesPanel adInstancePanel;

	/** The notify. */
	protected JPanel notify;

	/**
	 * Inits the.
	 */
	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		this.adPrototypePanel = this.applicationContext
				.getBean(ContentCardAdornmentPrototypePanel.class);
		this.adSchemaPanel = this.applicationContext
				.getBean(AlphaCardAdornmentSchemaPanel.class);
		this.adInstancePanel = this.applicationContext
				.getBean(AlphaCardAdornmentInstancesPanel.class);
	}

	/**
	 * Inits the.
	 * 
	 * @param aci
	 *            the aci
	 * @param config
	 *            the config
	 * @param homePath
	 *            the home path
	 */
	public void init(final AlphaCardID aci, final AlphadocConfig config,
			final String homePath) {
		this.basic = new JPanel(new BorderLayout());
		this.gbc = new GridBagConstraints();

		this.config = config;
		this.current = aci;
		this.homePath = homePath;
	}

	/**
	 * Gets the current.
	 * 
	 * @return the current
	 */
	public AlphaCardID getCurrent() {
		return this.current;
	}

	/**
	 * Sets the current.
	 * 
	 * @param current
	 *            the current to set
	 */
	public void setCurrent(final AlphaCardID current) {
		this.current = current;
	}

	/**
	 * Gets the config.
	 * 
	 * @return the config
	 */
	public AlphadocConfig getConfig() {
		return this.config;
	}

	/**
	 * Gets the basic.
	 * 
	 * @return the basic
	 */
	public JPanel getBasic() {
		return this.basic;
	}

	/**
	 * Sets the basic.
	 * 
	 * @param basic
	 *            the basic to set
	 */
	public void setBasic(final JPanel basic) {
		this.basic = basic;
	}

	/**
	 * Gets the adornments instances panel.
	 * 
	 * @return the adaptiveAdornmentsInstancesPanel
	 */
	public AlphaCardAdornmentInstancesPanel getAdornmentsInstancesPanel() {
		return this.adInstancePanel;
	}

	/**
	 * Gets the adornments schema panel.
	 * 
	 * @return the adSchemaPanel
	 */
	public AlphaCardAdornmentSchemaPanel getAdornmentsSchemaPanel() {
		return this.adSchemaPanel;
	}

	/**
	 * Gets the adornment_prototype.
	 * 
	 * @return the adPrototypePanel
	 */
	public ContentCardAdornmentPrototypePanel getAdornment_prototype() {
		this.adPrototypePanel.initAdornmentVisu();
		this.adPrototypePanel.displayAdornments();
		return this.adPrototypePanel;
	}

	/**
	 * Gets the gbc.
	 * 
	 * @return the gbcBasic
	 */
	public GridBagConstraints getGbc() {
		return this.gbc;
	}

	/**
	 * Gets the home path.
	 * 
	 * @return the homePath
	 */
	public String getHomePath() {
		return this.homePath;
	}

	/**
	 * Sets the home path.
	 * 
	 * @param homePath
	 *            the new home path
	 */
	public void setHomePath(final String homePath) {
		this.homePath = homePath;

	}

	/**
	 * Gets the notify.
	 * 
	 * @return the notify
	 */
	public JPanel getNotify() {
		return this.notify;
	}

	/**
	 * Sets the notify.
	 * 
	 * @param notify
	 *            the notify to set
	 */
	public void setNotify(final JPanel notify) {
		this.notify = notify;
	}

}