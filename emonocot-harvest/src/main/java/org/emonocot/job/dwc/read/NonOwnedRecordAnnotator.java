package org.emonocot.job.dwc.read;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public class NonOwnedRecordAnnotator extends AbstractRecordAnnotator implements Tasklet {
	private Logger logger = LoggerFactory.getLogger(NonOwnedRecordAnnotator.class);

	private String annotatedObjType;
	
	public void setAnnotatedObjType(String annotatedObjType) {
		this.annotatedObjType = annotatedObjType;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String annotationQuery = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select o.id, :annotatedObjType, :jobId, :dateTime, :authorityId, 'Warn', 'Absent', :annotatedObjType from #annotatedObjType o where o.authority_id = :authorityId";
	    annotationQuery = annotationQuery.replaceAll("#annotatedObjType", annotatedObjType);
	    Map<String,Object> annotationParameters = new HashMap<String,Object>();
		annotationParameters.put("annotatedObjType", annotatedObjType);	 
	    super.annotate(annotationQuery, annotationParameters);
		return RepeatStatus.FINISHED;
	}
}
