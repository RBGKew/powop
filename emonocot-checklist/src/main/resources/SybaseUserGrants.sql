-----------------------------------------------------------------------------
-- Grants permissions on the 'monocot_checklist' to the emonocot user
-----------------------------------------------------------------------------
USE monocot_checklist
go

IF NOT EXISTS (SELECT 1 FROM monocot_checklist.dbo.sysusers WHERE name = 'emonocot')
BEGIN
	PRINT "User 'emonocot' needs to have been created manually"
	SELECT syb_quit()
END
go

print 'granting emonocot user permissions on monocot_checklist'

grant select on Author_Types to emonocot
go

grant select on Authors to emonocot
go

grant select on Place_of_Publication to emonocot
go

grant select on Plant_Author to emonocot
go

grant select on Plant_Citation to emonocot
go

grant select on Plant_Locality to emonocot
go

grant select on Publication to emonocot
go

grant select on Publication_Edition to emonocot
go

grant select on Publication_Type to emonocot
go

grant select on vwMonocot_Name to emonocot
go