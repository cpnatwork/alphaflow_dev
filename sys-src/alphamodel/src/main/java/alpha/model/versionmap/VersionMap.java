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
 * $Id: VersionMap.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.versionmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import alpha.model.timestamp.LogicalTimestamp;
import alpha.model.timestamp.Occurrence;

/**
 * Subtype of logical timestamp for detecting mutual inconsistency in a
 * distributed system.
 *
 * @author cpn
 * @version $Id: VersionMap.java 3583 2012-02-16 01:52:45Z cpn $
 */
public class VersionMap implements LogicalTimestamp {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String args[]) {
		final VersionMap vv1 = new VersionMap("A", 5);
		vv1.putEntry("B", 1);
		vv1.putEntry("C", 1);

		final VersionMap vv2 = new VersionMap("A", 5);
		vv2.putEntry("B", 1);
		vv2.putEntry("C", 3);

		System.out.println(vv1.compare(vv2));
		System.out.println(vv2.compare(vv1));
		System.out.println(((VersionMap) vv1.merge(vv2)).versionCounters
				.toString());

		System.out.println(vv1.getDistance(vv2));
	}

	/** Representation of the version counters. */
	@XmlJavaTypeAdapter(VersionMapAdapter.class)
	public HashMap<String, Long> versionCounters;

	/**
	 * Instantiates a new empty {@link VersionMap}.
	 */
	public VersionMap() {
		this.versionCounters = new HashMap<String, Long>();
	}

	/**
	 * Instantiates a new {@link VersionMap}.
	 *
	 * @param initalMailAddress
	 *            the inital mail address
	 * @param initialVersionCount
	 *            the initial version count
	 */
	public VersionMap(final String initalMailAddress,
			final long initialVersionCount) {
		this.versionCounters = new HashMap<String, Long>();
		this.versionCounters.put(initalMailAddress, initialVersionCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.timestamp.LogicalTimestamp#putEntry(java.lang.String,
	 * long)
	 */
	/** {@inheritDoc} */
	@Override
	public void putEntry(final String actor, final long count) {
		this.versionCounters.put(actor, count);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.timestamp.LogicalTimestamp#getNumberOfModifications()
	 */
	/** {@inheritDoc} */
	@Override
	public long getNumberOfModifications() {
		long count = 0;
		for (final String key : this.versionCounters.keySet()) {
			count += this.getNumberOfModifications(key);
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.model.timestamp.LogicalTimestamp#getNumberOfModifications(java.
	 * lang.String)
	 */
	/** {@inheritDoc} */
	@Override
	public long getNumberOfModifications(final String actor) {
		if (this.versionCounters.containsKey(actor))
			return this.versionCounters.get(actor);
		else
			return 0L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.model.timestamp.LogicalTimestamp#getDistance(alpha.model.timestamp
	 * .LogicalTimestamp)
	 */
	/** {@inheritDoc} */
	@Override
	public long getDistance(final LogicalTimestamp ts) {

		if ((this.compare(ts) == Occurrence.PRECEDING))
			return (ts.getNumberOfModifications()
					- this.getNumberOfModifications() - 1);
		else
			return -1L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.model.timestamp.LogicalTimestamp#compare(alpha.model.timestamp.
	 * LogicalTimestamp)
	 */
	/** {@inheritDoc} */
	@Override
	public Occurrence compare(final LogicalTimestamp ts) {

		if (!(ts instanceof VersionMap))
			return Occurrence.UNDEFINED;

		final boolean equal = this.equals((VersionMap) ts);
		final boolean allCountersLesser = this
				.allCountersLesser((VersionMap) ts);
		final boolean allCountersLesserReverse = ((VersionMap) ts)
				.allCountersLesser(this);
		final boolean allCountersGreater = this
				.allCountersGreater((VersionMap) ts);
		final boolean concurrent = (!allCountersLesser)
				& (!allCountersLesserReverse);

		if (equal)
			return Occurrence.IDENTICAL;
		else if (concurrent)
			return Occurrence.CONCURRENT;
		else if (allCountersLesser)
			return Occurrence.PRECEDING;
		else if (allCountersGreater)
			return Occurrence.FOLLOWING;
		else
			return Occurrence.UNDEFINED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.timestamp.LogicalTimestamp#merge(alpha.model.timestamp.
	 * LogicalTimestamp)
	 */
	/** {@inheritDoc} */
	@Override
	public LogicalTimestamp merge(final LogicalTimestamp ts) {
		if (!(ts instanceof VersionMap))
			return null;

		final VersionMap result = new VersionMap();

		final Set<String> allKeys = new HashSet<String>();
		allKeys.addAll(this.versionCounters.keySet());
		allKeys.addAll(((VersionMap) ts).versionCounters.keySet());

		final Iterator<String> it = allKeys.iterator();
		while (it.hasNext()) {
			final String key = it.next();
			result.versionCounters.put(
					key,
					Math.max(this.getNumberOfModifications(key),
							ts.getNumberOfModifications(key)));
		}

		return result;
	}

	/**
	 * Checks if two {@link VersionMap} instances are equal.
	 * 
	 * @param ts
	 *            the {@link VersionMap} to compare with
	 * @return true, if both {@link VersionMap} instances are equal
	 */
	private boolean equals(final VersionMap ts) {
		if (this.versionCounters.equals(ts.versionCounters))
			return true;
		else
			return false;
	}

	/**
	 * Checks if all entries are lesser than those of the other.
	 * 
	 * @param ts
	 *            the {@link VersionMap} to compare with
	 * @return true, if all entries are lesser {@link VersionMap}
	 */
	private boolean allCountersLesser(final VersionMap ts) {

		boolean result = true;

		final Set<String> allKeys = new LinkedHashSet<String>();
		allKeys.addAll(this.versionCounters.keySet());
		allKeys.addAll(ts.versionCounters.keySet());

		for (final String key : allKeys) {
			if ((this.getNumberOfModifications(key) < ts
					.getNumberOfModifications(key))
					|| (this.getNumberOfModifications(key) == ts
							.getNumberOfModifications(key))) {
				continue;
			} else {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * Checks if all entries are greater than those of the other.
	 * 
	 * @param ts
	 *            the {@link VersionMap} to compare with
	 * @return true, if all entries are greater {@link VersionMap}
	 */
	private boolean allCountersGreater(final VersionMap ts) {

		boolean result = true;

		final Set<String> allKeys = new LinkedHashSet<String>();
		allKeys.addAll(this.versionCounters.keySet());
		allKeys.addAll(ts.versionCounters.keySet());

		for (final String key : allKeys) {
			if ((this.getNumberOfModifications(key) > ts
					.getNumberOfModifications(key))
					|| (this.getNumberOfModifications(key) == ts
							.getNumberOfModifications(key))) {
				continue;
			} else {
				result = false;
				break;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return this.versionCounters.toString();
	}
}
