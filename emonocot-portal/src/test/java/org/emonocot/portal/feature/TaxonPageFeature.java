package org.emonocot.portal.feature;

import static org.junit.Assert.assertEquals;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.portal.driver.Portal;
import org.emonocot.portal.driver.TaxonPage;
import org.emonocot.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.After;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;



/**
 *
 * @author ben
 *
 */
public class TaxonPageFeature {

    /**
     *
     */
    @Autowired
    private Portal portal;

    /**
     *
     */
    @Autowired
    private TaxonService taxonService;

    /**
     *
     */
    private TaxonPage taxonPage;

    /**
     *
     */
    private String taxonId;

    /**
     *
     * @param id Set the taxon identifier
     * @param name Set the taxon name
     */
    @Given("^there is a taxon with id \"([\\w\\:\\.\\d]+)\" and name \"([\\w\\s]+)\"$")
    public final void thereIsATaxonWithIdAndName(final String id,
            final String name) {
        this.taxonId =  id;
        Taxon  t = new Taxon();
        t.setName(name);
        t.setIdentifier(id);
        taxonService.save(t);
    }

    /**
     *
     */
    @When("^I navigate to the page$")
    public final void iNavigateToThePage() {
        taxonPage = portal.getTaxonPage(taxonId);
    }

    /**
     *
     * @param title Set the title
     */
    @Then("^the page title should be \"([^\"]*)\"$")
    public final void thePageTitleShouldBeAcorus(final String title) {
        assertEquals(title, taxonPage.getTaxonName());
    }

    /**
     *
     * @param clazz Set the page class
     */
    @Then("^the title class should be \"([^\"]*)\"$")
    public final void theTitleClassShouldBeTaxonName(final String clazz) {
        assertEquals(clazz, taxonPage.getTaxonNameClass());
    }

    /**
     *
     */
    @After
    public final void tearDown() {
        if (taxonId != null) {
            taxonService.delete(taxonId);
        }
    }
}
