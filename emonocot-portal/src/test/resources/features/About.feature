Feature: About and contacts
  As a new user of e-monocot.org, I would like to be able to find out 
  about the project by reading a page on the website 
  and get in contact with the project via email.

Scenario: About
  Users should be able to access the About page and the Contact pages in eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=186
  When I am on the portal home page
  And I select the about link in the footer
  Then I should be on the about page
  When I select the contact link in the footer
  Then I should be on the contact page
