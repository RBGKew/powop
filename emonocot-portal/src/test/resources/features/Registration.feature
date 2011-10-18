Feature: Registration
In order to ensure that Users cannot access restricted areas, 
check that they cannot access a restricted page and check that they
can register new accounts
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=36

Scenario: Deny access to unauthenticated user
Given there are groups with the following properties:
| identifier       | permission1            |
| PalmWeb          | PERMISSION_VIEW_SOURCE |
And there are users with the following properties:
| identifier       | password  | group1  |
| test@example.com | Poa annua | PalmWeb |
When I am not authenticated
And I navigate to source admin page for "PalmWeb"  
Then the login page should be displayed

Scenario: Registration
When I open the portal home page
And I select the registration link
And I enter the following data into the registration form:
| username         | repeatUsername   | password | repeatPassword |
| test@example.com | test@example.com | test1234 | test1234       |
And I submit the registration form
Then my profile page should be displayed