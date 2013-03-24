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
 * $Id: InsertionFileFilter.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.editor.insertionutil;

import java.io.File;

import javax.swing.filechooser.FileFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class InsertionFileFilter.
 * 
 * {@link FileFilter} implementation to sort our all files which are not
 * suitable for insertion into drools.
 */
public class InsertionFileFilter extends javax.swing.filechooser.FileFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(final File pathname) {

		if (pathname.getAbsolutePath()
				.substring(pathname.getAbsolutePath().lastIndexOf(".") + 1)
				.toLowerCase().equals("xml"))
			return true;
		else
			return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "XML-Files (*.xml) only";
	}
}