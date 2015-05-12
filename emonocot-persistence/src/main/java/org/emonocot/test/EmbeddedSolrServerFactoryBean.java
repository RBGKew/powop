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

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

public class EmbeddedSolrServerFactoryBean implements FactoryBean<SolrServer> {

	private SolrServer solrServer = null;
	
	private CoreContainer coreContainer = null;
	
	private Resource solrHome = null;
	
	public void setSolrHome(Resource solrHome) {
		this.solrHome = solrHome;
	}
	
	public void shutdown() {
		solrServer.shutdown();
		coreContainer.shutdown();
	}
	
	@Override
	public SolrServer getObject() throws Exception {
		if(solrServer == null) {
			 System.setProperty("solr.solr.home", solrHome.getFile().getAbsolutePath());
			 CoreContainer.Initializer initializer = new CoreContainer.Initializer();
			 coreContainer = initializer.initialize();
			 solrServer = new EmbeddedSolrServer(coreContainer, "collection1");
		}
		return solrServer;
	}

	@Override
	public Class<?> getObjectType() {
		return SolrServer.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
