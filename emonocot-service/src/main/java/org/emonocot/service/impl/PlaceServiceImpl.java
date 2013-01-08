/**
 * 
 */
package org.emonocot.service.impl;

import org.emonocot.api.PlaceService;
import org.emonocot.model.Place;
import org.emonocot.persistence.dao.PlaceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jk00kg
 *
 */
@Service
public class PlaceServiceImpl extends SearchableServiceImpl<Place, PlaceDao> implements
		PlaceService {

    /**
     *
     * @param placeDao Set the reference dao
     */
    @Autowired
    public final void setPlaceDao(final PlaceDao placeDao) {
        super.dao = placeDao;
    }
}
