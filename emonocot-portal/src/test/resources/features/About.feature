Feature: About and contacts
  As a new user of e-monocot.org, I would like to be able to find out 
  about the project by reading a page on the website 
  and get in contact with the project via email.

Background:
  Given that the indexes are clean
  And I am on the portal home page
  And there are organisations with the following properties:
  | identifier | title           | commentsEmailedTo  | footerLogoPosition | logoUrl                                              |
  | testOrg1   | A Test Source   | test1@example.com  | 200                | http://emonocot.org/css/images/footer/kew.png        |
  | testOrg2   | 2nd test source | test2@example.com  | 101                | http://emonocot.org/css/images/footer/uni-oxford.png |
  | testOrg3   | 3rd test source | test3@example.com  | 100                | http://emonocot.org/css/images/footer/nhm.png        |
  | testOrg4   | 4th test source | test4@example.com  | 201                |                                                      |
  | testOrg5   | 5th test source | test5@example.com  |                    |                                                      |

Scenario: About
  Users should be able to access the About page and the Contact pages in eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=186  
  When I am on the portal home page
  And I select the about link in the footer
  Then I should be on the about page
  And the paragraph named "sourcesParagraph" should contain the text "content from 5 systems"
  #And there should be a "A Test Source" link on the page
  And the contact link in the footer should be "mailto:enquiries@e-monocot.org?subject=eMonocot Portal enquiry"

Scenario: Organisation logos
  Users should see logos from selected organisations in the page footer, in the
  specified order (column = footerLogoPosition % 100, row = footerLogoPosition / 100).
  When I am on the portal home page
  Then I should see logos in this arrangement:
  | 3rd test source | 2nd test source |
  | A Test Source   |                 |
