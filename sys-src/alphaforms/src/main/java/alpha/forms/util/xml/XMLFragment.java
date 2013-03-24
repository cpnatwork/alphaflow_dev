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
package alpha.forms.util.xml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import alpha.forms.util.SimpleListSerializer;

/**
 * The Class XMLFragment.
 */
public class XMLFragment {

	/** The attributes. */
	private final Map<String, Object> attributes = new HashMap<String, Object>();

	/** The value. */
	private String value;

	/** The tag. */
	private String tag;

	/**
	 * Instantiates a new xML fragment.
	 * 
	 * @param value
	 *            the value
	 */
	public XMLFragment(final Object value) {
		if (value != null) {
			this.value = value.toString();
		}
	}

	/**
	 * Instantiates a new xML fragment.
	 */
	public XMLFragment() {
		this.value = null;
	}

	/**
	 * With attributes.
	 * 
	 * @param attributes
	 *            the attributes
	 * @return the xML fragment
	 */
	public XMLFragment withAttributes(final Map<String, Object> attributes) {
		this.attributes.putAll(attributes);
		return this;
	}

	/**
	 * With attribute.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the xML fragment
	 */
	public XMLFragment withAttribute(final String key, final Object value) {
		this.attributes.put(key, value);
		return this;
	}

	/**
	 * Wrap in.
	 * 
	 * @param tag
	 *            the tag
	 * @return the xML fragment
	 */
	public XMLFragment wrapIn(final String tag) {
		this.tag = tag;
		return this;
	}

	/**
	 * Wrap in cdata.
	 * 
	 * @return the xML fragment
	 */
	public XMLFragment wrapInCDATA() {
		this.value = "<![CDATA[" + this.value + "]]>";
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<").append(this.tag);
		for (final Entry<String, Object> e : this.attributes.entrySet()) {
			sb.append(" ").append(e.getKey()).append("=\"");
			if (e.getValue() instanceof Collection) {
				final SimpleListSerializer sl = new SimpleListSerializer();
				sb.append(sl.serialize((Collection<?>) e.getValue()));
			} else {
				sb.append(e.getValue());
			}
			sb.append("\"");
		}
		if (this.value != null) {
			sb.append(">").append(this.value).append("</").append(this.tag)
					.append(">");
		} else {
			sb.append(" />");
		}
		return sb.toString();
	}

}
