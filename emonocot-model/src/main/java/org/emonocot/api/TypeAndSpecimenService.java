package org.emonocot.api;

import org.emonocot.model.TypeAndSpecimen;

public interface TypeAndSpecimenService extends SearchableService<TypeAndSpecimen> {

	TypeAndSpecimen findByCatalogNumber(String catalogNumber);

}
