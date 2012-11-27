package org.emonocot.portal.remoting;

import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.persistence.dao.TypeAndSpecimenDao;
import org.springframework.stereotype.Repository;

@Repository
public class TypeAndSpecimenDaoImpl extends DaoImpl<TypeAndSpecimen> implements
		TypeAndSpecimenDao {

	public TypeAndSpecimenDaoImpl() {
		super(TypeAndSpecimen.class,"typeAndSpecimen");
	}

	@Override
	public TypeAndSpecimen findByCatalogNumber(String catalogNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
