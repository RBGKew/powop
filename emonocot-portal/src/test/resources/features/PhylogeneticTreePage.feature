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
  | identifier   | title                               | taxon                        | source | phylogeny                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
  | 987          | Phylogeny of Poaceae                | urn:kew.org:wcs:taxon:1234   | test   | <phyloxml xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.phyloxml.org" xsi:schemaLocation="http://www.phyloxml.org http://www.phyloxml.org/1.10/phyloxml.xsd"><phylogeny rooted="true"><name>Alcohol dehydrogenases</name><description>contains examples of commonly used elements</description><clade><events><speciations>1</speciations></events><clade><taxonomy><id provider="ncbi">6645</id><scientific_name>Octopus vulgaris</scientific_name></taxonomy><sequence><accession source="UniProtKB">P81431</accession><name>Alcohol dehydrogenase class-3</name></sequence></clade><clade><confidence type="bootstrap">100</confidence><events><speciations>1</speciations></events><clade><taxonomy><id provider="ncbi">1423</id><scientific_name>Bacillus subtilis</scientific_name></taxonomy><sequence><accession source="UniProtKB">P71017</accession><name>Alcohol dehydrogenase</name></sequence></clade><clade><taxonomy><id provider="ncbi">562</id><scientific_name>Escherichia coli</scientific_name></taxonomy><sequence><accession source="UniProtKB">Q46856</accession><name>Alcohol dehydrogenase</name></sequence></clade></clade></clade></phylogeny></phyloxml> |

Scenario: View Phylogeny Page
  Users should be able to see associated phylogenies in a taxon page 
  and then select a link to that phylogeny and be shown the phylogeny
  in a web page
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=94
  When I navigate to taxon page "urn:kew.org:wcs:taxon:1234"
  Then there should be 1 associated phylogeny
  When I select the 1st phylogeny link
  Then the title of the phylogeny should be "Phylogeny of Poaceae"
