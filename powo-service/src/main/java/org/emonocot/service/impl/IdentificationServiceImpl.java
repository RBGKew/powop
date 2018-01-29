package org.emonocot.service.impl;

import org.emonocot.api.IdentificationService;
import org.emonocot.model.Identification;
import org.emonocot.persistence.dao.IdentificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentificationServiceImpl extends ServiceImpl<Identification, IdentificationDao> implements IdentificationService {

	@Autowired
	public final void setIdentificationDao(final IdentificationDao identificationDao) {
		super.dao = identificationDao;
	}
}
