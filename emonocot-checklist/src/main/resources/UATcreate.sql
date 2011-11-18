-----------------------------------------------------------------------------
-- Load latest backup of 'monocot_checklist'
-----------------------------------------------------------------------------
use master
go

IF EXISTS (SELECT 1 FROM master.dbo.sysdatabases
	WHERE name = 'emonocot_checklist')
	DROP DATABASE emonocot_checklist
go


create database emonocot_checklist on userdata = 1750
     log on userlog = 150
go

/* For live */
--exec sp_changedbowner 'sa'
--go

load database emonocot_checklist from '/db/sybase/dumps/data/monocot_checklist'
go

online database emonocot_checklist
go

print 'You now need to *EDIT* the SybaseUpdate.sql script to refer to the emoncot_checklist DB and run it'
