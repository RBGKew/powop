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
