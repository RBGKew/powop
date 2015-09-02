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
package org.emonocot.portal.driver.organisation;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.portal.driver.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class ResourceOutput extends PageObject {

	/**
	 *
	 */
	@FindBy(how = How.ID, using = "results")
	private WebElement results;

	/**
	 *
	 */
	@FindBy(how = How.ID, using = "facets")
	private WebElement facets;

	/**
	 *
	 * @return the number of results
	 */
	public final int getResultNumber() {
		List<WebElement> rows = results.findElements(By.tagName("tr"));
		return rows.size();
	}

	/**
	 *
	 * @return the results as an array of strings
	 */
	public final List<String[]> getResults() {
		List<String[]> r = new ArrayList<String[]>();
		List<WebElement> rows = results.findElements(By.xpath("tr"));
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			String[] result = new String[4];
			result[0] = cells.get(0).getText();
			result[1] = cells.get(1).getText();
			result[2] = cells.get(2).getText();
			result[3] = cells.get(3).getText();
			r.add(result);
		}
		return r;
	}

	/**
	 *
	 * @param facetName TODO
	 * @param category Set the category
	 * @return the source job page
	 */
	public final ResourceOutput selectFacet(String facetName, final String facetValue) {
		WebElement facet = facets.findElement(By.xpath("div/div/div/ul/li[@class = '" + facetName + "']/a[span/text() = \'" + facetValue + "\']"));
		return openAs(facet.getAttribute("href"),ResourceOutput.class);
	}

}
