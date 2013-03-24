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
 * $Id: RuntimeLogbasenamePropertyDefiner.java 3598 2012-02-20 18:33:33Z cpn $
 *************************************************************************/
package alpha.logging;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import ch.qos.logback.core.PropertyDefinerBase;

/**
 * The Class RuntimeLogfilePropertyDefiner.
 * 
 */
public class RuntimeLogbasenamePropertyDefiner extends PropertyDefinerBase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.qos.logback.core.spi.PropertyDefiner#getPropertyValue()
	 */
	@Override
	public String getPropertyValue() {

		String selfURI = RuntimeLogbasenamePropertyDefiner.getClassURI(this
				.getClass());
		selfURI = RuntimeLogbasenamePropertyDefiner
				.removeClassUriPrefix(selfURI);
		selfURI = RuntimeLogbasenamePropertyDefiner
				.removePostJarSuffix(selfURI);
		final String homePath = RuntimeLogbasenamePropertyDefiner
				.removeExtention(selfURI) + "/";
		final boolean selfIsExistingAlphaDoc = new File(homePath).exists();

		final String basename = RuntimeLogbasenamePropertyDefiner
				.removeExtention(RuntimeLogbasenamePropertyDefiner
						.getRunningJarName(this.getClass()));
		return (selfIsExistingAlphaDoc) ? basename + "/" + basename : basename;
	}

	/**
	 * Gets the class uri.
	 * 
	 * @param clazz
	 *            the clazz
	 * @return the class uri
	 * @throws RuntimeException
	 *             the runtime exception
	 */
	public static String getClassURI(
			@SuppressWarnings("rawtypes") final Class clazz)
			throws RuntimeException {
		URI classURI = null;
		try {
			classURI = clazz.getProtectionDomain().getCodeSource()
					.getLocation().toURI();
		} catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}

		final String selfString = classURI.toString();
		return selfString;
	}

	/**
	 * Checks if is running as jar.
	 * 
	 * @param classURI
	 *            the class uri
	 * @return true, if is running as jar
	 */
	public static boolean isRunningAsJar(final String classURI) {
		return classURI.startsWith("jar:");
	}

	/**
	 * Removes the extention.
	 * 
	 * @param filePath
	 *            the file path
	 * @return the string
	 */
	public static String removeExtention(final String filePath) {
		final int lastPeriodPos = filePath.lastIndexOf('.');
		// if there is a '.' then remove it:
		return (lastPeriodPos == -1) ? filePath : filePath.substring(0,
				lastPeriodPos);
	}

	/**
	 * Removes the class uri prefix.
	 * 
	 * @param classURI
	 *            the class uri
	 * @return the string
	 */
	public static String removeClassUriPrefix(final String classURI) {

		String unprefixed = classURI.replace("jar:", "");

		final String osname = System.getProperty("os.name");
		// Detect Mac OS X from Java:
		// http://developer.apple.com/library/mac/#technotes/tn2002/tn2110.html
		if (osname.toLowerCase().contains("linux")
				|| osname.startsWith("Mac OS X")) {
			unprefixed = unprefixed.replace("file:", "");
		} else {
			unprefixed = unprefixed.replace("file:/", "");
		}

		return unprefixed;
	}

	/**
	 * Removes the post jar suffix.
	 * 
	 * @param classURI
	 *            the class uri
	 * @return the string
	 */
	public static String removePostJarSuffix(final String classURI) {
		int seperator = classURI.length();
		if (classURI.contains("!")) {
			seperator = classURI.lastIndexOf('!');
		}
		return classURI.substring(0, seperator);
	}

	/**
	 * Gets the running jar name.
	 * 
	 * @param clazz
	 *            the clazz
	 * @return the running jar name
	 */
	public static String getRunningJarName(
			@SuppressWarnings("rawtypes") final Class clazz) {
		final String classURI = RuntimeLogbasenamePropertyDefiner
				.getClassURI(clazz);

		String jarName = null;
		if (RuntimeLogbasenamePropertyDefiner.isRunningAsJar(classURI)) {
			final String vals[] = classURI.split("/");
			for (final String val : vals) {
				// skip all path fragments until the one with the '!'
				if (val.contains("!")) {
					jarName = val.substring(0, val.length() - 1);
					break;
				}
			}
		}
		return jarName;
	}

}
