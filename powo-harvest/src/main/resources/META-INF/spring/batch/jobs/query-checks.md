# Query checks

## `from Taxon`

Taxon query

```
select id from Taxon where identifier='urn:lsid:indexfungorum.org:names:808884' limit ?
```

Organisation query

```
select id from Organisation where id=?
```

One to many relationships
```
select synonymnam0_.acceptedNameUsage_id          from Taxon synonymnam0_ where synonymnam0_.acceptedNameUsage_id=?
select descriptio0_.taxon_id as taxon_i18_2_0_    from Description descriptio0_ where descriptio0_.taxon_id=?
select distributi0_.taxon_id as taxon_i18_5_0_    from Distribution distributi0_ where distributi0_.taxon_id=?
select vernacular0_.taxon_id as taxon_i25_27_0_   from VernacularName vernacular0_ where vernacular0_.taxon_id=?
select measuremen0_.taxon_id as taxon_i22_15_0_   from MeasurementOrFact measuremen0_ where measuremen0_.taxon_id=?
```

Many to many relationships
```
select authoritie0_.taxon_id as taxon_id1_23_0_   from taxon_organisation authoritie0_ inner join Organisation organisati1_ on authoritie0_.organisation_id=organisati1_.id where authoritie0_.taxon_id=?
select images0_.Taxon_id as Taxon_id2_22_0_       from Taxon_Image images0_ inner join Image image1_ on images0_.images_id=image1_.id where images0_.Taxon_id=? order by image1_.rating desc
```

## `from Taxon t left join fetch t.authority`

Taxon query - now has `LEFT OUTER JOIN` on `Organisation` to get `authority`

```
select taxon0_.id, organisati1_.id from Taxon taxon0_ left outer join Organisation organisati1_ on taxon0_.authority_id=organisati1_.id where taxon0_.identifier='urn:lsid:indexfungorum.org:names:808884' limit ?
```

Rest still same - if this works I am happy with that as a next step. Even if just the first query works I am happy with that.

Both the above queries work WITHOUT out of memory error when running `hibernateTest.xml` job!

## `from Taxon t left join fetch t.authority join fetch t.acceptedNameUsage`

Doesn't load the right number of taxa! Should be left join!

## `from Taxon t join fetch t.authority join fetch t.vernacularNames`


When I added `join fetch t.vernacularNames` it gave this error and went pretty weird
```
WARN  | poolTaskExecutor-1 | o.h.h.i.a.QueryTranslatorImpl | HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
```

**TOMORROW**

- check total duration of job - 3hr31
- check chunk completion time over duration of job - does increase
- redeploy with updates to query - done
- think about writing a clearing item write listener - done but trying read only
- think about package location of new code - done
- write some documentation
- remove old code - later MR after merged

# Old MR comment

I investigated a few different alternative implementations to try and get around this:

### Load everything as part of the `ItemReader`

I tried a couple of options to do this.

#### Custom HQL (Hibernate Query Language)

I tried changing the query from `select t.id from Taxon t` to something that would return an entity e.g. `select t from Taxon t join fetch t.authority`. This works OK for that relationship but because there are many to many and many to one relationships it's not possible to fetch all of the downstream objects in one query because it requires outer joins (and there is enough data for us to immediately run out of heap space).

#### Use JPA `EntityGraph`s

I tried using JPA and it's `EntityGraph` spec which I understand a bit better than HQL. This involved changing to a `JpaPagingItemReader` (which was surprisingly not that difficult to setup). However this too ran into issues with outer joins trying to load too much data.

### Add an `ItemProcessor`

The idea behind this is that if we have to use this approach of loading lots of information from the taxon, at least we could extract this logic from the `ItemWriter` so the flow of data in the code is clearer and the `ItemWriter` is simpler. This would look like:

```
reader                     processor                             writer
loads pages of taxon_id -> converts to TaxonSolrInputDocument -> uploads to Solr in batches
(or even Taxon entity)
```

This is an improvement in terms of readability however I encountered a serious issue while trying to get this working which was that I couldn't seem to get the `processor` to run properly in the context of a session. Ideally we'd want it to be the same session as the `reader`.

I also tried using our `TaxonDao` and one of our existing fetch profiles (`taxon-page`) to load the entity by ID. I found in testing this was actually slower - perhaps because the fetch profile was loading entities that we did not need. However given that it seemed slower and doesn't really address the issue of fetch loading I decided against exploring that further.

Another option could be to write a new `ItemReader` that would delegate to the standard `HibernatePagingItemReader` but would initialise properties before passing them on. The only problem with this is that new properties that are added to the `Taxon` will not be available for indexing unless this is updated - we saw this happen recently with the addition of `authorities`.

### Index chunks concurrently

One of my first thoughts to improve performance was to configure the job to run the chunks in this step concurrently. We already have a `taskExecutor` defined and to enable concurrent execution on a chunk based step I added a `task-executor="..."` to our configuration. I also needed to change our `ItemReader` from `HibernateCursorItemReader` to `HibernatePagingItemReader` because the former is not thread safe.

