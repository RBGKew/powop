package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.Concept;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.ConceptDao;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ConceptDaoImpl extends DaoImpl<Concept> implements ConceptDao {

    /**
     *
     */
    public ConceptDaoImpl() {
        super(Concept.class, "term");
    }

    public Page<Concept> searchByExample(Concept example, boolean ignoreCase,
            boolean useLike) {
        throw new UnsupportedOperationException("Remote searching by example is unimplemented");
    }
    
    @Override
    protected Page<Concept> page(Integer page, Integer size) {
    	HttpEntity<Concept> requestEntity = new HttpEntity<Concept>(httpHeaders);
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
    	
    	ParameterizedTypeReference<DefaultPageImpl<Concept>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<Concept>>() {};
        HttpEntity<DefaultPageImpl<Concept>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
    }

	@Override
	public final List<Concept> list(final Integer page, final Integer size, final String fetch) {
		return this.page(page, size).getRecords();
    }
}
