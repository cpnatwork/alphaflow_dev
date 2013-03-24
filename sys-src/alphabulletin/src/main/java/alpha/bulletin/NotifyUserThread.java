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
package alpha.bulletin;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.logging.Logger;


/**
 * The Class NotifyUserThread.
 * 
 * 
 * This thread informs the user about new changes on the AlphaDoc
 */
public class NotifyUserThread extends Thread {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(NotifyUserThread.class.getName());

	/** The notify. */
	private JPanel notify;

	/** The event. */
	private final String event;

	/** The s_event label. */
	private static JLabel s_eventLabel = new JLabel();

	{
		NotifyUserThread.s_eventLabel.setForeground(new Color(0, 150, 0));
	}

	/**
	 * Instantiates a new notify user thread.
	 * 
	 * @param notify
	 *            the notify
	 * @param event
	 *            the event
	 */
	public NotifyUserThread(final JPanel notify, final String event) {
		this.notify = notify;
		this.event = event;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO this notify handling seems strange...
		if (this.notify == null) {
			this.notify = new JPanel();
		} else {
			this.notify.removeAll();
		}
		NotifyUserThread.s_eventLabel.setText(this.event);
		this.notify.add(NotifyUserThread.s_eventLabel);
		this.notify.updateUI();
		this.notify.setVisible(true);
		try {
			Thread.sleep(5000);
		} catch (final InterruptedException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Sleep failed");
			NotifyUserThread.LOGGER.severe("Error: " + e);
		}
		this.notify.setVisible(false);
		this.notify.remove(NotifyUserThread.s_eventLabel);
	}

}
