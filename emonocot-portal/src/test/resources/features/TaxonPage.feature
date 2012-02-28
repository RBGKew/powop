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
  Given there are references with the following properties:
  | identifier                    | authors                      | title                                                        | datePublished | volume | page  | citation                | publisher                                               |
  | urn:kew.org:wcs:publication:1 |                              | Sp. Pl.                                                      | (1753)        | 1      | 304pp | Sp. Pl. (1753): 1 304pp |                                                         |
  | urn:kew.org:wcs:publication:2 | Govaerts, R. & Frodin, D.G.  | World Checklist and Bibliography of Araceae (and Acoraceae)  | 2002          |        | 1-560 |                         | The Board of Trustees of the Royal Botanic Gardens, Kew |
  And there are images with the following properties:
  | identifier | url                                                                                                                              | caption                       |
  | 123        | http://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Acorus_calamus1.jpg/450px-Acorus_calamus1.jpg                           | Acorus calamus                |
  | 456        | http://upload.wikimedia.org/wikipedia/commons/thumb/2/25/Illustration_Acorus_calamus0.jpg/376px-Illustration_Acorus_calamus0.jpg | Acorus calamus (Illustration) |
  | 789        | http://upload.wikimedia.org/wikipedia/commons/7/73/Acorus_calamus_illustration.jpg                                               | Sweet flag (drawing)          |
  And there are taxa with the following properties:
  | identifier                 | name                        | parent                     | protologue                    | protologueMicroReference | protologLink                                                         | distribution1 | diagnostic                                                          | habitat                                                                                   | image1 | image2 | image3 | diagnosticReference1          | reference1                    | reference2                    |
  | urn:kew.org:wcs:taxon:2295 | Acorus                      |                            | urn:kew.org:wcs:publication:1 | : 324                    | http://wp5.e-taxonomy.eu/media/palmae/protologe/palm_tc_100447_P.pdf | MAU           | These grasslike evergreen plants are hemicryptophytes or geophytes. | Their natural habitat is at the waterside or close to marshes, often found with reedbeds. | 789    | 456    | 123    | urn:kew.org:wcs:publication:1 | urn:kew.org:wcs:publication:1 | urn:kew.org:wcs:publication:2 |
  | urn:kew.org:wcs:taxon:2304 | Acorus calamus              | urn:kew.org:wcs:taxon:2295 |                               |                          |                                                                      |               |                                                                     |                                                                                           |        |        |        |                               |                               |                               |
  | urn:kew.org:wcs:taxon:2309 | Acorus calamus var. calamus | urn:kew.org:wcs:taxon:2304 |                               |                          |                                                                      |               |                                                                     |                                                                                           |        |        |        |                               |                               |                               |
  And I navigate to taxon page "urn:kew.org:wcs:taxon:2295" 

Scenario: Taxon Title
  Taxon pages should have the name of the taxon shown at the
  top of the page in an italic font, followed by the authorship
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=36
  Then the page title should be "Acorus"
  And the page title "font-style" should be "italic"

Scenario: Display Distribution Map
  Where the taxon has a distribution in the database, a distribution
  map will be shown on the taxon page
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=40
  # This is the url of a map of Mauritus - acceptance testers should verify that the map is displayed properly
  Then the distribution map should be "http://edit.br.fgov.be/edit_wp5/v1/areas.php?l=earth&ms=800&bbox=-180%2c-90%2c180%2c90&ad=tdwg3%3apresent%3aMAU&as=present%3aFF0000%2c%2c0.25"

Scenario: Display Protologue
  The protologue should be displayed prominently at the top
  of the taxon page in the correct format: Title, date published, volume, pages
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=35
  If there is a link to the protolog available, the protolog should be a link to that
  resource
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=139
  Then the protologue should be "Sp. Pl. 1: 324 (1753)"
  And the protolog link should be "http://wp5.e-taxonomy.eu/media/palmae/protologe/palm_tc_100447_P.pdf"

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
  Then the main image should be the 1st image
  And the main image caption should be the 1st caption
  And there should be 3 thumbnails
  When I select the 2nd thumbnail
  Then the main image should be the 2nd image
  And the main image caption should be the 2nd caption
  When I select the 3rd thumbnail
  Then the main image should be the 3rd image
  And the main image caption should be the 3rd caption
  When I select the main image
  Then the image page should be displayed
  
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

Scenario: Textual Citations
  The textual parts of the page (habitat, diagnosis, description etc) can have references
  The references should be displayed as small citation keys (e.g. [1]) which link to the 
  bibliography at the bottom of the page. At the bottom of the page, there should be the 
  bibliography with the references listed in full.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=38
  Then the citation for the "Diagnostic Description" topic should be "1"
  And the bibliography entry "1" should be "1 Sp. Pl. (1753): 1 304pp"

Scenario: Bibliography
  Taxa can have general references as well, and these should be displayed in the bibliography
  at the bottom of the page.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=118
  Then the bibliography entry "2" should be "2 Govaerts, R. & Frodin, D.G. (2002). World Checklist and Bibliography of Araceae (and Acoraceae): 1-560. The Board of Trustees of the Royal Botanic Gardens, Kew."