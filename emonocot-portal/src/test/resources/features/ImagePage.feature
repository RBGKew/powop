Feature: Image Page
  As a member of the public interested in biodiversity
  I would like to view pictures of monocot plants
  In order to find out what they look like
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=75

Background:
  Given there are images with the following properties:
  | identifier | url                                                                | caption   | description     | creator          | locality                          | keywords                     | license                                                                                        |
  | 123        | http://upload.wikimedia.org/wikipedia/commons/7/7b/Poa_annua.jpeg  | Poa annua | Habit           | Rasbak           | Nederlands                        | Poaceae, Habit               | This file is licensed under the Creative Commons Attribution-Share Alike 3.0 Unported license. |
  | 456        | http://upload.wikimedia.org/wikipedia/commons/4/4f/Poa.annua.jpg   | Poa annua | Panicle         | James K. Lindsey | Commanster, Belgian High Ardennes | Poaceae, Panicle, Commanster |                                                                                                |
  | 789        | http://upload.wikimedia.org/wikipedia/commons/7/78/Poa.annua.2.jpg | Poa annua | Panicle         | James K. Lindsey | Commanster, Belgian High Ardennes | Commanster                   |                                                                                                |
  And there are taxa with the following properties:
  | identifier                 | name      | image1 | image2 | image3 |
  | urn:kew.org:wcs:taxon:1234 | Poa annua | 123    | 456    | 789    |

Scenario: Check ImagePage
  The image page should display the image with the caption of the image displayed
  prominently as the title of the page. Metadata about the image should be displayed below
  the image including the license of the image if available
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=174
  When I navigate to image page "123"  
  Then the main image should be "http://upload.wikimedia.org/wikipedia/commons/7/7b/Poa_annua.jpeg"
  And the main image Caption should be "Poa annua"
  And the main image Description should be "Habit"
  And the main image Artist should be "Rasbak"
  And the main image Locality should be "Nederlands"
  And the main image License should be "This file is licensed under the Creative Commons Attribution-Share Alike 3.0 Unported license."

Scenario: Click on Image Keywords
  Users should be able to click on the keywords displayed below the image and search for 
  images that match the keyword
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=174
  When I navigate to image page "123"
  And I click on the keyword "Poaceae"
  Then the following results should be displayed:
  | page | text             |
  | 123  | Poa annua        |
  | 456  | Poa annua        |