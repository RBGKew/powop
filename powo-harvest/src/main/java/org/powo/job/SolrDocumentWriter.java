package org.powo.job;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolrDocumentWriter implements ItemWriter<SolrInputDocument> {
  private static final Logger logger = LoggerFactory.getLogger(SolrDocumentWriter.class);

  @Autowired
  private SolrClient solrClient;

  @Override
  @SuppressWarnings("unchecked") // Java complains about the cast to List<SolrInputDocument> below but it is safe
  public void write(List<? extends SolrInputDocument> documents) throws Exception {
    try {
      var updateResponse = solrClient.add((List<SolrInputDocument>) documents);
      if (updateResponse.getStatus() != 0) {
        logger.error("Exception adding solr documents " + updateResponse.toString());
        updateResponse = solrClient.rollback();
      } else {
        updateResponse = solrClient.commit(true, true);
      }
    } catch (SolrServerException | IOException ex) {
      logger.error(ex.getLocalizedMessage());
      for (var ste : ex.getStackTrace()) {
        logger.error(ste.toString());
      }
    }
  }
}
