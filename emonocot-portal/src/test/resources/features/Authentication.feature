Feature: Authentication
  As the owner of a source system contributing data to eMonocot
  I don't want information about my system, for example, issues
  with the harvesting process, data quality and so on, to be 
  publicly available. Also I do not want users I do not know and
  trust to be able to access restricted functionality relating to my
  system such as the ability to update the information which eMonocot
  displays about my system

Background:
  Given that the indexes are clean
  And there are organisations with the following properties:
  | identifier       | title          | commentsEmailedTo |
  | testOrganisation | A Test Source  | vagrant@localhost |
  And there are groups with the following properties:
  | identifier       | permission1             |
  | testOrganisation | PERMISSION_VIEW_SOURCE  |
  And there are users with the following properties:
  | identifier        | accountName | password  | group1           |
  | test@example.com  | Test User   | Poa annua | testOrganisation |
  | admin@example.com | Admin       | Poa annua | administrators   |
  And I am not authenticated
  And I am on the portal home page

  
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
  | object           |
  | testOrganisation |
  And I submit the access controls form
  Then an info message should say "READ access to Organisation testOrganisation was added to the group"