Feature: Display Textual Data in Taxon Page
As a user of eMonocot, I would like to access textual
content in the eMonocot portal about a specific taxon
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=37

Scenario: View textual data in a taxon page
Given there are taxa with the following properties:
| identifier                 | name   | diagnostic                                                          | habitat                                                                                   |
| urn:kew.org:wcs:taxon:2295 | Acorus | These grasslike evergreen plants are hemicryptophytes or geophytes. | Their natural habitat is at the waterside or close to marshes, often found with reedbeds. |
When I navigate to taxon page "urn:kew.org:wcs:taxon:2295"
Then there should be a paragraph "These grasslike evergreen plants are hemicryptophytes or geophytes." with the heading "Diagnostic"
And there should be a paragraph "Their natural habitat is at the waterside or close to marshes, often found with reedbeds." with the heading "Habitat"
And there should not be a paragraph with the heading "Ecology"