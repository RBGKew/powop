package org.powo.job;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.annotations.QueryHints;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.stereotype.Component;

@Component
public class ReindexTaxaQueryProvider implements JpaQueryProvider {
  private EntityManager entityManager;

  @Override
  public Query createQuery() {
    return entityManager.createQuery("from Taxon t join fetch t.authority join fetch t.acceptedNameUsage")
      .setHint(QueryHints.READ_ONLY, true);
  }

  @Override
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
}
