package org.emonocot.portal.remoting;

import org.emonocot.model.Distribution;
import org.emonocot.persistence.dao.DistributionDao;
import org.springframework.stereotype.Repository;

@Repository
public class DistributionDaoImpl extends DaoImpl<Distribution> implements DistributionDao {

	public DistributionDaoImpl() {
		super(Distribution.class, "distribution");
	}
}
