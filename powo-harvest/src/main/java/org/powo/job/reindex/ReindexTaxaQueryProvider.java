package org.powo.job.reindex;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.stereotype.Component;

@Component
public class ReindexTaxaQueryProvider implements JpaQueryProvider {
  private Logger log = LoggerFactory.getLogger(ReindexTaxaQueryProvider.class);
  private EntityManager entityManager;

  @Override
  public Query createQuery() {
    return entityManager.createQuery("from Taxon t join fetch t.authority");
  }

  @Override
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;

    // We set the default behaviour to be read only here because setting it as query hint only applies that read only
    // behaviour to entities loaded in the query. Because there are lots of entities that are loaded lazily as part of
    // reindexing we want all entities to be read as read only.
    var session = entityManager.unwrap(Session.class);
    session.setDefaultReadOnly(true);
  }
}
