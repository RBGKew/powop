Feature: Tour page
  As a user of eMonocot, I would like to understand how I can use
  the eMonocot portal in my work. I would also like to understand 
  the disclaimer and terms of use for the eMonocot portal

Scenario: View the Classify overview page
  Users should be able to access the Classify and Classification pages in eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=187
  When I am on the portal home page
  And I select the classify link on the home page
  Then I should be on the emonocot features page

#Scenario: View the classification page
#  When I am on the portal features page
#  And I select the classification tag
#  Then I should be on the "classification" page
  
Scenario: Identify Page
  As a biodiversity scientist, I want to get a quick overview of the identification
  tools in the portal that can help me
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=87
  When I am on the portal home page
  And I select the identify link on the home page
  Then I should be on the emonocot features page
  
Scenario: View the Disclaimer & Terms of Use
  As the hosting institute of eMonocot I would like to add a disclaimer and terms of use
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=402
  When I am on the portal home page
  And I select the terms of use link in the footer
  Then I should be on the eMonocot Terms of Use page