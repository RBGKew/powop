Feature: Image Gallery
In order to ensure that the image gallery works properly
in the taxon page, open a taxon page and check that
there are images
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=39

Scenario: Check Gallery
Given there are images with the following properties:
| identifier | url                                                                | caption   |
| 123        | http://upload.wikimedia.org/wikipedia/commons/7/7b/Poa_annua.jpeg  | Poa annua |
And there are taxa with the following properties:
| identifier                 | name      | image1 |
| urn:kew.org:wcs:taxon:1234 | Poa annua | 123    |
When I navigate to taxon page "urn:kew.org:wcs:taxon:1234"  
Then the main image should be "http://upload.wikimedia.org/wikipedia/commons/7/7b/Poa_annua.jpeg"
And the main image caption should be "Poa annua"
And there should be 1 thumbnail
