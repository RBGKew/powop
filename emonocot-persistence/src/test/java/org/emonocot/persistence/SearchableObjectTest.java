package org.emonocot.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.emonocot.model.description.Distribution;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.FacetName;
import org.emonocot.persistence.dao.SearchableObjectDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.facet.Facet;
import org.junit.Test;
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
public class SearchableObjectTest {

    /**
     *
     */
    @Autowired
    private SearchableObjectDao soDao;

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
    * @param task Set the method to run in a transaction
    * @return the object returned by the callable method
    * @throws Exception if there is a problem running the method
    */
   private Object doInTransaction(final Callable task) throws Exception {
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
   
   private Image createImage(String caption){
       Image img = new Image();
       img.setCaption(caption);
       return img;
   }

    /**
     *
     * @param name the name of the taxon
     * @param parent the taxonomic parent
     * @param accepted the accepted name
     * @param distributions the distribution of the taxon
     * @return a new taxon
     */
    private Taxon createTaxon(final String name, final Taxon parent,
            final Taxon accepted, final GeographicalRegion[] distributions) {
        Taxon taxon = new Taxon();
        taxon.setName(name);
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
        return taxon;
    }

    /**
     * @throws Exception if there is a problem with the callable
     */
    @Test
    public final void setUpTestDataWithinTransaction() throws Exception {

        doInTransaction(new Callable() {
            public Object call() {
                FullTextSession fullTextSession = Search
                        .getFullTextSession(sessionFactory.getCurrentSession());
                fullTextSession.purgeAll(Taxon.class);
                Taxon taxon1 = createTaxon("Aus", null, null,
                        new GeographicalRegion[] {});
                Taxon taxon2 = createTaxon("Aus bus", taxon1, null,
                        new GeographicalRegion[] {Continent.AUSTRALASIA,
                                Region.BRAZIL, Region.CARIBBEAN });
                Taxon taxon3 = createTaxon("Aus ceus", taxon1, null,
                        new GeographicalRegion[] {Region.NEW_ZEALAND});
                Taxon taxon4 = createTaxon("Aus deus", null, taxon2,
                        new GeographicalRegion[] {});
                Taxon taxon5 = createTaxon("Aus eus", null, taxon3,
                        new GeographicalRegion[] {});
                Image img1 = createImage("Aus");
                Image img2 = createImage("Aus bus");
                soDao.saveOrUpdate(taxon1);
                soDao.saveOrUpdate(taxon2);
                soDao.saveOrUpdate(taxon3);
                soDao.saveOrUpdate(taxon4);
                soDao.saveOrUpdate(taxon5);
                soDao.saveOrUpdate(img1);
                soDao.saveOrUpdate(img2);
                sessionFactory.getCurrentSession().flush();
                return null;
            }
        });
    }

    /**
     *
     */
    @Test
    public final void testSearch() {
        soDao.search("Aus", null, null, null, null, null);
    }
}
