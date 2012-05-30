Feature: Features page
  As a user of eMonocot, I would like to understand how I can use
  the eMonocot portal in my work.

Scenario: View the Classify overview page
  Users should be able to access the Classify and Classification pages in eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=187
  When I am on the portal home page
  And I select the classify link on the home page
  Then I should be on the emonocot features page

Scenario: View the classification page
  When I am on the portal features page
  And I select the "eMonocot Classification" link in the page
  Then I should be on the "classification" page
  
  Scenario: Identify Page
  As a biodiversity scientist, I want to get a quick overview of the identification
  tools in the portal that can help me
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=87
  When I am on the portal home page
  And I select the identify link on the home page
  Then I should be on the emonocot features page