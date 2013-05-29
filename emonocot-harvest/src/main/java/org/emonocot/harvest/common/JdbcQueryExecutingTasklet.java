package org.emonocot.harvest.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcQueryExecutingTasklet implements Tasklet {
	
	private static Logger logger = LoggerFactory.getLogger(JdbcQueryExecutingTasklet.class);

	private JdbcTemplate jdbcTemplate;
	
	private String query;	
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		logger.debug("Executing " + query);
		jdbcTemplate.execute(query);
		
		return RepeatStatus.FINISHED;
	}

}
