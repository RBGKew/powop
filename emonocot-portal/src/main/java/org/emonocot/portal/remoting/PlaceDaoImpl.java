/**
 * 
 */
package org.emonocot.portal.remoting;

import org.emonocot.model.Place;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.PlaceDao;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 *
 */
@Repository
public class PlaceDaoImpl extends DaoImpl<Place> implements PlaceDao {

	public PlaceDaoImpl() {
		super(Place.class, "place");
	}

	public Page<Place> searchByExample(Place example, boolean ignoreCase,
			boolean useLike) {
		throw new UnsupportedOperationException("Remote searching by example is not enabled");
	}

}
