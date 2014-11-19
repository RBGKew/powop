/**
 * 
 */
package org.emonocot.model.convert;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.emonocot.model.Image;
import org.emonocot.model.Multimedia;
import org.springframework.core.convert.converter.Converter;

/**
 * @author jk00kg
 *
 */
public class MultimediaToImageConverter implements Converter<Multimedia, Image> {


    /* (non-Javadoc)
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public Image convert(Multimedia source) {
        //TODO throw exception if it is not an image type
        if(source instanceof Image) {
            return (Image) source;
        } else {
            Image image = new Image();
            //Shortcut for image.setAccessRights(source.getAccessRights()); etc. but does more than it should!!!
            //BeanUtils.copyProperties(image, source);
            image.setAccessRights(source.getAccessRights());
            image.setAudience(source.getAudience());
            image.setAuthority(source.getAuthority());
            image.setContributor(source.getContributor());
            image.setCreated(source.getCreated());
            image.setCreator(source.getCreator());
            image.setDescription(source.getDescription());
            image.setFormat(source.getFormat());
            image.setIdentifier(source.getIdentifier());
            image.setLicense(source.getLicense());
            image.setModified(source.getModified());
            image.setPublisher(source.getPublisher());
            image.setReferences(source.getReferences());
            image.setRights(source.getRights());
            image.setRightsHolder(source.getRightsHolder());
            image.setSource(source.getSource());
            image.setTaxa(source.getTaxa());
            image.setTitle(source.getTitle());
            return image;
        }
    }

}
