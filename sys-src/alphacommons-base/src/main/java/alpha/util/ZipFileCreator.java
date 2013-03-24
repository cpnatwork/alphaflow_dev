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
 * $Id: ZipFileCreator.java 3579 2012-02-15 10:59:19Z cpn $
 *************************************************************************/
package alpha.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Offers functionality to generate a zip-File from a directory`s contents.
 */
public class ZipFileCreator {

	/**
	 * Creates a zip-File from the specified directory and writes it to the
	 * specified file.
	 * 
	 * @param inputPath
	 *            the path of the directory to be zipped
	 * @param outputFile
	 *            the file for the output data
	 * @param docName
	 *            the doc name
	 */
	public static void createZipFile(final String inputPath,
			final File outputFile, final String docName) {
		final File inputDirectory = new File(inputPath);
		final List<File> fileList = new ArrayList<File>();
		ZipFileCreator.createFileList(inputDirectory, fileList, docName, true);
		ZipFileCreator.createZipFromFiles(inputDirectory, outputFile, fileList);
	}

	/**
	 * Recursively creates a list of the contents of a directory.
	 * 
	 * @param dir
	 *            the directory to be indexed
	 * @param fileList
	 *            the resulting file list
	 * @param docName
	 *            the doc name
	 * @param filter
	 *            the filter
	 */
	private static void createFileList(final File dir,
			final List<File> fileList, final String docName,
			final boolean filter) {
		final File[] files = dir.listFiles();
		for (final File file : files) {
			if (file.getAbsolutePath().contains(docName + ".jar") && filter) {
				fileList.add(file);
			}
			if (!filter) {
				fileList.add(file);
			}
			if (file.isDirectory() && file.getAbsolutePath().contains(docName)) {
				ZipFileCreator.createFileList(file, fileList, docName, false);
			}
		}
	}

	/**
	 * Creates a zip-File from a file list.
	 * 
	 * @param inputDirectory
	 *            the input directory
	 * @param outputFile
	 *            the output file
	 * @param fileList
	 *            the file list
	 */
	private static void createZipFromFiles(final File inputDirectory,
			final File outputFile, final List<File> fileList) {

		try {
			final FileOutputStream fos = new FileOutputStream(outputFile);
			final ZipOutputStream zos = new ZipOutputStream(fos);

			for (final File file : fileList) {
				if (!file.isDirectory()) {
					ZipFileCreator.addFileToZip(inputDirectory, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a file to zip-File.
	 * 
	 * @param directoryToZip
	 *            the directory to be zipped
	 * @param file
	 *            the file to be added
	 * @param zos
	 *            the {@link ZipOutputStream} for the resulting zip-File
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void addFileToZip(final File directoryToZip,
			final File file, final ZipOutputStream zos) throws IOException {

		final FileInputStream fis = new FileInputStream(file);

		final String zipFilePath = file.getCanonicalPath().substring(
				directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		final ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		final byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
}