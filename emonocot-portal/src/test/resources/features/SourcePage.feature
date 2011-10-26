Feature: Source Page
In order to ensure that the source page is correct, open a source page and check that
there is the source name and the link to the source
http://build.e-monocot.org/bugzilla/show_bug.cgi?id=76

Scenario: Check SourcePage
Given there are source systems with the following properties:
| identifier | uri                 |
| test       | http://example.com  |
When I navigate to source page "test"  
Then the source name should be "test"
And there should be a link to "http://example.com/"
