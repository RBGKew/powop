package org.emonocot.job.dwc.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.emonocot.api.ImageService;
import org.emonocot.job.dwc.DarwinCoreValidator;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is slightly different from the description validator because we believe
 * (or at least, I believe) that descriptive content is somehow "owned" or
 * "contained in" the taxon i.e. that if the taxon does not exist, the
 * descriptive content doesn't either. There is a one-to-many relationship
 * between taxa and descriptive content because there can be many facts about
 * each taxon, but each fact is only about one taxon.
 *
 * Images, on the other hand, have an inherently many-to-many relationship with
 * taxa as one image can appear on several different taxon pages (especially the
 * family page, type genus page, type species page etc). Equally a taxon page
 * can have many images on it. So Images don't belong to any one taxon page
 * especially. If we delete the taxon, the image can hang around - it might have
 * value on its own.
 *
 * @author ben
 *
 */
public class Validator extends DarwinCoreValidator<Image> implements
        ChunkListener, ItemWriteListener<Image> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Validator.class);

    /**
     *
     */
    private Map<String, Image> boundImages = new HashMap<String, Image>();

    /**
     *
     */
    private ImageService imageService;

    /**
     *
     */
    @Autowired
    public final void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * @param image an image object
     * @throws Exception if something goes wrong
     * @return Image an image object
     */
    public final Image process(final Image image)
            throws Exception {
        logger.info("Validating " + image.getUrl());
        
        super.checkTaxon(RecordType.Image, image, image.getTaxon());

        Image boundImage = boundImages.get(image.getUrl());
        if (boundImage == null) {
            Image persistedImage = imageService.findByUrl(image.getUrl());
            if (persistedImage == null) {
                // We've not seen this image before
                image.setIdentifier(UUID.randomUUID().toString());
                boundImages.put(image.getUrl(), image);
                image.getTaxon().getImages().add(image);
                image.setAuthority(getSource());
                image.getSources().add(getSource());
                Annotation annotation = createAnnotation(image,
                        RecordType.Image, AnnotationCode.Create,
                        AnnotationType.Info);
                image.getAnnotations().add(annotation);
                logger.info("Adding image " + image.getUrl());
                return image;
            } else {
                // We've seen this image before, but not in this chunk

                if ((persistedImage.getModified() != null
                    && image.getModified() != null)
                    && !persistedImage.getModified().isBefore(image.getModified())) {
                    // Assume the image hasn't changed, but maybe this taxon
                    // should be associated with it
                    if (persistedImage.getTaxa().contains(image.getTaxon())) {
                        // do nothing
                        return null;
                    } else {
                        // Add the taxon to the list of taxa
                        boundImages.put(persistedImage.getUrl(), persistedImage);
                        logger.info("Updating image " + image.getUrl());
                        persistedImage.getTaxa().add(image.getTaxon());
                        image.getTaxon().getImages().add(persistedImage);
                        return persistedImage;
                    }
                } else {
                    // Assume that this is the first of several times this image
                    // appears in the result set, and we'll use this version to
                    // overwrite the existing image
                	for (Annotation annotation : persistedImage.getAnnotations()) {
                   	 if(logger.isInfoEnabled()) {
                    	   logger.info("Comparing " + annotation.getJobId() + " with " + getStepExecution().getJobExecutionId());
                   	 }
                        if (getStepExecution().getJobExecutionId().equals(
                        		annotation.getJobId())) {                         
                            annotation.setType(AnnotationType.Info);
                            annotation.setCode(AnnotationCode.Update);
                            break;
                        }
                    }

                    persistedImage.setCaption(image.getCaption());
                    persistedImage.setCreated(image.getCreated());
                    persistedImage.setCreator(image.getCreator());
                    persistedImage.setLicense(image.getLicense());
                    persistedImage.setModified(image.getModified());
                    persistedImage.setSource(image.getSource());
                    persistedImage.setTaxon(image.getTaxon());
                    persistedImage.getTaxa().clear();
                    persistedImage.getTaxa().add(image.getTaxon());
                    if (!image.getTaxon().getImages().contains(persistedImage)) {
                        image.getTaxon().getImages().add(persistedImage);
                    }                    
                    boundImages.put(image.getUrl(), persistedImage);
                    logger.info("Overwriting image " + image.getUrl());
                    return persistedImage;
                }
            }
        } else {
            // We've already seen this image within this chunk and we'll
            // update it with this taxon but that's it, assuming that it
            // isn't a more up to date version
            if (boundImage.getTaxa().contains(image.getTaxon())) {
                // do nothing
            } else {
                // Add the taxon to the list of taxa
                image.getTaxon().getImages().add(boundImage);
                boundImage.getTaxa().add(image.getTaxon());
            }
            // We've already returned this object once
            logger.info("Skipping image " + image.getUrl());
            return null;
        }
    }

    /**
     * @param images the list of images written
     */
    public void afterWrite(List<? extends Image> images) {

    }

    /**
     * @param images the list of images to write
     */
    public final void beforeWrite(final List<? extends Image> images) {
        logger.info("Before Write");
        Comparator<Taxon> comparator = new TaxonComparator();
        for (Image image : images) {
            if (!image.getTaxa().isEmpty()) {
                if (image.getTaxa().size() == 1) {
                    image.setTaxon(image.getTaxa().get(0));
                } else {
                    List<Taxon> sorted = new ArrayList<Taxon>(image.getTaxa());
                    Collections.sort(sorted, comparator);
                    image.setTaxon(sorted.get(0));
                }
            }
        }
        for (Image image : images) {
            if (!image.getTaxa().isEmpty()) {
                for (Taxon t : image.getTaxa()) {
                    if (t.getImage() == null) {
                        t.setImage(t.getImages().get(0));
                    }
                }
            }
        }
    }

    /**
     * @param exception the exception thrown
     * @param images the list of images
     */
    public final void onWriteError(final Exception exception, final List<? extends Image> images) {

    }

    /**
     *
     */
    public final void afterChunk() {
        logger.info("After Chunk");
    }

    /**
     *
     */
    public final void beforeChunk() {
        logger.info("Before Chunk");
        boundImages = new HashMap<String, Image>();
    }

    /**
     *
     * @author ben
     *
     */
    class TaxonComparator implements Comparator<Taxon> {

        /**
         * @param o1
         *            Set the first taxon
         * @param o2
         *            Set the second taxon
         * @return < 0 if the first should come before the second, > 0 if the
         *         first should come after and 0 if they are equal
         */
        public int compare(final Taxon o1, final Taxon o2) {
            if (o1 == o2) {
                return 0;
            }
            if (o1.getRank() == null) {
                if (o2.getRank() == null) {
                    return 0;
                }  else {
                    return 1;
                }
            } else {
              return o1.getRank().compareTo(o2.getRank());
            }
        }
    }

}