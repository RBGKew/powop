Feature: Classification Tree
  As a user of eMonocot I would like to be  able to browse the taxonomic
  classification of the monocots from the highest ranking taxon in order to understand
  how the monocot taxa are related and to find information about taxa I am interested in
  by following the links to taxon pages

Background:
  Given that the indexes are clean
  And there are organisations with the following properties:
  | identifier | uri                 | title | commentsEmailedTo |
  | test       | http://example.com  | test  | test@example.com  |
  And there are taxa with the following properties:
  | identifier                   | name                           | family    | rank    | status   | source | parent                     |
  | urn:kew.org:wcs:family:2     | Acorales                       |           | ORDER   | Accepted | test   |                            |
  | urn:kew.org:wcs:family:1     | Acoraceae                      | Acoraceae | FAMILY  | Accepted | test   | urn:kew.org:wcs:family:2   |
  | urn:kew.org:wcs:taxon:2295   | Acorus                         | Acoraceae | GENUS   | Accepted | test   | urn:kew.org:wcs:family:1   |
  | urn:kew.org:wcs:taxon:2304   | Acorus calamus                 | Acoraceae | SPECIES | Accepted | test   | urn:kew.org:wcs:taxon:2295 |
  | urn:kew.org:wcs:taxon:2305   | Acorus calamus var. americanus | Acoraceae | VARIETY | Accepted | test   | urn:kew.org:wcs:taxon:2304 |
  | urn:kew.org:wcs:taxon:2306   | Acorus calamus var. angustatus | Acoraceae | VARIETY | Accepted | test   | urn:kew.org:wcs:taxon:2304 |
  | urn:kew.org:wcs:taxon:2296   | Acorus adulterinus             | Acoraceae | SPECIES | Synonym  | test   |                            |

Scenario: Browse taxonomic hierarchy
  As a taxonomist I would like to be able to browse the taxonomic hierarchy
  starting at the root and expanding the nodes to see the child taxa. Clicking on
  a taxonomic name should take me to the taxon page
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=78
  When I am on the classification page
  And I wait for 2 seconds
  Then the following nodes should be displayed:
  | page                     | text      |
  | urn:kew.org:wcs:family:2 | Acorales  |
  When I expand "Acorales"
  And I wait for 2 seconds
  # Then the following nodes should be displayed:
  # | page                       | text      |
  # | urn:kew.org:wcs:family:2   | Acorales  |
  # | urn:kew.org:wcs:family:1   | Acoraceae |
  # The following does not seem to work because jsTree uses event
  # delegation and this does not work well with Selenium / Firefox
  # When I expand "Acorus"
  # And I wait for 2 seconds
  # Then the following nodes should be displayed:
  # | page                       | text           |
  # | urn:kew.org:wcs:family:2   | Acorales       |
  # | urn:kew.org:wcs:family:1   | Acoraceae      |
  # | urn:kew.org:wcs:taxon:2295 | Acorus         |
  # | urn:kew.org:wcs:taxon:2304 | Acorus calamus |
  # When I expand "Acorus calamus"
  # And I wait for 1 second
  # Then the following nodes should be displayed:
  # | page                       | text                           |
  # | urn:kew.org:wcs:family:2   | Acorales                       |
  # | urn:kew.org:wcs:family:1   | Acoraceae                      |
  # | urn:kew.org:wcs:taxon:2295 | Acorus                         |
  # | urn:kew.org:wcs:taxon:2304 | Acorus calamus                 |
  # | urn:kew.org:wcs:taxon:2305 | Acorus calamus var. americanus |
  # | urn:kew.org:wcs:taxon:2306 | Acorus calamus var. angustatus |