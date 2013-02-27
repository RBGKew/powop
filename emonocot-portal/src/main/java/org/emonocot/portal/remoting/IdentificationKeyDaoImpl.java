package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.IdentificationKey;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.IdentificationKeyDao;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    
    @Override
    protected Page<IdentificationKey> page(Integer page, Integer size) {
    	HttpEntity<IdentificationKey> requestEntity = new HttpEntity<IdentificationKey>(httpHeaders);
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
    	
    	ParameterizedTypeReference<DefaultPageImpl<IdentificationKey>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<IdentificationKey>>() {};
        HttpEntity<DefaultPageImpl<IdentificationKey>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();    	
    }
    
	@Override
	public final List<IdentificationKey> list(final Integer page, final Integer size, final String fetch) {
		return this.page(page, size).getRecords();
    }

}
