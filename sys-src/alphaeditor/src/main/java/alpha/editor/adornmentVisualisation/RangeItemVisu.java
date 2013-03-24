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
 * $Id: RangeItemVisu.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.editor.adornmentVisualisation;

import javax.swing.JTextField;

/**
 * The Class RangeItemVisu.
 */
public class RangeItemVisu {

	/** The item. */
	private JTextField item;

	/** The state. */
	private VisualisationState state;

	/**
	 * Instantiates a new type range item visu.
	 * 
	 * @param name
	 *            the name
	 * @param state
	 *            the state
	 */
	public RangeItemVisu(final String name, final VisualisationState state) {

		this.item = new JTextField(name);
		this.item.setName(name);
		this.state = state;
	}

	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public JTextField getItem() {
		return this.item;
	}

	/**
	 * Sets the item.
	 * 
	 * @param item
	 *            the item to set
	 */
	public void setItem(final JTextField item) {
		this.item = item;
	}

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	public VisualisationState getState() {
		return this.state;
	}

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(final VisualisationState state) {
		this.state = state;
	}

}
