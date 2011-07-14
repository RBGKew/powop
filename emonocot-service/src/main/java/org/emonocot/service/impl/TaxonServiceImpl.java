package org.emonocot.service.impl;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.TaxonDao;
import org.emonocot.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class TaxonServiceImpl extends ServiceImpl<Taxon, TaxonDao> implements
        TaxonService {

    @Transactional(readOnly = true)
    public final boolean verify(final String identifer,
            final String scientificName) {
        return dao.verify(identifer, scientificName);
    }

    @Autowired
    public void setTaxonDao(TaxonDao taxonDao) {
        super.dao = taxonDao;
    }

}
