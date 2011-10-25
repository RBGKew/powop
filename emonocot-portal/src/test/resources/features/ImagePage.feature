Feature: Image Page
In order to ensure that the image page is correct, open an image page and check that
there is the main image
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=75

Scenario: Check ImagePage
Given there are images with the following properties:
| identifier | url                                                                | caption   |
| 123        | http://upload.wikimedia.org/wikipedia/commons/7/7b/Poa_annua.jpeg  | Poa annua |
| 456        | http://upload.wikimedia.org/wikipedia/commons/4/4f/Poa.annua.jpg   | Poa annua |
| 789        | http://upload.wikimedia.org/wikipedia/commons/7/78/Poa.annua.2.jpg | Poa annua |
And there are taxa with the following properties:
| identifier                 | name      | image1 | image2 | image3 |
| urn:kew.org:wcs:taxon:1234 | Poa annua | 123    | 456    | 789    |
When I navigate to image page "123"  
Then the main image should be "http://upload.wikimedia.org/wikipedia/commons/7/7b/Poa_annua.jpeg"
And the main image caption should be "Poa annua"
