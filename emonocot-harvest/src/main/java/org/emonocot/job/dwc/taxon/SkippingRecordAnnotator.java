package org.emonocot.job.dwc.taxon;


import java.util.HashMap;
import java.util.Map;

import org.emonocot.job.dwc.read.AbstractRecordAnnotator;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 *
 * @author ben
 *
 */
public class SkippingRecordAnnotator extends AbstractRecordAnnotator implements Tasklet { 

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
	     
	    String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select distinct t.id, 'Taxon', :jobId, :dateTime, :authorityId, 'Warn', 'Absent', 'Taxon' from Taxon t inner join annotation a  on (t.id = a.annotatedObjId) where a.annotatedObjType = 'Taxon' and a.authority_id = :authorityId";
	    Map<String, Object> queryParameters = new HashMap<String,Object>();
        annotate(queryString, queryParameters);
		return RepeatStatus.FINISHED;
	}
}
