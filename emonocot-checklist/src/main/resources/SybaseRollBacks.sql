-----------------------------------------------------------------------------
-- RollBack script for View 'monocot_checklist.dbo.vwMonocot_Name'
-----------------------------------------------------------------------------
print 'Dropping View vwMonocot_Name'
go

use monocot_checklist
go

setuser 'dbo'
go

DROP VIEW dbo.vwMonocot_Name
go


-----------------------------------------------------------------------------
-- RollBack script for Altering Table'monocot_checklist.dbo.Family_Permissions'
-----------------------------------------------------------------------------
print 'Enabling select into option for monocot_checklist'

use monocot_checklist
go

exec master.dbo.sp_dboption monocot_checklist, 'select into/bulkcopy/pllsort', true
go

checkpoint
go

print 'Altering table monocot_checklist.dbo.Family_Permissions'
go

use monocot_checklist
go

setuser 'dbo'
go

alter table Family_Permissions
     drop  eMonocot
go

print 'Disabling select into option for monocot_checklist'

exec master.dbo.sp_dboption monocot_checklist, 'select into/bulkcopy/pllsort', false
go

checkpoint
go



-----------------------------------------------------------------------------
-- RollBack script for triggers
-----------------------------------------------------------------------------
print 'Dropping Triggers'
go

use monocot_checklist
go

setuser 'dbo'
go

drop trigger dbo.trg_plantName_update,
             dbo.trg_plantName_delete,
             dbo.trg_plantLocality_changes,
             dbo.trg_plantAuthor_changes,
             dbo.trg_plantCitation_changes
go             

-----------------------------------------------------------------------------
-- RollBack script for Table'monocot_checklist.dbo.Plant_Name_Deleted'
-----------------------------------------------------------------------------
use monocot_checklist
go

setuser 'dbo'
go

drop table Plant_Name_Deleted
go


-----------------------------------------------------------------------------
-- RollBack script for Altering Table'monocot_checklist.dbo.Plant_Name'
-----------------------------------------------------------------------------
print 'Enabling select into option for monocot_checklist'

use monocot_checklist
go

exec master.dbo.sp_dboption monocot_checklist, 'select into/bulkcopy/pllsort', true
go

checkpoint
go

print 'Altering table monocot_checklist.dbo.Plant_Name'

use monocot_checklist
go

setuser 'dbo'
go

alter table Plant_Name
     drop  Date_modified,
           Date_name_modified,
           Date_localities_modified,
           Date_authors_modified,
           Date_citations_modified
go

print 'Disabling select into option for monocot_checklist'

exec master.dbo.sp_dboption monocot_checklist, 'select into/bulkcopy/pllsort', false
go

checkpoint
go

