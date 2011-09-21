package org.emonocot.portal.remoting;

import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class RestApiFunctionalTest {

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
    @Test
    public final void testTaxon() {
        Taxon taxon = new Taxon();
        taxon.setName("Acorus");
        taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
        taxonDao.save(taxon);
        taxonDao.delete("urn:kew.org:wcs:taxon:2295");
    }

    /**
    *
    */
   @Test
   public final void testImage() {
       Image image = new Image();
       image.setCaption("Acorus");
       image.setIdentifier("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg");
       imageDao.save(image);
       imageDao.delete("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg");
   }

}
