package org.emonocot.harvest.common;

import java.io.Serializable;

import org.emonocot.model.Annotation;
import org.emonocot.model.registry.Organisation;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.batch.core.ExitStatus;
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
public abstract class AbstractRecordAnnotator extends HibernateDaoSupport implements StepExecutionListener {

    private TransactionTemplate transactionTemplate = null;

    private Organisation source = null;

    private String sourceName;
    
	protected StepExecution stepExecution;

    public final void setSourceName(final String newSourceName) {
        this.sourceName = newSourceName;
    }

    public final void setTransactionManager(
            final PlatformTransactionManager transactionManager) {
        Assert.notNull(transactionManager,
                "The 'transactionManager' argument must not be null.");
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate
                .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /**
     *
     * @return the source
     */
    protected Organisation getSource() {
        if (source == null) {
            Criteria criteria = getSession().createCriteria(Organisation.class).add(
                    Restrictions.eq("identifier", sourceName));

            source = (Organisation) criteria.uniqueResult();
        }
        return source;
    }

    /**
     *
     * @param annotation Set the annotation
     */
    public final void annotate(final Annotation annotation) {
        try {
            transactionTemplate.execute(new TransactionCallback() {
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
   
	public final void beforeStep(final StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

	public final ExitStatus afterStep(final StepExecution newStepExecution) {
		return null;
	}
}
