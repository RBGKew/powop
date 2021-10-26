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
