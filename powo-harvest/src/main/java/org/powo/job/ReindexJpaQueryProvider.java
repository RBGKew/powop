package org.powo.job;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.powo.model.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.stereotype.Component;

@Component
public class ReindexJpaQueryProvider implements JpaQueryProvider {
	private final Logger log = LoggerFactory.getLogger(ReindexJpaQueryProvider.class);
  private EntityManager entityManager;

  @Override
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;    
  }

  @Override
  public Query createQuery() {
    var entityGraph = entityManager.createEntityGraph(Taxon.class);
    entityGraph.addAttributeNodes("authority", "synonymNameUsages");
    
    var query = entityManager.createQuery("select t from Taxon t");
    query.setHint("javax.persistence.loadgraph", entityGraph);

    return query;
  }  
}
