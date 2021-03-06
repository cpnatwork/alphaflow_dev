/**************************************************************************
 * Hydra: multi-headed version control system
 * (originally for the alpha-Flow project)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Scott Hady
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
 * $Id: SCmdElementAdd.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package org.hydra.ui.commands;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hydra.core.Artifact;
import org.hydra.core.Configuration;
import org.hydra.core.Container;
import org.hydra.core.FingerprintedElement;
import org.hydra.core.Stage;

/**
 * Adds an element to the stage's container.
 *
 * @since 0.1
 * @version 0.2
 * @author Scott A. Hady
 */
public class SCmdElementAdd extends CommandStage {

	/** The Constant serialVersionUID. */
	public static final long serialVersionUID = 02L;

	/** The Constant DEFAULT_NAME. */
	public static final String DEFAULT_NAME = "Stage Add Element";

	/** The Constant DEFAULT_ID. */
	public static final String DEFAULT_ID = "SCmdElementAdd";

	/** The e name. */
	private String eName;

	/** The recursive. */
	private boolean recursive;

	/** The config. */
	private final Configuration config = Configuration.getInstance();
	// Regular Expressions
	/** The Constant cmdRegEx. */
	private static final String cmdRegEx = "^\\s*(?i:sadd)\\b";

	/** The Constant recRegEx. */
	private static final String recRegEx = "\\s+-(e(r)?|(r)?e)\\b";

	/** The Constant elRegEx. */
	private static final String elRegEx = "\\s+(\\S+.*)(\\\\|\\/)?\\s*$";

	/** The Constant cmdPattern. */
	private static final Pattern cmdPattern = Pattern
			.compile(SCmdElementAdd.cmdRegEx);

	/** The Constant completePattern. */
	private static final Pattern completePattern = Pattern
			.compile(SCmdElementAdd.cmdRegEx + SCmdElementAdd.recRegEx
					+ SCmdElementAdd.elRegEx);

	/** The Constant GROUP_RECURSIVE1. */
	private static final int GROUP_RECURSIVE1 = 2;

	/** The Constant GROUP_RECURSIVE2. */
	private static final int GROUP_RECURSIVE2 = 3;

	/** The Constant GROUP_ELEMENT. */
	private static final int GROUP_ELEMENT = 4;

	/**
	 * Specialized Constructor which designates which stage use add an element.
	 *
	 * @param stage
	 *            Stage.
	 */
	public SCmdElementAdd(final Stage stage) {
		super(SCmdElementAdd.DEFAULT_NAME, SCmdElementAdd.DEFAULT_ID, stage);
	}

	/**
	 * Specialized constructor; which specifies the stage and element's name to
	 * use.
	 *
	 * @param stage
	 *            Stage.
	 * @param eName
	 *            String.
	 */
	public SCmdElementAdd(final Stage stage, final String eName) {
		super(SCmdElementAdd.DEFAULT_NAME, SCmdElementAdd.DEFAULT_ID, stage);
		this.eName = eName;
		this.recursive = false;
	}

	/**
	 * Specialized constructor; which specifies the stage, element's name to use
	 * and if the element's content should be recursively added.
	 *
	 * @param stage
	 *            Stage.
	 * @param eName
	 *            String.
	 * @param recursive
	 *            boolean.
	 */
	public SCmdElementAdd(final Stage stage, final String eName,
			final boolean recursive) {
		super(SCmdElementAdd.DEFAULT_NAME, SCmdElementAdd.DEFAULT_ID, stage);
		this.eName = eName;
		this.recursive = recursive;
	}

	/**
	 * COMMAND METHODS OVERRIDDEN *********************************************.
	 * 
	 * @return the command pattern
	 */

	/**
	 * {@inheritDoc}
	 *
	 * Factory Method - Command Pattern accepts 'sadd' as the command.
	 */
	@Override
	public Pattern getCommandPattern() {
		return SCmdElementAdd.cmdPattern;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Factory Method - Complete Pattern accepts 'sadd -e{r} [ename]'.
	 */
	@Override
	public Pattern getCompletePattern() {
		return SCmdElementAdd.completePattern;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Template Method - Process the matcher to extract the logical unit,
	 * recursive flag and element name.
	 */
	@Override
	public boolean processMatcher(final Matcher matcher) {
		this.setRecursive(matcher);
		this.eName = matcher.group(SCmdElementAdd.GROUP_ELEMENT).trim();
		return true;
	}

	/**
	 * Determine if element and its content should be added recursively.
	 * 
	 * @param matcher
	 *            Matcher.
	 */
	private void setRecursive(final Matcher matcher) {
		if ((matcher.group(SCmdElementAdd.GROUP_RECURSIVE1) != null)
				|| (matcher.group(SCmdElementAdd.GROUP_RECURSIVE2) != null)) {
			this.recursive = true;
		} else {
			this.recursive = false;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Add the designated element to the designated logical unit.
	 */
	@Override
	public boolean execute() {
		boolean success = true;
		final File eFile = this.findElementFile(this.eName);
		FingerprintedElement element = null;
		try {
			if (eFile.isFile()) {
				element = new Artifact(eFile);
				success = this.stage.getContents().addElement(element);
			} else {
				element = new Container(eFile);
				if (this.recursive) {
					success = this.stage.getContents().addContainerAndContents(
							(Container) element);
				} else {
					success = this.stage.getContents().addElement(element);
				}
			}
			this.stage.recordReferences();
		} catch (final Exception e) {
			this.logger.exception("Unable to Add Element to Stage.", e);
			success = false;
		}
		if (success) {
			this.writer.println("Element [" + this.eName
					+ "] added to Stage.\n", this.cmdVerbosity);
		} else {
			this.writer.println("Unable to Add Element [" + this.eName
					+ "] to Stage.", this.cmdVerbosity);
			this.writer.println("\tFile [" + eFile + "] "
					+ (eFile.exists() ? "Does" : "Does Not") + " Exist.\n",
					this.cmdVerbosity);
		}
		return success;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Return a string describing the commands usage.
	 */
	@Override
	public String getUsage() {
		return "sAdd -e{r} <eName>\t\t\tAdds an Element to the Stage.";
	}

}
