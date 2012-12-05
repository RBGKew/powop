package org.emonocot.harvest.common;

import java.io.Serializable;

import org.emonocot.model.Annotation;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.registry.Organisation;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
public abstract class AbstractRecordAnnotator extends HibernateDaoSupport {

    /**
     *
     */
    private TransactionTemplate transactionTemplate = null;

    /**
     *
     */
    private Organisation source = null;

    /**
     *
     */
    private String sourceName;

    /**
     *
     * @param newSourceName
     *            Set the name of the Source
     */
    public final void setSourceName(final String newSourceName) {
        this.sourceName = newSourceName;
    }

    /**
     *
     * @param transactionManager
     *            Set the transaction manager
     */
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
     * @param sessionFactory
     *            Set the session factory
     */
    public final void setHibernateSessionFactory(
            final SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }

    /**
     *
     * @return the source
     */
    private Organisation getSource() {
        if (source == null) {
            Criteria criteria = getSession().createCriteria(Organisation.class).add(
                    Restrictions.eq("identifier", sourceName));

            source = (Organisation) criteria.uniqueResult();
        }
        return source;
    }

    /**
     *
     * @param recordType Set the record type
     * @param jobExecutionId Set the job execution id
     * @param annotationCode Set the annotation code
     * @param annotationValue Set the annotation value
     * @param annotationType Set the annotation type
     * @param message Set the message
     */
    public final void annotate(final RecordType recordType,
            final Long jobExecutionId, final AnnotationCode annotationCode,
            final String annotationValue, final AnnotationType annotationType,
            final String message) {
        final Annotation annotation = new Annotation();
        annotation.setId(1000L);
        annotate(annotation);
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
}
