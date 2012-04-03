Feature: Classify page
  As a user of eMonocot, I would like to understand how I can use
  the eMonocot classification in my work.

Scenario: View the Classify overview page
  Users should be able to access the Classify and Classification pages in eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=187
  When I select the "Classify" link in the navbar
  Then I should be on the "classify" page
  And there should be a "eMonocot classification" link on the page

Scenario: View the classification page
  When I am on the classify page
  And I select the "eMonocot classification!" link in the page
  Then I should be on the "classification" page