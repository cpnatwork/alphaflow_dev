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
package alpha.props.test.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The Class ServiceTest.
 */
public class ServiceTest {

	/**
	 * Main.
	 * 
	 * @param args
	 *            the args
	 * @return the int
	 */
	public static int main(final String[] args) {
		final BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		String userInput;

		try {
			while (true) {
				System.out.print(">");
				System.out.flush();
				userInput = stdIn.readLine();
				if (userInput != null) {
					System.out.println(userInput);
					if (userInput.equals("exit")) {
						System.exit(0);
					}
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return 0;
	}

}
