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
