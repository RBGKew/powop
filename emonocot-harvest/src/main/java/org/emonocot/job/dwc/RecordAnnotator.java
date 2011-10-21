package org.emonocot.job.dwc;

import javax.sql.DataSource;

import org.emonocot.model.hibernate.OlapDateTimeUserType;
import org.joda.time.DateTime;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ben
 *
 */
public class RecordAnnotator
    implements StepExecutionListener {

    /**
     *
     */
    private StepExecution stepExecution;

    /**
     *
     */
    private JdbcTemplate jdbcTemplate;

   /**
    *
    * @param dataSource set the data source
    */
   public final void setDataSource(
           final DataSource dataSource) {
       this.jdbcTemplate = new JdbcTemplate();
       jdbcTemplate.setDataSource(dataSource);
       jdbcTemplate.afterPropertiesSet();
   }

    /**
     * @param family Set the family
     * @param authority Set the authority
     * @return the exit status
     */
    public final ExitStatus annotateRecords(final String family,
            final String authority) {
        Integer sourceId = jdbcTemplate
                .queryForInt("Select id from source where identifier = '"
                        + authority  + "'");
        StringBuffer queryString = new StringBuffer(
          "insert into Annotation (annotatedObjId, annotatedObjType, jobId,  dateTime, source_id, type) select t.id, 'Taxon', ");
        queryString.append(stepExecution.getJobExecutionId());
        queryString.append(", ");
        queryString.append(OlapDateTimeUserType.convert(new DateTime()));
        queryString.append(", ");
        queryString.append(sourceId);
        queryString.append(", 'Absent' from Taxon t left join Taxon a on (t.accepted_id = a.id) where t.family = '");
        queryString.append(family);
        queryString.append("' or a.family = '");
        queryString.append(family);
        queryString.append("'");

        jdbcTemplate.update(queryString.toString());
        return ExitStatus.COMPLETED;
    }

    /**
     * @param newStepExecution Set the step execution
     */
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

    /**
     * @param newStepExecution Set the step execution
     * @return the exit status
     */
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }
}
