Feature: Spatial Search
  Spatial search allows users to search for pages (taxa, images) using 
  geospatial constraints (e.g. bounding boxes).
  As a conservation biologist, I want a list of taxa occuring 
  in a certain region so that I can assess the threatened taxa in that region

Background:
  Given there are source systems with the following properties:
  | identifier | uri                 |
  | test       | http://example.com  |
  And there are taxa with the following properties:
  | identifier                   | name                        | distribution1 |
  | urn:kew.org:wcs:family:3     | Araceae                     |               |
  | urn:kew.org:wcs:taxon:2295   | Acorus                      | NSW           |
  | urn:kew.org:wcs:taxon:2304   | Acorus calamus              |               |
  | urn:kew.org:wcs:taxon:2309   | Acorus calamus var. calamus |               |
  | urn:kew.org:wcs:taxon:109275 | Lemna                       | FOR           |
  | urn:kew.org:wcs:taxon:29332  | Calamus                     |               |
  And I am on the spatial search page

Scenario: Search using the bounding box
  As a user of eMonocot, I would like to search by Distribution either by drawing a
  polygon or selecting a point on a map.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=43
  When I search within 150.0 -40.0, 160.0 -20.0
#  Then the following results should be displayed:
#  | page                         | text   |
#  | urn:kew.org:wcs:taxon:2295   | Acorus |