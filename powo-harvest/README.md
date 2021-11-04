# POWO Harvest

This module holds the logic that takes data from a set of Darwin Core Archives and ingests it into POWO - referred to as harvesting.

## Core jobs

At it's core the harvesting involves running Spring Batch jobs - `DarwinCoreArchiveHarvesting` and `Reindex`.

### `DarwinCoreArchiveHarvesting` job

The `DarwinCoreArchiveHarvesting` job downloads an archive specified in the job parameters, extracts it, and then harvests the data. What is harvested depends on:

1. What files the DWCA contains (e.g. an archive may not contain any distributions)
2. What the job parameters are (e.g. the job can specify to import only the names and not the taxonomy)

> Configuration for the job can be found at [powo-harvest/src/main/resources/META-INF/spring/batch/jobs/darwinCoreArchiveHarvesting.xml](./src/main/resources/META-INF/spring/batch/jobs/darwinCoreArchiveHarvesting.xml)

### `Reindex` job

The `Reindex` job uses the data from the database to populate a Solr index.

> Configuration for the job can be found at [powo-harvest/src/main/resources/META-INF/spring/batch/jobs/reindex.xml](./src/main/resources/META-INF/spring/batch/jobs/reindex.xml)

## Managing jobs

What jobs are run and in which order is determined by the `JobConfiguration` and `JobList` entities in the database. This is most often loaded at harvest time by the `powo-builder` but can also be managed on the dashboard at `localhost:10081/admin/#/jobs`.

## Performance

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

### Known performance issues

There is a memory leak when processing references where `Taxon` objects are not properly released. This happens especially when processing references for the `WCS-Distributions` archive and is high priority to resolve. [VisualVM](https://visualvm.github.io/) is a useful tool for debugging memory leaks.
