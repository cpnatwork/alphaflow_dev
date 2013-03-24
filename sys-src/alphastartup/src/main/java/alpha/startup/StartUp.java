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
package alpha.startup;

import java.util.logging.Handler;
import java.util.logging.LogManager;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.org.lidalia.sysoutslf4j.context.LogLevel;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

/**
 * Here it all begins...
 * 
 */
public class StartUp {

	/** The factory. */
	static private BeanFactory factory = null;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {

		StartUp.initLoggingFramework();
		StartUp.initDependencyInjectionFramework();
		// Start the engine
		StartUp.go(args);
	}

	/**
	 * Inits the logging.
	 */
	private static void initLoggingFramework() {

		/* redirect System.out|err to slf4j */
		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J(LogLevel.DEBUG,
				LogLevel.WARN);

		/*
		 * redirect JUL (java.util.logging) to slf4j
		 * 
		 * [The SLF4JBridgeHandler will NOT remove the handler that is there by
		 * default; in conclusion, the bridge will produce doubled logging
		 * output. The following is necessary to avoid having everything logged
		 * twice. ]
		 */
		final java.util.logging.Logger rootLogger = LogManager.getLogManager()
				.getLogger("");
		for (final Handler handler : rootLogger.getHandlers()) {
			rootLogger.removeHandler(handler);
		}
		/*
		 * SLF4JBridgeHandler.install() installs an extra Handler on JUL's root
		 * logger
		 */
		SLF4JBridgeHandler.install();

		/*
		 * ... instead of removing the JUL-handlers, it would be possible to
		 * configure JUL ...
		 */
		// try {
		// LogManager.getLogManager().readConfiguration(
		// StartUp.class.getResourceAsStream("logging.properties"));
		// } catch (final Exception ex) {
		// ex.printStackTrace();
		// }

	}

	/**
	 * Inits the dependency injection.
	 */
	static private void initDependencyInjectionFramework() {
		StartUp.factory = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
	}

	/**
	 * Go.
	 * 
	 * @param args
	 *            the args
	 */
	static private void go(final String[] args) {
		final StarterBean starter = (StarterBean) StartUp.factory
				.getBean("starterBean");
		starter.go(args);
	}

}
