package org.emonocot.persistence;

import java.util.concurrent.Callable;

import org.emonocot.model.description.Distribution;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
     * @param datePublished set the date published
     * @param rank set the rank
     * @param status set the status
     * @param distributions the distribution of the taxon
     * @return a new taxon
    */
    final Taxon createTaxon(final String name, final String identifier,
            final Taxon parent, final Taxon accepted, final String family,
            final String datePublished, final Rank rank,
            final TaxonomicStatus status,
            final GeographicalRegion[] distributions) {
       Taxon taxon = new Taxon();
       taxon.setName(name);
       taxon.setFamily(family);
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
       return taxon;
   }

   /**
    *
    * @param caption Set the caption
    * @param identifier Set the identifier
    * @return an image
    */
   public final Image createImage(final String caption, String identifier) {
       Image img = new Image();
       img.setCaption(caption);
       img.setIdentifier(identifier);
       return img;
   }

    /**
     *
     * @return the current sesssion
     */
   protected final Session getSession() {
       return sessionFactory.getCurrentSession();
   }

}
