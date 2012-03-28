package org.emonocot.portal.steps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import org.emonocot.portal.driver.*;
import org.emonocot.portal.driver.source.JobDetails;
import org.emonocot.portal.rows.AccessControlRow;
import org.emonocot.portal.rows.GroupRow;
import org.emonocot.portal.rows.JobRow;
import org.emonocot.portal.rows.LoginRow;
import org.emonocot.portal.rows.RegistrationRow;
import org.emonocot.portal.rows.SourceRow;
import org.emonocot.portal.rows.SummaryRow;
import org.emonocot.portal.rows.UserRow;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.After;
import cucumber.annotation.en.And;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

/**
 * @author ben
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
     * @param nodeName
     *            the node name to expand
     */
    @When("^I expand \"([^\"]*)\"$")
    public final void iExpand(final String nodeName) {
        ((Classification) currentPage).expandNode(nodeName);
    }

    /**
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
    @When("^I am on the source admin page$")
    public final void iAmOnTheSourceAdminPage() {
        currentPage = portal.getSourceAdminPage();
    }

    @When("^I am on the source admin page for \"([^\"]*)\"$")
    public void iAmOnTheSourceAdminPageFor(String source) {
        currentPage = portal.getSourceAdminPage(source);
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
        currentPage = portal.getListGroupsPage();
    }

    /**
     *
     */
    @When("^I am on the search page$")
    public final void iAmOnTheSearchPage() {
        currentPage = portal.search("");
    }

    /**
     * @param text
     *            Set the link text
     */
    @When("^I select \"(Create a new job)\"$")
    public final void iSelectCreateANewJob(final String text) {
        currentPage = currentPage.selectLink(text,
                org.emonocot.portal.driver.admin.source.job.Create.class);
    }

    /**
     * @param text
     *            Set the link text
     */
    @When("^I select \"(Create a new group)\"$")
    public final void iSelectCreateANewGroup(final String text) {
        currentPage = currentPage.selectLink(text,
                org.emonocot.portal.driver.group.Create.class);
    }

    /**
     * @param groupRows
     *            set the group rows
     */
    @When("^I enter the following data into the group form:$")
    public final void iEnterTheFollowingDataInTheGroupForm(
            final List<GroupRow> groupRows) {
        ((org.emonocot.portal.driver.group.Create) currentPage)
                .setGroupName(groupRows.get(0).identifier);
    }

    /**
     * @param sourceRows
     *            set the source rows
     */
    @When("^I enter the following data into the create source form:$")
    public final void iEnterTheFollowingDataIntoTheCreateSourceForm(
            final List<SourceRow> sourceRows) {

        ((org.emonocot.portal.driver.admin.source.Create) currentPage)
                .setObjectIdentifier(sourceRows.get(0).identifier, "identifier");
        ((org.emonocot.portal.driver.admin.source.Create) currentPage)
                .setFormField("uri", sourceRows.get(0).uri);
        ((org.emonocot.portal.driver.admin.source.Create) currentPage)
                .setFormField("logoUrl", sourceRows.get(0).logoUrl);
        ((org.emonocot.portal.driver.admin.source.Create) currentPage)
                .setFormField("title", sourceRows.get(0).title);
    }

    /**
     * @param sourceRows
     *            set the source rows
     */
    @When("^I enter the following data into the update source form:$")
    public final void iEnterTheFollowingDataIntoTheUpdateSourceForm(
            final List<SourceRow> sourceRows) {
        ((org.emonocot.portal.driver.source.Update) currentPage).setFormField(
                "uri", sourceRows.get(0).uri);
        ((org.emonocot.portal.driver.source.Update) currentPage).setFormField(
                "logoUrl", sourceRows.get(0).logoUrl);
        ((org.emonocot.portal.driver.source.Update) currentPage).setFormField(
                "title", sourceRows.get(0).title);
    }

    /**
     * @param aceRows
     *            Set the access control rows
     */
    @When("^I enter the following data into the access controls form:$")
    public final void iEnterTheFollowingDataInTheAccessControlForm(
            final List<AccessControlRow> aceRows) {
        ((org.emonocot.portal.driver.group.Update) currentPage)
                .setSecureObject(aceRows.get(0).object);
    }

    /**
     * @param userRows
     *            Set the user rows
     */
    @When("^I enter the following data into the members form:$")
    public final void iEnterTheFollowingDataIntoTheMembersForm(
            final List<UserRow> userRows) {
        ((org.emonocot.portal.driver.group.Update) currentPage)
                .setMember(userRows.get(0).identifier);
    }

    /**
     * @param linkText
     *            Set the link text
     */
    @When("^I select \"(Create a new source)\"$")
    public final void iSelectCreateANewSource(final String linkText) {
        currentPage = currentPage.selectLink(linkText,
                org.emonocot.portal.driver.admin.source.Create.class);
    }

    /**
     * @param linkText
     *            Set the link text
     */
    @When("^I select \"(Edit this group)\"$")
    public final void iSelectEditThisGroup(final String linkText) {
        currentPage = ((org.emonocot.portal.driver.group.Show) currentPage)
                .selectLink(linkText,
                        org.emonocot.portal.driver.group.Update.class);
    }

    /**
     * @param linkText
     *            Set the link text
     */
    @When("^I select \"(Edit this source)\"$")
    public final void iSelectEditThisSource(final String linkText) {
        currentPage = ((org.emonocot.portal.driver.source.Show) currentPage)
                .selectLink(linkText,
                        org.emonocot.portal.driver.source.Update.class);
    }

    /**
     *
     */
    @When("^I submit the access controls form$")
    public final void iSubmitTheAccessControlsForm() {
        currentPage = ((org.emonocot.portal.driver.group.Update) currentPage)
                .submitAceForm();
    }

    /**
     *
     */
    @When("^I submit the group form$")
    public final void iSubmitTheGroupForm() {
        currentPage = ((org.emonocot.portal.driver.group.Create) currentPage)
                .submit();
    }

    /**
    *
    */
    @When("^I submit the update source form$")
    public final void iSubmitTheUpdateSourceForm() {
        currentPage = ((org.emonocot.portal.driver.source.Update) currentPage)
                .submit();
    }

    /**
   *
   */
    @When("^I submit the create source form$")
    public final void iSubmitTheCreateSourceForm() {
        currentPage = ((org.emonocot.portal.driver.admin.source.Create) currentPage)
                .submit();
    }

    /**
     *
     */
    @When("^I submit the members form$")
    public final void iSubmitTheMembersForm() {
        currentPage = ((org.emonocot.portal.driver.group.Update) currentPage)
                .submitMemberForm();
    }

    /**
     * @param groupName
     *            Set the groupName
     */
    @When("^I go to the page for the group \"([^\"]*)\"$")
    public final void iGoToThePageForTheGroup(final String groupName) {
        currentPage = portal.getGroupPage(groupName);
    }

    /**
     * @param sort
     *            Set the sort selection
     */
    @When("^I sort \"([^\"]+)\"$")
    public final void whenISort(final String sort) {
        currentPage = ((Search) currentPage).sort(sort);
    }

    /**
     * @param facetName
     *            the facet to restrict
     * @param facetValue
     *            Set the facet value to select
     */
    @When("^I restrict the \"([^\"]+)\" by selecting \"([^\"]+)\"$")
    public final void iSelect(final String facetName, final String facetValue) {
        currentPage = ((Search) currentPage).selectFacet(facetName, facetValue);
    }

    /**
     * @param facetValue
     *            Set the facet value to select
     */
    @When("^I restrict the type of object by selecting \"([^\"]+)\"$")
    public final void facetOnType(final String facetValue) {
        iSelect("CLASS", facetValue);
    }

    /**
     * @param query
     *            Set the query
     */
    @When("^I type for \"([^\"]*)\" in the search box$")
    public final void typeInTheSearchBox(final String query) {
        ((Search) currentPage).setQuery(query);
    }

    /**
     * @param wait
     *            Set the wait time
     */
    @When("^I wait for (\\d+) second[s]?$")
    public final void typeInTheSearchBox(final Integer wait) {
        currentPage.waitForAjax(wait * MILLISECONDS_IN_A_SECOND);
    }

    /**
     * @param view
     *            Click on the grid icon
     */
    @When("^I click on the \"([^\"]*)\" icon$")
    public final void iClickOnTheGridIcon(final String view) {
        currentPage = ((Search) currentPage).view(view);
    }

    /**
     * @param username
     *            Set the username
     * @param password
     *            Set the password
     */
    @When("^I am logged in as \"([^\"]*)\" with the password \"([^\"]*)\"$")
    public final void iAmLoggedInAsWithPassword(final String username,
            final String password) {
        currentPage = portal.getLoginPage();
        ((Login) currentPage).setUsername(username);
        ((Login) currentPage).setPassword(password);
        currentPage = ((Login) currentPage).submit();
    }

    /**
     * @param job
     *            Set the job
     */
    @When("^I select the (\\d+)\\w+ job$")
    public final void iSelectTheJob(final int job) {
        currentPage = ((org.emonocot.portal.driver.source.Show) currentPage)
                .selectJob(job);
    }

    /**
     * @param keyword
     *            Set the keyword
     */
    @When("^I click on the keyword \"([^\"]*)\"$")
    public final void iClickOnTheKeyword(final String keyword) {
        currentPage = ((org.emonocot.portal.driver.image.Show) currentPage)
                .selectKeyword(keyword);
    }

    /**
     * @param category
     *            Set the category
     */
    @When("^I select the job category \"([^\"]*)\"$")
    public final void iSelectTheJobCategory(final String category) {
        currentPage = ((org.emonocot.portal.driver.source.JobDetails) currentPage)
                .selectCategory(category);
    }

    /**
     * @param results
     *            Set the expected results
     */
    @Then("^the following nodes should be displayed:$")
    public final void theTheFollowingNodesShouldBeDisplayed(
            final List<ResultRow> results) {
        int actualNumberOfResults = (int) ((Classification) currentPage)
                .getNodeNumber();
        assertEquals(results.size(), actualNumberOfResults);
        List<String[]> actualResults = ((Classification) currentPage)
                .getNodes();
        for (int i = 0; i < actualNumberOfResults; i++) {
            assertArrayEquals(actualResults.get(i), results.get(i).toArray());
        }
    }

    /**
     * @param message
     *            Set the expected info message
     */
    @Then("^an info message should say \"([^\"]*)\"$")
    public final void anInfoMessageShouldSay(final String message) {
        assertEquals(message, currentPage.getInfoMessage());
    }

    /**
     * @param results
     *            Set the results
     */
    @Then("^the summary results should be as follows:$")
    public final void theSummaryResultsShouldBeAsFollows(
            final List<SummaryRow> results) {
        int actualNumberOfResults = (int) ((JobDetails) currentPage)
                .getResultNumber();
        assertEquals(results.size(), actualNumberOfResults);
        List<String[]> actualResults = ((JobDetails) currentPage).getResults();
        for (int i = 0; i < actualNumberOfResults; i++) {
            assertArrayEquals(actualResults.get(i), results.get(i).toArray());
        }
    }

    /**
     * @param jobs
     *            Set the number of jobs listed
     */
    @Then("^there should be (\\d+) jobs listed$")
    public final void thereShouldBeJobsListed(final int jobs) {
        assertEquals(jobs,
                ((org.emonocot.portal.driver.source.Show) currentPage)
                        .getJobsListed().intValue());
    }

    /**
     * @param uri
     *            Set the expected source uri
     */
    @Then("^the source uri should be \"([^\"]*)\"$")
    public final void theSourceUriShouldBe(final String uri) {
        assertEquals(uri,
                ((org.emonocot.portal.driver.source.Show) currentPage)
                        .getSourceUri());
    }

    /**
     * @param logo
     *            Set the expected source logo
     */
    @Then("^the source logo should be \"([^\"]*)\"$")
    public final void theSourceLogoShouldBe(final String logo) {
        assertEquals(logo,
                ((org.emonocot.portal.driver.source.Show) currentPage)
                        .getSourceLogo());
    }

    /**
     *
     */
    @Then("^the view icons should be displayed$")
    public final void theViewIconsShouldBeDisplayed() {
        assertTrue(((Search) currentPage).viewIconDisplay());
    }

    /**
    *
    */
    @When("I select the login link in the header")
    public final void selectLoginLink() {
        currentPage = currentPage.selectLoginLink();
    }

    /**
     * @param results
     *            Set the number of results
     */
    @Then("^there should be (\\d) result[s]?$")
    public final void thereShouldBeResults(final Integer results) {
        assertEquals(results, ((Search) currentPage).getResultNumber());
    }

    /**
     * @param ancestors
     *            Set the ancestors
     */
    @Then("^there should be (\\d+) ancestor[s]?$")
    public final void thereShouldBeAncestors(final Integer ancestors) {
        assertEquals(ancestors,
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getAncestorsNumber());
    }

    /**
     * @param subordinate
     *            Set the value of subordinate taxa link
     */
    @Then("^the subordinate taxon link should say \"([^\"]*)\"$")
    public final void theSubordinateTaxonLinkShouldSay(final String subordinate) {
        assertEquals(subordinate,
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getSubordinateTaxa());
    }

    /**
     * @param subordinateNumber
     *            Set the number of subordinate taxa
     */
    @Then("^there should be (\\d+) subordinate taxa$")
    public final void thereShouldBeSubordinateTaxa(
            final Integer subordinateNumber) {
        assertEquals(subordinateNumber,
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getChildrenNumber());
    }

    /**
     * @param results
     *            Set the results
     */
    @Then("^the following results should be displayed:$")
    public final void theFollowingResultsShouldBeDisplayed(
            final List<ResultRow> results) {
        int actualNumberOfResults = (int) ((Search) currentPage)
                .getResultNumber();
        assertEquals(results.size(), actualNumberOfResults);
        List<String[]> actualResults = ((Search) currentPage).getResults();
        for (int i = 0; i < actualNumberOfResults; i++) {
            assertArrayEquals(actualResults.get(i), results.get(i).toArray());
        }
    }

    /**
     * @param number
     *            set the thumbnail caption to compare to
     */
    @Then("^the main image caption should be the (\\d+)\\w+ caption$")
    public final void theMainImageCaptionShouldEqualTheCaption(final int number) {
        assertEquals(
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getMainImageProperty(null),
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getThumbnailCaption(number));
    }

    /**
     * @param number
     *            set the thumbnail image to compare to
     */
    @Then("^the main image should be the (\\d+)\\w+ image$")
    public final void theMainImageShouldBeTheImage(final int number) {
        assertEquals(
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getMainImage(),
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getThumbnailImage(number));
    }

    /**
     * @param citeKey
     *            Set the citation key
     * @param bibliographyEntry
     *            Set the expected bibliography entry
     */
    @Then("^the bibliography entry \"([^\"]*)\" should be \"([^\"]*)\"$")
    public final void theBibliographyEntryShouldBe(final String citeKey,
            final String bibliographyEntry) {
        assertEquals(
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getBibliographyEntry(citeKey),
                bibliographyEntry);
    }

    /**
     * @param topic
     *            Set the topic
     * @param citations
     *            Set the expected citations
     */
    @Then("^the citation for the \"([^\"]*)\" topic should be \"([^\"]*)\"$")
    public final void theCitationForTheTopicShouldBe(final String topic,
            final String citations) {
        assertEquals(
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getCitations(topic),
                citations);
    }

    /**
     * @param title
     *            Set the name
     */
    @Then("^the source title should be \"([^\"]*)\"$")
    public final void theSourceTitleShouldBe(final String title) {
        assertEquals(title,
                ((org.emonocot.portal.driver.source.Show) currentPage)
                        .getTitle());
    }

    /**
     * @param message
     *            Set the message
     */
    @Then("^the search results page should display \"([^\"]*)\"$")
    public final void theSearchResultsPageShouldDisplay(final String message) {
        assertEquals(message, ((Search) currentPage).getMessage());
    }

    /**
     *
     */
    @Then("^I select the identify link in the navigation bar$")
    public final void iSelectTheIdentifyLinkInTheNavigationBar() {
        currentPage = currentPage.selectIdentifyLink();
    }

    /**
     *
     */
    @Then("^I should be on the identify page$")
    public final void iShouldBeOnTheIdentifyPage() {
        assertEquals(currentPage.getClass(), Identify.class);
    }

    /**
    *
    */
    @Then("^I select the about link in the footer$")
    public final void iSelectTheAboutLinkInTheFooter() {
        currentPage = currentPage.selectAboutLink();
    }

    @Then("^I should be on the about page$")
    public void iShouldBeOnTheAboutPage() {
        assertEquals(currentPage.getClass(), About.class);
    }

    /**
    *
    */
    @When("^I select the contact link in the footer$")
    public final void iSelectTheContactLinkInTheFooter() {
        currentPage = currentPage.selectContactLink();
    }

    @Then("^I should be on the contact page$")
    public void iShouldBeOnTheContactPage() {
        assertEquals(currentPage.getClass(), Contact.class);
    }

    /**
     * @param options
     *            Set the options
     */
    @Then("^the Type facet should have the following options:$")
    public final void thereShouldBeOptionsForClassFacet(final List<Row> options) {
        assertFacets("CLASS", options);
    }

    /**
     * @param options
     *            Set the options
     */
    @Then("^the Family facet should have the following options:$")
    public final void thereShouldBeOptionsForFamilyFacet(final List<Row> options) {
        assertFacets("FAMILY", options);
    }

    /**
     * @param options
     *            Set the options
     */
    @Then("^the Rank facet should have the following options:$")
    public final void thereShouldBeOptionsForRankFacet(final List<Row> options) {
        assertFacets("RANK", options);
    }

    /**
     * @param options
     *            Set the options
     */
    @Then("^the Status facet should have the following options:$")
    public final void thereShouldBeOptionsForStatusFacet(final List<Row> options) {
        assertFacets("STATUS", options);
    }

    /**
     * @param options
     *            Set the options
     */
    @Then("^the autocomplete box should display the following options:$")
    public final void theAutocompleteBoxShouldDisplayTheFollowingOptions(
            final List<Row> options) {
        String[] expected = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            expected[i] = options.get(i).option;
        }
        String[] actual = ((Search) currentPage).getAutocompleteOptions();
        assertArrayEquals(expected, actual);
    }

    /**
     * @param facetName
     *            Set the facet name
     * @param options
     *            Set the options
     */
    public final void assertFacets(final String facetName,
            final List<Row> options) {
        String[] expected = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            expected[i] = options.get(i).option;
        }
        String[] actual = ((Search) currentPage).getFacets(facetName);
        assertArrayEquals(expected, actual);

    }

    /**
     *
     */
    @Then("^the images should be displayed in a grid.$")
    public final void theImagesShouldBeDisplayedInAGrid() {
        assertTrue(((Search) currentPage).resultsAreDisplayedInGrid());
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
     * @param identifier
     *            Set the page
     */
    @When("^I navigate to image page \"([^\"]*)\"$")
    public final void navigateToImagePage(final String identifier) {
        currentPage = portal.getImagePage(identifier);
    }

    /**
     * @param identifier
     *            Set the page
     */
    @When("^I navigate to source page \"([^\"]*)\"$")
    public final void navigateToSourcePage(final String identifier) {
        try {
            currentPage = portal.getSourcePage(identifier);
        } catch (RequiresLoginException rle) {
            currentPage = rle.getLoginPage();
        }
    }

    /**
     * @param job
     *            Set the job identifier
     * @param source
     *            Set the source identifier
     */
    @When("^I navigate to the job page \"([^\"]*)\" for source \"([^\"]*)\"$")
    public final void navigateToSourceJobPage(final String job,
            final String source) {
        try {
            currentPage = portal.getSourceJobPage(source, job);
        } catch (RequiresLoginException rle) {
            currentPage = rle.getLoginPage();
        }
    }

    /**
     * @param thumbnail
     *            Set the thumbnail
     */
    @When("I select the (\\d+)\\w+ thumbnail")
    public final void selectTheThumbnail(final Integer thumbnail) {
        ((org.emonocot.portal.driver.taxon.Show) currentPage)
                .selectThumbnail(thumbnail);
    }

    /**
     * @param title
     *            Set the title
     */
    @Then("^the page title should be \"([^\"]*)\"$")
    public final void thePageTitleShouldBeAcorus(final String title) {
        assertEquals(title,
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getTaxonName());
    }

    /**
     *
     */
    @Then("the image page should be displayed")
    public final void theImagePageShouldBeDisplayed() {
        assertEquals(currentPage.getClass(),
                org.emonocot.portal.driver.image.Show.class);
    }

    /**
     * @param attribute
     *            Set the title attribute to check
     * @param value
     *            Set the expected value
     */
    @Then("^the page title \"([^\"]*)\" should be \"([^\"]*)\"$")
    public final void theTitleAttributeShouldBeTaxonName(
            final String attribute, final String value) {
        assertEquals(value,
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getTaxonNameStyle(attribute));
    }

    /**
     * @param paragraph
     *            Set the paragraph
     * @param heading
     *            Set the heading
     */
    @Then("^there should be a paragraph \"([^\"]*)\" with the heading \"([^\"]*)\"$")
    public final void thereShouldBeAParagraphWithTheHeading(
            final String paragraph, final String heading) {
        assertEquals(paragraph,
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getParagraph(heading));
    }

    /**
     * /**
     * 
     * @param heading
     *            Set the heading
     */
    @Then("^there should not be a paragraph with the heading \"([^\"]*)\"$")
    public final void thereShouldNotBeAParagraphWithTheHeading(
            final String heading) {
        assertFalse(((org.emonocot.portal.driver.taxon.Show) currentPage)
                .doesParagraphExist(heading));
    }

    /**
     * @param protologue
     *            Set the protologue
     */
    @Then("^the protologue should be \"([^\"]*)\"$")
    public final void theProtologueShouldBe(final String protologue) {
        assertEquals(
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getProtologue(),
                protologue);
    }

    /**
     * @param protologLink
     *            Set the protologue link
     */
    @Then("^the protolog link should be \"([^\"]*)\"$")
    public final void theProtologueLinkShouldBe(final String protologLink) {
        assertEquals(
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getProtologueLink(),
                protologLink);
    }

    /**
     * @param property
     *            Set the property to find
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
     * @param image
     *            Set the image
     */
    @Then("^the main image should be \"([^\"]*)\"$")
    public final void theMainImageShouldBe(final String image) {
        assertEquals(image, ((IllustratedPage) currentPage).getMainImage());
    }

    /**
     * @param thumbnails
     *            Set the thumbnails
     */
    @Then("^there should be (\\d+) thumbnail[s]?$")
    public final void thereShouldBeThumbnails(final int thumbnails) {
        assertEquals(thumbnails,
                ((IllustratedPage) currentPage).getThumbnails());
    }

    /**
     * @param url
     *            Set the url
     */
    @Then("^the distribution map should be \"([^\"]*)\"$")
    public final void theDistributionMapShouldBe(final String url) {
        assertEquals(url,
                ((org.emonocot.portal.driver.taxon.Show) currentPage)
                        .getDistributionMap());
    }

    /**
     *
     */
    @When("^I select the registration link$")
    public final void selectTheRegistrationLink() {
        currentPage = ((Index) currentPage).selectRegistrationLink();
    }

    /**
    *
    */
    @When("^I select the main image$")
    public final void selectTheMainImage() {
        currentPage = ((org.emonocot.portal.driver.taxon.Show) currentPage)
                .selectMainImage();
    }

    /**
     * @param data
     *            Set the registration data
     */
    @When("^I enter the following data into the registration form:$")
    public final void enterTheFollowingDataIntoTheRegistrationForm(
            final List<RegistrationRow> data) {
        ((Register) currentPage).setUsername(data.get(0).username);
        ((Register) currentPage).setRepeatUsername(data.get(0).repeatUsername);
        ((Register) currentPage).setPassword(data.get(0).password);
        ((Register) currentPage).setRepeatPassword(data.get(0).repeatPassword);
    }

    /**
     * @param data
     *            Set the login data
     */
    @When("^I enter the following data into the login form:$")
    public final void enterTheFollowingDataIntoTheFormForm(
            final List<LoginRow> data) {
        ((Login) currentPage).setUsername(data.get(0).username);
        ((Login) currentPage).setPassword(data.get(0).password);
    }

    @When("^I enter the following data in the job form:$")
    public void iEnterTheFollowingDataInTheJobForm(final List<JobRow> rows) {
        ((org.emonocot.portal.driver.admin.source.job.Create) currentPage)
                .setObjectIdentifier(rows.get(0).identifier, "identifier");
        ((org.emonocot.portal.driver.admin.source.job.Create) currentPage)
                .setFormField("uri", rows.get(0).uri);
        ((org.emonocot.portal.driver.admin.source.job.Create) currentPage)
                .setFormSelection("jobType", rows.get(0).jobType);
        ((org.emonocot.portal.driver.admin.source.job.Create) currentPage)
                .setFormField("family", rows.get(0).family);
    }

    @When("^I submit the create job form$")
    public void iSubmitTheCreateJobForm() {
        currentPage = ((org.emonocot.portal.driver.admin.source.job.Create) currentPage)
                .submit();
    }

    /**
     *
     */
    @When("^I submit the registration form$")
    public final void submitTheRegistrationForm() {
        currentPage = ((Register) currentPage).submit();
    }

    /**
    *
    */
    @When("^I submit the login form$")
    public final void submitTheLoginForm() {
        currentPage = ((Login) currentPage).submit();
    }

    /**
     *
     */
    @Then("^my profile page should be displayed$")
    public final void myProfilePageShouldBeDisplayed() {
        assertEquals(Profile.class, currentPage.getClass());
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
        assertEquals(Login.class, currentPage.getClass());
    }

    @Then("^there should be (\\d+) job$")
    public void thereShouldBeJobs(Integer numberOfJobs) {
        assertEquals(numberOfJobs,
                ((org.emonocot.portal.driver.admin.source.Show) currentPage)
                        .getNumberOfJobs());
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

    @When("^I select the \"([^\"]+)\" link in the navbar$")
    @And("^I select the \"([^\"]+)\" link in the page$")
    public final void iSelectLink(String text) {
        currentPage = portal.selectLink(text);
    }

    @When("^I am on the classify page")
    public final void openTheClassifyPage(String text) {
        currentPage = portal.getClassifyPage();
    }

    @Then("^I should be on the classify page")
    public final void iShouldBeOnTheClassifyPage() {
        assertEquals("The URI should be \"classify\".", "classify", currentPage
                .getUri().startsWith("/") ? currentPage.getUri().substring(1)
                : currentPage.getUri());
    }

    @And("^there should be a \"([^\"]+)\" link on the page$")
    public final void thereShouldBeALink(String text) {
        // assertNotNull(
    }

    /**
     * @author ben
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
     * @author ben
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
         * @return the row as an array
         */
        public final String[] toArray() {
            return new String[] {page, text};
        }
    }
}
