package org.emonocot.job.dwc;

import java.io.Serializable;

import org.emonocot.job.dwc.description.DescriptionProcessingException;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.Base;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileParseException;
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
public class DwCProcessingExceptionProcessListener extends HibernateDaoSupport
        implements ItemProcessListener<Base, Base>,
        ItemReadListener<Base>, StepExecutionListener {
   /**
    *
    */
   private Logger logger = LoggerFactory.getLogger(
           DwCProcessingExceptionProcessListener.class);

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
    public void beforeProcess(final Base item) {

    }

    /**
     * @param item the item to be processed
     * @param result the resulting object
     */
    public void afterProcess(final Base item, final Base result) {

    }

    /**
     * @param item the item to be processed
     * @param e the exception thrown
     */
    public final void onProcessError(final Base item, final Exception e) {
        if (e instanceof DescriptionProcessingException) {
            DarwinCoreProcessingException dwcpe
                = (DarwinCoreProcessingException) e;
            logger.debug(dwcpe.getCode() + " | " + dwcpe.getMessage());
            final Annotation annotation = new Annotation();
            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setCode(dwcpe.getCode());
            annotation.setText(dwcpe.getMessage());
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

    /**
     * @param base the object read
     */
    public void afterRead(Base base) {

    }

    /**
     *
     */
    public void beforeRead() {
        
    }

    /**
     * @param e the exception
     */
    public void onReadError(Exception e) {
        if(e instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException)e;
            logger.debug("FlatFileParseException | " + ffpe.getMessage());
            final Annotation annotation = new Annotation();
            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setCode("FlatFileParseException");
            annotation.setText(ffpe.getMessage());
            transactionTemplate.execute(
                    new TransactionCallback<Serializable>() {

                public Serializable doInTransaction(
                        final TransactionStatus status) {
                  return getSession().save(annotation);
                }
              });
        }
    }

}
