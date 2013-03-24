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
 * $Id: CRAFilterTest.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.templates.filters;

import org.junit.Test;

import alpha.model.adornment.AdornmentDataType;
import alpha.model.adornment.AdpAdornment;
import alpha.model.adornment.ConsensusScope;
import alpha.model.apa.CorpusGenericusOC;
import alpha.model.cra.CRAPayload;
import alpha.model.cra.ContributorID;
import alpha.model.cra.EndpointID;
import alpha.model.cra.ObjectUnderConsideration;
import alpha.model.cra.Participant;
import alpha.templates.exceptions.FilterException;
import alpha.templates.gui.wizard.TemplateWizard;
import alpha.templates.gui.wizard.WizardContributorNamesPresenter;

/**
 * Class tests CRAFilters.
 */
public class CRAFilterTest {

	/**
	 * Tests for proper FilterExceptionss.
	 * 
	 * @throws FilterException
	 *             the filter exception
	 */
	@Test(expected = FilterException.class)
	public void testExceptions() throws FilterException {

		final CRAPayload cra = null;
		CRAPayload cra2 = null;

		final CRAFilter craFilter = new CRAFilter(
				new WizardContributorNamesPresenter(new TemplateWizard("Foo"),
						null), cra);

		craFilter.mergeCra(cra2);
		cra2 = new CRAPayload();
		craFilter.mergeCra(cra2);

		craFilter.filterCra();

		final CRAFilter craFilter2 = new CRAFilter(
				new WizardContributorNamesPresenter(new TemplateWizard("Foo"),
						null), cra2);
		craFilter2.filterCra();
	}

	/**
	 * Tests the filter and merge methods of CRAFilters.
	 * 
	 * @throws FilterException
	 *             the filter exception
	 */
	@Test
	public void filterMergeTest() throws FilterException {

		final CRAPayload cra = new CRAPayload();
		final CRAPayload cra2 = new CRAPayload();

		final ObjectUnderConsideration oc = new ObjectUnderConsideration("ID");
		final AdpAdornment aa = new AdpAdornment(
				CorpusGenericusOC.OCNAME.value(), ConsensusScope.GENERIC_STD,
				AdornmentDataType.STRING);
		aa.setValue("foo");
		oc.updateOrCreateAdornment(aa);

		cra.setOc(oc);

		final Participant p = new Participant();
		final ContributorID sub = new ContributorID("inst", "role", "schorsch");
		final EndpointID node = new EndpointID("foo", "var", "123", "adr");
		node.setEmailAddress("adr");
		p.setContributor(sub);
		p.setNode(node);
		cra.getListOfParticipants().add(p);

		final CRAFilter craFilter = new CRAFilter(
				new WizardContributorNamesPresenter(new TemplateWizard("foo"),
						oc), cra);

		craFilter.filterCra();

		final ObjectUnderConsideration oc2 = new ObjectUnderConsideration("ID1");
		final AdpAdornment aa2 = new AdpAdornment(
				CorpusGenericusOC.OCNAME.value(), ConsensusScope.GENERIC_STD,
				AdornmentDataType.STRING);
		aa2.setValue("foobar");
		oc2.updateOrCreateAdornment(aa2);

		cra2.setOc(oc2);

		final Participant p2 = new Participant();
		final ContributorID sub2 = new ContributorID("inst2", "role2",
				"schorsch2");
		final EndpointID node2 = new EndpointID("foo2", "var2", "1232", "adr2");
		node2.setEmailAddress("adr2");
		p2.setContributor(sub2);
		p2.setNode(node2);
		cra2.getListOfParticipants().add(p2);

		craFilter.mergeCra(cra2);

	}

}
