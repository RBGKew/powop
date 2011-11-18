Feature: Source Admin Page
  As an administrator of a source  system
  I would like to be able to view details of the data which eMonocot
  has harvested from my system and to be able to detect errors in
  the data provided by my system and differences between the checklist
  used by my system and the checklist used by eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=76

Background:
  Given there are source systems with the following properties:
  | identifier | uri                 |
  | test       | http://example.com  |
  And there are groups with the following properties:
  | identifier    | permission1            |
  | test          | PERMISSION_VIEW_SOURCE |
  And there are users with the following properties:
  | identifier       | password  | group1 |
  | test@example.com | Poa annua | test   |
  And there are job instances with the following properties:
  | id | name    | authorityName |
  | 1  | testJob | test          |
  And there are job executions with the following properties:
  | id | jobInstance |
  | 1  | 1           |

Scenario: Check SourcePage
  The source page should contain the name of the source
  and a link to the source (website) 
  When I navigate to source page "test"  
  Then the source name should be "test"
  And there should be a link to "http://example.com/"
