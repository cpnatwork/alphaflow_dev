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
 * $Id: VersionMapAdapter.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.versionmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for mapping version vectors to XML elements using JAXB.
 *
 * @author cpn
 * @version $Id: VersionMapAdapter.java 3583 2012-02-16 01:52:45Z cpn $
 */
public final class VersionMapAdapter extends
		XmlAdapter<VersionMapType, Map<String, Long>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public VersionMapType marshal(final Map<String, Long> arg0)
			throws Exception {
		final VersionMapType VersionVectorType = new VersionMapType();
		for (final Entry<String, Long> entry : arg0.entrySet()) {
			final VersionMapEntryType VersionVectorEntryType = new VersionMapEntryType();
			VersionVectorEntryType.key = entry.getKey();
			VersionVectorEntryType.value = entry.getValue();
			VersionVectorType.entry.add(VersionVectorEntryType);
		}
		return VersionVectorType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	/** {@inheritDoc} */
	@Override
	public Map<String, Long> unmarshal(final VersionMapType arg0)
			throws Exception {
		final HashMap<String, Long> hashMap = new HashMap<String, Long>();
		for (final VersionMapEntryType myEntryType : arg0.entry) {
			hashMap.put(myEntryType.key, myEntryType.value);
		}
		return hashMap;
	}

}
