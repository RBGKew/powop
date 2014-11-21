/**
 * 
 */
package org.emonocot.model.convert;

import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Multimedia;
import org.springframework.core.convert.converter.Converter;

/**
 * @author jk00kg
 *
 */
public class MultimediaToIdentificationKeyConverter implements Converter<Multimedia, IdentificationKey> {


    /* (non-Javadoc)
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public IdentificationKey convert(Multimedia source) {
        //TODO throw exception if it is not a key
        if(source instanceof IdentificationKey) {
            return (IdentificationKey) source;
        } else {
            IdentificationKey identificationKey = new IdentificationKey();
            identificationKey.setAccessRights(source.getAccessRights());
            identificationKey.setAudience(source.getAudience());
            identificationKey.setAuthority(source.getAuthority());
            identificationKey.setContributor(source.getContributor());
            identificationKey.setCreated(source.getCreated());
            identificationKey.setCreator(source.getCreator());
            identificationKey.setDescription(source.getDescription());
            identificationKey.setFormat(source.getFormat());
            identificationKey.setIdentifier(source.getIdentifier());
            identificationKey.setLicense(source.getLicense());
            identificationKey.setModified(source.getModified());
            identificationKey.setPublisher(source.getPublisher());
            identificationKey.setReferences(source.getReferences());
            identificationKey.setRights(source.getRights());
            identificationKey.setRightsHolder(source.getRightsHolder());
            identificationKey.setSource(source.getSource());
            identificationKey.setTaxa(source.getTaxa());
            identificationKey.setTitle(source.getTitle());
            return identificationKey;
        }
    }

}
