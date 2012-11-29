package org.emonocot.service.impl;

import org.emonocot.api.VernacularNameService;
import org.emonocot.model.VernacularName;
import org.emonocot.persistence.dao.VernacularNameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VernacularNameServiceImpl extends
		ServiceImpl<VernacularName, VernacularNameDao> implements
		VernacularNameService {
	
	@Autowired
    public final void setVernacularNameDao(final VernacularNameDao vernacularNameDao) {
        super.dao = vernacularNameDao;
    }

}
