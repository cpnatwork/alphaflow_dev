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
package alpha.editor;

import java.util.HashMap;

import alpha.facades.AlphaPropsFacade;
import alpha.model.AlphaCardDescriptor;
import alpha.model.Payload;
import alpha.model.apa.CorpusGenericus;

/**
 * The Class AlphaFormHelper.
 */
public class AlphaFormHelper {

	/** The ac. */
	private final AlphaCardDescriptor ac;

	/** The apf. */
	private final AlphaPropsFacade apf;

	/** The Constant emptyAlphaForm. */
	private static final String emptyAlphaForm = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<alphaForm width=\"600\" height=\"400\">\n<meta><title>%s</title></meta>\n<pbox></pbox>\n</alphaForm>";

	/** The Constant alphaFormFileType. */
	private static final String alphaFormFileType = "a-form.xml";

	/**
	 * Instantiates a new alpha form helper.
	 * 
	 * @param ac
	 *            the ac
	 * @param apf
	 *            the apf
	 */
	public AlphaFormHelper(final AlphaCardDescriptor ac,
			final AlphaPropsFacade apf) {
		this.ac = ac;
		this.apf = apf;
	}

	/**
	 * Creates the empty form.
	 */
	public void createEmptyForm() {
		final Payload p = new Payload();
		final String acTitle = this.ac.readAdornment(
				CorpusGenericus.TITLE.value()).getValue();
		final String alphaFormContent = String.format(
				AlphaFormHelper.emptyAlphaForm, acTitle);
		p.setContent(alphaFormContent.getBytes());
		final HashMap<String, String> adChangeRequest = new HashMap<String, String>();
		adChangeRequest.put(CorpusGenericus.SYNTACTICPAYLOADTYPE.value(),
				AlphaFormHelper.alphaFormFileType);
		this.apf.setPayload(this.ac.getId(), p, adChangeRequest);
	}

}
