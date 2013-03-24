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
 * $Id: ConcurrentModificationWarning.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.offsync.time;

import javax.swing.JOptionPane;

import alpha.model.AlphaCardDescriptor;

/**
 * GUI dialog for displaying a graphical warning if a concurrent modification is
 * detected.
 * 
 * Note for alpha-Conjoint: Information about differences between
 * conflicting/reconiled versions of an alphaCard are supposed to be displayed
 * here.
 */
public class ConcurrentModificationWarning {

	/**
	 * Shows concurrent modification warning popup.
	 * 
	 * @param acd
	 *            AlphaCardDescriptor of the conflicting/reconciled alphaCard
	 */
	public static void showConcurrentModificationWarning(
			final AlphaCardDescriptor acd) {

		final Object[] options = { "Proceed",
				"Show differences between versions (\u03B1-Conjoint)" };

		final int selection = JOptionPane.showOptionDialog(null,
				"Concurrent modification detected! \nAlphaCard with ID "
						+ acd.getId().getCardID()
						+ " was reset to last valid state.",
				"Important process status information",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				options, options[0]);

		if (selection == 1) {
			// FIXME a-Conjoint: Show diff here
		}

	}

}
