Feature: Display Distribution Map in Taxon Page
As a user of eMonocot, I would like to view the 
distribution map of a specific taxon in its taxon
page
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=40

Scenario: Distribution Map
Given there are taxa with the following properties:
| identifier                 | name   | distribution1 |
| urn:kew.org:wcs:taxon:2295 | Acorus | MAU           |
When I navigate to taxon page "urn:kew.org:wcs:taxon:2295" 
Then the distribution map should be "http://edit.br.fgov.be/edit_wp5/v1/areas.php?l=earth&ms=470&bbox=-180%2c-90%2c180%2c90&ad=tdwg3%3apresent%3aMAU&as=present%3aFF0000%2c%2c0.25"