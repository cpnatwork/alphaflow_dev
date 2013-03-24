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
 * $Id: APAFilterTest.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.templates.filters;

import org.junit.Test;

import alpha.adaptive.AlphaAdaptiveFacadeImpl;
import alpha.facades.AlphaAdaptiveFacade;
import alpha.model.apa.APAPayload;
import alpha.templates.exceptions.FilterException;
import alpha.templates.gui.wizard.TemplateWizard;
import alpha.templates.gui.wizard.WizardAdornmentNamesPresenter;

/**
 * Class tests APAFilter.
 */
public class APAFilterTest {

	/**
	 * Tests, if FilterExceptions are properly thrown.
	 * 
	 * @throws FilterException
	 *             the filter exception
	 */
	@Test(expected = FilterException.class)
	public void testExceptions() throws FilterException {

		final APAPayload apa = null;
		APAPayload apa2 = null;

		final APAFilter apaFilter = new APAFilter(
				new WizardAdornmentNamesPresenter(new TemplateWizard("Foo")));

		apaFilter.mergeApa(apa, apa2, "Foo");
		apa2 = new APAPayload();
		apaFilter.mergeApa(apa, apa2, "Foo");

		apaFilter.filterApa(apa, null, "Foo");
		apaFilter.filterApa(apa2, null, "Foo");

	}

	/**
	 * Tests the filter and merge methods of APAFilter.
	 * 
	 * @throws FilterException
	 *             the filter exception
	 */
	@Test
	public void filterMergeTest() throws FilterException {

		final APAPayload apa = new APAPayload();
		final AlphaAdaptiveFacade aaf = new AlphaAdaptiveFacadeImpl();
		apa.setContentCardAdornmentPrototype(aaf.getInitialContentCardAPA(
				"Foobar", "456"));

		@SuppressWarnings("unused")
		final APAFilter apaFilter = new APAFilter(
				new WizardAdornmentNamesPresenter(new TemplateWizard("Foo")));

		// apaFilter.filterApa(apa, null, "Foo");
		//
		// APAPayload apa2 = new APAPayload();
		// apa2.setContentCardAdornmentPrototype(aaf.getInitialContentCardAPA(
		// "1234", "11223"));
		// apaFilter.mergeApa(apa, apa2, "Foo");

	}
}
