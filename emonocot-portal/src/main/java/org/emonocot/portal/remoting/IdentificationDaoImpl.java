package org.emonocot.portal.remoting;

import org.emonocot.model.Identification;
import org.emonocot.persistence.dao.IdentificationDao;
import org.springframework.stereotype.Repository;

@Repository
public class IdentificationDaoImpl extends DaoImpl<Identification> implements IdentificationDao {

	public IdentificationDaoImpl() {
		super(Identification.class, "identification");
	}

}
