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
import org.emonocot.persistence.dao.TaxonDao;
import org.emonocot.service.FacetName;
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
public class SearchTest extends AbstractPersistenceTest {

    /**
     *
     */
    @Autowired
    private TaxonDao taxonDao;

    /**
     * @throws Exception if there is a problem with the callable
     */
    @Test
    public final void setUpTestDataWithinTransaction() throws Exception {

        doInTransaction(new Callable() {
            public Object call() {
                FullTextSession fullTextSession = Search
                        .getFullTextSession(getSession());
                fullTextSession.purgeAll(Taxon.class);
                fullTextSession.purgeAll(Image.class);
                Taxon taxon1 = createTaxon("Aus", null, null, null,
                        new GeographicalRegion[] {});
                Taxon taxon2 = createTaxon("Aus bus", null, taxon1, null,
                        new GeographicalRegion[] {Continent.AUSTRALASIA,
                                Region.BRAZIL, Region.CARIBBEAN });
                Taxon taxon3 = createTaxon("Aus ceus", null, taxon1, null,
                        new GeographicalRegion[] {Region.NEW_ZEALAND});
                Taxon taxon4 = createTaxon("Aus deus", null, null, taxon2,
                        new GeographicalRegion[] {});
                Taxon taxon5 = createTaxon("Aus eus", null, null, taxon3,
                        new GeographicalRegion[] {});
                taxonDao.saveOrUpdate(taxon1);
                taxonDao.saveOrUpdate(taxon2);
                taxonDao.saveOrUpdate(taxon3);
                taxonDao.saveOrUpdate(taxon4);
                taxonDao.saveOrUpdate(taxon5);
                getSession().flush();
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
        Page<Taxon> page = taxonDao.search("name:Aus", null, null, null,
                new FacetName[]{FacetName.CONTINENT}, null);
        for (Taxon t : page.getRecords()) {
            System.out.println(t.getName());
        }

        for (Facet facet
                : page.getFacets().get(FacetName.CONTINENT.name())) {
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

       Page<Taxon> page = taxonDao.search("name:Aus", null, null, null,
               new FacetName[]{FacetName.CONTINENT}, selectedFacets);
       for (Taxon t : page.getRecords()) {
           System.out.println(t.getName());
       }

       for (Facet facet
               : page.getFacets().get(FacetName.CONTINENT.name())) {
         System.out.println(facet.getValue() + " " + facet.getCount());
       }
   }

  /**
   *
   */
  @Test
  public final void testSpatialSearch() {
      System.out.println(
              "testSpatialSearch() should return Aus bus but not Aus ceus");
      Page<Taxon> page = taxonDao.search(

              "name:Aus", "Intersects(100.0 -40.0 155.0 -5.0)",
              null, null, null, null);
      for (Taxon t : page.getRecords()) {
          System.out.println(t.getName());
      }
  }
}
