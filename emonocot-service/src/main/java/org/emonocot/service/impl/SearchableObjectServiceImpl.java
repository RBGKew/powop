/**
 * 
 */
package org.emonocot.service.impl;

import org.emonocot.api.SearchableObjectService;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.persistence.dao.SearchableObjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jk00kg
 *
 */
@Service
public class SearchableObjectServiceImpl extends SearchableServiceImpl<SearchableObject, SearchableObjectDao> implements SearchableObjectService {

    @Autowired
    public void setSearchableObjectDao(SearchableObjectDao searchableObjectDao){
        super.dao = searchableObjectDao;
    }

}
