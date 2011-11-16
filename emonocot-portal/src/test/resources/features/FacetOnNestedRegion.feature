Feature:

Scenario: Facet on continent and region
Given there are source systems with the following properties:
| identifier | uri                 |
| test       | http://example.com  |
Given there are taxa with the following properties:
| identifier              | name              | distribution1 | distribution2 | source |
| urn:kew.org:wcs:taxon:1 | Arum alpinariae   | TUR           |               | test   |
| urn:kew.org:wcs:taxon:2 | Arum apulum       | ITA           |               | test   |
| urn:kew.org:wcs:taxon:3 | Arum balansanum   | TUR           |               | test   |
| urn:kew.org:wcs:taxon:4 | Arum besserianum  | POL           | UKR           | test   |
| urn:kew.org:wcs:taxon:5 | Arum byzantinum   | TUE           | TUR           | test   |
| urn:kew.org:wcs:taxon:6 | Arum concinnatum  | GRC           | KRI           | test   |
| urn:kew.org:wcs:taxon:7 | Arum creticum     | KRI           | EAI           | test   |
| urn:kew.org:wcs:taxon:8 | Arum cylindraceum | DEN           | SWE           | test   |
When I search for "Arum"
And I restrict the "Continent" by selecting "Europe"
Then there should be 6 results
When I restrict the "Region" by selecting "Southeastern Europe"
Then the following results should be displayed:
| page                     | text             |
| urn:kew.org:wcs:taxon:2  | Arum apulum      |
| urn:kew.org:wcs:taxon:5  | Arum byzantinum  |
| urn:kew.org:wcs:taxon:6  | Arum concinnatum |
| urn:kew.org:wcs:taxon:7  | Arum creticum    |