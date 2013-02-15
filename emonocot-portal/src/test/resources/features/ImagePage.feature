Feature: Image Page
  As a member of the public interested in biodiversity
  I would like to view pictures of monocot plants
  In order to find out what they look like
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=75

Background:
  Given that the indexes are clean
  And there are taxa with the following properties:
  | identifier                 | name      | 
  | urn:kew.org:wcs:taxon:1234 | Poa annua |
  And there are images with the following properties:
  | identifier | url                                                                | caption   | description     | creator          | locality                          | keywords                     | license                                                                                        | taxa1                      | format |
  | 123        | http://upload.wikimedia.org/wikipedia/commons/7/7b/Poa_annua.jpeg  | Poa annua | Habit           | Rasbak           | Nederlands                        | Poaceae, Habit               | This file is licensed under the Creative Commons Attribution-Share Alike 3.0 Unported license. | urn:kew.org:wcs:taxon:1234 | jpg    |
  | 456        | http://upload.wikimedia.org/wikipedia/commons/4/4f/Poa.annua.jpg   | Poa annua | Panicle         | James K. Lindsey | Commanster, Belgian High Ardennes | Poaceae, Panicle, Commanster |                                                                                                | urn:kew.org:wcs:taxon:1234 | jpg    |
  | 789        | http://upload.wikimedia.org/wikipedia/commons/7/78/Poa.annua.2.jpg | Poa annua | Panicle         | James K. Lindsey | Commanster, Belgian High Ardennes | Commanster                   |                                                                                                | urn:kew.org:wcs:taxon:1234 | jpg    |




Scenario: Click on Image Keywords
  Users should be able to click on the keywords displayed below the image and search for 
  images that match the keyword
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=174
  When I navigate to image page "123"
  And I click on the keyword "Poaceae"
  And the following results should be displayed:
  | page | text             |
  | 123  | Poa annua        |
  | 456  | Poa annua        |
