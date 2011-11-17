Feature: Source Page
  As a user of eMonocot
  I would like to be able to view details about the sources of information
  which have contributed to eMonocot
  In order to learn where to find more information and to verify
  the authoritativeness of the information presented in the eMonocot portal
  there is the source name and the link to the source
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=76

Background:
  Given there are source systems with the following properties:
  | identifier | uri                 |
  | test       | http://example.com  |

Scenario: Check SourcePage
  The source page should contain the name of the source
  and a link to the source (website) 
  When I navigate to source page "test"  
  Then the source name should be "test"
  And there should be a link to "http://example.com/"
