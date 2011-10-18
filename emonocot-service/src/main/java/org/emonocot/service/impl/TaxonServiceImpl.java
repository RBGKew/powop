package org.emonocot.service.impl;

import org.emonocot.api.TaxonService;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.TaxonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class TaxonServiceImpl extends SearchableServiceImpl<Taxon, TaxonDao> implements
        TaxonService {

    @Autowired
    public void setTaxonDao(TaxonDao taxonDao) {
        super.dao = taxonDao;
    }

}
