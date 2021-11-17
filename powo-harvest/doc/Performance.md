# Performance

The harvester is responsible for ingesting a large volume of data containing millions of taxa, references, distributions etc. Therefore there are several performance optimisations that are important for ensuring that the harvester.

1. Enabling JDBC Batching in `` - this allows JDBC to reuse insert/update statements efficiently.

```
# src/main/resources/META-INF/spring/applicationContext.xml
hibernate.jdbc.batch_size=5000
hibernate.order_inserts=true
hibernate.order_updates=true
```

2. Using custom sequences for high volume tables e.g. `seq_taxon`. This means fewer round trips to the DB to get identifiers AND without using sequences JDBC batching is silently disabled.

3. Fetching relations where possible and where it's a performance improvement e.g. in the `reindex` job fetching organisations with each taxon - see `ReindexTaxaQueryProvider.java`.

4. Using readonly queries where possible - see `ReindexTaxaQueryProvider.java`

5. Indexing in a batch at the end of the harvesting rather than throughout

The logger configuration in `src/main/resources/logback.xml` can help with seeing what SQL is executed and debugging.

## Known performance issues

There is a memory leak when processing references where `Taxon` objects are not properly released. This happens especially when processing references for the `WCS-Distributions` archive and is high priority to resolve. [VisualVM](https://visualvm.github.io/) is a useful tool for debugging memory leaks.

## Helpful SQL queries

For assessing bottlenecks in the performance it can be helpful to review how long the different resources / jobs / steps take. To check this you can port forwad to an existing database that has completed the harvesting steps:

```
kubectl port-forward $POD_NAME 3307:3306 -n $NAMESPACE
```

> The pod name and namespace will depend on when you check it as the deployment is rebuilt each week. You can get the namespace of the latest deployment using `helm ls`

To see a break down of the harvesting by resource, job and step you can use the following query:

```sql
SELECT
  p.STRING_VAL AS resource_identifier,
  ji.JOB_NAME AS job_name,
  TIMEDIFF(j.START_TIME, j.END_TIME) AS job_time,
  ABS(TIME_TO_SEC(TIMEDIFF(j.START_TIME, j.END_TIME))) AS job_time_seconds,
  s.STEP_NAME AS step_name,
  TIMEDIFF(s.START_TIME, s.END_TIME) AS step_time,
  ABS(TIME_TO_SEC(TIMEDIFF(s.START_TIME, s.END_TIME))) AS step_time_seconds
FROM batch_step_execution s
JOIN batch_job_execution j ON s.JOB_EXECUTION_ID = j.JOB_EXECUTION_ID
JOIN batch_job_instance ji ON j.JOB_INSTANCE_ID = ji.JOB_INSTANCE_ID
JOIN batch_job_execution_params p ON p.JOB_EXECUTION_ID = j.JOB_EXECUTION_ID AND p.KEY_NAME = "resource.identifier"
ORDER BY TIMEDIFF(j.END_TIME, j.START_TIME) DESC, TIMEDIFF(s.END_TIME, s.START_TIME) DESC;
```

To see the total time in hours spent running jobs (assuming there is only one run of each job):

```sql
SELECT SUM(ABS(TIME_TO_SEC(TIMEDIFF(j.START_TIME, j.END_TIME)))) / 60 / 60 AS job_time
FROM batch_job_execution j;
```

## Testing performance changes

Because of the volume of data and number of moving parts testing performance changes can be quite involved. It also depends on what you are testing. I recommend the following stages for tests that update the database:

1. Test using an integration test and verify that the changes (e.g. fewer SQL statements) are happening as you expect - see `DarwinCoreArchiveHarvestingJobFunctionalTest.java` for examples of this
2. If you are unsure whether there might be changes in behaviour then test locally using `docker compose` - to ensure behaviour remains the same
3. Test on UAT cluster using minimal deployment and a minimal set of jobs. This stage is important to test the changes on the infrastructure / OS that the application actually runs on. You want to run enough jobs to see if there is an improvement but not all jobs because that can take ages!
4. Test on UAT cluster using minimal deployment and the full workload. This stage gives a better understanding of how the changes affect the whole harvesting process.

> See `powop-infrastructure/powo/values-harvest-test.yaml` for a minimal deployment that can be spun up without disrupting UAT

If these stages have shown an overall improvement the changes should be good to go to production once POWO functionality has been tested to ensure everything is working properly.
