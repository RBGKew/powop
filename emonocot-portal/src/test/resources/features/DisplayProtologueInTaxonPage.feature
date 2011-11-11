Feature: Display Protologue in Taxon Page
As a user of eMonocot, I would like to access the protologue of 
a taxonomic name in the eMonocot portal whilst viewing 
a taxon page
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=35

Scenario: View protologue in a taxon page
Given there are references with the following properties:
| identifier                    | title    | datePublished | volume | page  | 
| urn:kew.org:wcs:publication:1 | Sp. Pl.  | (1753)        | 1      | 304pp |
And there are taxa with the following properties:
| identifier                 | name   | protologue                    | protologueMicroReference |
| urn:kew.org:wcs:taxon:2295 | Acorus | urn:kew.org:wcs:publication:1 | : 324                    |
When I navigate to taxon page "urn:kew.org:wcs:taxon:2295"
Then the protologue should be "Sp. Pl. 1: 324 (1753)"