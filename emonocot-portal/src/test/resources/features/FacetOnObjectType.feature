Feature: Facet on Object Type
In order to search for different types of objects
I want to search for only taxa and only images
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=72

Scenario: Search for Both Taxa and Images
Given there are taxa with the following properties:
| identifier                 | name   |
| urn:kew.org:wcs:taxon:2295 | Acorus |
And there is an image with id "urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg" and caption "Acorus"
When I search for "Acorus"
Then there should be 2 results
And there should be the following options:
| first  | second |
| Images | Taxa   |

Scenario: Search for Only Taxa
Given there are taxa with the following properties:
| identifier                 | name   |
| urn:kew.org:wcs:taxon:2295 | Acorus |
And there is an image with id "urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg" and caption "Acorus"
When I search for "Acorus"
And I select "Taxa"
Then there should be 1 result
And there should be the following options:
| first     | second |
| All Types | Taxa   |

Scenario: Search for Only Images
Given there are taxa with the following properties:
| identifier                 | name   |
| urn:kew.org:wcs:taxon:2295 | Acorus |
And there is an image with id "urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg" and caption "Acorus"
When I search for "Acorus"
And I select "Images"
Then there should be 1 result
And there should be the following options:
| first     | second   |
| All Types | Images   |