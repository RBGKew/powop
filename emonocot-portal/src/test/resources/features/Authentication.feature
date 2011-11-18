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
  | identifier       | password  | group1  |
  | test@example.com | Poa annua | PalmWeb |
  And I am not authenticated
  And I am on the portal home page

Scenario: Registration
  Users should be able to register an account in eMonocot.
  When I select the registration link
  And I enter the following data into the registration form:
  | username             | repeatUsername       | password        | repeatPassword  |
  | john.doe@example.com | john.doe@example.com | unsafe.password | unsafe.password |
  And I submit the registration form
  Then my profile page should be displayed
  When I select the login link in the header
  And I enter the following data into the login form:
  | username             | password        |
  | john.doe@example.com | unsafe.password |
  And I submit the login form
  Then I should be logged in to the portal
  
Scenario: Deny access to unauthenticated user
  In order to ensure that Users cannot access restricted areas, 
  check that they cannot access a restricted page - when accessing
  a restricted page they should be redirected to the login page
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=36
  And I navigate to source admin page for "PalmWeb"  
  Then the login page should be displayed