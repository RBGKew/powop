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
     * @param family Set the family of taxa to be harvested
     * @param sourceName Set the Source name
     * @return the exit status
     */
	public final ExitStatus annotateRecords(String family, String sourceName) {
      Integer sourceId = jdbcTemplate.queryForInt("Select id from source where identifier = '" + sourceName + "'");
      String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, source_id, type, code, recordType) select t.id, 'Taxon', ':jobId', :dateTime, :sourceId, 'Warn', 'Absent', 'Taxon' from Taxon t left join Taxon a on (t.accepted_id = a.id) where t.family = ':family' or a.family = ':family'";
      queryString = queryString.replaceAll(":sourceId", sourceId.toString());
      queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
      queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
      queryString = queryString.replaceAll(":family", family);
      jdbcTemplate.update(queryString);
      return ExitStatus.COMPLETED;
    }
}
