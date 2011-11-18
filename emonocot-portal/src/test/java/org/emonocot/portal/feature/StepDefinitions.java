package org.emonocot.portal.feature;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import org.emonocot.portal.driver.HomePage;
import org.emonocot.portal.driver.IllustratedPage;
import org.emonocot.portal.driver.ImagePage;
import org.emonocot.portal.driver.LoginPage;
import org.emonocot.portal.driver.PageObject;
import org.emonocot.portal.driver.Portal;
import org.emonocot.portal.driver.ProfilePage;
import org.emonocot.portal.driver.RegistrationPage;
import org.emonocot.portal.driver.RequiresLoginException;
import org.emonocot.portal.driver.SearchResultsPage;
import org.emonocot.portal.driver.SourcePage;
import org.emonocot.portal.driver.TaxonPage;
import org.emonocot.portal.driver.TestDataManager;
import org.emonocot.portal.rows.GroupRow;
import org.emonocot.portal.rows.ImageRow;
import org.emonocot.portal.rows.LoginRow;
import org.emonocot.portal.rows.ReferenceRow;
import org.emonocot.portal.rows.RegistrationRow;
import org.emonocot.portal.rows.SourceRow;
import org.emonocot.portal.rows.TaxonRow;
import org.emonocot.portal.rows.UserRow;
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
public class StepDefinitions {

    /**
     *
     */
    private static final Integer MILLISECONDS_IN_A_SECOND = 1000;

    /**
     *
     */
    private PageObject currentPage;

    /**
    *
    */
    @Autowired
    private Portal portal;

    /**
     *
     */
    @Autowired
    private TestDataManager testDataManager;

    /**
    *
    * @param imageRows set the image rows
    */
   @Given("^there are images with the following properties:$")
   public final void thereAreImagesWithTheFollowingProperties(
            final List<ImageRow> imageRows) {
        for (ImageRow imageRow : imageRows) {
            testDataManager.createImage(imageRow.identifier,
                     imageRow.caption, imageRow.url, imageRow.source);
        }
    }

   /**
    *  @param rows Set the rows
    */
   @Given("^there are groups with the following properties:$")
    public final void thereGroupsWithTheFollowingProperties(
            final List<GroupRow> rows) {
       for (GroupRow row : rows) {
           testDataManager.createGroup(row.identifier, row.permission1);
       }
   }

   /**
    *
    * @param rows Set the rows
    */
    @Given("^there are users with the following properties:$")
    public final void thereAreUsersWithTheFollowingProperties(
            final List<UserRow> rows) {
        for (UserRow row : rows) {
           testDataManager.createUser(row.identifier, row.password, row.group1);
       }
   }

   /**
    *
    * @param rows Set the rows
    */
   @Given("^there are source systems with the following properties:$")
   public final void thereAreSourceSystemsWithTheFollowingProperties(List<SourceRow> rows) {
       for (SourceRow row : rows) {
    	   testDataManager.createSourceSystem(row.identifier, row.uri);
       }
   }



/**
    *
    * @param name Set the name
    */
   @Given("^there are no taxa called \"([^\"]*)\"$")
   public final void thereAreNoTaxaCalled(final String name) {
       testDataManager.assertNoTaxaWithName(name);
   }

    /**
    *
    * @param rows set the taxon rows
    */
   @Given("^there are taxa with the following properties:$")
   public final void thereAreTaxaWithTheFollowingProperties(
           final List<TaxonRow> rows) {
       for (TaxonRow row : rows) {
            testDataManager.createTaxon(row.name, row.family, row.identifier,
                    row.rank, row.status, row.diagnostic, row.habitat, row.general,
                    row.protologue, row.protologueMicroReference, row.image1, row.image2,
                    row.image3, row.distribution1, row.distribution2, row.distribution3, row.source, row.created);
       }

   }

   /**
   *
   * @param rows set the rows
   */
  @Given("^there are references with the following properties:$")
  public final void thereAreReferencesWithTheFollowingProperties(
          final List<ReferenceRow> rows) {
      for (ReferenceRow row : rows) {
            testDataManager.createReference(row.identifier, row.title,
                    row.datePublished, row.volume, row.page);
      }
  }
    /**
     *
     */
    @After
    public final void tearDown() {
        currentPage.logOut();
        currentPage.disableAuthentication();
        testDataManager.tearDown();
    }

    /**
     *
     * @param query
     *            Set the search query
     */
    @When("^I search for \"([^\"]+)\"$")
    public final void whenISearchFor(final String query) {
        currentPage = portal.search(query);
    }

    /**
     *
     */
    @When("^I am on the search page$")
    public final void iAmOnTheSearchPage() {
        currentPage = portal.search("");
    }

    /**
     *
     * @param sort
     *            Set the sort selection
     */
   @When("^I sort \"([^\"]+)\"$")
   public final void whenISort(final String sort) {
       currentPage = ((SearchResultsPage) currentPage).sort(sort);
   }

    /**
     * @param facetName the facet to restric
     * @param facetValue
     *            Set the facet value to select
     */
    @When("^I restrict the \"([^\"]+)\" by selecting \"([^\"]+)\"$")
    public final void iSelect(final String facetName, final String facetValue) {
        currentPage = ((SearchResultsPage) currentPage).selectFacet(facetName,
                facetValue);
    }

    /**
     *
     * @param query Set the query
     */
    @When("^I type for \"([^\"]*)\" in the search box$")
    public final void typeInTheSearchBox(final String query) {
        ((SearchResultsPage) currentPage).setQuery(query);
    }

   /**
    *
    * @param wait Set the wait time
    */
   @When("^I wait for (\\d+) second[s]?$")
   public final void typeInTheSearchBox(final Integer wait) {
       currentPage.waitForAjax(wait * MILLISECONDS_IN_A_SECOND);
   }

   /**
    *
    */
   @When("I select the login link in the header")
   public final void selectLoginLink() {
       currentPage = currentPage.selectLoginLink();
   }

    /**
     *
     * @param results
     *            Set the number of results
     */
    @Then("^there should be (\\d) result[s]?$")
    public final void thereShouldBeResults(final Integer results) {
        assertEquals(results, ((SearchResultsPage) currentPage).getResultNumber());
    }

    /**
     *
     * @param results Set the results
     */
    @Then("^the following results should be displayed:$")
    public final void theFollowingResultsShouldBeDisplayed(
            final List<ResultRow> results) {
        int actualNumberOfResults = (int) ((SearchResultsPage) currentPage).getResultNumber();
        assertEquals(results.size(), actualNumberOfResults);
        List<String[]> actualResults = ((SearchResultsPage) currentPage).getResults();
        for (int i = 0; i < actualNumberOfResults; i++) {
            assertArrayEquals(actualResults.get(i), results.get(i).toArray());
        }
    }

    /**
     *
     * @param link
     */
    @Then("^there should be a link to \"([^\"]*)\"$")
    public void thereShouldBeALinkTo(String link) {
    	assertEquals(link, ((SourcePage) currentPage).getLink());
    }

    @Then("^the source name should be \"([^\"]*)\"$")
    public void theSourceNameShouldBe(String name) {
    	assertEquals(name, ((SourcePage) currentPage).getName());
    }

    /**
     *
     * @param message Set the message
     */
    @Then("^the search results page should display \"([^\"]*)\"$")
    public final void theSearchResultsPageShouldDisplay(final String message) {
        assertEquals(message, ((SearchResultsPage) currentPage).getMessage());
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
   *
   * @param options
   *            Set the options
   *
   */
  @Then("^the Rank facet should have the following options:$")
  public final void thereShouldBeOptionsForRankFacet(final List<Row> options) {
      assertFacets("Rank", options);
  }

  /**
  *
   * @param options
   *            Set the options
   *
   */
  @Then("^the Status facet should have the following options:$")
  public final void thereShouldBeOptionsForStatusFacet(final List<Row> options) {
      assertFacets("Status", options);
  }

  /**
  *
   * @param options
   *            Set the options
   *
   */
  @Then("^the autocomplete box should display the following options:$")
  public final void theAutocompleteBoxShouldDisplayTheFollowingOptions(final List<Row> options) {
      String[] expected = new String[options.size()];
      for (int i = 0; i < options.size(); i++) {
          expected[i] = options.get(i).option;
      }
      String[] actual = ((SearchResultsPage)currentPage).getAutocompleteOptions();
      assertArrayEquals(expected, actual);
  }

    /**
     * @param facetName Set the facet name
     * @param options
     *            Set the options
     */
    public final void assertFacets(final String facetName, final List<Row> options) {
        String[] expected = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            expected[i] = options.get(i).option;
        }
        String[] actual = ((SearchResultsPage) currentPage).getFacets(facetName);
        assertArrayEquals(expected, actual);

    }

    /**
     * @param identifier
     *            Set the page
     */
    @When("^I navigate to taxon page \"([^\"]*)\"$")
    public final void iNavigateToThePage(final String identifier) {
        currentPage = portal.getTaxonPage(identifier);
    }

    /**
     *
     * @param identifier Set the page
     */
    @When("^I navigate to image page \"([^\"]*)\"$")
    public final void navigateToImagePage(final String identifier) {
    	currentPage = portal.getImagePage(identifier);
    }

    /**
     *
     * @param identifier Set the page
     */
    @When("^I navigate to source page \"([^\"]*)\"$")
    public final void navigateToSourcePage(final String identifier) {
    	currentPage = portal.getSourcePage(identifier);
    }

    /**
     *
     * @param thumbnail Set the thumbnail
     */
    @When("I select the (\\d+)\\w+ thumbnail")
    public final void selectTheThumbnail(final Integer thumbnail) {
        ((TaxonPage) currentPage).selectThumbnail(thumbnail);
    }

    /**
     *
     * @param title
     *            Set the title
     */
    @Then("^the page title should be \"([^\"]*)\"$")
    public final void thePageTitleShouldBeAcorus(final String title) {
        assertEquals(title, ((TaxonPage) currentPage).getTaxonName());
    }

    /**
     *
     * @param identifier Set the identifier
     */
    @Then("the image page for image \"([^\"]*)\" should be displayed")
    public final void theImagePageShouldBeDisplayed(final String identifier) {
        assertEquals(currentPage.getClass(), ImagePage.class);
        assertEquals(currentPage.getUri(), "image/" + identifier);
    }

    /**
     *
     * @param attribute
     *            Set the title attribute to check
     * @param value Set the expected value
     */
    @Then("^the page title \"([^\"]*)\" should be \"([^\"]*)\"$")
    public final void theTitleAttributeShouldBeTaxonName(final String attribute, final String value) {
        assertEquals(value, ((TaxonPage) currentPage).getTaxonNameStyle(attribute));
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
        assertEquals(paragraph, ((TaxonPage) currentPage).getParagraph(heading));
    }

    /**
     *
     * @param heading
     *            Set the heading
     */
    @Then("^there should not be a paragraph with the heading \"([^\"]*)\"$")
    public final void thereShouldNotBeAParagraphWithTheHeading(
            final String heading) {
        assertFalse(((TaxonPage) currentPage).doesParagraphExist(heading));
    }

    /**
     *
     * @param protologue
     *            Set the protologue
     */
    @Then("^the protologue should be \"([^\"]*)\"$")
    public final void theProtologueShouldBe(final String protologue) {
        assertEquals(((TaxonPage) currentPage).getProtologue(), protologue);
    }

    /**
     *
     * @param caption
     *            Set the caption
     */
    @Then("^the main image caption should be \"([^\"]*)\"$")
    public final void theMainImageCaptionShouldBe(final String caption) {
        assertEquals(caption, ((IllustratedPage) currentPage).getMainImageCaption());
    }

    /**
     *
     * @param image
     *            Set the image
     */
    @Then("^the main image should be \"([^\"]*)\"$")
    public final void theMainImageShouldBe(final String image) {
        assertEquals(image, ((IllustratedPage) currentPage).getMainImage());
    }

    /**
     *
     * @param thumbnails Set the thumbnails
     */
    @Then("^there should be (\\d+) thumbnail[s]?$")
    public final void thereShouldBeThumbnails(final int thumbnails) {
        assertEquals(thumbnails, ((IllustratedPage) currentPage).getThumbnails());
    }

    /**
     *
     * @param url Set the url
     */
    @Then("^the distribution map should be \"([^\"]*)\"$")
    public final void theDistributionMapShouldBe(final String url) {
        assertEquals(url, ((TaxonPage) currentPage).getDistributionMap());
    }

    /**
     *
     */
    @When("^I select the registration link$")
    public final void selectTheRegistrationLink() {
        currentPage = ((HomePage) currentPage).selectRegistrationLink();
    }

   /**
    *
    */
   @When("^I select the main image$")
   public final void selectTheMainImage() {
       currentPage = ((TaxonPage) currentPage).selectMainImage();
   }

    /**
     *
     * @param data Set the registration data
     */
    @When("^I enter the following data into the registration form:$")
    public final void enterTheFollowingDataIntoTheRegistrationForm(
            final List<RegistrationRow> data) {
        ((RegistrationPage) currentPage).setUsername(data.get(0).username);
        ((RegistrationPage) currentPage)
                .setRepeatUsername(data.get(0).repeatUsername);
        ((RegistrationPage) currentPage).setPassword(data.get(0).password);
        ((RegistrationPage) currentPage)
                .setRepeatPassword(data.get(0).repeatPassword);
    }

    /**
    *
    * @param data Set the login data
    */
   @When("^I enter the following data into the login form:$")
   public final void enterTheFollowingDataIntoTheFormForm(
           final List<LoginRow> data) {
       ((LoginPage) currentPage).setUsername(data.get(0).username);
       ((LoginPage) currentPage).setPassword(data.get(0).password);
   }

    /**
     *
     * @param source Set the source admin page
     */
    @When("^I navigate to source admin page for \"([^\"]*)\"$")
    public final void navigateToSourceAdminPageFor(final String source) {
        try {
            currentPage = portal.getSourceAdminPage(source);
        } catch (RequiresLoginException rle) {
            currentPage = rle.getLoginPage();
        }
    }

    /**
     *
     */
    @When("^I submit the registration form$")
    public final void submitTheRegistrationForm() {
        currentPage = ((RegistrationPage) currentPage).submit();
    }

   /**
    *
    */
   @When("^I submit the login form$")
   public final void submitTheLoginForm() {
       currentPage = ((LoginPage) currentPage).submit();
   }

    /**
     *
     */
    @Then("^my profile page should be displayed$")
    public final void myProfilePageShouldBeDisplayed() {
        assertEquals(ProfilePage.class, currentPage.getClass());
    }

   /**
    *
    */
   @Then("^I should be logged in to the portal$")
   public final void iShouldBeLoggedInToThePortal() {
       assertTrue(currentPage.loggedIn());
   }

    /**
     *
     */
    @Then("^the login page should be displayed$")
    public final void theLoginPageShouldBeDisplayed() {
        assertEquals(LoginPage.class, currentPage.getClass());
    }

    /**
     *
     */
    @When("^I am not authenticated$")
    public final void amNotAuthenticated() {
        portal.disableAuthentication();
    }

    /**
     *
     */
    @When("^I am on the portal home page$")
    public final void openThePortalHomePage() {
        currentPage = portal.getHomePage();
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
        public String option;
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
