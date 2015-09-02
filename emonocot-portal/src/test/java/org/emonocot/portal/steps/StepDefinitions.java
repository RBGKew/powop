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
package org.emonocot.portal.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.lang.System;


import org.apache.commons.lang.ObjectUtils;
import org.emonocot.portal.driver.About;
import org.emonocot.portal.driver.Classification;
import org.emonocot.portal.driver.Identify;
import org.emonocot.portal.driver.IllustratedPage;
import org.emonocot.portal.driver.Login;
import org.emonocot.portal.driver.PageObject;
import org.emonocot.portal.driver.Portal;
import org.emonocot.portal.driver.Profile;
import org.emonocot.portal.driver.Register;
import org.emonocot.portal.driver.RequiresLoginException;
import org.emonocot.portal.driver.Search;
import org.emonocot.portal.driver.TermsOfUse;
import org.emonocot.portal.driver.organisation.ResourceOutput;
import org.emonocot.portal.rows.AccessControlRow;
import org.emonocot.portal.rows.GroupRow;
import org.emonocot.portal.rows.LoginRow;
import org.emonocot.portal.rows.OrganisationRow;
import org.emonocot.portal.rows.RegistrationRow;
import org.emonocot.portal.rows.ResourceRow;
import org.emonocot.portal.rows.SummaryRow;
import org.emonocot.portal.rows.UserRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;

import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;




/**
 * @author ben
 */
public class StepDefinitions {

	private static Logger logger = LoggerFactory.getLogger(StepDefinitions.class);

	/**
	 *
	 */
	private static Integer MILLISECONDS_IN_A_SECOND = 1000;

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
	public void tearDown() {
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
	public void iExpand(String nodeName) {
		((Classification) currentPage).expandNode(nodeName);
	}

	/**
	 * @param query
	 *            Set the search query
	 */
	@When("^I search for \"([^\"]*)\"$")
	public void whenISearchFor(String query) {
		currentPage = portal.search(query);
	}

	/**
	 *
	 */
	@When("^I am on the source list page$")
	public void iAmOnTheSourceListPage() {
		currentPage = portal.getSourceListPage();
	}

	/**
	 *
	 */
	@When("^I am on the classification page$")
	public void iAmOnTheClassificationPage() {
		currentPage = portal.getClassificationPage();
	}

	/**
	 *
	 */
	@When("^I am on the group page$")
	public void iAmOnTheGroupPage() {
		currentPage = portal.getListGroupsPage();
	}

	/**
	 *
	 */
	@When("^I am on the search page$")
	public void iAmOnTheSearchPage() {
		currentPage = portal.search("");
	}

	/**
	 *
	 */
	@When("^I am on the spatial search page$")
	public void iAmOnTheSpatialSearchPage() {
		currentPage = portal.spatialSearch(null, null, null, null);
	}

	/**
	 *
	 * @param x1 the first latitude
	 * @param y1 the first longitude
	 * @param x2 the second latitude
	 * @param y2 the second longitude
	 */
	@When("^I search within ([+-]?\\d+\\.\\d+) ([+-]?\\d+\\.\\d+), ([+-]?\\d+\\.\\d+) ([+-]?\\d+\\.\\d+)$")
	public void iSearchWithin(Float x1, Float y1, Float x2, Float y2) {
		currentPage = portal.spatialSearch(x1, y1, x2, y2);
	}

	/**
	 * @param text
	 *            Set the link text
	 */
	@When("^I select \"(Create a new resource)\"$")
	public void iSelectCreateANewJob(String text) {
		currentPage = currentPage.selectLink(text,
				org.emonocot.portal.driver.resource.Create.class);
	}

	@When("^I select the (\\d+)[\\w]+ phylogeny link$")
	public void selectThePhylogenyLink(int number) throws Throwable {
		currentPage = ((org.emonocot.portal.driver.taxon.Show)currentPage).selectPhylogeny(number);
	}

	/**
	 * @param text
	 *            Set the link text
	 */
	@When("^I select \"(Create a new group)\"$")
	public void iSelectCreateANewGroup(String text) {
		currentPage = currentPage.selectLink(text,
				org.emonocot.portal.driver.group.Create.class);
	}

	/**
	 * @param groupRows
	 *            set the group rows
	 */
	@When("^I enter the following data into the group form:$")
	public void iEnterTheFollowingDataInTheGroupForm(
			List<GroupRow> groupRows) {
		((org.emonocot.portal.driver.group.Create) currentPage)
		.setGroupName(groupRows.get(0).identifier);
	}

	/**
	 * @param sourceRows
	 *            set the source rows
	 */
	@When("^I enter the following data into the create source form:$")
	public void iEnterTheFollowingDataIntoTheCreateSourceForm(
			List<OrganisationRow> sourceRows) {

		((org.emonocot.portal.driver.organisation.Create) currentPage)
		.setObjectIdentifier(sourceRows.get(0).identifier, "identifier");
		((org.emonocot.portal.driver.organisation.Create) currentPage)
		.setFormField("uri", sourceRows.get(0).uri);
		((org.emonocot.portal.driver.organisation.Create) currentPage)
		.setFormField("logoUrl", sourceRows.get(0).logoUrl);
		((org.emonocot.portal.driver.organisation.Create) currentPage)
		.setFormField("title", sourceRows.get(0).title);
	}

	/**
	 * @param sourceRows
	 *            set the source rows
	 */
	@When("^I enter the following data into the update source form:$")
	public void iEnterTheFollowingDataIntoTheUpdateSourceForm(
			List<OrganisationRow> sourceRows) {
		((org.emonocot.portal.driver.organisation.Update) currentPage).setFormField(
				"uri", sourceRows.get(0).uri);
		((org.emonocot.portal.driver.organisation.Update) currentPage).setFormField(
				"logoUrl", sourceRows.get(0).logoUrl);
		((org.emonocot.portal.driver.organisation.Update) currentPage).setFormField(
				"title", sourceRows.get(0).title);
	}

	/**
	 * @param aceRows
	 *            Set the access control rows
	 */
	@When("^I enter the following data into the access controls form:$")
	public void iEnterTheFollowingDataInTheAccessControlForm(
			List<AccessControlRow> aceRows) {
		((org.emonocot.portal.driver.group.Update) currentPage)
		.setSecureObject(aceRows.get(0).object);
	}

	/**
	 * @param userRows
	 *            Set the user rows
	 */
	@When("^I enter the following data into the members form:$")
	public void iEnterTheFollowingDataIntoTheMembersForm(
			List<UserRow> userRows) {
		((org.emonocot.portal.driver.group.Update) currentPage)
		.setMember(userRows.get(0).identifier);
	}

	/**
	 * @param linkText
	 *            Set the link text
	 */
	@When("^I select \"(Create a new organisation)\"$")
	public void iSelectCreateANewSource(String linkText) {
		currentPage = currentPage.selectLink(linkText,
				org.emonocot.portal.driver.organisation.Create.class);
	}

	/**
	 * @param linkText
	 *            Set the link text
	 */
	@When("^I select \"(Edit this group)\"$")
	public void iSelectEditThisGroup(String linkText) {
		currentPage = ((org.emonocot.portal.driver.group.Show) currentPage)
				.selectLink(linkText,
						org.emonocot.portal.driver.group.Update.class);
	}

	/**
	 * @param linkText
	 *            Set the link text
	 */
	@When("^I select \"(Edit this organisation)\"$")
	public void iSelectEditThisSource(String linkText) {
		currentPage = ((org.emonocot.portal.driver.organisation.Show) currentPage)
				.selectLink(linkText,
						org.emonocot.portal.driver.organisation.Update.class);
	}

	/**
	 *
	 */
	@When("^I submit the access controls form$")
	public void iSubmitTheAccessControlsForm() {
		currentPage = ((org.emonocot.portal.driver.group.Update) currentPage)
				.submitAceForm();
	}

	/**
	 *
	 */
	@When("^I submit the group form$")
	public void iSubmitTheGroupForm() {
		currentPage = ((org.emonocot.portal.driver.group.Create) currentPage)
				.submit();
	}

	/**
	 *
	 */
	@When("^I submit the update source form$")
	public void iSubmitTheUpdateSourceForm() {
		currentPage = ((org.emonocot.portal.driver.organisation.Update) currentPage)
				.submit();
	}

	/**
	 *
	 */
	@When("^I submit the create source form$")
	public void iSubmitTheCreateSourceForm() {
		currentPage = ((org.emonocot.portal.driver.organisation.Create) currentPage)
				.submit();
	}

	/**
	 *
	 */
	@When("^I submit the members form$")
	public void iSubmitTheMembersForm() {
		currentPage = ((org.emonocot.portal.driver.group.Update) currentPage)
				.submitMemberForm();
	}

	/**
	 * @param groupName
	 *            Set the groupName
	 */
	@When("^I go to the page for the group \"([^\"]*)\"$")
	public void iGoToThePageForTheGroup(String groupName) {
		currentPage = portal.getGroupPage(groupName);
	}

	/**
	 * @param sort
	 *            Set the sort selection
	 */
	@When("^I sort \"([^\"]+)\"$")
	public void whenISort(String sort) {
		currentPage = ((Search) currentPage).sort(sort);
	}

	/**
	 * @param facetName
	 *            the facet to restrict
	 * @param facetValue
	 *            Set the facet value to select
	 */
	@When("^I restrict the \"([^\"]+)\" by selecting \"([^\"]+)\"$")
	public void iSelect(String facetName, String facetValue) {
		currentPage = ((Search) currentPage).selectFacet(facetName, facetValue);
	}

	/**
	 * @param facetValue
	 *            Set the facet value to select
	 */
	@When("^I restrict the type of object by selecting \"([^\"]+)\"$")
	public void facetOnType(String facetValue) {
		iSelect("base.class_s", facetValue);
	}

	/**
	 * @param query
	 *            Set the query
	 */
	@When("^I type for \"([^\"]*)\" in the search box$")
	public void typeInTheSearchBox(String query) {
		((Search) currentPage).setQuery(query);
	}

	/**
	 * @param wait
	 *            Set the wait time
	 */
	@When("^I wait for (\\d+) second[s]?$")
	public void typeInTheSearchBox(Integer wait) {
		currentPage.waitForAjax(wait * MILLISECONDS_IN_A_SECOND);
	}

	/**
	 * @param view
	 *            Click on the grid icon
	 */
	@When("^I click on the \"([^\"]*)\" icon$")
	public void iClickOnTheGridIcon(String view) {
		currentPage = ((Search) currentPage).view(view);
	}

	/**
	 * @param username
	 *            Set the username
	 * @param password
	 *            Set the password
	 */
	@When("^I am logged in as \"([^\"]*)\" with the password \"([^\"]*)\"$")
	public void iAmLoggedInAsWithPassword(String username,
			String password) {
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
	public void iSelectTheJob(int job) {
		currentPage = ((org.emonocot.portal.driver.organisation.Show) currentPage)
				.selectResource(job);
	}

	/**
	 * @param keyword
	 *            Set the keyword
	 */
	@When("^I click on the keyword \"([^\"]*)\"$")
	public void iClickOnTheKeyword(String keyword) {
		currentPage = ((org.emonocot.portal.driver.image.Show) currentPage)
				.selectKeyword(keyword);
	}

	@When("^I select the next page$")
	public void iSelectTheNextPage() {
		currentPage = ((org.emonocot.portal.driver.Search) currentPage).selectNextPage();
	}

	/**
	 * @param category
	 *            Set the category
	 */
	@When("^I restrict the job \"([^\"]*)\" by selecting \"([^\"]*)\"$")
	public void iSelectTheJobCategory(String facetName, String facetValue) {
		currentPage = ((org.emonocot.portal.driver.organisation.ResourceOutput) currentPage)
				.selectFacet(facetName, facetValue);
	}

	/**
	 * @param results
	 *            Set the expected results
	 */
	@Then("^the following nodes should be displayed:$")
	public void theFollowingNodesShouldBeDisplayed(
			List<ResultRow> results) {
		int actualNumberOfResults = (int) ((Classification) currentPage)
				.getNodeNumber();
		assertEquals(results.size(), actualNumberOfResults);
		List<String[]> actualResults = ((Classification) currentPage)
				.getNodes();
		for (int i = 0; i < actualNumberOfResults; i++) {
			assertArrayEquals(results.get(i).toArray(), actualResults.get(i));
		}
	}

	/**
	 * @param message
	 *            Set the expected info message
	 */
	@Then("^an info message should say \"([^\"]*)\"$")
	public void anInfoMessageShouldSay(String message) {
		assertEquals(message, currentPage.getInfoMessage());
	}

	/**
	 * @param results
	 *            Set the results
	 */
	@Then("^the summary results should be as follows:$")
	public void theSummaryResultsShouldBeAsFollows(
			List<SummaryRow> results) {
		int actualNumberOfResults = (int) ((ResourceOutput) currentPage)
				.getResultNumber();
		assertEquals(results.size(), actualNumberOfResults);
		List<String[]> actualResults = ((ResourceOutput) currentPage).getResults();
		for (int i = 0; i < actualNumberOfResults; i++) {
			assertArrayEquals(actualResults.get(i), results.get(i).toArray());
		}
	}

	/**
	 * @param jobs
	 *            Set the number of jobs listed
	 */
	@Then("^there should be (\\d+) jobs listed$")
	public void thereShouldBeJobsListed(int jobs) {
		assertEquals(jobs,
				((org.emonocot.portal.driver.organisation.Show) currentPage)
				.getResourcesListed().intValue());
	}

	/**
	 * @param uri
	 *            Set the expected source uri
	 */
	@Then("^the source uri should be \"([^\"]*)\"$")
	public void theSourceUriShouldBe(String uri) {
		assertEquals(uri,
				((org.emonocot.portal.driver.organisation.Show) currentPage)
				.getOrganisationUri());
	}

	/**
	 * @param logo
	 *            Set the expected source logo
	 */
	@Then("^the source logo should be \"([^\"]*)\"$")
	public void theSourceLogoShouldBe(String logo) {
		assertEquals(logo,
				((org.emonocot.portal.driver.organisation.Show) currentPage)
				.getOrganisationLogo());
	}

	/**
	 *
	 */
	@Then("^the view icons should be displayed$")
	public void theViewIconsShouldBeDisplayed() {
		assertTrue(((Search) currentPage).viewIconDisplay());
	}

	@When("I select the login link in the header")
	public void selectLoginLink() {
		currentPage = currentPage.selectLoginLink();
	}

	@When("I select the terms of use link in the footer")
	public void selectTermsOfUseLink() {
		currentPage = currentPage.selectTermsOfUseLink();
	}

	/**
	 * @param results
	 *            Set the number of results
	 */
	@Then("^there should be (\\d+) result[s]?$")
	public void thereShouldBeResults(Integer results) {
		assertEquals(results, ((Search) currentPage).getResultNumber());
	}


	/**
	 * @param entryRank
	 *            Set the entryRank of the result
	 */
	@And("^the result rank for Acorus calamus should be \"([^\"]*)\"$")
	public void theResultRankForAcorusCalamusShouldBe(String entryRank) {
		assertEquals(entryRank, ((Search) currentPage).getResultEntryRank());
	}


	/**
	 * @param entryRank
	 *            Set the entryStatus of the result
	 */
	@And("^the result status for Acorus calamus should be \"([^\"]*)\"$")
	public void theResultStatusForAcorusCalamusShouldBe(String entryStatus) {
		assertEquals(entryStatus, ((Search) currentPage).getResultEntryStatus());
	}


	@Then("^the pagination should show that results (\\d+) - (\\d+) are displayed$")
	public void thePaginationShouldShowThatResultsAreDisplayed(Integer from, Integer to) {
		String label = from + " - " + to;
		assertEquals(label, ((Search) currentPage).getPaginationLabel());
	}

	/**
	 * @param ancestors
	 *            Set the ancestors
	 */
	@Then("^there should be (\\d+) ancestor[s]?$")
	public void thereShouldBeAncestors(Integer ancestors) {
		assertEquals(ancestors,
				((org.emonocot.portal.driver.taxon.Show) currentPage)
				.getAncestorsNumber());
	}

	/**
	 *
	 * @param taxonStatus Set the taxonomic status
	 */
	@Then("^the taxon status should be '([^\"]*)'$")
	public void theTaxonStatusShouldBe(String taxonStatus) {
		assertEquals(taxonStatus,
				((org.emonocot.portal.driver.taxon.Show) currentPage)
				.getTaxonomicStatus());
	}

	/**
	 * @param subordinate
	 *            Set the value of subordinate taxa link
	 */
	@Then("^the subordinate taxon link should say \"([^\"]*)\"$")
	public void theSubordinateTaxonLinkShouldSay(String subordinate) {
		assertEquals(subordinate,
				((org.emonocot.portal.driver.taxon.Show) currentPage)
				.getSubordinateTaxa());
	}

	/**
	 * @param subordinateNumber
	 *            Set the number of subordinate taxa
	 */
	@Then("^there should be (\\d+) subordinate taxa$")
	public void thereShouldBeSubordinateTaxa(
			Integer subordinateNumber) {
		assertEquals(subordinateNumber,
				((org.emonocot.portal.driver.taxon.Show) currentPage)
				.getChildrenNumber());
	}

	@Then("^there should be (\\d+) associated phylogeny[s]?$")
	public void thereShouldBeAssociatedPhylogenies(Integer number) throws Throwable {
		assertEquals(number,
				((org.emonocot.portal.driver.taxon.Show) currentPage)
				.getPhylogenyNumber());
	}

	/**
	 * @param results
	 *            Set the results
	 */
	@Then("^the following results should be displayed:$")
	public void theFollowingResultsShouldBeDisplayed(
			List<ResultRow> results) {
		int actualNumberOfResults = (int) ((Search) currentPage)
				.getResultNumber();
		assertEquals(results.size(), actualNumberOfResults);
		List<String[]> actualResults = ((Search) currentPage).getResults();
		for (int i = 0; i < actualNumberOfResults; i++) {
			assertArrayEquals(results.get(i).toArray(), actualResults.get(i));
		}
	}

	@Then("^the following results should be displayed in any order:$")
	public void theFollowingResultsShouldBeDisplayedInAnyOrder(
			List<ResultRow> results) {
		List<String[]> actualResults = ((Search) currentPage).getResults();
		List<ResultRow> actualResultRows = new ArrayList<ResultRow>();
		for(String[] s : actualResults) {
			ResultRow r = new ResultRow();
			r.page = s[0];
			r.text = s[1];
			actualResultRows.add(r);
		}

		assertThat(actualResultRows,hasItems(results.toArray(new ResultRow[results.size()])));
	}

	/**
	 * @param number
	 *            set the thumbnail caption to compare to
	 */
	@Then("^the main image caption should be the (\\d+)\\w+ caption$")
	public void theMainImageCaptionShouldEqualTheCaption(int number) {
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
	public void theMainImageShouldBeTheImage(int number) {
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
	public void theBibliographyEntryShouldBe(String citeKey,
			String bibliographyEntry) {
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
	public void theCitationForTheTopicShouldBe(String topic,
			String citations) {
		assertEquals(
				citations, ((org.emonocot.portal.driver.taxon.Show)
						currentPage).getCitations(topic).replaceAll("\\s+",""));
	}

	/**
	 * @param title
	 *            Set the name
	 */
	@Then("^the source title should be \"([^\"]*)\"$")
	public void theSourceTitleShouldBe(String title) {
		assertEquals(title,
				((org.emonocot.portal.driver.organisation.Show) currentPage)
				.getTitle());
	}

	@Then("^the title of the phylogeny should be \"([^\"]*)\"$")
	public void theTitleOfThePhylogenyShouldBe(String title) throws Throwable {
		assertEquals(title,
				((org.emonocot.portal.driver.phylo.Show) currentPage)
				.getTitle());
	}

	/**
	 * @param message
	 *            Set the message
	 */
	@Then("^the search results page should display \"([^\"]*)\"$")
	public void theSearchResultsPageShouldDisplay(String message) {
		assertEquals(message, ((Search) currentPage).getMessage());
	}

	/**
	 *
	 */
	@Then("^I select the identify link on the home page$")
	public void iSelectTheIdentifyLinkOnTheHomePage() {
		currentPage = currentPage.selectIdentifyLink();
	}

	/**
	 *
	 */
	@Then("^I select the classify link on the home page$")
	public void iSelectTheClassifyLinkOnTheHomePage() {
		currentPage = currentPage.selectClassifyLink();
	}

	@Then("^I should be on the emonocot features page$")
	public void iShouldBeOnTheEmonocotFeaturePage() {
		assertEquals(currentPage.getClass(), Identify.class);
	}

	@Then("^I should be on the eMonocot Terms of Use page$")
	public void iShouldBeOnTheTermsOfUsePage() {
		assertEquals(currentPage.getClass(), TermsOfUse.class);
	}


	@Then("^I select the about link in the footer$")
	public void iSelectTheAboutLinkInTheFooter() {
		currentPage = currentPage.selectAboutLink();
	}

	@Then("^I should be on the about page$")
	public void iShouldBeOnTheAboutPage() {
		assertEquals(currentPage.getClass(), About.class);
	}


	/**
	 * @param options
	 *            Set the options
	 */
	@Then("^the Type facet should have the following options:$")
	public void thereShouldBeOptionsForClassFacet(List<Row> options) {
		assertFacets("base.class_s", options);
	}

	/**
	 * @param options
	 *            Set the options
	 */
	@Then("^the Family facet should have the following options:$")
	public void thereShouldBeOptionsForFamilyFacet(List<Row> options) {
		assertFacets("taxon.family_ss", options);
	}

	/**
	 * @param options
	 *            Set the options
	 */
	@Then("^the IUCN status facet should have the following options:$")
	public void thereShouldBeOptionsForIUCNFacet(List<Row> options) {
		assertFacets("taxon.measurement_or_fact_threatStatus_txt", options);
	}

	/**
	 * @param options
	 *            Set the options
	 */
	@Then("^the Rank facet should have the following options:$")
	public void thereShouldBeOptionsForRankFacet(List<Row> options) {
		assertFacets("taxon.taxon_rank_s", options);
	}

	/**
	 * @param options
	 *            Set the options
	 */
	@Then("^the Status facet should have the following options:$")
	public void thereShouldBeOptionsForStatusFacet(List<Row> options) {
		assertFacets("taxon.taxonomic_status_s", options);
	}

	/**
	 * @param options
	 *            Set the options
	 */
	@Then("^the autocomplete box should display the following options:$")
	public void theAutocompleteBoxShouldDisplayTheFollowingOptions(
			List<Row> options) {
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
	public void assertFacets(String facetName,
			List<Row> options) {
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
	public void theImagesShouldBeDisplayedInAGrid() {
		assertTrue(((Search) currentPage).resultsAreDisplayedInGrid());
	}

	/**
	 * @param identifier
	 *            Set the page
	 */
	@When("^I navigate to taxon page \"([^\"]*)\"$")
	public void iNavigateToThePage(String identifier) {
		currentPage = portal.getTaxonPage(identifier);
	}

	/**
	 * @param identifier
	 *            Set the page
	 */
	@When("^I navigate to image page \"([^\"]*)\"$")
	public void navigateToImagePage(String identifier) {
		currentPage = portal.getImagePage(identifier);
	}

	/**
	 * @param identifier
	 *            Set the page
	 */
	@When("^I navigate to source page \"([^\"]*)\"$")
	public void navigateToSourcePage(String identifier) {
		try {
			currentPage = portal.getSourcePage(identifier);
		} catch (RequiresLoginException rle) {
			currentPage = rle.getLoginPage();
		}
	}

	@Then("^the contact link in the footer should be \"([^\"]*)\"$")
	public void theContactLinkInTheFooterShouldBe(String contactLink) {
		contactLink = currentPage.getContactLink();
	}

	@Then("^I should see logos in this arrangement:$")
	public void I_should_see_logos_in_this_arrangement(List<List<String>> requiredArrangement) throws Throwable {

		List<List<String>> actual = currentPage.getFooterIconsArrangement();
		assertEquals(requiredArrangement.size(), actual.size());

		for (int i = 0; i < requiredArrangement.size(); i++) {
			List<String> row = requiredArrangement.get(i);

			// Ignore empty strings, which Cucumber includes.
			int count = 0;
			for (int j = 0; j < row.size(); j++) {
				if (!Strings.isNullOrEmpty(row.get(j))) count++;
			}

			assertEquals(count, actual.get(i).size());
			for (int j = 0; j < row.size(); j++) {
				if (!Strings.isNullOrEmpty(requiredArrangement.get(i).get(j))) {
					assertEquals(requiredArrangement.get(i).get(j), actual.get(i).get(j));
				}
			}
		}
	}

	/**
	 * @param job
	 *            Set the job identifier
	 * @param source
	 *            Set the source identifier
	 */
	@When("^I navigate to the update page for source \"([^\"]*)\"$")
	public void navigateToSourceUpdatePage(String source) {
		try {
			currentPage = portal.getUpdateSourcePage(source);
		} catch (RequiresLoginException rle) {
			currentPage = rle.getLoginPage();
		}
	}

	/**
	 * @param thumbnail
	 *            Set the thumbnail
	 */
	@When("I select the (\\d+)\\w+ thumbnail")
	public void selectTheThumbnail(Integer thumbnail) {
		((org.emonocot.portal.driver.taxon.Show) currentPage)
		.selectThumbnail(thumbnail);
	}

	/**
	 * @param title
	 *            Set the title
	 */
	@Then("^the page title should be \"([^\"]*)\"$")
	public void thePageTitleShouldBeAcorus(String title) {
		assertEquals(title,
				((org.emonocot.portal.driver.taxon.Show) currentPage)
				.getTaxonName());
	}

	/**
	 *
	 */
	@Then("the image page should be displayed")
	public void theImagePageShouldBeDisplayed() {
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
	public void theTitleAttributeShouldBeTaxonName(
			String attribute, String value) {
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
	public void thereShouldBeAParagraphWithTheHeading(
			String paragraph, String heading) {
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
	public void thereShouldNotBeAParagraphWithTheHeading(
			String heading) {
		assertFalse(((org.emonocot.portal.driver.taxon.Show) currentPage)
				.doesParagraphExist(heading));
	}

	/**
	 *
	 *
	 * @param elementId The element of the id you are checking for
	 */
	@Then("^there should not be a \"([^\"]*)\" visible$")
	public void thereShouldNotBeASectionWithTheHeading(String elementId) {
		assertFalse((currentPage)
				.isElementVisible(elementId));
	}

	/**
	 *
	 *
	 * @param elementId The element of the id you are checking for
	 */
	@Then("^there should be a \"([^\"]*)\" visible$")
	public void thereShouldBeASectionWithTheHeading(String elementId) {
		assertTrue((currentPage)
				.isElementVisible(elementId));
	}

	/**
	 * @param protologue
	 *            Set the protologue
	 */
	@Then("^the protologue should be \"([^\"]*)\"$")
	public void theProtologueShouldBe(String protologue) {
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
	public void theProtologueLinkShouldBe(String protologLink) {
		assertEquals(
				((org.emonocot.portal.driver.taxon.Show) currentPage)
				.getProtologueLink(),
				protologLink);
	}

	/**
	 * @param provenance
	 *            Set the provenance
	 */
	@And("^the provenance entry \"([^\"]*)\" should be \"([^\"]*)\"$")
	public void theProvenanceEntryShouldBe(String provenanceKey, String provenanceEntry) {
		assertEquals(
				((org.emonocot.portal.driver.taxon.Show) currentPage)
				.getProvenanceEntry(provenanceKey),
				provenanceEntry);
	}


	/**
	 * @param property
	 *            Set the property to find
	 * @param value
	 *            Set the expected value
	 */
	@Then("^the main image ([^\"]*) should be \"([^\"]*)\"$")
	public void theMainImagePropertyShouldBe(String property,
			String value) {
		assertEquals(value,
				((IllustratedPage) currentPage).getMainImageProperty(property));
	}

	/**
	 * @param image
	 *            Set the image
	 */
	@Then("^the main image should be \"([^\"]*)\"$")
	public void theMainImageShouldBe(String image) {
		assertEquals(image, ((IllustratedPage) currentPage).getMainImage());
	}

	/**
	 * @param thumbnails
	 *            Set the thumbnails
	 */
	@Then("^there should be (\\d+) thumbnail[s]?$")
	public void thereShouldBeThumbnails(int thumbnails) {
		assertEquals(thumbnails,
				((IllustratedPage) currentPage).getThumbnails());
	}

	/**
	 * @param url
	 *            Set the url
	 */
	@Then("^the distribution map should be \"([^\"]*)\"$")
	public void theDistributionMapShouldBe(String url) {
		assertEquals(url,
				((org.emonocot.portal.driver.taxon.Show) currentPage)
				.getDistributionMap());
	}

	/**
	 *
	 */
	@When("^I select \"Create a new account\"$")
	public void selectTheRegisterLink() {
		currentPage = ((Login) currentPage).selectRegistrationLink();
	}

	/**
	 *
	 */
	@When("^I select the main image$")
	public void selectTheMainImage() {
		currentPage = ((org.emonocot.portal.driver.taxon.Show) currentPage)
				.selectMainImage();
	}

	/**
	 * @param data
	 *            Set the registration data
	 */
	@When("^I enter the following data into the registration form:$")
	public void enterTheFollowingDataIntoTheRegistrationForm(
			List<RegistrationRow> data) {
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
	public void enterTheFollowingDataIntoTheFormForm(
			List<LoginRow> data) {
		((Login) currentPage).setUsername(data.get(0).username);
		((Login) currentPage).setPassword(data.get(0).password);
	}

	@When("^I enter the following data in the job form:$")
	public void iEnterTheFollowingDataInTheJobForm(List<ResourceRow> rows) {
		((org.emonocot.portal.driver.resource.Create) currentPage)
		.setFormField("title",rows.get(0).title);
		((org.emonocot.portal.driver.resource.Create) currentPage)
		.setFormField("uri", rows.get(0).uri);
		((org.emonocot.portal.driver.resource.Create) currentPage)
		.setFormSelection("resourceType", rows.get(0).jobType);
	}

	@When("^I submit the create job form$")
	public void iSubmitTheCreateJobForm() {
		((org.emonocot.portal.driver.resource.Create) currentPage).setObjectIdentifier();
		currentPage = ((org.emonocot.portal.driver.resource.Create) currentPage)
				.submit();
	}

	/**
	 *
	 */
	@When("^I submit the registration form$")
	public void submitTheRegistrationForm() {
		currentPage = ((Register) currentPage).submit();
	}

	/**
	 *
	 */
	@When("^I submit the login form$")
	public void submitTheLoginForm() {
		currentPage = ((Login) currentPage).submit();
	}

	/**
	 *
	 */
	@Then("^my profile page should be displayed$")
	public void myProfilePageShouldBeDisplayed() {
		assertEquals(Profile.class, currentPage.getClass());
	}

	/**
	 *
	 */
	@Then("^I should be logged in to the portal$")
	public void iShouldBeLoggedInToThePortal() {
		assertTrue(currentPage.loggedIn());
	}

	/**
	 *
	 */
	@Then("^the login page should be displayed$")
	public void theLoginPageShouldBeDisplayed() {
		assertEquals(Login.class, currentPage.getClass());
	}

	/**
	 *
	 */
	@When("^I am not authenticated$")
	public void amNotAuthenticated() {
		portal.disableAuthentication();
	}

	/**
	 *
	 */
	@When("^I am on the portal home page$")
	public void openThePortalHomePage() {
		currentPage = portal.getHomePage();
	}

	/**
	 *
	 */
	@When("^I am on the portal features page$")
	public void IAmOnThePortalFeaturesPage() {
		currentPage = portal.getFeaturePage();
	}

	/**
	 *
	 * @param text Set the text
	 */
	@When("^I select the \"([^\"]+)\" link in the navbar$")
	@And("^I select the \"([^\"]+)\" link in the page$")
	public void iSelectLink(String text) {
		currentPage = portal.selectLink(text);
	}


	/*@When("^I select the \"([^\"]+)\" link in the page$")
    public void iSelectPageLink(String text) {
        currentPage = portal.selectLink(text);
    }*/

	/**
	 *
	 */
	@When("^I am on the classify page$")
	public void openTheClassifyPage() {
		currentPage = portal.getClassifyPage();
	}

	/**
	 *
	 * @param relativePath
	 */
	@Then("^I should be on the \"([^\"]+)\" page$")
	public void iShouldBeOnThePageURL(String relativePath) {
		assertEquals("The URI should be \"" + relativePath + "\".",
				relativePath, currentPage.getUri());
	}

	@And("^there should be a \"([^\"]+)\" link on the page$")
	public void thereShouldBeALink(String text) {
		assertTrue("There should have been a link with " + text,
				currentPage.isLinkPresent(text));
	}

	/**
	 * @param distribution
	 *            Set the textual distribution
	 */
	@Then("^the distribution should list \"([^\"]*)\"$")
	public void theDistributionShouldList(String distribution) {
		assertEquals(((org.emonocot.portal.driver.taxon.Show)currentPage).getTextualDistribution(), distribution);
	}

	/**
	 * @param paragraphNameOrId The name or id attribute of the single paragraph we are looking for
	 * @param expectedPartialText The text that the paragraph should contain
	 */
	@Then("^the paragraph named \"([^\"]*)\" should contain the text \"([^\"]*)\"$")
	public void theParagraphContains(String paragraphNameOrId, String expectedPartialText) {
		String actualText = currentPage.paragraphText(paragraphNameOrId);
		assertNotNull("There should be a paragraph with an id or name " + paragraphNameOrId, actualText);
		assertTrue(actualText + " does not contain " + expectedPartialText,actualText.indexOf(expectedPartialText)>=0);
	}





	/**
	 * @author ben
	 */
	public static class Row implements Serializable {
		/**
		 *
		 */
		private static long serialVersionUID = 1L;
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
		public String[] toArray() {
			return new String[] {page, text};
		}

		@Override
		public boolean equals(Object other) {
			// check for self-comparison
			if (this == other) {
				return true;
			}
			if (other == null) {
				return false;
			}
			// Only works when classes are instantiated
			if ((other.getClass().equals(this.getClass()))) {

				ResultRow row = (ResultRow) other;
				if (this.page == null && row.page == null) {

					if (this.text != null && row.text != null) {
						return ObjectUtils.equals(this.text, row.text);
					} else {
						return false;
					}
				} else {
					return ObjectUtils.equals(this.page, row.page);
				}
			} else {
				return false;
			}
		}
	}
}
