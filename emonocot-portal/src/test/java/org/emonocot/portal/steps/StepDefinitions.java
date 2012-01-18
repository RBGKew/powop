package org.emonocot.portal.steps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import org.emonocot.portal.driver.ClassificationPage;
import org.emonocot.portal.driver.GroupUpdatePage;
import org.emonocot.portal.driver.HomePage;
import org.emonocot.portal.driver.IllustratedPage;
import org.emonocot.portal.driver.ImagePage;
import org.emonocot.portal.driver.GroupFormPage;
import org.emonocot.portal.driver.GroupPage;
import org.emonocot.portal.driver.LoginPage;
import org.emonocot.portal.driver.PageObject;
import org.emonocot.portal.driver.Portal;
import org.emonocot.portal.driver.ProfilePage;
import org.emonocot.portal.driver.RegistrationPage;
import org.emonocot.portal.driver.RequiresLoginException;
import org.emonocot.portal.driver.SearchResultsPage;
import org.emonocot.portal.driver.SourceAdminPage;
import org.emonocot.portal.driver.SourceJobPage;
import org.emonocot.portal.driver.SourceFormPage;
import org.emonocot.portal.driver.SourcePage;
import org.emonocot.portal.driver.TaxonPage;
import org.emonocot.portal.rows.AccessControlRow;
import org.emonocot.portal.rows.GroupRow;
import org.emonocot.portal.rows.LoginRow;
import org.emonocot.portal.rows.RegistrationRow;
import org.emonocot.portal.rows.SourceRow;
import org.emonocot.portal.rows.SummaryRow;
import org.emonocot.portal.rows.UserRow;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.After;
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
    @After
    public final void tearDown() {
        if (currentPage != null) {
            currentPage.logOut();
            currentPage.disableAuthentication();
        }
    }

    /**
     *
     * @param nodeName the node name to expand
     */
    @When("^I expand \"([^\"]*)\"$")
    public final void iExpand(final String nodeName) {
        ((ClassificationPage) currentPage).expandNode(nodeName);
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
    @When("^I am on the classification page$")
    public final void iAmOnTheClassificationPage() {
        currentPage = portal.getClassificationPage();
    }

   /**
    *
    */
   @When("^I am on the group page$")
   public final void iAmOnTheGroupPage() {
       currentPage = portal.getGroupPage();
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
     * @param text Set the link text
     */
    @When("^I select \"(Create a new group)\"$")
    public final void iSelect(final String text) {
        currentPage = currentPage.selectLink(text, GroupFormPage.class);
    }

    /**
     * @param groupRows
     *            set the group rows
     */
    @When("^I enter the following data into the group form:$")
    public final void iEnterTheFollowingDataInTheGroupForm(
            final List<GroupRow> groupRows) {
        ((GroupFormPage) currentPage).setGroupName(groupRows.get(0).identifier);
    }
    
    /**
     * @param sourceRows
     *            set the source rows
     */
    @When("^I enter the following data into the source form:$")
    public void IEnterTheFollowingDataIntoTheSourceForm(
    		final List<SourceRow> sourceRows) {
    	((SourceFormPage) currentPage).setSourceUri(sourceRows.get(0).uri);
    }

    /**
     *
     * @param aceRows
     *            Set the access control rows
     */
    @When("^I enter the following data into the access controls form:$")
    public final void iEnterTheFollowingDataInTheAccessControlForm(
            final List<AccessControlRow> aceRows) {
        ((GroupUpdatePage) currentPage).setSecureObject(aceRows.get(0).object);
    }

    /**
     *
     * @param userRows Set the user rows
     */
    @When("^I enter the following data into the members form:$")
    public final void iEnterTheFollowingDataIntoTheMembersForm(
            final List<UserRow> userRows) {
        ((GroupUpdatePage) currentPage).setMember(userRows.get(0).identifier);
    }

    /**
     *
     * @param linkText Set the link text
     */
    @When("^I select \"(Edit this group)\"$")
    public final void iSelectEditThisGroup(final String linkText) {
        currentPage = ((GroupPage) currentPage).selectLink(linkText,
                GroupUpdatePage.class);
    }
    
    /**
    *
    * @param linkText Set the link text
    */
   @When("^I select \"(Edit this source)\"$")
   public final void iSelectEditThisSource(final String linkText) {
       currentPage = ((SourceAdminPage) currentPage).selectLink(linkText,
               SourceFormPage.class);
   }

    /**
     *
     */
    @When("^I submit the access controls form$")
    public final void iSubmitTheAccessControlsForm() {
        currentPage = ((GroupUpdatePage) currentPage).submitAceForm();
    }

    /**
     *
     */
    @When("^I submit the group form$")
    public final void iSubmitTheGroupForm() {
         currentPage = ((GroupFormPage) currentPage).submit();
    }
    
    /**
    *
    */
   @When("^I submit the source form$")
   public final void iSubmitTheSourceForm() {
        currentPage = ((SourceFormPage) currentPage).submit();
   }

    /**
     *
     */
    @When("^I submit the members form$")
    public final void iSubmitTheMembersForm() {
       currentPage = ((GroupUpdatePage) currentPage).submitMemberForm();
    }

    /**
     *
     * @param groupName Set the groupName
     */
    @When("^I go to the page for the group \"([^\"]*)\"$")
    public final void iGoToThePageForTheGroup(final String groupName) {
      currentPage = portal.getGroupPage(groupName);
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
   * @param view Click on the grid icon
   */
   @When("^I click on the \"([^\"]*)\" icon$")
   public final void iClickOnTheGridIcon(final String view) {
       currentPage = ((SearchResultsPage) currentPage).view(view);
   }

   /**
    *
    * @param username Set the username
    * @param password Set the password
    */
    @When("^I am logged in as \"([^\"]*)\" with the password \"([^\"]*)\"$")
    public final void iAmLoggedInAsWithPassword(final String username,
            final String password) {
        currentPage = portal.getLoginPage();
        ((LoginPage) currentPage).setUsername(username);
        ((LoginPage) currentPage).setPassword(password);
        currentPage = ((LoginPage) currentPage).submit();
    }

    /**
     *
     * @param job Set the job
     */
    @When("^I select the (\\d+)\\w+ job$")
    public final void iSelectTheJob(final int job) {
        currentPage = ((SourceAdminPage) currentPage).selectJob(job);
    }

    /**
     *
     * @param category Set the category
     */
    @When("^I select the job category \"([^\"]*)\"$")
    public final void iSelectTheJobCategory(final String category) {
        currentPage = ((SourceJobPage) currentPage).selectCategory(category);
    }

    /**
     * @param results Set the expected results
     */
    @Then("^the following nodes should be displayed:$")
    public final void theTheFollowingNodesShouldBeDisplayed(
            final List<ResultRow> results) {
        int actualNumberOfResults = (int) ((ClassificationPage) currentPage)
                .getNodeNumber();
        assertEquals(results.size(), actualNumberOfResults);
        List<String[]> actualResults = ((ClassificationPage) currentPage)
                .getNodes();
        for (int i = 0; i < actualNumberOfResults; i++) {
            assertArrayEquals(actualResults.get(i), results.get(i).toArray());
        }
    }

    /**
     *
     * @param message Set the expected info message
     */
    @Then("^an info message should say \"([^\"]*)\"$")
    public final void anInfoMessageShouldSay(final String message) {
        assertEquals(message, currentPage.getInfoMessage());
    }

    /**
    *
    * @param results Set the results
    */
   @Then("^the summary results should be as follows:$")
   public final void theSummaryResultsShouldBeAsFollows(
           final List<SummaryRow> results) {
        int actualNumberOfResults = (int) ((SourceJobPage) currentPage)
                .getResultNumber();
        assertEquals(results.size(), actualNumberOfResults);
        List<String[]> actualResults = ((SourceJobPage) currentPage)
                .getResults();
       for (int i = 0; i < actualNumberOfResults; i++) {
           assertArrayEquals(actualResults.get(i), results.get(i).toArray());
       }
   }

    /**
     *
     * @param jobs Set the number of jobs listed
     */
    @Then("^there should be (\\d+) jobs listed$")
    public final void thereShouldBeJobsListed(final int jobs) {
        assertEquals(jobs, ((SourceAdminPage) currentPage).getJobsListed()
                .intValue());
    }
    
    @Then("^the source uri should be \"([^\"]*)\"$")
    public void theSourceUriShouldBe(final String uri) {
    	assertEquals(uri, ((SourceAdminPage) currentPage).getSourceUri());
    }

    /**
     *
     */
    @Then("^the view icons should be displayed$")
    public final void theViewIconsShouldBeDisplayed() {
        assertTrue(((SearchResultsPage) currentPage).viewIconDisplay());
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
        assertEquals(results,
                ((SearchResultsPage) currentPage).getResultNumber());
    }

    /**
     *
     * @param ancestors
     *            Set the ancestors
     */
    @Then("^there should be (\\d+) ancestor[s]?$")
    public final void thereShouldBeAncestors(final Integer ancestors) {
        assertEquals(ancestors, ((TaxonPage) currentPage).getAncestorsNumber());
    }

    /**
     *
     * @param subordinate
     *            Set the value of subordinate taxa link
     */
    @Then("^the subordinate taxon link should say \"([^\"]*)\"$")
    public final void theSubordinateTaxonLinkShouldSay(
                  final String subordinate) {
        assertEquals(subordinate,
                ((TaxonPage) currentPage).getSubordinateTaxa());
    }

    /**
     *
     * @param subordinateNumber
     *            Set the number of subordinate taxa
     */
    @Then("^there should be (\\d+) subordinate taxa$")
    public final void thereShouldBeSubordinateTaxa(
            final Integer subordinateNumber) {
        assertEquals(subordinateNumber,
                ((TaxonPage) currentPage).getChildrenNumber());
    }

    /**
     *
     * @param results
     *            Set the results
     */
    @Then("^the following results should be displayed:$")
    public final void theFollowingResultsShouldBeDisplayed(
            final List<ResultRow> results) {
        int actualNumberOfResults = (int) ((SearchResultsPage) currentPage)
                .getResultNumber();
        assertEquals(results.size(), actualNumberOfResults);
        List<String[]> actualResults = ((SearchResultsPage) currentPage)
                .getResults();
        for (int i = 0; i < actualNumberOfResults; i++) {
            assertArrayEquals(actualResults.get(i), results.get(i).toArray());
        }
    }

    /**
     *
     * @param number set the thumbnail caption to compare to
     */
    @Then("^the main image caption should be the (\\d+)\\w+ caption$")
    public final void theMainImageCaptionShouldEqualTheCaption(final int number) {
        assertEquals(((TaxonPage) currentPage).getMainImageProperty(null),
                ((TaxonPage) currentPage).getThumbnailCaption(number));
    }

    /**
     *
     * @param number set the thumbnail image to compare to
     */
    @Then("^the main image should be the (\\d+)\\w+ image$")
    public final void theMainImageShouldBeTheImage(final int number) {
        assertEquals(((TaxonPage) currentPage).getMainImage(),
                ((TaxonPage) currentPage).getThumbnailImage(number));
    }

    /**
     *
     * @param citeKey Set the citation key
     * @param bibliographyEntry Set the expected bibliography entry
     */
    @Then("^the bibliography entry \"([^\"]*)\" should be \"([^\"]*)\"$")
    public final void theBibliographyEntryShouldBe(final String citeKey,
            final String bibliographyEntry) {
        assertEquals(((TaxonPage) currentPage).getBibliographyEntry(citeKey),
                bibliographyEntry);
    }

    /**
     *
     * @param topic Set the topic
     * @param citations Set the expected citations
     */
    @Then("^the citation for the \"([^\"]*)\" topic should be \"([^\"]*)\"$")
    public final void theCitationForTheTopicShouldBe(final String topic,
            final String citations) {
        assertEquals(((TaxonPage) currentPage).getCitations(topic),
                citations);
    }
    /**
     *
     * @param link Set the link
     */
    @Then("^there should be a link to \"([^\"]*)\"$")
    public final void thereShouldBeALinkTo(final String link) {
        assertEquals(link, ((SourcePage) currentPage).getLink());
    }

    /**
     *
     * @param title Set the name
     */
    @Then("^the source title should be \"([^\"]*)\"$")
    public final void theSourceTitleShouldBe(final String title) {
        assertEquals(title, ((SourcePage) currentPage).getTitle());
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
     * 
     * 
     */
    @Then("^the images should be displayed in a grid.$")
    public void theImagesShouldBeDisplayedInAGrid() {
    	assertTrue(((SearchResultsPage) currentPage).resultsAreDisplayedInGrid());
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
     */
    @Then("the image page should be displayed")
    public final void theImagePageShouldBeDisplayed() {
        assertEquals(currentPage.getClass(), ImagePage.class); 
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
     * @param property Set the property to find
     * @param value
     *            Set the expected value
     */
    @Then("^the main image ([^\"]*) should be \"([^\"]*)\"$")
    public final void theMainImagePropertyShouldBe(final String property,
            final String value) {
        assertEquals(value,
                ((IllustratedPage) currentPage).getMainImageProperty(property));
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
