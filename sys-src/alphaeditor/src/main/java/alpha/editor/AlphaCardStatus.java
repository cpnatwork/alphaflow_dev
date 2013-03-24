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

import java.awt.Color;

/**
 * This defines the AlphaCardStatus. Possible values: valid or invalid
 * 
 * 
 */
public enum AlphaCardStatus {

	/** The INVALIDPRIVATE. */
	INVALIDPRIVATE {
		@Override
		public Color getColor() {
			return Color.RED;
		}
	},

	/** The VALIDPRIVATE. */
	VALIDPRIVATE {
		@Override
		public Color getColor() {
			return Color.BLUE;
		}
	},

	/** The INVALIDPUBLIC. */
	INVALIDPUBLIC {
		@Override
		public Color getColor() {
			return Color.BLUE;
		}
	},

	/** The VALIDPUBLIC. */
	VALIDPUBLIC {
		@Override
		public Color getColor() {
			return Color.GREEN;
		}
	};

	/**
	 * Gets the color.
	 * 
	 * @return the color
	 */
	public abstract Color getColor();
}
