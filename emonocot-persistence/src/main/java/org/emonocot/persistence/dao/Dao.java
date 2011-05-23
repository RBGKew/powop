package org.emonocot.persistence.dao;

import org.emonocot.model.common.Base;

public interface Dao<T extends Base> {
    
    T load(String identifier);
    
    T find(String identifier);

}
