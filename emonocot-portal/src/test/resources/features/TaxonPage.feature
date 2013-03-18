Feature: Taxon Page
  As a Taxonomist, I would like to access taxon pages and find out
  taxonomic, nomenclatural, and descriptive information about a taxon.
  As an Ecologist, I would like to access taxon pages to discover
  information about the habitat, life form, associations and traits
  of taxa. 
  Taxon pages are the main pages in eMonocot. There is one page
  per accepted name or synonym and they contain taxonomic and 
  nomenclatural information, images, maps and descriptive
  information about the taxon.

Background:
  Given that the indexes are clean
  Given there are organisations with the following properties:
  | identifier  | uri                     | title                                      | bibliographicCitation                                                                                                                                                              | commentsEmailedTo |
  | test        | http://example.com      | Test Organisation                          |                                                                                                                                                                                    | test@example.com  |
  | test2       | http://www.palmweb.org  | Palmweb - Palms of the World Online        | Palmweb 2011. Palmweb: Palms of the World Online. Published on the internet http://www.palmweb.org. Acccessed on 27/04/2011                                                        | test@example.com  |
  | test3       | http://apps.kew.org/wcs | World Checklist of Selected Plant Families | WCSP 2012. 'World Checklist of Selected Plant Families. Facilitated by the Royal Botanic Gardens, Kew. Published on the Internet; http://apps.kew.org/wcsp/ Retrieved 2011 onwards | test@example.com  |
  And there are references with the following properties:
  | identifier                    | authority | authors                      | title                                                        | datePublished | volume | page  | citation                | publisher                                               |
  | urn:kew.org:wcs:publication:1 |  test     |                              | Sp. Pl.                                                      | (1753)        | 1      | 304pp | Sp. Pl. (1753): 1 304pp |                                                         |
  And there are taxa with the following properties:
  | identifier                   | name                        | status   | source | parent                     | accepted                   | protologue                    | protologueMicroReference | protologLink                                                         | distribution1 | distributionSource | distributionLicense | distributionRights | diagnostic                                                           | diagnosticSource | diagnosticLicense | diagnosticRights   | habitat                                                                                   | diagnosticReference1          | habitatSource | habitatLicense | habitatRights | lifeForm | iucnConservationStatus |
  | urn:kew.org:wcs:family:3     | Araceae                     | Accepted | test   |                            |                            |                               |                          |                                                                      |               |                    |                     |                    |                                                                      |                  |                   |                    |                                                                                           |                               |               |                |               |          |                        |
  | urn:kew.org:wcs:taxon:2295   | Acorus                      | Accepted | test   |                            |                            | Sp. Pl. 1: 324 (1753)         | : 324                    | http://wp5.e-taxonomy.eu/media/palmae/protologe/palm_tc_100447_P.pdf | MAU           | test3              | License1            | Rights1            | These grasslike evergreen plants are hemicryptophytes or geophytes.  | test2            | License3          | Rights3            | Their natural habitat is at the waterside or close to marshes, often found with reedbeds. | urn:kew.org:wcs:publication:1 | test2         | License2       | Rights2       |          |                        |
  | urn:kew.org:wcs:taxon:2304   | Acorus calamus              | Accepted | test   | urn:kew.org:wcs:taxon:2295 |                            |                               |                          |                                                                      |               |                    |                     |                    |                                                                      |                  |                   |                    |                                                                                           |                               |               |                |               |          | LC                     |
  | urn:kew.org:wcs:taxon:2309   | Acorus calamus var. calamus | Accepted | test   | urn:kew.org:wcs:taxon:2304 |                            |                               |                          |                                                                      |               |                    |                     |                    |                                                                      |                  |                   |                    |                                                                                           |                               |               |                |               |          |                        |
  | urn:kew.org:wcs:taxon:109275 | Lemna                       | Accepted | test   | urn:kew.org:wcs:family:3   |                            |                               |                          |                                                                      | FOR           | test3              | License1            | Rights1            |                                                                      |                  |                   |                    |                                                                                           |                               |               |                |               |          |                        |
  | urn:kew.org:wcs:taxon:29332  | Calamus                     | Synonym  | test   |                            | urn:kew.org:wcs:taxon:2295 |                               |                          |                                                                      |               |                    |                     |                    |                                                                      |                  |                   |                    |                                                                                           |                               |               |                |               |          |                        |
  | urn:kew.org:wcs:taxon:65181  | Dunalia artensis            | Doubtful | test   |                            |                            |                               |                          |                                                                      |               |                    |                     |                    |                                                                      |                  |                   |                    |                                                                                           |                               |               |                |               |          |                        |
  And there are references with the following properties:
  | identifier                    | authority | authors                      | title                                                        | datePublished | citation                                                                                                                                                         |  taxa1                     |
  | urn:kew.org:wcs:publication:2 | test      |                              | Sp. Pl.                                                      | (1753)        | Sp. Pl. (1753): 1 304pp                                                                                                                                          | urn:kew.org:wcs:taxon:2295 | 
  | urn:kew.org:wcs:publication:3 | test      | Govaerts, R. & Frodin, D.G.  | World Checklist and Bibliography of Araceae (and Acoraceae)  | 2002          | Govaerts, R. & Frodin, D.G. (2002). World Checklist and Bibliography of Araceae (and Acoraceae): 1-560. The Board of Trustees of the Royal Botanic Gardens, Kew. | urn:kew.org:wcs:taxon:2295 |
  And there are images with the following properties:
  | identifier | url                                                                                                                              | caption                       | taxa1                      | source  |
  | 123        | http://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Acorus_calamus1.jpg/450px-Acorus_calamus1.jpg                           | Acorus calamus                | urn:kew.org:wcs:taxon:2295 | test    | 
  | 456        | http://upload.wikimedia.org/wikipedia/commons/thumb/2/25/Illustration_Acorus_calamus0.jpg/376px-Illustration_Acorus_calamus0.jpg | Acorus calamus (Illustration) | urn:kew.org:wcs:taxon:2295 | test    |
  | 789        | http://upload.wikimedia.org/wikipedia/commons/7/73/Acorus_calamus_illustration.jpg                                               | Sweet flag (drawing)          | urn:kew.org:wcs:taxon:2295 | test    |
  And I navigate to taxon page "urn:kew.org:wcs:taxon:2295" 

Scenario: Taxon Title
  Taxon pages should have the name of the taxon shown at the
  top of the page in an italic font, followed by the authorship
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=36
  Then the page title should be "Acorus"
  And the page title "font-style" should be "italic"

# Difficult to verify this test
# Scenario: Display Distribution Map
#  Where the taxon has a distribution in the database, a distribution
#  map will be shown on the taxon page
#  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=40
#  # This is the url of a map of Mauritus - acceptance testers should verify that the map is displayed properly
#  Then the distribution map should be "http://edit.br.fgov.be/edit_wp5/v1/areas.php?l=earth&ms=800&bbox=-180%2c-90%2c180%2c90&ad=tdwg3%3apresent%3aMAU&as=present%3aFF0000%2c%2c0.25"

Scenario: Display Protologue
  The protologue should be displayed prominently at the top
  of the taxon page in the correct format: Title, date published, volume, pages
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=35
  If there is a link to the protolog available, the protolog should be a link to that
  resource
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=139
  Then the protologue should be "Sp. Pl. 1: 324 (1753)"
#  And the protolog link should be "http://wp5.e-taxonomy.eu/media/palmae/protologe/palm_tc_100447_P.pdf"

Scenario: Display texual data in sections
  Textual data should be displayed on the taxon page where
  it exists for a given taxon. Each topic should have a title 
  that indicates the subject of the section (e.g. Morphology,
  Description, Habitat). Where a taxon does not have content for
  a given topic, the title should not be displayed
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=37
  Then there should be a paragraph "These grasslike evergreen plants are hemicryptophytes or geophytes." with the heading "Diagnostic Description"
  And there should be a paragraph "Their natural habitat is at the waterside or close to marshes, often found with reedbeds." with the heading "Habitat"
  And there should not be a paragraph with the heading "Ecology"

Scenario: Image Gallery
  Where taxa have associated images, those images should be displayed in
  an image gallery with one large image and the remaining images being
  displayed as thumbnails underneath 
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=39
  # This is the main image set for that taxon - acceptance testers should verify that the image is displayed properly
#  Then the main image should be the 1st image
#  And the main image caption should be the 1st caption
  And there should be 3 thumbnails
# Commenting out because this part of the test fails sporadically
#  When I select the 2nd thumbnail
#  Then the main image should be the 2nd image
#  And the main image caption should be the 2nd caption
#  When I select the 3rd thumbnail
#  Then the main image should be the 3rd image
#  And the main image caption should be the 3rd caption
#  When I select the main image
#  Then the image page should be displayed
  
Scenario: Display Taxa Relationship
  The taxon relationships should be displayed in a tree. 
  The tree should contain links to the taxon ancestors and an anchor to the section 
  of the taxon page listing its children, where they exist.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=77
  When I navigate to taxon page "urn:kew.org:wcs:taxon:2304"
  Then there should be 1 ancestor
  And the subordinate taxon link should say "Acorus calamus var. calamus"
  When I navigate to taxon page "urn:kew.org:wcs:taxon:2309"
  And there should be 2 ancestors
  And there should be 0 subordinate taxa

Scenario: Taxa Relationship Not Present For Synonyms
  The taxon relationships should not be displayed for synonyms. 
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=223
  When I navigate to taxon page "urn:kew.org:wcs:taxon:29332"
  Then there should not be a "classificationBox" visible
  
Scenario: Display Taxon Data
  Taxon data should be diplayed in a table.
  For IUCN data there should be also an image indicating the conservation status.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=189
  When I navigate to taxon page "urn:kew.org:wcs:taxon:2304"
  Then there should not be a "dataBox" visible
  
Scenario: Display Vernacular Names
  Vernacular Names should be diplayed in a table.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=189
  When I navigate to taxon page "urn:kew.org:wcs:taxon:2304"
  Then there should not be a "vernacularNames" visible
  
Scenario: Accepted name show for synonyms
  Taxa should show their status at the top of the page
  Then the taxon status should be 'This taxon is accepted by Test Organisation'
  When I navigate to taxon page "urn:kew.org:wcs:taxon:29332"
  Then the taxon status should be 'This taxon is a synonym of Acorus'
  When I navigate to taxon page "urn:kew.org:wcs:taxon:65181"
  Then the taxon status should be 'This taxon is unplaced'
  
Scenario: Textual Citations
  The textual parts of the page (habitat, diagnosis, description etc) can have references
  The references should be displayed as small citation keys (e.g. [1]) which link to the 
  bibliography at the bottom of the page. At the bottom of the page, there should be the 
  bibliography with the references listed in full.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=38
  Then the citation for the "Diagnostic Description" topic should be "C1"
  And the bibliography entry "1" should be "1 Sp. Pl. (1753): 1 304pp"
  And the provenance entry "C" should be "C Rights3 License3"
  
Scenario: Information from
  Informations in the taxon pages can come from different source systems, and these should be displayed
  in the organisation section at the bottom of the page. There should be also license and rights statements.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=197
  When I navigate to taxon page "urn:kew.org:wcs:taxon:2295"
  Then the provenance entry "B" should be "B Rights2 License2"

Scenario: Bibliography
  Taxa can have general references as well, and these should be displayed in the bibliography
  at the bottom of the page.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=118
  Then the bibliography entry "3" should be "3 Govaerts, R. & Frodin, D.G. (2002). World Checklist and Bibliography of Araceae (and Acoraceae): 1-560. The Board of Trustees of the Royal Botanic Gardens, Kew."
 
# Unfortunately, really difficult to test   
#Scenario: Distribution
#  The textual distribution lists the regions where a taxon is present.
#  The names can contain special characters.
#  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=201
#  When I navigate to taxon page "urn:kew.org:wcs:taxon:109275"
#  Then the distribution should list "Føroyar"
