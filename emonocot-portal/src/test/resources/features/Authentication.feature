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

Scenario: Deny access to unauthenticated user
  In order to ensure that Users cannot access restricted areas, 
  check that they cannot access a restricted page - when accessing
  a restricted page they should be redirected to the login page
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=36
  When I am not authenticated
  And I navigate to source admin page for "PalmWeb"  
  Then the login page should be displayed

Scenario: Registration
  Users should be able to register an account in eMonocot.
  When I open the portal home page
  And I select the registration link
  And I enter the following data into the registration form:
  | username             | repeatUsername       | password        | repeatPassword  |
  | john.doe@example.com | john.doe@example.com | unsafe.password | unsafe.password |
  And I submit the registration form
  Then my profile page should be displayed