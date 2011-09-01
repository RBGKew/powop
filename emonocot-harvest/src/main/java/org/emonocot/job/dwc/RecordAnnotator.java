package org.emonocot.job.dwc;

import javax.sql.DataSource;

import org.emonocot.model.hibernate.OlapDateTimeUserType;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

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
    * @param sessionFactory Set the session factory
    */
   public final void setDataSource(
           final DataSource dataSource) {
       this.jdbcTemplate = new JdbcTemplate();
       jdbcTemplate.setDataSource(dataSource);
       jdbcTemplate.afterPropertiesSet();
   }

    /**
     *
     * @param family Set the family of taxa to be harvested
     * @param authorityName Set the authority name
     * @return the exit status
     */
    public final ExitStatus annotateRecords(final String family, String authorityName) {
        StringBuffer queryString = new StringBuffer(
          "insert into Annotation (annotatedObjId, annotatedObjType, jobId,  dateTime, authority_id, type) select t.id, 'Taxon', ");
        queryString.append(stepExecution.getJobExecutionId());
        queryString.append(", ");
        queryString.append(OlapDateTimeUserType.convert(new DateTime()));
        queryString.append(", ");
        queryString.append(authorityName);
        queryString.append(", 'Absent' from Taxon t where t.family = '");
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
