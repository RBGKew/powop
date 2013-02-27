package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.Image;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.ImageDao;
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
public class ImageDaoImpl extends DaoImpl<Image> implements ImageDao {

    /**
     *
     */
    public ImageDaoImpl() {
        super(Image.class, "image");
    }

    public Page<Image> searchByExample(Image example, boolean ignoreCase,
            boolean useLike) {
        throw new UnsupportedOperationException("Remote searching by example is unimplemented");
    }
    
    @Override
    protected Page<Image> page(Integer page, Integer size) {
    	HttpEntity<Image> requestEntity = new HttpEntity<Image>(httpHeaders);
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
    	
    	ParameterizedTypeReference<DefaultPageImpl<Image>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<Image>>() {};
        HttpEntity<DefaultPageImpl<Image>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
    }

	@Override
	public final List<Image> list(final Integer page, final Integer size, final String fetch) {
		return this.page(page, size).getRecords();
    }
}
