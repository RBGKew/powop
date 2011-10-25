package org.emonocot.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.Base;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.persistence.dao.AnnotationDao;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
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
public abstract class AbstractPersistenceTest {

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
   private JobInstanceDao jobInstanceDao;

   /**
    *
    */
   @Autowired
   private JobExecutionDao jobExecutionDao;

    /**
     * A list of objects in the order they were created.
     */
    private List<Object> setUp = new ArrayList<Object>();

    /**
     * A stack of objects.
     */
    private Stack<Object> tearDown = new Stack<Object>();

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
    * @param name the name of the taxon
     * @param identifier set the identifier of the taxon
     * @param parent the taxonomic parent
     * @param accepted the accepted name
     * @param family the family
     * @param genus TODO
     * @param specificEpithet TODO
     * @param datePublished set the date published
     * @param rank set the rank
     * @param status set the status
     * @param distributions the distribution of the taxon
     * @return a new taxon
    */
    public final Taxon createTaxon(final String name, final String identifier,
            final Taxon parent, final Taxon accepted, final String family,
            final String genus, final String specificEpithet,
            final String datePublished, final Rank rank,
            final TaxonomicStatus status,
            final GeographicalRegion[] distributions) {
       Taxon taxon = new Taxon();
       taxon.setName(name);
       taxon.setFamily(family);
       taxon.setGenus(genus);
       taxon.setSpecificEpithet(specificEpithet);
       taxon.setIdentifier(identifier);
       taxon.setStatus(status);
       taxon.setRank(rank);
       Reference reference = new Reference();
       reference.setDatePublished(datePublished);
       taxon.setProtologue(reference);
       if (parent != null) {
           taxon.setParent(parent);
           parent.getChildren().add(taxon);
       }

       if (accepted != null) {
           taxon.setAccepted(accepted);
           accepted.getSynonyms().add(taxon);
       }

       for (GeographicalRegion region : distributions) {
           Distribution distribution = new Distribution();
           distribution.setRegion(region);
           distribution.setTaxon(taxon);
           taxon.getDistribution().put(region,  distribution);
       }
       setUp.add(taxon);
       tearDown.push(taxon);
       return taxon;
   }

   /**
    * @param base Set the annotated object
    * @return a new annotation
    */
    public final Annotation createAnnotation(final Base base) {
        Annotation annotation = new Annotation();

        if (base.getClass().equals(Taxon.class)) {
            annotation.setAnnotatedObjType("Taxon");
            ((Taxon) base).getAnnotations().add(annotation);
        }
        return annotation;
    }

    /**
     *
     * @param objectType Set the object type
     * @param jobId set the job id
     * @param objectId set the object id
     * @param type Set the annotation type
     * @return an annotation
     */
    public final Annotation createAnnotation(final String objectType,
            final Long jobId, final Long objectId, final AnnotationType type) {
        Annotation annotation = new Annotation();
        annotation.setAnnotatedObjType(objectType);
        annotation.setAnnotatedObjId(objectId);
        annotation.setJobId(jobId);
        annotation.setType(type);
        setUp.add(annotation);
        tearDown.push(annotation);
        return annotation;
    }

   /**
    *
    * @param caption Set the caption
    * @param identifier Set the identifier
    * @return an image
    */
   public final Image createImage(final String caption, String identifier) {
       Image image = new Image();
       image.setCaption(caption);
       image.setIdentifier(identifier);
       setUp.add(image);
       tearDown.push(image);
       return image;
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
    protected void setUpTestData() throws Exception {

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
                for (Object obj : setUp) {
                    if (obj.getClass().equals(Taxon.class)) {
                        taxonDao.saveOrUpdate((Taxon) obj);
                    } else if (obj.getClass().equals(Image.class)) {
                        imageDao.saveOrUpdate((Image) obj);
                    } else if (obj.getClass().equals(Annotation.class)) {
                            annotationDao.saveOrUpdate((Annotation) obj);
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
        setUp = new ArrayList<Object>();
        doInTransaction(new Callable() {
            public Object call() throws Exception {
                while (!tearDown.isEmpty()) {
                    Object obj = tearDown.pop();
                    if (obj.getClass().equals(Taxon.class)) {
                        taxonDao.delete(((Taxon) obj).getIdentifier());
                    } else if (obj.getClass().equals(Image.class)) {
                        imageDao.delete(((Image) obj).getIdentifier());
                    } else if (obj.getClass().equals(Annotation.class)) {
                        annotationDao.delete(((Annotation) obj).getIdentifier());
                    }
                }
                getSession().flush();
                return null;
            }
        });
    }

    /**
     *
     * @param jobInstance Set the job instance
     * @return a job execution
     */
    public final JobExecution createJobExecution(
            final JobInstance jobInstance) {
        JobExecution jobExecution = new JobExecution(jobInstance);
        setUp.add(jobExecution);
        tearDown.push(jobExecution);
        return jobExecution;
    }

    /**
    *
    * @param id set the id
    * @param jobParameters set the job parameters
    * @param jobName set the job name
    * @return a job instance
    */
    public final JobInstance createJobInstance(final Long id,
            final Map<String, JobParameter> jobParameters,
            final String jobName) {
        JobInstance jobInstance = new JobInstance(id, new JobParameters(
                jobParameters), jobName);
       setUp.add(jobInstance);
       tearDown.push(jobInstance);
       return jobInstance;
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
