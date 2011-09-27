package org.emonocot.portal.feature;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.Serializable;
import java.util.List;

import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;
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
    *
    * @param clazz
    *            Set the class to restrict the search results to
    */
   @When("^I select \"([^\"]+)\"$")
   public final void iSelect(final String clazz) {
       searchResultsPage = searchResultsPage.selectClassFacet(clazz);
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
     * @param options
     *            Set the options
     *
     */
    @Then("^there should be the following options:$")
    public final void thereShouldBeOptions(final List<Row> options) {
        assertArrayEquals(
                options.get(0).toArray(),
                searchResultsPage.getClassFacets());

    }

   /**
    * @param identifier Set the page
    */
   @When("^I navigate to taxon page \"([^\"]*)\"$")
   public final void iNavigateToThePage(final String identifier) {
       taxonPage = portal
               .getTaxonPage(identifier);
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
   * @param protologue Set the protologue
   */
  @Then("^the protologue should be \"([^\"]*)\"$")
  public final void theProtologueShouldBe(final String protologue) {
      assertEquals(taxonPage.getProtologue(), protologue);
  }
  
  @Then("^the main image caption should be \"([^\"]*)\"$")
  public void theMainImageCaptionShouldBe(String caption) {
      assertEquals(caption,taxonPage.getMainImageCaption());
  }

  @Then("^the main image should be \"([^\"]*)\"$")
  public void theMainImageShouldBe(String image) {
	  assertEquals(image,taxonPage.getMainImage());
  }
  
  @Then("^there should be (\\d+) thumbnails$")
  public void thereShouldBeThumbnails(int thumbnails) {
	  assertEquals(thumbnails,taxonPage.getThumbnails());
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
        public String first;
        /**
         *
         */
        public String second;

        /**
         * @return the value of the object as a string
         */
        public final String toString() {
            return "{first: \"" + first + "\", second: \"" + second + "\"}";
        }

        /**
         *
         * @return the row as an array
         */
        public final String[] toArray() {
            return new String[] {first, second};
        }
    }
}
