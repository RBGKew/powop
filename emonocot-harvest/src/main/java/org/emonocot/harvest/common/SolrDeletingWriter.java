package org.emonocot.harvest.common;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.springframework.batch.item.ItemWriter;

/**
 *
 * @author ben
 *
 */
public class SolrDeletingWriter implements ItemWriter<String> {
    
    private SolrServer solrServer;

	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	public void write(List<? extends String> documentIdentifiers) throws Exception {
		if (!documentIdentifiers.isEmpty()) {

			try {
				
				solrServer.deleteById((List) documentIdentifiers);
				solrServer.commit(true,true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
    }
}
