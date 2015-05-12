/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.key;

import org.emonocot.api.ImageService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.model.Annotation;
import org.emonocot.model.Image;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.MediaFormat;
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
                
                Annotation annotation = createAnnotation(persistedImage,
                        RecordType.Image, AnnotationCode.Update,
                        AnnotationType.Info);
                persistedImage.getAnnotations().add(annotation);
                imageService.saveOrUpdate(persistedImage);
            } else {
                Image image = new Image();
                int dotIndex = item.getSource().getHref().lastIndexOf(".");
                String format = item.getSource().getHref().substring(dotIndex + 1);
                image.setFormat(MediaFormat.valueOf(format));
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
            }
            return item;
        } else {
            // Skip URL's as we're not interested in them
            return null;
        }
    }

}
