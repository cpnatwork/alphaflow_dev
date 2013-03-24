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
 * $Id: AdpEnum.java 3583 2012-02-16 01:52:45Z cpn $
 *************************************************************************/
package alpha.model.adornment;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * The Interface AdpEnum.
 * 
 * @param <E>
 *            the element type
 * @author cpn
 * @version $Id: AdpEnum.java 3583 2012-02-16 01:52:45Z cpn $
 */
public interface AdpEnum<E> extends Comparator<E> {

	/**
	 * Exists.
	 * 
	 * @param enumValue
	 *            the enum value
	 * @return true, if successful
	 */
	public boolean exists(E enumValue);

	/**
	 * Value of.
	 * 
	 * @param name
	 *            the name
	 * @return the e
	 */
	public E valueOf(String name);

	/**
	 * Values.
	 * 
	 * @return the list
	 */
	public List<E> values();

	/**
	 * Ordinal.
	 * 
	 * @param enumValue
	 *            the enum value
	 * @return the int
	 */
	public int ordinal(E enumValue);

	/**
	 * Range.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the sets the
	 */
	public Set<E> range(E from, E to);

	/**
	 * Extend.
	 * 
	 * @param item
	 *            the item
	 * @param position
	 *            the position
	 */
	public void extend(String item, int position);

	/**
	 * Extend.
	 * 
	 * @param item
	 *            the item
	 */
	public void extend(String item);

	/**
	 * Narrow.
	 * 
	 * @param item
	 *            the item
	 */
	public void narrow(String item);

	/**
	 * Order.
	 * 
	 * @param items
	 *            the items
	 */
	public void order(String[] items);
}
