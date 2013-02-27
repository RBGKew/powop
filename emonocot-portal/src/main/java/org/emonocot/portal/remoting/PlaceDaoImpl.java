/**
 * 
 */
package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.Place;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.PlaceDao;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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

	@Override
    protected Page<Place> page(Integer page, Integer size) {
		HttpEntity<Place> requestEntity = new HttpEntity<Place>(httpHeaders);
    	Map<String,Object> uriVariables = new HashMap<String,Object>();
    	uriVariables.put("resource", resourceDir);
    	if(size == null) {
    		uriVariables.put("limit", "");
    	} else {
    		uriVariables.put("limit", size);
    	}
    	
    	if(page == null) {
    		uriVariables.put("start", "");
    	} else {
    		uriVariables.put("start", page);
    	}
    	
    	ParameterizedTypeReference<DefaultPageImpl<Place>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<Place>>() {};
        HttpEntity<DefaultPageImpl<Place>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
	}
	
	@Override
	public final List<Place> list(final Integer page, final Integer size, final String fetch) {
		return this.page(page, size).getRecords();
    }

}
