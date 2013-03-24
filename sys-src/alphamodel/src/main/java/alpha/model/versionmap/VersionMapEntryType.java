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
 * $Id: VersionMapEntryType.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.versionmap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * Representation of one entry of a {@link VersionMap}. Necessary for correct
 * serialization to XML using JAXB.
 *
 * @author cpn
 * @version $Id: VersionMapEntryType.java 3583 2012-02-16 01:52:45Z cpn $
 */
public class VersionMapEntryType {

	/** The key of the entry. */
	@XmlAttribute
	public String key;

	/** The value associated to the key. */
	@XmlValue
	public Long value;

}
