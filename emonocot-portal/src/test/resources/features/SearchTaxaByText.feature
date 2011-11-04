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