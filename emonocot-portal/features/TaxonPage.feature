Feature: Taxon Page Feature
In order to ensure that Taxon pages have italic titles
I want to check the title of a taxon page.

Scenario: Taxon Title Scenario
Given There is a taxon with id 2295 and name 'Acorus'
When I navigate to the page
Then The page title should be 'Acorus'
And The title class should be taxonName