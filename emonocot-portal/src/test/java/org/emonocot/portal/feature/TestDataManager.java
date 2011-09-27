package org.emonocot.portal.feature;

import java.util.List;
import java.util.Stack;

import org.emonocot.model.common.Base;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;

import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;

import org.emonocot.service.ImageService;
import org.emonocot.service.ReferenceService;
import org.emonocot.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.After;
import cucumber.annotation.en.Given;



/**
 *
 * @author ben
 *
 */
public class TestDataManager {

    /**
     *
     */
    private Stack<Base> data = new Stack<Base>();

    /**
    *
    */
   @Autowired
   private ImageService imageService;

    /**
     *
     */
    @Autowired
    private TaxonService taxonService;

    /**
     *
     */
    @Autowired
    private ReferenceService referenceService;

    /**
    *
    * @param imageRows set the image rows
    */
   @Given("^there are images with the following properties:$")
    public final void thereAreImagesWithTheFollowingProperties(
            final List<ImageRow> imageRows) {
        for (ImageRow imageRow : imageRows) {
            Image i = new Image();
            i.setCaption(imageRow.caption);
            i.setIdentifier(imageRow.identifier);
            imageService.save(i);
            data.push(i);
        }
    }

    /**
    *
    * @param taxonRows set the taxon rows
    */
   @Given("^there are taxa with the following properties:$")
   public final void thereAreTaxaWithTheFollowingProperties(
           final List<TaxonRow> taxonRows) {

       for (TaxonRow taxonRow : taxonRows) {
           Taxon t = new Taxon();
           data.push(t);
           t.setName(taxonRow.name);
           t.setIdentifier(taxonRow.identifier);
           if (taxonRow.diagnostic != null) {
               createTextualData(t, taxonRow.diagnostic, Feature.diagnostic);
           }
           if (taxonRow.habitat != null) {
               createTextualData(t, taxonRow.habitat, Feature.habitat);
           }
           if (taxonRow.protologue != null) {
               Reference reference = new Reference();
               reference.setIdentifier(taxonRow.protologue);
               t.setProtologue(reference);
           }
           taxonService.save(t);
       }
   }

   /**
   *
   * @param referenceRows set the reference rows
   */
  @Given("^there are references with the following properties:$")
  public final void thereAreReferencesWithTheFollowingProperties(
          final List<ReferenceRow> referenceRows) {

      for (ReferenceRow referenceRow : referenceRows) {
          Reference r = new Reference();
          data.push(r);
          r.setIdentifier(referenceRow.identifier);
          r.setTitle(referenceRow.title);
          r.setDatePublished(referenceRow.datePublished);
          r.setVolume(referenceRow.volume);
          r.setPages(referenceRow.page);
          referenceService.save(r);
      }
  }

  /**
   *
   * @param taxon Set the taxon
   * @param text Set the text
   * @param feature Set the feature
   */
  private void createTextualData(final Taxon taxon, final String text,
          final Feature feature) {
      TextContent textContent = new TextContent();
      textContent.setContent(text);
      textContent.setFeature(feature);
      textContent.setTaxon(taxon);
      taxon.getContent().put(feature, textContent);
  }

    /**
     *
     */
    @After
    public final void tearDown() {
        while (!data.isEmpty()) {
            Base base = data.pop();
            if (base instanceof Taxon) {
                taxonService.delete(base.getIdentifier());
            } else if (base instanceof Image) {
                imageService.delete(base.getIdentifier());
            } else if (base instanceof Reference) {
                referenceService.delete(base.getIdentifier());
            }
        }
    }
}
