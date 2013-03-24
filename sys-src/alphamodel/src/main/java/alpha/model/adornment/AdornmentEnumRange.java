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
 * $Id: AdornmentEnumRange.java 3597 2012-02-20 15:13:03Z cpn $
 *************************************************************************/
package alpha.model.adornment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.logging.Logger;


/**
 * The Class AdornmentEnumRange.
 *
 * @author cpn
 * @version $Id: AdornmentEnumRange.java 3597 2012-02-20 15:13:03Z cpn $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "rangeItems" })
@XmlRootElement(name = "rangeItems")
public class AdornmentEnumRange implements AdpEnum<String> {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AdornmentEnumRange.class.getName());

	/**
	 * The range items.
	 * 
	 * @aggregation
	 * */
	@XmlElement(name = "rangeItem", required = true)
	protected final List<String> rangeItems;

	/**
	 * Instantiates a new adaptive adornment enum range.
	 */
	public AdornmentEnumRange() {
		this(null);
	}

	/**
	 * Instantiates a new adaptive adornment enum range.
	 *
	 * @param range
	 *            the range
	 */
	public AdornmentEnumRange(final String[] range) {
		this.rangeItems = new ArrayList<String>();
		if (range != null) {
			for (final String s : range) {
				this.rangeItems.add(s.toUpperCase());
				AdornmentEnumRange.LOGGER.finer("Added new range item '"
						+ this.valueOf(s) + "'");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.apa.AdpEnum#exists(java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean exists(final String enumValue) {
		return this.rangeItems.contains(enumValue.toUpperCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.apa.AdpEnum#ordinal(java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public int ordinal(final String enumValue) {
		return this.rangeItems.indexOf(enumValue.toUpperCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.apa.AdpEnum#range(java.lang.Object, java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public Set<String> range(final String from, final String to) {
		final Set<String> range = new LinkedHashSet<String>();
		final int fromOrdinal = this.ordinal(from);
		final int toOrdinal = this.ordinal(to);

		if ((fromOrdinal < 0) || (toOrdinal < 0)) {
			AdornmentEnumRange.LOGGER
					.warning("At least one of 'from' and 'to' is out of range.");
		}

		for (final String s : this.rangeItems) {
			if ((this.ordinal(s) >= fromOrdinal)
					&& (this.ordinal(s) <= toOrdinal)) {
				AdornmentEnumRange.LOGGER.finer("range item is within range: "
						+ s);
				range.add(s);
			}
		}
		return range;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.apa.AdpEnum#valueOf(java.lang.String)
	 */
	/** {@inheritDoc} */
	@Override
	public String valueOf(final String name) {
		if (this.ordinal(name) == -1) {
			AdornmentEnumRange.LOGGER
					.warning("Searched item does not match with any range item.");
			return null;
		}
		return this.rangeItems.get(this.ordinal(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.apa.AdpEnum#values()
	 */
	/** {@inheritDoc} */
	@Override
	public List<String> values() {
		return this.rangeItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public int compare(final String arg0, final String arg1) {
		final int result = this.ordinal(arg1) - this.ordinal(arg0);
		AdornmentEnumRange.LOGGER.finer("Compare " + arg0 + " with " + arg1
				+ ": " + result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.apa.AdpEnum#extend(java.lang.String, int)
	 */
	/** {@inheritDoc} */
	@Override
	public void extend(final String item, final int position) {
		this.rangeItems.add(position, item.toUpperCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.apa.AdpEnum#extend(java.lang.String)
	 */
	/** {@inheritDoc} */
	@Override
	public void extend(final String item) {
		this.rangeItems.add(item.toUpperCase());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.apa.AdpEnum#narrow(java.lang.String)
	 */
	/** {@inheritDoc} */
	@Override
	public void narrow(final String item) {
		this.rangeItems.remove(item);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.apa.AdpEnum#order(java.lang.String[])
	 */
	/** {@inheritDoc} */
	@Override
	public void order(final String[] items) {
		// TODO order rangeItems according to the given Array of items
		// Future Work
	}

}
