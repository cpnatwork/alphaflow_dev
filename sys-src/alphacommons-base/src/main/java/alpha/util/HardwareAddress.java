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
 * $Id: HardwareAddress.java 3579 2012-02-15 10:59:19Z cpn $
 *************************************************************************/
package alpha.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Encapsulates static behavior to retrieve the MAC address of the local
 * machine.
 */
public class HardwareAddress {

	/**
	 * Gets the MAC address of the current local machine.
	 * 
	 * @return the hardware address of the local machine. If no hardware address
	 *         can be obtained an empty {@link String} is returned.
	 * 
	 */
	public static String getHardwareAddress() {

		try {
			final StringBuilder strb = new StringBuilder();

			for (final Enumeration<NetworkInterface> enm = NetworkInterface
					.getNetworkInterfaces(); enm.hasMoreElements();) {

				final NetworkInterface network = enm.nextElement();

				// Check if hardware address from specified network interface
				// can be accessed
				if (null != network.getHardwareAddress()) {
					final byte[] hwAddress = network.getHardwareAddress();
					for (int i = 0; i < hwAddress.length; i++) {
						// Create formatted output
						strb.append(String.format("%02X%s", hwAddress[i],
								(i < (hwAddress.length - 1)) ? "-" : ""));
					}
					// Valid hardware address could be obtained
					if (hwAddress.length != 0)
						return strb.toString();
				}
			}

		} catch (final SocketException e) {
			e.printStackTrace();
		}

		// No hardware address could be obtained.
		return new String("");
	}

}
