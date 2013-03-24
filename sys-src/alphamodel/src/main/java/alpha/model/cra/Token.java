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
 * $Id: Token.java 4000 2012-09-13 13:41:02Z uj32uvac $
 *************************************************************************/
package alpha.model.cra;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.AdpAdornment;
import alpha.model.adornment.ConsensusScope;
import alpha.model.timestamp.Occurrence;
import alpha.model.versionmap.VersionMap;

// TODO: Auto-generated Javadoc
/**
 * The Class Token.
 * 
 * @author cpn
 * @version $Id: Token.java 4000 2012-09-13 13:41:02Z uj32uvac $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "impactScope", "versionMap" })
@XmlRootElement(name = "Token")
public class Token extends AdpAdornment {

	/**
	 * The impactScope. Determines whether the value of a token is self choosen
	 * (like an attribute) or if it is passed on from one participant to the
	 * next (value state is assigned from outside): INTRINSIC vs. EXTRINSIC
	 * 
	 * @composition
	 * */
	@XmlElement(required = true)
	protected ImpactScope impactScope;

	/** The version map. */
	@XmlElement(required = true)
	protected VersionMap versionMap = new VersionMap();

	/**
	 * Gets the version map.
	 * 
	 * @return the version map
	 */
	public VersionMap getVersionMap() {
		return versionMap;
	}

	/**
	 * Sets the version map.
	 * 
	 * @param versionMap
	 *            the new version map
	 */
	public void setVersionMap(VersionMap versionMap) {
		this.versionMap = versionMap;
	}

	/* default constructor needed for xml binding */
	/**
	 * Instantiates a new prototyped adornment.
	 */
	public Token() {
	}

	/**
	 * Instantiates a new prototyped adornment.
	 * 
	 * @param name
	 *            the name
	 * @param consensusScope
	 *            the adaptive adornment consensusScope
	 * @param dataType
	 *            the data type
	 * @param impactScope
	 *            the impact scope
	 */
	public Token(final String name, final ConsensusScope consensusScope,
			final AdornmentDataType dataType, ImpactScope impactScope) {
		super(name, consensusScope, dataType);
		this.impactScope = impactScope;
	}

	/**
	 * Gets the impact scope.
	 * 
	 * @return the impact scope
	 */
	public ImpactScope getImpactScope() {
		return impactScope;
	}

	/**
	 * Returns the negated value of the Token. Only possible if EnumRange.size()
	 * == 2
	 * 
	 * @return the negated value
	 */
	public String getNegatedValue() {

		List<String> range = enumRange.values();

		if (range.size() != 2) {
			Logger.getLogger(Token.class.getName()).finer(
					"Token negation failed! (range size != 2)");
			return null;
		}
		return range.get(Math.abs(range.indexOf(value) - 1));

	}

	/**
	 * Sets the impact scope.
	 * 
	 * @param impactScope
	 *            the new impact scope
	 */
	public void setImpactScope(ImpactScope impactScope) {
		this.impactScope = impactScope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Token [name=" + name + ", impactScope=" + impactScope
				+ ", dataType=" + dataType + ", value=" + value + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.model.adornment.AdpAdornment#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (impactScope != other.impactScope)
			return false;
		if (versionMap == null) {
			if (other.versionMap != null)
				return false;
		} else if (!(versionMap.compare(other.versionMap) == Occurrence.IDENTICAL))
			return false;
		return true;
	}

}
