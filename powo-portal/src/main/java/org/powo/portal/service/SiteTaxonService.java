package org.powo.portal.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.powo.api.TaxonService;
import org.powo.model.Taxon;
import org.powo.model.exception.NotFoundException;
import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class SiteTaxonService {

  @Autowired
  @Qualifier("currentSite")
  Site site;

  @Autowired
  TaxonService taxonService;

  public Taxon load(String identifier) {
    var taxon = taxonService.load(identifier, "object-page");
    if (!site.hasTaxon(taxon)) {
      throw new NotFoundException(Taxon.class, identifier);
    }
    return taxon;
  }

  public Set<Taxon> getChildNameUsages(Taxon taxon) {
    return taxon.getChildNameUsages().stream().filter(t -> site.hasTaxon(t)).collect(Collectors.toSet());
  }

  public Set<Taxon> getSynonymNameUsages(Taxon taxon) {
    return taxon.getSynonymNameUsages().stream().filter(t -> site.hasTaxon(t)).collect(Collectors.toSet());
  }

}
