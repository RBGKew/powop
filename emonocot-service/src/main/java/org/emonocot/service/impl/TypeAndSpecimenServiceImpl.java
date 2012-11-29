package org.emonocot.service.impl;

import org.emonocot.api.TypeAndSpecimenService;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.persistence.dao.TypeAndSpecimenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TypeAndSpecimenServiceImpl extends
		ServiceImpl<TypeAndSpecimen, TypeAndSpecimenDao> implements
		TypeAndSpecimenService {

	@Autowired
    public final void setTypeAndSpecimenDao(final TypeAndSpecimenDao typeAndSpecimenDao) {
        super.dao = typeAndSpecimenDao;
    }

	@Override
	@Transactional(readOnly = true)
	public TypeAndSpecimen findByCatalogNumber(String catalogNumber) {
		return dao.findByCatalogNumber(catalogNumber);
	}

}
