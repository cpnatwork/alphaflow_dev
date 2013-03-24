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
 * $Id: TokenIcons.java 3792 2012-05-05 10:27:03Z uj32uvac $
 *************************************************************************/
package alpha.editor.tokenVisualisation;

import javax.swing.ImageIcon;

/**
 * The Enum TokenIcons.
 */
public enum TokenIcons {

	/** The Initiator. */
	INITIATOR {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("initiator.png");
		}

		@Override
		public String getDescription() {
			return "Participant is Initiator";
		}
	},

	/** The Doyen. */
	DOYEN {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("doyen.png");
		}

		@Override
		public String getDescription() {
			return "Participant is Doyen";
		}
	},

	/** The NewDoyen. */
	NEWDOYEN {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("new-doyen.png");
		}

		@Override
		public String getDescription() {
			return "Participant is the new Doyen";
		}
	},

	/** The PatientContact. */
	PATIENT_CONTACT {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("patient-contact.png");
		}

		@Override
		public String getDescription() {
			return "Participant has Patient Contact";
		}
	};

	/**
	 * Gets the icon.
	 * 
	 * @return the icon
	 */
	public abstract ImageIcon getIcon();

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public abstract String getDescription();

	/**
	 * Gets the icon resource.
	 * 
	 * @param name
	 *            the name
	 * @return the icon resource
	 */
	ImageIcon getIconResource(final String name) {
		return new ImageIcon(this.getClass().getClassLoader()
				.getResource("icons/" + name));
	}

}
