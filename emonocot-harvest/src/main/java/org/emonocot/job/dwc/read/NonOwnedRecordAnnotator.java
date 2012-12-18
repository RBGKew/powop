package org.emonocot.job.dwc.read;

import org.emonocot.model.hibernate.OlapDateTimeUserType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;


public class NonOwnedRecordAnnotator extends AbstractRecordAnnotator {
	private Logger logger = LoggerFactory.getLogger(OwnedRecordAnnotator.class);
	
	
	public final ExitStatus annotateRecords(String sourceName, String annotatedObjType) {
      Integer authorityId = jdbcTemplate.queryForInt("Select id from Organisation where identifier = '" + sourceName + "'");
      stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());
      String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select o.id, ':annotatedObjType', ':jobId', :dateTime, :authorityId, 'Warn', 'Absent', ':annotatedObjType' from :annotatedObjType o where o.authority_id = :authorityId";
      queryString = queryString.replaceAll(":authorityId", authorityId.toString());
      queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
      queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
      queryString = queryString.replaceAll(":annotatedObjType", annotatedObjType);
      logger.info(queryString);
      jdbcTemplate.update(queryString);
      return ExitStatus.COMPLETED;
    }
	
	public final ExitStatus annotateRelatedRecords(String sourceName, String annotatedObjType, String queryString, String family, String subfamily, String tribe, String subtribe) {
	      Integer authorityId = jdbcTemplate.queryForInt("Select id from Organisation where identifier = '" + sourceName + "'");
	      stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());
	      
	      String subsetRank = null;
	      String subsetValue = null;
	      
	      if(subtribe != null) {
	    	  subsetRank = "subtribe";
	    	  subsetValue = subtribe;
	      } else if(tribe != null) {
	    	  subsetRank = "tribe";
	    	  subsetValue = tribe;
	      } else if(subfamily != null) {
	    	  subsetRank = "subfamily";
	    	  subsetValue = subfamily;
	      } else if(family != null) {
	    	  subsetRank = "family";
	    	  subsetValue = family;
	      }
	      
	      queryString = queryString.replaceAll(":authorityId", authorityId.toString());
	      if(subsetValue != null) {
	          queryString = queryString.replaceAll(":subsetRank", subsetRank);
              queryString = queryString.replaceAll(":subsetValue", subsetValue);
	      }
	      queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
	      queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
	      queryString = queryString.replaceAll(":annotatedObjType", annotatedObjType);
	      logger.info(queryString);
	      jdbcTemplate.update(queryString);
	      return ExitStatus.COMPLETED;
	    }
}
