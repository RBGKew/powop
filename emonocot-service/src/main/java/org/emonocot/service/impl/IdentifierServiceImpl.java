package org.emonocot.service.impl;

import org.emonocot.api.IdentifierService;
import org.emonocot.model.Identifier;
import org.emonocot.persistence.dao.IdentifierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentifierServiceImpl extends
		ServiceImpl<Identifier, IdentifierDao> implements IdentifierService {

	@Autowired
    public final void setIdentifierDao(final IdentifierDao newIdentifierDao) {
        super.dao = newIdentifierDao;
    }
}
