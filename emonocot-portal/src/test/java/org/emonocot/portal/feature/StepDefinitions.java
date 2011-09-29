package org.emonocot.portal.feature;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.Serializable;
import java.util.List;

import org.emonocot.portal.driver.Portal;
import org.emonocot.portal.driver.SearchResultsPage;
import org.emonocot.portal.driver.TaxonPage;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

/**
 *
 * @author ben
 *
 */
public class StepDefinitions {

    /**
     *
     */
    private SearchResultsPage searchResultsPage;

    /**
    *
    */
    @Autowired
    private Portal portal;

    /**
     *
     */
    private TaxonPage taxonPage;

    /**
     *
     * @param query
     *            Set the search query
     */
    @When("^I search for \"([^\"]+)\"$")
    public final void whenISearchFor(final String query) {
        searchResultsPage = portal.search(query);
    }

    /**
     * @param facetName the facet to restric
     * @param facetValue
     *            Set the facet value to select
     */
    @When("^I restrict the \"([^\"]+)\" by selecting \"([^\"]+)\"$")
    public final void iSelect(final String facetName, final String facetValue) {
        searchResultsPage = searchResultsPage.selectFacet(facetName, facetValue);
    }

    /**
     *
     * @param results
     *            Set the number of results
     */
    @Then("^there should be (\\d) result[s]?$")
    public final void thereShouldBeResults(final Integer results) {
        assertEquals(results, searchResultsPage.getResultNumber());
    }

    /**
     *
     * @param results Set the results
     */
    @Then("^the following results should be displayed:$")
    public final void theFollowingResultsShouldBeDisplayed(
            final List<ResultRow> results) {
        int actualNumberOfResults = (int) searchResultsPage.getResultNumber();
        assertEquals(results.size(), actualNumberOfResults);
        List<String[]> actualResults = searchResultsPage.getResults();
        for (int i = 0; i < actualNumberOfResults; i++) {
            assertArrayEquals(actualResults.get(i), results.get(i).toArray());
        }
    }

    /**
     *
     * @param message Set the message
     */
    @Then("^the search results page should display \"([^\"]*)\"$")
    public final void theSearchResultsPageShouldDisplay(final String message) {
        assertEquals(message, searchResultsPage.getMessage());
    }

    /**
     *
     * @param options
     *            Set the options
     *
     */
    @Then("^the Type facet should have the following options:$")
    public final void thereShouldBeOptionsForClassFacet(final List<Row> options) {
        assertFacets("Type", options);

    }

    /**
    *
    * @param options
    *            Set the options
    *
    */
   @Then("^the Family facet should have the following options:$")
   public final void thereShouldBeOptionsForFamilyFacet(final List<Row> options) {
       assertFacets("Family", options);

   }

    /**
     * @param facetName Set the facet name
     * @param options
     *            Set the options
     */
    public final void assertFacets(final String facetName, final List<Row> options) {
        String[] expected = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            expected[i] = options.get(i).facet;
        }
        String[] actual = searchResultsPage.getFacets(facetName);
        System.out.println(expected);
        System.out.println(actual);
        assertArrayEquals(expected, actual);

    }

    /**
     * @param identifier
     *            Set the page
     */
    @When("^I navigate to taxon page \"([^\"]*)\"$")
    public final void iNavigateToThePage(final String identifier) {
        taxonPage = portal.getTaxonPage(identifier);
    }

    /**
     *
     * @param title
     *            Set the title
     */
    @Then("^the page title should be \"([^\"]*)\"$")
    public final void thePageTitleShouldBeAcorus(final String title) {
        assertEquals(title, taxonPage.getTaxonName());
    }

    /**
     *
     * @param clazz
     *            Set the page class
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
     * @param protologue
     *            Set the protologue
     */
    @Then("^the protologue should be \"([^\"]*)\"$")
    public final void theProtologueShouldBe(final String protologue) {
        assertEquals(taxonPage.getProtologue(), protologue);
    }

    /**
     *
     * @param caption
     *            Set the caption
     */
    @Then("^the main image caption should be \"([^\"]*)\"$")
    public final void theMainImageCaptionShouldBe(final String caption) {
        assertEquals(caption, taxonPage.getMainImageCaption());
    }

    /**
     *
     * @param image
     *            Set the image
     */
    @Then("^the main image should be \"([^\"]*)\"$")
    public final void theMainImageShouldBe(final String image) {
        assertEquals(image, taxonPage.getMainImage());
    }

    /**
     *
     * @param thumbnails Set the thumbnails
     */
    @Then("^there should be (\\d+) thumbnails$")
    public final void thereShouldBeThumbnails(final int thumbnails) {
        assertEquals(thumbnails, taxonPage.getThumbnails());
    }

    /**
     *
     * @param url Set the url
     */
    @Then("^the distribution map should be \"([^\"]*)\"$")
    public final void theDistributionMapShouldBe(final String url) {
        assertEquals(url, taxonPage.getDistributionMap());
    }

    /**
     *
     * @author ben
     *
     */
    public static class Row implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        /**
         *
         */
        public String facet;
    }

    /**
    *
    * @author ben
    *
    */
   public static class ResultRow {
       /**
        *
        */
       public String page;
       /**
        *
        */
       public String text;

       /**
       *
       * @return the row as an array
       */
      public final String[] toArray() {
          return new String[] {page, text };
      }
    }
}
