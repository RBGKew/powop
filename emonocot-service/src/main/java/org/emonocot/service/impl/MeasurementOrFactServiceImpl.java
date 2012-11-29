package org.emonocot.service.impl;

import org.emonocot.api.MeasurementOrFactService;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.persistence.dao.MeasurementOrFactDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasurementOrFactServiceImpl extends
		ServiceImpl<MeasurementOrFact, MeasurementOrFactDao> implements MeasurementOrFactService {
	
	@Autowired
    public final void setMeasurementOrFactDao(final MeasurementOrFactDao measurementOrFactDao) {
        super.dao = measurementOrFactDao;
    }

}
