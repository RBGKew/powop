package org.emonocot.portal.remoting;

import org.emonocot.model.MeasurementOrFact;
import org.emonocot.persistence.dao.MeasurementOrFactDao;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementOrFactDaoImpl extends DaoImpl<MeasurementOrFact>
		implements MeasurementOrFactDao {

	public MeasurementOrFactDaoImpl() {
		super(MeasurementOrFact.class,"measurementOrFact");
	}

	
}
