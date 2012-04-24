package org.emonocot.job.key;

import java.util.UUID;

import org.emonocot.api.ImageService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.harvest.media.ImageFileProcessor;
import org.emonocot.harvest.media.ImageMetadataExtractor;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.media.Image;
import org.emonocot.model.source.Source;
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
            Image persistedImage = imageService.findByUrl(item.getSource()
                    .getHref());
            if (persistedImage != null) {
                item.setDebuglabel(persistedImage.getIdentifier() + "." + persistedImage.getFormat());
                persistedImage.setCaption(item.getRepresentation().getLabel());
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
                image.setFormat(format);
                image.setIdentifier(UUID.randomUUID().toString());
                item.setDebuglabel(image.getIdentifier() + "." + format);
                image.setUrl(item.getSource().getHref());
                image.setCaption(item.getRepresentation().getLabel());
                image.setDescription(item.getRepresentation().getDetail());
                image.setAuthority(getSource());
                image.getSources().add(getSource());
                imageFileProcessor.process(image);
                imageMetadataExtractor.process(image);
                Annotation annotation = createAnnotation(image,
                        RecordType.Image, AnnotationCode.Create,
                        AnnotationType.Info);
                image.getAnnotations().add(annotation);
                imageService.saveOrUpdate(image);
            }
            return item;
        } else {
            // Skip URL's as we're not interested in them
            return null;
        }
    }

}
