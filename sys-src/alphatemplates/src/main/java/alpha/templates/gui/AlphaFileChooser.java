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
 * $Id: AlphaFileChooser.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.templates.gui;

import java.io.File;

/**
 * Makes the user choose a location from the file system.
 */
public interface AlphaFileChooser {

	/**
	 * Choose a location from the file system.
	 * 
	 * @param pathFile
	 *            the path file
	 * @param output
	 *            the output
	 * @return the file
	 */
	public File choose(File pathFile, String output);

}
