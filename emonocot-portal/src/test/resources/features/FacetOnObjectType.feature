Feature: Facet on Object Type
In order to search for different types of objects
I want to search for only taxa and only images
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=72

Scenario: Search for Both Taxa and Images
Given there are taxa with the following properties:
| identifier                 | name   |
| urn:kew.org:wcs:taxon:2295 | Acorus |
And there are images with the following properties:
| identifier                                                                             | caption   |
| urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg  | Acorus    |
When I search for "Acorus"
Then there should be 2 results
And the Type facet should have the following options:
| facet  |
| Images |
| Taxa   |

Scenario: Search for Only Taxa
Given there are taxa with the following properties:
| identifier                  | name                           | family    |
| urn:kew.org:wcs:taxon:2295  | Acorus                         | Acoraceae |
| urn:kew.org:wcs:taxon:2304  | Acorus calamus                 | Acoraceae |
| urn:kew.org:wcs:taxon:2305  | Acorus calamus var. americanus | Acoraceae |
| urn:kew.org:wcs:taxon:2306  | Acorus calamus var. angustatus | Acoraceae |
| urn:kew.org:wcs:taxon:16240 | Arum maculatum                 | Araceae   |
And there are images with the following properties:
| identifier                                                                             | caption   |
| urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg  | Acorus    |
When I search for "Acorus"
And I restrict the "Type" by selecting "Taxa"
Then there should be 4 results
And the Type facet should have the following options:
| facet     |
| All Types |
| Taxa      |
And the Family facet should have the following options:
| facet        |
| acoraceae    |

Scenario: Search for Only Images
Given there are taxa with the following properties:
| identifier                 | name   |
| urn:kew.org:wcs:taxon:2295 | Acorus |
And there are images with the following properties:
| identifier                                                                             | caption   |
| urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg  | Acorus    |
When I search for "Acorus"
And I restrict the "Type" by selecting "Images"
Then there should be 1 result
And the Type facet should have the following options:
| facet     |
| All Types |
| Images    |