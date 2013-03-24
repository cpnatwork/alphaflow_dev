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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import alpha.model.identification.AlphaCardID;
import alpha.overnet.event.AddAlphaCardEvent;
import alpha.overnet.event.ChangeAlphaCardDescriptorEvent;
import alpha.overnet.event.ChangePayloadEvent;

/**
 * This class utilizes the Observer pattern to observe events that change the
 * current AlphaDoc. Furthermore it is responsible for loading and storing the
 * document that contains the logged events.
 * 
 */
public class AlphadocEventLogger implements Observer {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphadocEventLogger.class.getName());

	/** The data. */
	private final List<String> data;

	/** The file. */
	private final File file;

	/** The notify. */
	private JPanel notify = null;

	/**
	 * This constructor loads the log documents and gets the AlphaDoc's homePath
	 * as a parameter.
	 * 
	 * @param homePath
	 *            the home path
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public AlphadocEventLogger(final String homePath) throws IOException {
		/*
		 * FIXME: removing the '/' should be possible, yet, it will result in a
		 * NPE (... *wtf*!)
		 */
		this.file = new File(homePath + "/bulletin.txt");
		if (this.file.createNewFile() == false) {
			AlphadocEventLogger.LOGGER.info("The file '" + this.file.getPath()
					+ "' already exists.");
		}
		// res_old = "init";
		final FileReader fr = new FileReader(this.file);
		final BufferedReader br = new BufferedReader(fr);
		this.data = new ArrayList<String>();
		for (int i = 0; i < 1000; i++) {
			final String line = br.readLine();
			if (line != null) {
				this.data.add(line);
			} else {
				break;
			}
		}
		br.close();
		fr.close();
	}

	/**
	 * This method serializes the logged events into the document in the file
	 * system.
	 * 
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void store(String value) throws IOException {
		final Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String dat = sdf.format(cal.getTime());
		sdf = new SimpleDateFormat("HH:mm:ss");
		final String tim = sdf.format(cal.getTime());
		final String time = dat + " " + tim;
		value = time + " " + value;
		if (!this.data.contains(value)) {
			this.data.add(value);
		}

		final FileWriter fw = new FileWriter(this.file);
		final BufferedWriter bw = new BufferedWriter(fw);
		// PrintWriter pw = new PrintWriter(bw);

		for (final String line : this.data) {
			bw.write(line);
			bw.newLine();
		}

		bw.flush();
		bw.close();
		fw.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlphadocEventLogger [data="
				+ Arrays.toString(this.data.toArray()) + "]";
	}

	/**
	 * This method returns a list with all events that occurred for the
	 * AlphaDoc.
	 * 
	 * @return the log
	 */
	public List<String> getLog() {
		return this.data;
	}

	/**
	 * Sets the notify.
	 * 
	 * @param notify
	 *            the new notify
	 */
	public void setNotify(final JPanel notify) {
		this.notify = notify;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(final Observable o, final Object arg) {
		try {
			String result = "";
			if (arg instanceof AddAlphaCardEvent) {
				final AddAlphaCardEvent aace = (AddAlphaCardEvent) arg;
				final AlphaCardID aci = aace.getAlphaCard().getId();
				result = "Added AlphaCardDescriptor: " + aci.getEpisodeID()
						+ "/" + aci.getCardID();
			} else if (arg instanceof ChangePayloadEvent) {
				final ChangePayloadEvent cpe = (ChangePayloadEvent) arg;
				final AlphaCardID aci = cpe.getAlphaCardID();
				result = "Changed Payload in AlphaCardDescriptor "
						+ aci.getEpisodeID() + "/" + aci.getCardID();
			} else if (arg instanceof ChangeAlphaCardDescriptorEvent) {
				final ChangeAlphaCardDescriptorEvent cae = (ChangeAlphaCardDescriptorEvent) arg;
				final AlphaCardID aci = cae.getAlphaCardID();
				result = "Changed alphaCardDescriptor " + aci.getEpisodeID()
						+ "/" + aci.getCardID() + ".";
			} else {
				result = arg.getClass().getName();
			}
			this.store(result);
			final NotifyUserThread nt = new NotifyUserThread(this.notify,
					result);
			nt.start();
		} catch (final IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.toString());
		}
	}
}
