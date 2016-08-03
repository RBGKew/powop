---
layout: page
title: "Legacy eMonocot docs"
---

A few bits of documentation from eMonocot that could still be useful but which are
mostly out of date

# Spatial Data

 * The TDWG Shapefiles are available here: http://www.kew.org/gis/tdwg/

 * These are ESRI Shapefiles, which we can use in e.g. ArcIMS, but if we want
 to integrate this data in eMonocot, we might need to transform it into another
 format. [http://www.gdal.org/](GDAL) is useful in this respect

 * We're using the latest version of Solr (4.0) to index these shapes, which has
 a recursive prefix tree field type which can index polygons (and query using
 polygons). See [http://wiki.apache.org/solr/SolrAdaptersForLuceneSpatial4](the
 Solr wiki) for more information on this capability.

 * The recursive prefix field type doesn't cope well with shapes which span the
 date-line. Fortunately, we can split geometries on the dateline (provided that we
 have a new version of the GEOS library) using ogr2ogr thus

```shell
ogr2ogr -t_srs WGS84 -overwrite -clipdst -180 -90 180 90 -wrapdateline shapefile shapefile
```

You can import the layers into mysql thus:

```shell
ogr2ogr -f MySQL MySQL:gis,user=user,password=passwd shapefile.shp  -nln tablename -update -overwrite -lco GEOMETRY_NAME=SHAPE
```

The tab delimited files in emonocot/emonocot-model/src/main/resources/org/emonocot/model/level\*.txt were created thus

```sql
> select concat('Location_',level1_cod),level1_cod,level1_nam,AsText(SHAPE) from level1 into outfile '/tmp/level1.txt' fields terminated by '\t' lines terminated by '\n';
> select concat('Location_',level2_cod),level2_cod,level2_nam,AsText(SHAPE) from level2 into outfile '/tmp/level2.txt' fields terminated by '\t' lines terminated by '\n';
> select concat('Location_',level3_cod),level3_cod,level3_nam,AsText(SHAPE) from level3 into outfile '/tmp/level3.txt' fields terminated by '\t' lines terminated by '\n';
> select concat('Location_',level4_cod),level3_cod,level_4_na,AsText(SHAPE) from level4 into outfile '/tmp/level4.txt' fields terminated by '\t' lines terminated by '\n';
```

This was then loaded into Solr thus:

   Split into one record per file (to detect errors more easily)

```shell
for i in 1 2 3 4; do
    split -l 1 -d -a3 level$i.txt level$i-;
done
```

   Upload each file in a separate request, log failures:

```shell
for i in level?-???; do
    curl -H 'Content-type:text/csv; charset=utf-8' 'http://localhost:8983/solr/update/csv?commit=true&separator=%09&stream.file=/tmp/'$i'&fieldnames=id,location.tdwg_code_s,location.name_s,geo' | \
    grep --silent code && touch $i.error;
done
```

 * NB: I found that level 1 continents could not be easily loaded into Solr, so these have been left out totally. I also found that pre-simplfying the polygons using
   com.vividsolutions.jts.simplify.TopologyPreservingSimplifier.simplify(geom, 0.01) made it more likely that some of the polygons could be imported.

 * NB2: Some polygons don't load, including Antarctica ("found non-noded intersection between LINESTRING …") and some regions with large archipelagos ("Ring Self-intersection at or near point").

 * The documentation for the spatial extensions in MySQL is here: http://dev.mysql.com/doc/refman/5.0/en/spatial-extensions.html

 * You need to remember that MySQL works on Minimum Bounding Rectangles, not proper spatial queries

```sql
> set @europe = (select SHAPE from level1 where OGR_FID = 1);
> select tdwg_code,region_nam from level2 where Contains(@europe,SHAPE);

+-----------+---------------------+
| tdwg_code | region_nam          |
+-----------+---------------------+
|        10 | Northern Europe     |
|        11 | Middle Europe       |
|        12 | Southwestern Europe |
|        14 | Eastern Europe      |
|        33 | Caucasus            |
|        13 | Southeastern Europe |
+-----------+---------------------+
```

# Support Tasks

_This section needs reviewing, the information may be out of date._

There are two recurring support tasks supported by the eMonocot codebase. They allow support to create Darwin
Core Archives from the PalmWeb and Grassbase datasets for import into the eMonocot Portal. Both are Spring Batch
Jobs and are configured to be run using the CommandLineJobRunner provided by Spring Batch. As part of the build,
an executable JAR file which contains all of the required dependencies is created, and with the main class property
of the JAR  manifest set as CommandLineJobRunner. This means that these tasks can be accomplished by using the jar
on the commandline.

### PalmWeb

This task relies on an up-to-date copy of the PalmWeb database (MySQL) which is hosted by the BGBM in Berlin.
[http://www.bgbm.org/bgbm/staff/Wiss/Kohlbecker/](Andreas Kohlbecker) or
[http://www.bgbm.org/BGBM/staff/Wiss/Mueller/](Andreas Müller) can provide this information,
[mailto:w.baker@kew.org](Bill Baker) is the owner.

The job relies on a network accessible database, I suggest that you import the database into a local MySQL server.
Once the data is imported into a new database, run the following commands on it (otherwise you will be JOIN-ing rows
using columns which are not indexed, which is S-L-O-W).

```sql
create index OriginalSourceBase_SourcedObject_id_IDX on originalsourcebase (sourcedObj_id);
create index OriginalSourceBase_SourcedObject_type_IDX on originalsourcebase (sourcedObj_type);
```

Then run the following command (requires java 7). The parameters which can be supplied on the commandline using
standard java arguments (-Dargument=value) are

| Name                       | Description                                                                                                                                                                       |
|----------------------------|------------------------|
| jdbc.driver.classname      | The driver class name (optional, defaults to com.mysql.jdbc.Driver)                                                                                                               |
| jdbc.driver.url            | The JDBC url of the palmweb database (optional, defaults to jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useCursorFetch=true&useServerPrepStmts=true) |
| jdbc.driver.username       | The JDBC url of the palmweb database (optional, defaults to root)                                                                                                                 |
| jdbc.driver.password       | The JDBC url of the palmweb database (optional, defaults to no password)                                                                                                          |

There are a number of parameters, found in emonocot-harvester/src/main/assembly/META-INF/spring/palmweb.default.properties which set values
in the meta.xml or eml.xml file of the generated archive. These can be more easily understood by looking at the job itself (emonocot-harvest/src/main/resources/META-INF/spring/batch/jobs/palmweb.xml).
[http://static.springsource.org/spring-batch/apidocs/org/springframework/batch/core/launch/support/CommandLineJobRunner.html](CommandLineJobRunner)
has two required arguments, the location of the application context file and the name of the job to be run. The PalmWeb job can thus be run:

```shell
>java -Djdbc.driver.url=jdbc:mysql://localhost:3306/palmweb -jar emonocot-harvest-jar-with-dependencies.jar classpath:org/emonocot/job/palmweb/palmweb.xml PalmWeb
```

This generates the palmweb archive which can made available for harvesting by putting it somewhere on the network.

### Grassbase

This task relies on an up-to-date copy of the Grassbase dataset (DELTA) which is found on the T: drive in the Grasses folder.
[mailto:m.vorontsova@kew.org](Maria Vorontsova) can provide this information, [mailto:d.simpson@kew.org](Dave Simpson) is the owner.

This task relies on a number of files from the DELTA dataset, ones which are most likely to be updated are ITEMS and geitems, the two files which
contain the coded descriptions. Other files should be replaced by the files in ./emonocot-harvest/src/test/resources/org/kew/grassbase unless new
characters or states have been added to the dataset, or if character specifications have been changed, in which case they may need to be updated.
Maria is probably the best person to talk to about this.

This task also requires two other files (species.taxon.file and genera.taxon.file) which contain a list of species and genera in the Poaceae. These files must have at
least two columns, one being the identifier of the taxon and the other being the scientificName of the taxon. They may contain extra columns, which will be ignored.
These files should be downloaded from the eMonocot Portal. I suggest that non-neccessary columns should be deleted using a spreadsheet program such as Excel.

Finally, a file containing the list of images associated with the grassbase data (images.file) is copied into the archive without modifying it. It should have two columns, the first being
the taxonID that the image is associated with, and the second being the identifier of the image (the full uri of the image file). This file was prepared by one of the content team based on
a list of images provided by Maria. If the images have not been altered, this file does not need to be altered.

Examples of these files can be found in emonocot-harvest/src/test/resources/org/kew/grassbase.

The parameters which can be supplied on the commandline as spring batch parameters (after the two required arguments as argument=value) are:

| Name               | Description                                                           |
|--------------------|-----------------------------------------------------------------------|
| species.specs.file | The DELTA SPECS file for the species data (TONAT)                     |
| species.items.file | The DELTA ITEMS file for the species data (ITEMS)                     |
| species.taxon.file | The file of grass species names and identifiers (species.txt)         |
| genera.specs.file  | The DELTA SPECS file for the species data (GTONATH)                   |
| genera.items.file  | The DELTA ITEMS file for the species data (geitems)                   |
| genera.taxon.file  | The file of grass genera names and identifiers (genera.txt)           |
| images.file        | The file of grass image identifier and taxon identifiers (images.txt) |

There are a number of optional parameters, found in emonocot-harvester/src/main/assembly/META-INF/spring/grassbase.default.properties which set values
in the meta.xml or eml.xml file of the generated archive. These can be more easily understood by looking at the job itself (emonocot-harvest/src/main/resources/META-INF/spring/batch/jobs/grassbase.xml).
The Grassbase job can thus be run:

```shell
>java -jar target\emonocot-harvest-jar-with-dependencies.jar classpath:org/emonocot/job/grassbase/grassbase.xml Grassbase species.specs.file=TONAT species.items.file=ITEMS species.taxon.file=species.txt genera.specs.file=GTONATH genera.items.file=geitems genera.taxon.file=genera.txt images.file=images.txt
```
This generates the grassbase archive which can made available for harvesting by putting it somewhere on the network.
