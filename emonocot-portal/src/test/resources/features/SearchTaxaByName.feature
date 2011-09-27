Feature: Search Taxa by Name
As a taxonomist, I want to search for a taxon by its
scientific name to find the taxon page and further information
about that taxon
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=3

Scenario: Search for a single taxon
Given there are taxa with the following properties:
| identifier                 | name             |
| urn:kew.org:wcs:taxon:123  | Rhipogonum album |
| urn:kew.org:wcs:taxon:456  | Arum italicum    |
When I search for "Rhipogonum album"
Then the following results should be displayed:
| page                       | text                  |
| urn:kew.org:wcs:taxon:123  | Rhipogonum album      |

Scenario: Search for multiple taxa with the same epithet
Given there are taxa with the following properties:
| identifier                 | name                  |
| urn:kew.org:wcs:taxon:123  | Anthurium discolor    |
| urn:kew.org:wcs:taxon:456  | Arum italicum         |
| urn:kew.org:wcs:taxon:789  | Bulbophyllum discolor |
When I search for "discolor"
Then the following results should be displayed:
| page                       | text                  |
| urn:kew.org:wcs:taxon:123  | Anthurium discolor    |
| urn:kew.org:wcs:taxon:789  | Bulbophyllum discolor |

Scenario: Search for multiple taxa within the same genus
Given there are taxa with the following properties:
| identifier                    | name                    |
| urn:kew.org:wcs:taxon:286768  | Rhipogonum              |
| urn:kew.org:wcs:taxon:286789  | Rhipogonum album        |
| urn:kew.org:wcs:taxon:286937  | Rhipogonum brevifolium  |
| urn:kew.org:wcs:taxon:286791  | Rhipogonum discolor     |
| urn:kew.org:wcs:taxon:286793  | Rhipogonum elseyanum    |
| urn:kew.org:wcs:taxon:286806  | Rhipogonum fawcettianum |
| urn:kew.org:wcs:taxon:286796  | Rhipogonum scandens     |
When I search for "Rhipogonum"
Then the following results should be displayed:
| page                          | text                    |
| urn:kew.org:wcs:taxon:286768  | Rhipogonum              |
| urn:kew.org:wcs:taxon:286789  | Rhipogonum album        |
| urn:kew.org:wcs:taxon:286937  | Rhipogonum brevifolium  |
| urn:kew.org:wcs:taxon:286791  | Rhipogonum discolor     |
| urn:kew.org:wcs:taxon:286793  | Rhipogonum elseyanum    |
| urn:kew.org:wcs:taxon:286806  | Rhipogonum fawcettianum |
| urn:kew.org:wcs:taxon:286796  | Rhipogonum scandens     |

Scenario: Negative search
Given there are taxa with the following properties:
| identifier                    | name                    |
| urn:kew.org:wcs:taxon:286768  | Rhipogonum              |
| urn:kew.org:wcs:taxon:286789  | Rhipogonum album        |
And there are no taxa called "Rhipoga"
When I search for "Rhipoga"
Then there should be 0 results
And the search results page should display "There are no results matching 'Rhipoga'"