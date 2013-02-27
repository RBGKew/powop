package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.emonocot.model.Image;
import org.emonocot.model.registry.Organisation;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.OrganisationDao;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author annapaola
 * 
 */
@Repository
public class OrganisationDaoImpl extends DaoImpl<Organisation> implements
		OrganisationDao {
	/**
    *
    */
	public OrganisationDaoImpl() {
		super(Organisation.class, "organisation");
	}

	@Override
	public Page<SolrDocument> searchForDocuments(String query,
			Integer pageSize, Integer pageNumber,
			Map<String, String> selectedFacets, String sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organisation loadObjectForDocument(SolrDocument solrDocument) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    protected Page<Organisation> page(Integer page, Integer size) {
		HttpEntity<Organisation> requestEntity = new HttpEntity<Organisation>(httpHeaders);
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
    	
    	ParameterizedTypeReference<DefaultPageImpl<Organisation>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<Organisation>>() {};
        HttpEntity<DefaultPageImpl<Organisation>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
	}
	
	@Override
	public final List<Organisation> list(final Integer page, final Integer size, final String fetch) {
		return this.page(page, size).getRecords();
    }

}
