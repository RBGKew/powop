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
package org.emonocot.model.convert;

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
