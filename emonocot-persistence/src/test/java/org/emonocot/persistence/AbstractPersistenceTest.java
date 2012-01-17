package org.emonocot.persistence;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.AnnotationDao;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.ReferenceDao;
import org.emonocot.persistence.dao.SourceDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext-test.xml" })
public abstract class AbstractPersistenceTest extends DataManagementSupport {

    /**
    *
    */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     *
     */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     *
     */
    @Autowired
    private TaxonDao taxonDao;
    
   /**
    *
    */
   @Autowired
   private ReferenceDao referenceDao;

    /**
     *
     */
   @Autowired
   private ImageDao imageDao;

    /**
     *
     */
    @Autowired
    private AnnotationDao annotationDao;

    /**
    *
    */
   @Autowired
   private SourceDao sourceDao;

   /**
    *
    */
   @Autowired
   private JobInstanceDao jobInstanceDao;

   /**
    *
    */
   @Autowired
   private JobExecutionDao jobExecutionDao;

    /**
     *
     * @param task
     *            Set the method to run in a transaction
     * @return the object returned by the callable method
     * @throws Exception
     *             if there is a problem running the method
     */
    protected final Object doInTransaction(
            final Callable task) throws Exception {
        DefaultTransactionDefinition transactionDefinition
            = new DefaultTransactionDefinition();
        transactionDefinition.setName("test");
        transactionDefinition
            .setPropagationBehavior(
                    TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager
                .getTransaction(transactionDefinition);
        Object value = null;
        try {
            value = task.call();
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }
        transactionManager.commit(status);
        return value;
    }

   /**
     *
     * @return the current sesssion
     */
   protected final Session getSession() {
       return sessionFactory.getCurrentSession();
   }

   /**
     *
     * @throws Exception if there is a problem setting up the test data
     */
    public final void doSetUp() throws Exception {
        doInTransaction(new Callable() {
            public Object call() throws Exception {
                FullTextSession fullTextSession = Search
                .getFullTextSession(getSession());
                fullTextSession.purgeAll(Taxon.class);
                fullTextSession.purgeAll(Image.class);
                setUpTestData();
                for (Object obj : getSetUp()) {
                    if (obj.getClass().equals(Taxon.class)) {
                        taxonDao.saveOrUpdate((Taxon) obj);
                    } else if (obj.getClass().equals(Image.class)) {
                        imageDao.saveOrUpdate((Image) obj);
                    } else if (obj.getClass().equals(Annotation.class)) {
                        annotationDao.saveOrUpdate((Annotation) obj);
                    } else if (obj.getClass().equals(Source.class)) {
                        sourceDao.saveOrUpdate((Source) obj);
                    } else if (obj.getClass().equals(Reference.class)) {
                        referenceDao.saveOrUpdate((Reference) obj);
                    } else if (obj.getClass().equals(JobExecution.class)) {
                        jobExecutionDao.saveJobExecution((JobExecution) obj);
                    } else if (obj.getClass().equals(JobInstance.class)) {
                        jobInstanceDao.createJobInstance(
                                ((JobInstance) obj).getJobName(),
                                ((JobInstance) obj).getJobParameters());
                    }
                }
                getSession().flush();
                return null;
            }
        });
    }

    /**
     *
     * @throws Exception if there is a problem tearing down the test
     */
    public final void doTearDown() throws Exception {
        setSetUp(new ArrayList<Object>());
        doInTransaction(new Callable() {
            public Object call() throws Exception {
                while (!getTearDown().isEmpty()) {
                    Object obj = getTearDown().pop();
                    if (obj.getClass().equals(Taxon.class)) {
                        taxonDao.delete(((Taxon) obj).getIdentifier());
                    } else if (obj.getClass().equals(Image.class)) {
                        imageDao.delete(((Image) obj).getIdentifier());
                    } else if (obj.getClass().equals(Annotation.class)) {
                        annotationDao.delete(((Annotation) obj).getIdentifier());
                    } else if (obj.getClass().equals(Source.class)) {
                        sourceDao.delete(((Source) obj).getIdentifier());
                    } else if (obj.getClass().equals(Reference.class)) {
                        referenceDao.delete(((Reference) obj).getIdentifier());
                    }
                }
                getSession().flush();
                return null;
            }
        });
    }

    /**
     * @return the taxonDao
     */
    public final TaxonDao getTaxonDao() {
        return taxonDao;
    }

    /**
     * @return the imageDao
     */
    public final ImageDao getImageDao() {
        return imageDao;
    }

}
