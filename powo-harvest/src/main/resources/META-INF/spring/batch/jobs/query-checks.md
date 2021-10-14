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