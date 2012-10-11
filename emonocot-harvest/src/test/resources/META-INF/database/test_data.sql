insert into Source (id, identifier, uri) values (10, 'test', 'http://example.com');
insert into Reference (id, identifier,title) values (1, 'urn:kew.org:wcs:placeOfPublication:899','Bull. Pacific Orchid Soc. Hawaii');
insert into Taxon (id, identifier, scientificName, family, authority_id) values (1, 'urn:lsid:cate-araceae.org:taxon:5cd5a6aa-6bfb-1014-a918-dc439151c9e5', 'Acontias conspurcatus','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (2, 'urn:lsid:cate-araceae.org:taxon:5cd5c756-6bfb-1014-a918-dc439151c9e5', 'Acontias cubensis','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (3, 'urn:lsid:cate-araceae.org:taxon:5cd5e85c-6bfb-1014-a918-dc439151c9e5', 'Acontias diversifolius','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (4, 'urn:lsid:cate-araceae.org:taxon:5c7a00ea-6bfb-1014-a918-dc439151c9e5', 'Acontias hastifolius','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (5, 'urn:lsid:cate-araceae.org:taxon:5cd6156a-6bfb-1014-a918-dc439151c9e5', 'Acontias helleborifolius','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (6, 'urn:lsid:cate-araceae.org:taxon:5c7a04f7-6bfb-1014-a918-dc439151c9e5', 'Acontias hoffmannii','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (7, 'urn:lsid:cate-araceae.org:taxon:5c7a08f9-6bfb-1014-a918-dc439151c9e5', 'Acontias luridus','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (8, 'urn:lsid:cate-araceae.org:taxon:5c7478df-6bfb-1014-a918-dc439151c9e5', 'Acontias platylobus','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (9, 'urn:lsid:cate-araceae.org:taxon:5c7a0cef-6bfb-1014-a918-dc439151c9e5', 'Acontias plumieri','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (10, 'urn:lsid:cate-araceae.org:taxon:5c747dbe-6bfb-1014-a918-dc439151c9e5', 'Acontias riedelianus','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (11, 'urn:lsid:cate-araceae.org:taxon:5cd66483-6bfb-1014-a918-dc439151c9e5', 'Acontias striatipes','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (12, 'urn:lsid:cate-araceae.org:taxon:5cd6850d-6bfb-1014-a918-dc439151c9e5', 'Acontias variegatus','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (13, 'urn:lsid:cate-araceae.org:taxon:5c74820b-6bfb-1014-a918-dc439151c9e5', 'Acontias wendlandii','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (14, 'urn:lsid:cate-araceae.org:taxon:5cd58503-6bfb-1014-a918-dc439151c9e5', 'Acontias','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (15, 'urn:lsid:cate-araceae.org:taxon:5cd6c8e9-6bfb-1014-a918-dc439151c9e5', 'Adelonema erythropus','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (16, 'urn:lsid:cate-araceae.org:taxon:5cd6a8b2-6bfb-1014-a918-dc439151c9e5', 'Adelonema','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (17, 'urn:lsid:cate-project.org:taxon:19c6f4d2-3336-46bc-bba3-a4880cf62196', 'Afrorhaphidophora africana','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (18, 'urn:lsid:grassbase.kew.org:taxa:387039', 'Elyhordeum × montanense','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (19, 'urn:kew.org:wcs:taxon:303017', 'Conanthera johowi','Tecophilaeaceae',10);
insert into Taxon (id, acceptedNameUsage_id, identifier, scientificName, family, authority_id) values (20, 18, 'urn:lsid:grassbase.kew.org:taxa:387038', '× Elyhordeum macounii','Arecaceae',10);
insert into Taxon (id, acceptedNameUsage_id, identifier, scientificName, family, authority_id) values (21, 18, 'urn:lsid:grassbase.kew.org:taxa:387040', '× Elyhordeum pavlovii','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, authority_id) values (22, 'urn:kew.org:wcs:family:32', 'Orchidaceae',10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, namePublishedIn_id, authority_id) values (23, 'urn:kew.org:wcs:taxon:70052', '× Epilaeliopsis auct.','Orchidaceae',22,1,10);
insert into Taxon (id, identifier, scientificName, family, parentNameUsage_id, authority_id) values (24, 'urn:kew.org:wcs:taxon:70053', NULL,'Orchidaceae',23,10);
insert into Taxon (id, identifier, scientificName, family, parentNameUsage_id, authority_id) values (25, 'urn:kew.org:wcs:taxon:67664', 'Epidendrum','Orchidaceae',22,10);
insert into Taxon (id, identifier, scientificName, family, parentNameUsage_id, authority_id) values (26, 'urn:kew.org:wcs:taxon:68574', 'Epidendrum hemiscleria','Orchidaceae',25,10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, authority_id) values (27, 'urn:kew.org:wcs:taxon:71680', NULL, NULL, NULL,10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, authority_id) values (29, 'urn:kew.org:wcs:taxon:345361', 'Eriochilus dilatatus subsp. dilatatus','Orchidaceae',27,10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, authority_id) values (30, 'urn:kew.org:wcs:taxon:71685', NULL, NULL, NULL,10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, authority_id) values (31, 'urn:kew.org:wcs:taxon:345362', 'Eriochilus scaber subsp. scaber','Orchidaceae',30,10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, authority_id) values (32, 'urn:kew.org:wcs:taxon:346829', NULL, NULL, NULL,10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, authority_id) values (33, 'urn:kew.org:wcs:taxon:345506', 'Erasanthe henrici var. henrici','Orchidaceae',32,10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, authority_id) values (34, 'urn:kew.org:wcs:taxon:67580', NULL, NULL, 22,10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, authority_id) values (35, 'urn:kew.org:wcs:taxon:67588', 'Epiblastus cuneatus', 'Orchidaceae', 34,10);
insert into Taxon (id, identifier, scientificName, family,parentNameUsage_id, authority_id) values (36, 'urn:kew.org:wcs:taxon:67589', 'Epiblastus cuneatus var. cuneatus', 'Orchidaceae', 35,10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (37, 'urn:kew.org:wcs:taxon:100446','Howea Becc.','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (38, 'urn:kew.org:wcs:taxon:100447','Howea belmoreana (C.Moore & F.Muell.) Becc.','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (39, 'urn:kew.org:wcs:taxon:100448','Howea forsteriana (F.Muell.) Becc.','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (40, 'urn:kew.org:wcs:taxon:101020','Hydriastele affinis (Becc.) W.J.Baker & Loo','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (41, 'urn:kew.org:wcs:taxon:37679','Chamaedorea verecunda Grayum & Hodel','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (42, 'urn:kew.org:wcs:taxon:37674','Chamaedorea undulatifolia Hodel & N.W.Uhl','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, family, authority_id) values (43, 'urn:kew.org:wcs:taxon:170755','Ptychococcus Becc.','Arecaceae',10);
insert into Taxon (id, identifier, scientificName, parentNameUsage_id, family, authority_id) values (44, 'urn:kew.org:wcs:taxon:170756','Ptychococcus albertisianus',43,'Arecaceae',10);
insert into Taxon (id, identifier, scientificName, parentNameUsage_id, family, authority_id) values (45, 'urn:kew.org:wcs:taxon:170757','Ptychococcus archboldianus',43,'Arecaceae',10);
insert into Taxon (id, identifier, scientificName, parentNameUsage_id, family, authority_id) values (46, 'urn:kew.org:wcs:taxon:170758','Ptychococcus arecinus',43,'Arecaceae',10);
insert into Taxon (id, identifier,scientificName,genus,taxonRank, authority_id) values (84, 'urn:kew.org:wcs:family:84','Alismatales','Alismatales','ORDER',10);
insert into Taxon (id, identifier,scientificName,genus,taxonRank,parentNameUsage_id, authority_id) values (83, 'urn:kew.org:wcs:family:3','Araceae','Araceae','FAMILY',84,10);
insert into Taxon (id, identifier,scientificName,genus,taxonRank,parentNameUsage_id, family, authority_id) values (81, 'urn:kew.org:wcs:taxon:16026','Arum','Arum','GENUS',83,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (47, 'urn:kew.org:wcs:taxon:16041','Arum apulum','Arum','apulum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (48, 'urn:kew.org:wcs:taxon:456456','Arum alpinariae','Arum','alpinariae','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (49, 'urn:kew.org:wcs:taxon:16050','Arum balansanum','Arum','balansanum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (50, 'urn:kew.org:wcs:taxon:16052','Arum besserianum','Arum','besserianum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (51, 'urn:kew.org:wcs:taxon:16060','Arum byzantinum','Arum','byzantinum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (52, 'urn:kew.org:wcs:taxon:16074','Arum concinnatum','Arum','concinnatum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (53, 'urn:kew.org:wcs:taxon:16088','Arum creticum','Arum','creticum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (54, 'urn:kew.org:wcs:taxon:16095','Arum cylindraceum','Arum','cylindraceum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (55, 'urn:kew.org:wcs:taxon:16097','Arum cyrenaicum','Arum','cyrenaicum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (82, 'urn:kew.org:wcs:taxon:16104','Arum dioscoridis','Arum','dioscoridis','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,infraspecificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (56, 'urn:kew.org:wcs:taxon:16108','Arum dioscoridis var. dioscoridis','Arum','dioscoridis','dioscoridis','VARIETY',82,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (57, 'urn:kew.org:wcs:taxon:16130','Arum elongatum','Arum','elongatum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (58, 'urn:kew.org:wcs:taxon:16141','Arum euxinum','Arum','euxinum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (59, 'urn:kew.org:wcs:taxon:16161','Arum gratum','Arum','gratum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (60, 'urn:kew.org:wcs:taxon:16166','Arum hainesii','Arum','hainesii','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (61, 'urn:kew.org:wcs:taxon:16171','Arum hygrophilum','Arum','hygrophilum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (62, 'urn:kew.org:wcs:taxon:16178','Arum idaeum','Arum','idaeum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (63, 'urn:kew.org:wcs:taxon:16186','Arum italicum','Arum','italicum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,infraspecificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (64, 'urn:kew.org:wcs:taxon:16187','Arum italicum subsp. albispathum','Arum','italicum','albispathum','InfraspecificName',63,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,infraspecificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (65, 'urn:kew.org:wcs:taxon:16191','Arum italicum subsp. canariense','Arum','italicum','canariense','InfraspecificName',63,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,infraspecificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (66, 'urn:kew.org:wcs:taxon:16205','Arum italicum subsp. italicum','Arum','italicum','italicum','InfraspecificName',63,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,infraspecificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (67, 'urn:kew.org:wcs:taxon:16212','Arum italicum subsp. neglectum','Arum','italicum','neglectum','InfraspecificName',63,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (68, 'urn:kew.org:wcs:taxon:16223','Arum jacquemontii','Arum','jacquemontii','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (69, 'urn:kew.org:wcs:taxon:16224','Arum korolkowii','Arum','korolkowii','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (70, 'urn:kew.org:wcs:taxon:16240','Arum maculatum','Arum','maculatum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (71, 'urn:kew.org:wcs:taxon:372172','Arum megobrebi','Arum','megobrebi','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (72, 'urn:kew.org:wcs:taxon:16288','Arum nigrum','Arum','nigrum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (73, 'urn:kew.org:wcs:taxon:16301','Arum orientale','Arum','orientale','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,infraspecificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (74, 'urn:kew.org:wcs:taxon:16320','Arum orientale ssp. orientale','Arum','orientale','orientale','InfraspecificName',73,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,infraspecificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (75, 'urn:kew.org:wcs:taxon:16316','Arum orientale ssp. longispathum','Arum','orientale','longispathum','InfraspecificName',73,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (76, 'urn:kew.org:wcs:taxon:16331','Arum palaestinum','Arum','palaestinum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (77, 'urn:kew.org:wcs:taxon:16344','Arum pictum','Arum','pictum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (78, 'urn:kew.org:wcs:taxon:16358','Arum purpureospathum','Arum','purpeospathum','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (79, 'urn:kew.org:wcs:taxon:16371','Arum rupicola','Arum','rupicola','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank,parentNameUsage_id, family, authority_id) values (80, 'urn:kew.org:wcs:taxon:16386','Arum sintenisii','Arum','sintenisii','SPECIES',81,'Araceae',10);
insert into Taxon (id, identifier,scientificName,genus,specificEpithet,taxonRank, family, authority_id) values (85, 'urn:kew.org:wcs:taxon:219530','Amomum uliginosum','Amomum','uliginosum','SPECIES','Zingiberaceae',10);
insert into Distribution (id, location, taxon_id) values (1,'DOM',23);
insert into Distribution (id, location, taxon_id) values (2,'NOR',23);
insert into Distribution (id, location, taxon_id) values (3,'SVA',23);
insert into Description (id, identifier, description, type, taxon_id, authority_id) values (1,'description1','lorem ipsum', 'general', 76,10);
insert into Description (id, identifier, description, type, taxon_id, authority_id) values (2,'description2','lorem ipsum', 'diagnostic', 76,10);
insert into Image (id, identifier, format, taxon_id, authority_id) values (1,'http://build.e-monocot.org/images/rss.png','png', 76,10);
insert into Image (id, identifier, format, taxon_id, authority_id) values (2,'http://build.e-monocot.org/images/logos/maven-feather.png','png', 76,10);
insert into Taxon_Image (Taxon_id, images_id) values (76,1);
insert into Taxon_Image (Taxon_id, images_id) values (81,1);
insert into Taxon_Image (Taxon_id, images_id) values (76,2);
insert into Taxon_Image (Taxon_id, images_id) values (81,2);
insert into Annotation (id, annotatedObjId, annotatedObjType, code, type, jobId) values (1,1,'Taxon','Create','Info', 600);
insert into Annotation (id, annotatedObjId, annotatedObjType, code, type, jobId) values (2,1,'Taxon','Present','Info', 601);
insert into Annotation (id, annotatedObjId, annotatedObjType, code, type, jobId) values (3,1,'Taxon','Absent','Warn', 602);
insert into Annotation (id, annotatedObjId, annotatedObjType, code, type, jobId) values (4,1,'Taxon','Absent','Warn', 603);