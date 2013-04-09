/**
 * 
 */
package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.emonocot.model.Comment;
import org.emonocot.pager.CellSet;
import org.emonocot.pager.Cube;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.CommentDao;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 *
 */
@Repository
public class CommentDaoImpl extends DaoImpl<Comment> implements CommentDao {

    /**
     * 
     */
    public CommentDaoImpl() {
        super(Comment.class, "comment");
    }
    
    @Override
    protected Page<Comment> page(Integer page, Integer size) {
    	HttpEntity<Comment> requestEntity = new HttpEntity<Comment>(httpHeaders);
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
    	
    	ParameterizedTypeReference<DefaultPageImpl<Comment>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<Comment>>() {};
        HttpEntity<DefaultPageImpl<Comment>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
    }
    
	@Override
	public final List<Comment> list(final Integer page, final Integer size, final String fetch) {
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
	public Comment loadObjectForDocument(SolrDocument solrDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellSet analyse(String rows, String cols, Integer firstCol,
			Integer maxCols, Integer firstRow, Integer maxRows,
			Map<String, String> selectedFacets, String[] array, Cube cube) {
		// TODO Auto-generated method stub
		return null;
	}
}
