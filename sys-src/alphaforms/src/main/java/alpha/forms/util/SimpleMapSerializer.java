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
import java.util.Map;
import java.util.Map.Entry;

/**
 * The Class SimpleMapSerializer.
 */
public class SimpleMapSerializer {

	/** The start token. */
	private final String startToken;

	/** The end token. */
	private final String endToken;

	/** The separator. */
	private final String separator;

	/** The kv separator. */
	private final String kvSeparator;

	/**
	 * Instantiates a new simple map serializer.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param separator
	 *            the separator
	 * @param kvSeparator
	 *            the kv separator
	 */
	public SimpleMapSerializer(final String start, final String end,
			final String separator, final String kvSeparator) {
		this.startToken = start;
		this.endToken = end;
		this.separator = separator;
		this.kvSeparator = kvSeparator;
	}

	/**
	 * Instantiates a new simple map serializer.
	 * 
	 * @param separator
	 *            the separator
	 */
	public SimpleMapSerializer(final String separator) {
		this("{", "}", separator, "=");
	}

	/**
	 * Instantiates a new simple map serializer.
	 */
	public SimpleMapSerializer() {
		this(",");
	}

	/**
	 * Serialize.
	 * 
	 * @param map
	 *            the map
	 * @return the string
	 */
	public String serialize(final Map<String, ?> map) {
		final StringBuilder sb = new StringBuilder();
		boolean start = true;
		sb.append(this.startToken);
		for (final Entry<String, ?> e : map.entrySet()) {
			if (!start) {
				sb.append(this.separator);
			}
			start = false;
			sb.append(e.getKey());
			sb.append(this.kvSeparator);
			if (e.getValue() instanceof Collection) {
				final SimpleListSerializer sl = new SimpleListSerializer();
				sb.append(sl.serialize((Collection<?>) e.getValue()));
			} else if (e.getValue() instanceof Map) {
				sb.append(this.serialize((Map<String, ?>) e.getValue()));
			} else {
				sb.append(e.getValue().toString());
			}
		}
		sb.append(this.endToken);
		return sb.toString();
	}
}
