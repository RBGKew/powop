package org.emonocot.job.dwc;

import java.io.Serializable;

import org.emonocot.job.dwc.description.DescriptionProcessingException;
import org.emonocot.job.dwc.taxon.CannotFindRecordException;
import org.emonocot.model.source.Source;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.Base;
import org.emonocot.model.common.RecordType;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
import org.springframework.validation.BindException;
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
    */
   private Source source = null;

   /**
    *
    */
   private String sourceName;

   /**
    *
    */
   private static final String PROCESS_CORE_FILE = "processCoreFile";

   /**
    *
    */
    private static final String PROCESS_DESCRIPTION_FILE
        = "processDescriptionFile";

    /**
     *
     */
    private static final String PROCESS_IMAGE_FILE
        = "processImageFile";

    /**
    *
    */
   private static final String PROCESS_REFERENCE_FILE
       = "processReferenceFile";

  /**
   *
   * @param sourceName Set the name of the Source
   */
   public final void setSourceName(final String sourceName) {
     this.sourceName = sourceName;
   }

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
        logger.debug("Process Error " + e.getMessage());
        if (e instanceof DarwinCoreProcessingException) {
            DarwinCoreProcessingException dwcpe
                = (DarwinCoreProcessingException) e;
            logger.debug(dwcpe.getCode() + " | " + dwcpe.getMessage());
            final Annotation annotation = new Annotation();
            annotation.setRecordType(getRecordType());
            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setCode(dwcpe.getCode());
            annotation.setRecordType(dwcpe.getRecordType());
            annotation.setValue(dwcpe.getValue());
            annotation.setText(dwcpe.getMessage());
            annotation.setSource(getSource());
            annotation.setType(dwcpe.getType());
            try {
                transactionTemplate
                        .execute(new TransactionCallback<Serializable>() {

                            public Serializable doInTransaction(
                                    final TransactionStatus status) {
                                return getSession().save(annotation);
                            }
                        });
            } catch (Throwable t) {
                logger.error(t.getMessage());
                throw new RuntimeException(t);
            }
        }
    }

    /**
     *
     * @return the record type we are currently processing
     */
    private RecordType getRecordType() {
        String stepName = stepExecution.getStepName();
        if (stepName.equals(PROCESS_CORE_FILE)) {
            return RecordType.Taxon;
        } else if (stepName.equals(PROCESS_DESCRIPTION_FILE)) {
            return RecordType.TextContent;
        } else if (stepName.equals(PROCESS_IMAGE_FILE)) {
            return RecordType.TextContent;
        } else if (stepName.equals(PROCESS_REFERENCE_FILE)) {
            return RecordType.Reference;
        }
        return null;
    }

    /**
     *
     * @return the source
     */
    private Source getSource() {
        if (source == null) {
            Criteria criteria = getSession().createCriteria(Source.class).add(
                    Restrictions.eq("identifier", sourceName));

            source = (Source) criteria.uniqueResult();
        }
        return source;
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
    public void afterRead(final Base base) {

    }

    /**
     *
     */
    public void beforeRead() {

    }

    /**
     * @param e the exception
     */
    public final void onReadError(final Exception e) {
        if (e instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) e;
            StringBuffer message = new StringBuffer();
            message.append(ffpe.getMessage());
            final Annotation annotation = new Annotation();
            if (ffpe.getCause() != null) {
                message.append(" " + ffpe.getCause().getMessage());
                logger.debug("FlatFileParseException | " + ffpe.getMessage()
                        + " Cause " + ffpe.getCause().getMessage());
                if (ffpe.getCause().getClass()
                        .equals(CannotFindRecordException.class)) {
                    annotation.setCode(AnnotationCode.BadIdentifier);
                    CannotFindRecordException cfre = (CannotFindRecordException) ffpe
                            .getCause();
                    annotation.setValue(cfre.getValue());
                } else if (ffpe.getCause().getClass()
                        .equals(BindException.class)) {
                    annotation.setCode(AnnotationCode.BadField);
                    BindException be = (BindException) ffpe
                            .getCause();
                    annotation.setValue(be.getFieldError().getField());
                } else {
                    annotation.setCode(AnnotationCode.BadRecord);
                }
            } else {
                logger.debug("FlatFileParseException | " + ffpe.getMessage());
            }

            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setRecordType(getRecordType());
            annotation.setSource(getSource());
            annotation.setType(AnnotationType.Error);
            annotation.setText(message.toString());
            try {
                transactionTemplate
                        .execute(new TransactionCallback<Serializable>() {

                            public Serializable doInTransaction(
                                    final TransactionStatus status) {
                                return getSession().save(annotation);
                            }
                        });
            } catch (Throwable t) {
                logger.error(t.getMessage());
                throw new RuntimeException(t);
            }
        }
    }

}
