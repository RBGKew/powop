package org.emonocot.harvest.common;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class SolrOptimizingTasklet implements Tasklet {
	
	private static Logger logger = LoggerFactory.getLogger(SolrOptimizingTasklet.class);
	
	private String core;
	
	private Integer maxSegments;
	
	private SolrServer solrServer;	

	public void setCore(String core) {
		this.core = core;
	}

	public void setMaxSegments(Integer maxSegments) {
		this.maxSegments = maxSegments;
	}

	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		CoreAdminResponse coreAdminResponse = CoreAdminRequest.getStatus(core, solrServer);
		NamedList<Object> index = (NamedList<Object>)coreAdminResponse.getCoreStatus(core).get("index");
		Integer segmentCount = (Integer)index.get("segmentCount");
		
		if(segmentCount < maxSegments) {
			logger.debug("Core " + core + " only has " + segmentCount + " segments, skipping optimization");
		} else {
			logger.debug("Core " + core + " has " + segmentCount + " segments, starting optimization");
			solrServer.optimize(true, true);
			logger.debug("Core " + core + " optimized");
		}
		
		return RepeatStatus.FINISHED;
	}

}
