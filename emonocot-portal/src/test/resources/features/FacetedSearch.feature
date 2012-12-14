Feature: Faceted Search
  As a user of eMonocot I would like to be  able to search for
  particular subsets of information, restricting the search to particular
  taxonomic or geographical groups. We achieve this by calculating "facets" which are
  counts of records matching particular values, for example taxa belonging 
  to taxonomic families or occuring in a particular continent. We can also count
  matches across different types of data, for example, Images, Taxon Pages, and Keys

Background:
  Given that the indexes are clean
  And there are source systems with the following properties:
  | identifier | uri                 |
  | test       | http://example.com  |
  And there are taxa with the following properties:
  | identifier                   | name                           | family    | rank    | status   | distribution1 | distribution2 | source |
  | urn:kew.org:wcs:taxon:2295   | Acorus                         | Acoraceae | GENUS   | Accepted |               |               | test   |
  | urn:kew.org:wcs:taxon:2304   | Acorus calamus                 | Acoraceae | SPECIES | Accepted |               |               | test   |
  | urn:kew.org:wcs:taxon:2305   | Acorus calamus var. americanus | Acoraceae | VARIETY | Accepted |               |               | test   |
  | urn:kew.org:wcs:taxon:2306   | Acorus calamus var. angustatus | Acoraceae | VARIETY | Accepted |               |               | test   |
  | urn:kew.org:wcs:taxon:2296   | Acorus adulterinus             | Acoraceae | SPECIES | Synonym  |               |               | test   |
  | urn:kew.org:wcs:taxon:456456 | Arum alpinariae                | Araceae   | SPECIES | Accepted | TUR           |               | test   |
  | urn:kew.org:wcs:taxon:16041  | Arum apulum                    | Araceae   | SPECIES | Accepted | ITA           |               | test   |
  | urn:kew.org:wcs:taxon:16050  | Arum balansanum                | Araceae   | SPECIES | Accepted | TUR           |               | test   |
  | urn:kew.org:wcs:taxon:16052  | Arum besserianum               | Araceae   | SPECIES | Accepted | POL           | UKR           | test   |
  | urn:kew.org:wcs:taxon:16060  | Arum byzantinum                | Araceae   | SPECIES | Accepted | TUE           | TUR           | test   |
  | urn:kew.org:wcs:taxon:16074  | Arum concinnatum               | Araceae   | SPECIES | Accepted | GRC           | KRI           | test   |
  | urn:kew.org:wcs:taxon:16088  | Arum creticum                  | Araceae   | SPECIES | Accepted | KRI           | EAI           | test   |
  | urn:kew.org:wcs:taxon:16095  | Arum cylindraceum              | Araceae   | SPECIES | Accepted | DEN           | SWE           | test   |
  | urn:kew.org:wcs:taxon:16240  | Arum maculatum                 | Araceae   | SPECIES | Accepted |               |               | test   |
  And there are images with the following properties:
  | identifier                                                                            | caption | source |
  | urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg | Acorus  | test   |
  And I am on the search page

Scenario: Search for Both Taxa and Images
  As a user, I would like to be able to search for any pages 
  matching a particular term. This is the default option. Users
  are able to restrict their search further by selecting one or more facets
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=72
  When I search for "Acorus"
  Then there should be 6 results
  And the Type facet should have the following options:
  | option |
  | Images |
  | Taxa   |

Scenario: Search for Only Taxa
  As a taxonomist I would like to search for taxa matching a particular term,
  and then to narrow down the list to include only accepted species
  When I search for "Acorus"
  And I restrict the type of object by selecting "Taxa"
  Then there should be 5 results
  And the Type facet should have the following options:
  | option    |
  | Taxa      |
  And the Family facet should have the following options:
  | option    |
  | Acoraceae |
  When I restrict the "taxon.taxon_rank_s" by selecting "Species"
  Then there should be 2 results
  When I restrict the "taxon.taxonomic_status_s" by selecting "Accepted Name"
  Then there should be 1 result

Scenario: Search for Only Images
  As a member of the public, I would like to be able to search for an
  image of a plant I am interested in
  When I search for "Acorus"
  And I restrict the type of object by selecting "Images"
  Then there should be 1 result
  And the Type facet should have the following options:
  | option    |
  | Images    |
  
Scenario: Facet on Family
  As a taxonomist specializing in the Araceae
  I would like to be able to search within on family of plants
  When I restrict the "taxon.family_s" by selecting "Araceae"
  Then there should be 9 results
  And the following results should be displayed:
  | page                         | text              |
  | urn:kew.org:wcs:taxon:16052  | Arum besserianum  |
  | urn:kew.org:wcs:taxon:16060  | Arum byzantinum   |
  | urn:kew.org:wcs:taxon:16050  | Arum balansanum   |
  | urn:kew.org:wcs:taxon:456456 | Arum alpinariae   |  
  | urn:kew.org:wcs:taxon:16041  | Arum apulum       |
  | urn:kew.org:wcs:taxon:16074  | Arum concinnatum  |
  | urn:kew.org:wcs:taxon:16088  | Arum creticum     |
  | urn:kew.org:wcs:taxon:16095  | Arum cylindraceum |
  | urn:kew.org:wcs:taxon:16240  | Arum maculatum    |

Scenario: Facet on continent and region
  We should also be able to facet on the continent and region where a taxon occurs.
  The region facet is only displayed once a continent has been selected
  When I search for "Arum"
  And I restrict the "taxon.distribution_TDWG_0_ss" by selecting "Europe"
  Then there should be 6 results
  When I restrict the "taxon.distribution_TDWG_1_ss" by selecting "Southeastern Europe"
  Then the following results should be displayed:
  | page                        | text             |
  | urn:kew.org:wcs:taxon:16060 | Arum byzantinum  |
  | urn:kew.org:wcs:taxon:16041 | Arum apulum      |
  | urn:kew.org:wcs:taxon:16074 | Arum concinnatum |
  | urn:kew.org:wcs:taxon:16088 | Arum creticum    |