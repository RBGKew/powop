package org.emonocot.job.dwc;


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
	public final ExitStatus annotateRecords(String sourceName, String annotatedObjType) {
      Integer authorityId = jdbcTemplate.queryForInt("Select id from Organisation where identifier = '" + sourceName + "'");
      String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select o.id, ':annotatedObjType', ':jobId', :dateTime, :authorityId, 'Warn', 'Absent', ':annotatedObjType' from :annotatedObjType o where o.authority_id = :authorityId";
      stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());
      queryString = queryString.replaceAll(":authorityId", authorityId.toString());
      queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
      queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
      queryString = queryString.replaceAll(":annotatedObjType", annotatedObjType);
      jdbcTemplate.update(queryString);
      return ExitStatus.COMPLETED;
    }
}
