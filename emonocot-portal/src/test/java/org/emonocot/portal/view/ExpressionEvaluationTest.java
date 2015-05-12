/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;

import org.emonocot.model.Taxon;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;

public class ExpressionEvaluationTest {
	
	private PageContext pageContext;
	
	/**
	 *
	 */
	@Before
	public void setUp() {
		Taxon taxon = new Taxon();
		taxon.setScientificName("Aus aus");
		taxon.setScientificNameAuthorship("Clark.");
		pageContext = new MockPageContext();
		pageContext.setAttribute("taxon", taxon, PageContext.PAGE_SCOPE);
	}
	
	@Test
	public void testEvaluatePageTitleExpression() throws ELException {
		assertEquals("Evaluate should return 'Aus aus Clark.'", Functions.evaluate("${taxon.scientificName} ${taxon.scientificNameAuthorship}", pageContext), "Aus aus Clark.");
	}

}
