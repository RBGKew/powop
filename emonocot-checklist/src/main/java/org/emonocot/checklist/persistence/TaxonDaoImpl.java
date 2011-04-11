package org.emonocot.checklist.persistence;

import java.util.List;

import org.emonocot.checklist.model.Taxon;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TaxonDaoImpl extends HibernateDaoSupport implements TaxonDao {
	
	@Autowired
	public void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Transactional(readOnly = true)
	public List<Taxon> search(String search) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(readOnly = true)
	public Taxon get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
