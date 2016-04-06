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
package org.emonocot.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

public class EmbeddedSolrServerFactoryBean implements FactoryBean<SolrClient> {

	private SolrClient solrClient = null;

	private CoreContainer coreContainer = null;

	private Resource solrHome = null;

	public void setSolrHome(Resource solrHome) {
		this.solrHome = solrHome;
	}

	public void shutdown() throws IOException {
		if(solrClient != null) {
			solrClient.close();
		}
		if(coreContainer != null) {
			coreContainer.shutdown();
		}
	}

	@Override
	public SolrClient getObject() throws Exception {
		if(solrClient == null) {
			System.setProperty("solr.solr.home", solrHome.getFile().getAbsolutePath());
			coreContainer = new CoreContainer();
			coreContainer.load();
			solrClient = new EmbeddedSolrServer(coreContainer, "powop");
		}
		return solrClient;
	}

	@Override
	public Class<?> getObjectType() {
		return SolrClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
