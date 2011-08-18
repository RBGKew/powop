create table Annotation (id bigint generated by default as identity, annotatedObjId bigint, annotatedObjType varchar(255), code varchar(255), jobId bigint, present boolean not null default 0, text clob, primary key (id));
create table Content (DTYPE varchar(31) not null, id bigint not null, created timestamp, creator varchar(255), identifier varchar(255), license varchar(255), modified timestamp, source varchar(255), feature varchar(255), content clob, taxon_id bigint, primary key (id));
create table Distribution (id bigint not null, created timestamp, creator varchar(255), identifier varchar(255), license varchar(255), modified timestamp, source varchar(255), region varchar(255), taxon_id bigint, primary key (id));
create table Image (id bigint not null, created timestamp, creator varchar(255), identifier varchar(255), license varchar(255), modified timestamp, source varchar(255), url varchar(255), caption varchar(255), taxon_id bigint, primary key (id));
create table Reference (id bigint not null, created timestamp, creator varchar(255), identifier varchar(255), license varchar(255), modified timestamp, source varchar(255), primary key (id));
create table Taxon (id bigint not null, created timestamp, creator varchar(255), identifier varchar(255), license varchar(255), modified timestamp, source varchar(255), accordingTo varchar(255), authorship varchar(255), basionymAuthorship varchar(255), family varchar(255), genus varchar(255), infraGenericEpithet varchar(255), infraSpecificEpithet varchar(255), kingdom varchar(255), name varchar(255), nomenclaturalCode varchar(255), ordr varchar(255), phylum varchar(255), rank varchar(255), specificEpithet varchar(255), status varchar(255), uninomial varchar(255), accepted_id bigint, parent_id bigint, primary key (id));
create table Taxon_Image (Taxon_id bigint not null, images_id bigint not null);
create table Taxon_Reference (Taxon_id bigint not null, references_id bigint not null, primary key (Taxon_id, references_id));
alter table Content add constraint FK9BEFCC591EDCD08D foreign key (taxon_id) references Taxon;
alter table Distribution add constraint FKAB93A2A41EDCD08D foreign key (taxon_id) references Taxon;
alter table Image add constraint FK437B93B1EDCD08D foreign key (taxon_id) references Taxon;
alter table Taxon add constraint FK4CD9EAA54493690 foreign key (accepted_id) references Taxon;
alter table Taxon add constraint FK4CD9EAAA9E98AAD foreign key (parent_id) references Taxon;
alter table Taxon_Image add constraint FK56D693661EDCD08D foreign key (Taxon_id) references Taxon;
alter table Taxon_Image add constraint FK56D69366437564A foreign key (images_id) references Image;
alter table Taxon_Reference add constraint FK164D2BD6968322D1 foreign key (references_id) references Reference;
alter table Taxon_Reference add constraint FK164D2BD61EDCD08D foreign key (Taxon_id) references Taxon;