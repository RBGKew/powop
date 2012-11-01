package org.emonocot.portal.remoting;

import org.emonocot.model.Identifier;
import org.emonocot.persistence.dao.IdentifierDao;
import org.springframework.stereotype.Repository;

@Repository
public class IdentifierDaoImpl extends DaoImpl<Identifier> implements
		IdentifierDao {

	public IdentifierDaoImpl() {
		super(Identifier.class, "identifier");	
	}
}
