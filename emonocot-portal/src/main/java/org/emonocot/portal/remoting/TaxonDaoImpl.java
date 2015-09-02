/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.remoting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.Taxon;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.TaxonDao;
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
public class TaxonDaoImpl extends DaoImpl<Taxon> implements TaxonDao {

	/**
	 *
	 */
	public TaxonDaoImpl() {
		super(Taxon.class, "taxon");
	}

	/**
	 * Returns the root taxa in the resultset (accepted taxa with no parent
	 * taxon).
	 * @param pageSize
	 *            The maximum number of results to return
	 * @param pageNumber
	 *            The offset (in pageSize chunks, 0-based) from the beginning of
	 *            the recordset
	 * @param fetch
	 *            Set the fetch profile
	 *
	 * @return a Page from the resultset
	 */
	public final List<Taxon> loadChildren(String identifier,
			final Integer pageSize, final Integer pageNumber, final String fetch) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Taxon> searchByExample(Taxon example, boolean ignoreCase,
			boolean useLike) {
		throw new UnsupportedOperationException("Remote searching by example is unimplemented");
	}

	@Override
	protected Page<Taxon> page(Integer page, Integer size) {
		HttpEntity<Taxon> requestEntity = new HttpEntity<Taxon>(httpHeaders);
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

		ParameterizedTypeReference<DefaultPageImpl<Taxon>> typeRef = new ParameterizedTypeReference<DefaultPageImpl<Taxon>>() {};
		HttpEntity<DefaultPageImpl<Taxon>> responseEntity = restTemplate.exchange(baseUri + "/{resource}?limit={limit}&start={start}", HttpMethod.GET,
				requestEntity, typeRef,uriVariables);
		return responseEntity.getBody();
	}

	@Override
	public final List<Taxon> list(final Integer page, final Integer size, final String fetch) {
		return this.page(page, size).getRecords();
	}

}
