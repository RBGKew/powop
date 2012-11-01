package org.emonocot.portal.remoting;

import org.emonocot.model.Reference;
import org.emonocot.persistence.dao.ReferenceDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ReferenceDaoImpl extends DaoImpl<Reference> implements
        ReferenceDao {

    /**
     *
     */
    public ReferenceDaoImpl() {
        super(Reference.class, "reference");
    }

    /**
     * @param source The source of the reference you want to find
     * @return a reference or null if it does not exist
     */
    public final Reference findByBibliographicCitation(final String source) {
        // TODO Auto-generated method stub
        return null;
    }
}
