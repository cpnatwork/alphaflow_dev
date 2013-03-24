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
package alpha.adaptive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import alpha.facades.AlphaAdaptiveFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.AdornmentEnumRange;
import alpha.model.adornment.ConsensusScope;
import alpha.model.adornment.PrototypedAdornment;
import alpha.model.apa.AlphaCardType;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.FundamentalSemanticType;
import alpha.model.apa.Priority;
import alpha.model.apa.Validity;
import alpha.model.apa.VersionControl;
import alpha.model.apa.Visibility;
import alpha.model.identification.AlphaCardID;
import alpha.util.XmlBinder;

/**
 * This class is an implementation of the {@link AlphaAdaptiveFacade} interface.
 * 
 * 
 */
@Service
public class AlphaAdaptiveFacadeImpl implements AlphaAdaptiveFacade {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = Logger
			.getLogger(AlphaAdaptiveFacadeImpl.class.getName());

	/**
	 * Instantiates a new {@link AlphaAdaptiveFacade} implementation.
	 */
	public AlphaAdaptiveFacadeImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaAdaptiveFacade#cloneAdornment(alpha.model.adornment
	 * .PrototypedAdornment)
	 */
	@Override
	public PrototypedAdornment cloneAdornment(final PrototypedAdornment seed) {
		return (PrototypedAdornment) this.getClone(seed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaAdaptiveFacade#cloneAlphaCardDescriptor(alpha.model
	 * .AlphaCardDescriptor)
	 */
	@Override
	public AlphaCardDescriptor cloneAlphaCardDescriptor(
			final AlphaCardDescriptor seed) {
		return (AlphaCardDescriptor) this.getClone(seed);
	}

	/**
	 * Gets a clone of the object.
	 * 
	 * @param seed
	 *            the to clone
	 * @return the clone of the object
	 */
	private Object getClone(final Object seed) {
		Object clone = null;
		final String schema = "alpha.model";
		final XmlBinder xmlb = new XmlBinder();

		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(bos);
			xmlb.store(seed, out, schema);

			final ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			clone = xmlb.load(in, schema);

			bos.close();
		} catch (final IOException e) {
			AlphaAdaptiveFacadeImpl.LOGGER.severe("Error: " + e);
		}
		AlphaAdaptiveFacadeImpl.LOGGER.finer("The clone is: " + clone);
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaAdaptiveFacade#getInitialCoordinationCardAPA(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public AlphaCardDescriptor getInitialCoordinationCardAPA(
			final String episodeID, final String ocId) {
		final AlphaCardDescriptor acd = new AlphaCardDescriptor();

		/* create the default generic adornments */
		// member of Contributor
		PrototypedAdornment pa = new PrototypedAdornment(
				CorpusGenericus.TITLE.value(), ConsensusScope.GENERIC_STD,
				AdornmentDataType.STRING);
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		pa = new PrototypedAdornment(CorpusGenericus.ACTOR.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		// member of Contributor
		pa = new PrototypedAdornment(CorpusGenericus.ROLE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		// member of Contributor
		pa = new PrototypedAdornment(CorpusGenericus.INSTITUTION.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		// ID of ObjectUnderConsideration
		pa = new PrototypedAdornment(CorpusGenericus.OCID.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		// pa = new PrototypedAdornment(CorpusGenericus.OCNAME.value(),
		// ConsensusScope.GENERIC_STD,
		// AdornmentDataType.STRING);
		// pa.setInstance(true);
		// acd.updateOrCreateAdornment(pa);

		pa = new PrototypedAdornment(CorpusGenericus.VISIBILITY.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM);
		pa.setEnumRange(new AdornmentEnumRange(Visibility.stringValues()));
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		pa = new PrototypedAdornment(CorpusGenericus.VALIDITY.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM);
		pa.setEnumRange(new AdornmentEnumRange(Validity.stringValues()));
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		pa = new PrototypedAdornment(CorpusGenericus.VERSION.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		pa = new PrototypedAdornment(CorpusGenericus.VERSIONCONTROL.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM);
		pa.setEnumRange(new AdornmentEnumRange(VersionControl.stringValues()));
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		pa = new PrototypedAdornment(CorpusGenericus.VARIANT.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		pa = new PrototypedAdornment(
				CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.STRING);
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		pa = new PrototypedAdornment(
				CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM);
		pa.setEnumRange(new AdornmentEnumRange(FundamentalSemanticType
				.stringValues()));
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		pa = new PrototypedAdornment(CorpusGenericus.ALPHACARDTYPE.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM);
		pa.setEnumRange(new AdornmentEnumRange(AlphaCardType.stringValues()));
		pa.setInstance(true);
		acd.updateOrCreateAdornment(pa);

		/* setting default-values */
		acd.readAdornment(CorpusGenericus.TITLE.value()).setValue(
				CorpusGenericus.TITLE.value());

		acd.readAdornment(CorpusGenericus.ACTOR.value()).setValue("$anyactor");
		acd.readAdornment(CorpusGenericus.ROLE.value()).setValue("$anyrole");
		acd.readAdornment(CorpusGenericus.INSTITUTION.value()).setValue(
				"$anyinstitution");

		acd.readAdornment(CorpusGenericus.OCID.value()).setValue(ocId);
		// acd.readAdornment(CorpusGenericus.OCNAME.value()).setValue(
		// oc.getName());

		acd.setId(new AlphaCardID(episodeID, "Coordination APA"));

		acd.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PUBLIC.value());

		acd.readAdornment(CorpusGenericus.VALIDITY.value()).setValue(
				Validity.VALID.value());

		acd.readAdornment(CorpusGenericus.VERSION.value()).setValue("1.0");

		acd.readAdornment(CorpusGenericus.VERSIONCONTROL.value()).setValue(
				VersionControl.VERSIONED.value());

		acd.readAdornment(CorpusGenericus.VARIANT.value()).setValue("0");

		acd.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.COORDINATION.value());

		return acd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaAdaptiveFacade#getInitialContentCardAPA(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public AlphaCardDescriptor getInitialContentCardAPA(final String episodeID,
			final String ocId) {

		/* extend coordination card APA */
		final AlphaCardDescriptor acd = this.getInitialCoordinationCardAPA(
				episodeID, ocId);

		/* set content prototype specific values */
		acd.setId(new AlphaCardID(episodeID, "Content APA"));
		acd.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PRIVATE.value());
		acd.readAdornment(CorpusGenericus.VALIDITY.value()).setValue(
				Validity.INVALID.value());
		acd.readAdornment(CorpusGenericus.VERSION.value()).setValue("0");

		acd.readAdornment(CorpusGenericus.VERSIONCONTROL.value()).setValue(
				VersionControl.UNVERSIONED.value());

		acd.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.CONTENT.value());

		/* add content prototype specific generic adornments */
		// member of Contributor
		PrototypedAdornment aa = new PrototypedAdornment(
				CorpusGenericus.DUEDATE.value(), ConsensusScope.GENERIC_STD,
				AdornmentDataType.TIMESTAMP);
		aa.setInstance(true);
		acd.updateOrCreateAdornment(aa);

		aa = new PrototypedAdornment(CorpusGenericus.DEFERRED.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM);
		final String[] values = new String[] { "true", "false" };
		aa.setEnumRange(new AdornmentEnumRange(values));
		aa.setValue(Boolean.FALSE.toString());
		aa.setInstance(true);
		acd.updateOrCreateAdornment(aa);

		aa = new PrototypedAdornment(CorpusGenericus.DELETED.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM);
		aa.setEnumRange(new AdornmentEnumRange(values));
		aa.setValue(Boolean.FALSE.toString());
		aa.setInstance(true);
		acd.updateOrCreateAdornment(aa);

		aa = new PrototypedAdornment(CorpusGenericus.PRIORITY.value(),
				ConsensusScope.GENERIC_STD, AdornmentDataType.ENUM);
		aa.setEnumRange(new AdornmentEnumRange(Priority.stringValues()));
		aa.setValue(Priority.NORMAL.value());
		aa.setInstance(true);
		acd.updateOrCreateAdornment(aa);

		return acd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.facades.AlphaAdaptiveFacade#updateAdornment(alpha.model.adornment
	 * .PrototypedAdornment, alpha.model.adornment.PrototypedAdornment,
	 * alpha.model.adornment.PrototypedAdornment)
	 */
	@Override
	public PrototypedAdornment updateAdornment(
			final PrototypedAdornment cardAdornment,
			final PrototypedAdornment apaAdornment,
			final PrototypedAdornment oldApaAdornment) {

		/* deep copy required, so that all AlphaCards use their own instance! */
		final PrototypedAdornment newAdornment = this
				.cloneAdornment(apaAdornment);

		/* set the clone as new card adornment, but consider the following: */
		/*
		 * 1) if the clone instance flag is false, then the card's flag has to
		 * be favoured only if the instance flag has changed, the card's flag is
		 * overwritten
		 */
		if (newAdornment.isInstance() == oldApaAdornment.isInstance()) {
			newAdornment.setInstance(cardAdornment.isInstance());
		}
		/*
		 * 2) if the card's adornment value has changed (--> it isn't anymore
		 * equal to the old default value), then it MUST NOT be overwritten
		 */
		if (!this.checkValueEquality(cardAdornment.getValue(),
				oldApaAdornment.getValue())) {
			newAdornment.setValue(cardAdornment.getValue());
		}
		// TODO don't forget to validate the value

		return newAdornment;
	}

	/**
	 * Check value equality.
	 * 
	 * @param one
	 *            the one
	 * @param another
	 *            the another
	 * @return true, if successful
	 */
	private boolean checkValueEquality(final String one, final String another) {
		return one == null ? another == null : one.equals(another);
	}

}
