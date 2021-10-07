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
package org.powo.harvest.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.powo.model.Searchable;
import org.powo.persistence.hibernate.SolrIndexingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

public class SolrIndexingWriter extends HibernateDaoSupport implements ItemWriter<Long> {
	private final Logger log = LoggerFactory.getLogger(SolrIndexingWriter.class);

	private Class type;

	private SolrIndexingInterceptor solrIndexingInterceptor;

	/**
	 * @param type the type to set
	 */
	public void setType(Class type) {
		this.type = type;
	}

	/**
	 * @param solrIndexingInterceptor the solrIndexingInterceptor to set
	 */
	public void setSolrIndexingInterceptor(SolrIndexingInterceptor solrIndexingInterceptor) {
		this.solrIndexingInterceptor = solrIndexingInterceptor;
	}

	@Override
	public void write(List<? extends Long> items) throws Exception {
		// Loading all data required for indexing the Taxon before this step is not feasible due to the volume of data 
		// (and relationships). Therefore this method receives a Taxon id and loads it directly. This enables lazy loading of
		// relationships, which doesn't work if you try and move the loading earlier e.g. into the reader/processor.
		var searchables = items.stream()
			.map(i -> (Searchable) currentSession().load(type, i))
			.collect(Collectors.toList());
		solrIndexingInterceptor.indexObjects(searchables);
	}
}
