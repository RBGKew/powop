---
layout: page
title: Names and Taxa
---

### Description

Names form the backbone to which all other records and information are attached. For any
data to be harvested the name that it will be attached to must already exist. Thus, the
names must be harvested first.

To harvest a names archive, set `taxon.processing.mode = IMPORT_NAMES`.

Taxonomy can be imported after names have been harvested by setting
`taxon.processing.mode = IMPORT_TAXONOMY`.  This will import the taxonomic links and
ignore any other data found in the taxon data table.

### Datatype

[Darwin Core Terms: Taxon](http://tdwg.github.io/dwc/terms/index.htm#Taxon)

### Harvester Parameters

Default is in **bold**

`taxon.processing.mode`

* `IMPORT_NAMES`
* `IMPORT_TAXONOMY`
* **`SKIP_TAXA`**
