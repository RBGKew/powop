SET NAMES utf8;
# Update the taxa with the identifiers from the checklist
#
update taxonbase as taxon join taxonnamebase as name on (taxon.name_id = name.id) left join originalsourcebase as source on (name.id = source.sourcedObj_id AND source.sourcedObj_type = 'eu.etaxonomy.cdm.model.name.BotanicalName') set taxon.lsid_lsid = if(source.idinsource IS NULL, NULL, concat('urn:kew.org:wcs:taxon:',substr(source.idinsource,9)));

update taxonbase as taxon join originalsourcebase as source on (taxon.id = source.sourcedObj_id AND source.sourcedObj_type = 'eu.etaxonomy.cdm.model.taxon.Taxon') set taxon.lsid_lsid = if(source.idinsource IS NULL, NULL, concat('urn:kew.org:wcs:taxon:',substr(source.idinsource,9))) where taxon.lsid_lsid is NULL;

Query OK, 6312 rows affected (22 min 39.47 sec)
Rows matched: 8481  Changed: 6312  Warnings: 


select IFNULL(taxon.lsid_lsid,''), IFNULL(name.nameCache,''), IFNULL(name.authorshipCache,''), IFNULL(lower(rank.titleCache),''), IF(taxon.DTYPE = 'Taxon', 'accepted','synonym'), IFNULL(accepted.lsid_lsid,''), IFNULL(name.genusOrUninomial,''), IFNULL(name.infraGenericEpithet,''), IFNULL(name.specificEpithet,''), IFNULL(name.infraSpecificEpithet,''), concat('http://www.palmweb.org/?q=cdm_dataportal/taxon/',taxon.uuid) AS source, IF(taxon.updated is NULL, DATE_FORMAT(taxon.created,"%Y-%m-%dT%TZ"),DATE_FORMAT(taxon.updated,"%Y-%m-%dT%TZ")) as updated,DATE_FORMAT(taxon.created,"%Y-%m-%dT%TZ") as created,IFNULL(parent.lsid_lsid,'') as parent_id, IFNULL(IF(name.nomenclaturalmicroreference IS NULL,reference.titleCache,concat(reference.titleCache," ", name.nomenclaturalmicroreference)),'') as citation, IFNULL(IF(reference.uuid IS NULL,NULL,concat('http://www.palmweb.org/q=cdm_dataportal/reference/',reference.uuid)),'') as citationId, if(status.type_id IS NULL,'', IF(status.type_id=848,'nom. illeg.',IF(status.type_id=834,'nom. invalid.',IF(status.type_id=831,'nom. provis.',IF(status.type_id=833,'nom. nudum.','nom. subnud.'))))) as status into outfile 'c:\\temp\\taxon.txt' FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n'  from taxonbase as taxon join taxonnamebase as name on (taxon.name_id = name.id) join DefinedTermBase as rank on (name.rank_id = rank.id)  left join synonymrelationship on (taxon.id = synonymrelationship.relatedfrom_id) left join taxonbase as accepted on (accepted.id = synonymrelationship.relatedto_id) left join taxonnode as node on (taxon.id = node.taxon_id) left join taxonnode as parentnode on (node.parent_id = parentnode.id) left join taxonbase as parent on (parentnode.taxon_id = parent.id) join reference on (name.nomenclaturalreference_id = reference.id) left join taxonnamebase_nomenclaturalstatus as name_status on (name.id = name_status.TaxonNameBase_id) left join nomenclaturalstatus as status on (name_status.status_id = status.id);



select IFNULL(taxon.lsid_lsid,''), CASE feature_id WHEN 937 THEN 'morphology' WHEN 936 THEN 'biology' WHEN 924 THEN 'vernacular' WHEN 945 THEN 'conservation' WHEN 934 THEN 'general' WHEN 929 THEN 'diagnostic' WHEN  940 THEN 'distribution' WHEN 2075 THEN 'habitat' WHEN 925 THEN 'use' END as type, concat('http://www.palmweb.org/?q=cdm_dataportal/taxon/',taxon.uuid) AS source, IF(element.updated is NULL, DATE_FORMAT(element.created,"%Y-%m-%dT%TZ"),DATE_FORMAT(element.updated,"%Y-%m-%dT%TZ")) as updated, DATE_FORMAT(element.created,"%Y-%m-%dT%TZ") as created, IFNULL(user.username,''), "CC BY-NC-SA 3.0", "EN", IFNULL(string.text,' '), IFNULL(IF(source.citationMicroReference IS NULL,reference.titleCache,concat(reference.titleCache,' ',source.citationMicroReference)),''), IFNULL(IF(reference.uuid IS NULL,NULL,concat('http://www.palmweb.org/?q=cdm_dataportal/reference/'
,reference.uuid)),'') INTO OUTFILE 'c:\\temp\\description.tsv' FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' from descriptionelementbase as element join descriptionbase as description on (element.indescription_id = description.id) join taxonbase as taxon on (description.taxon_id = taxon.id) left join useraccount as user on (user.id = element.createdBy_id) join descriptionelementbase_languagestring as deb_ls on (element.id = deb_ls.DescriptionElementBase_id) join languagestring as string on (deb_ls.multilanguagetext_id = string.id) left join originalsourcebase as source on (element.id = source.sourcedObj_id AND source.sourcedObj_type = "eu.etaxonomy.cdm.model.description.TextData") left join Reference as reference on (source.citation_id = reference.id) where feature_id in (937,924,936,945,934,929,940,2075,925);

create temporary table description select element.id as id, IFNULL(taxon.lsid_lsid,'') AS lsid, CASE feature_id WHEN 937 THEN 'morphology' WHEN 936 THEN 'biology' WHEN 924 THEN 'vernacular' WHEN 945 THEN 'conservation' WHEN 934 THEN 'general' WHEN 929 THEN 'diagnostic' WHEN  940 THEN 'distribution' WHEN 2075 THEN 'habitat' WHEN 925 THEN 'use' END as type, concat('http://www.palmweb.org/?q=cdm_dataportal/taxon/',taxon.uuid) AS source, IF(element.updated is NULL, DATE_FORMAT(element.created,"%Y-%m-%dT%TZ"),DATE_FORMAT(element.updated,"%Y-%m-%dT%TZ")) as updated, DATE_FORMAT(element.created,"%Y-%m-%dT%TZ") as created, IFNULL(user.username,'') as creator, "CC BY-NC-SA 3.0" as license, "EN" as language, IFNULL(string.text,'') as text, IFNULL(IF(source.citationMicroReference IS NULL,reference.titleCache,concat(reference.titleCache,' ',source.citationMicroReference)),'') as citation, IFNULL(IF(reference.uuid IS NULL,NULL,concat('http://www.palmweb.org/?q=cdm_dataportal/reference/',reference.uuid)),'') as citationId from descriptionelementbase as element join descriptionbase as description on (element.indescription_id = description.id) join taxonbase as taxon on (description.taxon_id = taxon.id) left join useraccount as user on (user.id = element.createdBy_id) join descriptionelementbase_languagestring as deb_ls on (element.id = deb_ls.DescriptionElementBase_id) join languagestring as string on (deb_ls.multilanguagetext_id = string.id) left join originalsourcebase as source on (element.id = source.sourcedObj_id AND source.sourcedObj_type = "eu.etaxonomy.cdm.model.description.TextData") left join Reference as reference on (source.citation_id = reference.id) where feature_id in (937,924,936,945,934,929,940,2075,925);

select lsid, type, source, updated, created, creator,text, group_concat(citation,","), group_concat(citationId,",") INTO OUTFILE 'c:\\temp\\description.txt' FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' from description group by id;



select IFNULL(taxon.lsid_lsid,''), IFNULL(part.uri,''), IFNULL(media.titleCache,''), IFNULL(agent.titleCache,''), IFNULL(IF(media.updated is NULL, DATE_FORMAT(media.created,"%Y-%m-%dT%TZ"),DATE_FORMAT(media.updated,"%Y-%m-%dT%TZ")),'') as updated, IFNULL(DATE_FORMAT(media.created,"%Y-%m-%dT%TZ"),'') as created INTO OUTFILE 'c:\\temp\\images.txt' FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' from media join mediarepresentation as rep on (media.id = rep.media_id) join mediarepresentationpart as part on (rep.id = part.representation_id)left join media_rights on (media.id = media_rights.Media_id) left join rights on (media_rights.rights_id = rights.id) left join AgentBase as agent on (rights.agent_id = Agent.id) left join descriptionelementbase_media as deb_media on (media.id = deb_media.media_id) left join descriptionelementbase as element on (element.id = deb_media.DescriptionElementBase_id) left join DescriptionBase as description on (element.indescription_id = description.id) left join taxonbase as taxon on (description.taxon_id = taxon.id);

select IFNULL(taxon.lsid_lsid,'') as id, IFNULL(IF(reference.uuid IS NULL,NULL,concat('http://www.palmweb.org/?q=cdm_dataportal/reference/',reference.uuid)),'') as identifier, ifnull(reference.titleCache,'') as bibliographicCitation, ifnull(reference.title,'') as title, ifnull(author.titleCache,'') as creator, ifnull(reference.datePublished_freetext,'') as date, ifnull(inref.titleCache,'') as source INTO OUTFILE 'c:\\temp\\reference.txt' FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' from reference left join agentbase as author on (reference.authorTeam_id = author.id) left join originalsourcebase as source on (source.citation_id = reference.id) left join descriptionelementbase as element on (source.sourcedObj_id = element.id and source.sourcedObj_type = 'eu.etaxonomy.cdm.model.description.TextData') left join descriptionbase as description on (element.indescription_id = description.id) left join taxonbase as taxon on (taxon.id = description.taxon_id) left join reference as inref on (reference.inreference_id = inref.id) where reference.title != 'XXX';

select name.id, count(taxonbase.id) as cnt, name.titleCache from taxonbas
e join taxonnamebase as name on  (name_id = name.id) group by name.id order by c
ount(taxonbase.id) desc limit 55;
+------+-----+------------------------------------------------------------------
--------+
| id   | cnt | titleCache
        |
+------+-----+------------------------------------------------------------------
--------+
| 6780 |   3 | Calamus oxycarpus var. angustifolius San Y.Chen & K.L.Wang
        |
| 3628 |   3 | Atitara granatensis (W.Bull) Kuntze
        |
| 3557 |   3 | Aiphanes disticha (Linden) Burret
        |
|  817 |   3 | Areca humilis Willd.
        |
| 5329 |   3 | Chrysalidocarpus oleraceus Jum. & H.Perrier
        |
| 7026 |   3 | Ceroxylon interruptum (H.Karst.) H.Wendl.
        |
| 5326 |   3 | Chrysalidocarpus madagascariensis var. oleraceus (Jum. & H.Perrie
r) Jum. |
| 5378 |   3 | Chrysalidocarpus madagascariensis var. lucubensis (Becc.) Jum.
        |
| 8294 |   3 | Acanthorrhiza arborea Drude ex Hook.f.
        |
| 5377 |   3 | Chrysalidocarpus madagascariensis f. oleraceus (Jum. & H.Perrier)
 Jum.   |
| 7772 |   3 | Chelyocarpus wallisii (H.Wendl.) Burret
        |
| 7037 |   3 | Ceroxylon pityrophyllum (Mart.) Mart. ex H.Wendl.
        |
| 5379 |   3 | Chrysalidocarpus madagascariensis Becc.
        |
| 2444 |   3 | Attalea lapidea (Gaertn.) Burret
        |
| 7892 |   3 | Calappa pityrophylla (Mart.) Kuntze
        |
| 4411 |   3 | Ceratolobus javanicus (Osbeck) Merr.
        |
| 5381 |   3 | Chrysalidocarpus lucubensis Becc.
        |
| 2089 |   3 | Calamus maximus Blanco
        |
| 5142 |   3 | Chamaedorea romana Guillaumin
        |
| 7304 |   3 | Ceroxylon utile (H.Karst.) H.Wendl.
        |
| 3635 |   3 | Atitara latifrons (W.Bull) Kuntze
        |
| 7105 |   2 | Palmijuncus elegans (H.Wendl.) Kuntze
        |
| 6204 |   2 | Tilmia disticha (Linden) O.F.Cook
        |
| 2273 |   2 | Oreodoxa ghiesbreghtii (H.Wendl.) auct.
        |
| 3462 |   2 | Syagrus purusana (Huber) Burret
        |
|  519 |   2 | Tessmanniophoenix wallisii (H.Wendl.) Burret
        |
| 7038 |   2 | Nunnezharia wallisii (Linden) Kuntze
        |
| 3259 |   2 | Rotang verus (Lour.) Baill.
        |
| 1921 |   2 | Ptychosperma saxatilis (Burm.f. ex Giseke) Blume
        |
| 7457 |   2 | Nunnezharia andreana (Linden) Kuntze
        |
| 7706 |   2 | Butiarecastrum Prosch.
        |
| 5679 |   2 | Socratea forgetiana (auct.) L.H.Bailey
        |
| 7609 |   2 | Nunnezharia glauca (Linden) Kuntze
        |
| 7705 |   2 | Butiarecastrum nabonnandii Prosch.
        |
| 7377 |   2 | Palma amboinensis Garsault
        |
| 6062 |   2 | Dypsis madagascariensis (Mart.) W.Watson
        |
| 3492 |   2 | Syagrus nabonnandii (Prosch.) Demoly
        |
| 3808 |   2 | Asparagus draco L.
        |
| 3469 |   2 | Syagrus sapida (Barb.Rodr.) Becc.
        |
| 7391 |   2 | Palma pumila Mill.
        |
| 1678 |   2 | Arenga nana (Griff.) H.E.Moore
        |
| 3375 |   2 | Phoenix hybrida Andr+®
         |
| 7213 |   2 | Calamus aggregatus Burret
        |
| 2973 |   2 | Syagrus chloroleuca (Barb.Rodr.) Burret
        |
| 7388 |   2 | Palma polypodiifolia Mill.
        |
| 6507 |   2 | Drymophloeus saxatilis (Burm.f. ex Giseke) Mart.
        |
| 3176 |   2 | Syagrus edulis (Barb.Rodr.) Frambach ex Dahlgren
        |
|  342 |   2 | Kentia subglobosa (H.Wendl.) F.Muell.
        |
| 7100 |   2 | Palmijuncus extensus (Roxb.) Kuntze
        |
|  609 |   2 | Seaforthia saxatilis (Burm.f. ex Giseke) Blume ex Mart.
        |
| 3233 |   2 | Rotang petraeus (Lour.) Baill.
        |
| 2080 |   2 | Oreodoxa ventricosa H.Wendl.
        |
| 6482 |   2 | Palmijuncus petraeus (Lour.) Kuntze
        |
|   51 |   2 | Saguaster saxatilis (Burm.f. ex Giseke) Kuntze
        |
| 1439 |   1 | Gastrococos crispa (Kunth) H.E.Moore
        |
+------+-----+------------------------------------------------------------------
--------+
55 rows in set (0.10 sec)



mysql> select titleCache from taxonbase where name_id = 817
    -> ;
+-------------------------------------------------------------------------------
----------+
| titleCache
          |
+-------------------------------------------------------------------------------
----------+
| Areca humilis Willd. sec. in  - World Checklist of Seed Plants
          |
| Areca humilis Willd. sec. World Checklist of Arecaceae
          |
| Areca humilis Willd. sec. in  - Govaerts, R. & Dransfield, J., World Checklist
 of Palms |
+-------------------------------------------------------------------------------
----------+
3 rows in set (0.00 sec)

mysql>