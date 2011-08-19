package org.emonocot.job.dwc.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.ImageService;
import org.emonocot.service.TaxonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
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
public class ImageValidator implements
        ItemProcessor<Image, Image>, StepExecutionListener, ChunkListener,
        ItemWriteListener<Image>{
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(ImageValidator.class);

    /**
     *
     */
    private Map<String, Image> boundImages = new HashMap<String, Image>();

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private ImageService imageService;

    /**
     *
     */
    private StepExecution stepExecution;

    /**
     *
     */
    @Autowired
    public final void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

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
        logger.info("Validating " + image);

        Image boundImage = boundImages.get(image.getIdentifier());
        if (boundImage == null) {
            Image persistedImage = imageService.find(image.getIdentifier());
            if (persistedImage == null) {
                // We've not seen this image before
                boundImages.put(image.getIdentifier(), image);
                return image;
            } else {
                // We've seen this image before, but not in this chunk

                if ((persistedImage.getModified() == null
                    && image.getModified() == persistedImage.getModified())
                    || persistedImage.getModified().equals(image.getModified())) {
                    boundImages.put(persistedImage.getIdentifier(), persistedImage);
                    // Assume the image hasn't changed, but maybe this taxon
                    // should be associated with it
                    if(persistedImage.getTaxa().contains(image.getTaxa())) {
                        // do nothing
                    } else {
                        // Add the taxon to the list of taxa
                        persistedImage.getTaxa().add(image.getTaxon());
                    }
                    return persistedImage;
                } else {
                    // Assume that this is the first of several times this image
                    // appears in the result set, and we'll use this version to
                    // overwrite the existing image
                    image.setId(persistedImage.getId());
                    boundImages.put(image.getIdentifier(), image);
                    return image;
                }
            }
        } else {
            // We've already seen this image within this chunk and we'll
            // update it with this taxon but that's it, assuming that it
            // isn't a more up to date version
            if (boundImage.getTaxa().contains(image.getTaxa())) {
                // do nothing
            } else {
                // Add the taxon to the list of taxa
                boundImage.getTaxa().add(image.getTaxon());
            }
            // We've already returned this object once
            return null;
        }
    }

    /**
     * @param newStepExecution Set the step execution
     */
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

    /**
     * @param newStepExecution Set the step execution
     * @return the exit status
     */
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    /**
     * @param images the list of images written
     */
    public void afterWrite(List<? extends Image> images) {

    }

    /**
     * @param images the list of images to write
     */
    public void beforeWrite(List<? extends Image> images) {
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
    }

    /**
     * @param exception the exception thrown
     * @param images the list of images
     */
    public void onWriteError(Exception exception, List<? extends Image> images) {

    }

    /**
     *
     */
    public void afterChunk() {
    }

    public void beforeChunk() {
        boundImages.clear();
    }

    /**
     *
     * @author ben
     *
     */
    class TaxonComparator implements Comparator<Taxon> {

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
