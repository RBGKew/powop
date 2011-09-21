Feature: Taxon Page
In order to ensure that Taxon pages have italic titles
I want to check the title of a taxon page.
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=36

Scenario: Taxon Title
Given there is a taxon with id "urn:kew.org:wcs:taxon:2295" and name "Acorus"
When I navigate to the page
Then the page title should be "Acorus"
And the title class should be "taxonName"