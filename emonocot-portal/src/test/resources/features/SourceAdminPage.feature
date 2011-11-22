Feature: Source Admin Page
  As an administrator of a source  system
  I would like to be able to view details of the data which eMonocot
  has harvested from my system and to be able to detect errors in
  the data provided by my system and differences between the checklist
  used by my system and the checklist used by eMonocot
  http://build.e-monocot.org/bugzilla/show_bug.cgi?id=76

Background:
  Given there are source systems with the following properties:
  | identifier | uri                 |
  | test       | http://example.com  |
  And there are groups with the following properties:
  | identifier    | permission1            |
  | test          | PERMISSION_VIEW_SOURCE |
  And there are users with the following properties:
  | identifier       | password  | group1 |
  | test@example.com | Poa annua | test   |
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
  The source admin page should contain a list of jobs run on that source.
  Selecting a job should show the numbers of records harvested by that job broken down by data type
  of data. Selecting a type of data should show the number of records broken down by category of message. 
  Selecting a category of message should display the list of messages.
  Given I am logged in as "test@example.com" with the password "Poa annua"
  When I navigate to source admin page for "test"  
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
