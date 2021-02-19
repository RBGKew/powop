package org.powo.harvest.service;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.powo.model.Taxon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A small helper service for improving the harvester performance.
 * 
 * Fetches taxons from the database with FlushMode=COMMIT, which avoids
 * checking for queued updates and flushing them. This gets slow when
 * the cache has a lot of entities in it.
 * 
 */
@Service
public class PersistedTaxonService {

    @Autowired
    private SessionFactory sessionFactory;
    
    public Taxon find(String identifier) {
		var session = sessionFactory.getCurrentSession();
        var builder = session.getCriteriaBuilder();
        var criteria = builder.createQuery(Taxon.class);
        var taxonRoot = criteria.from(Taxon.class);
        criteria.where(builder.equal(taxonRoot.get("identifier"), identifier));
		return session.createQuery(criteria).setHibernateFlushMode(FlushMode.COMMIT).uniqueResult();
    }

}
