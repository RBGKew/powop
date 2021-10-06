package org.powo.harvest.common;

import org.apache.solr.common.SolrInputDocument;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.powo.model.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.powo.api.TaxonService;

public class TaxonIdToLoadedTaxonProcessor implements ItemProcessor<Long, Taxon> {
  private final Logger log = LoggerFactory.getLogger(TaxonIdToLoadedTaxonProcessor.class);
  
	private TaxonService taxonService;

	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

  @Override
  public Taxon process(Long id) throws Exception {
    // todo: what we would like to do is have either:
    // - all properties necessary loaded by this time
    // - most properties loaded and ABLE to load other properties required
    // currently this is not working because of LazyInitializationException - at this stage there is no session
    // - try and load a session properly? either using SessionFactory or HibernateDaoSupport
    return taxonService.load(id, "taxon-page");
  }
}
