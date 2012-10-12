package org.emonocot.job.key;

import org.emonocot.api.ImageService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.harvest.media.ImageFileProcessor;
import org.emonocot.harvest.media.ImageMetadataExtractor;
import org.emonocot.model.Annotation;
import org.emonocot.model.Image;
import org.emonocot.model.Source;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.ImageFormat;
import org.emonocot.model.constants.RecordType;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.tdwg.ubif.MediaObject;

/**
 *
 * @author ben
 *
 */
public class MediaObjectProcessor extends AuthorityAware implements
        ItemProcessor<MediaObject, MediaObject> {
    /**
     *
     */
    private ImageService imageService;

    /**
     *
     */
    private ImageMetadataExtractor imageMetadataExtractor;

    /**
     *
     */
    private ImageFileProcessor imageFileProcessor;


    /**
     * @param newImageMetadataExtractor
     *            the imageMetadataExtractor to set
     */
    @Autowired
    public final void setImageMetadataExtractor(
            final ImageMetadataExtractor newImageMetadataExtractor) {
        this.imageMetadataExtractor = newImageMetadataExtractor;
    }

    /**
     * @param newImageFileProcessor
     *            the imageFileProcessor to set
     */
    @Autowired
    public final void setImageFileProcessor(
            final ImageFileProcessor newImageFileProcessor) {
        this.imageFileProcessor = newImageFileProcessor;
    }

    /**
     *
     * @param newImageService Set the image service
     */
    @Autowired
    public final void setImageService(final ImageService newImageService) {
        this.imageService = newImageService;
    }

    /**
     * @param item Set the item to process
     * @return a media object
     * @throws Exception if there is a problem
     */
    public final MediaObject process(final MediaObject item) throws Exception {
        if (item.getType() != null && item.getType().equals("Image")) {
            Image persistedImage = imageService.find(item.getSource()
                    .getHref());
            if (persistedImage != null) {
                item.setDebuglabel(persistedImage.getId() + "." + persistedImage.getFormat());
                persistedImage.setTitle(item.getRepresentation().getLabel());
                persistedImage.setDescription(item.getRepresentation()
                        .getDetail());
                Source source = getSource();
                boolean contains = false;
                for (Source s : persistedImage.getSources()) {
                    if (s.getIdentifier().equals(source.getIdentifier())) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    persistedImage.getSources().add(source);
                }
                imageFileProcessor.process(persistedImage);
                imageMetadataExtractor.process(persistedImage);
                Annotation annotation = createAnnotation(persistedImage,
                        RecordType.Image, AnnotationCode.Update,
                        AnnotationType.Info);
                persistedImage.getAnnotations().add(annotation);
                imageService.saveOrUpdate(persistedImage);
            } else {
                Image image = new Image();
                int dotIndex = item.getSource().getHref().lastIndexOf(".");
                String format = item.getSource().getHref().substring(dotIndex + 1);
                image.setFormat(ImageFormat.valueOf(format));
                image.setIdentifier(item.getSource().getHref());
                
                image.setTitle(item.getRepresentation().getLabel());
                image.setDescription(item.getRepresentation().getDetail());
                image.setAuthority(getSource());
                image.getSources().add(getSource());
                
                Annotation annotation = createAnnotation(image,
                        RecordType.Image, AnnotationCode.Create,
                        AnnotationType.Info);
                image.getAnnotations().add(annotation);
                imageService.saveOrUpdate(image);
                item.setDebuglabel(image.getId() + "." + format);
                image = imageFileProcessor.process(image);
                if(image != null) {
                   image = imageMetadataExtractor.process(image);
                   if(image != null) {
                       imageService.saveOrUpdate(image);                       
                   }
                }
            }
            return item;
        } else {
            // Skip URL's as we're not interested in them
            return null;
        }
    }

}
