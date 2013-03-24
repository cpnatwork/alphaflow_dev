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
package alpha.props.test.utility;

import java.io.File;
import java.io.FilenameFilter;

/**
 * The Class FileExtensionFilter.
 */
public class FileExtensionFilter implements FilenameFilter {

	/** The ext. */
	protected String ext[];

	/**
	 * Instantiates a new file extension filter.
	 * 
	 * @param extensions
	 *            the extensions
	 */
	public FileExtensionFilter(final String extensions[]) {
		this.ext = extensions;
		for (int i = 0; i < this.ext.length; i++) {
			this.ext[i] = "." + this.ext[i];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(final File dir, final String name) {
		for (final String element : this.ext) {
			if (name.endsWith(element))
				return true;
		}
		return false;
	}

	/**
	 * Gets the file extension.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the file extension
	 */
	public static String getFileExtension(final String fileName) {
		final File tmpFile = new File(fileName);
		tmpFile.getName();
		final int whereDot = tmpFile.getName().lastIndexOf('.');
		if ((0 < whereDot) && (whereDot <= (tmpFile.getName().length() - 2)))
			return tmpFile.getName().substring(whereDot + 1);
		return "";
	}

}
