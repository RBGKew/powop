Feature: Taxon Page
In order to ensure that Taxon pages have italic titles
I want to check the title of a taxon page.
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=36

Scenario: Taxon Title
Given there are taxa with the following properties:
| identifier                 | name   |
| urn:kew.org:wcs:taxon:2295 | Acorus |
When I navigate to taxon page "urn:kew.org:wcs:taxon:2295" 
Then the page title should be "Acorus"
And the title class should be "taxonName"