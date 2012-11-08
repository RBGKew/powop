package org.emonocot.persistence.dao;

import org.emonocot.model.TypeAndSpecimen;

public interface TypeAndSpecimenDao extends Dao<TypeAndSpecimen> {
	
	TypeAndSpecimen findByCatalogNumber(String catalogNumber);

}
