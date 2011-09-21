package org.emonocot.portal.remoting;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.TaxonDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class TaxonDaoImpl extends DaoImpl<Taxon> implements TaxonDao {

    /**
     *
     */
    public TaxonDaoImpl() {
        super(Taxon.class, "taxon");
    }

    /**
     * @param identifier Set the identifier
     * @param scientificName Set the scientificName
     * @return true if the name is valid, false otherwise
     */
    public final boolean verify(final String identifier,
            final String scientificName) {
        // TODO Auto-generated method stub
        return false;
    }

}
