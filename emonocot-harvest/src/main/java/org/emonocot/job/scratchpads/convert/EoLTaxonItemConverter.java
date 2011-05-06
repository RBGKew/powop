package org.emonocot.job.scratchpads.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.job.scratchpads.model.EoLReference;
import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataRetrievalFailureException;

/**
 *
 * @author ben
 *
 */
public class EoLTaxonItemConverter implements Converter<EoLTaxonItem, Taxon> {
    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(EoLTaxonItemConverter.class);

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private Converter<EoLDataObject, TextContent> textDataConverter;

    /**
     *
     */
    private Converter<EoLDataObject, Image> imageConverter;

    /**
     *
     */
    private Converter<EoLReference, Reference> referenceConverter;

    /**
     *
     * @param textDataConverter Set the text data converter to use.
     */
    @Autowired
    public final void setTextDataConverter(
            final Converter<EoLDataObject, TextContent> textDataConverter) {
        this.textDataConverter = textDataConverter;
    }

    /**
     *
     * @param imageConverter Set the image converter to use.
     */
    @Autowired
    public final void setImageConverter(
            final Converter<EoLDataObject, Image> imageConverter) {
        this.imageConverter = imageConverter;
    }

    /**
     *
     * @param referenceConverter Set the reference converter to use.
     */
    @Autowired
    public final void setReferenceConverter(
            final Converter<EoLReference, Reference> referenceConverter) {
        this.referenceConverter = referenceConverter;
    }

    /**
     *
     * @param newTaxonService Set the taxon service to use.
     */
    @Autowired
    public final void setTaxonService(final TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     * @param input The eol taxon item to convert.
     * @return A Taxon item.
     */
    public final Taxon convert(final EoLTaxonItem input) {

        Taxon taxon = null;
        try {
            taxon = taxonService.load(input.getIdentifer());
        } catch (DataRetrievalFailureException drfe) {
            logger.info("Could not retrieve taxon with identifier "
                    + input.getIdentifer());
            taxon = new Taxon();
        }

        for (EoLDataObject dataObject : input.getDataObjects()) {
            if (dataObject.getDataType().equals(
                    "http://purl.org/dc/dcmitype/StillImage")) {
                handleImage(dataObject, taxon);
            } else if (dataObject.getDataType().equals(
                    "http://purl.org/dc/dcmitype/Text")) {
                handleText(dataObject, taxon);
            }
        }

        for (EoLReference reference : input.getReferences()) {
            handleReference(reference, taxon);
        }

        // Now deal with the deletes i.e. data which is in the
        // target which is not in the source

        // To avoid the ConcurrentModificationException
        List<Image> images = new ArrayList<Image>();
        Collections.copy(taxon.getImages(), images);

        for (Image image : images) {
            // Assume we have a convenience method to determine if the input
            // object contains an image using it's URL
            if (!input.containsImage(image.getURL())) {
                taxon.getImages().remove(image);
            }
        }

        // and do the same for references and for text elements, noting that the
        // semantics are slightly different - removing a reference does not
        // delete it as
        // the taxon-reference relationship is many-to-many wheras the
        // taxon-contentelement is
        // one-to-many thus removing a content element from its parent
        // collection has the
        // effect of deleting it from the database (and should be mapped using
        // orphanRemoval = true)

        return taxon;
    }

    /**
     *
     * @param dataObject The text object to convert into text content.
     * @param taxon The taxon this text content is about.
     */
    private void handleText(final EoLDataObject dataObject, final Taxon taxon) {
        // We need to know which taxon the dataObject refers in order to look it
        // up properly
        dataObject.setTaxon(taxon);

        TextContent text = textDataConverter.convert(dataObject);
        taxon.getContent().put(text.getFeature(), text);
    }

    /**
     *
     * @param eolReference The eol reference to convert into a reference.
     * @param taxon The taxon this reference is about.
     */
    private void handleReference(final EoLReference eolReference,
            final Taxon taxon) {
        /**
         * see the comment below about the conversion service
         */
        Reference reference = referenceConverter.convert(eolReference);
        if (taxon.getReferences().contains(reference)) {
            taxon.getReferences().remove(reference);
            taxon.getReferences().add(reference);
        } else {
            taxon.getReferences().add(reference);
        }
    }

    /**
     *
     * @param dataObject The eol data object to convert into an Image
     * @param taxon The taxon this image is about.
     */
    private void handleImage(final EoLDataObject dataObject,
            final Taxon taxon) {
        /**
         * conversion service internally calls the persistance layer and either
         * returns a new, unpersisted Image instance if that image is unknown to
         * eMonocot i.e. if the image has not ever been known to eMonocot, or if
         * the image is persisted within eMonocot, the persisted version,
         * possibly updated to include recent changes in the DTO if the object
         * has been changed.
         */
        Image image = imageConverter.convert(dataObject);
        if (taxon.getImages().contains(image)) {
            int index = taxon.getImages().indexOf(image);
            taxon.getImages().remove(index);
            taxon.getImages().add(index, image);
        } else {
            taxon.getImages().add(image);
        }
    }

}
