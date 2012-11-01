package org.emonocot.service.impl;

import org.emonocot.api.TypeAndSpecimenService;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.persistence.dao.TypeAndSpecimenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeAndSpecimenServiceImpl extends
		ServiceImpl<TypeAndSpecimen, TypeAndSpecimenDao> implements
		TypeAndSpecimenService {

	@Autowired
    public final void setTypeAndSpecimenDao(final TypeAndSpecimenDao newTextContentDao) {
        super.dao = newTextContentDao;
    }

}
