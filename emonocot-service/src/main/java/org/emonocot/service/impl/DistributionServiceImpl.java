package org.emonocot.service.impl;

import org.emonocot.api.DistributionService;
import org.emonocot.model.Distribution;
import org.emonocot.persistence.dao.DistributionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributionServiceImpl extends
		ServiceImpl<Distribution, DistributionDao> implements
		DistributionService {
	
	@Autowired
    public final void setDistributionDao(final DistributionDao newDistributionDao) {
        super.dao = newDistributionDao;
    }

	

}
