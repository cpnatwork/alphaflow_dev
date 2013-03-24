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
 * $Id: PSAFilterTest.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.templates.filters;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.junit.Test;

import alpha.adaptive.AlphaAdaptiveFacadeImpl;
import alpha.facades.AlphaAdaptiveFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.identification.AlphaCardID;
import alpha.model.psa.AlphaCardRelationship;
import alpha.model.psa.PSAPayload;
import alpha.templates.exceptions.FilterException;
import alpha.templates.gui.wizard.TemplateWizard;
import alpha.templates.gui.wizard.WizardAlphaCardNamesPresenter;

/**
 * Class tests PSAFilter.
 */
public class PSAFilterTest {

	/**
	 * Tests, if correct exceptions are thrown.
	 * 
	 * @throws FilterException
	 *             the filter exception
	 */
	@Test(expected = FilterException.class)
	public void testExceptions() throws FilterException {

		final PSAPayload psa = null;
		PSAPayload psa2 = null;
		final PSAFilter psaFilter = new PSAFilter(
				new WizardAlphaCardNamesPresenter(new TemplateWizard("Foo")));

		psaFilter.filterPsa(psa, null, "Foo");
		psaFilter.mergePsa(null, psa, psa2, "Dummy");

		psa2 = new PSAPayload();
		psa2.setListOfTodoItems(new LinkedHashSet<AlphaCardID>());
		psa2.setListOfTodoRelationships(null);

		psaFilter.filterPsa(psa2, null, "Foo");
		psaFilter.mergePsa(null, psa, psa2, "Bla");

	}

	/**
	 * Tests filter and merge methods of PSAFilter.
	 * 
	 * @throws FilterException
	 *             the filter exception
	 */
	@Test
	public void filterMergeTest() throws FilterException {

		final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();

		AlphaCardDescriptor acd = new AlphaCardDescriptor();
		acd = aaf.getInitialContentCardAPA("foo", "bar");
		final ArrayList<AlphaCardDescriptor> acdlist = new ArrayList<AlphaCardDescriptor>();
		acdlist.add(acd);

		final PSAPayload psapl = new PSAPayload();
		psapl.setListOfTodoItems(new LinkedHashSet<AlphaCardID>());
		psapl.setListOfTodoRelationships(new LinkedHashSet<AlphaCardRelationship>());
		psapl.getListOfTodoItems().add(acd.getId());

		final PSAFilter psaFilter = new PSAFilter(
				new WizardAlphaCardNamesPresenter(new TemplateWizard("Foo")));
		psaFilter.filterPsa(psapl, acdlist, "foo");

		final PSAPayload psapl2 = new PSAPayload();
		psapl2.setListOfTodoItems(new LinkedHashSet<AlphaCardID>());
		psapl2.setListOfTodoRelationships(new LinkedHashSet<AlphaCardRelationship>());
		psapl2.getListOfTodoItems().add(acd.getId());

		psaFilter.mergePsa(acdlist, psapl, psapl2, "foo");

	}

}
