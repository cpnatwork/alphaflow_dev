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
 * $Id: AdornmentsFilterTest.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.templates.filters;

import java.util.ArrayList;

import org.junit.Test;

import alpha.adaptive.AlphaAdaptiveFacadeImpl;
import alpha.facades.AlphaAdaptiveFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.apa.APAPayload;
import alpha.templates.exceptions.FilterException;
import alpha.templates.gui.AdornmentNamesPresenter;
import alpha.templates.gui.AlphaCardNamesPresenter;
import alpha.templates.gui.wizard.TemplateWizard;
import alpha.templates.gui.wizard.WizardAdornmentNamesPresenter;
import alpha.templates.gui.wizard.WizardAlphaCardNamesPresenter;

/**
 * Class tests behavior of AdornmentsFilter.
 */
public class AdornmentsFilterTest {

	/**
	 * Tests, if correct filter exceptions are thrown.
	 * 
	 * @throws FilterException
	 *             the filter exception
	 */
	@Test(expected = FilterException.class)
	public void testExceptions() throws FilterException {

		final APAPayload apapl = null;
		final TemplateWizard tw = new TemplateWizard("foo");
		final AdornmentNamesPresenter anp = new WizardAdornmentNamesPresenter(
				tw);

		final AlphaCardNamesPresenter acnp = new WizardAlphaCardNamesPresenter(
				tw);

		final AdornmentsFilter af = new AdornmentsFilter(apapl, anp, acnp,
				false);

		af.filterAdornments(null);

	}

	/**
	 * test the filter-method.
	 * 
	 * @throws FilterException
	 *             the filter exception
	 */
	@Test
	public void filterTest() throws FilterException {

		final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();

		final APAPayload apapl = new APAPayload();
		apapl.setContentCardAdornmentPrototype(aaf.getInitialContentCardAPA(
				"ffo", "bar"));
		final TemplateWizard tw = new TemplateWizard("foo");
		final AdornmentNamesPresenter anp = new WizardAdornmentNamesPresenter(
				tw);

		final AlphaCardNamesPresenter acnp = new WizardAlphaCardNamesPresenter(
				tw);

		final AdornmentsFilter af = new AdornmentsFilter(apapl, anp, acnp,
				false);

		final ArrayList<AlphaCardDescriptor> acdlist = new ArrayList<AlphaCardDescriptor>();

		AlphaCardDescriptor acd = new AlphaCardDescriptor();
		acd = aaf.getInitialContentCardAPA("foo", "bar");
		acdlist.add(acd);

		AlphaCardDescriptor acd2 = new AlphaCardDescriptor();
		acd2 = aaf.getInitialContentCardAPA("foo2", "bar2");
		acdlist.add(acd2);

		af.filterAdornments(acdlist);

	}

}
