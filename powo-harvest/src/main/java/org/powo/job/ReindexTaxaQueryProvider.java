package org.powo.job;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.powo.model.Taxon;
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
    
    // var taxon = entityManager.createQuery("from Taxon t where t.id = 23", Taxon.class)
    //   .setHint(QueryHints.READ_ONLY, true)
    //   .getSingleResult();

    // var taxonState = getPersistenceContextEntry(taxon).getLoadedState();
    // log.info("taxon loaded state {} {}", taxonState, taxonState == null);
    
    // var description = taxon.getDescriptions().iterator().next();
    // var descriptionState = getPersistenceContextEntry(description).getLoadedState();
    // log.info("description loaded state {} {}", descriptionState, descriptionState == null);

    // throw new RuntimeException();

    return entityManager.createQuery("from Taxon t join fetch t.authority left join fetch t.acceptedNameUsage");
      // .setHint(QueryHints.READ_ONLY, true);
  }

  @Override
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
    var session = entityManager.unwrap(Session.class);
    session.setDefaultReadOnly(true);
  }

  private EntityEntry getPersistenceContextEntry(Object object) {
    var session = entityManager.unwrap(SharedSessionContractImplementor.class);
    var persistenceContext = session.getPersistenceContext();

    return persistenceContext.getEntry(object);
  }
}
