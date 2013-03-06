package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.emonocot.model.auth.User;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.UserDao;
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
public class UserDaoImpl extends DaoImpl<User> implements UserDao {

    /**
     *
     */
    public UserDaoImpl() {
        super(User.class, "user");
    }
    
    @Override
    protected Page<User> page(Integer page, Integer size) {
    	HttpEntity<User> requestEntity = new HttpEntity<User>(httpHeaders);
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
    	
    	ParameterizedTypeReference<DefaultPageImpl<User>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<User>>() {};
        HttpEntity<DefaultPageImpl<User>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
    }
    
    @Override
	public final List<User> list(final Integer page, final Integer size, final String fetch) {
    	return this.page(page, size).getRecords();
    }

	@Override
	public Page<SolrDocument> searchForDocuments(String query,
			Integer pageSize, Integer pageNumber,
			Map<String, String> selectedFacets, String sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User loadObjectForDocument(SolrDocument solrDocument) {
		// TODO Auto-generated method stub
		return null;
	}
}
