Feature: Display Protologue in Taxon Page
As a user of eMonocot, I would like to access the protologue of 
a taxonomic name in the eMonocot portal whilst viewing 
a taxon page
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=35

Scenario: View protologue in a taxon page
Given there are references with the following properties:
| identifier                    | title        | datePublished | volume | page  | 
| urn:kew.org:wcs:publication:1 | Lorem ipsum  | (2011)        | 1:     | 304pp |
And there are taxa with the following properties:
| identifier                 | name   | protologue                    |
| urn:kew.org:wcs:taxon:2295 | Acorus | urn:kew.org:wcs:publication:1 |
When I navigate to taxon page "urn:kew.org:wcs:taxon:2295"
Then the protologue should be "Lorem ipsum (2011) 1: 304pp"