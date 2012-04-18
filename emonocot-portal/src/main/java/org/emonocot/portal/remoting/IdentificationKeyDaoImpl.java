/**
 * 
 */
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
public class IdentificationKeyDaoImpl extends DaoImpl<IdentificationKey> implements
        IdentificationKeyDao {
    
    public IdentificationKeyDaoImpl(){
        super(IdentificationKey.class, "key");
    }
        

    public Page<IdentificationKey> searchByExample(IdentificationKey example,
            boolean ignoreCase, boolean useLike) {
        throw new UnsupportedOperationException("Remote searching by example is unimplemented");
    }

}
