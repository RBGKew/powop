package org.emonocot.job.dwc;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author ben
 *
 */
public class RecordAnnotator extends HibernateDaoSupport
    implements StepExecutionListener {

    /**
     *
     */
    private StepExecution stepExecution;

   /**
    *
    * @param sessionFactory Set the session factory
    */
   public final void setHibernateSessionFactory(
           final SessionFactory sessionFactory) {
       this.setSessionFactory(sessionFactory);
   }

    /**
     *
     * @param family Set the family of taxa to be harvested
     * @return the exit status
     */
    public final ExitStatus annotateRecords(final String family) {
        StringBuffer queryString = new StringBuffer(
          "insert into Annotation (annotatedObjId, annotatedObjType, jobId) select t.id, 'Taxon', ");
        queryString.append(stepExecution.getJobExecutionId());
        queryString.append("L from Taxon t where t.family = :family");

        Query query = getSession().createQuery(queryString.toString());
        query.setParameter("family", family);
        int createdAnnotations = query.executeUpdate();
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
