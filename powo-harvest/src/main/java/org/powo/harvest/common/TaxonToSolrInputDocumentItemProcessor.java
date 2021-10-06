package org.powo.harvest.common;

import org.apache.solr.common.SolrInputDocument;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.powo.model.Taxon;

@Component
public class TaxonToSolrInputDocumentItemProcessor implements ItemProcessor<Taxon, SolrInputDocument> {
  
  @Autowired
  private ApplicationContext ctx;

  // todo: this does not work
  // @Autowired
  // private SessionFactory sessionFactory;

  @Override
  public SolrInputDocument process(Taxon item) throws Exception {
    // todo: what we would like to do is have either:
    // - all properties necessary loaded by this time
    // - most properties loaded and ABLE to load other properties required
    // currently this is not working because of LazyInitializationException - at this stage there is no session
    // - try and load a session properly? either using SessionFactory or HibernateDaoSupport
    return item.toSolrInputDocument(ctx);
  }
}
