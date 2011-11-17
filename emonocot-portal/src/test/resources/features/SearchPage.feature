Feature: Search Page
  The search page is a central part of eMonocot. There is a 
  search box which users can use to query the database by taxonomic
  name, or to query other content such as the text in the taxon page
  As a taxonomist, I want to search for a taxon by its
  scientific name to find the taxon page and further information
  about that taxon

Background:
  Given there are source systems with the following properties:
  | identifier | uri                 |
  | test       | http://example.com  |
  And there are taxa with the following properties:
  | identifier                    | name                    | source | general          |
  | urn:kew.org:wcs:taxon:286768  | Rhipogonum              | test   | acuminate leaves |
  | urn:kew.org:wcs:taxon:286793  | Rhipogonum elseyanum    | test   |                  |
  | urn:kew.org:wcs:taxon:286806  | Rhipogonum fawcettianum | test   |                  |
  | urn:kew.org:wcs:taxon:286937  | Rhipogonum brevifolium  | test   |                  |
  | urn:kew.org:wcs:taxon:286789  | Rhipogonum album        | test   | obovate leaves   |
  | urn:kew.org:wcs:taxon:286791  | Rhipogonum discolor     | test   |                  |
  | urn:kew.org:wcs:taxon:286796  | Rhipogonum scandens     | test   |                  |
  | urn:kew.org:wcs:taxon:16186   | Arum italicum           | test   |                  |
  | urn:kew.org:wcs:taxon:10924   | Anthurium discolor      | test   |                  |
  | urn:kew.org:wcs:taxon:209460  | Typhonium discolor      | test   |                  |
  And there are no taxa called "Rhipoga"
  And I am on the search page

Scenario: Search for a single taxon
  Search for a taxon by typing its taxonomic name in to the search box
  The taxon should appear near to the top of the list of results
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=3
  When I search for "Rhipogonum album"
  Then the following results should be displayed:
  | page                         | text                    |
  | urn:kew.org:wcs:taxon:286789 | Rhipogonum album        |
  | urn:kew.org:wcs:taxon:286768 | Rhipogonum              |
  | urn:kew.org:wcs:taxon:286793 | Rhipogonum elseyanum    |
  | urn:kew.org:wcs:taxon:286806 | Rhipogonum fawcettianum |
  | urn:kew.org:wcs:taxon:286937 | Rhipogonum brevifolium  |
  | urn:kew.org:wcs:taxon:286791 | Rhipogonum discolor     |
  | urn:kew.org:wcs:taxon:286796 | Rhipogonum scandens     |

Scenario: Search for multiple taxa with the same epithet
  If multiple taxa have the same epithet, then they should all be returned
  if I search using just the epithet
  When I search for "discolor"
  Then the following results should be displayed:
  | page                         | text                |
  | urn:kew.org:wcs:taxon:286791 | Rhipogonum discolor |
  | urn:kew.org:wcs:taxon:10924  | Anthurium discolor  |
  | urn:kew.org:wcs:taxon:209460 | Typhonium discolor  |

Scenario: Search for multiple taxa within the same genus
  Searching using the generic epithet on its own should return all
  that have that generic epithet
  When I search for "Rhipogonum"
  Then the following results should be displayed:
  | page                          | text                   |
  | urn:kew.org:wcs:taxon:286768 | Rhipogonum              |
  | urn:kew.org:wcs:taxon:286793 | Rhipogonum elseyanum    |
  | urn:kew.org:wcs:taxon:286806 | Rhipogonum fawcettianum |
  | urn:kew.org:wcs:taxon:286937 | Rhipogonum brevifolium  |
  | urn:kew.org:wcs:taxon:286789 | Rhipogonum album        |
  | urn:kew.org:wcs:taxon:286791 | Rhipogonum discolor     |
  | urn:kew.org:wcs:taxon:286796 | Rhipogonum scandens     |

Scenario: Negative search
  Searching using a term which is not in the database should not
  return any results, along with a message to explain that there
  were no matching results 
  When I search for "Rhipoga"
  Then there should be 0 results
  And the search results page should display "There are no results matching 'Rhipoga'"

Scenario: Search for a taxon by the content in the taxon page
  As a taxonomist, I want to search for a taxon based on the content
  which is found in its taxon pages, for example the morphological
  description of the taxon
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=5
  When I search for "obovate"
  Then the following results should be displayed:
  | page                          | text             |
  | urn:kew.org:wcs:taxon:286789  | Rhipogonum album |

Scenario: Autocomplete
  Autocomplete queries the database for matching terms while you type
  your query, prompting the user with matches before performing the search.
  Only a few results are returned, and the user is able to select one and search
  using that result or continue with their own query.
  When I type for "Rhipogonum" in the search box
  And I wait for 1 second
  Then the autocomplete box should display the following options:
  | option                  |
  | Rhipogonum              |
  | Rhipogonum elseyanum    |
  | Rhipogonum fawcettianum | 
  | Rhipogonum brevifolium  |
  | Rhipogonum album        |
  | Rhipogonum discolor     |
  | Rhipogonum scandens     |

Scenario: Sort taxa Alphabetically
  As a taxonomist, in order to produce a checklist of taxa
  I want to sort the results returned alphabetically
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=74
  When I search for "Rhipogonum"
  And I sort "Alphabetically"
  Then there should be 7 results
  And the following results should be displayed:
  | page                          | text                    |
  | urn:kew.org:wcs:taxon:286768  | Rhipogonum              |
  | urn:kew.org:wcs:taxon:286789  | Rhipogonum album        |
  | urn:kew.org:wcs:taxon:286937  | Rhipogonum brevifolium  |
  | urn:kew.org:wcs:taxon:286791  | Rhipogonum discolor     |
  | urn:kew.org:wcs:taxon:286793  | Rhipogonum elseyanum    |
  | urn:kew.org:wcs:taxon:286806  | Rhipogonum fawcettianum |
  | urn:kew.org:wcs:taxon:286796  | Rhipogonum scandens     |