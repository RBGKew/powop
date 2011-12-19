Feature: Authentication
  As the owner of a source system contributing data to eMonocot
  I don't want information about my system, for example, issues
  with the harvesting process, data quality and so on, to be 
  publicly available. Also I do not want users I do not know and
  trust to be able to access restricted functionality relating to my
  system such as the ability to update the information which eMonocot
  displays about my system

Background:
  Given there are groups with the following properties:
  | identifier       | permission1            |
  | PalmWeb          | PERMISSION_VIEW_SOURCE |
  And there are users with the following properties:
  | identifier        | password  | group1         |
  | test@example.com  | Poa annua | PalmWeb        |
  | admin@example.com | Poa annua | administrators |
  And I am not authenticated
  And I am on the portal home page

Scenario: Registration
  Users should be able to register an account in eMonocot.
  When I select the registration link
  And I enter the following data into the registration form:
  | username             | repeatUsername       | password        | repeatPassword  |
  | john.doe@example.com | john.doe@example.com | unsafe.password | unsafe.password |
  And I submit the registration form
  Then the login page should be displayed
  And an info message should say "Registration successful, now log in"  
  When I enter the following data into the login form:
  | username             | password        |
  | john.doe@example.com | unsafe.password |
  And I submit the login form
  Then I should be logged in to the portal
  And my profile page should be displayed
  
Scenario: Deny access to unauthenticated user
  In order to ensure that Users cannot access restricted areas, 
  check that they cannot access a restricted page - when accessing
  a restricted page they should be redirected to the login page
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=36
  And I navigate to source admin page for "PalmWeb"  
  Then the login page should be displayed
  
Scenario: Add privileges to a group
  In order to allow privileged users access to restricted areas, as an
  administrator, I want to be able to create a new group, add a privilege
  to it, and add a user to that group
  Given I am logged in as "admin@example.com" with the password "Poa annua"
  And I am on the group page
  When I select "Create a new group"
  And I enter the following data into the group form:
  | identifier |
  | test       |
  And I submit the group form
  Then an info message should say "Group test created"
  When I go to the page for the group "test"
  And I select "Edit this group"
  And I enter the following data into the members form:
  | identifier        |
  | admin@example.com |
  And I submit the members form
  Then an info message should say "admin@example.com was added to the group"
  When I enter the following data into the access controls form:
  | object  |
  | PalmWeb |
  And I submit the access controls form
  Then an info message should say "READ access to Source PalmWeb was added to the group"