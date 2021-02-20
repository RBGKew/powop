package org.powo.harvest.service;

import org.powo.model.Taxon;
import org.springframework.stereotype.Repository;

@Repository
public class TaxonPersistedService extends PersistedService<Taxon> {
    
    public TaxonPersistedService() {
        super(Taxon.class);
    }

}
