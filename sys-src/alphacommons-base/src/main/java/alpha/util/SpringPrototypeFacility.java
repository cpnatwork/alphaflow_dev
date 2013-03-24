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
 * $Id: SpringPrototypeFacility.java 3579 2012-02-15 10:59:19Z cpn $
 *************************************************************************/
package alpha.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Create scope=prototype beans with <br/>
 * 'spf.autowire(new FooProtoBean())' <br/>
 * 'spf.autowire(new GooProtoBean( constructorArgs ))'
 * 
 * @see <a
 *      href="http//static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/beans.html#beans-factory-scopes-singleton">Original
 *      Idea</a>
 * @see <a href="http://i-proving.ca/space/Ken+Stevens/blog/2010-06-23_1">Spring
 *      Docu</a>
 */
@Service
public class SpringPrototypeFacility {

	/** The context. */
	@Autowired
	private ApplicationContext context;

	/**
	 * Autowire.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param existingBean
	 *            the existing bean
	 * @return the t
	 */
	public <T> T autowire(final T existingBean) {
		this.context.getAutowireCapableBeanFactory().autowireBean(existingBean);
		return existingBean;
	}
}