package org.powo.portal.service;

import org.powo.model.Taxon;
import org.powo.service.impl.TaxonServiceImpl;
import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class SiteTaxonService extends TaxonServiceImpl {

  @Autowired
  @Qualifier("currentSite")
  Site site;

  public Taxon load(String identifier, String fetch) {
    var taxon = super.load(identifier, fetch);
    if (site.hasTaxon(taxon)) {
      throw new NotFoundException(Taxon.class, identifier);
    }
    taxon.setChildNameUsages(taxon.getChildNameUsages().stream().filter(t -> site.hasTaxon(t)).collect(Collectors.toSet());
    return taxon;
  }

}
