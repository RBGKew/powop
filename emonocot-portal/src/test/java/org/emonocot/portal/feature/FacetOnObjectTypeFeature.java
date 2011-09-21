package org.emonocot.portal.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.emonocot.model.media.Image;
import org.emonocot.portal.driver.Portal;
import org.emonocot.portal.driver.SearchResultsPage;
import org.emonocot.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;

import cuke4duke.Table;
import cuke4duke.annotation.After;
import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;
import cuke4duke.spring.StepDefinitions;

/**
 *
 * @author ben
 *
 */
@StepDefinitions
public class FacetOnObjectTypeFeature {
    /**
     *
     */
    private String imageId;

    /**
     *
     */
    private SearchResultsPage searchResultsPage;

    /**
     *
     */
    @Autowired
    private ImageService imageService;

    /**
    *
    */
    @Autowired
    private Portal portal;

    /**
     *
     * @param id
     *            Set the image identifier
     * @param caption
     *            Set the image caption
     */
    @Given("^there is an image with id \"([^\"]+)\" and caption \"([^\"]+)\"$")
    public final void thereIsAnImageWithIdAndCaption(final String id,
            final String caption) {
        this.imageId = id;
        Image i = new Image();
        i.setCaption(caption);
        i.setIdentifier(id);
        imageService.save(i);
    }

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
    @Then("^there should be the following options$")
    public final void thereShouldBeOptions(final Table options) {
        assertEquals((Integer) options.rows().get(0).size(),
                searchResultsPage.getClassFacetNumber());
        assertArrayEquals(
                options.rows().get(0)
                        .toArray(new String[options.rows().get(0).size()]),
                searchResultsPage.getClassFacets());

    }

    /**
   *
   */
    @After
    public final void tearDown() {
        if (imageId != null) {
            imageService.delete(imageId);
        }
    }

}
