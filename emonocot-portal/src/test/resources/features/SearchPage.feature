Feature: Search Page
  The search page is a central part of eMonocot. There is a 
  search box which users can use to query the database by taxonomic
  name, or to query other content such as the text in the taxon page
  As a taxonomist, I want to search for a taxon by its
  scientific name to find the taxon page and further information
  about that taxon

Background:
  Given that the indexes are clean
  And there are organisations with the following properties:
  | identifier | uri                 | title | commentsEmailedTo |
  | test       | http://example.com  | Test  | test@example.com  |
  And there are taxa with the following properties:
  | identifier                   | name                      | source | general          | created                  | family        |
  | urn:kew.org:wcs:taxon:286768 | Rhipogonum                | test   | acuminate leaves | 1980-10-01T12:00:01.000Z | Rhipogonaceae |
  | urn:kew.org:wcs:taxon:286793 | Rhipogonum elseyanum      | test   |                  | 1981-10-01T12:00:01.000Z | Rhipogonaceae |
  | urn:kew.org:wcs:taxon:286806 | Rhipogonum fawcettianum   | test   |                  | 1982-10-01T12:00:01.000Z | Rhipogonaceae |
  | urn:kew.org:wcs:taxon:286937 | Rhipogonum brevifolium    | test   |                  | 1983-10-01T12:00:01.000Z | Rhipogonaceae |
  | urn:kew.org:wcs:taxon:286789 | Rhipogonum album          | test   | obovate leaves   | 1984-10-01T12:00:01.000Z | Rhipogonaceae |
  | urn:kew.org:wcs:taxon:286791 | Rhipogonum discolor       | test   |                  | 1985-10-01T12:00:01.000Z | Rhipogonaceae |
  | urn:kew.org:wcs:taxon:286796 | Rhipogonum scandens       | test   |                  | 1986-10-01T12:00:01.000Z | Rhipogonaceae |
  | urn:kew.org:wcs:taxon:16186  | Arum italicum             | test   |                  | 1987-10-01T12:00:01.000Z | Araceae       |
  | urn:kew.org:wcs:taxon:10924  | Anthurium discolor        | test   |                  | 1988-10-01T12:00:01.000Z | Araceae       |
  | urn:kew.org:wcs:taxon:209460 | Typhonium discolor        | test   |                  | 1989-10-01T12:00:01.000Z | Araceae       |
  | urn:kew.org:wcs:taxon:16205  | Arum italicum italicum    | test   |                  | 1990-10-01T12:00:01.000Z | Araceae       |
  | urn:kew.org:wcs:taxon:16187  | Arum italicum albispathum | test   |                  | 1991-10-01T12:00:01.000Z | Araceae       |
  | urn:kew.org:wcs:taxon:16191  | Arum italicum canariense	 | test   |                  | 1991-11-01T12:00:01.000Z | Araceae       |
  | urn:kew.org:wcs:taxon:16212	 | Arum italicum neglectum   | test   |                  | 1991-12-01T12:00:01.000Z | Araceae       |
  | urn:kew.org:wcs:family:32	 | Orchidaceae               | test   |                  | 1991-12-01T12:00:01.000Z |               |
  And there are no taxa called "Rhipoga"
  And there are images with the following properties:
  | identifier | url                                                                | caption   | description     | creator          | locality                          |
  | 123        | http://upload.wikimedia.org/wikipedia/commons/7/7b/Poa_annua.jpeg  | Poa annua | Habit           | Rasbak           | Nederlands                        |
  | 456        | http://upload.wikimedia.org/wikipedia/commons/4/4f/Poa.annua.jpg   | Poa annua | Panicle         | James K. Lindsey | Commanster, Belgian High Ardennes |
  | 789        | http://upload.wikimedia.org/wikipedia/commons/7/78/Poa.annua.2.jpg | Poa annua | Panicle         | James K. Lindsey | Commanster, Belgian High Ardennes |
  And there are identification keys with the following properties:
  | identifier   | title                               | taxon                        |
  | 987          | Key to the subtribes of Orchidaceae | urn:kew.org:wcs:family:32    |
  | 999          | Key to the genus Rhipogonum         | urn:kew.org:wcs:taxon:286768 |
  And there are places with the following properties:
  | identifier     | name      | shape                                                                                               |
  | test:place:468 | Narnia    | MULTIPOLYGON (((38.3235 2.9734, 24.2348 90.2389, 23.0492 23.9047, 38.3235 2.9734)))                 |
  | test:place:123 | Bartledan | MULTIPOLYGON (((48.7545 92.9734, 17.4929 98.3473, 83.0492 73.9047, 48.7545 92.9734)))               |
  | test:place:753 | Klatch    | MULTIPOLYGON (((-38.3235 2.9734, -72.5483 -92.4733, 3.0492 2.9047, -38.3235 2.9734)))               |
  And I am on the search page

Scenario: Search for images by their descriptions
  As a botanist in the herbarium, in order to find an image of a particular character
  I want to be able to search for images by their metadata including their descriptions.
  When I restrict the type of object by selecting "Images"
  And I search for "Panicle"
  Then the following results should be displayed:
  | page | text      |
  | 456  | Poa annua |
  | 789  | Poa annua |
