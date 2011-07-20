-----------------------------------------------------------------------------
-- Load latest backup of 'monocot_checklist'
-----------------------------------------------------------------------------
use master
go

IF EXISTS (SELECT 1 FROM master.dbo.sysdatabases
	WHERE name = 'monocot_checklist')
	DROP DATABASE monocot_checklist
go


create database monocot_checklist on userdata = 1750
     log on userlog = 150
go

/* For live */
--exec sp_changedbowner 'sa'
--go

load database monocot_checklist from '/db/sybase/dumps/data/monocot_checklist'
go

online database monocot_checklist
go

print 'You now need to run the SybaseSchemaUpdate.sql script'
