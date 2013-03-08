package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.emonocot.model.registry.Resource;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.ResourceDao;
import org.joda.time.DateTime;
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
public class ResourceDaoImpl extends DaoImpl<Resource> implements ResourceDao {

    /**
     *
     */
    public ResourceDaoImpl() {
        super(Resource.class, "resource");
    }

    /**
     * @param sourceId Set the source identifier
     * @return the total number of jobs for a given source
     */
    public final Long count(final String sourceId) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected Page<Resource> page(Integer page, Integer size) {
    	HttpEntity<Resource> requestEntity = new HttpEntity<Resource>(httpHeaders);
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
    	
    	ParameterizedTypeReference<DefaultPageImpl<Resource>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<Resource>>() {};
        HttpEntity<DefaultPageImpl<Resource>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
    }

    @Override
	public final List<Resource> list(final Integer page, final Integer size, final String fetch) {
    	return this.page(page, size).getRecords();
    }

    /**
     * @param id Set the job id
     * @return the job
     */
    public final Resource findByJobId(final Long id) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public boolean isHarvesting() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Resource> listResourcesToHarvest(Integer limit, DateTime now, String fetch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<SolrDocument> searchForDocuments(String query,
			Integer pageSize, Integer pageNumber,
			Map<String, String> selectedFacets, String sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource loadObjectForDocument(SolrDocument solrDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Resource> list(String sourceId, Integer page, Integer size) {
		// TODO Auto-generated method stub
		return null;
	}

}
