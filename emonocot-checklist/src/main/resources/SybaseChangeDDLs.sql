-----------------------------------------------------------------------------
-- Load latest backup of 'monocot_checklist'
-----------------------------------------------------------------------------
use master
go

drop database if exists monocot_checklist


create database monocot_checklist on userdata = 1500
     log on userlog = 150
go

/* For live */
--exec sp_changedbowner 'sa'
--go

load database monocot_checklist from '/db/sybase/dumps/data/monocot_checklist'
go

online database monocot_checklist
go



-----------------------------------------------------------------------------
-- DDL for Altering Table'monocot_checklist.dbo.Plant_Name'
-----------------------------------------------------------------------------
print 'Altering table monocot_checklist.dbo.Plant_Name'

use monocot_checklist
go

setuser 'dbo'
go

alter table Plant_Name
     add  Date_modified     datetime     null
     add  Date_name_modified     datetime     null
     add  Date_localities_modified     datetime     null
     add  Date_authors_modified     datetime     null
     add  Date_citations_modified     datetime     null
go



-----------------------------------------------------------------------------
-- DDL for Table'monocot_checklist.dbo.Plant_Name_Deleted'
-----------------------------------------------------------------------------
print 'Creating Table monocot_checklist.dbo.Plant_Name_Deleted'
go

use monocot_checklist
go

setuser 'dbo'
go

create table Plant_Name_Deleted (
	Plant_name_id                   int                              not null  ,
	Date_of_entry                   datetime                         not null  ,
	Full_epithet                    varchar(100)                         null  ,
	Family                          varchar(30)                          null  ,
	Genus                           varchar(30)                          null  ,
	Genus_hybrid_marker             varchar(1)                           null  ,
	Species                         varchar(50)                          null  ,
	Species_hybrid_marker           varchar(1)                           null  ,
	Infraspecific_rank              varchar(15)                          null  ,
	Infraspecific_epithet           varchar(50)                          null  ,
	Supraspecific_rank              varchar(15)                          null  ,
	Supraspecific_epithet           varchar(40)                          null  ,
	Taxon_status_id                 varchar(1)                           null  ,
	Accepted_plant_name             varchar(250)                         null  ,
	Basionym                        varchar(50)                          null  ,
	Hybrid_formula                  varchar(100)                         null  ,
	Publication_author              varchar(60)                          null  ,
	Volume_and_page                 varchar(30)                          null  ,
	First_published                 varchar(22)                          null  ,
	Geographic_area                 varchar(150)                         null  ,
	Nomenclatural_remarks           varchar(50)                          null  ,
	Remarks                         varchar(255)                         null  ,
	Accepted_plant_name_id          int                                  null  ,
	Checklist_editor_id             varchar(6)                           null  ,
	Basionym_id                     int                                  null  ,
	Climate_id                      int                                  null  ,
	Lifeform_id                     int                                  null  ,
	IUCN_red_list_category_id       int                                  null  ,
	Place_of_publication_id         int                                  null  ,
	Institute                       varchar(50)                          null  ,
	Institute_id                    varchar(100)                         null  ,
    Date_modified                   datetime                             null  ,
	Date_name_modified     			datetime        					 null  ,
    Date_localities_modified     	datetime     						 null  ,
    Date_authors_modified     		datetime						     null  ,
    Date_citations_modified     	datetime						     null  ,
	Date_deleted                    datetime                             default getdate()   not null
)
lock allpages
 on 'default'
go



-----------------------------------------------------------------------------
-- DDL for Trigger 'monocot_checklist.dbo.trg_plantName_update'
-----------------------------------------------------------------------------
print 'Creating Trigger trg_plantName_update'
go

create trigger dbo.trg_plantName_update

on dbo.Plant_Name

for update as

begin

     declare @num_updated int



/* Determine how many rows were updated. */

select @num_updated = @@rowcount

    if @num_updated = 0

    return

    update Plant_Name
    set Date_name_modified = getDate(),
        Date_modified = getDate()
    from inserted, Plant_Name
    where inserted.Plant_name_id = Plant_Name.Plant_name_id
    


end
go


-----------------------------------------------------------------------------
-- DDL for Trigger 'monocot_checklist.dbo.trg_plantName_delete'
-----------------------------------------------------------------------------
print 'Creating Trigger trg_plantName_delete'
go

create trigger dbo.trg_plantName_delete

on dbo.Plant_Name

for delete as

begin

     declare @num_updated int


/* Determine how many rows were updated. */

select @num_updated = @@rowcount

    if @num_updated = 0

    return

    /* We assume that if a name is deleted, the primary key will not be reused, i.e. there will no duplicate key violation */
    insert Plant_Name_Deleted
    select     Plant_name_id,
               Date_of_entry,
               Full_epithet,
               Family,
               Genus,
               Genus_hybrid_marker,
               Species,
               Species_hybrid_marker,
               Infraspecific_rank,
               Infraspecific_epithet,
               Supraspecific_rank,
               Supraspecific_epithet,
               Taxon_status_id,
               Accepted_plant_name,
               Basionym,
               Hybrid_formula,
               Publication_author,
               Volume_and_page,
               First_published,
               Geographic_area,
               Nomenclatural_remarks,
               Remarks,
               Accepted_plant_name_id,
               Checklist_editor_id,
               Basionym_id,
               Climate_id,
               Lifeform_id,
               IUCN_red_list_category_id,
               Place_of_publication_id,
               Institute,
               Institute_id,
               Date_modified,
               Date_name_modified,
               Date_localities_modified,
               Date_authors_modified,
               Date_citations_modified,
               getdate() AS Date_deleted
    from deleted

end
go



-----------------------------------------------------------------------------
-- DDL for Trigger 'monocot_checklist.dbo.trg_plantLocality_changes'
-----------------------------------------------------------------------------
print 'Creating Trigger trg_plantLocality_changes'
go

create trigger dbo.trg_plantLocality_changes

on dbo.Plant_Locality

for insert, update, delete as

begin

     declare @num_updated int



/* Determine how many rows were updated. */

select @num_updated = @@rowcount

    if @num_updated = 0
/* TODO: This escape may nolonger be valid */
    return


/* TODO: Replace with stored procedure */
    update Plant_Name
    set Date_localities_modified = getDate(),
        Date_modified = getDate()
    from inserted, Plant_Name
    where inserted.Plant_name_id = Plant_Name.Plant_name_id
    


end                                                                                                                          
go

-----------------------------------------------------------------------------
-- DDL for Trigger 'monocot_checklist.dbo.trg_plantAuthor_changes'
-----------------------------------------------------------------------------
print 'Creating Trigger trg_plantAuthor_changes'
go

create trigger dbo.trg_plantAuthor_changes

on dbo.Plant_Author

for insert, update, delete as

begin

     declare @num_updated int



/* Determine how many rows were updated. */

select @num_updated = @@rowcount

    if @num_updated = 0
/* TODO: This escape may nolonger be valid */
    return

/* TODO: Replace with stored procedure */
    update Plant_Name
    set Date_authors_modified = getDate(),
        Date_modified = getDate()
    from inserted, Plant_Name
    where inserted.Plant_name_id = Plant_Name.Plant_name_id
    


end                                                                                                                          
go


-----------------------------------------------------------------------------
-- DDL for Trigger 'monocot_checklist.dbo.trg_plantCitation_changes'
-----------------------------------------------------------------------------
print 'Creating Trigger trg_plantCitation_changes'
go

create trigger dbo.trg_plantCitation_changes

on dbo.Plant_Citation

for insert, update, delete as

begin

     declare @num_updated int



/* Determine how many rows were updated. */

select @num_updated = @@rowcount

    if @num_updated = 0
/* TODO: This escape may nolonger be valid */
    return

/* TODO: Replace with stored procedure */
    update Plant_Name
    set Date_citations_modified = getDate(),
        Date_modified = getDate()
    from inserted, Plant_Name
    where inserted.Plant_name_id = Plant_Name.Plant_name_id
    


end                                                                                                                          
go


-----------------------------------------------------------------------------
-----------------------------------------------------------------------------
-----------------------------------------------------------------------------
--     Expose data to eMoncot via a View combining
--     Plant_Name and Plant_Name_Deleted
-----------------------------------------------------------------------------
-----------------------------------------------------------------------------
-----------------------------------------------------------------------------



-----------------------------------------------------------------------------
-- DDL for Altering Table'monocot_checklist.dbo.Family_Permissions'
-----------------------------------------------------------------------------
print 'Enabling select into option for monocot_checklist'

use monocot_checklist
go

exec master.dbo.sp_dboption monocot_checklist, 'select into/bulkcopy/pllsort', true
go

checkpoint
go


print 'Altering table monocot_checklist.dbo.Family_Permissions'

alter table Family_Permissions
     add  eMonocot     bit     default 0     not null
go


print 'Disabling select into option for monocot_checklist'

exec master.dbo.sp_dboption monocot_checklist, 'select into/bulkcopy/pllsort', false
go

checkpoint
go



-----------------------------------------------------------------------------
-- DDL for View 'monocot_checklist.dbo.vwMonocot_Name'
-----------------------------------------------------------------------------
print 'Creating View vwMonocot_Name'
go

use monocot_checklist
go

setuser 'dbo'
go

CREATE VIEW dbo.vwMonocot_Name
AS /* View created to pull Monocot data from for the eMonocot project */
   /* Also see 'eMonocot' User                                        */
       SELECT  Plant_name_id,
               Date_of_entry,
               Full_epithet,
               Family,
               Genus,
               Genus_hybrid_marker,
               Species,
               Species_hybrid_marker,
               Infraspecific_rank,
               Infraspecific_epithet,
               Supraspecific_rank,
               Supraspecific_epithet,
               Taxon_status_id,
               Accepted_plant_name,
               Basionym,
               Hybrid_formula,
               Publication_author,
               Volume_and_page,
               First_published,
               Geographic_area,
               Nomenclatural_remarks,
               Remarks,
               Accepted_plant_name_id,
               Checklist_editor_id,
               Basionym_id,
               Climate_id,
               Lifeform_id,
               IUCN_red_list_category_id,
               Place_of_publication_id,
               Institute,
               Institute_id,
               Date_modified,
               NULL AS Date_deleted
       FROM  Plant_Name,    
             Family_Permissions
          WHERE  Plant_Name.Family = Family_Permissions.family
            AND  Family_Permissions.eMonocot = 1
UNION ALL
       SELECT  Plant_name_id,
               Date_of_entry,
               Full_epithet,
               Family,
               Genus,
               Genus_hybrid_marker,
               Species,
               Species_hybrid_marker,
               Infraspecific_rank,
               Infraspecific_epithet,
               Supraspecific_rank,
               Supraspecific_epithet,
               Taxon_status_id,
               Accepted_plant_name,
               Basionym,
               Hybrid_formula,
               Publication_author,
               Volume_and_page,
               First_published,
               Geographic_area,
               Nomenclatural_remarks,
               Remarks,
               Accepted_plant_name_id,
               Checklist_editor_id,
               Basionym_id,
               Climate_id,
               Lifeform_id,
               IUCN_red_list_category_id,
               Place_of_publication_id,
               Institute,
               Institute_id,
               Date_modified,
               Deleted_date
       FROM  Plant_Name_Deleted,    
             Family_Permissions   
       WHERE  Plant_Name_Deleted.Family = Family_Permissions.family
         AND  Family_Permissions.eMonocot = 1
go 




setuser
go