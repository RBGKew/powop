Feature: Phylogenetic Tree Page
  As an evolutionary biologist, I would like to view phylogenies of 
  monocot plants, in order to see how taxa have evolved over time
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=94

Background:
  Given that the indexes are clean
  Given there are organisations with the following properties:
  | identifier  | uri                     | title                                      | commentsEmailedTo |
  | test        | http://example.com      | Test Organisation                          | test@example.com  |
  And there are taxa with the following properties:
  | identifier                 | name      | source |
  | urn:kew.org:wcs:taxon:1234 | Poaceae   | test   |
  And there are phylogenetic trees with the following properties:
  | identifier   | title                               | taxon                        | source |
  | 987          | Phylogeny of Poaceae                | urn:kew.org:wcs:taxon:1234   | test   |

Scenario: View Phylogeny Page
  Users should be able to see associated phylogenies in a taxon page 
  and then select a link to that phylogeny and be shown the phylogeny
  in a web page
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=94
  When I navigate to taxon page "urn:kew.org:wcs:taxon:1234"
  Then there should be 1 associated phylogeny
  When I select the 1st phylogeny link
  Then the title of the phylogeny should be "Phylogeny of Poaceae"
