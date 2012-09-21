package org.emonocot.job.dwc.taxon;


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
	 * @param family Set the family of taxa to be harvested
	 * @param subfamily TODO
	 * @param tribe TODO
	 * @param subtribe TODO
	 * @return the exit status
     */
	public final ExitStatus annotateRecords(String sourceName, String family, String subfamily, String tribe, String subtribe) {
      Integer sourceId = jdbcTemplate.queryForInt("Select id from source where identifier = '" + sourceName + "'");
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
      } else {
    	  subsetRank = "family";
    	  subsetValue = family;
      }
      String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, source_id, type, code, recordType) select t.id, 'Taxon', ':jobId', :dateTime, :sourceId, 'Warn', 'Absent', 'Taxon' from Taxon t left join Taxon a on (t.accepted_id = a.id) where t.:subsetRank = ':subsetValue' or a.:subsetRank = ':subsetValue'";
      
      queryString = queryString.replaceAll(":sourceId", sourceId.toString());
      queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
      queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
      queryString = queryString.replaceAll(":subsetRank", subsetRank);
      queryString = queryString.replaceAll(":subsetValue", subsetValue);
      jdbcTemplate.update(queryString);
      return ExitStatus.COMPLETED;
    }
}
