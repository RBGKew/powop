package org.emonocot.portal.remoting;

import org.emonocot.model.VernacularName;
import org.emonocot.persistence.dao.VernacularNameDao;
import org.springframework.stereotype.Repository;

@Repository
public class VernacularNameDaoImpl extends DaoImpl<VernacularName> implements
		VernacularNameDao {

	public VernacularNameDaoImpl() {
		super(VernacularName.class,"vernacularName");
	}

}
