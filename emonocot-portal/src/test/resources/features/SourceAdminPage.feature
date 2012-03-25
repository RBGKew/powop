Feature: Source Admin Page
  As an administrator of a source  system
  I would like to be able to view details of the data which eMonocot
  has harvested from my system and to be able to detect errors in
  the data provided by my system and differences between the checklist
  used by my system and the checklist used by eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=76

Background:
  Given there are source systems with the following properties:
  | identifier | uri                 | title        |
  | test       | http://example.com  | test title   |
  And there are groups with the following properties:
  | identifier    | permission1             |
  | test          | PERMISSION_VIEW_SOURCE  |
  And there are the following access controls:
  | principal | principalType | object | objectType | permission |
  | test      | group         | test   | Source     | READ       |
  And there are users with the following properties:
  | identifier          | password       | group1   |
  | test@example.com    | Poa annua      | test     |
  | admin@e-monocot.org | Nardus stricta | test     |
  And there are job instances with the following properties:
  | jobId | jobName | authorityName | version |
  | 1     | testJob | test          | 1       |
  And there are job executions with the following properties:
  | jobId | jobInstance | createTime               | startTime                | endTime                  | status    | exitCode  | exitMessage                     |
  | 1     | 1           | 2011-11-11T12:00:01.000Z | 2011-11-11T13:00:01.000Z | 2011-11-11T13:34:01.000Z | COMPLETED | COMPLETED | This job completed successfully |
  And there are references with the following properties:
  | identifier                    | title    | datePublished | volume | page  | 
  | urn:kew.org:wcs:publication:1 | Sp. Pl.  | (1753)        | 1      | 304pp |
  And there are images with the following properties:
  | identifier | url                                                                                                                              | caption                       |
  | 123        | http://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Acorus_calamus1.jpg/450px-Acorus_calamus1.jpg                           | Acorus calamus                |
  | 456        | http://upload.wikimedia.org/wikipedia/commons/thumb/2/25/Illustration_Acorus_calamus0.jpg/376px-Illustration_Acorus_calamus0.jpg | Acorus calamus (Illustration) |
  | 789        | http://upload.wikimedia.org/wikipedia/commons/7/73/Acorus_calamus_illustration.jpg                                               | Sweet flag (drawing)          |
  And there are taxa with the following properties:
  | identifier                 | name                           |
  | urn:kew.org:wcs:taxon:2295 | Acorus                         |
  | urn:kew.org:wcs:taxon:2304 | Acorus calamus                 |
  | urn:kew.org:wcs:taxon:2306 | Acorus calamus var. angustatus |
  | urn:kew.org:wcs:taxon:2305 | Acorus calamus var. americanus |
  And there are annotations with the following properties:
  | identifier | code      | type  | recordType | jobId | source | text                                                                             | object                     |
  | 1          | BadField  | Warn  | Taxon      | 1     | test   | Could not find identifier for relationship of taxon urn:kew.org:wcs:taxon:224934 |                            |                                      |
  | 2          | Update    | Info  | Taxon      | 1     | test   |                                                                                  | urn:kew.org:wcs:taxon:2295 |
  | 3          | Create    | Info  | Taxon      | 1     | test   |                                                                                  | urn:kew.org:wcs:taxon:2304 |
  | 4          | BadRecord | Error | Taxon      | 1     | test   |                                                                                  | urn:kew.org:wcs:taxon:2306 |


Scenario: Check SourcePage
  The source page should contain a list of jobs run on that source.
  Selecting a job should show the numbers of records harvested by that job broken down by data type
  of data. Selecting a type of data should show the number of records broken down by category of message. 
  Selecting a category of message should display the list of messages.
  Given I am logged in as "test@example.com" with the password "Poa annua"
  When I navigate to source page "test"  
  Then there should be 1 jobs listed
  When I select the 1st job
  Then the summary results should be as follows:
  | label | value |
  | Taxa  | 3     |
  When I select the job category "Taxa"
  Then the summary results should be as follows:
  | label | value |
  | Error | 1     |
  | Info  | 2     |
  | Warn  | 1     |

Scenario: Create Source
  As an administrator, in order to improve eMonocot, I want to be able
  to register new source systems so that they can be harvested and their
  content made available in the emonocot portal
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=44
  When I am on the source admin page
  And I select "Create a new source"
  And I enter the following data into the create source form:
  | identifier | title | uri | logoUrl |
  | test2 | test2 title | http://example.com | http://example.com/logo.png |
  And I submit the create source form
  Then an info message should say "test2 title created"
  When I navigate to source page "test2"
  Then the source uri should be "http://example.com"
  And the source logo should be "http://example.com/logo.png"

Scenario: Edit Source
  As a privileged source system owner, I want to be able to edit a source page
  So that the eMonocot portal displays information about my system correctly
  Given I am logged in as "test@example.com" with the password "Poa annua"
  When I navigate to source page "test" 
  And I select "Edit this source"
  And I enter the following data into the update source form:
  | uri                | logoUrl                     | title      |
  | http://example.com | http://example.com/logo.png | test title |
  And I submit the update source form
  Then an info message should say "test title was updated"
  When I navigate to source page "test" 
  Then the source uri should be "http://example.com"
  And the source logo should be "http://example.com/logo.png"

 
Scenario: Create Job
  As an eMonocot Portal administrator, I would like to list and create harvesting
  jobs for a given source, so that data from that source can be harvested.
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=240
  Given I am logged in as "admin@e-monocot.org" with the password "Nardus stricta"
  And I am on the source admin page for "test"
  And I select "Create a new job"
  And I enter the following data in the job form:
  | identifier | family    | uri                                  | jobType     |
  | New Job    | Testaceae | http://www.testaceae.org/archive.zip | DwC_Archive |
  And I submit the create job form
  Then an info message should say "New Job was created"
  When I am on the source admin page for "test"
  Then there should be 1 job
  
