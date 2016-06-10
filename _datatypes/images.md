---
layout: page
title: Images
---

### Description


### Datatype

Combination of a subset of [Audobon](http://rs.gbif.org/extension/ac/audubon.xml)
and [Simple Multimedia](http://rs.gbif.org/extension/gbif/1.0/multimedia.xml) GBIF
extensions.

Mapped fields are:

| Field                            | Notes                                           |
| -------------------------------- | ----------------------------------------------- |
| Audience                         |
| Contributor                      |
| Creator                          |
| Description                      |
| Format                           |
| Identifier                       |
| Publisher                        |
| References                       |
| Subject                          |
| Title                            |
| Type                             |
| AssociatedObservationReference   |
| AssociatedSpecimenReference      |
| Caption                          |
| ProviderManagedID                |
| SubjectPart                      | Uses a controlled vocabulary defined by [...]
| TaxonCoverage                    | Requires an identifier for an existing taxon
| AccessUri                        | URI of media to be displayed
| Subtype                          |
| SubjectCategoryVocabulary        |
| Lattitude                        |
| Longitude                        |
| Rating                           |
| WorldRegion                      |
| CountryCode                      |
| CountryName                      |
| ProvinceState                    |
| Sublocation                      |
| PixelXDimension                  |
| PixelYDimension                  |

### Harvester Parameters

Default is in **bold**

`image.processing.mode`

* **`IMPORT_IMAGES`**
* `SKIP_IMAGES`

`image.server`

* Base url of images. Prepended to `AccessUri`
