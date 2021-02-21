package org.powo.harvest.service;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A small helper service for improving the harvester performance.
 * 
 * Fetches entities from the database with FlushMode=COMMIT, which avoids checking
 * for queued updates and flushing them. This gets slow when the cache has a lot
 * of entities in it.
 * 
 * This can be used safely when either:
 *
 * a) The entity is not being modified by the current import step (e.g. Taxon in reference.Processor)
 * b) The entity is being managed by an application level cache (e.g. Reference in reference.Processor)
 *
 */
public class PersistedService<T> {

  @Autowired
  private SessionFactory sessionFactory;

  private Class<T> type;

  public PersistedService(final Class<T> type) {
		this.type = type;
	}

  public T find(final String identifier) {
    var session = sessionFactory.getCurrentSession();
    var builder = session.getCriteriaBuilder();
    var criteria = builder.createQuery(type);
    var root = criteria.from(type);
    criteria.where(builder.equal(root.get("identifier"), identifier));
    return session.createQuery(criteria).setHibernateFlushMode(FlushMode.COMMIT).uniqueResult();
  }

}
