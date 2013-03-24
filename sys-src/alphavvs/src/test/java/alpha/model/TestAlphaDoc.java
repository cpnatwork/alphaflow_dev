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

import alpha.model.apa.AlphaCardType;
import alpha.model.apa.CorpusGenericus;
import alpha.model.apa.FundamentalSemanticType;
import alpha.model.apa.Validity;
import alpha.model.apa.Visibility;
import alpha.model.cra.ContributorID;
import alpha.model.identification.AlphaCardID;

/**
 * The Class TestAlphaDoc.
 */
public class TestAlphaDoc extends AlphaDoc {

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.props.interfaces.AlphaPropsFacade#getAlphaDoc()
	 */
	/**
	 * Gets the test alpha doc a.
	 * 
	 * @return the test alpha doc a
	 */
	public static AlphaDoc getTestAlphaDocA() {
		final AlphaDoc doc = new AlphaDoc();
		doc.setEpisodeID("ep43");
		final AlphaCardDescriptor cac = new AlphaCardDescriptor();// AlphaCardType.REFERRAL_VOUCHER,
		// "test");
		cac.setId(new AlphaCardID("ep23", "00TEST00"));
		// cac.setFundamentalSemanticType(FundamentalSemanticType.CONTENT);
		cac.readAdornment(CorpusGenericus.FUNDAMENTALSEMANTICTYPE.value())
				.setValue(FundamentalSemanticType.CONTENT.value());
		// cac.setValidity(Validity.INVALID);
		cac.readAdornment(CorpusGenericus.VALIDITY.value()).setValue(
				Validity.INVALID.value());
		// cac.setVisibility(Visibility.PRIVATE);
		cac.readAdornment(CorpusGenericus.VISIBILITY.value()).setValue(
				Visibility.PRIVATE.value());
		// cac.setAlphaCardType(AlphaCardType.REFERRAL_VOUCHER);
		cac.readAdornment(CorpusGenericus.ALPHACARDTYPE.value()).setValue(
				AlphaCardType.REFERRAL_VOUCHER.value());
		// cac.setSPType("pdf");
		cac.readAdornment(CorpusGenericus.SYNTACTICPAYLOADTYPE.value())
				.setValue("pdf");
		// ObjectUnderConsideration oid = new ObjectUnderConsideration();
		// oid.setId("1337");
		// oid.setName("Cuddy");
		final ContributorID sid = new ContributorID();
		sid.setActor("=wilson");
		sid.setInstitution("@prinston");
		sid.setRole("+gynecologist");
		// cac.setContributor(sid);
		cac.readAdornment(CorpusGenericus.ACTOR.value()).setValue(
				sid.getActor());
		cac.readAdornment(CorpusGenericus.ROLE.value()).setValue(sid.getRole());
		cac.readAdornment(CorpusGenericus.INSTITUTION.value()).setValue(
				sid.getInstitution());
		// cac.setAlphaCardName("Report");
		cac.readAdornment(CorpusGenericus.TITLE.value()).setValue("Report");
		// cac.setObject(oid);
		cac.readAdornment(CorpusGenericus.OCID.value()).setValue("1337");

		// cac.setVariant("-");
		cac.readAdornment(CorpusGenericus.VARIANT.value()).setValue("-");
		// cac.setVersion("1.0");
		cac.readAdornment(CorpusGenericus.VERSION.value()).setValue("1.0");

		return doc;
	}

}
