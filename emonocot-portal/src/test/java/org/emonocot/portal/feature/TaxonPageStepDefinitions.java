package org.emonocot.portal.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
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
public class TaxonPageStepDefinitions {

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
    private List<TaxonRow> taxonRows;

    /**
     *
     * @param newTaxonRows set the taxon rows
     */
    @Given("^there are taxa with the following properties:$")
    public final void thereIsATaxonWithIdAndName(
            final List<TaxonRow> newTaxonRows) {
        this.taxonRows =  newTaxonRows;
        for (TaxonRow taxonRow : newTaxonRows) {
            Taxon t = new Taxon();
            t.setName(taxonRow.name);
            t.setIdentifier(taxonRow.identifier);
            if (taxonRow.diagnostic != null) {
                createTextualData(t, taxonRow.diagnostic, Feature.diagnostic);
            }
            if (taxonRow.habitat != null) {
                createTextualData(t, taxonRow.habitat, Feature.habitat);
            }
            taxonService.save(t);
        }
    }

    /**
     *
     * @param taxon Set the taxon
     * @param text Set the text
     * @param feature Set the feature
     */
    private void createTextualData(final Taxon taxon, final String text,
            final Feature feature) {
        TextContent textContent = new TextContent();
        textContent.setContent(text);
        textContent.setFeature(feature);
        textContent.setTaxon(taxon);
        taxon.getContent().put(feature, textContent);
    }

    /**
     * @param pageNumber Set the page number
     */
    @When("^I navigate to page (\\d+)$")
    public final void iNavigateToThePage(final Integer pageNumber) {
        taxonPage = portal
                .getTaxonPage(taxonRows.get(pageNumber - 1).identifier);
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
     * @param paragraph
     *            Set the paragraph
     * @param heading
     *            Set the heading
     */
    @Then("^there should be a paragraph \"([^\"]*)\" with the heading \"([^\"]*)\"$")
    public final void thereShouldBeAParagraphWithTheHeading(
            final String paragraph, final String heading) {
        assertEquals(paragraph, taxonPage.getParagraph(heading));
    }

    /**
    *
    * @param heading
    *            Set the heading
    */
   @Then("^there should not be a paragraph with the heading \"([^\"]*)\"$")
    public final void thereShouldNotBeAParagraphWithTheHeading(
            final String heading) {
       assertFalse(taxonPage.doesParagraphExist(heading));
   }

    /**
     *
     */
    @After
    public final void tearDown() {
        if (taxonRows != null) {
            for (TaxonRow taxonRow : taxonRows) {
                taxonService.delete(taxonRow.identifier);
            }
        }
    }

    /**
    *
    * @author ben
    *
    */
   public static class TaxonRow {

       /**
        *
        */
       public String name;

       /**
        *
        */
       public String identifier;

       /**
        *
        */
       public String habitat;

       /**
        *
        */
       public String diagnostic;
   }
}
