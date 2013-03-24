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
 * $Id: AdornmentNamesPresenter.java 3573 2012-02-14 11:01:02Z cpn $
 *************************************************************************/
package alpha.templates.gui;

import java.util.List;
import java.util.Map;

/**
 * Presents names of adornment types, under which the user can choose.
 */
public interface AdornmentNamesPresenter {

	/**
	 * Creates the adornment map: Adornment Name -> wasChosen.
	 * 
	 * @param adornmentList
	 *            the aalist
	 * @param output
	 *            the output
	 * @return the hash map
	 */
	public Map<String, Boolean> createAdornmentMap(List<String> adornmentList,
			String output);

}
