package org.emonocot.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

//import org.apache.lucene.spatial.base.context.SpatialContext;
//import org.apache.lucene.spatial.base.io.sample.SampleData;
//import org.apache.lucene.spatial.base.io.sample.SampleDataReader;
//import org.apache.lucene.spatial.base.shape.Shape;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.FacetName;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.SessionFactory;
import org.hibernate.search.Search;
import org.hibernate.search.FullTextSession;
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

//import com.googlecode.lucene.spatial.base.context.JtsSpatialContext;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext-test.xml" })
public class SearchTest {
    
   /**
    *
    */
//   private SpatialContext spatialContext = new JtsSpatialContext();

    /**
     *
     */
    @Autowired
    private TaxonDao taxonDao;

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
//        SampleDataReader level1DataReader = new SampleDataReader(
//                getClass().getClassLoader().getResourceAsStream("org/emonocot/model/level1.txt"));
//        while (level1DataReader.hasNext()) {
//            SampleData data = level1DataReader.next();
//            Shape shape = spatialContext.readShape(data.shape);
//            Continent continent = Continent.fromString(data.id);
//            continent.setShape(shape);
//        }
//
//        SampleDataReader level2DataReader = new SampleDataReader(
//                getClass().getClassLoader().getResourceAsStream("org/emonocot/model/level2.txt"));
//        while (level2DataReader.hasNext()) {
//            SampleData data = level2DataReader.next();
//            Shape shape = spatialContext.readShape(data.shape);
//            Region region = Region.fromString(data.id);
//            region.setShape(shape);
//        }
//        
//        SampleDataReader level3DataReader = new SampleDataReader(
//                getClass().getClassLoader().getResourceAsStream("org/emonocot/model/level3.txt"));
//        while (level3DataReader.hasNext()) {
//            SampleData data = level3DataReader.next();
//            Shape shape = spatialContext.readShape(data.shape);
//            Country country = Country.fromString(data.id);
//            country.setShape(shape);
//        }
        doInTransaction(new Callable() {
            public Object call() {
                FullTextSession fullTextSession = Search
                        .getFullTextSession(sessionFactory.getCurrentSession());
                fullTextSession.purgeAll(Taxon.class);
                Taxon taxon1 = createTaxon("Aus", null, null,
                        new GeographicalRegion[] {});
                Taxon taxon2 = createTaxon("Aus bus", taxon1, null,
                        new GeographicalRegion[] { Continent.AUSTRALASIA,
                                Region.BRAZIL, Region.CARIBBEAN });
                Taxon taxon3 = createTaxon("Aus ceus", taxon1, null,
                        new GeographicalRegion[] { Region.NEW_ZEALAND });
                Taxon taxon4 = createTaxon("Aus deus", null, taxon2,
                        new GeographicalRegion[] {});
                Taxon taxon5 = createTaxon("Aus eus", null, taxon3,
                        new GeographicalRegion[] {});
                taxonDao.saveOrUpdate(taxon1);
                taxonDao.saveOrUpdate(taxon2);
                taxonDao.saveOrUpdate(taxon3);
                taxonDao.saveOrUpdate(taxon4);
                taxonDao.saveOrUpdate(taxon5);
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
        assertNotNull("taxonDao should not be null", taxonDao);
        Page<Taxon> page = taxonDao.search("name:Aus", null, null,
                new FacetName[]{FacetName.CONTINENT}, null);
        for (Taxon t : page.getRecords()) {
            System.out.println(t.getName());
        }

        for (Facet facet
                : page.getFacets(FacetName.CONTINENT.name())) {
          System.out.println(facet.getValue() + " " + facet.getCount());
        }
    }

   /**
    *
    */
   @Test
   public final void testRestrictedSearch() {
        Map<FacetName, Integer> selectedFacets
            = new HashMap<FacetName, Integer>();

        selectedFacets.put(FacetName.CONTINENT, 0);

       Page<Taxon> page = taxonDao.search("name:Aus", null, null,
               new FacetName[]{FacetName.CONTINENT}, selectedFacets);
       for (Taxon t : page.getRecords()) {
           System.out.println(t.getName());
       }

       for (Facet facet
               : page.getFacets(FacetName.CONTINENT.name())) {
         System.out.println(facet.getValue() + " " + facet.getCount());
       }
   }

}
