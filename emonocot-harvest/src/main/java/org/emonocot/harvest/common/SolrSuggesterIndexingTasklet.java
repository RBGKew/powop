package org.emonocot.harvest.common;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class SolrSuggesterIndexingTasklet implements Tasklet {

	private SolrClient solrClient;

	private String suggester;

	public String getSuggester() {
		return suggester;
	}

	public void setSuggester(String suggester) {
		this.suggester = suggester;
	}

	public SolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(SolrClient solrClient) {
		this.solrClient = solrClient;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext context){
		SolrQuery query = new SolrQuery();
		query.setRequestHandler("/suggest");
		query.setParam("suggest.dictionary", suggester);
		query.setParam("suggest.build", true);
			try {
				solrClient.query(query);
			} catch (Exception e) {
				e.printStackTrace();
			}

		return RepeatStatus.FINISHED;
	}
}
