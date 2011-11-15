Feature:

Scenario: Facet on continent and region
Given there are taxa with the following properties:
| identifier              | name              | distribution1 | distribution2 |
| urn:kew.org:wcs:taxon:1 | Arum alpinariae   | TUR           |               |
| urn:kew.org:wcs:taxon:2 | Arum apulum       | ITA           |               |
| urn:kew.org:wcs:taxon:3 | Arum balansanum   | TUR           |               |
| urn:kew.org:wcs:taxon:4 | Arum besserianum  | POL           | UKR           |
| urn:kew.org:wcs:taxon:3 | Arum byzantinum   | TUE           | TUR           |
| urn:kew.org:wcs:taxon:4 | Arum concinnatum  | GRC           | KRI           |
| urn:kew.org:wcs:taxon:3 | Arum creticum     | KRI           | EAI           |
| urn:kew.org:wcs:taxon:4 | Arum cylindraceum | DEN           | SWE           |
When I search for "Arum"
And I restrict the "Continent" by selecting "Europe"
Then there should be 6 results
When I restrict the "Region" by selecting "Southeastern Europe"
Then the following results should be displayed:
| page                     | text        |
| urn:kew.org:wcs:taxon:2  | Arum apulum |