package org.emonocot.portal.remoting;

import org.emonocot.model.key.IdentificationKey;
import org.emonocot.model.pager.Page;
import org.emonocot.persistence.dao.IdentificationKeyDao;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 *
 */
@Repository
public class IdentificationKeyDaoImpl extends DaoImpl<IdentificationKey>
        implements IdentificationKeyDao {

    /**
     *
     */
    public IdentificationKeyDaoImpl() {
        super(IdentificationKey.class, "key");
    }

    /**
     *
     * @param example
     * @param ignoreCase
     * @param useLike
     * @return
     */
    public Page<IdentificationKey> searchByExample(IdentificationKey example,
            boolean ignoreCase, boolean useLike) {
        throw new UnsupportedOperationException(
                "Remote searching by example is unimplemented");
    }

    /**
    *
    * @param source Set the source of the identification key
    * @return an identification key
    */
    public final IdentificationKey findBySource(final String source) {
        throw new UnsupportedOperationException("not implemented");
    }

}
