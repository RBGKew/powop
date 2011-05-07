package org.emonocot.service.impl;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class TaxonServiceImpl extends ServiceImpl<Taxon> implements
        TaxonService {

    @Override
    public final boolean verify(final String identifer,
            final String scientificName) {
        // TODO Auto-generated method stub
        return false;
    }

}
