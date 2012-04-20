package org.emonocot.persistence.dao;

import org.emonocot.model.key.IdentificationKey;

/**
 *
 * @author ben
 *
 */
public interface IdentificationKeyDao extends SearchableDao<IdentificationKey> {

   /**
    *
    * @param source Set the source of the identification key
    * @return an identification key
    */
    IdentificationKey findBySource(String source);

}
