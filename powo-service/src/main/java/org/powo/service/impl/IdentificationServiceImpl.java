package org.powo.service.impl;

import org.powo.api.IdentificationService;
import org.powo.model.Identification;
import org.powo.persistence.dao.IdentificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentificationServiceImpl extends ServiceImpl<Identification, IdentificationDao> implements IdentificationService {

	@Autowired
	public final void setIdentificationDao(final IdentificationDao identificationDao) {
		super.dao = identificationDao;
	}
}
