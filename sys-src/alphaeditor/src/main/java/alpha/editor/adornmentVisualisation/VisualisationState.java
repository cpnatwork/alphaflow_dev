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
 * $Id: VisualisationState.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.editor.adornmentVisualisation;

import javax.swing.ImageIcon;

/**
 * The Enum VisualisationState.
 * 
 */
public enum VisualisationState {

	/** The AVAILABLE. */
	AVAILABLE("Available") {
		@Override
		public ImageIcon getIcon() {
			return null;
		}
	},

	/** The UNCHANGEABLE. */
	UNCHANGEABLE("Unchangeable") {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("emblem-readonly.png");
		}
	},

	/** The UPDATE. */
	UPDATE("Update") {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("accessories-text-editor.png");
		}
	},

	/** The DELETE. */
	DELETE("Delete") {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("list-remove.png");
		}
	},

	/** The UNAVAILABLE. */
	UNAVAILABLE("Not Available in Instance View") {
		@Override
		public ImageIcon getIcon() {
			return null;
		}
	},

	/** The ADD. */
	ADD("Add") {
		@Override
		public ImageIcon getIcon() {
			return this.getIconResource("list-add.png");
		}
	};

	/** The description. */
	private final String description;

	/**
	 * Instantiates a new visualisation state.
	 * 
	 * @param desc
	 *            the desc
	 */
	private VisualisationState(final String desc) {
		this.description = desc;
	}

	/**
	 * Description.
	 * 
	 * @return the string
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Gets the icon.
	 * 
	 * @return the icon
	 */
	public abstract ImageIcon getIcon();

	/**
	 * Gets the icon resource.
	 * 
	 * @param name
	 *            the name
	 * @return the icon resource
	 */
	protected ImageIcon getIconResource(final String name) {
		return new ImageIcon(this.getClass().getClassLoader()
				.getResource("icons/" + name));
	}

}
