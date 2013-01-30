package org.emonocot.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.emonocot.model.Annotation;
import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.registry.Organisation;
import org.emonocot.persistence.dao.AnnotationDao;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.JobExecutionDao;
import org.emonocot.persistence.dao.JobInstanceDao;
import org.emonocot.persistence.dao.ReferenceDao;
import org.emonocot.persistence.dao.SearchableObjectDao;
import org.emonocot.persistence.dao.OrganisationDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.emonocot.test.DataManagementSupport;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author ben
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext*.xml" })
public abstract class AbstractPersistenceTest extends DataManagementSupport {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TaxonDao taxonDao;

    @Autowired
    private ReferenceDao referenceDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private AnnotationDao annotationDao;

    @Autowired
    private OrganisationDao sourceDao;

    @Autowired
    private JobInstanceDao jobInstanceDao;

    @Autowired
    private JobExecutionDao jobExecutionDao;

    @Autowired
    SearchableObjectDao searchableObjectDao;
    
    @Autowired
    SolrServer solrServer;

    /**
     * @param task
     *            Set the method to run in a transaction
     * @return the object returned by the callable method
     * @throws Exception
     *             if there is a problem running the method
     */
    protected final Object doInTransaction(final Callable task)
            throws Exception {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("test");
        transactionDefinition
                .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
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
     * @return the current sesssion
     */
    protected final Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * @throws Exception
     *             if there is a problem setting up the test data
     */
    public final void doSetUp() throws Exception {

        doInTransaction(new Callable() {
            public Object call() throws Exception {
            	ModifiableSolrParams params = new ModifiableSolrParams();
            	params.add("q","*:*");
            	params.add("df", "id");
            	QueryResponse queryResponse = solrServer.query(params);
            	SolrDocumentList solrDocumentList = queryResponse.getResults();
            	List<String> documentsToDelete = new ArrayList<String>();
            	for(int i = 0; i < solrDocumentList.size(); i++) {
            		documentsToDelete.add(solrDocumentList.get(i).getFirstValue("id").toString());
            	}
            	if(!documentsToDelete.isEmpty()) {
            	    solrServer.deleteById(documentsToDelete);
            	    solrServer.commit(true,true);
            	}
                setUpTestData();
                for (Object obj : getSetUp()) {
                    if (obj.getClass().equals(Taxon.class)) {
                        taxonDao.saveOrUpdate((Taxon) obj);
                    } else if (obj.getClass().equals(Image.class)) {
                        imageDao.saveOrUpdate((Image) obj);
                    } else if (obj.getClass().equals(Annotation.class)) {
                        annotationDao.saveOrUpdate((Annotation) obj);
                    } else if (obj.getClass().equals(Organisation.class)) {
                        sourceDao.saveOrUpdate((Organisation) obj);
                    } else if (obj.getClass().equals(Reference.class)) {
                        referenceDao.saveOrUpdate((Reference) obj);
                    } else if (obj.getClass().equals(JobExecution.class)) {
                        jobExecutionDao.save((JobExecution) obj);
                    } else if (obj.getClass().equals(JobInstance.class)) {
                        jobInstanceDao.save(((JobInstance) obj));
                    } else {
                        System.out.println("WHAT the **** is a " + obj.toString());
                    }
                }
                getSession().flush();
                return null;
            }
        });
    }

    /**
     * @throws Exception
     *             if there is a problem tearing down the test
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
                        annotationDao
                                .delete(((Annotation) obj).getIdentifier());
                    } else if (obj.getClass().equals(Organisation.class)) {
                        sourceDao.delete(((Organisation) obj).getIdentifier());
                    } else if (obj.getClass().equals(Reference.class)) {
                        referenceDao.delete(((Reference) obj).getIdentifier());
                    } else if (obj.getClass().equals(JobInstance.class)) {
                        System.out.println("Deleting JobInstance "
                                + ((JobInstance) obj).getId());
                        String authorityName = ((JobInstance) obj)
                                .getJobParameters().getString("authority.name");
                        List<JobExecution> jobExecutions = jobExecutionDao
                                .getJobExecutions(authorityName, null, null);
                        for (JobExecution jobExecution : jobExecutions) {
                            jobExecutionDao.delete(jobExecution.getId());
                        }
                        jobInstanceDao.delete(((JobInstance) obj).getId());
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

    /**
     * @return the searchableObjectDao
     */
    public final SearchableObjectDao getSearchableObjectDao() {
        return searchableObjectDao;
    }
}
