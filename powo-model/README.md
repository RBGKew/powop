# POWO Model

This module holds the core data model for the POWO project including entity mappings and service definitions.

## Data

The main entity model revolves around `Taxon` which maps to a taxon in a Darwin Core Archive. All data loaded from DWCA is linked to a taxon.

An `Organisation` contributes data to a `Taxon` via the `Resource` it uploads. This data is ingested in the harvesting process that is run by the `powo-harvest` module.

We also have the concept of a `JobConfiguration` (and related `JobList`) which represents the required information to ingest a resource, including:
- the organisation it is from
- the url location of the resource
- parameters indicating how to ingest the resource

See [Data Mapping](./doc/DataMapping.md) for fine detailed information about what Darwin Core terms map to entity fields.
