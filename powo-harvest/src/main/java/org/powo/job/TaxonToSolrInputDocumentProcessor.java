package org.powo.job;

import javax.annotation.PostConstruct;

import org.apache.solr.common.SolrInputDocument;
import org.powo.model.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TaxonToSolrInputDocumentProcessor implements ItemProcessor<Taxon, SolrInputDocument> {
  private static final Logger log = LoggerFactory.getLogger(TaxonToSolrInputDocumentProcessor.class);
  
  @Autowired
  private ApplicationContext context;

  @Override
  public SolrInputDocument process(Taxon item) throws Exception {
    log.info("processing item {}", item.getId());
    return item.toSolrInputDocument(context);
  }
}