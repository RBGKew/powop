package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.TaxonService;
import org.emonocot.model.taxon.Family;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.TaxonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Returns the genera associated with this family.
     * TODO Remove once families are imported
     *
     * @param family the family
     * @return A list of genera
     */
    @Transactional(readOnly = true)
    public List<Taxon> getGenera(Family family) {
        return dao.getGenera(family);
    }

    /**
     * Returns the number of genera in a family.
     * TODO Remove once families are imported
     *
     * @param family the family
     * @return the number of accepted genera
     */
    @Transactional(readOnly = true)
    public Integer countGenera(Family family) {
        return dao.countGenera(family);
    }

}
