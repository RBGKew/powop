Feature: Search Taxa by Text
As a taxonomist, I want to search for a taxon based on the content
which is found in its taxon pages, for example the morphological
description of the taxon
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=5

Scenario: Search for a taxon by text content
Given there are source systems with the following properties:
| identifier | uri                 |
| test       | http://example.com  |
And there are taxa with the following properties:
| identifier              | name      | source | general            |
| urn:kew.org:wcs:taxon:1 | Aus bus   | test   | acuminate leaves   |
| urn:kew.org:wcs:taxon:2 | Aus cus   | test   | obovate leaves     |
When I search for "obovate"
Then the following results should be displayed:
| page                     | text    |
| urn:kew.org:wcs:taxon:2  | Aus cus |

Scenario: Search using autocomplete
Given there are source systems with the following properties:
| identifier | uri                 |
| test       | http://example.com  |
Given there are taxa with the following properties:
| identifier                   | name                    | source |
| urn:kew.org:wcs:taxon:286768 | Rhipogonum              | test   |
| urn:kew.org:wcs:taxon:286789 | Rhipogonum album        | test   |
| urn:kew.org:wcs:taxon:286937 | Rhipogonum brevifolium  | test   |
| urn:kew.org:wcs:taxon:286791 | Rhipogonum discolor     | test   |
| urn:kew.org:wcs:taxon:286793 | Rhipogonum elseyanum    | test   |
| urn:kew.org:wcs:taxon:286806 | Rhipogonum fawcettianum | test   |
| urn:kew.org:wcs:taxon:286796 | Rhipogonum scandens     | test   |
When I am on the search page
And I type for "Rhipogonum" in the search box
And I wait for 1 second
Then the autocomplete box should display the following options:
| option                  |
| Rhipogonum              |
| Rhipogonum album        |
| Rhipogonum brevifolium  | 
| Rhipogonum discolor     |
| Rhipogonum elseyanum    |
| Rhipogonum fawcettianum |
| Rhipogonum scandens     |