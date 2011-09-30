Feature: Sort results
In order to produce a checklist of taxa
I want to sort the results returned alphabetically
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=74

Scenario: Sort taxa alphabetically
Given there are taxa with the following properties:
| identifier               | name          |
| urn:kew.org:wcs:taxon:1  | Acorus zeus   |
| urn:kew.org:wcs:taxon:2  | Acorus heus   |
| urn:kew.org:wcs:taxon:3  | Acorus deus   |
| urn:kew.org:wcs:taxon:4  | Acorus eus    |
| urn:kew.org:wcs:taxon:5  | Acorus threus |
| urn:kew.org:wcs:taxon:6  | Arum beus     |
When I search for "Acorus"
And I sort "Alphabetically"
Then there should be 5 results
And the following results should be displayed:
| page                     | text          |
| urn:kew.org:wcs:taxon:3  | Acorus deus   |
| urn:kew.org:wcs:taxon:4  | Acorus eus    |
| urn:kew.org:wcs:taxon:2  | Acorus heus   |
| urn:kew.org:wcs:taxon:5  | Acorus threus |
| urn:kew.org:wcs:taxon:1  | Acorus zeus   |