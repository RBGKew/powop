---
layout: page
title: "Harvester Usage"
category: content
date: 2016-04-26 16:51:58
---

Default values are in **bold**

| Parameter | Values | Description |
| ----------| ------ | ----------- |
| `description.processing.mode` |
| | **IMPORT_DESCRIPTIONS** | Imports any description data found in archive |
| | SKIP_DESCRIPTIONS       | Skips any description data found in archive |
| `distribution.processing.mode` |
| | **IMPORT_DISTRIBUTIONS** | imports any distribution data found in archive |
| | SKIP_DISTRIBUTIONS       | skips any distribution data found in archive |
| `key.processing.mode` |
| | **SKIP_KEYS**   | skips any identification key data found in archive |
| | IMPORT_KEYS     | imports any identification key data found in archive |
| `image.processing.mode` |
| | **IMPORT_IMAGES** | imports any image data found in archive |
| | SKIP_IMAGES       | skips any image data found in archive |
| `image.server` |
| | [free text]  | Prefix to add to accessUri field when harvesting |
| `measurementOrFact.processing.mode` |
| |  **IMPORT_MEASUREMENT_OR_FACT**  | imports any measurement or fact data found in archive |
| | SKIP_MEASUREMENT_OR_FACT         | skips any measurement or fact data found in archive |
| `taxon.processing.mode` |
| | **SKIP_TAXA**       | skips any name or taxonomy data found in archive |
| | IMPORT_NAMES        | imports any name data found in archive. Does not create any taxonomic links, even if they exist in the data |
| | IMPORT_TAXONOMY     | imports any taxonomy data found in archive. Only creates linkages, names must be harvested first |
| `description.processing.mode` |
| | **IMPORT_DESCRIPTIONS** | imports any description data found in archive |
| | SKIP_DESCRIPTIONS       | skips any description data found in archive |
| `reference.processing.mode` |
| | **IMPORT_REFERENCES** | imports any reference data found in archive |
| | SKIP_REFERENCES       | skips any reference data found in archive |
| `typeAndSpecimen.processing.mode` |
| | **IMPORT_TYPE_AND_SPECIMEN** | imports any type and specimen data found in archive |
| | SKIP_TYPE_AND_SPECIMEN       | skips any type and specimen data found in archive |
| `identification.processing.mode` |
| | **IMPORT_IDENTIFICATION** | imports any identification data found in archive |
| | SKIP_IDENTIFICATION       | skips any identification data found in archive |
| `vernacularName.processing.mode` |
| | **IMPORT_VERNACULAR_NAME** | imports any vernacular name data found in archive |
| | SKIP_VERNACULAR_NAME       | skips any vernacular name data found in archive |


### Images

Image archives are specified using a subset of the [Audobon gbif extension](http://rs.gbif.org/extension/ac/audubon.xml).
Mapped fields are:

| Field | Notes|
| Audience |
| Contributor |
| Creator |
| Description |
| Format |
| Identifier |
| Publisher |
| References |
| Subject |
| Title |
| Type |
| AssociatedObservationReference |
| AssociatedSpecimenReference |
| Caption |
| ProviderManagedID |
| SubjectPart | Uses a controlled vocabulary defined by [...]
| TaxonCoverage | Requires an identifier for an existing taxon
| AccessUri | URI of media to be displayed
| Subtype |
| SubjectCategoryVocabulary |
| Lattitude |
| Longitude |
| Rating |
| WorldRegion |
| CountryCode |
| CountryName |
| ProvinceState |
| Sublocation |
| PixelXDimension |
| PixelYDimension |
