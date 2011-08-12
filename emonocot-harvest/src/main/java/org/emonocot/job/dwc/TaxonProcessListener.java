package org.emonocot.job.dwc;

import java.io.Serializable;

import org.emonocot.job.dwc.taxon.TaxonProcessingException;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
/**
 *
 * @author ben
 *
 */
public class TaxonProcessListener extends HibernateDaoSupport implements
        ItemProcessListener<Taxon, Taxon>, StepExecutionListener {
   /**
    *
    */
   private Logger logger = LoggerFactory.getLogger(TaxonProcessListener.class);

   /**
    *
    */
   private StepExecution stepExecution;

   /**
    *
    */
   private TransactionTemplate transactionTemplate = null;

   /**
    *
    * @param transactionManager Set the transaction manager
    */
   public final void setTransactionManager(
           final PlatformTransactionManager transactionManager) {
       Assert.notNull(transactionManager,
               "The 'transactionManager' argument must not be null.");
       this.transactionTemplate = new TransactionTemplate(transactionManager);
       this.transactionTemplate.setPropagationBehavior(
               TransactionDefinition.PROPAGATION_REQUIRES_NEW);
   }

  /**
   *
   * @param sessionFactory Set the session factory
   */
  public final void setHibernateSessionFactory(
          final SessionFactory sessionFactory) {
      this.setSessionFactory(sessionFactory);
  }

    /**
     * @param item the item to be processed
     */
    public void beforeProcess(final Taxon item) {

    }

    /**
     * @param item the item to be processed
     * @param result the resulting object
     */
    public void afterProcess(final Taxon item, final Taxon result) {

    }

    /**
     * @param item the item to be processed
     * @param e the exception thrown
     */
    public final void onProcessError(final Taxon item, final Exception e) {
        if (e instanceof TaxonProcessingException) {
            TaxonProcessingException tpe = (TaxonProcessingException) e;
            logger.debug(tpe.getCode() + " | " + tpe.getMessage());
            final Annotation annotation = new Annotation();
            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setCode(tpe.getCode());
            annotation.setText(tpe.getMessage());
            transactionTemplate.execute(
                    new TransactionCallback<Serializable>() {

                public Serializable doInTransaction(
                        final TransactionStatus status) {
                  return getSession().save(annotation);
                }
              });
        }
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
