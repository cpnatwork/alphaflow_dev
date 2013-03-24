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
package alpha.model;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import alpha.model.deliveryacknowledgment.AcknowledgmentStructure;
import alpha.model.identification.AlphaCardID;
import alpha.model.identification.CoordCardType;
import alpha.model.timestamp.Occurrence;
import alpha.model.versionmap.VersionMap;

// TODO: Auto-generated Javadoc
/**
 * This class defines an alphaEpisode (a.k.a. alphaDocument). An alphaDoc is
 * abstract. It is initialized when a both coordination cards PSA and a CRA are
 * created. It consists always of these two and none, one or more other
 * AlphaCards.
 * 
 * @author cpn
 * @version $Id: $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "episodeID", "title", "asList" })
@XmlRootElement(name = "alphaDoc")
public class AlphaDoc {

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger
			.getLogger(AlphaDoc.class.getName());

	/** The episode id. */
	@XmlElement(required = true)
	protected String episodeID;

	/** The title. */
	@XmlElement(required = true)
	protected String title;

	/**
	 * The list of acknowledged messages.
	 */
	@XmlElementWrapper(name = "asList")
	@XmlElement(name = "AS", required = true)
	protected List<AcknowledgmentStructure> asList;

	/**
	 * Instantiates a new alpha doc.
	 * 
	 * @param episodeID
	 *            the episode id
	 */
	public AlphaDoc(final String episodeID) {
		super();
		this.episodeID = episodeID;
	}

	/**
	 * Instantiates a new alpha doc.
	 */
	public AlphaDoc() {
	}

	/**
	 * Gets an {@link AlphaCardID} of a coordination card.
	 * 
	 * @param coordCardType
	 *            the coord card type
	 * @return the {@link AlphaCardID} of a coordination card.
	 */
	public AlphaCardID getCoordCardId(final CoordCardType coordCardType) {
		return new AlphaCardID(this.episodeID, coordCardType);
	}

	/**
	 * Gets the value of the episodeID property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getEpisodeID() {
		return this.episodeID;
	}

	/**
	 * Sets the value of the episodeID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setEpisodeID(final String value) {
		this.episodeID = value;
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Gets the as list.
	 * 
	 * @return the as list
	 */
	public List<AcknowledgmentStructure> getAsList() {
		if (this.asList == null) {
			this.asList = new LinkedList<AcknowledgmentStructure>();
		}
		return this.asList;
	}

	/**
	 * Sets the as set.
	 * 
	 * @param as
	 *            the new as set
	 */
	public void setAsSet(List<AcknowledgmentStructure> as) {
		this.asList = as;
	}

	/**
	 * Gets the as for a specific alphacardid and version.
	 * 
	 * @param acid
	 *            the acid
	 * @param versionMap
	 *            the version map
	 * @return the as
	 */
	public AcknowledgmentStructure getAs(AlphaCardID acid, VersionMap versionMap) {
		AcknowledgmentStructure ackStructure = new AcknowledgmentStructure();
		boolean newAs = true;
		for (AcknowledgmentStructure as : this.getAsList()) {
			if (as.getAcid().equals(acid)
					&& as.getVersionMap().compare(versionMap) == Occurrence.IDENTICAL) {
				// && as.getVersionMap().equals(versionMap)) {
				ackStructure = as;
				newAs = false;
			}
		}
		if (newAs) {
			ackStructure.setAcid(acid);
			ackStructure.setVersionMap(versionMap);
			this.addAs(ackStructure);
		}
		return ackStructure;
	}

	/**
	 * Gets a list of as for a specified alphacardid. Used for finding the head
	 * as.
	 * 
	 * @param acid
	 *            the acid
	 * @return the as
	 */
	private List<AcknowledgmentStructure> getAs(AlphaCardID acid) {
		List<AcknowledgmentStructure> listOfAs = new LinkedList<AcknowledgmentStructure>();
		boolean newAs = true;

		for (AcknowledgmentStructure as : this.getAsList()) {
			if (as.getAcid().equals(acid)) {
				listOfAs.add(as);
				newAs = false;
			}
		}
		if (newAs) {
			AcknowledgmentStructure ackStructure = new AcknowledgmentStructure();
			ackStructure.setAcid(acid);
			ackStructure.setVersionMap(new VersionMap());
			this.addAs(ackStructure);
			listOfAs.add(ackStructure);
		}
		return listOfAs;
	}

	/**
	 * Gets the head as (the as with the newest version)
	 * 
	 * @param acid
	 *            the acid
	 * @return the head as
	 */
	public AcknowledgmentStructure getHeadAs(AlphaCardID acid) {
		List<AcknowledgmentStructure> listOfAs = this.getAs(acid);

		// take first AS as start element
		AcknowledgmentStructure headAs = listOfAs.get(0);

		for (int i = 1; i < listOfAs.size(); i++) {
			AcknowledgmentStructure as_i = listOfAs.get(i);
			// if headAs is all smaller than as_i then use as_i as head
			if (headAs.getVersionMap().compare(as_i.getVersionMap()) == Occurrence.PRECEDING) {
				headAs = as_i;
			}
		}
		return headAs;
	}

	/**
	 * Adds the as to the asList.
	 * 
	 * @param as
	 *            the as
	 */
	public void addAs(AcknowledgmentStructure as) {
		this.asList.add(as);
	}

}
