Feature: Identification
  When complete, eMonocot will enable the identification of monocot plants anywhere in the world.

Scenario: Identify Page
  As a biodiversity scientist, I want to get a quick overview of the identification
  tools in the portal that can help me
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=87
  When I am on the portal home page
  And I select the identify link on the home page
  Then I should be on the emonocot features page