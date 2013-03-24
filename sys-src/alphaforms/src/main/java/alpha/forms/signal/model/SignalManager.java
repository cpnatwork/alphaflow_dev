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
package alpha.forms.signal.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class SignalManager.
 */
public class SignalManager {

	/** The instance. */
	private static SignalManager instance;

	/** The sink map. */
	private final Map<String, SignalSink> sinkMap = new HashMap<String, SignalSink>();

	/**
	 * Instantiates a new signal manager.
	 */
	private SignalManager() {

	}

	/**
	 * Gets the single instance of SignalManager.
	 * 
	 * @return single instance of SignalManager
	 */
	public static SignalManager getInstance() {
		if (SignalManager.instance == null) {
			SignalManager.instance = new SignalManager();
		}
		return SignalManager.instance;
	}

	/**
	 * Subscribe sink.
	 * 
	 * @param s
	 *            the s
	 * @param sinkName
	 *            the sink name
	 */
	public void subscribeSink(final Subscriber s, final String sinkName) {
		SignalSink q = this.sinkMap.get(sinkName);
		if (q == null) {
			q = new SignalSink(sinkName);
			this.sinkMap.put(sinkName, q);
		}
		q.subscribe(s);
	}

	/**
	 * Send signal.
	 * 
	 * @param signal
	 *            the signal
	 * @param sinkName
	 *            the sink name
	 */
	public void sendSignal(final Signal signal, final String sinkName) {
		SignalSink q = this.sinkMap.get(sinkName);
		if (q == null) {
			q = new SignalSink(sinkName);
			this.sinkMap.put(sinkName, q);
		}
		q.send(signal);
	}

	/**
	 * Unsubscribe sink.
	 * 
	 * @param s
	 *            the s
	 * @param sinkName
	 *            the sink name
	 */
	public void unsubscribeSink(final Subscriber s, final String sinkName) {
		final SignalSink q = this.sinkMap.get(sinkName);
		if (q != null) {
			q.unsubscribe(s);
		}
	}
}
