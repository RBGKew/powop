package org.emonocot.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;

import org.emonocot.model.common.Annotation;
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
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.runner.RunWith;
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
    protected TaxonDao taxonDao;

    /**
     *
     */
   @Autowired
   protected ImageDao imageDao;

    /**
     * A list of objects in the order they were created.
     */
    private List<Base> setUp = new ArrayList<Base>();

    /**
     * A stack of objects.
     */
    private Stack<Base> tearDown = new Stack<Base>();

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
                for (Base base : setUp) {
                    if (base.getClass().equals(Taxon.class)) {
                        taxonDao.saveOrUpdate((Taxon) base);
                    } else if (base.getClass().equals(Image.class)) {
                        imageDao.saveOrUpdate((Image) base);
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
        setUp = new ArrayList<Base>();
        doInTransaction(new Callable() {
            public Object call() throws Exception {
                while (!tearDown.isEmpty()) {
                    Base base = tearDown.pop();
                    if (base.getClass().equals(Taxon.class)) {
                        taxonDao.delete(base.getIdentifier());
                    } else if (base.getClass().equals(Image.class)) {
                        imageDao.delete(base.getIdentifier());
                    }
                }                
                getSession().flush();
                return null;
            }
        });
    }

}
