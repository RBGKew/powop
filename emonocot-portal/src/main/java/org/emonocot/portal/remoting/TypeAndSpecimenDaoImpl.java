package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.pager.CellSet;
import org.emonocot.pager.Cube;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.TypeAndSpecimenDao;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

@Repository
public class TypeAndSpecimenDaoImpl extends DaoImpl<TypeAndSpecimen> implements
		TypeAndSpecimenDao {

	public TypeAndSpecimenDaoImpl() {
		super(TypeAndSpecimen.class,"typeAndSpecimen");
	}

	@Override
	public TypeAndSpecimen findByCatalogNumber(String catalogNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    protected Page<TypeAndSpecimen> page(Integer page, Integer size) {
		HttpEntity<TypeAndSpecimen> requestEntity = new HttpEntity<TypeAndSpecimen>(httpHeaders);
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
    	
    	ParameterizedTypeReference<DefaultPageImpl<TypeAndSpecimen>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<TypeAndSpecimen>>() {};
        HttpEntity<DefaultPageImpl<TypeAndSpecimen>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
                requestEntity, typeRef,uriVariables);
        return responseEntity.getBody();
	}

	@Override
	public final List<TypeAndSpecimen> list(final Integer page, final Integer size, final String fetch) {
		return this.page(page, size).getRecords();
    }

	@Override
	public CellSet analyse(String rows, String cols, Integer firstCol,
			Integer maxCols, Integer firstRow, Integer maxRows,
			Map<String, String> selectedFacets, String[] array, Cube cube)
			throws SolrServerException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
