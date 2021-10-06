package org.powo.job;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.query.Query;
import org.powo.model.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.orm.HibernateQueryProvider;
import org.springframework.stereotype.Component;

@Component
public class ReindexQueryProvider implements HibernateQueryProvider<Taxon> {
	private final Logger log = LoggerFactory.getLogger(ReindexQueryProvider.class);
  private Session session;
  private StatelessSession statelessSession;

  @Override
  public Query<Taxon> createQuery() {
    log.info("creating query");
    var query = session.createQuery("select t from Taxon t", Taxon.class);    
    query.setHint("org.hibernate.readOnly", true);
    return query;
  }

  @Override
  public void setSession(Session session) {
    this.session = session;
  }

  @Override
  public void setStatelessSession(StatelessSession session) {
    this.statelessSession = session;
  }
  
}
