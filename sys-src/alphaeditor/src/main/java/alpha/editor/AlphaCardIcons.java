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
 * $Id: AlphaCardIcons.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.editor;

import javax.swing.ImageIcon;

/**
 * The Enum AlphaCardIcons.
 */
public enum AlphaCardIcons {

	/** The DELETED. */
	DELETED {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("deleted.png");
		}

		@Override
		public String getDescription() {
			return "Card is deleted";
		}
	},

	/** The DEFERRED. */
	DEFERRED {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("deferred.png");
		}

		@Override
		public String getDescription() {
			return "Card is deferred";
		}
	},

	/** The DISPLAYE d_ card. */
	DISPLAYED_CARD {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("system-search.png");
		}

		@Override
		public String getDescription() {
			return "Displayed AlphaCard";
		}
	},

	/** The PRIORIT y_ low. */
	PRIORITY_LOW {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("low.png");
		}

		@Override
		public String getDescription() {
			return "Card has low priority";
		}
	},

	/** The PRIORIT y_ normal. */
	PRIORITY_NORMAL {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("normal.png");
		}

		@Override
		public String getDescription() {
			return "Card has normal priority";
		}
	},

	/** The PRIORIT y_ high. */
	PRIORITY_HIGH {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("high.png");
		}

		@Override
		public String getDescription() {
			return "Card has high priority";
		}
	},

	/** The VALIDIT y_ valid. */
	VALIDITY_VALID {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("valid.png");
		}

		@Override
		public String getDescription() {
			return "Card is valid";
		}
	},

	/** The VALIDIT y_ invalid. */
	VALIDITY_INVALID {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("invalid.png");
		}

		@Override
		public String getDescription() {
			return "Card is invalid";
		}
	},

	/** The VISIBILIT y_ public. */
	VISIBILITY_PUBLIC {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("public.png");
		}

		@Override
		public String getDescription() {
			return "Card is public";
		}
	},

	/** The VISIBILIT y_ private. */
	VISIBILITY_PRIVATE {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("private.png");
		}

		@Override
		public String getDescription() {
			return "Card is private";
		}
	},

	/** The PAYLOAD. */
	PAYLOAD {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("mail-attachment.png");
		}

		@Override
		public String getDescription() {
			return "Open payload";
		}

	},

	/** The alphaForms Payload. */
	AFORMS_PAYLOAD {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("aforms-attachment.png");
		}

		@Override
		public String getDescription() {
			return "Open alpha-Form";
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
