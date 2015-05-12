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
			chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("record.identifier", results.get(0));
			contribution.setExitStatus(new ExitStatus("MORE_RESULTS"));
		}
		
		
		return RepeatStatus.FINISHED;
	}

}
