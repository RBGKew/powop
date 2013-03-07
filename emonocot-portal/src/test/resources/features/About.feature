Feature: About and contacts
  As a new user of e-monocot.org, I would like to be able to find out 
  about the project by reading a page on the website 
  and get in contact with the project via email.

Background:
  Given that the indexes are clean
  And I am on the portal home page
  And there are organisations with the following properties:
  | identifier       | title          | commentsEmailedTo |
  | testOrganisation | A Test Source  | test@example.com  |
Scenario: About
  Users should be able to access the About page and the Contact pages in eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=186  
  When I am on the portal home page
  And I select the about link in the footer
  Then I should be on the about page
  And the paragraph named "sourcesParagraph" should contain the text "content from 1 systems"
  And there should be a "A Test Source" link on the page
  When I select the contact link in the footer
  Then I should be on the contact page
