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
package org.emonocot.harvest.common;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.Searchable;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author ben
 *
 */
public class SolrIndexingWriter
extends HibernateDaoSupport implements ItemWriter<Long> {

	private Class type;

	private SolrIndexingListener solrIndexingListener;

	/**
	 * @param type the type to set
	 */
	public void setType(Class type) {
		this.type = type;
	}

	/**
	 * @param solrIndexingListener the solrIndexingListener to set
	 */
	public void setSolrIndexingListener(SolrIndexingListener solrIndexingListener) {
		this.solrIndexingListener = solrIndexingListener;
	}

	public void index(Long identifier, Class type) {
		Searchable searchable = (Searchable)getSession().load(type, identifier);
		solrIndexingListener.indexObject(searchable);
	}

	public void write(List<? extends Long> identifiers) throws Exception {
		List<Searchable> searchables = new ArrayList<Searchable>();
		for (Long l : identifiers) {
			searchables.add((Searchable)getSession().load(type, l));

		}
		solrIndexingListener.indexObjects(searchables);
	}
}
