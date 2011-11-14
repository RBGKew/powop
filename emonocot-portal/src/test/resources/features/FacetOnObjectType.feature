Feature: Facet on Object Type
In order to search for different types of objects
I want to search for only taxa and only images
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=72

Scenario: Search for Both Taxa and Images
Given there are source systems with the following properties:
| identifier | uri                 |
| test       | http://example.com  |
And there are taxa with the following properties:
| identifier                 | name   | source |
| urn:kew.org:wcs:taxon:2295 | Acorus | test   |
And there are images with the following properties:
| identifier                                                                            | caption | source |
| urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg | Acorus  | test   |
When I search for "Acorus"
Then there should be 2 results
And the Type facet should have the following options:
| option |
| Images |
| Taxa   |

Scenario: Search for Only Taxa
Given there are source systems with the following properties:
| identifier | uri                 |
| test       | http://example.com  |
And there are taxa with the following properties:
| identifier                  | name                           | family    | rank    | status   | source |
| urn:kew.org:wcs:taxon:2295  | Acorus                         | Acoraceae | GENUS   | accepted | test   |
| urn:kew.org:wcs:taxon:2304  | Acorus calamus                 | Acoraceae | SPECIES | accepted | test   |
| urn:kew.org:wcs:taxon:2305  | Acorus calamus var. americanus | Acoraceae | VARIETY | accepted | test   |
| urn:kew.org:wcs:taxon:2306  | Acorus calamus var. angustatus | Acoraceae | VARIETY | accepted | test   |
| urn:kew.org:wcs:taxon:2296  | Acorus adulterinus             | Acoraceae | SPECIES | synonym  | test   |
| urn:kew.org:wcs:taxon:16240 | Arum maculatum                 | Araceae   | SPECIES | accepted | test   |
And there are images with the following properties:
| identifier                                                                             | caption   | source |
| urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg  | Acorus    | test   |
When I search for "Acorus"
And I restrict the "Type" by selecting "Taxa"
Then there should be 5 results
And the Type facet should have the following options:
| option    |
| All Types |
| Taxa      |
And the Family facet should have the following options:
| option       |
| Acoraceae    |
When I restrict the "Rank" by selecting "Species"
Then there should be 2 results
When I restrict the "Status" by selecting "Accepted Name"
Then there should be 1 result

Scenario: Search for Only Images
Given there are source systems with the following properties:
| identifier | uri                 |
| test       | http://example.com  |
Given there are taxa with the following properties:
| identifier                 | name   | source |
| urn:kew.org:wcs:taxon:2295 | Acorus | test   |
And there are images with the following properties:
| identifier                                                                            | caption | source |
| urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg | Acorus  | test   |
When I search for "Acorus"
And I restrict the "Type" by selecting "Images"
Then there should be 1 result
And the Type facet should have the following options:
| option    |
| All Types |
| Images    |