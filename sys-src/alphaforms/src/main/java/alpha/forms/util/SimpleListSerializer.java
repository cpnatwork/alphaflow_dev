/**************************************************************************
 * alpha-Forms: self-editable formulars in form of an active document
 * (originally for the alpha-Flow project)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Florian Wagner
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
package alpha.forms.util;

import java.util.Collection;

/**
 * The Class SimpleListSerializer.
 */
public class SimpleListSerializer {

	/** The start token. */
	private final String startToken;

	/** The end token. */
	private final String endToken;

	/** The separator. */
	private final String separator;

	/**
	 * Instantiates a new simple list serializer.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param separator
	 *            the separator
	 */
	public SimpleListSerializer(final String start, final String end,
			final String separator) {
		this.startToken = start;
		this.endToken = end;
		this.separator = separator;
	}

	/**
	 * Instantiates a new simple list serializer.
	 * 
	 * @param separator
	 *            the separator
	 */
	public SimpleListSerializer(final String separator) {
		this("[", "]", separator);
	}

	/**
	 * Instantiates a new simple list serializer.
	 */
	public SimpleListSerializer() {
		this(",");
	}

	/**
	 * Serialize.
	 * 
	 * @param list
	 *            the list
	 * @return the string
	 */
	public String serialize(final Collection<?> list) {
		final StringBuilder sb = new StringBuilder();
		boolean start = true;
		sb.append(this.startToken);
		for (final Object item : list) {
			if (!start) {
				sb.append(this.separator);
			}
			start = false;
			if (item instanceof Collection) {
				sb.append(this.serialize((Collection<?>) item));
			} else {
				sb.append(item.toString());
			}
		}
		sb.append(this.endToken);
		return sb.toString();
	}

}
