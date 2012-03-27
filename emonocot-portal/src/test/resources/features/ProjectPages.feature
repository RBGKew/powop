Feature: Classify page
  As a user of eMonocot, I would like to understand how I can use
  the eMonocot classification in my work.

Scenario: Classify
  Users should be able to access the Classify and Classification pages in eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=187
  When I am on the portal home page
  And I select the classify link in the header
  Then I should be on the classify page
  And there should be a link to the Classification page

  When I am on the classify page
  And I select the classification link in the page
  Then I should be on the classification page