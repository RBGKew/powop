package org.emonocot.job.key;

import org.emonocot.api.ImageService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.harvest.media.ImageFileProcessor;
import org.emonocot.harvest.media.ImageMetadataExtractor;
import org.emonocot.harvest.media.ImageThumbnailGenerator;
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

    private ImageService imageService;

    private ImageMetadataExtractor imageMetadataExtractor;

    private ImageFileProcessor imageFileProcessor;
    
    private ImageThumbnailGenerator imageThumbnailGenerator;


    @Autowired
    public void setImageMetadataExtractor(ImageMetadataExtractor newImageMetadataExtractor) {
        this.imageMetadataExtractor = newImageMetadataExtractor;
    }
    
    @Autowired
    public void setImageThumbnailGenerator(ImageThumbnailGenerator newImageThumbnailGenerator) {
        this.imageThumbnailGenerator = newImageThumbnailGenerator;
    }

    @Autowired
    public void setImageFileProcessor(ImageFileProcessor newImageFileProcessor) {
        this.imageFileProcessor = newImageFileProcessor;
    }

    @Autowired
    public void setImageService(ImageService newImageService) {
        this.imageService = newImageService;
    }

    public MediaObject process(MediaObject item) throws Exception {
        if (item.getType() != null && item.getType().equals("Image")) {
            Image persistedImage = imageService.find(item.getSource()
                    .getHref());
            if (persistedImage != null) {
                item.setDebuglabel(persistedImage.getId() + "." + persistedImage.getFormat());
                persistedImage.setTitle(item.getRepresentation().getLabel());
                persistedImage.setDescription(item.getRepresentation()
                        .getDetail());
                
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
                
                Annotation annotation = createAnnotation(image,
                        RecordType.Image, AnnotationCode.Create,
                        AnnotationType.Info);
                image.getAnnotations().add(annotation);
                imageService.saveOrUpdate(image);
                item.setDebuglabel(image.getId() + "." + format);
                image = imageFileProcessor.process(image);
                image = imageThumbnailGenerator.process(image);
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
