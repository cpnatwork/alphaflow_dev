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

import java.util.ArrayList;
import java.util.List;

/**
 * The Class SignalSink.
 */
public class SignalSink {

	/** The id. */
	private String id;

	/** The subscribers. */
	private final List<Subscriber> subscribers = new ArrayList<Subscriber>();

	/**
	 * Instantiates a new signal sink.
	 * 
	 * @param id
	 *            the id
	 */
	public SignalSink(final String id) {
		this.id = id;
	}

	/**
	 * Subscribe.
	 * 
	 * @param s
	 *            the s
	 */
	public void subscribe(final Subscriber s) {
		this.subscribers.add(s);
	}

	/**
	 * Unsubscribe.
	 * 
	 * @param s
	 *            the s
	 */
	public void unsubscribe(final Subscriber s) {
		this.subscribers.remove(s);
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Send.
	 * 
	 * @param sig
	 *            the sig
	 */
	public void send(final Signal sig) {
		sig.setSink(this);
		for (final Subscriber s : this.subscribers) {
			s.signalReceived(sig);
		}
	}
}
