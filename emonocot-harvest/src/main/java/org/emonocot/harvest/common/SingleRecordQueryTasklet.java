package org.emonocot.harvest.common;


import java.util.List;

import org.hibernate.Query;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
public class SingleRecordQueryTasklet extends HibernateDaoSupport implements
		Tasklet {
	
	/**
	 *
	 */
	private String queryString;
	
	/**
	 *
	 * @param queryString Set the query string
	 */
	public void setQueryString(final String queryString) {
		this.queryString = queryString;
	}

	@Override
	@Transactional
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Query query = this.getSession().createQuery(queryString);
		query.setMaxResults(1);
		query.setFirstResult(0);
		
		List<String> results = (List<String>)query.list();
		
		if(results.size() == 0) {
			contribution.setExitStatus(new ExitStatus("NO_MORE_RESULTS"));
		} else {
			chunkContext.getStepContext().getJobExecutionContext().put("record.identifier", results.get(0));
			contribution.setExitStatus(new ExitStatus("MORE_RESULTS"));
		}
		
		
		return RepeatStatus.FINISHED;
	}

}
