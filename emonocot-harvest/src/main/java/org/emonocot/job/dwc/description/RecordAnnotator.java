package org.emonocot.job.dwc.description;


import org.emonocot.job.dwc.AbstractRecordAnnotator;
import org.emonocot.model.hibernate.OlapDateTimeUserType;
import org.joda.time.DateTime;
import org.springframework.batch.core.ExitStatus;

/**
 *
 * @author ben
 *
 */
public class RecordAnnotator extends AbstractRecordAnnotator { 

	/**
     *
     * @param sourceName Set the Source name
     * @return the exit status
     */
	public final ExitStatus annotateRecords(String sourceName) {
      Integer sourceId = jdbcTemplate.queryForInt("Select id from source where identifier = '" + sourceName + "'");
      String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, source_id, type, code, recordType) select t.id, 'TextContent', ':jobId', :dateTime, :sourceId, 'Warn', 'Absent', 'TextContent' from TextContent t where t.authority_id = :sourceId";
      stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());
      queryString = queryString.replaceAll(":sourceId", sourceId.toString());
      queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
      queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
      jdbcTemplate.update(queryString);
      return ExitStatus.COMPLETED;
    }
}
