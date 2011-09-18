package org.emonocot.portal;

import cuke4duke.annotation.After;
import cuke4duke.annotation.Before;
import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;

/**
 *
 * @author ben
 *
 */
public class TaxonPageFeature extends AbstractSeleniumDriver {
    
    private String taxonId;
        
    @Given("^There is a taxon with id (\\d+) and name '([\\d\\s]+)'$")
    public void thereIsATaxonWithIdAndName(final String id, String name) {
        this.taxonId = "urn:kew.org:wcs:taxon:" + id;
        super.addTaxon(taxonId, name);
    }
    
    @When("^I navigate to the page$")
    public void iNavigateToThePage() {
        super.getPage("taxon/urn:kew.org:wcs:taxon:" + taxonId);
        
    }
    
    @Then("^The page title should be ([\\d\\s]+)$")
    public void thePageTitleShouldBeAcorus(final String title) {
        assertEquals(title,super.getPageTitle());
        
    }
    
    @Then("^The title class should be (\\d+)$")
    public void theTitleClassShouldBeTaxonName(String clazz) {
        assertEquals(clazz,super.getPageTitleClass());
    }
}
